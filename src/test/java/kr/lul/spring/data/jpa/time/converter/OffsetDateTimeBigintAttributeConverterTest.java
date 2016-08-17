/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

import org.junit.Before;
import org.junit.Test;

import kr.lul.util.SystemTimeProvider;
import kr.lul.util.TimeProvider;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class OffsetDateTimeBigintAttributeConverterTest {
  private long                                   now;
  private OffsetDateTimeBigintAttributeConverter converter;

  /**
   * @throws java.lang.Exception
   * @author Just Burrow
   * @since 2016. 8. 18.
   */
  @Before
  public void setUp() throws Exception {
    this.now = System.currentTimeMillis();
    TimeProvider timeProvider = new SystemTimeProvider();
    this.converter = new OffsetDateTimeBigintAttributeConverter(timeProvider);

    assertThat(this.converter.getTimeProvider()).isSameAs(timeProvider);
  }

  @Test
  public void testConstructor() throws Exception {
    assertThat(new OffsetDateTimeBigintAttributeConverter().getTimeProvider()).isNotNull();
  }

  @Test
  public void testWithNow() throws Exception {
    // Given
    final OffsetDateTime odt = OffsetDateTime.now();

    // When
    final long utc = this.converter.convertToDatabaseColumn(odt);
    final OffsetDateTime actual = this.converter.convertToEntityAttribute(utc);

    // Then
    assertThat(utc).isGreaterThanOrEqualTo(this.now);
    assertThat(actual).isNotNull()
        .isNotSameAs(odt)
        .isEqualTo(odt);
  }

  @Test
  public void testConvertWithNull() throws Exception {
    assertThat(this.converter.convertToEntityAttribute(null)).isNull();
    assertThat(this.converter.convertToDatabaseColumn(null)).isNull();
  }
}
