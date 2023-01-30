package com.project.ecomapplication.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Data;

@Data
@Entity
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private CategoryCompositeKey id = new CategoryCompositeKey();

    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("categoryId")
    private Category category;

    //@JsonBackReference
    @ManyToOne
    @MapsId("categoryMetaDataFieldId")
    private CategoryMetadataField categoryMetadataField;


}