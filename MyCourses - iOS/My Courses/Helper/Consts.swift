//
//  Consts.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/10/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

class Consts{
    
    // Constants
    static var TWITTER_PROVIDER = "twitter"
    static var FACEBOOK_PROVIDER = "facebook"
    
    // API Urls
    public static var WEBSITE = "https://mycoursesapp.com/";
    public static var SERVER = WEBSITE; //"https://46.101.1.65/";
    public static var API = SERVER + "api/";
    public static var LOGIN = API + "login";
    public static var LOGIN_SOCIAL = API + "socialSignIn/";
    public static var SIGN_UP_SOCIAL = API + "socialSignUp/";
    public static var SIGN_UP = API + "signup";
    public static var COMPLETE_PROFILE = API + "userProfile/";
    public static var CATEGORIES = API + "categories";
    public static var CENTERS = API + "centres";
    public static var IMGS = SERVER + "media/images/";
    public static var CENTERS_COURSES = API + "centreCourses/";
    
    public static var COURSES = API + "courses";
    public static var RECOMMENDED = API + "recommended/";
    public static var COURSES_LANG = API + "coursesLang";
    
    
    public static var SUB_COURSES = API + "subCourse/";
    
    public static var PROMO_CODE = API + "promoCodeCheck/";
    public static var PROMO_USER = API + "UserPromo/";
    
    public static var BOOKING = API + "booking/";
    public static var BOOKING_USER = API + "userBooking/";
    public static var BOOKING_CANCEL = API + "bookingDelete/";
    
    public static var COURSES_FILTER = API + "courseFilter/";
    
    public static var SEARCH = API + "searchCourse?search=";
    
    public static var APP_URL = WEBSITE + "app/";
    
    public static var APP_ABOUT = WEBSITE + "about";
    public static var APP_TERMS = WEBSITE + "terms";
    
    public static var TWT_IMG = "https://api.twitter.com/1.1/users/lookup.json";
    
    public static var TWT_CKEY = "vf0zpRD0apGzKvDUg5GZa7FYK";
    public static var TWT_CSECRET = "r8SfYDSVb1D8CUDA922kmHZuFswpt5R9Cv8TEqWXCb1Dc80b7m";
    public static var TWT_ATOKEN = "192149462-d836n1cmvDV4IlyqHO2KaBdPkrAectxZOZxQpWSv";
    public static var TWT_TSECRET = "J5iyRXIcwEdhureasyuZRBAETaSei9e23vewXxzQnB8Gv";
    
    public static var GOOGLE_CLIENT_ID = "J5iyRXIcwEdhureasyuZRBAETaSei9e23vewXxzQnB8Gv";
}
