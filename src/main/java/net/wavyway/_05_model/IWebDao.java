package net.wavyway._05_model;

import java.util.List;

public interface IWebDao
{
	String insert(RowObject rowObject);

	String insertAll(List<RowObject> rowObjectList);

	List<RowObject> selectAll();

	String deleteAll();

	int getNumberOfRows();

	List<RowObject> getRowsSet(int startNumber, int endNumber);
}
