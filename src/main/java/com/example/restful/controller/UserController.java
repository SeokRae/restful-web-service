package com.example.restful.controller;

import com.example.restful.exception.NotFoundUserException;
import com.example.restful.service.UserDaoService;
import com.example.restful.user.User;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// 의존성 주입은 생성자, setter를 통한 주입이 가능하다.
@RestController
public class UserController {
    private final UserDaoService userDaoService;

    public UserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // curl -i -X GET http://localhost:8888/users
    // GET /users
    @GetMapping(path = "/users")
    public List<User> getAllUsers() {
        return userDaoService.findAll();
    }

    // curl -i -X GET http://localhost:8888/users/1
    // GET /users/1
    @GetMapping(path = "/users/{id}")
    public User getUser(@PathVariable int id) {
        User findUser = userDaoService.findById(id);
        if(findUser == null) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }
        return findUser;
    }

    // curl -i -X POST http://localhost:8888/users \
    //   -H 'Content-Type: application/json' \
    //   -d '{"id":"3","name":"user3","joinDate":"2022-01-29T10:33:03.979+00:00"}'
    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saveUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userDaoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}