//
//  Category.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/10/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

struct Category: Codable {
    
    var id = 0
    var name = ""
    var img = ""
    var selected = false
    
    init(id: Int, name: String) {
        
        self.id = id
        self.name = name
    }
    
    init(id: Int, name: String, img: String) {
        
        self.id = id
        self.name = name
        self.img = img
    }
}
