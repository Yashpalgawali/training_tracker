package com.example.demo.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Category;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ICategoryService;

import lombok.RequiredArgsConstructor;

@Service("categoryserv")
@RequiredArgsConstructor
public class CategoryServImpl implements ICategoryService {

	private final CategoryRepository categoryrepo;

	@Override
	@CacheEvict(value="categories", allEntries = true )
	public Category saveCategory(Category category) {
		var savedCategory = categoryrepo.save(category);
		if (savedCategory != null) {
			return savedCategory;
		} else {
			throw new GlobalException("Category " + category.getCategory() + " is not saved");
		}
	}

	@Override
	@Cacheable(value = "categories", key = "#id")	
	public Category getCategoryById(Long id) {
		System.err.println("FETCHing From DB.....");
		return categoryrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No Category found for given ID " + id));
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> categoryList = categoryrepo.findAll();
		if (!categoryList.isEmpty()) {
			return categoryList;
		} else {
			throw new ResourceNotFoundException("No Categories found");
		}
	}

	@Override
	@Transactional
	@CachePut(value = "categories", key = "#category.category_id")
	public Category updateCategory(Category category) {
		var result = categoryrepo.save(category);
		if (result != null) {
			return result;
		} else {
			throw new ResourceNotModifiedException("Category " + category.getCategory() + " is not updated");
		}
	}
   
	@Override
	public Category getCategoryByCategoryName(String category_name) {

		return categoryrepo.getCategoryByName(category_name);
	}

}
