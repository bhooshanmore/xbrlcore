package gbicc.xbrl.validate.controller;

import java.util.List;

import gbicc.xbrl.validate.service.ResultService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理的控制器
 * 
 * @author joephoenix
 */
@Controller
@Component
public class ResultController {
	@Resource(name = "resultService")
	private ResultService resultService;

	protected static Logger logger = Logger.getLogger("controller");

	/**
	 * 首页跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/")
	public String IndexPage(Model model) {
		return "welcome";
	}

	/**
	 * 校验首页跳转
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/validate")
	public String validateIndex(Model model) {
		return "validate";
	}

	/**
	 * 校验功能方法
	 * 
	 * @param p
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/validate/{p}")
	public String FirstPage(@PathVariable Integer p, Model model) {
		// Pageable pp = new PageRequest(p, 10);
		List<String> result = resultService.returnErrorList();

		model.addAttribute("validate_results", result);
		return "validateresults";
	}
}
