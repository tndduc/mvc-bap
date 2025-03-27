package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.OrderItemRepository;
import bap.jp.mvcbap.repository.OrderRepository;
import bap.jp.mvcbap.repository.ProductRepository;
import bap.jp.mvcbap.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/user/{userId}")
    public String listOrdersByUser(@PathVariable("userId") Integer userId, Model model) {
	List<Order> orders = orderService.getOrdersByUser(userId);
	model.addAttribute("orders", orders);
	return "order-list.html";
    }
    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId, Model model) {
	Order order = orderRepository.findById(orderId).orElse(null);

	if (order == null) {
	    return "redirect:/orders";
	}

	List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

	Map<Integer, Product> productMap = new HashMap<>();
	for (OrderItem item : orderItems) {
	    Product product = productRepository.findById(item.getProduct().getId()).orElse(null);
	    if (product != null) {
		productMap.put(item.getId(), product);
	    }
	}

	model.addAttribute("order", order);
	model.addAttribute("orderItems", orderItems);
	model.addAttribute("productMap", productMap);

	return "order-detail.html";
    }

    @GetMapping
    public String listOrders(Model model) {
	List<Order> orders = orderService.getAllOrders();
	model.addAttribute("orders", orders);
	return "orders";
    }
}
