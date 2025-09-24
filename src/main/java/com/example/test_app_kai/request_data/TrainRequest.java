package com.example.test_app_kai.request_data;

import com.example.test_app_kai.response_data.DataRequest;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TrainRequest {

    private List<String> fields;
    private DataRequest data;
    private String clientInterface;
}
