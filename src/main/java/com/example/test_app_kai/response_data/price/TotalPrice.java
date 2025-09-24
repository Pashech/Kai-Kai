package com.example.test_app_kai.response_data.price;

import com.example.test_app_kai.response_data.Fare;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TotalPrice {

    private String itemLabel;
    private Fare fare;

}
