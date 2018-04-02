//
//  PromotionTVCell.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SDWebImage

class PromotionTVCell: UITableViewCell {

    @IBOutlet weak var bg: UIImageView!
    @IBOutlet weak var gradient: GradientView!
    @IBOutlet weak var discountLbl: UILabel!
    @IBOutlet weak var promoCodeLbl: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        bg.layer.cornerRadius = 20
        gradient.layer.cornerRadius = 20
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
