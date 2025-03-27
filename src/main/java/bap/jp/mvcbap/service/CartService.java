package bap.jp.mvcbap.service;

import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartService {
    private final ProductRepository productRepository;
    private Map<Integer, Integer> cart = new HashMap<>();

    public CartService(ProductRepository productRepository) {
	this.productRepository = productRepository;
    }

    public Map<Integer, Integer> getCart() {
	return cart;
    }

    public void addProduct(Integer productId, Integer quantity) {
	cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
    }

    public void removeProduct(Integer productId) {
	cart.remove(productId);
    }

    public void clearCart() {
	cart.clear();
    }

    public Map<Integer, Product> getCartProducts() {
	Map<Integer, Product> productMap = new HashMap<>();
	for (Integer productId : cart.keySet()) {
	    productRepository.findById(productId).ifPresent(product -> productMap.put(productId, product));
	}
	return productMap;
    }
}
