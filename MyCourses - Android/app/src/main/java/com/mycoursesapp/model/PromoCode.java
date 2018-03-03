package com.mycoursesapp.model;

/**
 * Created by mamdouhelnakeeb on 2/14/18.
 */

public class PromoCode {

    public int id;
    public int discount;
    public String code;

    public PromoCode(int discount, String code){
        this.discount = discount;
        this.code = code;
    }

    public PromoCode(int discount, String code, int id){
        this.discount = discount;
        this.code = code;
        this.id = id;
    }

    public PromoCode(){}
}
