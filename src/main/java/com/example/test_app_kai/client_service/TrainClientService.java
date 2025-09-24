package com.example.test_app_kai.client_service;

import com.example.test_app_kai.request_data.TrainRequest;
import com.example.test_app_kai.response_data.TrainResponseData;

public interface TrainClientService {

    TrainResponseData clientTrainRequest(TrainRequest trainRequest) throws Exception;
}
