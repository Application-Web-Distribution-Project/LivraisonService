package com.restaurant.reclamations.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackingInfoDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private double latitude;

    private double longitude;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime estimatedArrivalTime;

    public TrackingInfoDTO(double latitude, double longitude, Status status, String s) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.estimatedArrivalTime = LocalDateTime.now();
    }
}
