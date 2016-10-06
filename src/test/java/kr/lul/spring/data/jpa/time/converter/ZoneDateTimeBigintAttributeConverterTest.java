/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import kr.lul.common.util.SystemTimeProvider;
import kr.lul.common.util.TimeProvider;

/**
 * @author Just Burrow
 * @since 2016. 8. 18.
 */
public class ZoneDateTimeBigintAttributeConverterTest {
  @Test
  public void testConstructor() throws Exception {
    // When
    ZoneDateTimeBigintAttributeConverter converter = new ZoneDateTimeBigintAttributeConverter();

    // Then
    assertThat(converter.getTimeProvider()).isNotNull()
        .isInstanceOf(SystemTimeProvider.class);
  }

  @Test
  public void testConstructorWithTimeProvider() throws Exception {
    // Given
    final TimeProvider timeProvider = new TimeProvider() {
      @Override
      public ZoneId zoneId() {
        return null;
      }

      @Override
      public Instant now() {
        return null;
      }
    };

    // When
    ZoneDateTimeBigintAttributeConverter converter = new ZoneDateTimeBigintAttributeConverter(timeProvider);

    // Then
    assertThat(converter).isNotNull();
    assertThat(converter.getTimeProvider()).isNotNull()
        .isSameAs(timeProvider);
  }

  @Test
  public void testWithNow() throws Exception {
    // Given
    final ZoneDateTimeBigintAttributeConverter converter = new ZoneDateTimeBigintAttributeConverter();
    final ZonedDateTime zdt = ZonedDateTime.now();

    // When
    final long utc = converter.convertToDatabaseColumn(zdt);
    final ZonedDateTime actual = converter.convertToEntityAttribute(utc);

    // Then
    assertThat(utc).isEqualTo(zdt.toInstant().toEpochMilli());
    assertThat(actual).isNotNull()
        .isNotSameAs(zdt)
        .isEqualTo(zdt);
  }
}
