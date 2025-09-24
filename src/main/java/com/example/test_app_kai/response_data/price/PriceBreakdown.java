package com.example.test_app_kai.response_data.price;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceBreakdown {

    private List<PriceEntry> priceEntry;
    private TotalPrice totalPrice;
    private Object additionalInfo;
}
