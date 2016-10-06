/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static kr.lul.common.util.Asserts.notNull;

import java.time.Instant;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import kr.lul.common.util.SystemTimeProvider;
import kr.lul.common.util.TimeProvider;

/**
 * @author Just Burrow
 * @since 2016. 8. 17.
 * @see TimeProvider
 */
@Converter(autoApply = true)
public class ZoneDateTimeBigintAttributeConverter implements AttributeConverter<ZonedDateTime, Long> {
  private TimeProvider timeProvider;

  /**
   * 타임존 정보를 시스템 기본 정보를 사용한다.
   *
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  public ZoneDateTimeBigintAttributeConverter() {
    this.timeProvider = new SystemTimeProvider();
  }

  /**
   * @author Just Burrow
   * @since 2016. 8. 17.
   * @param timeProvider
   */
  public ZoneDateTimeBigintAttributeConverter(TimeProvider timeProvider) {
    notNull(timeProvider, "timeProvider");
    this.timeProvider = timeProvider;
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  public TimeProvider getTimeProvider() {
    return this.timeProvider;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>AttributeConverter<ZonedDateTime, Long>
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public Long convertToDatabaseColumn(ZonedDateTime zdt) {
    return null == zdt ? null : zdt.toInstant().toEpochMilli();
  }

  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public ZonedDateTime convertToEntityAttribute(Long utcMillis) {
    return null == utcMillis ? null : Instant.ofEpochMilli(utcMillis).atZone(this.timeProvider.zoneId());
  }
}
