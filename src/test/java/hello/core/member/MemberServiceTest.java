package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();

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
