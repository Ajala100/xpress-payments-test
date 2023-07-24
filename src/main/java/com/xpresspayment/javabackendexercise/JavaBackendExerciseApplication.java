package com.xpresspayment.javabackendexercise;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
public class JavaBackendExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBackendExerciseApplication.class, args);
	}

	@Bean
	public HttpClient createHttpClient(){
		return HttpClient.newBuilder().build();
	}

	@Bean
	public Gson createGsonBean(){
		return new Gson();
	}

}
