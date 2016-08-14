/**
 *
 */
package kr.lul.spring.data.jpa.timestamp.mapping;

import javax.persistence.PrePersist;

import kr.lul.spring.data.jpa.timestamp.configuration.Timestamps;

/**
 * 생성 타임스탬프를 가진 엔티티의 기본적인 매핑 정보.
 *
 * @author Just Burrow just.burrow@lul.kr
 * @since 2016. 4. 5.
 */
public abstract class CreatablMapping {
  /**
   * 엔티티 타입의 설정 정보.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   * @see Timestamp
   * @see Timestamps
   */
  public static abstract class CreatableEntity {
    /**
     * 데이터 생성 시각의 필드명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     */
    public static final String CREATE = "create";

    protected CreatableEntity() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * 테이블의 설정 정보.
   *
   * @author Just Burrow
   * @since 2016. 8. 13.
   */
  public static abstract class CreatableTable {
    /**
     * 데이터 생성 시각을 저장할 컬럼명.
     *
     * @author Just Burrow
     * @since 2016. 8. 13.
     * @see PrePersist
     */
    public static final String CREATE_UTC = "create_utc";

    protected CreatableTable() {
      throw new UnsupportedOperationException();
    }
  }

  protected CreatablMapping() {
    throw new UnsupportedOperationException();
  }
}
