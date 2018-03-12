//
//  RegisterPage.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/5/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import FSPagerView

class RegisterPage: FSPagerViewCell {
    
    var title = UILabel()
    var nameTF = UITextField()
    var nameIV = UIImageView()
    var line0 = UIView()
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
    
    var registerDelegate: RegisterDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        self.contentView.layer.shadowColor = UIColor.clear.cgColor
        
        title.frame = CGRect(x: 0, y: 20, width: self.contentView.frame.width, height: 30)
        title.text = "REGISTER"
        title.font = UIFont.systemFont(ofSize: 25)
        title.textAlignment = .center
        title.textColor = UIColor.primaryColor()
        
        nameIV.frame = CGRect(x: 20, y: title.frame.maxY + 20, width: 30, height: 30)
        nameIV.image = UIImage(named: "profile_icon")?.withRenderingMode(.alwaysTemplate)
        nameIV.tintColor = UIColor.primaryColor()
        
        nameTF.frame = CGRect(x: nameIV.frame.maxX + 10, y: nameIV.frame.minY, width: contentView.frame.width - nameIV.frame.width - 50, height: 40)
        nameTF.placeholder = "Name"
        
        line0.frame = CGRect(x: nameIV.frame.minX, y: nameIV.frame.maxY, width: nameTF.frame.maxX - nameIV.frame.minX, height: 1)
        line0.backgroundColor = UIColor.primaryColor()
        
        
        emailIV.frame = CGRect(x: 20, y: line0.frame.maxY + 20, width: 30, height: 30)
        emailIV.image = UIImage(named: "mail_icn")?.withRenderingMode(.alwaysTemplate)
        emailIV.tintColor = UIColor.primaryColor()
        
        emailTF.frame = CGRect(x: emailIV.frame.maxX + 10, y: emailIV.frame.minY, width: contentView.frame.width - emailIV.frame.width - 50, height: 40)
        emailTF.placeholder = "Email Address"
        emailTF.keyboardType = .emailAddress
        emailTF.autocapitalizationType = .none
        
        line1.frame = CGRect(x: emailIV.frame.minX, y: emailIV.frame.maxY, width: emailTF.frame.maxX - emailIV.frame.minX, height: 1)
        line1.backgroundColor = UIColor.primaryColor()
        
        passIV.frame = CGRect(x: emailIV.frame.minX, y: line1.frame.maxY + 20, width: 30, height: 30)
        passIV.image = UIImage(named: "lock_icn")?.withRenderingMode(.alwaysTemplate)
        passIV.tintColor = UIColor.primaryColor()
        
        passTF.frame = CGRect(x: emailTF.frame.minX, y: passIV.frame.minY, width: emailTF.frame.width, height: emailTF.frame.height)
        passTF.placeholder = "Password"
        passTF.isSecureTextEntry = true
        
        line2.frame = CGRect(x: line1.frame.minX, y: passTF.frame.maxY, width: line1.frame.width, height: line1.frame.height)
        line2.backgroundColor = line1.backgroundColor
        
        
        loginBtn.frame = CGRect(x: line1.frame.minX, y: line2.frame.maxY + 20, width: line1.frame.width, height: 40)
        
        loginBtn.setTitle("Register", for: .normal)
        loginBtn.backgroundColor = UIColor.primaryColorDark()
        loginBtn.layer.cornerRadius = 20
        loginBtn.layer.masksToBounds = true
        
        loginBtn.addTarget(self, action: #selector(registerBtnOnClick), for: .touchUpInside)
        
        socialLoginLbl.frame = CGRect(x: line1.frame.minX, y: loginBtn.frame.maxY + 20, width: line1.frame.width, height: 21)
        socialLoginLbl.text = "Or signup using"
        socialLoginLbl.textColor = UIColor.gray
        socialLoginLbl.textAlignment = .center
        
        
        fbBtn.frame = CGRect(x: contentView.frame.midX - 60, y: socialLoginLbl.frame.maxY + 20, width: 50, height: 50)
        fbBtn.setImage(UIImage(named: "fb_icn"), for: .normal)
        fbBtn.addTarget(self, action: #selector(fbRegister), for: .touchUpInside)
        
        twtBtn.frame = CGRect(x: contentView.frame.midX + 20, y: fbBtn.frame.minY, width: 50, height: 50)
        twtBtn.setImage(UIImage(named: "twt_icn"), for: .normal)
        
        twtBtn.addTarget(self, action: #selector(twtRegister), for: .touchUpInside)
        
        
        self.contentView.addSubview(title)
        self.contentView.addSubview(nameIV)
        self.contentView.addSubview(nameTF)
        self.contentView.addSubview(line0)
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
    
    @objc func fbRegister(_ sender: UIButton) {
        
        registerDelegate?.registerUserWithSocial(provider: Consts.FACEBOOK_PROVIDER)
    }
    
    @objc func twtRegister(_ sender: UIButton) {
        
        registerDelegate?.registerUserWithSocial(provider: Consts.TWITTER_PROVIDER)
    }
    
    @objc func registerBtnOnClick(_ sender: UIButton) {
        
        registerDelegate?.registerUserWithMail(name: nameTF.text!, email: emailTF.text!, password: passTF.text!)
    }
}

protocol RegisterDelegate: class{
    
    func registerUserWithMail(name: String, email: String, password: String)
    func registerUserWithSocial(provider: String)
    
}
