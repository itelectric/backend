package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_services")
@PrimaryKeyJoinColumn(name = "id")
public class ServiceManager extends BaseProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "estimated_time", nullable = false)
    private Long estimatedTime;

    public Duration getEstimatedTime() {
        return (estimatedTime != null) ? Duration.ofSeconds(estimatedTime) : null;
    }

    public void setEstimatedTime(Duration duration) {
        this.estimatedTime = (duration != null) ? duration.getSeconds() : null;
    }
}