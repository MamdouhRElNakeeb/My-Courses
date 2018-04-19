//
//  Booking.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/29/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

class Booking{
    
    var id = 0
    var centerName = ""
    var courseImage = ""
    var courseName = ""
    var startDate = ""
    
    init(id: Int, centerName: String, courseImage: String, courseName: String, startDate: String) {
        
        self.id = id
        self.centerName = centerName
        self.courseImage = courseImage
        self.courseName = courseName
        self.startDate = startDate
    }
    
    init(){}
}
