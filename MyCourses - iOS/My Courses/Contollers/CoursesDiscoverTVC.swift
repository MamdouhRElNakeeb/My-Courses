//
//  CoursesDiscoverTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/12/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class CoursesDiscoverTVC: UITableViewController {

    
    var items = Array<Category>()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        getData()
    }

    func getData() {
        
        Alamofire.request(Consts.CATEGORIES).responseJSON{
            
            response in
            
            switch response.result {
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                for (_, subJson):(String, JSON) in json {
                    // Do something you want
                    var categoryName = subJson["category"].stringValue
                    categoryName = categoryName.replacingOccurrences(of: "_", with: " ")
                    
                    self.items.append(Category.init(id: subJson["id"].intValue, name: categoryName))
                }
                
                self.tableView.reloadData()
                
            case .failure(let error):
                print(error)
                
                let alert = UIAlertController(title: "Error", message: "An error occurred, Try again later!", preferredStyle: UIAlertControllerStyle.alert)
                alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                return
            }
        }
    }
    
    // MARK: - Table view data source
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return items.count
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CategoryTVCell", for: indexPath) as! CoursesDiscoverTVCell

        // Configure the cell...
        cell.name.text = items[indexPath.row].name
        cell.img.sd_setImage(with: URL(string: Consts.SERVER + items[indexPath.row].img), placeholderImage: UIImage(named: "bg1"))

        return cell
    }
    

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        let vc =  self.storyboard?.instantiateViewController(withIdentifier: "CoursesTVC") as! CoursesTVC
        vc.url = Consts.COURSES_FILTER + items[indexPath.row].name
        self.navigationController?.pushViewController(vc, animated: true)
    }

}
