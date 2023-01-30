package com.project.ecomapplication.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.project.ecomapplication.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM Category a WHERE a.name = ?1", nativeQuery = true)
    Category findByCategoryName(String categoryName);

    @Query(value = "SELECT * FROM Category a WHERE a.parent_category_id = ?1", nativeQuery = true)
    List<Category> findChildCategories(Long id);

    @Query(value = "SELECT * FROM Category a WHERE a.parent_category_id IS NULL", nativeQuery = true)
    List<Category> findCategoryByNull();

    @Query(value="select * from Category ",nativeQuery = true)
    List<Category> findAllCategory();


}