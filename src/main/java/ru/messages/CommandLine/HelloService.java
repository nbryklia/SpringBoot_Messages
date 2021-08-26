package ru.messages.CommandLine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    @Value("${name:unknown}")
    private String name;

    public HelloService(){

    }

    public HelloService(String name){
        this.name = name;
    }

    public String getMessage() {
        return getMessage(name);
    }

    public String getMessage(String name) {
        return "Hello " + name;
    }
}