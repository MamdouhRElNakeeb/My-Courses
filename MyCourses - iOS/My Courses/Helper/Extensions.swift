//
//  Extensions.swift
//  quick-assist
//
//  Created by Mamdouh El Nakeeb on 2/25/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit

@IBDesignable
class GradientView: UIView {
    
    @IBInspectable var startColor:   UIColor = .black { didSet { updateColors() }}
    @IBInspectable var endColor:     UIColor = .white { didSet { updateColors() }}
    @IBInspectable var startLocation: Double =   0.05 { didSet { updateLocations() }}
    @IBInspectable var endLocation:   Double =   0.95 { didSet { updateLocations() }}
    @IBInspectable var horizontalMode:  Bool =  false { didSet { updatePoints() }}
    @IBInspectable var diagonalMode:    Bool =  false { didSet { updatePoints() }}
    
    override class var layerClass: AnyClass { return CAGradientLayer.self }
    
    var gradientLayer: CAGradientLayer { return layer as! CAGradientLayer }
    
    func updatePoints() {
        if horizontalMode {
            gradientLayer.startPoint = diagonalMode ? CGPoint(x: 1, y: 0) : CGPoint(x: 0, y: 0.5)
            gradientLayer.endPoint   = diagonalMode ? CGPoint(x: 0, y: 1) : CGPoint(x: 1, y: 0.5)
        } else {
            gradientLayer.startPoint = diagonalMode ? CGPoint(x: 0, y: 0) : CGPoint(x: 0.5, y: 0)
            gradientLayer.endPoint   = diagonalMode ? CGPoint(x: 1, y: 1) : CGPoint(x: 0.5, y: 1)
        }
    }
    func updateLocations() {
        gradientLayer.locations = [startLocation as NSNumber, endLocation as NSNumber]
    }
    func updateColors() {
        gradientLayer.colors    = [startColor.cgColor, endColor.cgColor]
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        updatePoints()
        updateLocations()
        updateColors()
    }
}

extension UIColor {
    
    class func primaryColor() -> UIColor {
        
        return UIColor(red: 67/255, green: 97/255, blue: 120/255, alpha: 1)
    }
    
    class func primaryColorDark() -> UIColor {
        
        return UIColor(red: 0/255, green: 76/255, blue: 104/255, alpha: 1)
    }
    
    class func secondryColor() -> UIColor {
        
        return UIColor(red: 241/255, green: 157/255, blue: 36/255, alpha: 1)
    }
    
    class func secondryColorDark() -> UIColor {
        
        return UIColor(red: 206/255, green: 91/255, blue: 42/255, alpha: 1)
    }
    
    class func greyLightColor() -> UIColor {
        
        return UIColor(red: 245/255, green: 245/255, blue: 245/255, alpha: 1)
    }
    
    class func greyMidColor() -> UIColor {
        
        return UIColor(red: 224/255, green: 224/255, blue: 224/255, alpha: 1)
    }
    
    class func blue() -> UIColor {
        
        return UIColor(red: 48/255, green: 73/255, blue: 153/255, alpha: 1)
    }
}

extension String {
    
    // Get Index of specific char in a String
    func indexOf(string: String) -> Int {
        
        var index = Int()
        let searchStartIndex = self.startIndex
        
        while searchStartIndex < self.endIndex,
            let range = self.range(of: string, range: searchStartIndex..<self.endIndex),
            !range.isEmpty
        {
            index = distance(from: self.startIndex, to: range.lowerBound)
            break
        }
        
        return index
    }
    
    func toBool() -> Bool{
        if self == "false" {
            return false
        }else{
            return true
        }
    }
    
}

extension UIImage{
    
    class func textToImage(drawText text: String, imgFrame frame: CGRect) -> UIImage {
        
        let image = UIImage(named: "")
        
        let viewToRender = UIView(frame: CGRect(x: 0, y: 0, width: frame.width, height: frame.height))
        
        let imgView = UIImageView(frame: viewToRender.frame)
        
        imgView.image = image
        
        viewToRender.addSubview(imgView)
        
        let textImgView = UIImageView(frame: viewToRender.frame)
        
        textImgView.image = imageFrom(text: text, frame: frame)
        
        viewToRender.addSubview(textImgView)
        
        UIGraphicsBeginImageContextWithOptions(viewToRender.frame.size, false, 0)
        viewToRender.layer.render(in: UIGraphicsGetCurrentContext()!)
        let finalImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return finalImage!
    }
    
    class func imageFrom(text: String , frame: CGRect) -> UIImage {
        
        let renderer = UIGraphicsImageRenderer(size: frame.size)
        let img = renderer.image { ctx in
            let paragraphStyle = NSMutableParagraphStyle()
            paragraphStyle.alignment = .center
            
            let attrs = [NSAttributedStringKey.font: UIFont(name: "HelveticaNeue", size: 17)!,
                         NSAttributedStringKey.foregroundColor: UIColor.black,
                         NSAttributedStringKey.paragraphStyle: paragraphStyle]
            
            text.draw(with: CGRect(x: 0, y: 5, width: frame.width, height: frame.height), options: .usesLineFragmentOrigin, attributes: attrs, context: nil)
            
        }
        return img
    }
    
}

