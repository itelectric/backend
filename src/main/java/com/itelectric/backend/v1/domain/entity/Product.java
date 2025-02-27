package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_products")
@PrimaryKeyJoinColumn(name = "id")
public class Product extends BaseProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer stockQuantity;
}