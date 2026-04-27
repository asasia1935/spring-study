package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

// 생성자를 통해 DIP를 지킬 수 있게 됨 (memberRepository의 구현체와 discountPolicy의 구현체를 모름)
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId);
        // 할인 정책 적용 -> 주문 서비스는 할인 정책을 몰라도 할인이 되도록 설계 (단일 책임의 원칙)
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
