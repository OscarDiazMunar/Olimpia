package com.oscar.olimpia.Modelo;

/**
 * Created by daniel on 13/06/2017.
 */

public class BaseData {
    private String message;
    private int type;

    public BaseData(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public BaseData() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
