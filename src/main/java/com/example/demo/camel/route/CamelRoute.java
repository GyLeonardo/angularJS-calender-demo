package com.example.demo.camel.route;

import java.io.InputStream;

import com.example.demo.camel.processor.CalendarProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.demo.camel.processor.LoginProcessor;
import com.example.demo.camel.processor.RegistryProcessor;
import com.example.demo.utils.CamelProcessorUtils;

@Component
@PropertySource({"classpath:config/mule.properties"})
public class CamelRoute extends RouteBuilder {
	
	@Value("${user.login.url}")
	private String loginUrl;
	
	@Value("${user.registry.url}")
	private String registryUrl;
	
	@Value("${username.check.url}")
	private String usernameCheckUrl;
	
	@Value("${applications.overview.url}")
	private String appsOverviewUrl;

	@Value("${calendar.time.url}")
	private String calendarTimeUrl;

	@Value("${daySchedule.time.url}")
	private String dayScheduleUrl;

	@Value("${getGrade.url}")
	private String getGradeUrl;

	@Value("${calendarSchduleUrl.time.url}")
	private String calendarSchduleUrl;
	@Value("${getAllTimeScheduleUrl.time.url}")
	private String getAllTimeScheduleUrl;

	@Value("${getStaffListUrl.url}")
	private String getStaffListUrl;

	@Value("${getAllBranchUrl.url}")
	private String getAllBranchUrl;


	@Override
	public void configure() throws Exception {
		restConfiguration()
		  .contextPath("/camel") 
		  .port("8080")
		  .enableCORS(true)
		  .apiContextPath("/api-doc")
		  .apiProperty("api.title", "Camel REST API")
		  .apiProperty("api.version", "v1")
		  .apiContextRouteId("doc-api")
		  .component("servlet");

		rest("/api/")
				.id("login-route")
				.consumes("application/json")
				.post("/getAllBranch")
				.to("direct:getAllBranchService");
		from("direct:getAllBranchService").process(new CalendarProcessor()).to(getAllBranchUrl);

		rest("/api/")
		  .id("login-route")
		  .consumes("application/json")
		  .post("/login")
		  .to("direct:loginService");
		from("direct:loginService").process(new LoginProcessor()).to(loginUrl);
		
		rest("/api/")
		  .id("registry-route")
		  .consumes("application/json")
		  .post("/registry")
		  .to("direct:registryService");
		from("direct:registryService").process(new RegistryProcessor()).to(registryUrl);

		rest("/api/")
				.id("calendarTime-route")
				.consumes("application/json")
				.post("/getCalendarTimeList")
				.to("direct:calendarTimeService");
		from("direct:calendarTimeService").process(new CalendarProcessor()).to(calendarTimeUrl);

		rest("/api/")
				.id("daySchedule-route")
				.consumes("application/json")
				.post("/getDaySchedule")
				.to("direct:dayScheduleService");
		from("direct:dayScheduleService").process(new CalendarProcessor()).to(dayScheduleUrl);

		rest("/api/")
				.id("daySchedule-route")
				.consumes("application/json")
				.post("/getGrade")
				.to("direct:getGradeService");
		from("direct:getGradeService").process(new LoginProcessor()).to(getGradeUrl);

		rest("/api/")
				.id("daySchedule-route")
				.consumes("application/json")
				.post("/saveCalendarSchdule")
				.to("direct:calendarSchduleService");
		from("direct:calendarSchduleService").process(new CalendarProcessor()).to(calendarSchduleUrl);

		rest("/api/")
				.id("daySchedule-route")
				.consumes("application/json")
				.post("/getAllTimeSchedule")
				.to("direct:getAllTimeScheduleService");
		from("direct:getAllTimeScheduleService").process(new CalendarProcessor()).to(getAllTimeScheduleUrl);

		rest("/api/")
				.id("daySchedule-route")
				.consumes("application/json")
				.post("/getStaffList")
				.to("direct:getStaffListService");
		from("direct:getStaffListService").process(new CalendarProcessor()).to(getStaffListUrl);
		
		rest("/api/")
		  .id("usrcheck-route")
		  .consumes("application/json")
		  .post("/usrcheck")
		  .to("direct:usrcheckService");
		from("direct:usrcheckService").process(new Processor() {
		   
			@Override
			public void process(Exchange exchange) throws Exception {
				InputStream body = null;
				body = exchange.getIn().getBody(InputStream.class);
				String data = CamelProcessorUtils.setHttpBody(body);
		        exchange.getOut().setHeader("content-type", "application/json");
		        exchange.getOut().setBody(data);
			}
		  }).to(usernameCheckUrl);
		
		rest("/api/")
		  .id("apps-route")
		  .consumes("application/json")
		  .post("/apps")
		  .to("direct:appsService");
		from("direct:appsService").process(new Processor() {
			
			@Override
			public void process(Exchange exchange) throws Exception {
				InputStream body = null;
				body = exchange.getIn().getBody(InputStream.class);
				String data = CamelProcessorUtils.setHttpBody(body);
		        exchange.getOut().setHeader("content-type", "application/json");
		        exchange.getOut().setBody(data);
			}
		  }).to(appsOverviewUrl);
	}

}
