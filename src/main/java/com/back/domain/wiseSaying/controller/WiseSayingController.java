package com.back.domain.wiseSaying.controller;

import com.back.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Optional;
import java.util.Scanner;

/**
 * 명언 관련 응대. 입력을 받고 결과를 출력하며, 판단은 서비스에 맡긴다.
 */
public class WiseSayingController {
    /** id 파라미터가 없거나 숫자가 아닐 때 Rq가 돌려줄 기본값 */
    private static final int INVALID_ID = -1;

    private final Scanner scanner;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner scanner, WiseSayingService wiseSayingService) {
        this.scanner = scanner;
        this.wiseSayingService = wiseSayingService;
    }

    public void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        WiseSaying wiseSaying = wiseSayingService.write(content, author);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying wiseSaying : wiseSayingService.findForList()) {
            System.out.println("%d / %s / %s".formatted(
                    wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));
        }
    }

    public void actionDelete(Rq rq) {
        int id = rq.getParamAsInt("id", INVALID_ID);

        if (id == INVALID_ID) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        if (!wiseSayingService.delete(id)) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }

    public void actionModify(Rq rq) {
        int id = rq.getParamAsInt("id", INVALID_ID);

        if (id == INVALID_ID) {
            System.out.println("id를 정확히 입력해주세요.");
            return;
        }

        Optional<WiseSaying> opWiseSaying = wiseSayingService.findById(id);

        if (opWiseSaying.isEmpty()) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id));
            return;
        }

        WiseSaying wiseSaying = opWiseSaying.get();

        System.out.println("명언(기존) : %s".formatted(wiseSaying.getContent()));
        System.out.print("명언 : ");
        String content = scanner.nextLine().trim();

        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
        System.out.print("작가 : ");
        String author = scanner.nextLine().trim();

        wiseSayingService.modify(wiseSaying, content, author);
    }

    public void actionBuild() {
        wiseSayingService.build();

        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
