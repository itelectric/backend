package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_quotation_order")
public class QuotationOrder extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    private LocalDate deadlineAnswer;

    @OneToMany(mappedBy = "quotationOrder", fetch = FetchType.LAZY)
    private Set<QuotationItem> items;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
