package com.example.restful.controller;

import com.example.restful.exception.NotFoundUserException;
import com.example.restful.repository.UserRepository;
import com.example.restful.user.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/jpa")
@RequiredArgsConstructor
public class UserJpaController {

    public final UserRepository userRepository;

    // curl -i -X GET http://localhost:8888/jpa/users
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // curl -i -X GET http://localhost:8888/jpa/users/1001
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isEmpty()) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }

        EntityModel<User> entityModel = EntityModel.of(optionalUser.get());
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllUsers());
        entityModel.add(linkTo.withRel("all-users"));

        return ResponseEntity.ok(entityModel);
    }

    // curl -i -X DELETE http://localhost:8888/jpa/users/1001
    @DeleteMapping(path = "/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    // curl -i -X POST http://localhost:8888/jpa/users \
    // -H 'Content-Type: application/json' \
    // -d '{"name":"user3","joinDate":"2022-01-29T10:33:03.979+00:00","password":"1234","ssn":"123456-123456"}'
    @PostMapping(path = "/users")
    public ResponseEntity<User> postUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}