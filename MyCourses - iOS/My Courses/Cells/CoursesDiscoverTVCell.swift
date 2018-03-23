//
//  CoursesDiscoverTVCell.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/12/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

class CoursesDiscoverTVCell: UITableViewCell {

    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var gradient: GradientView!
    @IBOutlet weak var name: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        img.layer.cornerRadius = 20
        img.layer.masksToBounds = true
        
        gradient.layer.cornerRadius = 20
        gradient.layer.masksToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
