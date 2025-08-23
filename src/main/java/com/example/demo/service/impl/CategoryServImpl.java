package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Category;
import com.example.demo.exception.GlobalException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceNotModifiedException;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.ICategoryService;

@Service("categoryserv")
public class CategoryServImpl implements ICategoryService {

	private final CategoryRepository categoryrepo;

	public CategoryServImpl(CategoryRepository categoryrepo) {
		super();
		this.categoryrepo = categoryrepo;
	}

	@Override
	public Category saveCategory(Category category) {
		var savedCategory = categoryrepo.save(category);
		if(savedCategory!=null ) {
			return savedCategory;
		}
		else {
			throw new GlobalException("Category "+category.getCategory()+" is not saved");
		}
		
	}

	@Override
	public Category getCategoryById(Long id) {
	 
		return categoryrepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("No Category found for given ID "+id));
	}

	@Override
	public List<Category> getAllCategories() {
		List<Category> categoryList = categoryrepo.findAll();
		if(!categoryList.isEmpty()) {
			return categoryList;
		}
		else {
			throw new ResourceNotFoundException("No Categories found");
		}
	}

	@Override
	public int updateCategory(Category category) {
		var result = categoryrepo.save(category);
		if(result!=null) {
			return 1;
		}
		else {
			throw new ResourceNotModifiedException("Category "+category.getCategory()+" is not updated");
		}		
	}

	@Override
	public Category getCategoryByCategoryName(String category_name) {
	 
		return categoryrepo.getCategoryByName(category_name);
	}

}
