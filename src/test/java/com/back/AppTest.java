package com.back;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {
    @TempDir
    Path tempDir;

    private String run(String input) {
        return AppTestRunner.run(input, tempDir.resolve("data.json"));
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
}
