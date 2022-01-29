package com.example.restful.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
public class User {

    private final Integer id;
    @Size(min = 2, message = "name의 길이는 2자 이상 입력해주세요.")
    private final String name;
    @Past
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
