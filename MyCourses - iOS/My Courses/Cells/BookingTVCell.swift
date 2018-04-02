//
//  BookingTVCell.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/25/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SDWebImage

class BookingTVCell: UITableViewCell {

    
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var gradient: GradientView!
    @IBOutlet weak var centerName: UILabel!
    @IBOutlet weak var courseDate: UILabel!
    @IBOutlet weak var courseName: UILabel!
    @IBOutlet weak var removeBtn: UIButton!
    
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
