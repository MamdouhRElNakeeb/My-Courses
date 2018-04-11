//
//  CenterInfoTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

class CenterInfoTVC: UITableViewController {

    var center = Center()
    var info = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        
        
        self.tableView.estimatedRowHeight = 400
        self.tableView.rowHeight = UITableViewAutomaticDimension
        self.tableView.separatorStyle = UITableViewCellSeparatorStyle.none
        
        self.tableView.setNeedsLayout()
        self.tableView.layoutIfNeeded()
        
        
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return info.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CenterInfoTVCell", for: indexPath) as! CenterInfoTVCell
        cell.centerInfoLbl.text = info[indexPath.row]
        
        if indexPath.row == 0{
            cell.centerInfoLbl.font = UIFont.boldSystemFont(ofSize: 50)
        }
//        switch indexPath.row {
//        case 0:
//            cell.centerInfoLbl.text = "\n\n\n"
//            cell.centerInfoLbl.font = UIFont.boldSystemFont(ofSize: 50)
//            break
//
//        case 1:
//            cell.centerInfoLbl.text = "Info"
//            cell.centerInfoLbl.font = UIFont.boldSystemFont(ofSize: 17)
//            break
//
//        case 2:
//
//            cell.centerInfoLbl.text = self.center.info
//            break
//        case 3:
//
//            cell.centerInfoLbl.text = " "
//            break
//        case 4:
//            cell.centerInfoLbl.text = "Address"
//            cell.centerInfoLbl.font = UIFont.boldSystemFont(ofSize: 17)
//            break
//
//        case 5:
//            cell.centerInfoLbl.text = self.center.address
//            break
//
//        default:
//            break
//        }
        
        return cell
    }

}
