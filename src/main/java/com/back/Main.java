package com.back;

import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 콘솔 인코딩(예: Windows 터미널의 MS949)에 맞춰 입력을 읽어야 한글이 깨지지 않는다.
        Scanner scanner = new Scanner(System.in, consoleCharset());
        new App(scanner).run();
        scanner.close();
    }

    /**
     * stdin.encoding은 Java 25부터 있으므로, 없으면 콘솔 출력 인코딩(stdout.encoding, Java 19+)을 쓰고
     * 그것도 없으면 기본 인코딩을 쓴다.
     */
    private static Charset consoleCharset() {
        for (String key : new String[]{"stdin.encoding", "stdout.encoding"}) {
            String name = System.getProperty(key);
            if (name != null) return Charset.forName(name, Charset.defaultCharset());
        }

        return Charset.defaultCharset();
    }
}
