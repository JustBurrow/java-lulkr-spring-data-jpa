package kr.lul.spring.data.jpa.timestamp.listener;

import static java.lang.String.format;
import static kr.lul.common.util.Asserts.notNull;
import static kr.lul.spring.data.jpa.timestamp.Trigger.POST_LOAD;
import static kr.lul.spring.data.jpa.timestamp.Trigger.PRE_PERSIST;
import static kr.lul.spring.data.jpa.timestamp.Trigger.PRE_UPDATE;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import kr.lul.common.util.MapBuilder;
import kr.lul.common.util.TimeProvider;
import kr.lul.spring.data.jpa.timestamp.Trigger;
import kr.lul.spring.data.jpa.timestamp.annotation.Timestamp;
import kr.lul.spring.data.jpa.timestamp.annotation.Timestamps;

/**
 * 어노테이션 기반의 JPA 2.1 규격의 엔티티에 {@link Timestamp}, {@link Timestamps}를 사용한 엔티티에 타임스탬프를 찍는 {@link EntityListeners}.
 * 상속을 사용한 경우, {@link MappedSuperclass}의 타임스탬프 속성을 찾아서 시각을 찍는다.
 *
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public class Timestamper {
  /**
   * 각 이벤트별로 분리한 처리 로직.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public class InternalHandler {
    /**
     * 핸들러가 처리할 JPA 콜백 이벤트 타입.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private Trigger                   event;

    /**
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    final private Object              excludeLock;
    /**
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private Set<Class<?>>             excludes;

    /**
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    final private Object              cacheLock;
    /**
     * {@link Map}&lt;<code>entity type, timestamp fields</code>%gt;
     */
    private Map<Class<?>, Set<Field>> cache;

    /**
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @param event
     */
    private InternalHandler(Trigger event) {
      notNull(event, "event");
      this.event = event;

      this.excludeLock = new Object();
      this.excludes = new HashSet<>();

      this.cacheLock = new Object();
      this.cache = new HashMap<>();
    }

    /**
     * 캐시에서 엔티티의 타임스탬프 대상 필드를 반환한다.
     *
     * @param entityType
     * @return
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private Set<Field> checkCache(Class<?> entityType) {
      synchronized (this.cacheLock) {
        return this.cache.containsKey(entityType)
            ? this.cache.get(entityType)
            : new HashSet<>();
      }
    }

    /**
     * 엔티티의 {@link Timestamps} 어노테이션을 분석해서 대상 필드를 찾는다.
     *
     * @param entityType
     *          엔티티 타입.
     * @param targetFields
     *          필드 반환용.
     * @throws TimestampConfigurationException
     *           <ul>
     *           <li>기본 필드 이름이 없는 설정법을 사용하면서 필드 이름을 지정하지 않은 경우.</li>
     *           <li>지정한 이름의 필드가 없는 경우.</li>
     *           </ul>
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private void checkTimestamps(final Class<?> entityType, final Set<Field> targetFields)
        throws TimestampConfigurationException {
      Class<?> target = entityType;
      do {
        if (null != target.getAnnotation(Entity.class) || null != target.getAnnotation(MappedSuperclass.class)) {
          final Timestamps timstamps = target.getAnnotation(Timestamps.class);
          if (null != timstamps) {
            for (Timestamp timestamp : timstamps.value()) {
              if (this.event != timestamp.trigger()) {
                continue;
              } else if ("".equals(timestamp.name())) {
                throw new TimestampConfigurationException(
                    format("%s(via %s) configuration for class[%s via %s] could not use default name.",
                        Timestamp.class.getSimpleName(), Timestamps.class.getSimpleName(), entityType, target));
              } else {
                try {
                  targetFields.add(target.getDeclaredField(timestamp.name()));
                } catch (NoSuchFieldException e) {
                  throw new TimestampConfigurationException(e);
                }
              }
            }
          }
        }
        target = target.getSuperclass();
      } while (Object.class != target);
    }

    /**
     * 엔티티 타입에 설정한 {@link Timestamp} 어노테이션을 분석해서 대상 필드를 찾는다.
     *
     * @param entityType
     *          엔티티 타입.
     * @param targetFields
     *          필드 반환용.
     * @throws TimestampConfigurationException
     *           <ul>
     *           <li>기본 필드 이름이 없는 설정법을 사용하면서 필드 이름을 지정하지 않은 경우.</li>
     *           <li>지정한 이름의 필드가 없는 경우.</li>
     *           </ul>
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private void checkTypeTimestamp(final Class<?> entityType, final Set<Field> targetFields)
        throws TimestampConfigurationException {
      Class<?> target = entityType;
      do {
        if (null != target.getAnnotation(Entity.class) || null != target.getAnnotation(MappedSuperclass.class)) {
          Timestamp timestamp = target.getAnnotation(Timestamp.class);
          if (null != timestamp && this.event == timestamp.trigger()) {
            if ("".equals(timestamp.name())) {
              throw new TimestampConfigurationException(
                  format("%s configuration for class[%s] could not use default name.",
                      Timestamp.class.getSimpleName(), entityType));
            } else {
              try {
                targetFields.add(target.getDeclaredField(timestamp.name()));
              } catch (NoSuchFieldException e) {
                throw new TimestampConfigurationException(e);
              }
            }
          }
        }
        target = target.getSuperclass();
      } while (Object.class != target);
    }

    /**
     * 필드에 설정한 {@link Timestamp} 어노테이션을 분석해서 대상 필드를 찾는다.
     *
     * @param entityType
     *          엔티티 타입.
     * @param targetFields
     *          필드 반환용.
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private void checkFieldTimestamp(final Class<?> entityType, final Set<Field> targetFields) {
      Class<?> target = entityType;
      do {
        if (null != target.getAnnotation(Entity.class) || null != target.getAnnotation(MappedSuperclass.class)) {
          for (Field field : target.getDeclaredFields()) {
            Timestamp timestamp = field.getAnnotation(Timestamp.class);
            if (null == timestamp || this.event != timestamp.trigger()) {
              continue;
            } else {
              targetFields.add(field);
            }
          }
        }
        target = target.getSuperclass();
      } while (Object.class != target);
    }

    /**
     * 캐시를 업데이트한다.
     *
     * @param entityType
     * @param targetFields
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private void updateCache(Class<?> entityType, Set<Field> targetFields) {
      synchronized (this.cacheLock) {
        final Set<Field> merged;
        if (this.cache.containsKey(entityType)) {
          merged = this.cache.get(entityType);
          merged.addAll(targetFields);
        } else {
          merged = new HashSet<>(targetFields);
        }
        this.cache.put(entityType, merged);
      }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 엔티티에 타임스탬프를 설정한다.
     *
     * @param entity
     * @author Just Burrow
     * @since 2016. 8. 13.
     */
    private void doTimestamp(final Object entity, final Instant now) {
      final Class<?> clz = entity.getClass();
      if (this.excludes.contains(clz)) {
        return;
      }

      Set<Field> targetFields = this.checkCache(clz);
      if (targetFields.isEmpty()) {
        this.checkTimestamps(clz, targetFields);
        this.checkTypeTimestamp(clz, targetFields);
        this.checkFieldTimestamp(clz, targetFields);

        if (targetFields.isEmpty()) {
          synchronized (this.excludeLock) {
            this.excludes.add(clz);
            return;
          }
        } else {
          this.updateCache(clz, targetFields);
        }
      }

      // timestamp
      for (Field f : targetFields) {
        boolean accessable = f.isAccessible();
        if (!accessable) {
          f.setAccessible(true);
        }

        try {
          f.set(entity, now);
        } catch (IllegalArgumentException | IllegalAccessException e) {
          throw new TimestampConfigurationException(e);
        }

        if (!accessable) {
          f.setAccessible(false);
        }
      }
    }
  }

  /**
   * 이벤트 종류별로 등록한 이벤트 리스너.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  private Map<Trigger, InternalHandler> handlerMap;
  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  private static TimeProvider           TIME_PROVIDER;

  public Timestamper() {
    this.handlerMap = MapBuilder.<Trigger, InternalHandler>hashmap()
        .put(PRE_PERSIST, new InternalHandler(PRE_PERSIST))
        .put(POST_LOAD, new InternalHandler(POST_LOAD))
        .put(PRE_UPDATE, new InternalHandler(PRE_UPDATE))
        .build();
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  protected Instant now() {
    return null == Timestamper.TIME_PROVIDER
        ? Instant.now()
        : Timestamper.TIME_PROVIDER.now();
  }

  /**
   * 인스턴스가 JPA 엔티티인지 확인한다.
   *
   * @param entity
   *          엔티티 인스턴스.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  private void isEntity(Object entity) {
    if (null == entity) {
      throw new IllegalArgumentException("entity is null.");
    } else if (null == entity.getClass().getAnnotation(Entity.class)) {
      throw new IllegalArgumentException(format("entity[%s] is not annotated %s.", entity.getClass(), Entity.class));
    }
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /**
   * <code>INSERT</code>를 실행하기 전 타임스탬프를 찍는다.
   *
   * @param entity
   *          엔티티 인스턴스.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  @PrePersist
  public void prePersist(Object entity) {
    this.isEntity(entity);
    Instant now = this.now();
    this.handlerMap.get(PRE_PERSIST)
        .doTimestamp(entity, now);
    this.handlerMap.get(PRE_UPDATE)
        .doTimestamp(entity, now);
  }

  /**
   * <code>SELECT</code>를 실행한 후 타임스탬프를 찍는다.
   *
   * @param entity
   *          엔티티 인스턴스.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  @PostLoad
  public void postLoad(Object entity) {
    this.isEntity(entity);
    this.handlerMap.get(POST_LOAD).doTimestamp(entity, this.now());
  }

  /**
   * <code>UPDATE</code>를 실행하기 전 타임스탬프를 찍는다.
   *
   * @param entity
   *          엔티티 인스턴스.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  @PreUpdate
  public void preUpdate(Object entity) {
    this.isEntity(entity);
    this.handlerMap.get(PRE_UPDATE).doTimestamp(entity, this.now());
  }

  /**
   * 타임스탬프에 사용할 시각 정보를 제공하는 {@link TimeProvider} 객체를 반환한다.
   *
   * @return {@link TimeProvider} 혹은 <code>null</code>.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public static TimeProvider getTimeProvider() {
    return TIME_PROVIDER;
  }

  /**
   * 타임스탬프에 사용할 시각 정보를 제공할 {@link TimeProvider} 객체를 지정한다.
   *
   * @param timeProvider
   *          not null.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public static void setTimeProvider(TimeProvider timeProvider) {
    notNull(timeProvider);
    Timestamper.TIME_PROVIDER = timeProvider;
  }
}
