/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.listener;

import static kr.lul.common.util.RandomUtil.R;
import static kr.lul.spring.data.jpa.timestamp.Trigger.POST_LOAD;
import static kr.lul.spring.data.jpa.timestamp.Trigger.PRE_PERSIST;
import static kr.lul.spring.data.jpa.timestamp.Trigger.PRE_UPDATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;

import kr.lul.common.data.Updatable;
import kr.lul.spring.data.jpa.timestamp.annotation.Timestamp;
import kr.lul.spring.data.jpa.timestamp.annotation.Timestamps;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public class TimestamperTest {
  @Entity
  @Timestamps({ @Timestamp(trigger = PRE_PERSIST), @Timestamp(trigger = PRE_UPDATE),
      @Timestamp(trigger = POST_LOAD) })
  @Data
  class TimestampsAsDefaultName {
    private Instant create;
    private Instant read;
    private Instant update;
  }

  @Entity
  @Timestamp(trigger = PRE_PERSIST)
  class TimestampPrePersistAsDefaultName {
    @SuppressWarnings("unused")
    private Instant create;
  }

  @Entity
  @Timestamp(trigger = POST_LOAD)
  class TimestampPostLoadAsDefaultName {
    @SuppressWarnings("unused")
    private Instant read;
  }

  @Entity
  @Timestamp(trigger = PRE_UPDATE)
  class TimestampPreUpdateAsDefaultName {
    @SuppressWarnings("unused")
    private Instant update;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  private Instant     now;
  private Timestamper listener;

  @Before
  public void setUp() throws Exception {
    this.now = Instant.now();
    this.listener = new Timestamper();
  }

  private void assertExceptions(Class<? extends Throwable> exception, ThrowingCallable... callbacks) {
    assertThat(exception).isNotNull();
    assertThat(callbacks).isNotNull();

    for (ThrowingCallable callback : callbacks) {
      assertThatThrownBy(callback)
          .isInstanceOf(exception);
    }
  }

  @Test
  public void testWithNull() throws Exception {
    this.assertExceptions(IllegalArgumentException.class,
        () -> this.listener.prePersist(null),
        () -> this.listener.preUpdate(null),
        () -> this.listener.postLoad(null));
  }

  @Test
  public void testWithTimestampsDefaultName() throws Exception {
    this.assertExceptions(TimestampConfigurationException.class,
        () -> this.listener.prePersist(new TimestampsAsDefaultName()),
        () -> this.listener.postLoad(new TimestampsAsDefaultName()),
        () -> this.listener.preUpdate(new TimestampsAsDefaultName()));
  }

  @Test
  public void testWithTimestampDefaultName() throws Exception {
    this.assertExceptions(TimestampConfigurationException.class,
        () -> this.listener.prePersist(new TimestampPrePersistAsDefaultName()),
        () -> this.listener.postLoad(new TimestampPostLoadAsDefaultName()),
        () -> this.listener.preUpdate(new TimestampPreUpdateAsDefaultName()));
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @MappedSuperclass
  class FieldCreateUpdateSuperclass implements Updatable {
    @Timestamp(trigger = PRE_PERSIST)
    private Instant create;
    @Timestamp(trigger = PRE_UPDATE)
    private Instant update;

    @Override
    public Instant getCreate() {
      return this.create;
    }

    @Override
    public Instant getUpdate() {
      return this.update;
    }
  }

  @Entity
  class InheritFieldEntity extends FieldCreateUpdateSuperclass {
  }

  @Test
  public void testWithInheritFieldEntity() throws Exception {
    // Given
    final InheritFieldEntity entity = new InheritFieldEntity();
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    // When & Then
    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    final Instant update = entity.getUpdate();
    assertThat(create).isEqualTo(update).isGreaterThanOrEqualTo(this.now);

    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getUpdate()).isEqualTo(update);

    Thread.sleep(R.in(500L, 1000L));

    this.listener.preUpdate(entity);
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getUpdate()).isGreaterThan(update);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @MappedSuperclass
  @Timestamp(trigger = PRE_PERSIST, name = "create")
  @Data
  class TypeCreateSuperclass {
    private Instant create;
  }

  @Entity
  @Timestamp(trigger = PRE_UPDATE, name = "update")
  @Data
  @EqualsAndHashCode(callSuper = true)
  class InheritCreateUpdateEntity extends TypeCreateSuperclass {
    private Instant update;
  }

  @Entity
  @Timestamp(trigger = PRE_UPDATE, name = "update")
  @Data
  @EqualsAndHashCode(callSuper = true)
  class ComplexEntity extends TypeCreateSuperclass {
    @Timestamp(trigger = POST_LOAD)
    private Instant read;
    private Instant update;
  }

  @Test
  public void testWithInheritCreateUpdateEntity() throws Exception {
    // Given
    final InheritCreateUpdateEntity entity = new InheritCreateUpdateEntity();
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    // When & Then
    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    final Instant update = entity.getUpdate();
    assertThat(create).isEqualTo(update).isGreaterThanOrEqualTo(this.now);

    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getUpdate()).isEqualTo(update);
    Thread.sleep(R.in(500L, 1000L));

    this.listener.preUpdate(entity);
    final Instant update2 = entity.getUpdate();
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(update2).isGreaterThan(update);

    for (int i = R.in(5, 10); i > 0; i--) {
      Thread.sleep(10L);
      assertThat(entity.getCreate()).isEqualTo(create);
      assertThat(entity.getUpdate()).isEqualTo(update2);
    }
  }

  @Test
  public void testWithComplexEntity() throws Exception {
    // Given
    final ComplexEntity entity = new ComplexEntity();
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getRead()).isNull();
    assertThat(entity.getUpdate()).isNull();

    // When & Then
    this.listener.postLoad(entity);
    final Instant read = entity.getRead();
    assertThat(entity.getCreate()).isNull();
    assertThat(read).isGreaterThanOrEqualTo(this.now);
    assertThat(entity.getUpdate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    Instant update = entity.getUpdate();

    assertThat(create).isEqualTo(update).isGreaterThanOrEqualTo(this.now);
    assertThat(entity.getRead()).isEqualTo(read);

    Thread.sleep(R.in(500L, 1000L));

    this.listener.postLoad(entity);
    final Instant read2 = entity.getRead();
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(read2).isGreaterThanOrEqualTo(read);
    assertThat(entity.getUpdate()).isEqualTo(update);

    Thread.sleep(R.in(500L, 1000L));

    this.listener.preUpdate(entity);
    final Instant update2 = entity.getUpdate();

    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getRead()).isEqualTo(read2);
    assertThat(update2).isGreaterThan(update);

    Thread.sleep(10L);

    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getRead()).isEqualTo(read2);
    assertThat(entity.getUpdate()).isEqualTo(update2);
  }

  @Data
  class NonEntity {
    @Timestamp(trigger = PRE_PERSIST)
    private Instant aaa;
  }

  @Entity
  @Data
  class NoTimestampEntity {
    private Instant bbb;
  }

  @Entity
  @Timestamp(trigger = PRE_PERSIST, name = "create")
  @Data
  class TypeCreateEntity {
    private Instant create;
  }

  @Entity
  @Timestamps({ @Timestamp(trigger = PRE_PERSIST, name = "create"),
      @Timestamp(trigger = PRE_UPDATE, name = "update") })
  @Data
  class TypeCreateUpdateEntity {
    private Instant create;
    private Instant update;
  }

  @Entity
  @Data
  class FieldCreateUpdateEntity {
    @Timestamp(trigger = PRE_PERSIST)
    private Instant create;
    @Timestamp(trigger = PRE_UPDATE)
    private Instant update;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  @Test
  public void testWithNonEntity() throws Exception {
    // Given
    final NonEntity entity = new NonEntity();
    assertThat(entity.getAaa()).isNull();

    // When & Then
    assertThatThrownBy(() -> this.listener.prePersist(entity)).isInstanceOf(IllegalArgumentException.class);
    ;
    assertThat(entity.getAaa()).isNull();

    assertThatThrownBy(() -> this.listener.preUpdate(entity)).isInstanceOf(IllegalArgumentException.class);
    assertThat(entity.getAaa()).isNull();

    assertThatThrownBy(() -> this.listener.postLoad(entity)).isInstanceOf(IllegalArgumentException.class);
    assertThat(entity.getAaa()).isNull();
  }

  @Test
  public void testWithNoTimestampEntity() throws Exception {
    // Given
    final NoTimestampEntity entity = new NoTimestampEntity();
    assertThat(entity.getBbb()).isNull();

    // When & Then
    this.listener.prePersist(entity);
    assertThat(entity.getBbb()).isNull();

    this.listener.preUpdate(entity);
    assertThat(entity.getBbb()).isNull();

    this.listener.postLoad(entity);
    assertThat(entity.getBbb()).isNull();

    for (int i = R.in(3, 10); i > 0; i--) {
      Thread.sleep(10L);
      assertThat(entity.getBbb()).isNull();
    }
  }

  @Test
  public void testWithTypeCreateEntity() throws Exception {
    // Given
    final TypeCreateEntity entity = new TypeCreateEntity();
    assertThat(entity.getCreate()).isNull();

    // When & Then
    this.listener.preUpdate(entity);
    assertThat(entity.getCreate()).isNull();

    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    assertThat(create).isGreaterThanOrEqualTo(this.now);
    Thread.sleep(10L);
    assertThat(entity.getCreate()).isEqualTo(create);
  }

  @Test
  public void testWithTypeCreateUpdateEntity() throws Exception {
    // Given
    final TypeCreateUpdateEntity entity = new TypeCreateUpdateEntity();
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    // When & Then
    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    final Instant update = entity.getUpdate();
    assertThat(create).isGreaterThanOrEqualTo(this.now).isEqualTo(update).isEqualTo(entity.getCreate())
        .isEqualTo(entity.getUpdate());

    Thread.sleep(R.in(500L, 1000L));

    this.listener.preUpdate(entity);
    Instant update2 = entity.getUpdate();
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(update2).isGreaterThan(create).isGreaterThan(update);

    Thread.sleep(10L);
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getUpdate()).isEqualTo(update2);
  }

  @Test
  public void testFieldCreateUpdateEntity() throws Exception {
    // Given
    final FieldCreateUpdateEntity entity = new FieldCreateUpdateEntity();
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    // When & Then
    this.listener.postLoad(entity);
    assertThat(entity.getCreate()).isNull();
    assertThat(entity.getUpdate()).isNull();

    this.listener.prePersist(entity);
    final Instant create = entity.getCreate();
    final Instant update = entity.getUpdate();
    assertThat(create).isGreaterThanOrEqualTo(this.now).isEqualTo(update);

    Thread.sleep(R.in(500L, 1000L));

    this.listener.preUpdate(entity);
    Instant update2 = entity.getUpdate();
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(update2).isGreaterThan(create).isGreaterThan(update);

    Thread.sleep(10L);
    assertThat(entity.getCreate()).isEqualTo(create);
    assertThat(entity.getUpdate()).isEqualTo(update2);
  }
}
