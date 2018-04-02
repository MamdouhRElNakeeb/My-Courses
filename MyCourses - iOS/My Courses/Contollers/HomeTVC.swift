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
    
    var searchController = UISearchController()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Large Navigation Bar with Search Bar
        self.navigationController?.navigationBar.prefersLargeTitles = false
        self.navigationController?.navigationItem.largeTitleDisplayMode = .never
        searchController = UISearchController(searchResultsController: nil)
        searchController.searchResultsUpdater = self
        searchController.obscuresBackgroundDuringPresentation = false
        searchController.searchBar.delegate = self
        
        searchController.searchBar.textColor = UIColor.white
        
        navigationItem.searchController = searchController
        navigationItem.hidesSearchBarWhenScrolling = false
        
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
            vc.url = Consts.RECOMMENDED + "\(UserDefaults.standard.integer(forKey: "id"))/"
            self.navigationController?.pushViewController(vc, animated: true)
            break
        case 1:
            let vc =  self.storyboard?.instantiateViewController(withIdentifier: "CoursesDiscoverTVC") as! CoursesDiscoverTVC
            self.navigationController?.pushViewController(vc, animated: true)
            break
        case 2:
            let vc =  self.storyboard?.instantiateViewController(withIdentifier: "CoursesTVC") as! CoursesTVC
            vc.url = Consts.COURSES_FILTER + "Language"
            self.navigationController?.pushViewController(vc, animated: true)
            break
        case 3:
            let vc =  self.storyboard?.instantiateViewController(withIdentifier: "CoursesTVC") as! CoursesTVC
            vc.url = Consts.COURSES_FILTER + "Human_Development"
            self.navigationController?.pushViewController(vc, animated: true)
        case 4:
            let vc =  self.storyboard?.instantiateViewController(withIdentifier: "CentersMapVC") as! CentersMapVC
            self.navigationController?.pushViewController(vc, animated: true)
            break
        default:
            break
        }
        
    }

}

extension HomeTVC: UISearchResultsUpdating, UISearchBarDelegate {
    // MARK: - UISearchResultsUpdating Delegate
    func updateSearchResults(for searchController: UISearchController) {
        //
        print(searchController.searchBar.text!)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        print(searchBar.text!)
        // TODO: Open search results
        let vc =  self.storyboard?.instantiateViewController(withIdentifier: "RecommendedCVC") as! RecommendedCVC
        vc.url = Consts.SEARCH + searchBar.text! + "/"
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
