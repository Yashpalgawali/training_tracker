package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Category;


@Repository("categoryrepo")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Optional<Category> findByCategory(String category);
	
	@Query("SELECT c FROM Category c WHERE c.category=:name")
//	@Query(value="select * from tbl_emp_category where category=?",nativeQuery = true)	
	public Category getCategoryByName(String name);
}
