//
//  LoginMainVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import FSPagerView
import FacebookCore
import FacebookLogin
import Alamofire
import SwiftyJSON
import SideMenuController

class LoginMainVC: UIViewController {

    @IBOutlet weak var bgUp: UIImageView!
    @IBOutlet weak var bgDown: UIImageView!
    @IBOutlet weak var blueTintIV: UIImageView!
    @IBOutlet weak var gradientUp: GradientView!
    @IBOutlet weak var gradientDown: GradientView!
    @IBOutlet weak var gradientDown2: GradientView!
    @IBOutlet weak var logoIV: UIImageView!
    @IBOutlet weak var loginRegSwitchBtn: UIButton!
    
    @IBOutlet weak var pageControl: FSPageControl! {
        didSet {
            self.pageControl.numberOfPages = 2
            self.pageControl.currentPage = 0
            self.pageControl.contentHorizontalAlignment = .right
            self.pageControl.contentInsets = UIEdgeInsets(top: 0, left: 20, bottom: 0, right: 20)
            self.pageControl.hidesForSinglePage = true
            self.pageControl.setFillColor(UIColor.primaryColorDark(), for: .normal)
            self.pageControl.setFillColor(UIColor.primaryColor(), for: .selected)
        }
    }
    
    var pagerView = FSPagerView()
    
