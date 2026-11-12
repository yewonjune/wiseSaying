package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.nio.file.Path;
import java.util.Scanner;

public class App {
    private final Scanner scanner;
    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public App(Scanner scanner, Path dataJsonPath) {
        this.scanner = scanner;

        // 스프링이 해 줄 생성자 주입을 직접 한다: Repository → Service → Controller
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository, dataJsonPath);

        this.systemController = new SystemController();
        this.wiseSayingController = new WiseSayingController(scanner, wiseSayingService);
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        boolean running = true;

        while (running) {
            System.out.print("명령) ");

            if (!scanner.hasNextLine()) break;

            Rq rq = new Rq(scanner.nextLine());

            switch (rq.getActionName()) {
                case "종료" -> running = systemController.actionExit();
                case "등록" -> wiseSayingController.actionWrite();
                case "목록" -> wiseSayingController.actionList();
                case "삭제" -> wiseSayingController.actionDelete(rq);
                case "수정" -> wiseSayingController.actionModify(rq);
                case "빌드" -> wiseSayingController.actionBuild();
                default -> System.out.println("알 수 없는 명령입니다.");
            }
        }
    }
}
