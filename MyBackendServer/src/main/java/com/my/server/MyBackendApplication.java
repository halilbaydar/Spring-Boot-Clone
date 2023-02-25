package com.my.server;

import myspring.MySpringApplication;
import myspring.annotation.MyComponentScan;
import myspring.annotation.MyRestController;
import myspring.annotation.MyService;
import myspring.annotation.MySpringBootApplication;

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
