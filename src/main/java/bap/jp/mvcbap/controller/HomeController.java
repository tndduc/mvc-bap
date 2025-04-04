package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home() {
	return "home";
    }

    @GetMapping("/login")
    public String login() {
	return "login";
    }

    @GetMapping("/user/dashboard")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userDashboard() {
	return "user-dashboard";
    }
}
