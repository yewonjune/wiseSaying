package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @TempDir
    Path tempDir;

    private String run(String input) {
        return AppTestRunner.run(input, dataJsonPath());
    }

    private Path dataJsonPath() {
        return tempDir.resolve("data.json");
    }

    private String readDataJson() throws IOException {
        return Files.readString(dataJsonPath(), StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("시작하면 제목과 명령 프롬프트가 나오고, 종료하면 추가 메시지 없이 끝난다")
    void startAndExit() {
        String output = run("""
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령)\s""");
    }

    @Test
    @DisplayName("알 수 없는 명령에는 안내 메시지를 출력하고 계속 명령을 받는다")
    void unknownCommand() {
        String output = run("""
                안녕
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) 알 수 없는 명령입니다.
                명령)\s""");
    }

    @Test
    @DisplayName("파라미터가 붙은 알 수 없는 명령도 명령 이름으로 판단한다")
    void unknownCommandWithParams() {
        String output = run("""
                안녕?id=1
                종료
                """);

        assertThat(output).contains("알 수 없는 명령입니다.");
    }

    @Test
    @DisplayName("등록하면 내용과 작가를 입력받고 1번 명언이 등록된다")
    void register() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) 명언 : 작가 : 1번 명언이 등록되었습니다.
                명령)\s""");
    }

    @Test
    @DisplayName("등록할 때마다 번호가 1씩 증가한다")
    void registerIncrementsId() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                종료
                """);

        assertThat(output)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록은 번호 내림차순으로 번호 / 작가 / 명언 형식으로 출력된다")
    void list() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                홍길동
                목록
                종료
                """);

        assertThat(output).endsWith("""
                명령) 번호 / 작가 / 명언
                ----------------------
                2 / 홍길동 / 과거에 집착하지 마라.
                1 / 작자미상 / 현재를 사랑하라.
                명령)\s""");
    }

    @Test
    @DisplayName("삭제하면 성공 메시지가 나오고 목록에서 사라진다")
    void delete() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                종료
                """);

        assertThat(output)
                .contains("명령) 1번 명언이 삭제되었습니다.\n")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("없는 번호를 삭제하면 존재하지 않는다고 알려준다")
    void deleteNotFound() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                삭제?id=1
                삭제?id=1
                종료
                """);

        assertThat(output)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("삭제할 때 id가 없거나 숫자가 아니면 id를 정확히 입력하라고 안내한다")
    void deleteInvalidId() {
        String output = run("""
                삭제
                삭제?id=abc
                삭제?id=
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) id를 정확히 입력해주세요.
                명령) id를 정확히 입력해주세요.
                명령) id를 정확히 입력해주세요.
                명령)\s""");
    }

    @Test
    @DisplayName("음수 id도 숫자이므로 삭제·수정 시 존재하지 않는다고 알려준다")
    void negativeIdNotFound() {
        String output = run("""
                삭제?id=-1
                수정?id=-1
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) -1번 명언은 존재하지 않습니다.
                명령) -1번 명언은 존재하지 않습니다.
                명령)\s""");
    }

    @Test
    @DisplayName("등록·수정 시 내용과 작가의 앞뒤 공백도 입력한 그대로 저장한다")
    void contentAndAuthorStoredAsTyped() throws IOException {
        run("""
                등록
                  현재를 사랑하라.\s
                 작자미상\s
                등록
                과거에 집착하지 마라.
                작자미상
                수정?id=2
                 현재와 자신을 사랑하라.
                홍길동\s\s
                빌드
                종료
                """);

        assertThat(readDataJson())
                .contains("\"content\": \"  현재를 사랑하라. \"")
                .contains("\"author\": \" 작자미상 \"")
                .contains("\"content\": \" 현재와 자신을 사랑하라.\"")
                .contains("\"author\": \"홍길동  \"");
    }

    @Test
    @DisplayName("삭제된 번호는 다시 쓰지 않는다")
    void deletedIdIsNotReused() {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=2
                등록
                미래를 꿈꿔라.
                작자미상
                종료
                """);

        assertThat(output).contains("3번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("수정하면 기존 내용·작가를 보여주고 새 값을 입력받아 목록에 반영한다")
    void modify() {
        String output = run("""
                등록
                과거에 집착하지 마라.
                작자미상
                수정?id=1
                현재와 자신을 사랑하라.
                홍길동
                목록
                종료
                """);

        assertThat(output).endsWith("""
                명령) 명언(기존) : 과거에 집착하지 마라.
                명언 : 작가(기존) : 작자미상
                작가 : 명령) 번호 / 작가 / 명언
                ----------------------
                1 / 홍길동 / 현재와 자신을 사랑하라.
                명령)\s""");
    }

    @Test
    @DisplayName("없는 번호를 수정하면 입력을 받지 않고 존재하지 않는다고 알려준다")
    void modifyNotFound() {
        String output = run("""
                수정?id=1
                목록
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) 1번 명언은 존재하지 않습니다.
                명령) 번호 / 작가 / 명언
                ----------------------
                명령)\s""");
    }

    @Test
    @DisplayName("수정할 때 id가 없거나 숫자가 아니면 id를 정확히 입력하라고 안내한다")
    void modifyInvalidId() {
        String output = run("""
                수정
                수정?id=abc
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) id를 정확히 입력해주세요.
                명령) id를 정확히 입력해주세요.
                명령)\s""");
    }

    @Test
    @DisplayName("빌드하면 완료 메시지가 나오고 data.json에 번호 오름차순 배열이 저장된다")
    void build() throws IOException {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                홍길동
                빌드
                종료
                """);

        assertThat(output).contains("명령) data.json 파일의 내용이 갱신되었습니다.\n");
        assertThat(readDataJson()).isEqualTo("""
                [
                  {
                    "id": 1,
                    "content": "현재를 사랑하라.",
                    "author": "작자미상"
                  },
                  {
                    "id": 2,
                    "content": "과거에 집착하지 마라.",
                    "author": "홍길동"
                  }
                ]""");
    }

    @Test
    @DisplayName("명언이 없을 때 빌드하면 빈 배열이 저장된다")
    void buildEmpty() throws IOException {
        run("""
                빌드
                종료
                """);

        assertThat(readDataJson()).isEqualTo("[]");
    }

    @Test
    @DisplayName("내용과 작가의 따옴표·역슬래시는 이스케이프되어 저장된다")
    void buildEscapes() throws IOException {
        run("""
                등록
                그는 "괜찮아"라고 말했다.
                C:\\Users
                빌드
                종료
                """);

        assertThat(readDataJson())
                .contains("\"content\": \"그는 \\\"괜찮아\\\"라고 말했다.\"")
                .contains("\"author\": \"C:\\\\Users\"");
    }

    @Test
    @DisplayName("빌드할 때마다 data.json을 최신 상태로 덮어쓴다")
    void buildOverwrites() throws IOException {
        run("""
                등록
                현재를 사랑하라.
                작자미상
                빌드
                삭제?id=1
                빌드
                종료
                """);

        assertThat(readDataJson()).isEqualTo("[]");
    }

    @Test
    @DisplayName("명언이 없으면 목록은 헤더와 구분선만 출력한다")
    void emptyList() {
        String output = run("""
                목록
                종료
                """);

        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) 번호 / 작가 / 명언
                ----------------------
                명령)\s""");
    }

    @Test
    @DisplayName("과제 예시 시나리오 전체가 명세의 목표 출력과 같고, 빌드 결과에 수정된 2번 명언만 남는다")
    void fullExampleScenario() throws IOException {
        String output = run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                삭제?id=1
                삭제?id=1
                수정?id=2
                현재와 자신을 사랑하라.
                홍길동
                목록
                빌드
                종료
                """);

        // 터미널이 보여주는 입력 에코는 stdout에 없으므로 프롬프트 뒤에 곧바로 다음 출력이 붙는다.
        assertThat(output).isEqualTo("""
                == 명언 앱 ==
                명령) 명언 : 작가 : 1번 명언이 등록되었습니다.
                명령) 명언 : 작가 : 2번 명언이 등록되었습니다.
                명령) 번호 / 작가 / 명언
                ----------------------
                2 / 작자미상 / 과거에 집착하지 마라.
                1 / 작자미상 / 현재를 사랑하라.
                명령) 1번 명언이 삭제되었습니다.
                명령) 1번 명언은 존재하지 않습니다.
                명령) 명언(기존) : 과거에 집착하지 마라.
                명언 : 작가(기존) : 작자미상
                작가 : 명령) 번호 / 작가 / 명언
                ----------------------
                2 / 홍길동 / 현재와 자신을 사랑하라.
                명령) data.json 파일의 내용이 갱신되었습니다.
                명령)\s""");

        assertThat(readDataJson()).isEqualTo("""
                [
                  {
                    "id": 2,
                    "content": "현재와 자신을 사랑하라.",
                    "author": "홍길동"
                  }
                ]""");
    }
}
