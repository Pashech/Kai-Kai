package com.example.test_app_kai.service.impl;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.output_data.TrainTicket;
import com.example.test_app_kai.response_data.*;
import com.example.test_app_kai.service.TrainTicketService;
import com.example.test_app_kai.task_service.Task;
import com.example.test_app_kai.utils.ResultWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.test_app_kai.constants.Constants.*;
import static com.example.test_app_kai.utils.NameFile.getNameCsvFile;
import static java.lang.Thread.currentThread;
import static java.time.LocalDate.now;

@Slf4j
@Service
@AllArgsConstructor
public class TrainTicketServiceImpl implements TrainTicketService {
    private final ResultWriter resultWriter;
    private static final String DEFAULT_TRAIN_BRAND = "KAI";
    private static final String EXPIRED_STATUS_TICKET = "EXPIRED";

    @Override
    public HarvesterResult getTickets(TrainResponseData trainResponseData, Task task) {

        List<TrainTicket> resultTickets = new ArrayList<>();
        String csvFileName = getNameCsvFile(task.getDepartureStation(), task.getArrivalStation());
        String emptyTicket = TrainTicket.getEmptyTicket(task.getDepartureDate(), task.getDepartureStation(), task.getArrivalStation());
        List<TrainTicket> emptyTicketList = new ArrayList<>();
        emptyTicketList.add(new TrainTicket(task.getDepartureDate(), task.getDepartureStation(), task.getArrivalStation()));
        HarvesterResult harvesterResult = new HarvesterResult();

        List<TrainInventory> trainInventories = trainResponseData.getData().getDepartTrainInventories();

        List<TrainTicket> trainTickets = getTickets(trainInventories, task);

        harvesterResult.setTickets(trainTickets);

        Map<Integer, TrainTicket> ticketsToWrite = trainTickets.stream().collect(Collectors.toMap(TrainTicket::hashCode, TrainTicket::new));

        if (!trainTickets.isEmpty()) {
            harvesterResult.setResultCode(SUCCESS_CODE);
            harvesterResult.setHarvestMessage(SUCCESS_MESSAGE);
        } else {
            harvesterResult.setResultCode(SUCCESS_EMPTY_LIST_CODE);
            harvesterResult.setHarvestMessage(SUCCESS_EMPTY_LIST_MESSAGE);
        }
        harvesterResult.setDateTimestamp(Instant.now().getEpochSecond());
        String filePath = resultWriter.getFilePathForS3(task);
        harvesterResult.setResultFileName(filePath);

        if (!harvesterResult.getTickets().isEmpty()) {
            ticketsToWrite.forEach((key, value) -> resultWriter.writeInThreadFile(csvFileName, task.getDepartureDate(), value.toString()));
            ticketsToWrite.forEach((key, value) -> resultTickets.add(value));
            log.info("Day  {} in thread {} is written.", task.getDepartureDate(), currentThread().getName());
            return new HarvesterResult(SUCCESS_HARVESTER_MESSAGE, SUCCESS_HARVESTER_CODE, csvFileName, resultTickets, now().toEpochDay());
        }
        resultWriter.writeInThreadFile(csvFileName, task.getDepartureDate(), emptyTicket);
        resultTickets.add(new TrainTicket(task.getDepartureDate(), task.getDepartureStation(), task.getArrivalStation()));
        return new HarvesterResult(EMPTY_HARVESTER_MESSAGE, NULL_TICKET_HARVESTER_CODE, csvFileName, emptyTicketList, now().toEpochDay());
    }

    private List<TrainTicket> getTickets(List<TrainInventory> trainInventories, Task task) {
        List<String> coachClasses = Arrays.stream(task.getCoachClasses().split(SPLIT_DELIMITER)).toList();
        List<String> trainBrands = Arrays.stream(task.getTrainBrands().split(SPLIT_DELIMITER)).toList();
        List<TrainTicket> trainTickets;

        trainTickets = trainInventories.stream()
                .filter(this::isAvailable)
                .map(inv -> toTrainTicket(task, inv, coachClasses, trainBrands))
                .collect(Collectors.toList());

        return trainTickets;
    }

    private TrainTicket toTrainTicket(Task task, TrainInventory inv, List<String> coachClasses, List<String> trainBrands) {
        TrainTicket ticket = getTrainTicket(inv);
        getDataFromTrainSegments(inv, ticket);
        if(isValidTicket(task, inv, ticket, coachClasses) && !trainBrandFilter(trainBrands, ticket)) {
            ticket.setTrainBrand(DEFAULT_TRAIN_BRAND);
        }
        return ticket;
    }

