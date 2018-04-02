//
//  BookCourseVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

class BookCourseVC: UIViewController {

    @IBOutlet weak var courseImg: UIImageView!
    @IBOutlet weak var courseNameLbl: UILabel!
    
    @IBOutlet weak var courseVPChildView: UIView!
    
    var subCourse = SubCourse()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        self.courseImg.sd_setImage(with: URL(string: Consts.SERVER + subCourse.img), placeholderImage: UIImage(named: "bg1"))
        self.courseNameLbl.text = subCourse.name
        
        print("course name;;" + subCourse.name)
        setupCenterCoursesView()
    }


    func setupCenterCoursesView(){
        
        let bookCourseVP = storyboard!.instantiateViewController(withIdentifier: "BookCourseVP") as! BookCourseVP
        bookCourseVP.subCourse = self.subCourse
        configureChildViewController(childController: bookCourseVP, onView: courseVPChildView)
        
    }
    
}
