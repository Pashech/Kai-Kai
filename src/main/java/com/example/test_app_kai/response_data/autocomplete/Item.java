package com.example.test_app_kai.response_data.autocomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    private String label;
    private String subLabel;
    private String code;
    private String searchFormLabel;
}
