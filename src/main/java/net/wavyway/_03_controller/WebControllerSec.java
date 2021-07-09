package net.wavyway._03_controller;

import net.wavyway._04_service.DataService;
import net.wavyway._05_model.RowObject;
import net.wavyway._06_download.TableToExcel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

@RequestMapping(value = "/private")
@RestController()
public class WebControllerSec
{
	private DataService dataService;

	public WebControllerSec(@Autowired DataService dataService) {
		this.dataService = dataService;
	}

	@GetMapping(value = "/data_table")
	public ModelAndView welcome(ModelAndView modelAndView)
	{
		modelAndView.addObject("message", "En construcción...");
		modelAndView.setViewName("data_table");
		return modelAndView;
	}

	@PostMapping(value = "/init_table", headers = "Accept=application/json", produces = "application/json; charset=UTF-8")
	public ModelAndView getInitialSetOfRows()
	{
		ModelAndView model = new ModelAndView();

		List<RowObject> list = dataService.getRowSet(0, 20);
		System.out.println("Lista: " + list.toString());
		model.addObject("singleRecord", list);

		model.setViewName("fragments :: table_part");
		return model;
	}

	@PostMapping(value = "/part_post", headers = "Accept=application/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json; charset=UTF-8")
	public ModelAndView ajaxFragmentPost(@RequestBody TreeMap treeMap)
	{
		ModelAndView model = new ModelAndView();

		int offset = Integer.parseInt(treeMap.get("firstParam").toString());
		int limit = Integer.parseInt(treeMap.get("lastParam").toString());

		List<RowObject> list = dataService.getRowSet(offset, limit);
		model.addObject("singleRecord", list);

		model.setViewName("fragments :: table_part");
		return model;
	}

	@GetMapping(value = "/delete_table")
	@ResponseBody
	public ResponseEntity<?> deleteTable()
	{
		if (dataService.deleteData().equals("SUCCESS"))
		{
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/rows_number")
	@ResponseBody
	public int getNumberOfRows(ModelMap modelMap)
	{
		return dataService.getTotalRows();
	}

	@RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
	@ResponseBody
	public void downloadExcelTestFile(
			HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{

		XSSFWorkbook wb = new TableToExcel().createExcelDocument(dataService.selectAll());

		Date date = new Date();
		String strDateFormat = "yyyy-MM-dd_hh_mm_ss";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
		String formattedDate = dateFormat.format(date);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=qr_excel_" + formattedDate + ".xlsx");

		OutputStream out = response.getOutputStream();
		wb.write(out);

		out.flush();
		out.close();
		wb.close();
	}
}
