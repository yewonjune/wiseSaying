# 01: 프로젝트 뼈대 — 시작과 종료

**What to build:** 사용자가 앱을 실행하면 `== 명언 앱 ==` 제목과 `명령) ` 프롬프트를 보고, `종료`를 입력하면 추가 메시지 없이 앱이 끝난다. 알 수 없는 명령에는 `알 수 없는 명령입니다.`가 출력되고 다시 명령을 입력할 수 있다. 이후 티켓들이 기댈 프로젝트 골격(Gradle Kotlin DSL, Java 25, JUnit 5 + AssertJ), 명령 파싱(Rq), App 테스트 경계를 함께 마련한다.

부모 스펙: `.scratch/wise-saying-console-app/spec.md`

**Blocked by:** None (can start immediately)

**Status:** done

- [x] Gradle(Kotlin DSL) 프로젝트, Java 25 툴체인, JUnit 5·AssertJ 테스트 의존성이 설정되어 명령줄과 IntelliJ에서 테스트가 돈다
- [x] `git init` 되어 있고 `.gitignore`에 Gradle 산출물과 `data.json`이 포함된다
- [x] Main은 App을 표준 입력 Scanner와 실행 위치의 `data.json` 경로로 생성·실행만 한다
- [x] App 생성자는 Scanner와 data.json 경로를 받는다
- [x] 시작 시 `== 명언 앱 ==`, 매 입력 전 `명령) `가 출력된다
- [x] `종료` 입력 시 SystemController가 처리하고 메시지 없이 루프가 끝난다
- [x] 알 수 없는 명령에 `알 수 없는 명령입니다.`를 출력하고 루프가 계속된다
- [x] Rq가 명령 이름(`?` 앞부분)과 정수 파라미터(`&`로 여러 개, 없거나 숫자가 아니면 기본값)를 제공한다 (명령 이름은 App 테스트로 검증; 정수 파라미터는 03·04 티켓의 App 테스트에서 검증)
- [x] App 테스트 틀: 문자열 입력 Scanner 주입, `System.out` 가로채기, `@TempDir` data.json 경로 — 위 동작을 테스트로 검증한다
