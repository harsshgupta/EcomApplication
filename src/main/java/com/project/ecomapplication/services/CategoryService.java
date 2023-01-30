package com.project.ecomapplication.services;

import org.springframework.http.ResponseEntity;
import com.project.ecomapplication.dto.AddCategoryMetaDataFieldValueDTO;
import com.project.ecomapplication.dto.UpdateCategoryMetaValueDto;

public interface CategoryService {

    ResponseEntity<?> addCategory(String categoryName, Long parentCategoryId);

    ResponseEntity<?> addMetadataField(String fieldName);

    ResponseEntity<?> addMetaDataFieldValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO, Long categoryId, Long metaDataFieldId);

    ResponseEntity<?> viewMetadataField();

    ResponseEntity<?> updateCategory(Long categoryId, String categoryName);

    ResponseEntity<?> updateCategoryMetadataFieldValues(UpdateCategoryMetaValueDto updateCategoryMetaValueDto, Long categoryId, Long metaDataFieldId);

    ResponseEntity<?> viewAllCategories();

    ResponseEntity<?> viewCategoriesByOptionalId(Long id);
}