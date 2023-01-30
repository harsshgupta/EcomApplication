package com.project.ecomapplication.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.project.ecomapplication.exceptions.CategoryNotFoundException;
import com.project.ecomapplication.dto.AddCategoryMetaDataFieldValueDTO;
import com.project.ecomapplication.dto.CategoryMetaDataFieldViewDto;
import com.project.ecomapplication.dto.UpdateCategoryMetaValueDto;
import com.project.ecomapplication.entities.Category;
import com.project.ecomapplication.entities.CategoryMetadataField;
import com.project.ecomapplication.entities.CategoryMetadataFieldValues;
import com.project.ecomapplication.repository.CategoryMetadataFieldRepository;
import com.project.ecomapplication.repository.CategoryMetadataFieldValuesRepository;
import com.project.ecomapplication.repository.CategoryRepository;
import com.project.ecomapplication.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetaDataFieldValuesRepository;

    @Autowired
    CategoryMetadataFieldRepository categoryMetaDataFieldRepository;

    @Override
    public ResponseEntity<?> addCategory(String categoryName, Long parentCategoryId) {

        if (parentCategoryId != null) {
            if (categoryRepository.existsById(parentCategoryId)) {
                Category parent = categoryRepository.getById(parentCategoryId);
                if (parent.getName().equals(categoryName)) {
                    return new ResponseEntity<>("You cannot create a sub-category of a Parent category",
                            HttpStatus.BAD_REQUEST);
                } else {
                    Category category2 = categoryRepository.findByCategoryName(categoryName);
                    if (category2 != null) {
                        return new ResponseEntity<>("You cannot create duplicate categories!",
                                HttpStatus.BAD_REQUEST);
                    } else {
                        Category category = new Category();
                        category.setName(categoryName);
                        category.setParentCategory(parent);
                        categoryRepository.save(category);
                        return new ResponseEntity<>(
                                String.format("Sub-category created under category: " + parent.getName()),
                                HttpStatus.CREATED);
                    }
                }
            } else {
                return new ResponseEntity<>(
                        String.format("No category exists with this id: " + parentCategoryId),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            Category category2 = categoryRepository.findByCategoryName(categoryName);
            if (category2 != null) {
                return new ResponseEntity<>("You cannot create duplicate categories!",
                        HttpStatus.BAD_REQUEST);
            } else {
                Category category = new Category();
                category.setName(categoryName);
                category = categoryRepository.save(category);
                return new ResponseEntity<>(
                        String.format("Parent category created with ID: " + category.getId()),
                        HttpStatus.CREATED);
            }
        }
    }

    @Override
    public ResponseEntity<?> addMetadataField(String fieldName) {
        CategoryMetadataField categoryMetadataField = categoryMetaDataFieldRepository.findByCategoryMetadataFieldName(
                fieldName);
        if (categoryMetadataField != null) {
            return new ResponseEntity<>("You cannot create duplicate category metadata field!",
                    HttpStatus.BAD_REQUEST);

        } else {

            CategoryMetadataField field = new CategoryMetadataField();
            field.setName(fieldName);
            field = categoryMetaDataFieldRepository.save(field);
            return new ResponseEntity<>(
                    String.format("Category metadata field created with ID: " + field.getId()),
                    HttpStatus.CREATED);
        }
    }
    /*public ResponseEntity<?> viewMetadataField() {
      List<CategoryMetaDataField> list = categoryMetaDataFieldRepository.findAll();
      return new ResponseEntity<>(list, HttpStatus.OK);
    }

     */
    //to get all metadata field
    @Override
    public ResponseEntity<?> viewMetadataField() {
        List<CategoryMetadataField> list = categoryMetaDataFieldRepository.findAll();
        List<CategoryMetaDataFieldViewDto> responseFieldList = new ArrayList<>();
        list.forEach(field -> {
            CategoryMetaDataFieldViewDto categoryMetaDataFieldDto = new CategoryMetaDataFieldViewDto();
            categoryMetaDataFieldDto.setName(field.getName());
            categoryMetaDataFieldDto.setId(field.getId());
            responseFieldList.add(categoryMetaDataFieldDto);
        });
        return new ResponseEntity<>(responseFieldList, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<?> updateCategory(Long categoryId, String categoryName) {
        if (categoryRepository.existsById(categoryId)) {
            Category category = categoryRepository.getReferenceById(categoryId);
            Category categoryDuplicate = categoryRepository.findByCategoryName(categoryName);
            if (categoryDuplicate == null) {
                category.setName(categoryName);
                categoryRepository.save(category);
                return new ResponseEntity<>("Saved category with updated name: " + category.getName(),
                        HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("You cannot create duplicate categories!",
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No Category exists with name: " + categoryName,
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> addMetaDataFieldValue(
            AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO, Long categoryId,
            Long metaDataFieldId) {

        System.out.println(categoryId + " " + metaDataFieldId);
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new CategoryNotFoundException("Category Not Found"));

        CategoryMetadataField categoryMetaDataField = categoryMetaDataFieldRepository.findById(
                        metaDataFieldId).
                orElseThrow(() -> new CategoryNotFoundException("Category MetaData Field Not Found"));
        System.out.println(category.getName() + " " + categoryMetaDataField.getName());
        CategoryMetadataFieldValues categoryMetadataFieldValue = new CategoryMetadataFieldValues();
        String values = String.join(",", addCategoryMetaDataFieldValueDTO.getMetaDataFieldValues());
        System.out.println(values);
        categoryMetadataFieldValue.setCategory(category);
        categoryMetadataFieldValue.setCategoryMetadataField(categoryMetaDataField);
        categoryMetadataFieldValue.setValue(values);
        categoryMetaDataFieldValuesRepository.save(categoryMetadataFieldValue);
        return new ResponseEntity<>("Category MetaDataValue Created", HttpStatus.CREATED);

    }
    @Override
    public ResponseEntity<?> updateCategoryMetadataFieldValues(UpdateCategoryMetaValueDto updateCategoryMetaValueDto,Long categoryId, Long metadataFieldId ) {
        Optional<Category> category= categoryRepository.findById(categoryId);
        Optional<CategoryMetadataField> categoryMetaDataField= categoryMetaDataFieldRepository.findById(metadataFieldId);
        if (!category.isPresent()) {
            throw new CategoryNotFoundException("Category does not exists");
        }else if (!categoryMetaDataField.isPresent()) {
            throw new CategoryNotFoundException("Metadata field does not exists");
        }else{
            Category category1=category.get();
            CategoryMetadataField categoryMetaDataField1 = categoryMetaDataField.get();
            CategoryMetadataFieldValues categoryMetadataFieldValues = categoryMetaDataFieldValuesRepository.findByCategoryMetadataFieldId(metadataFieldId).orElseThrow(()->new CategoryNotFoundException(
                    +categoryId + "And MetaDataField Value ID -> " + metadataFieldId));
            String values = updateCategoryMetaValueDto.getValues().stream().collect(Collectors.joining(","));
            categoryMetadataFieldValues.setValue(values);
            categoryMetaDataFieldValuesRepository.save(categoryMetadataFieldValues);
            return new ResponseEntity<>("Updated the passed values to category metadata field: "+ categoryMetaDataField1.getName(), HttpStatus.CREATED);
        }
    }
    @Override
    public ResponseEntity<?> viewAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> viewCategoriesByOptionalId(Long id) {
        if (id != null) {
            if (categoryRepository.existsById(id)) {
                List<Category> categories = categoryRepository.findChildCategories(id);

                return new ResponseEntity<>(categories, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Category exists with this id: "+id, HttpStatus.NOT_FOUND);
            }
        } else {
            List<Category> categories = categoryRepository.findCategoryByNull();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
    }


}
