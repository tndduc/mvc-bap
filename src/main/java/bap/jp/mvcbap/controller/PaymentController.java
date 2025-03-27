package bap.jp.mvcbap.controller;
import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.service.OrderService;
import bap.jp.mvcbap.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout")
    public String checkout(@RequestParam("userId") Integer userId, Model model) {
	Order order = paymentService.checkout(userId);
	model.addAttribute("order", order);
	return "order-confirmation.html";
    }
}
