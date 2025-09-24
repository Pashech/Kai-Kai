package com.example.test_app_kai.response_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductSummary {

    private String originCode;
    private String destinationCode;
    private DepartureTime departureTime;
    private DepartureTime arrivalTime;
    private String providerType;
    private String seatClass;
    private String subClass;
    private String trainBrand;
    private String trainNumber;
    private String ticketType;
}
