//
//  BookCourseTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import DropDown
import Alamofire
import SwiftyJSON

class BookCourseTVC: UITableViewController {

    var subCourse = SubCourse()
    
    @IBOutlet weak var callCenterBtn: UIButton!
    @IBOutlet weak var bookNowBtn: UIButton!
    @IBOutlet weak var feesLbl: UILabel!
    @IBOutlet weak var discountLbl: UILabel!
    @IBOutlet weak var finalFeesLbl: UILabel!
    @IBOutlet weak var dateBtn: UIButton!
    @IBOutlet weak var promoCodeBtn: UIButton!
    
    var datesDropDown = DropDown()
    var promoCodesDropDown = DropDown()
    var datesArr = [String]()
    var promoCodesArr = [String]()
    
    var promoCodes = [PromoCode]()
    var discount = 0
    
    var centerMobile = ""
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        callCenterBtn.frame = CGRect(x: 16, y: 0, width: self.view.frame.width / 2 - 32, height: 44)
        bookNowBtn.frame = CGRect(x: callCenterBtn.frame.maxX + 32, y: 0, width: self.view.frame.width / 2 - 32, height: 44)
        
        callCenterBtn.layer.cornerRadius = 20
        bookNowBtn.layer.cornerRadius = 20
        
        getUserPromo()
        getCenterMobile()
        
        self.feesLbl.text = "\(self.subCourse.fees) L.E"
        self.finalFeesLbl.text = "\(self.subCourse.fees) L.E"
        
        print(subCourse.startingDateArrayList)
        
        for date in subCourse.startingDateArrayList{
            
            datesArr.append(date.date)
            print(date.date)
            
        }
        
        setupDatesDropDown()
        
    }

    struct JSONStringArrayEncoding: ParameterEncoding {
        private let myString: String
        
        init(string: String) {
            self.myString = string
        }
        
        func encode(_ urlRequest: URLRequestConvertible, with parameters: Parameters?) throws -> URLRequest {
            var urlRequest = urlRequest.urlRequest
            
            let data = myString.data(using: .utf8)!
            
            if urlRequest?.value(forHTTPHeaderField: "Content-Type") == nil {
                urlRequest?.setValue("application/json", forHTTPHeaderField: "Content-Type")
            }
            
            urlRequest?.httpBody = data
            
            return urlRequest!
        }
        
    }
    
    @IBAction func bookNowOnClick(_ sender: Any) {
        
        guard let ind = datesDropDown.indexForSelectedRow else{
          
            let alert = UIAlertController(title: "Error", message: "Please select starting date", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            return
        }
        
        let url = Consts.BOOKING + "\(UserDefaults.standard.integer(forKey: "id"))/"
        print(url)
        
        var promoCode = "\"\""
        if discount != 0{
            let codeID = promoCodes[promoCodesDropDown.indexForSelectedRow!].id
            promoCode = "\(codeID)"
            print("promoCodeFound: \(codeID)")
        }
        
        var request = URLRequest(url: URL(string: url)!)
        request.httpMethod = HTTPMethod.post.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let postString = "{\"subCourse\":\(subCourse.subCourseID), \"promoCode\":\(promoCode), \"startingDate\":\"\(datesDropDown.selectedItem!)\"}"
        
        print(postString)
        
        request.httpBody = postString.data(using: .utf8)
        
        Alamofire.request(request).responseJSON { (response) in
            
            
            print(response)

            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                let success = json["booking"].boolValue
                
                if success{
                    print("booked")
                    
                    let alert = UIAlertController(title: "Success", message: "Course is booked successfully", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: {
                        
                        (action) in
                        
                        let vc = self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as? UINavigationController
                        self.sideMenuController?.embed(centerViewController: vc!)
                        
                    }))
                    self.present(alert, animated: true, completion: nil)
                    
                    return
                }
                
                if let error = json["error"].string{
                    let alert = UIAlertController(title: "Error", message: error, preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    return
                }
                
                if let errors = json["errors"].string{
                    let alert = UIAlertController(title: "Error", message: errors, preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    return
                }
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    func getCenterMobile(){
        
        Alamofire.request(Consts.COMPLETE_PROFILE + "\(subCourse.centerID)/").responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let mobile = json["mobile"].string{
                    self.centerMobile = mobile
                }
                
                break
                
            case .failure(let error):
                print(error)
                self.callCenterBtn.isHidden = true
                break
            }
        }
        
    }
    
    @IBAction func dateBtnOnClick(_ sender: Any) {
        
        datesDropDown.show()
    }
    
    @IBAction func promoCodeBtnOnClick(_ sender: Any) {
        promoCodesDropDown.show()
    }
    
    @IBAction func callCenterBtnOnClick(_ sender: Any) {
        
        guard let number = URL(string: "telprompt://" + centerMobile) else { return }
        if #available(iOS 10.0, *) {
            UIApplication.shared.open(number)
        } else {
            // Fallback on earlier versions
            UIApplication.shared.openURL(number)
        }
        
    }
    
    func getUserPromo(){
        
        let url = Consts.PROMO_USER + "\(UserDefaults.standard.integer(forKey: "id"))/"
        
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                for (_, subJson):(String, JSON) in json{
                    
                    let id = subJson["promoCode"]["id"].intValue
                    let code = subJson["promoCode"]["promoCode"].stringValue
                    let discount = subJson["promoCode"]["discount"].intValue
                    
                    self.promoCodes.append(PromoCode.init(discount: discount, code: code, id: id))
                    self.promoCodesArr.append(code + " - \(discount) %")
                }
            
                self.setupPromoCodesDropDown()
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
                
        }
    }
    
    func setupDatesDropDown(){
        
        datesDropDown.frame = self.dateBtn.frame
        datesDropDown.anchorView = dateBtn
        datesDropDown.direction = .any
        datesDropDown.dismissMode = .onTap
        datesDropDown.dataSource = datesArr
        
        // Action triggered on selection
        datesDropDown.selectionAction = { [weak self] (index, item) in
            self?.dateBtn.setTitle(item, for: .normal)
           
        }
    }
    
    func setupPromoCodesDropDown(){
        
        promoCodesDropDown.frame = self.promoCodeBtn.frame
        promoCodesDropDown.anchorView = promoCodeBtn
        promoCodesDropDown.direction = .any
        promoCodesDropDown.dismissMode = .onTap
        promoCodesDropDown.dataSource = promoCodesArr
        
        // Action triggered on selection
        promoCodesDropDown.selectionAction = { [weak self] (index, item) in
            self?.promoCodeBtn.setTitle(item, for: .normal)
            
            self?.discount = (self?.promoCodes[index].discount)!
            self?.discountLbl.text = "\(self?.discount ?? 0) %"
            self?.finalFeesLbl.text = "\((self?.subCourse.fees)! * (100 - (self?.discount)!) / 100) L.E"
            
        }
        
    }
    
}

extension String: ParameterEncoding {
    
    public func encode(_ urlRequest: URLRequestConvertible, with parameters: Parameters?) throws -> URLRequest {
        var request = try urlRequest.asURLRequest()
        request.httpBody = data(using: .utf8, allowLossyConversion: false)
        return request
    }
    
}
