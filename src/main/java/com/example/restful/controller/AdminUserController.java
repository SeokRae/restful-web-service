package com.example.restful.controller;

import com.example.restful.exception.NotFoundUserException;
import com.example.restful.service.UserDaoService;
import com.example.restful.user.User;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminUserController {

    private final UserDaoService userDaoService;

    public AdminUserController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // curl -i -X GET http://localhost:8888/admin/users \
    //      -H 'Accept: application/json'
    // curl -i -X GET http://localhost:8888/admin/users \
    //      -H 'Accept: application/xml'
    // curl -i -X GET http://localhost:8888/admin/users
    // GET /users
    @GetMapping(path = "/users")
    public MappingJacksonValue getAllUsers() {
        List<User> all = userDaoService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(all);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    // curl -i -X GET http://localhost:8888/admin/users/1
    // GET /users/1
    @GetMapping(path = "/users/{id}")
    public MappingJacksonValue getUser(@PathVariable int id) {
        User findUser = userDaoService.findById(id);
        if (findUser == null) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password", "ssn");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfo", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(findUser);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }
}
