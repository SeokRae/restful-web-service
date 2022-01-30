package com.example.restful.service;

import com.example.restful.exception.DuplicateUserIdException;
import com.example.restful.exception.NotFoundUserException;
import com.example.restful.user.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "user1", new Date(), "1234", "ssn1"));
        users.add(new User(2, "user2", new Date(),  "5678", "ssn2"));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (users.contains(user)) {
            throw new DuplicateUserIdException("이미 존재하는 Id 입니다.");
        }
        users.add(user);
        return user;
    }

    public User findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findAny()
                .orElseThrow(() -> new NotFoundUserException("존재하지 않는 사용자 입니다."));
    }

    public void deleteById(int id) {
        if(users.stream().noneMatch(user -> user.getId() == id)) {
            throw new NotFoundUserException("존재하지 않는 사용자입니다.");
        }
        users.removeIf(user -> user.getId() == id);
    }
}
