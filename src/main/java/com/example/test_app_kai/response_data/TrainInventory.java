package com.example.test_app_kai.response_data;

import com.example.test_app_kai.response_data.price.PriceBreakdown;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainInventory {

    private String ticketLabel;
    private String trainBrandLabel;
    private String promoLabel;
    private String promoIconUrl;
    private Fare fare;
    private DepartureTime departureTime;
    private DepartureTime arrivalTime;
    private Duration duration;
    private String availability;
    private String numSeatsAvailable;
    private String numTransits;
    private List<TrainSegment> trainSegments;
    private PriceBreakdown priceBreakdown;
}
