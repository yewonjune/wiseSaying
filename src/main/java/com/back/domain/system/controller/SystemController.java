package com.back.domain.system.controller;

public class SystemController {
    /**
     * 종료는 별도 메시지 없이 끝낸다. 종료 시 정리할 일이 생기면 여기에 둔다.
     *
     * @return 앱을 계속 실행할지 여부. 종료 명령이므로 항상 false
     */
    public boolean actionExit() {
        return false;
    }
}
