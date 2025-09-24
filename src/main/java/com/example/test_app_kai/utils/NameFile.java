package com.example.test_app_kai.utils;


import static com.example.test_app_kai.constants.Constants.*;
import static java.lang.Thread.currentThread;

public class NameFile {

    public static String getNameCsvFile(String departureStationCode, String arrivalStationCode) {
        return FilesAndFolderChecker.getProgrammCatalog() + SLASH + "tmp" + SLASH +
                currentThread().getName() + DASH +
                departureStationCode.replace(SLASH, DASH) + DASH +
                arrivalStationCode.replace(SLASH, DASH) + CSV_EXTENSION;
    }
}