    var blurBG = UIImageView()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        setupViews()
    }
    
    func setupViews(){
        
        bgUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        bgDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        blueTintIV.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        gradientUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown2.frame = CGRect(x: 0, y: self.view.frame.height * 3 / 4, width: self.view.frame.width, height: self.view.frame.height / 4)
        
        blurBG.frame = CGRect(x: self.view.frame.width * 0.08, y: self.view.frame.height * 0.18, width: self.view.frame.width * 0.84, height: self.view.frame.height * 0.64)
        
        blurBG.image = UIImage(named: "blur_bg")
        
        blurBG.layer.cornerRadius = 30
        blurBG.layer.masksToBounds = true
        
        loginRegSwitchBtn.frame = CGRect(x: 0, y: blurBG.frame.maxY, width: self.view.frame.width, height: 30)
        loginRegSwitchBtn.addTarget(self, action: #selector(switchLoginReg(_:)), for: .touchUpInside)
        
        logoIV.frame = CGRect(x: 0, y: loginRegSwitchBtn.frame.maxY, width: self.view.frame.width, height: self.view.frame.maxY - loginRegSwitchBtn.frame.maxY)
        
        setupViewPager()
        
    }
    
    func setupViewPager(){
        
        // Create a pager view
        pagerView = FSPagerView(frame: blurBG.frame)
        pagerView.backgroundView = blurBG
        pagerView.layer.cornerRadius = 30
        pagerView.layer.masksToBounds = true
        
        pagerView.dropShadow2()
        
        pagerView.dataSource = self
        pagerView.delegate = self
        pagerView.register(LoginPage.self, forCellWithReuseIdentifier: "loginPage")
        pagerView.register(RegisterPage.self, forCellWithReuseIdentifier: "registerPage")
        self.view.addSubview(pagerView)
        // Create a page control
//        pageControl.frame = CGRect(x: 0, y: blurBG.frame.height - 40, width: pagerView.frame.width, height: 30)
//        pageControl.numberOfPages = 2
//        pageControl.currentPage = 0
//        pageControl.contentHorizontalAlignment = .center
//
//        pageControl.setFillColor(UIColor.primaryColor(), for: .normal)
//        pageControl.setFillColor(UIColor.primaryColorDark(), for: .selected)
        
//        self.view.addSubview(pageControl)
        
    }

    @objc func switchLoginReg(_ sender: UIButton){
        
        if pagerView.currentIndex == 0 {
            pagerView.scrollToItem(at: 1, animated: true)
        }
        else{
            pagerView.scrollToItem(at: 0, animated: true)
        }
        
    }
    
    func loginWithEmail(email: String, password: String){
        
        let params: Parameters = [
            "username": email.lowercased(),
            "password": password
        ]
        
        Alamofire.request(Consts.LOGIN, method: .post, parameters: params).responseJSON{
            
            response in
            
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let login = Bool(json["login"].stringValue){
                    
                    if login{
                        
                        if let staff = json["is_staff"].bool,
                            let superuser = json["is_superuser"].bool{
                            
                            if staff || superuser{
                                
                                let alert = UIAlertController(title: "Error", message: "You're not permitted to login here", preferredStyle: UIAlertControllerStyle.alert)
                                alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                                self.present(alert, animated: true, completion: nil)
                                return
                                
                            }
                        }
                        
                        UserDefaults.standard.set(true, forKey: "login")
                        UserDefaults.standard.set(json["first_name"].string, forKey: "name")
                        UserDefaults.standard.set(email, forKey: "email")
                        UserDefaults.standard.set(json["id"].int, forKey: "id")
                        
                        if let mobile = json["mobile"].string{
                            
                            if !mobile.isEmpty{
                                // Home
                                UserDefaults.standard.set(json["mobile"].string, forKey: "mobile")
                                self.goHome()
                            }
                            else{
                                // Complete Registration
                                let compReg =  self.storyboard?.instantiateViewController(withIdentifier: "CompRegNC") as! UINavigationController
                                self.present(compReg, animated: true, completion: nil)
                            }
                            
                        }
                        else{
                            // Complete Registration
                            let compReg =  self.storyboard?.instantiateViewController(withIdentifier: "CompRegNC") as! UINavigationController
                            self.present(compReg, animated: true, completion: nil)
                        }
                    }
                    else{
                        
                        let alert = UIAlertController(title: "Error", message: "Wrong Email or Password", preferredStyle: UIAlertControllerStyle.alert)
                        alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                        return
                        
                    }
                }
                else if let error = json["errors"].string{
                    
                    let alert = UIAlertController(title: "Error", message: error, preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    
                    return
                    
                }
                
            case .failure(let error):
                print(error)
                
                let alert = UIAlertController(title: "Error", message: "An error occuerd, Try again Later", preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                return
            }
        }
    }
    
    func registerWithEmail(name: String, email: String, password: String){
        
        let params: Parameters = [
            "first_name": name,
            "username": email.lowercased(),
            "password": password
        ]
        
        Alamofire.request(Consts.SIGN_UP, method: .post, parameters: params).responseJSON{
            
            response in
            
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let created = Bool(json["created"].stringValue){
                    
                    if created{
                        
                        UserDefaults.standard.set(true, forKey: "login")
                        UserDefaults.standard.set(name, forKey: "name")
                        UserDefaults.standard.set(email, forKey: "email")
                        UserDefaults.standard.set(json["id"].int, forKey: "id")
                        
                        // Complete Registration
                        let compReg =  self.storyboard?.instantiateViewController(withIdentifier: "CompRegNC") as! UINavigationController
                        self.present(compReg, animated: true, completion: nil)
                        
                    }
                    else{
                        
                        let alert = UIAlertController(title: "Error", message: "Wrong Email or Password", preferredStyle: UIAlertControllerStyle.alert)
                        alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                        
                        return
                        
                    }
                    
                }
                else if var error = json["username"][0].string{
                    
                    error = "User is already existed"
                    
                    let alert = UIAlertController(title: "Error", message: error, preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    
                    return
                    
                }
                else{
                    let alert = UIAlertController(title: "Error", message: "An error occuerd, Try again Later", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    
                    return
                }
                
                
            case .failure(let error):
                print(error)
                
                let alert = UIAlertController(title: "Error", message: "An error occuerd, Try again Later", preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                return
            }
            
        }
    }
    
    func goHome(){
        
        let mainVC =  self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as! UINavigationController
        self.present(mainVC, animated: true, completion: nil)
        
    }
}


extension LoginMainVC: FSPagerViewDelegate, FSPagerViewDataSource{
    
    func numberOfItems(in pagerView: FSPagerView) -> Int {
        return 2
    }
    
    func pagerView(_ pagerView: FSPagerView, cellForItemAt index: Int) -> FSPagerViewCell {
        
        if index == 0 {
            
            let page = pagerView.dequeueReusableCell(withReuseIdentifier: "loginPage", at: index) as! LoginPage
            page.loginDelegate = self
            return page
        }
        else{
            
            let page = pagerView.dequeueReusableCell(withReuseIdentifier: "registerPage", at: index) as! RegisterPage
            page.registerDelegate = self
            return page
        }
    }
    
    func pagerViewDidScroll(_ pagerView: FSPagerView) {
        guard self.pageControl.currentPage != pagerView.currentIndex else {
            return
        }
        self.pageControl.currentPage = pagerView.currentIndex // Or Use KVO with property "currentIndex"
        
        if pagerView.currentIndex == 0 {
            loginRegSwitchBtn.setTitle("Don't have an account? Register here", for: .normal)
        }
        else{
            loginRegSwitchBtn.setTitle("Already user? ... Login here", for: .normal)
        }
    }
    
}


extension LoginMainVC: LoginDelegate, RegisterDelegate{
    
    func loginUserWithMail(email: String, password: String) {
        
        print(email)
        print(password)
        
        if (email.isEmpty) {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your Email", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            print("missing email")
            return
        }
        
        if (password.isEmpty) {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your Password", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            print("missing password")
            return
        }
        
        self.loginWithEmail(email: email, password: password)
        
    }
    
    func loginUserWithSocial(provider: String) {
        
        
    }
    
    func registerUserWithMail(name: String, email: String, password: String) {
        
        print(name)
        print(email)
        print(password)
        
        if (name.isEmpty) {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your Name", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            print("missing name")
            return
        }
        
        if (email.isEmpty) {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your Email", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            print("missing email")
            return
        }
        
        if (password.isEmpty) {
            
            let alert = UIAlertController(title: "Error", message: "Please enter your Password", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Try again", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
            print("missing password")
            return
        }
        
        registerWithEmail(name: name, email: email, password: password)
    }
    
    func registerUserWithSocial(provider: String) {
        
        
        if provider == Consts.FACEBOOK_PROVIDER{
            
            let loginManager = LoginManager()
            
            loginManager.logIn(readPermissions: [ .publicProfile, .email ], viewController: self) { loginResult in
                
                switch loginResult {
                case .failed(let error):
                    print(error)
                case .cancelled:
                    print("User cancelled login.")
                case .success(let grantedPermissions, let declinedPermissions, let accessToken):
                    print("Logged in!")
//                    print("Login succeeded with granted permissions: \(grantedPermissions)")
                    print(accessToken)
                
//                    let params: Parameters = [
//                        "access_token": accessToken
//
//                    ]
//
//                    let headers: HTTPHeaders = [
//                        "sdk": "ios",
//                        "fields": "id,name,email",
//                        "include_headers": "false",
//                        "format": "json",
//                        "access_token": "\(accessToken)"
//                    ]
//
//                    Alamofire.request("https://graph.facebook.com/v2.11/me", method: .get, parameters: params, headers: headers).responseJSON{
//
//                        response in
//
//                        print(response)
//
//
//                    }
                    
                    let connection = GraphRequestConnection()
                    connection.add(MyProfileRequest()) { response, result in

                        print(response)

                        switch result {
                        case .success(let response):
                            print("Custom Graph Request Succeeded: \(response)")
//                            print("My facebook id is \(response.dictionaryValue?["id"])")
//                            print("My name is \(response.dictionaryValue?["name"])")
                        case .failed(let error):
                            print("Custom Graph Request Failed: \(error)")
                        }
                    }
                    connection.start()
                }
            }
                
        }
        else if provider == Consts.TWITTER_PROVIDER{
            
        }
        
        
    }
    
}

struct MyProfileRequest: GraphRequestProtocol {
    struct Response: GraphResponseProtocol {
        init(rawResponse: Any?) {
            // Decode JSON from rawResponse into other properties here.
        }
    }
    
    var graphPath = "/me"
    var parameters: [String : Any]? = ["fields": "id, name, email"]
    var accessToken = AccessToken.current
    var httpMethod: GraphRequestHTTPMethod = .GET
    var apiVersion: GraphAPIVersion = .defaultVersion
}
