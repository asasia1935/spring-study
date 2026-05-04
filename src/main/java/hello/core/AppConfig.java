package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration : 설정 정보 (애플리케이션의 구성 정보를 어떻게 구성하는지 구성)
// ApplicationContext -> 스프링 컨테이너
// @Bean : 해당 어노테이션이 붙은 메서드를 호출해서 반환된 객체를 스프링 컨테이너에 등록
// 빈은 @Bean이 붙은 메서드 명을 스프링의 빈 이름으로 사용 (ex) memberService)
// ApplicationContext의 getBean 메서드를 이용하여 찾을 수 있음

@Configuration
public class AppConfig {
    // 애플리케이션에 대한 환경설정 구성을 여기서 진행

    // 생성자 주입 할 때 기존 코드에서 중복 발생 -> 리팩토링 하여 해결
    // 또한 역할과 구현 클래스가 한눈에 보임 -> 멤버 리포지토리를 넣는데 걔는 메모리구나
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        // 생성자 주입
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    // 이 부분만 수정하면 모든 멤버 리포지토리를 주입하는 부분을 바꿀 수 있음
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // 할인 정책 변경 -> 객체 변경
        // (구성을 담당하는 AppConfig만 고치면 되고,
        // 클라이언트 코드인 OrderServiceImpl를 포함한 어떤 코드도 바꾸지 않고 변경함)
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
