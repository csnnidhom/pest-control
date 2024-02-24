package com.holding.pestcontrol.service;

import com.holding.pestcontrol.entity.User;
import com.holding.pestcontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
