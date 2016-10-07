/**
 */
package kr.lul.spring.data.jpa.time.converter;

import java.time.Instant;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * {@link Instant} 오브젝트를 <code>BIGINT(20)</code> 컬럼에 저장할 수 있도록 UTC milliseconds로 상호 변환.
 *
 * @author Just Burrow
 * @since 2016. 8. 17.
 */
@Converter(autoApply = true)
public class InstantBigintAttributeConverter implements AttributeConverter<Instant, Long> {
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>AttributeConverter<Instant, Long>
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public Long convertToDatabaseColumn(Instant instant) {
    return null == instant ? null : instant.toEpochMilli();
  }

  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  @Override
  public Instant convertToEntityAttribute(Long utcMillis) {
    return null == utcMillis ? null : Instant.ofEpochMilli(utcMillis);
  }
}
