## 스프링 컨테이너 생성

스프링 컨테이너가 생성되는 과정을 알아보자.
```java
//스프링 컨테이너 생성
ApplicationContext applicationContext = 
                        new AnnotationConfigApplicationContext(AppConfig.class);
```

- `ApplicationContext`를 스프링 컨테이너라 한다.
- `ApplicationContext`는 인터페이스이다.
- 스프링 컨테이너는 XML을 기반으로 만들 수 있고, 애노테이션 기반의 자바 설정 클래스로 만들 수 있다.
- 직전에 `AppConfig`를 사용했던 방식이 애노테이션 기반의 자바 설정 클래스로 스프링 컨테이너를 만든 것이다.
- 자바 설정 클래스를 기반으로 스프링 컨테이너(`ApplicationContext`)를 만들어보자.
  - `new AnnotationConfigApplicationContext(AppConfig.class);`
  - 이 클래스는 `ApplicationContext` 인터페이스의 구현체이다.
> 참고: 더 정확히는 스프링 컨테이너를 부를 때 `BeanFactory`, `ApplicationContext`로 구분해서 이야기한다.
> 이 부분은 뒤에서 설명하겠다. `BeanFactory`를 직접 사용하는 경우는 거의 없으므로 일반적으로 `ApplicationContext`를 스프링 컨테이너라 한다.

## 스프링 컨테이너의 생성 과정

1. 스프링 컨테이너 생성 (AppConfig.class)
   - `new AnnotationConfigApplicationContext(AppConfig.class)`
   - 스프링 컨테이너 안에 스프링 빈 저장소 있음 (빈 이름과 빈 객체)
   - 스프링 컨테이너를 생성할 때 구성 정보 지정 -> `AppConfig.class` 라는 구성정보 활용
2. 스프링 빈 등록
    - 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록
    - AppConfig.class의 @Bean이 붙은 메서드 이름을 빈 이름(@Bean(name="~~")으로 직접 부여 가능)으로, 해당 반환 객체를 빈 객체로 설정
    - 빈의 이름은 항상 다른 이름을 부여해야 한다. (같은 이름을 부여하면 다른 빈이 무시되거나 설정에 따라 오류 발생)
3. 스프링 빈 의존관계 설정 - 준비
    - 스프링 컨테이너 안에 각 빈이 존재
4. 스프링 빈 의존관계 설정 - 완료
    - 컨테이너 안에서 각 빈이 의존관계 연결 (설정 정보를 참고해서 의존관계 주입 DI)
    - `public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }` (memberService -> memberRepository 의존관계)
    - 단순히 자바 코드를 호출하는 것 같지만, 차이가 있다. 이 차이는 뒤에 싱글톤 컨테이너에서 설명한다.