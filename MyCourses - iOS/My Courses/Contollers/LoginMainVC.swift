//
//  LoginMainVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import FSPagerView

class LoginMainVC: UIViewController {

    @IBOutlet weak var bgUp: UIImageView!
    @IBOutlet weak var bgDown: UIImageView!
    @IBOutlet weak var blueTintIV: UIImageView!
    @IBOutlet weak var gradientUp: GradientView!
    @IBOutlet weak var gradientDown: GradientView!
    @IBOutlet weak var gradientDown2: GradientView!
    @IBOutlet weak var logoIV: UIImageView!
    @IBOutlet weak var loginRegSwitchBtn: UIButton!
    
    var blurBG = UIImageView()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        setupViews()
    }
    
    func setupViews(){
        
        bgUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        bgDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        blueTintIV.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height)
        gradientUp.frame = CGRect(x: 0, y: 0, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown.frame = CGRect(x: 0, y: self.view.frame.height / 2, width: self.view.frame.width, height: self.view.frame.height / 2)
        gradientDown2.frame = CGRect(x: 0, y: self.view.frame.height * 3 / 4, width: self.view.frame.width, height: self.view.frame.height / 4)
        
        blurBG.frame = CGRect(x: self.view.frame.width * 0.08, y: self.view.frame.height * 0.18, width: self.view.frame.width * 0.84, height: self.view.frame.height * 0.64)
        
        blurBG.image = UIImage(named: "blur_bg")
        
        blurBG.layer.cornerRadius = 30
        blurBG.layer.masksToBounds = true
        
        loginRegSwitchBtn.frame = CGRect(x: 0, y: blurBG.frame.maxY, width: self.view.frame.width, height: 30)
        logoIV.frame = CGRect(x: 0, y: loginRegSwitchBtn.frame.maxY, width: self.view.frame.width, height: self.view.frame.maxY - loginRegSwitchBtn.frame.maxY)
        
        setupViewPager()
        
    }
    
    func setupViewPager(){
        
        // Create a pager view
        let pagerView = FSPagerView(frame: blurBG.frame)
        pagerView.backgroundView = blurBG
        pagerView.layer.cornerRadius = 30
        pagerView.layer.masksToBounds = true
        
        pagerView.dropShadow2()
        
        pagerView.dataSource = self
        pagerView.delegate = self
        pagerView.register(LoginPage.self, forCellWithReuseIdentifier: "loginPage")
        self.view.addSubview(pagerView)
        // Create a page control
        let pageControl = FSPageControl(frame: CGRect(x: 0, y: pagerView.frame.height - 40, width: pagerView.frame.width, height: 30))
        pageControl.numberOfPages = 2
        pageControl.currentPage = 0
        pageControl.contentHorizontalAlignment = .center
        
        pageControl.setFillColor(UIColor.primaryColor(), for: .normal)
        pageControl.setFillColor(UIColor.primaryColorDark(), for: .selected)
        
        self.view.addSubview(pageControl)
        
    }

}


extension LoginMainVC: FSPagerViewDelegate, FSPagerViewDataSource{
    
    func numberOfItems(in pagerView: FSPagerView) -> Int {
        return 2
    }
    
    func pagerView(_ pagerView: FSPagerView, cellForItemAt index: Int) -> FSPagerViewCell {
        
        if index == 0 {
            
            let page = pagerView.dequeueReusableCell(withReuseIdentifier: "loginPage", at: index) as! LoginPage
            
            return page
        }
        else{
            
            let page = pagerView.dequeueReusableCell(withReuseIdentifier: "loginPage", at: index) as! LoginPage
            
            return page
        }
    }
    
}
