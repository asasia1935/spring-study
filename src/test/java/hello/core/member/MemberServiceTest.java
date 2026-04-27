package hello.core.member;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;

    // 테스트 전에 공통으로 실행되는 코드
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {
        // 테스트 환경에서 검증
        // given (어떤 환경이 주어졌을 때)
        Member member = new Member(1L, "memberA", Grade.VIP);

        // when (어떻게 했을 때)
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        // then (이렇게 된다)
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
