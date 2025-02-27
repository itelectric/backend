package com.itelectric.backend.v1.domain.entity;

import com.itelectric.backend.v1.domain.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_base_products")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseProduct extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_product_seq")
    @SequenceGenerator(name = "base_product_seq", sequenceName = "seq_base_product", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean available;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}