package bap.jp.mvcbap.service;

import bap.jp.mvcbap.entity.CustomUserDetails;
import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
	return userRepository.findAll();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return userRepository.findByUserName(username)
		.map(CustomUserDetails::new)
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public User save(User user) {
	return userRepository.save(user);
    }
}