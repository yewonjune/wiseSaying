package com.back;

import com.back.standard.util.TestUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * 문자열 입력으로 실제 App을 실행하고 콘솔 출력을 문자열로 돌려준다.
 * 매번 새 App(새 저장소)을 만들므로 테스트마다 저장소와 번호가 초기화된다.
 */
public final class AppTestRunner {
    public static String run(String input, Consumer<Scanner> runApp) {
        var bytes = TestUtil.setOutToByteArray();

        try (Scanner scanner = TestUtil.genScanner(input)) {
            runApp.accept(scanner);
            System.out.flush();
            return bytes.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
        } finally {
            TestUtil.clearSetOutToByteArray();
        }
    }

    public static String run(String input) {
        return run(input, scanner -> new App(scanner).run());
    }
}
