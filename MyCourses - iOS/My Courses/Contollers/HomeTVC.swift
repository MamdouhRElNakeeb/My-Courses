//
//  HomeTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import SideMenuController

class HomeTVC: UITableViewController {

    @IBOutlet weak var recommenedIV: UIImageView!
    @IBOutlet weak var recGrad: GradientView!
    @IBOutlet weak var discoverIV: UIImageView!
    @IBOutlet weak var discGrad: GradientView!
    @IBOutlet weak var langIV: UIImageView!
    @IBOutlet weak var langGrad: GradientView!
    @IBOutlet weak var hdIV: UIImageView!
    @IBOutlet weak var hdGrad: GradientView!
    @IBOutlet weak var centersIV: UIImageView!
    @IBOutlet weak var cenGrad: GradientView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupViews()
    }

    func setupViews(){
        
        recommenedIV.layer.cornerRadius = 20
        recommenedIV.layer.masksToBounds = true
        recGrad.layer.cornerRadius = 20
        recGrad.layer.masksToBounds = true
        
        discoverIV.layer.cornerRadius = 20
        discoverIV.layer.masksToBounds = true
        discGrad.layer.cornerRadius = 20
        discGrad.layer.masksToBounds = true
        
        langIV.layer.cornerRadius = 20
        langIV.layer.masksToBounds = true
        langGrad.layer.cornerRadius = 20
        langGrad.layer.masksToBounds = true
        
        hdIV.layer.cornerRadius = 20
        hdIV.layer.masksToBounds = true
        hdGrad.layer.cornerRadius = 20
        hdGrad.layer.masksToBounds = true
        
        centersIV.layer.cornerRadius = 20
        centersIV.layer.masksToBounds = true
        cenGrad.layer.cornerRadius = 20
        cenGrad.layer.masksToBounds = true
    }
    
    @IBAction func sideMenuOnClick(_ sender: Any) {
        self.sideMenuController?.toggle()
    }
    
    // MARK: - Table view data source
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch indexPath.row {
        case 0:
            let vc =  self.storyboard?.instantiateViewController(withIdentifier: "RecommendedCVC") as! RecommendedCVC
            self.navigationController?.pushViewController(vc, animated: true)
            break
        default:
            break
        }
        
    }

}
