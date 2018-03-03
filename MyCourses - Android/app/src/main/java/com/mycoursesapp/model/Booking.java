package com.mycoursesapp.model;

/**
 * Created by mamdouhelnakeeb on 2/14/18.
 */

public class Booking {

    public int id;
    public String centreName;
    public String courseImage;
    public String courseName;
    public String startDate;

    public Booking(String centreName, String courseName, String courseImage, String startDate){
        this.centreName = centreName;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.startDate = startDate;
    }

    public Booking(int id, String centreName, String courseName, String courseImage, String startDate){
        this.id = id;
        this.centreName = centreName;
        this.courseName = courseName;
        this.courseImage = courseImage;
        this.startDate = startDate;
    }

    public Booking(){}
}
