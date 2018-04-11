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
            NSLocalizedString("info", tableName: "localized" ,comment: ""),
            self.subCourse.info,
            " ",
            NSLocalizedString("instructor", tableName: "localized" ,comment: ""),
            self.subCourse.instructorName,
            " ",
            NSLocalizedString("starting_at", tableName: "localized" ,comment: ""),
            self.subCourse.startingDateArrayList[0].date,
            " ",
            NSLocalizedString("fees", tableName: "localized" ,comment: ""),
            "\(self.subCourse.fees) L.E"
        ]
        
        
        let bookCourseTVC = storyboard?.instantiateViewController(withIdentifier: "BookCourseTVC") as! BookCourseTVC
        bookCourseTVC.subCourse = self.subCourse
        
        viewControllers.append(bookCourseTVC)
        viewControllers.append(centerInfoTVC)
        
        self.dataSource = self
        
        self.bar.items = [
            Item.init(title: NSLocalizedString("book", tableName: "localized" ,comment: "")),
            Item.init(title: NSLocalizedString("info", tableName: "localized" ,comment: ""))
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
