package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.repository.ProductRepository;
import bap.jp.mvcbap.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String viewCart(Model model) {
	model.addAttribute("cart", cartService.getCart());
	model.addAttribute("productMap", cartService.getCartProducts());
	return "cart.html";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Integer productId,
			    @RequestParam("quantity") Integer quantity) {
	cartService.addProduct(productId, quantity);
	return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") Integer productId) {
	cartService.removeProduct(productId);
	return "redirect:/cart";
    }
}
