//
//  PromotionsTVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class PromotionsTVC: UITableViewController {

    var items = [PromoCode]()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        
        getData()
    }
    
    @IBAction func sideMenuAction(_ sender: Any) {
        
        self.sideMenuController?.toggle()
        
    }

    @IBAction func addPromoCodeOnClick(_ sender: Any) {
        let vc =  self.storyboard?.instantiateViewController(withIdentifier: "QRScanner") as! QRScanner
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    func getData(){
        
        items.removeAll()
        
        let url = Consts.PROMO_USER + "\(UserDefaults.standard.integer(forKey: "id"))/"
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                for (_, subJson):(String, JSON) in json{
                    
                    let id = subJson["promoCode"]["id"].intValue
                    let code = subJson["promoCode"]["promoCode"].stringValue
                    let discount = subJson["promoCode"]["discount"].intValue
                    
                    self.items.append(PromoCode.init(discount: discount, code: code, id: id))
                    
                }
                
                self.tableView.reloadData()
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "PromotionTVCell", for: indexPath) as! PromotionTVCell

        // Configure the cell...
        cell.discountLbl.text = " \(items[indexPath.row].discount)%"
        cell.promoCodeLbl.text = items[indexPath.row].code
        

        return cell
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150
    }
    
}
