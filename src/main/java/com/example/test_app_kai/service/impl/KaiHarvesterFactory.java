package com.example.test_app_kai.service.impl;

import com.example.test_app_kai.client_service.TrainClientService;
import com.example.test_app_kai.service.RequestDataService;
import com.example.test_app_kai.service.TrainTicketService;
import com.example.test_app_kai.task_service.Task;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KaiHarvesterFactory {

    private RequestDataService requestDataService;
    private TrainTicketService trainTicketService;
    private TrainClientService trainClientService;

    public KaiHarvester create(Task task) {
        return new KaiHarvester(task, requestDataService, trainTicketService, trainClientService);
    }
}
