package com.back;

import com.back.domain.system.controller.SystemController;

import java.nio.file.Path;
import java.util.Scanner;

public class App {
    private final Scanner scanner;
    private final Path dataJsonPath;
    private final SystemController systemController;

    public App(Scanner scanner, Path dataJsonPath) {
        this.scanner = scanner;
        this.dataJsonPath = dataJsonPath;
        this.systemController = new SystemController();
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");

            if (!scanner.hasNextLine()) break;

            Rq rq = new Rq(scanner.nextLine());

            switch (rq.getActionName()) {
                case "종료" -> {
                    systemController.actionExit();
                    return;
                }
                default -> System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}
