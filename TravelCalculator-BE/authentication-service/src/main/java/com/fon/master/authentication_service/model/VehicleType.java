package com.fon.master.authentication_service.model;

import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "vehicle_types")
public class VehicleType {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            name = "vehicleType", nullable = false
    )
    private String vehicleType;

    @Column(
            name = "seats", nullable = false
    )
    private int seats;
}
