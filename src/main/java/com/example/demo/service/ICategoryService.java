package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Category;

 
public interface ICategoryService {

	public Category saveCategory(Category category);
	public Category getCategoryById(Long id);
	public List<Category> getAllCategories();
	public int updateCategory(Category category);
	public Category getCategoryByCategoryName(String category_name);
}
