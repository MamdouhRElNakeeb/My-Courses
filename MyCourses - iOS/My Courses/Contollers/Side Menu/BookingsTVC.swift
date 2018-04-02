//
//  BookingsTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/25/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON
import SideMenuController

class BookingsTVC: UITableViewController {

    var items = Array<Booking>()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        getBookings()
    }

    @IBAction func sideMenuAction(_ sender: Any) {
        
        self.sideMenuController?.toggle()
        
    }
    
    func getBookings(){
        
        let url = Consts.BOOKING_USER + "\(UserDefaults.standard.integer(forKey: "id"))/"
        print(url)
        
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
                for (key, subJson):(String, JSON) in json{
                    
                    let id = subJson["id"].intValue
                    let centerName = subJson["centreName"].stringValue
                    let courseImage = subJson["courseImage"].stringValue
                    let courseName = subJson["courseName"].stringValue
                    let startDate = subJson["startData"].stringValue
                    
                    self.items.append(Booking.init(id: id, centerName: centerName, courseImage: courseImage, courseName: courseName, startDate: startDate))
                }
                
                print(self.items)
                self.tableView?.reloadData()
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    @objc func cancelBooking(_ sender: UIButton){
        
        let url = Consts.BOOKING_CANCEL + "\(items[sender.tag].id)/"
        
        let params: Parameters = [:]
        Alamofire.request(url, method: .delete, parameters: params).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
                
                let deleted = json["deleted"].boolValue
                    
                if deleted{
                    
                    self.items.remove(at: sender.tag)
                    self.tableView?.reloadData()
                }
                
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
        }
        
        
    }
    
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return items.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "BookingTVCell", for: indexPath) as! BookingTVCell

        // Configure the cell...
        cell.centerName.text = items[indexPath.row].centerName
        cell.courseName.text = items[indexPath.row].courseName
        cell.courseDate.text = items[indexPath.row].startDate
        cell.img.sd_setImage(with: URL(string: items[indexPath.row].courseImage), placeholderImage: UIImage(named: "books_bg"))
        
        cell.removeBtn.tag = indexPath.row
        cell.removeBtn.addTarget(self, action: #selector(cancelBooking(_:)), for: .touchUpInside)
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150
    }
}
