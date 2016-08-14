/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.entity;

import java.time.Instant;

/**
 * 애플리케이션 로직에서 생성과 갱신이 가능한 엔티티.
 *
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public interface Updatable {
  /**
   * 엔티티 생성 시각.
   *
   * @return 엔티티 생성 시각.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public Instant getCreate();

  /**
   * 엔티티 저장 시각.
   *
   * @return 엔티티 저장 시각.
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public Instant getUpdate();
}
