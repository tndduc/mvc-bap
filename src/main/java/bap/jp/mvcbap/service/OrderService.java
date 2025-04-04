package bap.jp.mvcbap.service;


import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.OrderItemRepository;
import bap.jp.mvcbap.repository.OrderRepository;
import bap.jp.mvcbap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	return orderRepository.findByUser_id(userId);
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

    public List<Order> getAllOrders() {
	return orderRepository.findAll();
    }
}