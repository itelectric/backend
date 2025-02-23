package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.*;
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
@Table(name = "t_service")
@PrimaryKeyJoinColumn(name = "id")
public class ServiceManager extends BaseProduct implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "estimated_time", nullable = false)
    private Long estimatedTime;

    public void setEstimatedTime(Duration duration) {
        this.estimatedTime = (duration != null) ? duration.getSeconds() : null;
    }

    public Duration getEstimatedTime() {
        return (estimatedTime != null) ? Duration.ofSeconds(estimatedTime) : null;
    }
}