extension UIView {
    
    func addBorder(view: UIView, stroke: UIColor, fill: UIColor, radius: Int, width: CGFloat){
        // Add border
        let borderLayer = CAShapeLayer()
        
        let rectShape = CAShapeLayer()
        rectShape.bounds = view.frame
        rectShape.position = view.center
        
        rectShape.path = UIBezierPath(roundedRect: view.bounds, byRoundingCorners: [.bottomRight , .topLeft, .topRight, .bottomLeft], cornerRadii: CGSize(width: radius, height: radius)).cgPath
        borderLayer.path = rectShape.path // Reuse the Bezier path
        borderLayer.fillColor = fill.cgColor
        borderLayer.strokeColor = stroke.cgColor
        borderLayer.lineWidth = width
        borderLayer.frame = view.bounds
        view.layer.addSublayer(borderLayer)
        view.layer.mask = rectShape
        
    }
    
    func removeBorder(view: UIView){
        // remove border
        view.layer.removeFromSuperlayer()
    }
    
    func dropShadow() {
        
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 0.4
        self.layer.shadowOffset = CGSize(width: -1, height: 0)
        self.layer.shadowRadius = 3
        
        self.layer.shadowPath = UIBezierPath(rect: self.bounds).cgPath
        self.layer.shouldRasterize = true
        self.layer.cornerRadius = 15
        self.layer.masksToBounds = true
    }
    
    func dropShadow2() {
        
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 0.4
        self.layer.shadowOffset = CGSize(width: -1, height: 0)
        self.layer.shadowRadius = 9
        
        //self.layer.shadowPath = UIBezierPath(rect: self.bounds).cgPath
        self.layer.shouldRasterize = true
        //self.layer.cornerRadius = 15
        self.layer.masksToBounds = false
    }
    
    
    func outerGlow() {
        
        self.layer.shadowColor = UIColor(red: 252/255, green: 247/255, blue: 192/255, alpha: 1).cgColor
        self.layer.shadowOpacity = 2
        self.layer.shadowOffset = CGSize(width: -1, height: 0)
        self.layer.shadowRadius = 5
        
        //self.layer.shadowPath = UIBezierPath(rect: self.bounds).cgPath
        self.layer.shouldRasterize = true
        //self.layer.cornerRadius = 5
        
        self.layer.masksToBounds = false
    }
    
}

extension UIApplication {
    class func topViewController(base: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {
        if let nav = base as? UINavigationController {
            return topViewController(base: nav.visibleViewController)
        }
        if let tab = base as? UITabBarController {
            if let selected = tab.selectedViewController {
                return topViewController(base: selected)
            }
        }
        if let presented = base?.presentedViewController {
            return topViewController(base: presented)
        }
        return base
    }
    
    class func tryURL(urls: [String]) {
        let application = UIApplication.shared
        for url in urls {
            if application.canOpenURL(URL(string: url)!) {
                application.openURL(URL(string: url)!)
                return
            }
        }
    }
}

extension UIViewController {
    func configureChildViewController(childController: UIViewController, onView: UIView?) {
        var holderView = self.view
        if let onView = onView {
            holderView = onView
        }
        addChildViewController(childController)
        holderView?.addSubview(childController.view)
        constrainViewEqual(holderView: holderView!, view: childController.view)
        childController.didMove(toParentViewController: self)
    }
    
    
    func constrainViewEqual(holderView: UIView, view: UIView) {
        view.translatesAutoresizingMaskIntoConstraints = false
        //pin 100 points from the top of the super
        let pinTop = NSLayoutConstraint(item: view, attribute: .top, relatedBy: .equal,
                                        toItem: holderView, attribute: .top, multiplier: 1.0, constant: 0)
        let pinBottom = NSLayoutConstraint(item: view, attribute: .bottom, relatedBy: .equal,
                                           toItem: holderView, attribute: .bottom, multiplier: 1.0, constant: 0)
        let pinLeft = NSLayoutConstraint(item: view, attribute: .left, relatedBy: .equal,
                                         toItem: holderView, attribute: .left, multiplier: 1.0, constant: 0)
        let pinRight = NSLayoutConstraint(item: view, attribute: .right, relatedBy: .equal,
                                          toItem: holderView, attribute: .right, multiplier: 1.0, constant: 0)
        
        holderView.addConstraints([pinTop, pinBottom, pinLeft, pinRight])
    }
}

extension UISearchBar {
    
    var textColor:UIColor? {
        get {
            if let textField = self.value(forKey: "searchField") as?
                UITextField  {
                return textField.textColor
            } else {
                return nil
            }
        }
        
        set (newValue) {
            if let textField = self.value(forKey: "searchField") as?
                UITextField  {
                textField.textColor = newValue
            }
        }
    }
    
    public func setTextColor(color: UIColor) {
        let svs = subviews.flatMap { $0.subviews }
        guard let tf = (svs.filter { $0 is UITextField }).first as? UITextField else { return }
        tf.textColor = color
    }
}
