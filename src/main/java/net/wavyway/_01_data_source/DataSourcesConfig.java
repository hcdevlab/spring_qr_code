
package net.wavyway._01_data_source;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

@PropertySource("classpath:jdbc_01.properties")
@PropertySource("classpath:jdbc_02.properties")
@Component
public class DataSourcesConfig {

	@Resource
	private Environment env;

	private static final String PROPERTY_NAME_DATABASE_DRIVER = "jdbc_1.driverClassName";
	private static final String PROPERTY_NAME_DATABASE_URL = "jdbc_1.url";
	private static final String PROPERTY_NAME_DATABASE_USERNAME = "jdbc_1.username";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD = "jdbc_1.password";

	@Bean(name = "dataSource_1")
	public DataSource getDataSource_1() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

		return dataSource;
	}

	@Bean(name = "jdbcTemplate_1")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource_1") DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}

	private static final String PROPERTY_NAME_DATABASE_DRIVER_2 = "jdbc_2.driverClassName";
	private static final String PROPERTY_NAME_DATABASE_URL_2 = "jdbc_2.url";
	private static final String PROPERTY_NAME_DATABASE_USERNAME_2 = "jdbc_2.username";
	private static final String PROPERTY_NAME_DATABASE_PASSWORD_2 = "jdbc_2.password";

	@Bean(name = "dataSourceSecurity")
	public DataSource getDataSource_2() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER_2));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL_2));
		dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME_2));
		dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD_2));

		return dataSource;
	}
}
