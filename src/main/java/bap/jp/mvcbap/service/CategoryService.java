package bap.jp.mvcbap.service;

import bap.jp.mvcbap.entity.Category;
import bap.jp.mvcbap.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
	return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
	return categoryRepository.findById(id).orElse(null);
    }

    public void save(Category category) {
	categoryRepository.save(category);
    }

    public void delete(Integer id) {
	categoryRepository.deleteById(id);
    }
}
