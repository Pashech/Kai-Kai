package com.example.test_app_kai.response_data;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrackingMap {

    private String utmId;
    private int utmEntryTimeMillis;
}
