//
//  RecCourseCell.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SDWebImage

class RecCourseCell: UICollectionViewCell {
    
    var img = UIImageView()
    var gradient = GradientView()
    var name = UILabel()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        let size = UIScreen.main.bounds.width / 2
        contentView.frame = CGRect(x: 0, y: 0, width: size, height: size)
        
        img.frame = CGRect(x: 10, y: 10, width: size - 20, height: size - 20)
        img.contentMode = .scaleAspectFill
        img.layer.cornerRadius = 20
        img.layer.masksToBounds = true
        
        
        gradient.frame = CGRect(x: img.frame.minX, y: img.frame.height / 2 + 10, width: img.frame.width, height: img.frame.height / 2)
        gradient.startColor = UIColor.clear
        gradient.endColor = UIColor.black.withAlphaComponent(0.7)
        gradient.startLocation = 0
        gradient.endLocation = 1
        gradient.horizontalMode = false
        gradient.diagonalMode = false
        gradient.layer.cornerRadius = 20
        gradient.layer.masksToBounds = true
        
        
        name.frame = CGRect(x: img.frame.minX + 10, y: img.frame.height - 50, width: img.frame.width - 20, height: 50)
        name.numberOfLines = 2
        name.textColor = UIColor.white
        name.font = UIFont.systemFont(ofSize: 18)
        
        self.contentView.addSubview(img)
        self.contentView.addSubview(gradient)
        self.contentView.addSubview(name)
        
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}
