package com.example.test_app_kai.service.impl;

import com.example.test_app_kai.request_data.TrainRequest;
import com.example.test_app_kai.response_data.DataRequest;
import com.example.test_app_kai.response_data.DepartureDate;
import com.example.test_app_kai.response_data.TrackingMap;
import com.example.test_app_kai.service.RequestDataService;
import com.example.test_app_kai.task_service.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.test_app_kai.constants.Constants.*;

@Service
@AllArgsConstructor
public class RequestDataServiceImpl implements RequestDataService {

    @Override
    public TrainRequest createTrainRequest(Task task) {

        return TrainRequest.builder()
                .fields(List.of())
                .data(createDataRequest(task))
                .clientInterface(CLIENT_INTERFACE)
                .build();
    }

    @Override
    public DepartureDate createDepartureDate(String date) {

        LocalDate localDate = getLocalDate(date);

        int day = localDate.getDayOfMonth();
        int month = localDate.getMonth().getValue();
        int year = localDate.getYear();

        return DepartureDate.builder()
                .day(day)
                .month(month)
                .year(year)
                .build();
    }



    @Override
    public TrackingMap createTrackingMap() {
        return TrackingMap.builder()
                .utmId(null)
                .utmEntryTimeMillis(0)
                .build();
    }

    @Override
    public DataRequest createDataRequest(Task task) {

        return DataRequest.builder()
                .departureDate(createDepartureDate(task.getDepartureDate()))
                .returnDate(null)
                .destination(task.getArrivalStation())
                .origin(task.getDepartureStation())
                .numOfAdult(1)
                .numOfInfant(0)
                .providerType(PROVIDER_TYPE)
                .currency(CURRENCY_CODE)
                .trackingMap(createTrackingMap())
                .build();
    }

    private LocalDate getLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date, formatter);
    }
}
