/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class InstantBigintAttributeConverterTest {
  private long                            now;
  private InstantBigintAttributeConverter converter;

  /**
   * @throws java.lang.Exception
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Before
  public void setUp() throws Exception {
    this.now = System.currentTimeMillis();
    this.converter = new InstantBigintAttributeConverter();
  }

  @Test
  public void testWithNow() throws Exception {
    // Given
    Instant instant = Instant.now();

    // When
    long utc = this.converter.convertToDatabaseColumn(instant);
    Instant actual = this.converter.convertToEntityAttribute(utc);

    // Then
    assertThat(utc).isGreaterThanOrEqualTo(this.now);
    assertThat(actual).isNotNull()
        .isNotSameAs(instant)
        .isEqualTo(instant);
  }

  @Test
  public void testWithNull() throws Exception {
    assertThat(this.converter.convertToDatabaseColumn(null)).isNull();
    assertThat(this.converter.convertToEntityAttribute(null)).isNull();
  }
}
