package com.fon.master.pricing_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "FuelType")
public class FuelType {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "fuelName", nullable = false)
    private String fuelName;

    @Column(name = "fuelTags", nullable = false)
    private String fuelTags;

    @Column(name = "fuelUnit")
    private String fuelUnit;
}
