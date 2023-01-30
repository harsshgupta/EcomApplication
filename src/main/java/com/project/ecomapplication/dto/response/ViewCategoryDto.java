package com.project.ecomapplication.dto.response;

import com.project.ecomapplication.entities.Category;
import lombok.Data;
import java.util.List;

@Data
public class ViewCategoryDto {

    private Category currentCategory;
    private List<Category> childCategories;
}