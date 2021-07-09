package net.wavyway._06_download;

import net.wavyway._05_model.IWebDao;
import net.wavyway._05_model.RowObject;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;


public class TableToExcel
{
	private IWebDao webDao;

	public XSSFWorkbook createExcelDocument(List<RowObject> rowObjectList)
	{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("dataFromDB");

		spreadsheet.setColumnWidth(0, 1500);
		spreadsheet.setColumnWidth(1, 3000);
		spreadsheet.setColumnWidth(2, 9000);
		spreadsheet.setColumnWidth(3, 8000);
		spreadsheet.setColumnWidth(4, 6000);
		spreadsheet.setColumnWidth(5, 6000);

		XSSFRow row = spreadsheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("NOMBRE");
		row.createCell(2).setCellValue("FECHA");
		row.createCell(3).setCellValue("QR_CODE");
		row.createCell(4).setCellValue("LATITUD");
		row.createCell(5).setCellValue("LONGITUD");

		spreadsheet.setAutoFilter(new CellRangeAddress(0, 0, 0, 5));

		int rowCount = 1;
		for (RowObject rowObject : rowObjectList)
		{
			Row rowExcel = spreadsheet.createRow(rowCount++);
			rowExcel.createCell(0).setCellValue(rowObject.getId());
			rowExcel.createCell(1).setCellValue(rowObject.getUserName());
			rowExcel.createCell(2).setCellValue(rowObject.getDate());
			rowExcel.createCell(3).setCellValue(rowObject.getQrCode());
			rowExcel.createCell(4).setCellValue(rowObject.getLatCoord());
			rowExcel.createCell(5).setCellValue(rowObject.getLongCoord());
		}

		return workbook;
	}
}