    private boolean isAvailable(TrainInventory trainInventory) {
        return !trainInventory.getAvailability().equals(BLOCKED_STATUS) && !trainInventory.getAvailability().equals(EXPIRED_STATUS_TICKET);
    }

    private boolean isValidTicket(Task task, TrainInventory trainInventory, TrainTicket trainTicket, List<String> coachClasses) {
        return trainTicket.getDeparture().equals(task.getDepartureStation()) && trainTicket.getArrival().equals(task.getArrivalStation()) && coachClassFilter(coachClasses, trainTicket) && trainInventory.getTrainSegments().size() - 1 <= task.getChanges();
    }

    private boolean coachClassFilter(List<String> coachClasses, TrainTicket ticket) {
        return coachClasses.contains(ticket.getCoachClassName());
    }

    private boolean trainBrandFilter(List<String> trainBrands, TrainTicket ticket) {
        return trainBrands.contains(ticket.getTrainBrand());
    }

    private void getDataFromTrainSegments(TrainInventory trainInventory, TrainTicket trainTicket) {
        trainTicket.setDeparture(trainInventory.getTrainSegments().getFirst().getProductSummary().getOriginCode());
        trainTicket.setArrival(arrivalStation(trainInventory.getTrainSegments()));
        trainTicket.setChangeStation(getChangeStation(trainInventory.getTrainSegments()));
        trainTicket.setTrainNumber(getTrainNumber(trainInventory.getTrainSegments()));
        trainTicket.setTrainClass(trainClass(trainInventory.getTrainSegments()));
        trainTicket.setCoachClassName(trainInventory.getTrainSegments().getFirst().getProductSummary().getSeatClass());
    }

    private TrainTicket getTrainTicket(TrainInventory trainInventory) {
        TrainTicket trainTicket = new TrainTicket();
        trainTicket.setDate(String.valueOf(getDepartureDateFromData(trainInventory.getDepartureTime().getMonthDayYear())));
        trainTicket.setDepartureTime(String.valueOf(getDepartureTimeAndDuration(trainInventory.getDepartureTime().getHourMinute())));
        trainTicket.setDuration(String.valueOf(getDepartureTimeAndDuration(trainInventory.getDuration())));
        trainTicket.setTrainBrand(trainBrandFormatter(trainInventory.getTrainBrandLabel()));
        trainTicket.setFare(FARE_CODE_TICKET);
        trainTicket.setPrice(String.valueOf(trainInventory.getPriceBreakdown().getTotalPrice().getFare().getCurrencyValue().getAmount()));
        trainTicket.setCurrency(trainInventory.getPriceBreakdown().getTotalPrice().getFare().getCurrencyValue().getCurrency());
        return trainTicket;
    }

    private String arrivalStation(List<TrainSegment> trainSegments) {
        return trainSegments.size() == 1 ? trainSegments.getFirst().getProductSummary().getDestinationCode() : trainSegments.getLast().getProductSummary().getDestinationCode();

    }

    private String trainClass(List<TrainSegment> trainSegments) {
        int change = trainSegments.size() - 1;
        return trainSegments.size() == 1 ? TRAIN_CLASS_WITHOUT_CHANGES : "Train with " + change + " change";
    }

    private String getTrainNumber(List<TrainSegment> trainSegments) {
        return trainSegments.size() == 1 ? trainSegments.getFirst().getProductSummary().getTrainNumber() : trainSegments.stream().map(TrainSegment::getProductSummary).map(ProductSummary::getTrainNumber).collect(Collectors.joining(JOINING_SEPARATOR));
    }

    private String getChangeStation(List<TrainSegment> trainSegments) {
        return trainSegments.size() == 1 ? "NULL" : trainSegments.stream().limit(trainSegments.size() - 1).map(TrainSegment::getDestinationStationLabel).collect(Collectors.joining(JOINING_SEPARATOR));
    }

    private LocalDate getDepartureDateFromData(DepartureDate dayMonthYear) {
        return LocalDate.of(dayMonthYear.getYear(), dayMonthYear.getMonth(), dayMonthYear.getDay());
    }

    private int getDepartureTimeAndDuration(Duration time) {
        return time.getHour() * 3600 + time.getMinute() * 60;
    }

    private String trainBrandFormatter(String trainBrand) {
        return trainBrand.replaceAll(TRAIN_BRAND_REGEX_PATTERN, TRAIN_BRAND_REPLACE_PATTERN).trim();
    }
}
