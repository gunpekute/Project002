package com.example.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages={Array("com.example")})
class Application

object Application extends App {
  SpringApplication.run(classOf[Application]);
}