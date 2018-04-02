//
//  SubCoursesVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/31/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

class SubCoursesTVC: UITableViewController {

    
    var items = [SubCourse]()
    
    var isCenterProfile = false
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        
    }
    
    @objc func bookCourse(_ sender: UIButton){
        
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "BookCourseVC") as! BookCourseVC
        vc.subCourse = items[sender.tag]
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "SubCourseTVCell", for: indexPath) as! SubCourseTVCell

        print(items[indexPath.row].instructorName)
        // Configure the cell...
        cell.bookBtn.tag = indexPath.row
        cell.bookBtn.addTarget(self, action: #selector(bookCourse(_:)), for: .touchUpInside)
        
        cell.instructorNameLbl.text = items[indexPath.row].instructorName
        cell.startingDateLbl.text = items[indexPath.row].startingDateArrayList[0].date
        cell.feesLbl.text = "\(items[indexPath.row].fees) L.E"
        cell.rateLbl.text = "\(items[indexPath.row].rate)"
        cell.img.sd_setImage(with: URL(string: Consts.SERVER + items[indexPath.row].imagesAL[0]), placeholderImage: UIImage(named: "logo"))
        
        if isCenterProfile{
            cell.centerNameLbl.text = items[indexPath.row].name // Course name
        }
        else{
            cell.centerNameLbl.text = items[indexPath.row].slogan //  Center name instead of course name
        }
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        if isCenterProfile{
            
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "CourseProfileVC") as! CourseProfileVC
            vc.courseID = items[indexPath.row].id
            self.navigationController?.pushViewController(vc, animated: true)
            
        }
        else {
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "CenterProfileVC") as! CenterProfileVC
            vc.centerID = items[indexPath.row].centerID
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 130
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return items.count
    }

}
