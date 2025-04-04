package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.entity.type.Role;
import bap.jp.mvcbap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
	model.addAttribute("user", new User());
	return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

	try {
	    userService.loadUserByUsername(user.getUserName());
	    model.addAttribute("error", "Username đã tồn tại!");
	    return "register";
	} catch (UsernameNotFoundException e) {
	}

	user.setPassword(passwordEncoder.encode(user.getPassword()));
	user.setRole(Role.USER);
	user.setDeleteFlg(false);
	userService.save(user);
	return "redirect:/login?registered";
    }
}
