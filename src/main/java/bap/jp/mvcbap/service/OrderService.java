package bap.jp.mvcbap.service;


import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.entity.User;
import bap.jp.mvcbap.repository.OrderItemRepository;
import bap.jp.mvcbap.repository.OrderRepository;
import bap.jp.mvcbap.repository.ProductRepository;
import bap.jp.mvcbap.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Order> getOrdersByUser(Integer userId) {
	return orderRepository.findByUserid(userId);
    }

    public Order getOrderById(Integer orderId) {
	return orderRepository.findById(orderId).orElse(null);
    }

    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
	return orderItemRepository.findByOrderId(orderId);
    }

    public Map<Integer, Product> getProductsForOrder(List<OrderItem> orderItems) {
	Map<Integer, Product> productMap = new HashMap<>();
	for (OrderItem item : orderItems) {
	    productRepository.findById(item.getProduct().getId())
		    .ifPresent(product -> productMap.put(item.getId(), product));
	}
	return productMap;
    }


    public Map<String, BigDecimal> getMonthlyReport() {
	List<Order> orders = orderRepository.findOrdersForCurrentMonth();

	BigDecimal total = BigDecimal.ZERO;
	if (!orders.isEmpty()) {
	    for (Order order : orders) {
		total = total.add(order.getTotalAmount());
	    }
	}

	BigDecimal average = BigDecimal.ZERO;
	if (!orders.isEmpty()) {
	    average = total.divide(new BigDecimal(orders.size()), 2, RoundingMode.HALF_UP);
	}

	Map<String, BigDecimal> report = new HashMap<>();
	report.put("total", total);
	report.put("average", average);

	return report;
    }

    public List<Order> getAllOrders() {
	return orderRepository.findAll();
    }
    public List<Order> findOrdersByTotalAmountBetween(BigDecimal min, BigDecimal max) {
	return orderRepository.findOrdersByTotalAmountBetween(min, max);
    }

    public List<Order> findOrdersByTotalAmountGreaterThan(BigDecimal min) {
	return orderRepository.findOrdersByTotalAmountGreaterThan(min);
    }

    public List<Order> findOrdersByTotalAmountLessThan(BigDecimal max) {
	return orderRepository.findOrdersByTotalAmountLessThan(max);
    }
}