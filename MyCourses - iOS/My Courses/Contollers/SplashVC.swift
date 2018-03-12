//
//  SplashVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SideMenuController

class SplashVC: UIViewController {
    
    @IBOutlet weak var bgUp: UIImageView!
    @IBOutlet weak var bgDown: UIImageView!
    @IBOutlet weak var blueTintIV: UIImageView!
    @IBOutlet weak var gradientUp: GradientView!
    @IBOutlet weak var gradientDown: GradientView!
    @IBOutlet weak var logoIV: UIImageView!
    
    var count : Int = 2
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        _ = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(update), userInfo: nil, repeats: true)
        
        setupViews()
    }
    
    func setupViews(){
        
        bgUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        bgDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        blueTintIV.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        gradientUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        
        let width = self.view.frame.width * 0.6
        
        logoIV.frame = CGRect(x: self.view.frame.width * 0.2, y: self.view.frame.height / 2 - width / 2, width: width, height: width)
        
    }
    
    @objc func update() {
        if(count > 0) {
            count -= 1
        }
        else{
            showVC()
        }
    }
    
    func showVC()  {
        if UserDefaults.standard.bool(forKey: "login") {
            
            
            let mainVC =  self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as! UINavigationController
            //            self.present(mainVC, animated: true, completion: nil)
            
            let sideMenuVC =  self.storyboard?.instantiateViewController(withIdentifier: "SideMenuVC") as! SideMenuVC
            let sideMenuViewController = SideMenuController()
            // embed the side and center controllers
            sideMenuViewController.embed(sideViewController: sideMenuVC)
            sideMenuViewController.embed(centerViewController: mainVC)
            
            show(sideMenuViewController, sender: nil)
            
        }
        else{
            
            let loginVC =  self.storyboard?.instantiateViewController(withIdentifier: "LoginMainVC") as! LoginMainVC
            self.present(loginVC, animated: true, completion: nil)
            
        }
    }

}
