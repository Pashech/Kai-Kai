package com.example.test_app_kai.response_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainData {

    private String status;
    private String originLabel;
    private String destinationLabel;
    private List<TrainInventory> departTrainInventories;
    private List<TrainInventory> returnTrainInventories;
}
