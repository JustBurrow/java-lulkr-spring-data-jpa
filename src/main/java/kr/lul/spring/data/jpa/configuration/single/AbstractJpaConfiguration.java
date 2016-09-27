/**
 * @see https://github.com/JustBurrow/spring-data-jpa
 */
package kr.lul.spring.data.jpa.configuration.single;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Just Burrow
 * @since 2016. 9. 26.
 */
@Configuration
public abstract class AbstractJpaConfiguration {
  /**
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  public static final String BEAN_NAME_ENTITY_MANAGER_FACTORY = "entityManagerFactory";
  /**
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  public static final String BEAN_NAME_TRANSACTION_MANAGER    = "transactionManager";

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  protected abstract String[] getPackagesToScan();

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  @Bean(name = BEAN_NAME_ENTITY_MANAGER_FACTORY)
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setDatabase(Database.MYSQL);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setDataSource(this.dataSource());
    factory.setJpaVendorAdapter(adapter);
    factory.setPackagesToScan(this.getPackagesToScan());

    return factory;
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  @Bean(name = BEAN_NAME_TRANSACTION_MANAGER)
  public PlatformTransactionManager transactionManager() {
    return new JpaTransactionManager(this.entityManagerFactory().getObject());
  }

  /**
   * @return
   * @author Just Burrow
   * @since 2016. 9. 26.
   */
  @Bean
  public HibernateExceptionTranslator hibernateExceptionTranslator() {
    return new HibernateExceptionTranslator();
  }
}
