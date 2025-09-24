package com.example.test_app_kai.response_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DepartureTime {

    private DepartureDate monthDayYear;
    private Duration hourMinute;
}
