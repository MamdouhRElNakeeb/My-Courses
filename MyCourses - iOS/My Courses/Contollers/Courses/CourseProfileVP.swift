//
//  SubCoursesVP.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation
import Pageboy
import Tabman

class CourseProfileVP: TabmanViewController, PageboyViewControllerDataSource {
    
    
    var items = [SubCourse]()
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        self.dataSource = self
        
        self.bar.items = [
            Item.init(title: NSLocalizedString("centers", tableName: "localized" ,comment: ""))
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
        return 1
    }
    
    func viewController(for pageboyViewController: PageboyViewController, at index: PageboyViewController.PageIndex) -> UIViewController? {
        
        let subCoursesTVC = storyboard?.instantiateViewController(withIdentifier: "SubCoursesTVC") as! SubCoursesTVC
        subCoursesTVC.items = items
        subCoursesTVC.isCenterProfile = false
        
        return subCoursesTVC
    }
    
    func defaultPage(for pageboyViewController: PageboyViewController) -> PageboyViewController.Page? {
        return nil
    }
}
