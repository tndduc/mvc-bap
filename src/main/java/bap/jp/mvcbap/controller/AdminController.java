package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.Product;
import bap.jp.mvcbap.service.OrderService;
import bap.jp.mvcbap.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
	List<Object[]> monthlyRevenue = orderService.getMonthlyRevenueForYear();
	model.addAttribute("monthlyRevenue", monthlyRevenue);
	model.addAttribute("products", productService.findAll());
	return "admin-dashboard";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam("productName") String productName,
			     @RequestParam("price") BigDecimal price) {
	Product product = new Product();
	product.setProductName(productName);
	product.setPrice(price);
	productService.save(product);
	return "redirect:/admin/dashboard";
    }
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable("id") Integer id, Model model) {
	Product product = productService.findById(id);
	if (product == null) {
	    return "redirect:/admin/dashboard"; // Nếu không tìm thấy sản phẩm, quay lại danh sách
	}
	model.addAttribute("product", product);
	return "edit-product";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,
			      @RequestParam("productName") String productName,
			      @RequestParam("price") BigDecimal price) {
	Product product = productService.findById(id);
	if (product != null) {
	    product.setProductName(productName);
	    product.setPrice(price);
	    productService.save(product);
	}
	return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
	productService.delete(id);
	return "redirect:/admin/dashboard";
    }
}
