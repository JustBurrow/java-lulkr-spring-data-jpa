/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import kr.lul.spring.data.jpa.timestamp.Trigger;

/**
 * 엔티티가 언제 타임스탬프를 찍는지 설정한다.
 * 상속받은 필드를 확장해 타임스탬프로 사용할 경우, 필드에 어노테이션 설정이 불가능하기 때문에 이때 클래스에 사용한다.
 * 보통은 필드에 직접 설정하는 것을 권장.
 *
 * @author justburrow
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
@Target({ TYPE, FIELD })
@Retention(RUNTIME)
public @interface Timestamp {
  /**
   * 타임스탬프를 찍어야 하는 JPA 콜백 이벤트 정보.
   *
   * @return 타임스탬프 트리거.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  Trigger trigger();

  /**
   * (Optional)타임스탬프를 찍을 필드의 이름.
   * 기본값은 필드에 설정한 경우에만 사용할 수 있다.
   *
   * @return 대상 속성 이름.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  String name() default "";
}
