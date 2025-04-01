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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}")
    public String listOrdersByUser(@PathVariable("userId") Integer userId, Model model) {
	model.addAttribute("orders", orderService.getOrdersByUser(userId));
	return "order-list.html";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId, Model model) {
	Order order = orderService.getOrderById(orderId);
	if (order == null) {
	    return "redirect:/orders";
	}

	List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderId);
	model.addAttribute("order", order);
	model.addAttribute("orderItems", orderItems);
	model.addAttribute("productMap", orderService.getProductsForOrder(orderItems));

	return "order-detail.html";
    }

    @GetMapping
    public String listOrders(Model model) {
	model.addAttribute("orders", orderService.getAllOrders());
	return "orders";
    }
    @GetMapping("/search")
    public String searchOrders(
	    @RequestParam(name = "minAmount", required = false) BigDecimal minAmount,
	    @RequestParam(name = "maxAmount", required = false) BigDecimal maxAmount,
	    Model model) {

	List<Order> orders;

	if (minAmount != null && maxAmount != null) {
	    orders = orderService.findOrdersByTotalAmountBetween(minAmount, maxAmount);
	} else if (minAmount != null) {
	    orders = orderService.findOrdersByTotalAmountGreaterThan(minAmount);
	} else if (maxAmount != null) {
	    orders = orderService.findOrdersByTotalAmountLessThan(maxAmount);
	} else {
	    orders = orderService.getAllOrders();
	}

	model.addAttribute("orders", orders);
	return "orders";
    }
    @GetMapping("/monthly-report")
    public String getMonthlyReport(Model model) {
	Map<String, BigDecimal> report = orderService.getMonthlyReport();
	model.addAttribute("total", report.get("total"));
	model.addAttribute("average", report.get("average"));
	return "monthly-report.html";
    }

}