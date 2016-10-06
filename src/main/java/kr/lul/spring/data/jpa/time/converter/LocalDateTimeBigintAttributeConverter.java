/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static kr.lul.common.util.Asserts.notNull;

import java.time.Instant;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import kr.lul.common.util.SystemTimeProvider;
import kr.lul.common.util.TimeProvider;

/**
 * @author Just Burrow
 * @since 2016. 8. 17.
 */
@Converter(autoApply = true)
public class LocalDateTimeBigintAttributeConverter implements AttributeConverter<LocalDateTime, Long> {
  private TimeProvider timeProvider;

  /**
   * 시스템 기본 설정을 사용한다.
   *
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  public LocalDateTimeBigintAttributeConverter() {
    this.timeProvider = new SystemTimeProvider();
  }

  /**
   * @author Just Burrow
   * @since 2016. 8. 17.
   * @param timeProvider
   */
  public LocalDateTimeBigintAttributeConverter(TimeProvider timeProvider) {
    notNull(timeProvider, "timeProvider");
    this.timeProvider = timeProvider;
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  public TimeProvider getTimeProvider() {
    return this.timeProvider;
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>AttributeConverter<LocalDateTime, Long>
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public Long convertToDatabaseColumn(LocalDateTime ldt) {
    return null == ldt
        ? null
        : ldt.atZone(this.timeProvider.zoneId()).toInstant().toEpochMilli();
  }

  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public LocalDateTime convertToEntityAttribute(Long utcMillis) {
    return null == utcMillis
        ? null
        : Instant.ofEpochMilli(utcMillis).atZone(this.timeProvider.zoneId()).toLocalDateTime();
  }
}
