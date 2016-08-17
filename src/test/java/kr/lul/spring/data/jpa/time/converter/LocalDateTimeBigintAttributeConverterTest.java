/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import kr.lul.util.SystemTimeProvider;
import kr.lul.util.TimeProvider;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class LocalDateTimeBigintAttributeConverterTest {
  private long                                  now;
  private LocalDateTimeBigintAttributeConverter converter;

  /**
   * @throws java.lang.Exception
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Before
  public void setUp() throws Exception {
    this.now = System.currentTimeMillis();
    TimeProvider timeProvider = new SystemTimeProvider();
    this.converter = new LocalDateTimeBigintAttributeConverter(timeProvider);

    assertThat(this.converter.getTimeProvider()).isNotNull()
        .isSameAs(timeProvider);
  }

  @Test
  public void testWithNow() throws Exception {
    // Given
    LocalDateTime ldt = LocalDateTime.now();

    // When
    final long utc = this.converter.convertToDatabaseColumn(ldt);
    final LocalDateTime actual = this.converter.convertToEntityAttribute(utc);

    // Then
    assertThat(utc).isGreaterThanOrEqualTo(this.now);
    assertThat(actual).isNotNull()
        .isNotSameAs(ldt)
        .isEqualTo(ldt);
  }

  @Test
  public void testWithNull() throws Exception {
    assertThat(this.converter.convertToEntityAttribute(null)).isNull();
    assertThat(this.converter.convertToDatabaseColumn(null)).isNull();
  }
}
