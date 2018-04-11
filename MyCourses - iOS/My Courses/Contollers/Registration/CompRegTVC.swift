//
//  CompRegTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/10/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import DropDown
import Alamofire
import SwiftyJSON
import SideMenuController

class CompRegTVC: UITableViewController {

    @IBOutlet weak var mobileNoTF: UITextField!
    @IBOutlet weak var universityTF: UITextField!
    @IBOutlet weak var departmentTF: UITextField!
    var fieldsOfStudyDD = DropDown()
    @IBOutlet weak var chooseFieldsBtn: UIButton!
    @IBOutlet weak var registerBtn: UIButton!
    
    var categories = Array<Category>()
    var catagoriesArr = [String]()
    var selectedCategories = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getCategories()
        
        registerBtn.layer.cornerRadius = 15
        registerBtn.layer.masksToBounds = true
        
        registerBtn.addTarget(self, action: #selector(completeProfile(_:)), for: .touchUpInside)
    }

    @IBAction func chooseFieldsOfStudy(_ sender: Any) {
        
        print("show drop down")
        fieldsOfStudyDD.show()
        
    }
    
    @IBAction func backOnClick(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func goHome(){
        
        let mainVC =  self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as! UINavigationController
        
        let sideMenuVC =  self.storyboard?.instantiateViewController(withIdentifier: "SideMenuVC") as! SideMenuVC
        let sideMenuViewController = SideMenuController()
        // embed the side and center controllers
        sideMenuViewController.embed(sideViewController: sideMenuVC)
        sideMenuViewController.embed(centerViewController: mainVC)
        
        let langStr = Locale.preferredLanguages[0]
        
        if langStr == "en"{
            SideMenuController.preferences.drawing.sidePanelPosition = .underCenterPanelLeft
        }
        else{
            SideMenuController.preferences.drawing.sidePanelPosition = .underCenterPanelRight
        }
        
        show(sideMenuViewController, sender: nil)
        
    }
    
    @objc func completeProfile(_ sender: UIButton){
        
        let mobile = mobileNoTF.text!
        let university = universityTF.text!
        let department = departmentTF.text!
        
        if mobile.isEmpty{
            let alert = UIAlertController(title: "Error", message: "Please enter your mobile!", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            
            return
        }
        
        if university.isEmpty {
            let alert = UIAlertController(title: "Error", message: "Please enter your university!", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            
            return
        }
        
        if department.isEmpty {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your department!", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            
            return
            
        }
        
        if !selectedCategories{
            
            let alert = UIAlertController(title: "Error", message: "Please select one field of study at least!", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            
            return
            
        }
        
        var fieldsJsonArr = JSON().array
        
        for item in categories{
            
            if item.selected{
                fieldsJsonArr?.append(JSON(item.id))
            }
        }
        
        let params: Parameters = [
            "mobile": mobile,
            "certificate":"",
            "fieldOfStudy": fieldsJsonArr!
        
        ]
        
        print(params)
        
        Alamofire.request(Consts.COMPLETE_PROFILE, method: .post, parameters: params).responseJSON{
            
            response in
            
            switch response.result{
                
            case .success(let value):
                
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let created = json["created"].bool{
                    
                    if created{
                        
                        UserDefaults.standard.set(true, forKey: "login")
                        
                        self.goHome()
                        
                    }
                    else{
                        
                        let alert = UIAlertController(title: "Error", message: "An error occurred, Try again later!", preferredStyle: UIAlertControllerStyle.alert)
                        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                        
                        return
                        
                    }
                }
                
                break
                
            case .failure(let error):
                print(error)
                
                let alert = UIAlertController(title: "Error", message: "An error occurred, Try again later!", preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                return
                
                break
            }
        }
    }
    
    func setupDropDown(){
        
        fieldsOfStudyDD.frame = self.chooseFieldsBtn.frame
        fieldsOfStudyDD.anchorView = self.chooseFieldsBtn
        fieldsOfStudyDD.direction = .any
        fieldsOfStudyDD.dismissMode = .onTap
        fieldsOfStudyDD.dataSource = catagoriesArr
        
        // Action triggered on selection
        fieldsOfStudyDD.selectionAction = { [weak self] (index, item) in
            self?.chooseFieldsBtn.setTitle(item, for: .normal)
            
            if (self?.categories[index].selected)! {
                self?.categories[index].selected = false
            }
            else{
                self?.categories[index].selected = true
            }
        }
        
        fieldsOfStudyDD.multiSelectionAction = { [weak self] (indices, items) in
            print("Muti selection action called with: \(items)")
            if items.isEmpty {
                self?.chooseFieldsBtn.setTitle("Choose Fields of Studies", for: .normal)
                self?.selectedCategories = false
            }
            else {
                self?.selectedCategories = true
                var names = ""
                
                for i in 0..<items.count {
                    names += items[i]
                    if i != items.count - 1{
                        
                        names += ", "
                    }
                }
                self?.chooseFieldsBtn.setTitle(names, for: .normal)
            }
           
        }
        
    }
    
    func getCategories(){
        
        Alamofire.request(Consts.CATEGORIES, method: .get).responseJSON{
            
            response in
            
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                for (_, subJson):(String, JSON) in json {
                    // Do something you want
                    var categoryName = subJson["category"].stringValue
                    categoryName = categoryName.replacingOccurrences(of: "_", with: " ")
                    
                    self.categories.append(Category.init(id: subJson["id"].intValue, name: categoryName))
                    self.catagoriesArr.append(categoryName)
                }
                
                self.setupDropDown()
                
            case .failure(let error):
                print(error)
                
                let alert = UIAlertController(title: "Error", message: "An error occurred, Try again later!", preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                return
            }
            
        }
        
    }

}
