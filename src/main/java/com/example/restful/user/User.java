package com.example.restful.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonFilter("UserInfo")
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    @ApiModelProperty(notes = "사용자 이름을 입력해 주세요.")
    @Size(min = 2, message = "name의 길이는 2자 이상 입력해주세요.")
    private String name;
    @ApiModelProperty(notes = "사용자의 등록일을 입력해 주세요.")
    @Past
    private Date joinDate;
    @ApiModelProperty(notes = "사용자의 패스워드를 입력해 주세요.")
    private String password;
    @ApiModelProperty(notes = "사용자의 주민번호를 입력해 주세요.")
    private String ssn;
}
