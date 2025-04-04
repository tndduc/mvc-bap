package bap.jp.mvcbap.controller;

import bap.jp.mvcbap.entity.Category;
import bap.jp.mvcbap.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/category")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
	model.addAttribute("categories", categoryService.findAll());
	return "admin-category";
    }

    @PostMapping("/add")
    public String addCategory(@RequestParam("categoryName") String categoryName) {
	Category category = new Category();
	category.setCategoryName(categoryName);
	categoryService.save(category);
	return "redirect:/admin/category";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable("id") Integer id, Model model) {
	Category category = categoryService.findById(id);
	if (category == null) {
	    return "redirect:/admin/category";
	}
	model.addAttribute("category", category);
	return "edit-category";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id,
			       @RequestParam("categoryName") String categoryName) {
	Category category = categoryService.findById(id);
	if (category != null) {
	    category.setCategoryName(categoryName);
	    categoryService.save(category);
	}
	return "redirect:/admin/category";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Integer id) {
	categoryService.delete(id);
	return "redirect:/admin/category";
    }
}
