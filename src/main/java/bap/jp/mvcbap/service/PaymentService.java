package bap.jp.mvcbap.service;


import bap.jp.mvcbap.entity.Order;
import bap.jp.mvcbap.entity.OrderItem;
import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.OrderItemRepository;
import bap.jp.mvcbap.repository.OrderRepository;
import bap.jp.mvcbap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductRepository productRepository;
    public Order checkout(Integer userId) {
	Order order = new Order();
	order.setUserid(userId);
	order.setOrderDate(Instant.now());
	order.setTotalAmount(BigDecimal.ZERO);
	order.setDeleteFlg(false);
	order = orderRepository.save(order);

	BigDecimal total = BigDecimal.ZERO;

	for (Map.Entry<Integer, Integer> entry : cartService.getCart().entrySet()) {
	    Integer productId = entry.getKey();
	    Integer quantity = entry.getValue();

	    Product product = productRepository.findById(productId).orElse(null);
	    if (product != null) {
		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setProduct(product);
		item.setPrice(product.getPrice());
		item.setQuantity(quantity);
		item.setDeleteFlg(false);
		orderItemRepository.save(item);

		total = total.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
	    }
	}

	order.setTotalAmount(total);
	order = orderRepository.save(order);

	cartService.clearCart();
	return order;
    }

}
