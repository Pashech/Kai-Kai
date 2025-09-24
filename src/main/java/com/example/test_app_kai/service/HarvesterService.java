package com.example.test_app_kai.service;

import com.example.test_app_kai.output_data.HarvesterResult;
import com.example.test_app_kai.output_data.TrainTicket;
import com.example.test_app_kai.service.impl.KaiHarvester;
import com.example.test_app_kai.service.impl.KaiHarvesterFactory;
import com.example.test_app_kai.task_service.Task;
import com.example.test_app_kai.utils.FilesAndFolderChecker;
import com.example.test_app_kai.utils.ResultWriter;
import com.example.test_app_kai.task_service.TaskInverser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.NoSuchFileException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.example.test_app_kai.constants.Constants.*;


@Service
@Slf4j
@AllArgsConstructor
public class HarvesterService {

    private final KaiHarvesterFactory kaiHarvesterFactory;
    private final HarvesterResult harvestResult;
    private final ResultWriter resultWriter;
    public static final String SERVER_NUMBER = System.getenv("SERVER_NUMBER");
    private static final String UNKNOWN_PARSING_ERROR = "Unknown parsing error";

    public HarvesterResult harvestData(Task task) throws ExecutionException, InterruptedException {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String oneFile =
                FilesAndFolderChecker.getProgrammCatalog() + "/rib/" + task.getCrawlerType() + formatter.format(
                        currentDate) + DASH + task.getDepartureStation().replace(SLASH, DASH).replace(SPACE, DASH)
                        + DASH + task.getArrivalStation().replace(SLASH, DASH).replace(SPACE, DASH) + DASH
                        + HARVESTER_NAME + ".csv";
        if (null != SERVER_NUMBER && !SERVER_NUMBER.equals("null")) {
            oneFile = FilesAndFolderChecker.getProgrammCatalog() + "/rib/" + task.getCrawlerType() + formatter.format(
                    currentDate) + DASH + task.getDepartureStation().replace(SLASH, DASH).replace(SPACE, DASH) + DASH
                    + task.getArrivalStation().replace(SLASH, DASH).replace(SPACE, DASH) + DASH + SERVER_NUMBER
                    + UNDERSCORE + HARVESTER_NAME + ".csv";
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<HarvesterResult> resultFirst;
        Future<HarvesterResult> resultSecond;

        HarvesterResult harvesterResultFirst = null;
        HarvesterResult harvesterResultSecond = null;

        Task reversedTask = TaskInverser.getInversedTask(task);

        List<TrainTicket> resultTickets = new ArrayList<>();

        log.info(START_HARVESTER_TYPE, task.getCrawlerType());
        try {
            if(task.getCrawlerType().equalsIgnoreCase("KAI")){
                KaiHarvester harvester = kaiHarvesterFactory.create(task);
                resultFirst = executorService.submit(harvester);
                harvester = kaiHarvesterFactory.create(reversedTask);
                resultSecond = executorService.submit(harvester);
                harvesterResultFirst = resultFirst.get();
                harvesterResultSecond = resultSecond.get();
            }

            log.info(FINISH_HARVESTER_TYPE, task.getCrawlerType());
        } catch (Exception exception) {
            log.error(UNKNOWN_EXCEPTION, task.getCrawlerType(), exception,
                    Arrays.stream(exception.getStackTrace()).map(Objects::toString)
                            .collect(Collectors.joining("\n")));
            harvesterResultFirst = new HarvesterResult(UNKNOWN_PARSING_ERROR, ERROR_HARVESTER_CODE);
            harvesterResultSecond = new HarvesterResult(UNKNOWN_PARSING_ERROR, ERROR_HARVESTER_CODE);
        }

        log.info(ALL_FINISHED);

        if (!isSuccess(harvesterResultFirst.getResultCode()) || !isSuccess(harvesterResultSecond.getResultCode())) {
            log.error(ONE_FAILED);
            harvestResult.setResultCode(!isSuccess(harvesterResultFirst.getResultCode()) ?
                    harvesterResultFirst.getResultCode() :
                    harvesterResultSecond.getResultCode());
            String resMessage = "";
            if (!isSuccess(harvesterResultFirst.getResultCode())) {
                resMessage = harvesterResultFirst.getHarvestMessage();
            }
            if (!isSuccess(harvesterResultSecond.getResultCode())) {
                resMessage = resMessage + " | " + harvesterResultSecond.getHarvestMessage();
            }
            if (resMessage.isEmpty()) {
                harvestResult.setHarvestMessage("Thread fail");
            } else {
                harvestResult.setHarvestMessage(resMessage);
            }

            return harvestResult;
        }

        try {
            oneFile = resultWriter.writeJsonInFile(task, oneFile);
            resultWriter.resultFile(harvesterResultFirst.getResultFileName(), oneFile);
            resultWriter.resultFile(harvesterResultSecond.getResultFileName(), oneFile);
            List<List<TrainTicket>> lists = List.of(harvesterResultFirst.getTickets(), harvesterResultSecond.getTickets());
            resultTickets = joinLists(lists);

            String filePath = resultWriter.getFilePathForS3(task);
            System.out.println(filePath);
            String finalOneFile = oneFile;
            //CompletableFuture.runAsync(() -> resultWriter.fileUploadToS3(finalOneFile, filePath));
            harvestResult.setResultFileName(filePath);
        } catch (NoSuchFileException e) {
            log.error("Result file creating failed !");
        } catch (ParseException e) {
            log.error("Error in calculate depth !");
        } catch (FileNotFoundException e) {
            log.error("Thread file not found. Ex.: ", e);
        }

        harvestResult.setHarvestMessage(harvesterResultFirst.getHarvestMessage());
        harvestResult.setResultCode(harvesterResultFirst.getResultCode());
        harvestResult.setTickets(resultTickets);
        harvestResult.setDateTimestamp(Instant.now().getEpochSecond());

        if (!isSuccess(harvesterResultFirst.getResultCode())) {
            harvestResult.setUploadSuccess(false);
            log.info(THREAD_FAILED);
            throw new NotFoundException(THREAD_FAILED);
        }

        return harvestResult;
    }

    private boolean isSuccess(Integer inCode) {
        return inCode == SUCCESS_HARVESTER_CODE || inCode == NULL_TICKET_HARVESTER_CODE;
    }

    public static <T> List<T> joinLists(List<List<T>> lists) {
        return lists.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
