//
//  BookCourseVP.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Tabman
import Pageboy

class BookCourseVP: TabmanViewController, PageboyViewControllerDataSource {

    var subCourse = SubCourse()
    
    private var viewControllers = [UIViewController]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if subCourse.startingDateArrayList.count == 0{
            subCourse.startingDateArrayList.append(StartingDate())
        }
        let centerInfoTVC = storyboard?.instantiateViewController(withIdentifier: "CenterInfoTVC") as! CenterInfoTVC
//        centerInfoTVC.center = self.center
        centerInfoTVC.info = [
            "\n\n\n",
            "Info",
            self.subCourse.info,
            " ",
            "Instructor:",
            self.subCourse.instructorName,
            " ",
            "Starting at:",
            self.subCourse.startingDateArrayList[0].date,
            " ",
            "Fees:",
            "\(self.subCourse.fees) L.E"
        ]
        
        
        let bookCourseTVC = storyboard?.instantiateViewController(withIdentifier: "BookCourseTVC") as! BookCourseTVC
        bookCourseTVC.subCourse = self.subCourse
        
        viewControllers.append(bookCourseTVC)
        viewControllers.append(centerInfoTVC)
        
        self.dataSource = self
        
        self.bar.items = [
            Item.init(title: "Book"),
            Item.init(title: "Info")
        ]
        
        
        bar.style = .buttonBar
        bar.location = .top
        
        bar.appearance = TabmanBar.Appearance({ (appearance) in
            
            // customize appearance here
            appearance.state.color = UIColor.primaryColor()
            appearance.state.selectedColor = UIColor.primaryColor()
            appearance.text.font = .systemFont(ofSize: 16.0)
            appearance.indicator.isProgressive = false
        })
    }

    func numberOfViewControllers(in pageboyViewController: PageboyViewController) -> Int {
        return 2
    }
    
    func viewController(for pageboyViewController: PageboyViewController, at index: PageboyViewController.PageIndex) -> UIViewController? {
        
        return viewControllers[index]
        
    }
    
    func defaultPage(for pageboyViewController: PageboyViewController) -> PageboyViewController.Page? {
        return nil
    }

}
