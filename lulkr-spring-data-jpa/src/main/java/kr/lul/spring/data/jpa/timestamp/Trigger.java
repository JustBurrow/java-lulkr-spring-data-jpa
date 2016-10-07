/**
 *
 */
package kr.lul.spring.data.jpa.timestamp;

import java.lang.annotation.Annotation;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * 타임스탬프를 지원하는 JPA 콜백 이벤트 정보.
 * 모든 콜백 이벤트를 지원하지 않기 때문에 별도의 <code>enum</code>으로 이벤트 목록을 관리한다.
 *
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public enum Trigger {
  PRE_PERSIST(PrePersist.class),
  POST_LOAD(PostLoad.class),
  PRE_UPDATE(PreUpdate.class);

  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public final Class<? extends Annotation> eventType;

  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  private Trigger(Class<? extends Annotation> eventType) {
    if (null == eventType) {
      throw new NullPointerException("eventType");
    }
    this.eventType = eventType;
  }
}
