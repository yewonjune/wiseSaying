package com.back.domain.wiseSaying.controller;

import com.back.Rq;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;

/**
 * 명언 관련 응대. 입력을 받고 결과를 출력하며, 판단은 서비스에 맡긴다.
 */
public class WiseSayingController {
    private final Scanner scanner;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner scanner, WiseSayingService wiseSayingService) {
        this.scanner = scanner;
        this.wiseSayingService = wiseSayingService;
    }

    public void actionWrite() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String author = scanner.nextLine();

        WiseSaying wiseSaying = wiseSayingService.write(content, author);

        System.out.println("%d번 명언이 등록되었습니다.".formatted(wiseSaying.getId()));
    }

    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (WiseSaying wiseSaying : wiseSayingService.findAllOrderByIdDesc()) {
            System.out.println("%d / %s / %s".formatted(
                    wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));
        }
    }

    public void actionDelete(Rq rq) {
        Optional<WiseSaying> target = findTargetById(rq);
        if (target.isEmpty()) return;

        int id = target.get().getId();
        wiseSayingService.delete(id);

        System.out.println("%d번 명언이 삭제되었습니다.".formatted(id));
    }

    public void actionModify(Rq rq) {
        Optional<WiseSaying> target = findTargetById(rq);
        if (target.isEmpty()) return;

        WiseSaying wiseSaying = target.get();

        System.out.println("명언(기존) : %s".formatted(wiseSaying.getContent()));
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
        System.out.print("작가 : ");
        String author = scanner.nextLine();

        wiseSayingService.modify(wiseSaying, content, author);
    }

    public void actionBuild() {
        wiseSayingService.build();

        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    /**
     * 명령의 id 파라미터로 대상 명언을 찾는다. 찾지 못하면 그 이유를 출력하고 빈 값을 돌려준다.
     */
    private Optional<WiseSaying> findTargetById(Rq rq) {
        OptionalInt id = rq.findParamAsInt("id");

        if (id.isEmpty()) {
            System.out.println("id를 정확히 입력해주세요.");
            return Optional.empty();
        }

        Optional<WiseSaying> wiseSaying = wiseSayingService.findById(id.getAsInt());

        if (wiseSaying.isEmpty()) {
            System.out.println("%d번 명언은 존재하지 않습니다.".formatted(id.getAsInt()));
        }

        return wiseSaying;
    }
}
