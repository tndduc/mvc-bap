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
	// Lưu Order trước để có order_id nếu cần
	order = orderRepository.save(order);

	BigDecimal total = BigDecimal.ZERO;
	// Giả sử cart lưu dưới dạng Map<productId, quantity>
	for(Map.Entry<Integer, Integer> entry : cartService.getCart().entrySet()){
	    Integer productId = entry.getKey();
	    Integer quantity = entry.getValue();

	    Product product = productRepository.findById(productId).orElse(null);
	    if(product != null) {
		OrderItem item = new OrderItem();
		item.setPrice(product.getPrice());
		item.setQuantity(quantity);
		item.setDeleteFlg(false);
		// Nếu có quan hệ giữa Order và OrderItem, gán order cho item
		// item.setOrder(order);
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
