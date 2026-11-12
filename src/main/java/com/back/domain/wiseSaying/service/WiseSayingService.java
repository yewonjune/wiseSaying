package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;

/**
 * 명언 관련 순수 비즈니스 로직. 콘솔 입출력 금지.
 */
public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
    }

    public WiseSaying write(String content, String author) {
        return wiseSayingRepository.save(content, author);
    }

    /**
     * 최신 명언부터(번호 내림차순).
     */
    public List<WiseSaying> findForList() {
        return wiseSayingRepository.findAll().reversed();
    }

    /**
     * @return 삭제했으면 true, 해당 번호의 명언이 없으면 false
     */
    public boolean delete(int id) {
        return wiseSayingRepository.deleteById(id);
    }
}
