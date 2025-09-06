package com.idb.microservicedemo.library.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public final class Result<T> {

    @JsonProperty("data")
    private T data;

    @JsonProperty("errorMessages")
    private List<String> errorMessages;

    @JsonProperty("isSuccessful")
    private boolean isSuccessful = true;

    @JsonProperty("statusCode")
    private int statusCode = 200;

    // Default constructor (for Jackson)
    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(int statusCode, List<String> errorMessages) {
        this.isSuccessful = false;
        this.statusCode = statusCode;
        this.errorMessages = errorMessages;
    }

    public Result(int statusCode, String errorMessage) {
        this.isSuccessful = false;
        this.statusCode = statusCode;
        this.errorMessages = Collections.singletonList(errorMessage);
    }

    // Factory methods
    public static <T> Result<T> succeed(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> failure(int statusCode, List<String> errorMessages) {
        return new Result<>(statusCode, errorMessages);
    }

    public static <T> Result<T> failure(int statusCode, String errorMessage) {
        return new Result<>(statusCode, errorMessage);
    }

    public static <T> Result<T> failure(String errorMessage) {
        return new Result<>(500, errorMessage);
    }

    public static <T> Result<T> failure(List<String> errorMessages) {
        return new Result<>(500, errorMessages);
    }

    // Getters & Setters
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{ \"statusCode\":500, \"errorMessages\":[\"Serialization error\"] }";
        }
    }
}
