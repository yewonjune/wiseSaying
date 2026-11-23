package com.back;

import com.back.standard.util.TestUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 테스트 경계: 문자열 입력으로 실제 App을 실행하고 콘솔 출력을 문자열로 돌려준다.
 * App을 매번 새로 만들므로 저장소와 번호도 테스트마다 초기화된다.
 */
public final class AppTestRunner {
    private AppTestRunner() {
    }

    public static String run(String input, Consumer<Scanner> runApp) {
        var bytes = TestUtil.setOutToByteArray();

        try (Scanner scanner = TestUtil.genScanner(input)) {
            runApp.accept(scanner);
            System.out.flush();
            // Windows의 println은 \r\n을 쓰므로, 기대값의 \n과 비교할 수 있게 맞춘다.
            return bytes.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        } finally {
            TestUtil.clearSetOutToByteArray();
        }
    }

    public static String run(String input) {
        return run(input, scanner -> new App(scanner).run());
    }

    /**
     * 빌드 결과를 확인하는 테스트용: data.json을 지정한 경로(보통 임시 폴더)에 쓴다.
     */
    public static String run(String input, Path dataJsonPath) {
        return run(input, scanner -> new App(scanner, dataJsonPath).run());
    }
}
