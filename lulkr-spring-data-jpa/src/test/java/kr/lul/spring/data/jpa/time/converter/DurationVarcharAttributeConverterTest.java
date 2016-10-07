/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class DurationVarcharAttributeConverterTest {
  private DurationVarcharAttributeConverter converter;

  /**
   * @throws java.lang.Exception
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Before
  public void setUp() throws Exception {
    this.converter = new DurationVarcharAttributeConverter();
  }

  @Test
  public void testWith1Second() throws Exception {
    // Given
    Duration duration = Duration.ofSeconds(1L);

    // When
    String literal = this.converter.convertToDatabaseColumn(duration);
    Duration actual = this.converter.convertToEntityAttribute(literal);

    // Then
    assertThat(literal).isNotNull()
        .isNotEmpty();
    assertThat(actual).isNotNull()
        .isNotSameAs(duration)
        .isEqualTo(duration);
  }

  @Test
  public void testWithNull() throws Exception {
    assertThat(this.converter.convertToDatabaseColumn(null)).isNull();
    assertThat(this.converter.convertToEntityAttribute(null)).isNull();
  }
}
