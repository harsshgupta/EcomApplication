package com.project.ecomapplication.entities;

import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "seller")
@PrimaryKeyJoinColumn(name = "user_id")
@AllArgsConstructor
public class Seller extends User {

   //private Integer userId foreign key
    private String gstNumber;
    private String companyContact;
    private String companyName;



}
