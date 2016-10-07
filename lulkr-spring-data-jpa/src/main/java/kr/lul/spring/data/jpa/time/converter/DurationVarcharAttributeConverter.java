/**
 */
package kr.lul.spring.data.jpa.time.converter;

import java.time.Duration;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * {@link Duration}을 <code>varchar</code> 컬럼에 저장, 조회할 수 있도록 변환한다.
 *
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
@Converter(autoApply = true)
public class DurationVarcharAttributeConverter implements AttributeConverter<Duration, String> {
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>AttributeConverter<Duration, String>
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Override
  public String convertToDatabaseColumn(Duration duration) {
    return null == duration
        ? null
        : duration.toString();
  }

  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Override
  public Duration convertToEntityAttribute(String literal) {
    return null == literal
        ? null
        : Duration.parse(literal);
  }
}
