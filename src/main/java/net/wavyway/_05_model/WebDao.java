
package net.wavyway._05_model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WebDao implements IWebDao
{

	private final JdbcTemplate jdbcTemplate;

	public WebDao(@Autowired @Qualifier("dataSource_1") DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private final String sqlInsertOne = "insert into qr_control (name_user, machine_qr_code, date_hour, lat_coord, long_coord) values (?, ?, ?, ?, ?);";

	@Override
	public String insert(RowObject rowObject)
	{
		String result = "";

		try
		{
			if((rowObject.getUserName().isEmpty() || rowObject.getUserName().equals(null))
					|| (rowObject.getQrCode().isEmpty() || rowObject.getQrCode().equals(null))
					|| (rowObject.getDate().isEmpty() || rowObject.getDate().equals(null))
					|| (rowObject.getLatCoord().isEmpty() || rowObject.getLatCoord().equals(null))
					|| (rowObject.getLongCoord().isEmpty() || rowObject.getLongCoord().equals(null)))
			{
				result = "TROUBLE";
			}
			else
			{
				if (jdbcTemplate.update(sqlInsertOne, new Object[]{
						rowObject.getUserName(),
						rowObject.getQrCode(),
						rowObject.getDate(),
						rowObject.getLatCoord(),
						rowObject.getLongCoord()}) == 1)
				{
					result = "SUCCESS";
				}
			}
		}
		catch (DataAccessException dae)
		{
			dae.getMessage();
			result = "TROUBLE";
		}
		return result;
	}

	@Override
	public String insertAll(List<RowObject> rowObjectList)
	{
		String result = "";

		for (RowObject rowObject: rowObjectList)
		{
			if((rowObject.getUserName().isEmpty() || rowObject.getUserName().equals(null))
					|| (rowObject.getQrCode().isEmpty() || rowObject.getQrCode().equals(null))
					|| (rowObject.getDate().isEmpty() || rowObject.getDate().equals(null))
					|| (rowObject.getLatCoord().isEmpty() || rowObject.getLatCoord().equals(null))
					|| (rowObject.getLongCoord().isEmpty() || rowObject.getLongCoord().equals(null)))
			{
				result = "TROUBLE";
				return result;
			}
		}

		List<Object[]> parameters = new ArrayList<Object[]>();

		for (RowObject rowObject : rowObjectList)
		{
			parameters.add(new Object[]{
					null,
					rowObject.getUserName(),
					rowObject.getQrCode(),
					rowObject.getDate(),
					rowObject.getLatCoord(),
					rowObject.getLongCoord()}
			);
		}

		int[] argTypes = new int[6];
		argTypes[0] = Types.INTEGER;
		argTypes[1] = Types.VARCHAR;
		argTypes[2] = Types.VARCHAR;
		argTypes[3] = Types.VARCHAR;
		argTypes[4] = Types.VARCHAR;
		argTypes[5] = Types.VARCHAR;

		int[] resultArray = jdbcTemplate.batchUpdate(sqlInsertOne, parameters, argTypes);
		if (resultArray.length > 0)
		{
			result = "SUCCESS";
		}
		else
		{
			result = "TROUBLE";
		}
		return result;
	}

	@Override
	public List<RowObject> selectAll()
	{
		String sqlRetrieveAll = "select * from qr_control;";
		List<RowObject> rowObjectList = jdbcTemplate.query(sqlRetrieveAll, new RowMapper<RowObject>()
		{
			@Override
			public RowObject mapRow(ResultSet rs, int rowNum)
			{
				RowObject rowObject = new RowObject();
				try
				{
					rowObject.setId(Integer.toString(rs.getInt("id_qr")));
					rowObject.setUserName(rs.getString("name_user"));
					rowObject.setQrCode(rs.getString("machine_qr_code"));
					rowObject.setDate(rs.getString("date_hour"));
					rowObject.setLatCoord(rs.getString("lat_coord"));
					rowObject.setLongCoord(rs.getString("long_coord"));
				}
				catch (SQLException sqle)
				{
					System.out.println("Error de SQLException: " + sqle.getSQLState() + " - " + sqle.getMessage());
				}
				catch (DataAccessException dae)
				{
					System.out.println("Error de DataAccessException: " + dae.getMessage() + " - " + dae.getCause());
					dae.printStackTrace();
				}
				return rowObject;
			}
		});

		return rowObjectList;
	}

	@Override
	public String deleteAll()
	{
		String response = "";
		String deleteAll = "truncate table qr_control;";
		jdbcTemplate.execute(deleteAll);

		if (jdbcTemplate.getFetchSize() > 0)
		{
			response = "FAILED";
		}
		else
		{
			response = "SUCCESS";
		}
		return response;
	}

	public int getNumberOfRows()
	{
		String getNumberOfRows = "select count(*) from qr_control;";
		return jdbcTemplate.queryForObject(getNumberOfRows, Integer.class);
	}

	public List<RowObject> getRowsSet(int limitNumber, int offsetNumber)
	{
		String sql = "SELECT * FROM qr_control ORDER BY id_qr DESC LIMIT ? OFFSET ? ;";

		RowMapper<RowObject> rowObjectRowMapper = new RowMapper<RowObject>()
		{
			@Override
			public RowObject mapRow(ResultSet rs, int rowNum)
			{
				RowObject rowObject = new RowObject();
				try
				{
					rowObject.setId(Integer.toString(rs.getInt("id_qr")));
					rowObject.setUserName(rs.getString("name_user"));
					rowObject.setQrCode(rs.getString("machine_qr_code"));
					rowObject.setDate(rs.getString("date_hour"));
					rowObject.setLatCoord(rs.getString("lat_coord"));
					rowObject.setLongCoord(rs.getString("long_coord"));
				}
				catch (SQLException sqle)
				{
					System.out.println("Error de SQLException: " + sqle.getSQLState() + " - " + sqle.getMessage());
				}
				catch (DataAccessException dae)
				{
					System.out.println("Error de DataAccessException: " + dae.getMessage() + " - " + dae.getCause());
					dae.printStackTrace();
				}
				return rowObject;
			}
		};

		List<RowObject> rowObjectList = jdbcTemplate.query(sql, rowObjectRowMapper, new Object[]{offsetNumber, limitNumber});

		return rowObjectList;
	}
}
