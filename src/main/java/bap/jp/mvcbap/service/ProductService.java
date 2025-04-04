package bap.jp.mvcbap.service;

import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> findAll() {
	return productRepository.findAll();
    }

    public Product findById(Integer id) {
	return productRepository.findById(id).orElse(null);
    }

    public void save(Product product) {
	productRepository.save(product);
    }

    public void delete(Integer id) {
	productRepository.deleteById(id);
    }

}
