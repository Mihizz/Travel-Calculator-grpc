package com.fon.master.pricing_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "countries")
public class Country {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name = "countryName", nullable = false, unique = true)
    private String countryName;

    @Column(name = "countryNameEng", nullable = false, unique = true)
    private String countryNameEng;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "baseCurrencyId", nullable = false)
    private Currency baseCurrency;


}
