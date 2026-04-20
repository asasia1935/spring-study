# 📌 주문과 할인 도메인 설계

## 1. 요구사항

### 주문과 할인 정책
- 회원은 상품을 주문할 수 있다.
- 회원 등급에 따라 할인 정책을 적용할 수 있다.
- 할인 정책은 모든 VIP에게 1,000원을 할인해주는 고정 금액 할인을 적용해달라. (나중에 변경 될 수 있다.)
- 할인 정책은 변경 가능성이 높다. 회사의 기본 할인정책을 아직 정하지 못했고, 오픈 직전까지 고민을 미루고 싶다. 최악의 경우 할인을 적용하지 않을 수 도 있다. (미확정)

## 2. 전체 흐름

클라이언트 -> 주문 서비스 -> 회원 저장소 -> (주문 서비스 반환) -> 할인 정책 -> 할인 적용 -> 주문 결과 반환

## 3. 설계 구조

### 3.1 서비스 계층

- `OrderService` (인터페이스)
- `OrderServiceImpl` (구현체)

### 3.2 저장소 계층

- `MemberRepository` (인터페이스)
- `MemoryMemberRepository` (메모리 구현)
- `DbMemberRepository` (DB 구현)

### 3.3 로직 계층

- `DiscountPolicy` (인터페이스)
- `FixDiscountPolicy` (고정 할인 정책)
- `RateDiscountPolicy` (비율 할인 정책)

## 4. 다이어그램

## 주문 도메인 협력, 역할, 책임

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant OrderService as 주문 서비스
    participant MemberRepo as 회원 저장소
    participant DiscountPolicy as 할인 정책

    Client->>OrderService: 주문 생성 요청 (회원ID, 상품명, 가격)
    OrderService->>MemberRepo: 회원 조회
    MemberRepo-->>OrderService: 회원 정보 반환
    OrderService->>DiscountPolicy: 할인 적용 요청
    DiscountPolicy-->>OrderService: 할인 금액 반환
    OrderService-->>Client: 주문 결과 반환
```

1. 주문 생성: 클라이언트는 주문 서비스에 주문 생성을 요청한다.
2. 회원 조회: 할인을 위해서는 회원 등급이 필요하다. 그래서 주문 서비스는 회원 저장소에서 회원을 조회한다.
3. 할인 적용: 주문 서비스는 회원 등급에 따른 할인 여부를 할인 정책에 위임한다.
4. 주문 결과 반환: 주문 서비스는 할인 결과를 포함한 주문 결과를 반환한다.

> 참고: 실제로는 주문 데이터를 DB에 저장하겠지만, 예제가 너무 복잡해 질 수 있어서 생략하고, 단순히 주문 결과를 반환한다.

## 주문 도메인 전체

```mermaid
graph LR

%% 클라이언트 → 주문 서비스
Client[클라이언트] -->|1. 주문 생성<br/>- 회원ID<br/>- 상품명<br/>- 상품 가격| OrderService[주문 서비스 역할]

%% 주문 결과 반환
OrderService -.->|return<br/>4. 주문 결과 반환| Client

%% 주문 서비스 구현체
OrderServiceImpl[주문 서비스 구현체]
OrderServiceImpl -.-> OrderService

%% 회원 조회 흐름
OrderService -->|2. 회원 조회| MemberRepository[회원 저장소 역할]

MemoryRepo[메모리 회원 저장소]
DbRepo[DB 회원 저장소]

MemoryRepo -.-> MemberRepository
DbRepo -.-> MemberRepository

%% 할인 적용 흐름
OrderService -->|3. 할인 적용| DiscountPolicy[할인 정책 역할]

FixPolicy[정액 할인 정책]
RatePolicy[정률 할인 정책]

FixPolicy -.-> DiscountPolicy
RatePolicy -.-> DiscountPolicy
```

**역할과 구현**을 분리해서 자유롭게 구현 객체를 조립할 수 있게 설계했다. 덕분에 회원 저장소는 물론이고, 할인 정책도 유연하게 변경할 수 있다.

## 주문 도메인 클래스 다이어그램

```mermaid
classDiagram

class OrderService {
    <<interface>>
}

class OrderServiceImpl

class MemberRepository {
    <<interface>>
}

class MemoryMemberRepository
class DbMemberRepository

class DiscountPolicy {
    <<interface>>
}

class FixDiscountPolicy
class RateDiscountPolicy

OrderService <|.. OrderServiceImpl

MemberRepository <|.. MemoryMemberRepository
MemberRepository <|.. DbMemberRepository

DiscountPolicy <|.. FixDiscountPolicy
DiscountPolicy <|.. RateDiscountPolicy

OrderServiceImpl --> MemberRepository
OrderServiceImpl --> DiscountPolicy
```

## 주문 도메인 객체 다이어그램1

```mermaid
graph LR

Client --> OrderServiceImpl

OrderServiceImpl --> MemoryMemberRepository
OrderServiceImpl --> FixDiscountPolicy
```

회원을 메모리에서 조회하고, 정액 할인 정책(고정 금액)을 지원해도 주문 서비스를 변경하지 않아도 된다.
역할들의 협력 관계를 그대로 재사용 할 수 있다.

## 주문 도메인 객체 다이어그램2

```mermaid
graph LR

Client --> OrderServiceImpl

OrderServiceImpl --> DbMemberRepository
OrderServiceImpl --> RateDiscountPolicy
```

회원을 메모리가 아닌 실제 DB에서 조회하고, 정률 할인 정책(주문 금액에 따라 % 할인)을 지원해도 주문 서비스를 변경하지 않아도 된다.
협력 관계를 그대로 재사용 할 수 있다.

