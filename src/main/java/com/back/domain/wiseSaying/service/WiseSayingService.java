package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 명언 관련 순수 비즈니스 로직. 콘솔 입출력 금지.
 * (빌드의 파일 쓰기는 콘솔 입출력이 아니므로 여기서 한다.)
 */
public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;
    private final Path dataJsonPath;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository, Path dataJsonPath) {
        this.wiseSayingRepository = wiseSayingRepository;
        this.dataJsonPath = dataJsonPath;
    }

    public WiseSaying write(String content, String author) {
        return wiseSayingRepository.save(content, author);
    }

    /**
     * 최신 명언부터(번호 내림차순).
     */
    public List<WiseSaying> findAllOrderByIdDesc() {
        return wiseSayingRepository.findAll().reversed();
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    public void modify(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.modify(content, author);
        wiseSayingRepository.update(wiseSaying);
    }

    /**
     * @return 삭제했으면 true, 해당 번호의 명언이 없으면 false
     */
    public boolean delete(int id) {
        return wiseSayingRepository.deleteById(id);
    }

    /**
     * 모든 명언을 번호 오름차순 JSON 배열로 data.json에 덮어쓴다.
     */
    public void build() {
        try {
            Files.writeString(dataJsonPath, toJson(wiseSayingRepository.findAll()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static String toJson(List<WiseSaying> wiseSayings) {
        if (wiseSayings.isEmpty()) return "[]";

        return wiseSayings.stream()
                .map(WiseSayingService::toJson)
                .collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

    private static String toJson(WiseSaying wiseSaying) {
        // 텍스트 블록은 공통 들여쓰기를 지우므로 2칸 들여쓰기를 직접 적는다.
        return ("  {\n"
                + "    \"id\": %d,\n"
                + "    \"content\": \"%s\",\n"
                + "    \"author\": \"%s\"\n"
                + "  }")
                .formatted(wiseSaying.getId(), escape(wiseSaying.getContent()), escape(wiseSaying.getAuthor()));
    }

    private static String escape(String value) {
        StringBuilder sb = new StringBuilder(value.length());

        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append("\\u%04x".formatted((int) c));
                    else sb.append(c);
                }
            }
        }

        return sb.toString();
    }
}
