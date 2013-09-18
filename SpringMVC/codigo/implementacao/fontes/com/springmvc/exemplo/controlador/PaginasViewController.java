package com.springmvc.exemplo.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="paginas/privada")
public class PaginasViewController extends AbstractViewController {
	
	@RequestMapping(value="/perfil1/pagina1", method=RequestMethod.GET)
	public String mostrarPagina1() {
		return "paginas/privada/perfil1/pagina1";
	}
	
	@RequestMapping(value="/perfil2/pagina2", method=RequestMethod.GET)
	public String mostrarPagina2() {
		return "paginas/privada/perfil2/pagina2";
	}
}