package com.back;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 콘솔 인코딩(예: Windows 터미널의 MS949)에 맞춰 입력을 읽어야 한글이 깨지지 않는다.
        Charset stdinCharset = Charset.forName(System.getProperty("stdin.encoding"), Charset.defaultCharset());
        Scanner scanner = new Scanner(System.in, stdinCharset);
        new App(scanner, Path.of("data.json")).run();
        scanner.close();
    }
}
