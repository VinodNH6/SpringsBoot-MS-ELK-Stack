package demo.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
class ELKController {
	private static final Logger LOG = Logger.getLogger(ELKController.class.getName());

	@Autowired
	RestTemplate restTemplete;

	@RequestMapping(value = "/elkdemo")
	public String helloWorld() {
		String response = "Welcome to JavaInUse" + new Date();
		LOG.log(Level.INFO, response);

		return response;
	}

	 
    @RequestMapping(value = "/elk")
    public String helloWorld1() {
    	String url = "http://localhost:8086";
        String response = (String) restTemplete.exchange( url + "/elkdemo", HttpMethod.GET, null, String.class).getBody();
 
        try {
            String exceptionrsp = (String) restTemplete.exchange(url + "/exception", HttpMethod.GET, null, String.class).getBody();
            response = response + " === " + exceptionrsp;
        } catch (Exception e) {
        	e.printStackTrace();
        }
 
        return response;
    }
	
	@RequestMapping(value = "/exception")
	public String exception() {
		String response = "";
		try {
			
			int i = 1 / 0;     // should get exception
		     //or
			throw new Exception("Exception has occured....");
		     
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e);
			
			StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
			String stackTrace = sw.toString();
			LOG.error("Exception - " + stackTrace);
			response = stackTrace;
		}

		return response;
	}
}