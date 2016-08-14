/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.entity;

import static kr.lul.spring.data.jpa.timestamp.configuration.Trigger.PRE_PERSIST;
import static kr.lul.spring.data.jpa.timestamp.mapping.CreatablMapping.CreatableTable.CREATE_UTC;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import kr.lul.spring.data.jpa.timestamp.configuration.Timestamp;

/**
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
public abstract class AbstractCreatable implements Creatable {
  @Column(name = CREATE_UTC, nullable = false, updatable = false)
  @Timestamp(trigger = PRE_PERSIST)
  private Instant create;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>Creatable
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
    return String.format("create=%s", this.create);
  }
}
