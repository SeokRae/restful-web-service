package com.example.restful.controller;

import com.example.restful.exception.NotFoundUserException;
import com.example.restful.service.UserDaoService;
import com.example.restful.user.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// 의존성 주입은 생성자, setter를 통한 주입이 가능하다.
@RestController
public class UserController {

    private final UserDaoService userDaoService;

    public UserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // curl -i -X GET http://localhost:8888/users \
    //      -H 'Accept: application/json'
    // curl -i -X GET http://localhost:8888/users \
    //      -H 'Accept: application/xml'
    // curl -i -X GET http://localhost:8888/users
    // GET /users
    @GetMapping(path = "/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getAllUsers() {
        /*
         * 1. 전체 데이터 조회
         * 2. 조회된 데이터를 EntityModel로 Wrapping 후 linkTo
         */
        List<User> users = userDaoService.findAll();
        List<EntityModel<User>> collect = users.stream()
                .map(user -> {
                    EntityModel<User> entityModel = EntityModel.of(user);
                    entityModel.add(linkTo(methodOn(this.getClass()).getAllUsers()).withSelfRel());
                    return entityModel;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(collect, linkTo(methodOn(this.getClass()).getAllUsers()).withSelfRel()));
    }

    // curl -i -X GET http://localhost:8888/users/1
    // GET /users/1
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable int id) {
        User findUser = userDaoService.findById(id);
        if (findUser == null) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }

        EntityModel<User> entityModel = EntityModel.of(findUser);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    // curl -i -X POST http://localhost:8888/users \
    //   -H 'Content-Type: application/json' \
    //   -d '{"id":"3","name":"user3","joinDate":"2022-01-29T10:33:03.979+00:00"}'
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User saveUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // curl -i -X DELETE http://localhost:8888/users/1
    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userDaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}