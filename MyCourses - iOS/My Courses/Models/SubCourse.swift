//
//  SubCourse.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/31/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

class SubCourse: Course{
    
    var subCourseID = 0
    var centerID = 0
    var rate = 0.0
    var fees = 0
    var instructorName = ""
    var info = ""
    var startingDateArrayList = [StartingDate]()
    var datesJSONArr = ""
    var imagesAL = [String]()
    
    
    init(id: Int, name: String, slogan: String, img: String, info: String,
         subCourseID: Int, centerID: Int, rate: Double, fees: Int, instructorName: String,
         startingDateArrayList: [StartingDate], imagesAL: [String]) {
        
        super.init(id: id, name: name, slogan: slogan, img: img)
        
        self.info = info
        self.subCourseID = subCourseID
        self.centerID = centerID
        self.rate = rate
        self.fees = fees
        self.instructorName = instructorName
        self.startingDateArrayList = startingDateArrayList
        self.imagesAL = imagesAL
        
    }
    
    init(id: Int, name: String, slogan: String, img: String, subCourseID: Int, centerID: Int, rate: Double,
         fees: Int, instructorName: String, startingDateArrayList: [StartingDate], imagesAL: [String], datesJSONArr: String){
        
        super.init(id: id, name: name, slogan: slogan, img: img)
    
        self.subCourseID = subCourseID
        self.centerID = centerID
        self.rate = rate
        self.fees = fees
        self.instructorName = instructorName
        self.startingDateArrayList = startingDateArrayList
        self.imagesAL = imagesAL
        self.datesJSONArr = datesJSONArr
    
    }
    
    init(id: Int, name: String, slogan: String, img: String, info: String,
         subCourseID: Int, centerID: Int, rate: Double, fees: Int, instructorName: String,
         startingDateArrayList: [StartingDate], imagesAL: [String], datesJSONArr: String){
    
        super.init(id: id, name: name, slogan: slogan, img: img)
        
        self.info = info
        self.subCourseID = subCourseID
        self.centerID = centerID
        self.rate = rate
        self.fees = fees
        self.instructorName = instructorName
        self.startingDateArrayList = startingDateArrayList
        self.imagesAL = imagesAL
        self.datesJSONArr = datesJSONArr
    }
    
    override init(){
        super.init()
    }
    
}
