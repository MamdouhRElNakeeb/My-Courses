package com.mycoursesapp.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class SubCourse extends Course{

    public int subCourseID;
    public int centerID;
    public float rate;
    public int fees;
    public String instructorName;
    public String info;
//    public boolean isTrend;
    public ArrayList<StartingDate> startingDateArrayList;
    public String datesJSONArr;
    public ArrayList<String> imagesAL;

    public SubCourse(int id, String name, String slogan, String img, String info,
                     int subCourseID, int centerID, float rate, int fees, String instructorName,
                     ArrayList<StartingDate> startingDateArrayList, ArrayList<String> imagesAL){

        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.img = img;
        this.info = info;
        this.subCourseID = subCourseID;
        this.centerID = centerID;
        this.rate = rate;
        this.fees = fees;
        this.instructorName = instructorName;
        this.startingDateArrayList = startingDateArrayList;
        this.imagesAL = imagesAL;
    }

    public SubCourse(int id, String name, String slogan, String img,
                     int subCourseID, int centerID, float rate, int fees, String instructorName,
                     ArrayList<StartingDate> startingDateArrayList, ArrayList<String> imagesAL, String datesJSONArr){

        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.img = img;
        this.subCourseID = subCourseID;
        this.centerID = centerID;
        this.rate = rate;
        this.fees = fees;
        this.instructorName = instructorName;
        this.startingDateArrayList = startingDateArrayList;
        this.imagesAL = imagesAL;
        this.datesJSONArr = datesJSONArr;
    }

    public SubCourse(int id, String name, String slogan, String img, String info,
                     int subCourseID, int centerID, float rate, int fees, String instructorName,
                     ArrayList<StartingDate> startingDateArrayList, ArrayList<String> imagesAL, String datesJSONArr){

        this.id = id;
        this.name = name;
        this.slogan = slogan;
        this.img = img;
        this.subCourseID = subCourseID;
        this.centerID = centerID;
        this.rate = rate;
        this.fees = fees;
        this.instructorName = instructorName;
        this.info = info;
        this.startingDateArrayList = startingDateArrayList;
        this.imagesAL = imagesAL;
        this.datesJSONArr = datesJSONArr;
    }

    public SubCourse(){}


    /*Comparator for sorting the list by roll no*/
    public static Comparator<SubCourse> feesLH = new Comparator<SubCourse>() {

        public int compare(SubCourse s1, SubCourse s2) {

	        /*For ascending order*/
            return s1.fees - s2.fees;

        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<SubCourse> feesHL = new Comparator<SubCourse>() {

        public int compare(SubCourse s1, SubCourse s2) {

	        /*For descending order*/
            return s2.fees - s1.fees;

        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<SubCourse> rateLH = new Comparator<SubCourse>() {

        public int compare(SubCourse s1, SubCourse s2) {

	        /*For ascending order*/
            return (int) (s1.rate - s2.rate);

        }
    };

    /*Comparator for sorting the list by roll no*/
    public static Comparator<SubCourse> rateHL = new Comparator<SubCourse>() {

        public int compare(SubCourse s1, SubCourse s2) {

	        /*For descending order*/
            return (int) (s2.rate - s1.rate);

        }
    };
}