package com.project.ecomapplication.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.project.ecomapplication.entities.CategoryMetadataFieldValues;

public interface CategoryMetadataFieldValuesRepository extends
        JpaRepository<CategoryMetadataFieldValues, Long> {

    @Query(value = "SELECT * FROM category_meta_data_field_value a WHERE a.category_meta_data_field_id=?1", nativeQuery = true)
    Optional<CategoryMetadataFieldValues> findByCategoryMetadataFieldId(Long id);
}
