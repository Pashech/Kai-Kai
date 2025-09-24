package com.example.test_app_kai.response_data;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataRequest {

    private DepartureDate departureDate;
    private DepartureDate returnDate;
    private String destination;
    private String origin;
    private int numOfAdult;
    private int numOfInfant;
    private String providerType;
    private String currency;
    private TrackingMap trackingMap;
}