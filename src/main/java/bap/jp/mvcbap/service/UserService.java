package bap.jp.mvcbap.service;

import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
	return userRepository.findAll();
    }
}