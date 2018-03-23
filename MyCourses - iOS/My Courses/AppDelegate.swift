//
//  AppDelegate.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/1/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import FacebookCore
import GoogleSignIn
import TwitterKit
import DropDown
import GoogleMaps

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        // Facebook config
        SDKApplicationDelegate.shared.application(application, didFinishLaunchingWithOptions: launchOptions)
        // twitter Config
        TWTRTwitter.sharedInstance().start(withConsumerKey: Consts.TWT_CKEY, consumerSecret: Consts.TWT_CSECRET)
        // Initialize Google sign-in
//        GIDSignIn.sharedInstance().clientID = Consts.GOOGLE_CLIENT_ID
        
        DropDown.startListeningToKeyboard()
        GMSServices.provideAPIKey("AIzaSyAtxdt3WBppHr9sNSAKJFtbpA_q3YONk1o")
        
        return true
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplicationOpenURLOptionsKey : Any] = [:]) -> Bool {
        // twitter config
        if TWTRTwitter.sharedInstance().application(app, open: url, options: options) {
            return true
        }
        // google config
//        GIDSignIn.sharedInstance().handle(url,
//                                          sourceApplication: options[UIApplicationOpenURLOptionsKey.sourceApplication] as? String,
//                                          annotation: options[UIApplicationOpenURLOptionsKey.annotation])
        
        return SDKApplicationDelegate.shared.application(app, open: url, options: options)
    }

}

