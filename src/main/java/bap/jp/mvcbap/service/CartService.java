package bap.jp.mvcbap.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartService {
    private Map<Integer, Integer> cart = new HashMap<>(); // key: productId, value: quantity

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
}
