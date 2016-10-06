/**
 * @see https://github.com/JustBurrow/spring-data-jpa
 */
package kr.lul.spring.data.jpa.common.mapping;

import kr.lul.common.data.IntIdDomainObject;
import kr.lul.common.data.LongIdDomainObject;
import kr.lul.common.data.ObjectIdDomainObject;
import kr.lul.spring.data.jpa.timestamp.mapping.UpdatableMapping;

/**
 * {@link IntIdDomainObject}, {@link LongIdDomainObject}, {@link ObjectIdDomainObject}를 위한 기본 JPA 엔티티 매핑.
 * 복합 키를 가지는 {@link ObjectIdDomainObject}는 사용할 수 없다.
 *
 * @author Just Burrow
 * @since 2016. 9. 3.
 */
public abstract class IdMapping {
  public static abstract class IdEntity extends UpdatableMapping.UpdatableEntity {
    public static final String ID = "id";

    public IdEntity() {
      throw new UnsupportedOperationException();
    }
  }

  public static abstract class IdTable extends UpdatableMapping.UpdatableTable {
    public static final String PK = "pk";

    public IdTable() {
      throw new UnsupportedOperationException();
    }
  }

  public IdMapping() {
    throw new UnsupportedOperationException();
  }
}
