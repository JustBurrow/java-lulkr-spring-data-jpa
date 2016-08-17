/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import java.time.Period;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * {@link Period}를 <code>varchar</code> 컬럼에 저장, 조회할 수 있도록 변환한다.
 *
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
@Converter(autoApply = true)
public class PeriodVarcharAttributeConverter implements AttributeConverter<Period, String> {
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // <I>AttributeConverter<Period, String>
  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Override
  public String convertToDatabaseColumn(Period period) {
    return null == period
        ? null
        : period.toString();
  }

  /*
   * (non-Javadoc)
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Override
  public Period convertToEntityAttribute(String literal) {
    return null == literal
        ? null
        : Period.parse(literal);
  }
}
