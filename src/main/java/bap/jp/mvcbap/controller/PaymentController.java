package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.repository.UserRepository;
import bap.jp.mvcbap.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/checkout")
    public String checkout(Model model) {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	String username = authentication.getName();

	User user = userRepository.findByUserName(username)
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));

	Order order = paymentService.checkout(user.getId());
	model.addAttribute("order", order);
	return "order-confirmation.html";
    }
}
