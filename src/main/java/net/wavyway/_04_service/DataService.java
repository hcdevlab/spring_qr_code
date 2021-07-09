package net.wavyway._04_service;

import net.wavyway._05_model.IWebDao;
import net.wavyway._05_model.RowObject;
import net.wavyway._05_model.WebDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private IWebDao webDao;

    public DataService(@Autowired WebDao webDao) {
        this.webDao = webDao;
    }

    public List<RowObject> selectAll() {
        return webDao.selectAll();
    }

    public List<RowObject> getRowSet(int offset, int limit) {
        List<RowObject> list = webDao.getRowsSet(offset, limit);
        return list;
    }

    public String insert(RowObject rowObject)
    {
        return webDao.insert(rowObject);
    }

    public String insertAll(List<RowObject> rowObjectList)
    {
        return webDao.insertAll(rowObjectList);
    }

    public String deleteData() {
        if (webDao.deleteAll().equals("SUCCESS"))
        {
            return "SUCCESS";
        }
        else
        {
            return "FAILED";
        }
    }

    public int getTotalRows() {
        int result = webDao.getNumberOfRows();
        System.out.println("Rows: " + Integer.toString(result));
        return result;
    }
}
