package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_quotation_items")
public class QuotationItem extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quotation_item_seq")
    @SequenceGenerator(name = "quotation_item_seq", sequenceName = "seq_quotation_item", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "fk_base_product", nullable = false)
    private BaseProduct baseProduct;

    @ManyToOne
    @JoinColumn(name = "fk_quotation_order_id", nullable = false)
    private Quotation quotation;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
