package com.example.test_app_kai.utils;

import com.example.test_app_kai.TestAppKaiApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

import static java.lang.String.format;

@Slf4j
@Component
public class FilesAndFolderChecker {

    public static void directoryCheck() {
        File logsPath = new File(getProgrammCatalog() + "/logs");
        if (logsPath.exists() && logsPath.isDirectory()) {
            log.info("Logs directory exist.");
        } else {
            try {
                boolean mkdir = logsPath.mkdir();
                if (mkdir) log.info("Directory logs not found, directory logs created successful.");
            } catch (SecurityException e) {
                log.info(format("Creating directory logs FAILED !!!! %s", e));
            }
        }

        File dataPath = new File(getProgrammCatalog() + "/data");
        if (dataPath.exists() && dataPath.isDirectory()) {
            log.info("Data directory exist.");
        } else {
            try {
                boolean mkdir = dataPath.mkdir();
                if (mkdir) log.info("Directory data not found, directory data created successful.");
            } catch (SecurityException e) {
                log.info(format("Creating directory data FAILED !!!! %s", e));
            }
        }

        File ribPath = new File(getProgrammCatalog() + "/rib");
        if (ribPath.exists() && ribPath.isDirectory()) {
            log.info("Rib directory exist.");
        } else {
            try {
                boolean mkdir = ribPath.mkdir();
                if (mkdir) log.info("Directory rib not found, directory rib created successful.");
            } catch (SecurityException e) {
                log.info(format("Creating directory rib FAILED !!!! %s", e));
            }
        }

        File tmpPath = new File(getProgrammCatalog() + "/tmp");
        if (tmpPath.exists() && tmpPath.isDirectory()) {
            log.info("Tmp directory exist.");
            boolean delete = recursiveDelete(tmpPath);
            boolean mkdir = tmpPath.mkdir();
            if (delete && mkdir) log.info("Tmp directory deleted and recreated.");
        } else {
            try {
                boolean mkdir = tmpPath.mkdir();
                if (mkdir) log.info("Directory tmp not found, directory tmp created successful.");
            } catch (SecurityException e) {
                log.info(format("Creating directory tmp FAILED !!!! %s", e));
            }
        }
    }

    public static boolean recursiveDelete(File file) {
        if (!file.exists())
            return false;
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                recursiveDelete(f);
            }
        }
        return file.delete();
    }

    public static String getProgrammCatalog() {
        String jarCatalog = null;
        try {
            jarCatalog = TestAppKaiApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new File(jarCatalog).getParent();
    }
}
