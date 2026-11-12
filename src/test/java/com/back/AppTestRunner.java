package com.back;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * 테스트 경계: 문자열 입력을 App에 넣고 콘솔 출력을 문자열로 돌려준다.
 */
public class AppTestRunner {
    public static String run(String input, Path dataJsonPath) {
        Scanner scanner = new Scanner(input);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();

        try (PrintStream out = new PrintStream(captured, true, StandardCharsets.UTF_8)) {
            System.setOut(out);
            new App(scanner, dataJsonPath).run();
        } finally {
            System.setOut(originalOut);
        }

        return captured.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
