package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

public class AppConfig {
    // 애플리케이션에 대한 환경설정 구성을 여기서 진행

    // 생성자 주입 할 때 기존 코드에서 중복 발생 -> 리팩토링 하여 해결
    // 또한 역할과 구현 클래스가 한눈에 보임 -> 멤버 리포지토리를 넣는데 걔는 메모리구나
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        // 생성자 주입
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // 이 부분만 수정하면 모든 멤버 리포지토리를 주입하는 부분을 바꿀 수 있음
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
    }
}
