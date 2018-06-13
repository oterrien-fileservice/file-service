package com.ote.file.service.controller.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Error {
    private final String advice = "Contact your support";
    private final List<String> messages = new ArrayList();

    public Error(String... msg) {
        this.messages.addAll(Arrays.asList(msg));
    }

    public String getAdvice() {
        this.getClass();
        return "Contact your support";
    }

    public List<String> getMessages() {
        return this.messages;
    }
}