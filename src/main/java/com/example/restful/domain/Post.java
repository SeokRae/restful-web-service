package com.example.restful.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

}
