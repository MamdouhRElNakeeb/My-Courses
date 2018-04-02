//
//  SubCoursesVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
//import Parchment
import Pageboy
import Alamofire
import SwiftyJSON

class CourseProfileVC: UIViewController {
    
    @IBOutlet weak var headerIV: UIImageView!
    @IBOutlet weak var courseNameLbl: UILabel!
    @IBOutlet weak var courseSloganLbl: UILabel!
    
    @IBOutlet weak var courseProfileVPChildView: UIView!
    var courseID = 0
    
    var subCourseArrayList = [SubCourse]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        if courseID == 0 {
            _ = self.navigationController?.popViewController(animated: true)
        }
        
        getData()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        // Hide the navigation bar on the this view controller
        self.navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        // Show the navigation bar on other view controllers
        self.navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    @IBAction func backOnClick(_ sender: Any) {
        _ = self.navigationController?.popViewController(animated: true)
    }
    
    func getData(){
        
        Alamofire.request(Consts.COURSES + "/\(courseID)/").responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
                
                self.courseNameLbl.text = json["info"]["courseName"].stringValue
                self.courseSloganLbl.text = json["info"]["courseSlogun"].stringValue
                self.headerIV.sd_setImage(with: URL(string: Consts.SERVER + json["info"]["courseImage"].stringValue), placeholderImage: UIImage(named: "books_bg"))
                
                let centresArr = json["subCourses"]
                
                self.getCenters(jsonArray: centresArr);
                
                self.setupSubCoursesView()
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    
    func getCenters(jsonArray: JSON){
        
        subCourseArrayList = [SubCourse]()
        
        for (_, subJson):(String, JSON) in jsonArray{
            
            let datesArr = subJson["dates"].arrayValue
            var startingDateArr = [StartingDate]()
            
            for date in datesArr{
                startingDateArr.append(StartingDate.init(id: date["id"].intValue, date: date["dates"].stringValue))
            }
            if startingDateArr.count == 0{
                startingDateArr.append(StartingDate.init())
            }
            
            let imgsArr = subJson["images"].arrayValue
            var imgAL = [String]()
            
            for img in imgsArr{
                imgAL.append(img["images"].stringValue)
            }
            
            if (imgAL.count == 0){
                imgAL.append("")
            }
            
            let rate = subJson["rate"].doubleValue
            let info = subJson["info"].stringValue
            let fees = subJson["fees"].intValue
            let instructorName = subJson["instructorName"].stringValue
        
            let courseName = subJson["course"]["courseName"].stringValue
            let centerID = subJson["centre"].intValue
            let centerName = subJson["centreName"].stringValue
            let subCourseID = subJson["id"].intValue
            let courseID = subJson["course"]["id"].intValue
            
            subCourseArrayList.append(SubCourse.init(id: courseID, name: courseName, slogan: centerName, img: imgAL[0], info: info, subCourseID: subCourseID, centerID: centerID, rate: rate, fees: fees, instructorName: instructorName, startingDateArrayList: startingDateArr, imagesAL: imgAL, datesJSONArr: "\(subJson["dates"].arrayValue)"))
            
        }
      
        
    }
    
    
    func setupSubCoursesView(){
        
        let courseProfileVP = storyboard!.instantiateViewController(withIdentifier: "CourseProfileVP") as! CourseProfileVP
        courseProfileVP.items = self.subCourseArrayList
        configureChildViewController(childController: courseProfileVP, onView: courseProfileVPChildView)

    }
    
   
}
