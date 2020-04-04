package com.doublefish.langlang.utils;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;

    private boolean success;

    private T detail;
}
