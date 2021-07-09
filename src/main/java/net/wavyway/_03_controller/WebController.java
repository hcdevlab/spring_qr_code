
package net.wavyway._03_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin
@Controller
// @RestController
public class WebController
{
	@GetMapping(value = {"/", "/index"})
	public ModelAndView login()
	{
		ModelAndView model = new ModelAndView();

		model.setViewName("index");

		return model;
	}
}
