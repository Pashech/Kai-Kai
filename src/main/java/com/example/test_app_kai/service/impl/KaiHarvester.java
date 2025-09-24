package com.example.test_app_kai.service.impl;

import com.example.test_app_kai.client_service.TrainClientService;
import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.request_data.TrainRequest;
import com.example.test_app_kai.response_data.TrainResponseData;
import com.example.test_app_kai.service.RequestDataService;
import com.example.test_app_kai.service.TrainTicketService;
import com.example.test_app_kai.task_service.Task;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Service
@Slf4j
public class KaiHarvester implements Callable<HarvesterResult> {

    private final Task task;
    private final RequestDataService requestDataService;
    private final TrainTicketService trainTicketService;
    private final TrainClientService trainClientService;

    @Override
    public HarvesterResult call() {
        TrainRequest trainRequest = requestDataService.createTrainRequest(task);

        TrainResponseData responseData;
        try {
            responseData = trainClientService.clientTrainRequest(trainRequest);
        } catch (WebClientRequestException e) {
            log.error(e.getMessage());
            return new HarvesterResult(e.getMessage(), 610);
        } catch (WebClientResponseException e) {
            log.error(e.getMessage());
            return new HarvesterResult(e.getMessage(), 601);
        } catch (SSLHandshakeException e) {
            log.error(e.getMessage());
            return new HarvesterResult("SSL certificate error", 602);
        } catch (SocketTimeoutException e) {
            log.error(e.getMessage());
            return new HarvesterResult("Socket connection error", 603);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new HarvesterResult(e.getMessage(), 613);
        }

        return trainTicketService.getTickets(responseData, task);
    }
}
