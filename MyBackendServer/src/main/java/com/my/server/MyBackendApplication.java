package com.my.server;

import my.spring.boot.MySpringApplication;
import my.spring.boot.annotation.MyComponentScan;
import my.spring.boot.annotation.MyRestController;
import my.spring.boot.annotation.MyService;
import my.spring.boot.annotation.MySpringBootApplication;

@MyComponentScan(
        path = {"."},
        supportedComponents = {MyService.class, MyRestController.class}
)
@MySpringBootApplication
public class MyBackendApplication {
    public static void main(String[] args) {
        MySpringApplication.run(MyBackendApplication.class);
    }
}
