package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // 새로운 할인 정책을 적용하려면 기존 코드 대신 아래 코드를 사용하면 됨 -> 즉 OrderServiceImpl 코드를 고쳐야 함
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId);
        // 할인 정책 적용 -> 주문 서비스는 할인 정책을 몰라도 할인이 되도록 설계 (단일 책임의 원칙)
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
