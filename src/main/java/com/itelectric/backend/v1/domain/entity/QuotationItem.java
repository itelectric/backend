package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_quotation_item")
public class QuotationItem extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fk_base_product", nullable = false)
    private BaseProduct baseProduct;

    @ManyToOne
    @JoinColumn(name = "fk_quotation_order_id", nullable = false)
    private QuotationOrder quotationOrder;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
