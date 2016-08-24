/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.mapping;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import kr.lul.spring.data.jpa.timestamp.annotation.Timestamp;
import kr.lul.spring.data.jpa.timestamp.annotation.Timestamps;

/**
 * 생성과 갱신 타임스탬프를 가진 엔티티의 기본적인 매핑 정보.
 *
 * @author Just Burrow
 * @since 2016. 8. 13.
 */
public abstract class UpdatableMapping {
  /**
   * 엔티티 타입의 설정 정보.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   * @see Timestamp
   * @see Timestamps
   */
  public abstract static class UpdatableEntity {
    /**
     * 데이터 생성 시각의 필드명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     */
    public static final String CREATE = "create";
    /**
     * 마지막 데이터 갱신 시각의 필드명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     * @see PreUpdate
     */
    public static final String UPDATE = "update";

    protected UpdatableEntity() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * 테이블의 설정 정보.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public abstract static class UpdatableTable {
    /**
     * 데이터 생성 시각을 저장할 컬럼명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     */
    public static final String CREATE_UTC = "create_utc";
    /**
     * 마지막 데이터 갱신 시각을 저장할 컬럼명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     * @see PreUpdate
     */
    public static final String UPDATE_UTC = "update_utc";

    protected UpdatableTable() {
      throw new UnsupportedOperationException();
    }
  }

  protected UpdatableMapping() {
    throw new UnsupportedOperationException();
  }
}
