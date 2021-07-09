package net.wavyway._02_security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Service("userBinder")
public class UserBinder implements IUserBinder {

	private JdbcTemplate jdbcTemplateSecurity;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplateSecurity;

	public UserBinder(@Autowired @Qualifier("dataSourceSecurity") DataSource dataSource) {
		jdbcTemplateSecurity = new JdbcTemplate(dataSource);
		namedParameterJdbcTemplateSecurity = new NamedParameterJdbcTemplate(jdbcTemplateSecurity);
	}

	@Override
	public UserData getUserData(String userName) {
		try {

			String sql =  "SELECT u.username username_value, u.password password_value, a.authority role_value " +
						  "FROM users u " +
						  "INNER JOIN authorities a ON (u.username=a.username) " +
						  "WHERE (u.enabled=true) AND (u.username=:name)";
			MapSqlParameterSource msps = new MapSqlParameterSource();
			msps.addValue("name", userName, Types.VARCHAR);

			RowMapper<UserData> rowMapper = new RowMapper<UserData>() {
				@Override
				public UserData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					UserData userData = new UserData(
							resultSet.getString("username_value"),
							resultSet.getString("password_value"),
							resultSet.getString("role_value")
					);

					return userData;
				}
			};

			UserData userData = namedParameterJdbcTemplateSecurity.queryForObject(sql, msps, rowMapper);
			return userData;

		} catch (EmptyResultDataAccessException exception) {
			exception.printStackTrace();
			return null;
		}
	}
}
