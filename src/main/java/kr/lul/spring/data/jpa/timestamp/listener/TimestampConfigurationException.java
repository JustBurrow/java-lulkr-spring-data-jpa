package kr.lul.spring.data.jpa.timestamp.listener;

import kr.lul.spring.data.jpa.timestamp.PackageConstants;

/**
 * 엔티티의 타임스탬프 설정이 잘못된 경우.
 *
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public class TimestampConfigurationException extends RuntimeException {
  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  private static final long serialVersionUID = PackageConstants.SERIAL_VERSION_UID;

  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public TimestampConfigurationException() {
    super();
  }

  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   * @param message
   */
  public TimestampConfigurationException(String message) {
    super(message);
  }

  /**
   * @author Just Burrow
   * @since 2016. 8. 13.
   * @param cause
   */
  public TimestampConfigurationException(Throwable cause) {
    super(cause);
  }
}
