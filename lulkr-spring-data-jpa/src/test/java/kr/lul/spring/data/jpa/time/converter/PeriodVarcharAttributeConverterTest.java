/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Period;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class PeriodVarcharAttributeConverterTest {
  private PeriodVarcharAttributeConverter converter;

  @Before
  public void setUp() throws Exception {
    this.converter = new PeriodVarcharAttributeConverter();
  }

  @Test
  public void testWith1Day() throws Exception {
    // Given
    final Period expected = Period.ofDays(1);

    // When
    final String literal = this.converter.convertToDatabaseColumn(expected);
    final Period actual = this.converter.convertToEntityAttribute(literal);

    // Then
    assertThat(literal).isNotNull()
        .isNotEmpty();
    assertThat(actual).isNotNull()
        .isEqualTo(expected)
        .isNotSameAs(expected);
  }

  @Test
  public void testWithNull() throws Exception {
    assertThat(this.converter.convertToDatabaseColumn(null)).isNull();
    assertThat(this.converter.convertToEntityAttribute(null)).isNull();
  }
}
