package com.example.restful.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@JsonFilter("UserInfo")
public class User {

    private final Integer id;
    @Size(min = 2, message = "name의 길이는 2자 이상 입력해주세요.")
    private final String name;
    @Past
    private final Date joinDate;
    private final String password;
    private final String ssn;

    @Builder
    public User(Integer id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name = name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }

    public boolean match(int id) {
        return this.getId() == id;
    }
}
