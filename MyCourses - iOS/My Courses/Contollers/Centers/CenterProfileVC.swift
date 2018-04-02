//
//  CenterProfileVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class CenterProfileVC: UIViewController {

    @IBOutlet weak var headerIV: UIImageView!
    @IBOutlet weak var centerNameLbl: UILabel!
    
    
    @IBOutlet weak var centerProfileChild: UIView!
    
    var centerID = 0
    
    var center = Center()
    var subCourseArrayList = [SubCourse]()
    var info = ""
    var address = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        if centerID == 0 {
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
        
        Alamofire.request(Consts.CENTERS_COURSES + "\(centerID)/").responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
               
                let centerInfo = json["info"]
                let coursesArr = json["courses"]
                
                self.getCenters(jsonArray: coursesArr)
                self.getCenterInfo(jsonObject: centerInfo)
                
                self.setupCenterCoursesView()
                self.setupCenterInfoView()
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    func getCenterInfo(jsonObject: JSON) {
    
        let id = jsonObject["user"].intValue
        let lat = jsonObject["lat"].doubleValue
        let lon = jsonObject["lon"].doubleValue
        
    
        let centerName = jsonObject["centreName"].stringValue
        let info = jsonObject["info"].stringValue
        let address = jsonObject["address"].stringValue
        let img = jsonObject["image"].stringValue
        
        self.center = Center.init(id: id, name: centerName, info: info, address: address, latitude: lat, longitude: lon, img: img)
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
            
            print("Log: " + courseName)
            print("Log: \(courseID)")
            
            subCourseArrayList.append(SubCourse.init(id: courseID, name: courseName, slogan: "", img: imgAL[0], info: info, subCourseID: subCourseID, centerID: centerID, rate: rate, fees: fees, instructorName: instructorName, startingDateArrayList: startingDateArr, imagesAL: imgAL, datesJSONArr: "\(subJson["dates"].arrayValue)"))
            
        }
        
        
    }
    
    func setupCenterInfoView(){
        
        self.centerNameLbl.text = center.name
        self.headerIV.sd_setImage(with: URL(string: Consts.SERVER + center.img), placeholderImage: UIImage(named: "books_bg"))
        
        
    }
    
    func setupCenterCoursesView(){
      
        let centerProfileVP = storyboard!.instantiateViewController(withIdentifier: "CenterProfileVP") as! CenterProfileVP
        centerProfileVP.items = self.subCourseArrayList
        centerProfileVP.center = self.center
        
        configureChildViewController(childController: centerProfileVP, onView: centerProfileChild)
        
    }

}
