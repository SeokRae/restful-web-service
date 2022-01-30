package com.example.restful.controller;

import com.example.restful.domain.Post;
import com.example.restful.domain.User;
import com.example.restful.exception.NotFoundUserException;
import com.example.restful.repository.PostRepository;
import com.example.restful.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // curl -i -X GET http://localhost:8888/jpa/users
    @GetMapping(path = "/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // curl -i -X GET http://localhost:8888/jpa/users/1001
    @GetMapping(path = "/users/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable int id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
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

    // curl -i -X GET http://localhost:8888/jpa/users/1001/posts
    @GetMapping(path = "/users/{id}/posts")
    public List<Post> getAllPostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }

        return user.get().getPosts();
    }

    // curl -i -X POST http://localhost:8888/jpa/users/1001/posts \
    // -H 'Content-Type: application/json'
    // -d '{"description": "posting!!!"}'
    @PostMapping(path = "/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }
        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}