package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 명언 데이터의 생성·조회·수정·삭제만 담당한다. 이 단계의 저장소는 메모리 리스트다.
 * 콘솔 입출력 금지.
 */
public class WiseSayingRepository {
    private final List<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    public WiseSaying save(String content, String author) {
        WiseSaying wiseSaying = new WiseSaying(++lastId, content, author);
        wiseSayings.add(wiseSaying);
        return wiseSaying;
    }

    /**
     * 등록 순서(번호 오름차순) 그대로 돌려준다. 정렬 방향은 서비스가 정한다.
     */
    public List<WiseSaying> findAll() {
        return List.copyOf(wiseSayings);
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayings.stream()
                .filter(wiseSaying -> wiseSaying.getId() == id)
                .findFirst();
    }

    /**
     * 메모리 저장소에서는 엔티티가 이미 바뀌어 있으므로 할 일이 없다.
     * 파일·DB 저장소로 바꾸면 여기서 변경 내용을 기록한다.
     */
    public void update(WiseSaying wiseSaying) {
    }

    /**
     * @return 삭제했으면 true, 해당 번호가 없으면 false
     */
    public boolean deleteById(int id) {
        return wiseSayings.removeIf(wiseSaying -> wiseSaying.getId() == id);
    }
}
