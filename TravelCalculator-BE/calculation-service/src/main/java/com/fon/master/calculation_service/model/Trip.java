package com.fon.master.calculation_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(name = "tripDistance", nullable = false)
    private double tripDistance;

    @Column(name = "tripTime")
    private String time;

    @Column(name = "paytool")
    private int paytool;

    @Column(name = "accountId")
    private Long accountId;

    @Column(name = "vehicleId")
    private Long vehicleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city1Id")
    private City city1;

    @Column(name = "cityName1", nullable = false)
    private String cityName1;

    @Column(name = "city1Code")
    private String city1Code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city2Id")
    private City city2;

    @Column(name = "cityName2", nullable = false)
    private String cityName2;

    @Column(name = "city2Code")
    private String city2Code;

    @Column(name = "fullPrice")
    private double fullPrice;

    @Column(name = "fuelId")
    private Long fuelId;

    @Column(name = "fuelSpent")
    private int fuelSpent;

    @Column(name = "consumptionRate")
    private double consumptionRate;

    @Column(name = "seats")
    private int seats;

    @Column(name = "countryId")
    private Long countryId;

    @Column(name = "currencyId")
    private Long currencyId;

    @Column(name = "executionTime")
    private Long executionTime;
}
