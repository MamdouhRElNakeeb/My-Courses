package com.mycoursesapp.model;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class Category {

    public int id = 0;
    public String name = "";
    public boolean selected = false;
    public String img = "";

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, String img){
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public Category(){

    }
}
