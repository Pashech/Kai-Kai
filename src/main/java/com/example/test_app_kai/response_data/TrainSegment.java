package com.example.test_app_kai.response_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainSegment {

    private String segmentOrder;
    private ProductSummary productSummary;
    private String destinationStationLabel;
}

