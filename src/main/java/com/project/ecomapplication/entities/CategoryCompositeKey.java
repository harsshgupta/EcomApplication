package com.project.ecomapplication.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class CategoryCompositeKey implements Serializable {

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_meta_data_field_id")
    private Long categoryMetaDataFieldId;
}