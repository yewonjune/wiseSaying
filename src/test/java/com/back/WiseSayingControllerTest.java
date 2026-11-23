package com.back;

import com.back.standard.util.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 명령어별 "입력 → 출력" 통합 테스트.
 *
 * <p>저장소 초기화: AppTestRunner.run()은 호출할 때마다 new App(scanner)를 만들고, App 생성자가
 * 새 WiseSayingRepository(빈 리스트, lastId = 0)를 만든다. 저장소에 static 상태가 없으므로
 * 각 테스트는 항상 빈 저장소·1번부터 시작한다.
 *
 * <p>순서 무관: 실행 순서를 매번 무작위로 섞어도(MethodOrderer.Random) 결과가 같아야 한다.
 */
@TestMethodOrder(MethodOrderer.Random.class)
class WiseSayingControllerTest {
    @AfterEach
    void restoreSystemOut() {
        // 테스트가 중간에 실패해도 다음 테스트에 출력 캡처가 남지 않게 한다.
        TestUtil.clearSetOutToByteArray();
    }

    @Test
    @DisplayName("종료")
    void t1() {
        String out = AppTestRunner.run("""
                종료
                """);

        assertThat(out)
                .contains("== 명언 앱 ==")
                .endsWith("명령) ");
    }

    @Test
    @DisplayName("등록 - 명언과 작가를 입력받는다")
    void t2() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        assertThat(out).contains("명언 :", "작가 :");
    }

    @Test
    @DisplayName("등록")
    void t3() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        assertThat(out).contains("명언 :", "작가 :", "1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록할 때마다 번호가 1씩 증가한다")
    void t4() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                종료
                """);

        assertThat(out).contains("1번 명언이 등록되었습니다.", "2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 - 번호 내림차순으로 번호 / 작가 / 명언을 출력한다")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                홍길동
                목록
                종료
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언", "----------------------")
                .containsSubsequence(
                        "2 / 홍길동 / 과거에 집착하지 마라.",
                        "1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("삭제 - 성공 메시지가 나오고 목록에서 사라진다")
    void t6() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                삭제?id=1
                목록
                종료
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("없는 번호 삭제")
    void t7() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                삭제?id=1
                삭제?id=1
                종료
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정 - 기존 값을 보여주고 새 값을 목록에 반영한다")
    void t8() {
        String out = AppTestRunner.run("""
                등록
                과거에 집착하지 마라.
                작자미상
                수정?id=1
                현재와 자신을 사랑하라.
                홍길동
                목록
                종료
                """);

        assertThat(out)
                .contains("명언(기존) : 과거에 집착하지 마라.", "작가(기존) : 작자미상")
                .contains("1 / 홍길동 / 현재와 자신을 사랑하라.")
                .doesNotContain("1 / 작자미상 / 과거에 집착하지 마라.");
    }

    @Test
    @DisplayName("없는 번호 수정 - 입력을 받지 않고 존재하지 않는다고 알려준다")
    void t9() {
        String out = AppTestRunner.run("""
                수정?id=1
                목록
                종료
                """);

        assertThat(out)
                .contains("1번 명언은 존재하지 않습니다.")
                .doesNotContain("명언(기존) :")
                .contains("번호 / 작가 / 명언");
    }

    @Test
    @DisplayName("테스트마다 저장소와 번호가 초기화된다")
    void t10() {
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                종료
                """);

        String out = AppTestRunner.run("""
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                종료
                """);

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .doesNotContain("현재를 사랑하라.");
    }
}
