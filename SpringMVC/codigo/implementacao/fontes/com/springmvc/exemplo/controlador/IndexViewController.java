package com.springmvc.exemplo.controlador;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexViewController extends AbstractViewController {

	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String visualizarIndex(Map<String, Object> model) {
		return "index";
	}
}