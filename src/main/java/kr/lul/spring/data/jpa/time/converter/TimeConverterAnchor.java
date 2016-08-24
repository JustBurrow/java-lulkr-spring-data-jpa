/**
 * @see https://github.com/JustBurrow/java-lulkr-spring-data-jpa
 */
package kr.lul.spring.data.jpa.time.converter;

import kr.lul.util.Anchor;

/**
 * @author Just Burrow
 * @since 2016. 8. 17.
 */
public abstract class TimeConverterAnchor implements Anchor {
  /**
   * @author Just Burrow
   * @since 2016. 8. 25.
   */
  public static final Package PACKAGE      = TimeConverterAnchor.class.getPackage();
  /**
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  public static final String  PACKAGE_NAME = PACKAGE.getName();

  /**
   * @author Just Burrow
   * @since 2016. 8. 17.
   */
  protected TimeConverterAnchor() {
    throw new UnsupportedOperationException();
  }
}
