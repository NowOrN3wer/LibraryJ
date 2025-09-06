package com.idb.microservicedemo.library;

import org.springframework.boot.SpringApplication;

public class TestLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibraryApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
