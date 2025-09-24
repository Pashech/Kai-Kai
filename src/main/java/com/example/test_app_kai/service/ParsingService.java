package com.example.test_app_kai.service;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.task_service.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
@Service
@AllArgsConstructor
public class ParsingService {

    private final HarvesterService harvesterService;

    public HarvesterResult getParsedData(Task task) {

        HarvesterResult harvestResult;

        try {
            harvestResult = harvesterService.harvestData(task);
        } catch (ExecutionException e) {
            harvestResult = new HarvesterResult("ExecutionException", 222);
        } catch (InterruptedException e) {
            harvestResult = new HarvesterResult("InterruptedException", 111);
        }

        return harvestResult;
    }
}
