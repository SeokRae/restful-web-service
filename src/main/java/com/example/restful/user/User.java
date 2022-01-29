package com.example.restful.user;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
public class User {

    private final Integer id;
    private final String name;
    private final Date joinDate;

    @Builder
    public User(Integer id, String name, Date joinDate) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
    }

    public boolean match(int id) {
        return this.getId() == id;
    }
}
