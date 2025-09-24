package com.example.test_app_kai.service;

import java.util.Scanner;
import java.util.regex.Pattern;

public interface ValidationInputService {
    String getReturnDate(Scanner scanner, Pattern pattern);
}
