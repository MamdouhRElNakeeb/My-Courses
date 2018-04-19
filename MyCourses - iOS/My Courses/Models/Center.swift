//
//  Center.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/12/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation
import UIKit
import GoogleMaps

class Center: NSObject /*,GMUClusterItem*/{
    
    var id = 0
    var name = ""
    var info = ""
    var address = ""
    var latitude = 0.0
    var longitude = 0.0
    var img = ""
    var position = CLLocationCoordinate2D()
    
    init(id: Int, name: String, info: String, address: String, latitude: Double, longitude: Double, img: String) {
        
        self.id = id
        self.name = name
        self.info = info
        self.address = address
        self.latitude = latitude
        self.longitude = longitude
        self.img = img
        
    }

//    init(position: CLLocationCoordinate2D, name: String) {
//        self.position = position
//        self.name = name
//    }
    
    override init() {}
    
}
