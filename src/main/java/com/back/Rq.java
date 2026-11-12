package com.back;

import java.util.HashMap;
import java.util.Map;

/**
 * 명령 한 줄(예: "삭제?id=1")을 명령 이름과 파라미터로 나눈다.
 * 스프링의 요청 객체(HttpServletRequest)에 해당한다.
 */
public class Rq {
    private final String actionName;
    private final Map<String, String> params = new HashMap<>();

    public Rq(String cmd) {
        String trimmed = cmd.trim();
        String[] cmdBits = trimmed.split("\\?", 2);

        actionName = cmdBits[0].trim();

        if (cmdBits.length < 2) return;

        for (String param : cmdBits[1].split("&")) {
            String[] paramBits = param.split("=", 2);
            if (paramBits.length < 2) continue;

            String key = paramBits[0].trim();
            String value = paramBits[1].trim();
            if (key.isEmpty()) continue;

            params.put(key, value);
        }
    }

    public String getActionName() {
        return actionName;
    }

    public String getParam(String name, String defaultValue) {
        return params.getOrDefault(name, defaultValue);
    }

    public int getParamAsInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(getParam(name, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
