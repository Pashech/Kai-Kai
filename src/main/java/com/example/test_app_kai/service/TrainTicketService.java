package com.example.test_app_kai.service;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.response_data.TrainResponseData;
import com.example.test_app_kai.task_service.Task;

public interface TrainTicketService {
    HarvesterResult getTickets(TrainResponseData trainResponseData, Task task);
}
