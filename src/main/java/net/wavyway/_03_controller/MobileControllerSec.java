package net.wavyway._03_controller;

import net.wavyway._04_service.DataService;
import net.wavyway._05_model.RowObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/mobile")
@RestController()
public class MobileControllerSec
{

	private DataService dataService;

	public MobileControllerSec(@Autowired DataService dataService) {
		this.dataService = dataService;
	}

	@PostMapping(value = "/insert/", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void insert(@RequestBody List<RowObject> rowObjectList)
	{
		dataService.insertAll(rowObjectList);
	}
}
