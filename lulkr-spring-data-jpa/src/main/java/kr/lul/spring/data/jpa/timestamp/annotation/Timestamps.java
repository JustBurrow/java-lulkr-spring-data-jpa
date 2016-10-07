package kr.lul.spring.data.jpa.timestamp.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 하나의 엔티티가 여러개의 타임스탬프를 가지고 있을 때 클래스에 설정할 때 사용한다.
 * 상속받은 필드를 확장해 타임스탬프로 사용할 경우, 필드에 어노테이션 설정이 불가능하기 때문에 이때 사용한다.
 * 그 외의 경우에는 필드에 {@link Timestamp}를 사용하는 것을 권장.
 *
 * @author justburrow
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface Timestamps {
  /**
   * 각 타임스탬프의 정보.
   *
   * @return 타임스탬프 정보.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  Timestamp[] value();
}
