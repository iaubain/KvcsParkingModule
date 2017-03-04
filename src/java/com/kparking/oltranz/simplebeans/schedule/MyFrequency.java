/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kparking.oltranz.simplebeans.schedule;

/**
 *
 * @author Hp
 */
public class MyFrequency {
    private String key;
    private String value;
    private boolean custom;

    public MyFrequency() {
    }

    public MyFrequency(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the custom
     */
    public boolean isCustom() {
        return custom;
    }

    /**
     * @param custom the custom to set
     */
    public void setCustom(boolean custom) {
        this.custom = custom;
    }
    
}
