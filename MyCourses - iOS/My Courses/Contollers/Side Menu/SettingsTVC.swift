//
//  SettingsTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/3/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import DropDown

class SettingsTVC: UITableViewController {

    @IBOutlet weak var versionNoLbl: UILabel!
    @IBOutlet weak var langBtn: UIButton!
    
    var langDropDown = DropDown()
    
    var languages = ["English", "العربية"]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        if let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
            self.versionNoLbl.text = version
        }
        
        setupLanguagesDropDown()
    }

    @IBAction func sideMenuBtnOnClick(_ sender: Any) {
        self.sideMenuController?.toggle()
    }
    
    func setupLanguagesDropDown(){
        
        let langStr = Locale.preferredLanguages[0]
        
        print(langStr)
        
        if langStr == "en"{
            self.langBtn.setTitle("Language: \(languages[0])", for: .normal)
        }
        else{
            self.langBtn.setTitle("Language: \(languages[1])", for: .normal)
        }
        
        
        langDropDown.frame = self.langBtn.frame
        langDropDown.anchorView = self.langBtn
        langDropDown.direction = .any
        langDropDown.dismissMode = .onTap
        langDropDown.dataSource = languages
        
        // Action triggered on selection
        langDropDown.selectionAction = { [weak self] (index, item) in
            self?.langBtn.setTitle("Language: \(item)", for: .normal)
            
            let message: String = "Are you sure you want to change language to \(item) ? \n The app should restart"
            let alertController = UIAlertController(title: "Change Language", message: message, preferredStyle: UIAlertControllerStyle.alert)
            
            let dismissAction = UIAlertAction(title: "Cancel", style: UIAlertActionStyle.default) { (action) -> Void in
            }
            
            let OkAction = UIAlertAction(title: "Ok", style: UIAlertActionStyle.default) { (action) -> Void in
                
                if index == 0{
                    UserDefaults.standard.set(["en"], forKey: "AppleLanguages")
                }
                else{
                    UserDefaults.standard.set(["ar"], forKey: "AppleLanguages")
                }
                
                UserDefaults.standard.synchronize()
                
                exit(0)
                
            }
            
            alertController.addAction(dismissAction)
            alertController.addAction(OkAction)
            
            self?.present(alertController, animated: true, completion: nil)
            
            
        }
    }
    
    @IBAction func languageBtnOnClick(_ sender: Any) {
        langDropDown.show()
    }
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            
            langDropDown.show()
            break
            
        case 1:
            UIApplication.tryURL(urls: [Consts.APP_ABOUT])
            break
        
        case 2:
            UIApplication.tryURL(urls: [Consts.APP_TERMS])
            break
            
        default:
            break
        }
    }
}
