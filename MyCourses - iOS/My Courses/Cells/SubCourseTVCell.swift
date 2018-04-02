//
//  SubCourseTVCell.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/31/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SDWebImage

class SubCourseTVCell: UITableViewCell {

    @IBOutlet weak var cellBg: UIView!
    @IBOutlet weak var centerNameLbl: UILabel!
    @IBOutlet weak var instructorNameLbl: UILabel!
    @IBOutlet weak var startingDateLbl: UILabel!
    @IBOutlet weak var rateLbl: UILabel!
    @IBOutlet weak var feesLbl: UILabel!
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var bookBtn: UIButton!
    
    @IBOutlet weak var lineV: UIView!
    @IBOutlet weak var infoSV: UIStackView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        cellBg.layer.cornerRadius = 20
        cellBg.layer.masksToBounds = true
        
        img.layer.cornerRadius = 20
        img.layer.masksToBounds = true
        
        
        NSLayoutConstraint.activate([
            infoSV.leadingAnchor.constraint(equalTo: img.trailingAnchor),
            infoSV.trailingAnchor.constraint(equalTo: lineV.leadingAnchor),
            infoSV.bottomAnchor.constraint(equalTo: img.bottomAnchor),
            infoSV.topAnchor.constraint(equalTo: img.topAnchor)
            ])
        
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
