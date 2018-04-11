//
//  SideMenuVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

class SideMenuVC: UIViewController {
    
    @IBOutlet weak var bgUp: UIImageView!
    @IBOutlet weak var bgDown: UIImageView!
    @IBOutlet weak var blueTintIV: UIImageView!
    @IBOutlet weak var gradientUp: GradientView!
    @IBOutlet weak var gradientDown: GradientView!
    
    var menuTV = UITableView()
    var logoIV = UIImageView()
    
    let menuItems = 8
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        menuTV.dataSource = self
        menuTV.delegate = self
        menuTV.frame = CGRect(x: 30, y: view.frame.height / 2 - 20 * CGFloat(menuItems) / 2, width: view.frame.width * 2 / 3 - 60, height: 50 * CGFloat(menuItems))
        menuTV.backgroundColor = UIColor.clear
        
        logoIV.frame = CGRect(x: menuTV.frame.minX, y: menuTV.frame.minY - 150, width: menuTV.frame.width, height: 130)
        logoIV.image = UIImage(named: "logo")
        logoIV.contentMode = .scaleAspectFit
       
        view.addSubview(menuTV)
        view.addSubview(logoIV)
    
        setupViews()
    }
    
    func setupViews(){
        
        bgUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        bgDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        blueTintIV.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        gradientUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        
    }
}

extension SideMenuVC: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        switch indexPath.row {
        case 0:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("home", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        case 1:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("my_bookings", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        case 2:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("promo_code", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        case 3:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("invite_friends", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        case 4:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("settings", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        case 5:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Facebook"
            cell.backgroundColor = UIColor.clear
            return cell
        case 6:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Instagram"
            cell.backgroundColor = UIColor.clear
            return cell
        default:
            let cell = UITableViewCell()
            cell.textLabel?.text = NSLocalizedString("logout", tableName: "localized" ,comment: "")
            cell.backgroundColor = UIColor.clear
            return cell
        }
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return menuItems
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
        case 1:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "BookingsNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        case 2:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "PromotionsNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        case 3:
            // Invite Friends
            let text = "Try My Courses App now. \n \(Consts.WEBSITE)"
            
            // set up activity view controller
            let textToShare = [ text ]
            let activityViewController = UIActivityViewController(activityItems: textToShare, applicationActivities: nil)
            activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
            
            // exclude some activity types from the list (optional)
//            activityViewController.excludedActivityTypes = [ UIActivityType.airDrop, UIActivityType.postToFacebook ]
            
            // present the view controller
            self.present(activityViewController, animated: true, completion: nil)
            break
            
        case 4:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "SettingsNC") as? UINavigationController
            //            self.navigationController?.pushViewController(vc!, animated: true)
            sideMenuController?.embed(centerViewController: vc!)
            break
        case 5:
            
            UIApplication.tryURL(urls: [
                "fb://profile/mycoursesapp", // App
                "http://www.facebook.com/mycoursesapp" // Website if app fails
                ])
            
            break
        case 6:
            
            UIApplication.tryURL(urls: [
                "instagram://user?username=mycoursesapp", // App
                "http://www.instagram.com/mycoursesapp" // Website if app fails
                ])
            
            break
            
        default:
            UserDefaults.standard.set(false, forKey: "login")
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "LoginMainVC") as? LoginMainVC
            sideMenuController?.embed(centerViewController: vc!)
            break
        }
    }
}

