package com.example.demo;

import org.springframework.boot.actuate.info.Info;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoWS {

	// http://localhost:8080/demo?name=test
	// http://localhost:8080/demo
	@RequestMapping("/demo")
	public ResponseEntity<Info> greeting(@RequestParam(value = "name", defaultValue = "World") final String name) {
		return new ResponseEntity<>(new Info.Builder().withDetail("hello", name).build(), HttpStatus.OK);
	}
}


