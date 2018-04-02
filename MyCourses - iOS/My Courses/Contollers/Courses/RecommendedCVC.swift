//
//  RecommendedCVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/11/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import Alamofire
import SwiftyJSON

class RecommendedCVC: UICollectionViewController {

    
    var items = Array<Course>()
    var url = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()

        initCollectionView()
        getData()

    }

    func initCollectionView(){
        
        let itemSize = self.view.frame.width / 2
        
        let flowLayout = UICollectionViewFlowLayout()
        
        flowLayout.itemSize = CGSize(width: itemSize, height: itemSize)
        flowLayout.scrollDirection = .vertical
        flowLayout.minimumInteritemSpacing = 0
        flowLayout.minimumLineSpacing = 0
        
        collectionView?.collectionViewLayout = flowLayout
        
        collectionView?.setCollectionViewLayout(flowLayout, animated: true)
        collectionView?.register(RecCourseCell.self, forCellWithReuseIdentifier: "RecCourseCell")
        collectionView?.dataSource = self
        collectionView?.delegate = self
        collectionView?.backgroundColor = UIColor.white
        collectionView?.allowsSelection = true
        
    }

    func getData() {
        
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(value)")
                for (key, subJson):(String, JSON) in json{
                    
                    let id = subJson["id"].intValue
                    let name = subJson["courseName"].stringValue
                    let slogan = subJson["courseSlogun"].stringValue
                    let img = subJson["courseImage"].stringValue
                    
                    self.items.append(Course.init(id: id, name: name, slogan: slogan, img: img))
                }
                
                self.collectionView?.reloadData()
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
        }
    }
    
    
    // MARK: UICollectionViewDataSource
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of items
        return items.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "RecCourseCell", for: indexPath) as! RecCourseCell
    
        // Configure the cell
        cell.name.text = items[indexPath.row].name
        cell.img.sd_setImage(with: URL(string: Consts.SERVER + items[indexPath.row].img), placeholderImage: UIImage(named: "logo"))
    
        return cell
    }

    // MARK: UICollectionViewDelegate
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        collectionView.deselectItem(at: indexPath, animated: true)
        
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "CourseProfileVC") as! CourseProfileVC
        vc.courseID = items[indexPath.row].id
        self.navigationController?.pushViewController(vc, animated: true)
        
    }

}
