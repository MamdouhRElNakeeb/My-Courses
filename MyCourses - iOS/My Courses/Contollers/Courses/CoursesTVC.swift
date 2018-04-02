//
//  CoursesTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/12/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class CoursesTVC: UITableViewController {

    var items = Array<Course>()
    var url = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        getData()
    }

    func getData() {
                
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
                for (_, subJson):(String, JSON) in json{
                    
                    let id = subJson["id"].intValue
                    let name = subJson["courseName"].stringValue
                    let slogan = subJson["courseSlogun"].stringValue
                    let img = subJson["courseImage"].stringValue
                    
                    self.items.append(Course.init(id: id, name: name, slogan: slogan, img: img))
                }
                
                self.tableView.reloadData()
                
                break
                
            case .failure(let error):
                print(error)
                break
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
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "CourseTVCell", for: indexPath) as! CoursesDiscoverTVCell
        
        // Configure the cell...
        cell.name.text = items[indexPath.row].name
        cell.img.sd_setImage(with: URL(string: Consts.SERVER + items[indexPath.row].img), placeholderImage: UIImage(named: "bg1"))
        
        return cell
    }
 
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "CourseProfileVC") as! CourseProfileVC
        vc.courseID = items[indexPath.row].id
        self.navigationController?.pushViewController(vc, animated: true)
        
    }

}
