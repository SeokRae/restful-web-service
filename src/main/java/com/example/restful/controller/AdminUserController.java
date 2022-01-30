package com.example.restful.controller;

import com.example.restful.exception.NotFoundUserException;
import com.example.restful.service.UserDaoService;
import com.example.restful.user.User;
import com.example.restful.user.UserV2;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.BeanUtils;
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
    @GetMapping(path = "/v1/users")
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

    // curl -i -X GET http://localhost:8888/admin/v1/users/1
    // GET /users/1
    // @GetMapping(path = "/v1/users/{id}", params = "version=1")
    // curl -i -X GET http://localhost:8888/admin/users/1/?version=1
    // @GetMapping(path = "/users/{id}", params = "version=1")
    // curl -i -X GET http://localhost:8888/admin/users/1 \
    //      -H 'X-API-VERSION: 1'
    // @GetMapping(path = "/users/{id}", headers = "X-API-VERSION=1")
    // MIME 타입을 이용하는 방법
    // curl -i -X GET http://localhost:8888/admin/users/1/ \
    // -H 'Accept: application/vnd.company.appv1+json'
    @GetMapping(path = "/users/{id}", produces = "application/vnd.company.appv1+json")
    public MappingJacksonValue getUser(@PathVariable Integer id) {
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

    // GET /users/1
    // curl -i -X GET http://localhost:8888/admin/v2/users/1
    // @GetMapping(path = "/v2/users/{id}")
    // curl -i -X GET http://localhost:8888/admin/users/1/?version=2
    // @GetMapping(path = "/users/{id}", params = "version=2")
    // curl -i -X GET http://localhost:8888/admin/users/1/ \
    // -H 'X-API-VERSION: 2'
    // @GetMapping(path = "/users/{id}", headers = "X-API-VERSION=2")
    // MIME 타입을 이용하는 방법
    // curl -i -X GET http://localhost:8888/admin/users/1/ \
    // -H 'Accept: application/vnd.company.appv2+json'
    @GetMapping(path = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue getUserV2(@PathVariable Integer id) {
        User findUser = userDaoService.findById(id);
        if (findUser == null) {
            throw new NotFoundUserException(String.format("ID[%s] not found", id));
        }

        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(findUser, userV2);
        userV2.setGrade("VIP");

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "password", "ssn", "grade");

        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("UserInfoV2", filter);

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(userV2);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }
}
