//
//  Course.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

class Course {
    
    var id = 0
    var name = ""
    var slogan = ""
    var img = ""
    var categories = Array<Category>()
    
    init(id: Int, name: String, slogan: String, img: String){
        
        self.id = id
        self.name = name
        self.slogan = slogan
        self.img = img        
    }
    
    init() {}
}
