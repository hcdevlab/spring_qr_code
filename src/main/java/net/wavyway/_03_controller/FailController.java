package net.wavyway._03_controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class FailController implements ErrorController {

	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		ModelAndView model = new ModelAndView();
		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
				model.setViewName("errors/401");
				return model;
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				model.setViewName("errors/403");
				return model;
			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
				model.setViewName("errors/404");
				return model;
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				model.setViewName("errors/error");
				return model;
			}
		}
		model.setViewName("errors/error");
		return model;
	}
}
