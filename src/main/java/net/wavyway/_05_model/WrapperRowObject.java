
package net.wavyway._05_model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class WrapperRowObject
{
	private List<RowObject> rowObjectList;

	public WrapperRowObject(){}

	public WrapperRowObject(List<RowObject> rowObjectList)
	{
		this.rowObjectList = rowObjectList;
	}

	public List<RowObject> getRowObjectList()
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			String jsonArray = mapper.writeValueAsString(rowObjectList);
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
		}

		return rowObjectList;
	}

	public void setPersons(List<RowObject> rowObjectList)
	{
		this.rowObjectList = rowObjectList;
	}
}
