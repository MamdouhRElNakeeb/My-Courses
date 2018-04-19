//
//  PromoCode.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation

class PromoCode {
    
    
    var id = 0
    var discount = 0
    var code = ""
    
    init(discount: Int, code: String){
        self.discount = discount
        self.code = code
    }
    
    init (discount: Int, code: String, id: Int){
        self.discount = discount;
        self.code = code;
        self.id = id;
    }
    
    init(){}
    
}
