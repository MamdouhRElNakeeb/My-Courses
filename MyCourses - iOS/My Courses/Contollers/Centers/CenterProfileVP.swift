//
//  CenterProfileVP.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Pageboy
import Tabman

class CenterProfileVP: TabmanViewController, PageboyViewControllerDataSource {
    
    
    var items = [SubCourse]()
    
    var center = Center()
    
    private var viewControllers = [UIViewController]()
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        let centerInfoTVC = storyboard?.instantiateViewController(withIdentifier: "CenterInfoTVC") as! CenterInfoTVC
        centerInfoTVC.center = self.center
        centerInfoTVC.info = [
            "\n\n\n",
            NSLocalizedString("info", tableName: "localized" ,comment: ""),
            self.center.info,
            " ",
            NSLocalizedString("address", tableName: "localized" ,comment: ""),
            self.center.address
        ]
        
        let subCoursesTVC = storyboard?.instantiateViewController(withIdentifier: "SubCoursesTVC") as! SubCoursesTVC
        subCoursesTVC.items = items
        subCoursesTVC.isCenterProfile = true
        
        viewControllers.append(subCoursesTVC)
        viewControllers.append(centerInfoTVC)
        
        self.dataSource = self
        
        self.bar.items = [
            Item.init(title: NSLocalizedString("courses", tableName: "localized" ,comment: "")),
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

