package com.project.ecomapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecomapplication.dto.AddCategoryMetaDataFieldValueDTO;
import com.project.ecomapplication.dto.UpdateCategoryMetaValueDto;
import com.project.ecomapplication.services.CategoryService;
import com.project.ecomapplication.services.CategoryServiceImpl;


@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @PostMapping("/add-category")//child and parent key category
    public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName,
                                         @RequestParam(required = false, value = "parentId") Long parentCategoryId) {
        return categoryService.addCategory(categoryName, parentCategoryId);
    }
    @PostMapping("/add-category/Metadata-field")
    public ResponseEntity<?> addCategoryMetadataField(@RequestParam("fieldName") String fieldName)
    {
        return categoryService.addMetadataField(fieldName);
    }

    @PostMapping("add/metadata/values/{categoryId}/{metaDataFieldId}")
    public ResponseEntity<?> addMetaFieldValues(@RequestBody AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO,@PathVariable Long categoryId,@PathVariable Long metaDataFieldId){

        return categoryService.addMetaDataFieldValue(addCategoryMetaDataFieldValueDTO,categoryId,metaDataFieldId);
    }

    @GetMapping("/view/metadatafield")
    public ResponseEntity<?> viewMetadataField() {

        return categoryService.viewMetadataField();
    }

    @PutMapping("/update/category")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long categoryId,
                                            @RequestParam("categoryName") String categoryName) {
        return categoryService.updateCategory(categoryId, categoryName);
    }

    @PutMapping("/update/category-metadata-field-values/{categoryId}/{metaDataFieldId}")
    public ResponseEntity<?> updateCategoryMetadataFieldValues(@RequestBody UpdateCategoryMetaValueDto updateCategoryMetaValueDto,@PathVariable Long categoryId,@PathVariable Long metaDataFieldId)
    {
        return categoryService.updateCategoryMetadataFieldValues(updateCategoryMetaValueDto,categoryId,metaDataFieldId);
    }
    /* @GetMapping("/view-category/{id}")
     public CategoryMetaDataFieldViewDto viewCategory(@PathVariable Long id) {
       return categoryService.viewCategory(id);
     }

     */
    @GetMapping("/all/categories")
    public ResponseEntity<?> allCategories() {

        return categoryService.viewAllCategories();
    }
    @GetMapping("/list-all-categories/{id}")
    public ResponseEntity<?> listCategories(@PathVariable Long id) {
        return categoryService.viewCategoriesByOptionalId(id);
    }



}