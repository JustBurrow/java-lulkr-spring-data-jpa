/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.entity;

import static kr.lul.spring.data.jpa.timestamp.configuration.Trigger.PRE_UPDATE;
import static kr.lul.spring.data.jpa.timestamp.mapping.UpdatableMapping.UpdatableEntity.CREATE;
import static kr.lul.spring.data.jpa.timestamp.mapping.UpdatableMapping.UpdatableEntity.UPDATE;
import static kr.lul.spring.data.jpa.timestamp.mapping.UpdatableMapping.UpdatableTable.CREATE_UTC;
import static kr.lul.spring.data.jpa.timestamp.mapping.UpdatableMapping.UpdatableTable.UPDATE_UTC;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import kr.lul.spring.data.jpa.timestamp.configuration.Timestamp;

/**
 * 타임스탬프 필드와 메서드를 구현해둔 추상 클래스.
 * 이 클래스를 상속한 엔티티 클래스는 엔티티 리스너를 설정하는 것 만으로 타임스탬프를 사용할 수 있다.
 *
 * <pre>
 * <code>
 * &#64;javax.persistence.Entity
 * &#64;javax.persistence.EntityListeners({ kr.lul.spring.data.jpa.timestamp.Timestamper.class })
 * public class SomeEntity extends AbstractUpdatable {
 *   ...
 * }
 * </code>
 * </pre>
 *
 * @author just.burrow@lul.kr
 * @since 2016. 8. 3.
 */
@MappedSuperclass
public abstract class AbstractUpdatable implements Updatable {
  @Column(name = CREATE_UTC, nullable = false, updatable = false)
  @Timestamp(trigger = PRE_UPDATE, name = CREATE)
  private Instant create;
  @Column(name = UPDATE_UTC, nullable = false)
  @Timestamp(trigger = PRE_UPDATE, name = UPDATE)
  private Instant update;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>Updatable
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author justburrow
   * @since 2016. 8. 3.
   */
  @Override
  public Instant getCreate() {
    return this.create;
  }

  /*
   * (non-Javadoc)
   * @author justburrow
   * @since 2016. 8. 3.
   */
  @Override
  public Instant getUpdate() {
    return this.update;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Object
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author justburrow
   * @since 2016. 8. 3.
   */
  @Override
  public String toString() {
    return String.format("create=%s, update=%s", this.create, this.update);
  }
}
