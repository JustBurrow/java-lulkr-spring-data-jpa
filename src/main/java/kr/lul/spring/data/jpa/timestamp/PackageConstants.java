package kr.lul.spring.data.jpa.timestamp;

import kr.lul.spring.data.jpa.ModuleConstants;

/**
 * 타임스탬프 패키지의 공통 상수 모음.
 *
 * @author justburrow
 * @since 2016. 8. 3.
 */
public abstract class PackageConstants {
  public static final long SERIAL_VERSION_UID = ModuleConstants.SERIAL_VERSION_UID;

  private PackageConstants() {
    throw new UnsupportedOperationException();
  }
}
