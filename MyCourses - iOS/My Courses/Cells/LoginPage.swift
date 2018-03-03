//
//  LoginPage.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import Foundation
import UIKit
import FSPagerView

class LoginPage: FSPagerViewCell {
    
    var title = UILabel()
    var emailTF = UITextField()
    var emailIV = UIImageView()
    var line1 = UIView()
    var passTF = UITextField()
    var passIV = UIImageView()
    var line2 = UIView()
    var loginBtn = UIButton()
    
    var socialLoginLbl = UILabel()
    
    var fbBtn = UIButton()
    var twtBtn = UIButton()
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.contentView.layer.shadowColor = UIColor.clear.cgColor
        
        title.frame = CGRect(x: 0, y: 20, width: self.contentView.frame.width, height: 30)
        title.text = "LOGIN"
        title.font = UIFont.systemFont(ofSize: 25)
        title.textAlignment = .center
        title.textColor = UIColor.primaryColor()
        
        emailIV.frame = CGRect(x: 20, y: title.frame.maxY + 30, width: 40, height: 40)
        emailIV.image = UIImage(named: "mail_icn")?.withRenderingMode(.alwaysTemplate)
        emailIV.tintColor = UIColor.primaryColor()
        
        emailTF.frame = CGRect(x: emailIV.frame.maxX + 10, y: emailIV.frame.minY, width: contentView.frame.width - emailIV.frame.width - 50, height: 40)
        emailTF.placeholder = "Email Address"
        emailTF.keyboardType = .emailAddress
        
        line1.frame = CGRect(x: emailIV.frame.minX, y: emailIV.frame.maxY, width: emailTF.frame.maxX - emailIV.frame.minX, height: 1)
        line1.backgroundColor = UIColor.primaryColor()
        
        passIV.frame = CGRect(x: emailIV.frame.minX, y: line1.frame.maxY + 20, width: 40, height: 40)
        passIV.image = UIImage(named: "lock_icn")?.withRenderingMode(.alwaysTemplate)
        passIV.tintColor = UIColor.primaryColor()
        
        passTF.frame = CGRect(x: emailTF.frame.minX, y: passIV.frame.minY, width: emailTF.frame.width, height: emailTF.frame.height)
        passTF.placeholder = "Password"
        passTF.isSecureTextEntry = true
        
        line2.frame = CGRect(x: line1.frame.minX, y: passTF.frame.maxY, width: line1.frame.width, height: line1.frame.height)
        line2.backgroundColor = line1.backgroundColor
        
        
        loginBtn.frame = CGRect(x: line1.frame.minX, y: line2.frame.maxY + 20, width: line1.frame.width, height: 40)
        
        loginBtn.setTitle("Login", for: .normal)
        loginBtn.backgroundColor = UIColor.primaryColorDark()
        loginBtn.layer.cornerRadius = 20
        loginBtn.layer.masksToBounds = true
        
        
        socialLoginLbl.frame = CGRect(x: line1.frame.minX, y: loginBtn.frame.maxY + 20, width: line1.frame.width, height: 21)
        socialLoginLbl.text = "Or login using"
        socialLoginLbl.textColor = UIColor.gray
        socialLoginLbl.textAlignment = .center
        
        
        fbBtn.frame = CGRect(x: contentView.frame.midX - 60, y: socialLoginLbl.frame.maxY + 20, width: 50, height: 50)
        fbBtn.setImage(UIImage(named: "fb_icn"), for: .normal)
        
        twtBtn.frame = CGRect(x: contentView.frame.midX + 20, y: fbBtn.frame.minY, width: 50, height: 50)
        twtBtn.setImage(UIImage(named: "twt_icn"), for: .normal)
        
        self.contentView.addSubview(title)
        self.contentView.addSubview(emailIV)
        self.contentView.addSubview(emailTF)
        self.contentView.addSubview(line1)
        self.contentView.addSubview(passIV)
        self.contentView.addSubview(passTF)
        self.contentView.addSubview(line2)
        self.contentView.addSubview(loginBtn)
        self.contentView.addSubview(socialLoginLbl)
        self.contentView.addSubview(fbBtn)
        self.contentView.addSubview(twtBtn)
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
}
