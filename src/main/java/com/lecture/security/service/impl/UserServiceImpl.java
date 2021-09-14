package com.lecture.security.service.impl;

import com.lecture.security.domain.entitiy.Account;
import com.lecture.security.repository.UserRepository;
import com.lecture.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);

    }
}
