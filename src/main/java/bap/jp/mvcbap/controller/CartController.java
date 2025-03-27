package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.ProductRepository;
import bap.jp.mvcbap.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String viewCart(Model model) {
	Map<Integer, Integer> cart = cartService.getCart();

	Map<Integer, Product> productMap = new HashMap<>();
	for (Integer productId : cart.keySet()) {
	    productRepository.findById(productId).ifPresent(product -> productMap.put(productId, product));
	}

	model.addAttribute("cart", cart);
	model.addAttribute("productMap", productMap);
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
