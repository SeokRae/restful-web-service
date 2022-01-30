package com.example.restful.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("UserInfoV2")
public class UserV2 extends User {

    private String grade;
}