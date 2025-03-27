package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.repository.UserRepository;
import bap.jp.mvcbap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/users", method= RequestMethod.GET)
    public String listUsers(Model model) {
	model.addAttribute("users", userService.getAllUsers());
	return "users";
    }
}