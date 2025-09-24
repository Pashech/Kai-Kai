package com.example.test_app_kai.service;

import com.example.test_app_kai.request_data.TrainRequest;
import com.example.test_app_kai.response_data.DataRequest;
import com.example.test_app_kai.response_data.DepartureDate;
import com.example.test_app_kai.response_data.TrackingMap;
import com.example.test_app_kai.task_service.Task;

public interface RequestDataService {

    TrainRequest createTrainRequest(Task task);
    DepartureDate createDepartureDate(String date);
    TrackingMap createTrackingMap();
    DataRequest createDataRequest(Task task);
}
