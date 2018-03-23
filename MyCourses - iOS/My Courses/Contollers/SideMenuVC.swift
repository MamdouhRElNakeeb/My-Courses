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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        
        menuTV.dataSource = self
        menuTV.delegate = self
        menuTV.frame = CGRect(x: 20, y: view.frame.height / 2 - 50 * 5 / 2, width: view.frame.width * 2 / 3 - 60, height: 50 * 5)
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
            cell.textLabel?.text = "My Bookings"
            cell.backgroundColor = UIColor.clear
            return cell
        case 1:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Promo Code"
            let badge = UILabel()
            badge.frame = CGRect(x: menuTV.frame.width - 50, y: 10, width: 30, height: 30)
            badge.backgroundColor = UIColor.red
            badge.layer.cornerRadius = 15
            badge.layer.masksToBounds = true
            badge.text = "1"
            badge.textColor = UIColor.white
            badge.textAlignment = .center
            //            cell.contentView.addSubview(badge)
            cell.backgroundColor = UIColor.clear
            return cell
        case 2:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Invite Friends"
            cell.backgroundColor = UIColor.clear
            return cell
        case 3:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Settings"
            cell.backgroundColor = UIColor.clear
            return cell
        default:
            let cell = UITableViewCell()
            cell.textLabel?.text = "Logout"
            cell.backgroundColor = UIColor.clear
            return cell
        }
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 5
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "homeNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        case 1:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "ApplicationsNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        case 2:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "AboutNC") as? UINavigationController
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        case 3:
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "ContactNC") as? UINavigationController
            //            self.navigationController?.pushViewController(vc!, animated: true)
            sideMenuController?.embed(centerViewController: vc!)
            break
            
        default:
            UserDefaults.standard.set(false, forKey: "login")
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "LoginMainVC") as? LoginMainVC
            sideMenuController?.embed(centerViewController: vc!)
            break
        }
    }
}

