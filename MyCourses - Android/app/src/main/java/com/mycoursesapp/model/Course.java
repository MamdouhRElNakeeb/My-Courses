package com.mycoursesapp.model;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class Course {

    public int id;
    public String name;
    public String slogan;
    public String img;
    public ArrayList<Category> categoryArrayList;

    public Course(int id, String name, String slogan, String img, ArrayList<Category> categoryArrayList){

        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.img = img;
        this.categoryArrayList = categoryArrayList;
    }

    public Course(){}
}
