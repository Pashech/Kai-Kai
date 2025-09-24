package com.example.test_app_kai.utils;

import com.example.test_app_kai.task_service.Task;
import com.opencsv.CSVWriter;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.minio.MinioClient;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.StringJoiner;
import static com.example.test_app_kai.constants.Constants.*;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;


@Slf4j
@Service
public class ResultWriter {

    @Value("${s3.endpoint}")
    private String endpoint;
    @Value("${s3.access-key}")
    private String accessKey;
    @Value("${s3.secret-key}")
    private String secretKey;
    @Value("${s3.bucket-name}")
    private String bucketName;
    private static final String HARVESTER_NAME = System.getenv("HARVESTER_NAME");
    private static final String SERVER_NUMBER = System.getenv("SERVER_NUMBER");

    public void writeInThreadFile(String csv, String date, String write) {
        final boolean fileExist = isFileExist(csv);
        try {
            File inputFile = new File(csv);
            CSVWriter csvWriter;

            if (!fileExist) {
                boolean newFileIsCreated = inputFile.createNewFile();
                if (!newFileIsCreated) {
                    log.error("New file => " + inputFile + " is not created");
                }
            }
            csvWriter = new CSVWriter(new FileWriter(inputFile.getAbsolutePath(), true));

            String[] recordStrings = write.split(" {3}");
            csvWriter.writeNext(recordStrings);
            csvWriter.close();
        } catch (IOException e) {
            log.error(format("Writing in file %s FAILED !!!!! Date %s Exc.: %s", currentThread().getName(), date, e));
        }
    }

    public void resultFile(String threadFile, String oneFileInput) throws NoSuchFileException, FileNotFoundException {
        BufferedOutputStream bufOut;
        File oneFile = new File(oneFileInput);
        if (!oneFile.isFile()) {
            log.error("Result file not exist !");
            throw new NoSuchFileException("Result file not exist !");
        }

        try {
            bufOut = new BufferedOutputStream(new FileOutputStream(oneFile, true));
            BufferedInputStream bufReadFirst = new BufferedInputStream(new FileInputStream(threadFile));
            int n;
            while ((n = bufReadFirst.read()) != -1) {
                bufOut.write(n);
            }
            bufOut.flush();
            bufOut.close();
            bufReadFirst.close();
            log.info(format("Data from '%s' insert in result file successfully", threadFile));
        } catch (NullPointerException | IOException e) {
            log.error(format("Result file not found %s", e));
            log.info(format("Data from '%s' insert in result file failed - %s", threadFile, e));
        }
    }

    public void fileUploadToS3(String filename, String filePath) {
        boolean fileUploadSuccess = false;
        for (int i = 1; i <= 3; i++) {
            try {
                MinioClient minioClient = new MinioClient(
                        endpoint,
                        accessKey,
                        secretKey
                );
                minioClient.putObject(bucketName, filePath, filename);
                fileUploadSuccess = true;
                break;
            } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException |
                     XmlPullParserException e) {
                log.error(format("Uploading file: %s FAILED !!!! Exc.: %s", filename, e));
            }

            log.warn("Retry upload file on S3.");
        }
        log.info("File upload success => " + fileUploadSuccess);
    }

    public String getFilePathForS3(Task task) {
        StringJoiner joiner = new StringJoiner("\\");
        String route = task.getDepartureStationUuid() + DOUBLE_UNDERSCORE + task.getArrivalStationUuid();
        joiner.add("rib").add(route).add(task.getDepartureDate()).add(LocalDate.now().toString()).add(task.getCrawlerType()).add(task.getTaskUuid());
        return joiner + CSV_EXTENSION;
    }

    public boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists() && file.isFile();
    }

    public String writeJsonInFile(Task task, String oneFileInput) throws ParseException {
        Date currentDate = new Date();
        LocalDate today = LocalDate.now();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        int index = 1;
        File oneFile = new File(oneFileInput);
        do {
            if (!oneFile.isFile()) {
                log.info("Datafile not found, create datafile");
            } else {
                log.info("Datafile exist, try increase index for +1 and create datafile");
                index++;
                oneFileInput = FilesAndFolderChecker.getProgrammCatalog() + "\\rib\\" + task.getCrawlerType() + formatter.format(currentDate) + DASH + task.getDepartureStation().replaceAll("\\s+", "-").replace(SLASH, DASH) + DASH + task.getArrivalStation().replaceAll("\\s+", "-").replace(SLASH, DASH) + DASH + index + DASH + HARVESTER_NAME + ".csv";
                if (SERVER_NUMBER != null && !SERVER_NUMBER.equals("null")) {
                    oneFileInput = FilesAndFolderChecker.getProgrammCatalog() + "\\rib\\" + task.getCrawlerType() + formatter.format(currentDate) + DASH + task.getDepartureStation().replaceAll("\\s+", "-").replace(SLASH, DASH) + DASH + task.getArrivalStation().replaceAll("\\s+", "-").replace(SLASH, DASH) + DASH + index + DASH + SERVER_NUMBER + "_" + HARVESTER_NAME + ".csv";
                }
                oneFile = new File(oneFileInput);
            }
        } while (oneFile.isFile());

        LocalDate dateFromTask = LocalDate.parse(task.getDepartureDate());
        long dayFrom = ChronoUnit.DAYS.between(today, dateFromTask);
        String writeConfigString = "{\"date\":\"" + formatter.format(currentDate) + "\",\"route\":\"" + task.getDepartureStation() + DASH + task.getArrivalStation() + "\",\"crawler_type\":\"" + task.getCrawlerType() + "\",\"plugin_type\":\"external\",\"from\":" + dayFrom + ",\"depth\":" + 1 + ",\"task_uuid\":\"" + task.getTaskUuid() + "\"}\n";

        try (FileOutputStream fos = new FileOutputStream(oneFile)) {
            byte[] buffer = writeConfigString.getBytes();
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            log.info("Writing config first string failed !");
        }
        return oneFileInput;
    }
}
