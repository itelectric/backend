package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_quotation_orders")
public class Quotation extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quotation_order_seq")
    @SequenceGenerator(name = "quotation_order_seq", sequenceName = "seq_quotation_order", allocationSize = 1)
    private Integer id;

    private String description;

    private LocalDate deadlineAnswer;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "quotation", fetch = FetchType.LAZY)
    private Set<QuotationItem> items;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
