# 05: 빌드 (data.json)

**What to build:** 사용자가 `빌드`를 입력하면 모든 명언이 data.json 파일로 내보내지고 `data.json 파일의 내용이 갱신되었습니다.`를 본다. 파일은 사람이 읽을 수 있는 JSON 배열이며 빌드할 때마다 최신 상태로 통째로 덮어쓰인다.

부모 스펙: `.scratch/wise-saying-console-app/spec.md`

**Blocked by:** 02 (명언 등록과 목록)

**Status:** ready-for-agent

- [ ] data.json은 번호 오름차순 JSON 배열, 2칸 들여쓰기, 원소 키 순서 `id`, `content`, `author`, UTF-8이다
- [ ] 명언이 없을 때 빌드하면 빈 배열이 저장된다
- [ ] 내용·작가의 `"`, `\`, 줄바꿈이 이스케이프되어 올바른 JSON이 된다
- [ ] 여러 번 빌드하면 매번 최신 상태로 덮어쓴다
- [ ] JSON은 라이브러리 없이 서비스에서 만들고 파일도 서비스가 쓴다; 컨트롤러는 완료 메시지만 출력한다
- [ ] 위 동작을 `@TempDir` 경로를 넘긴 App 테스트로 파일 내용까지 검증한다
