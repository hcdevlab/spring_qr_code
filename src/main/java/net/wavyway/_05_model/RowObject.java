
package net.wavyway._05_model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

@JsonDeserialize(using = RowObjectDeserializer.class)
public class RowObject implements Serializable
{
	private String id;
	private String userName;
	private String date;
	private String qrCode;
	private String latCoord;
	private String longCoord;

	public RowObject()
	{
	}

	public RowObject(String id, String userName, String date, String qrCode, String latCoord, String longCoord)
	{
		this.id = id;
		this.userName = userName;
		this.date = date;
		this.qrCode = qrCode;
		this.latCoord = latCoord;
		this.longCoord = longCoord;
	}

	public RowObject(String userName, String date, String qrCode, String latCoord, String longCoord)
	{
		this.userName = userName;
		this.date = date;
		this.qrCode = qrCode;
		this.latCoord = latCoord;
		this.longCoord = longCoord;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public String getQrCode()
	{
		return qrCode;
	}

	public void setQrCode(String qrCode)
	{
		this.qrCode = qrCode;
	}

	public String getLatCoord()
	{
		return latCoord;
	}

	public void setLatCoord(String latCoord)
	{
		this.latCoord = latCoord;
	}

	public String getLongCoord()
	{
		return longCoord;
	}

	public void setLongCoord(String longCoord)
	{
		this.longCoord = longCoord;
	}
}
