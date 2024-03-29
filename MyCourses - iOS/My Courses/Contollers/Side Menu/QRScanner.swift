//
//  QRScanner.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 4/2/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import AVFoundation
import QRCodeReader
import Alamofire
import SwiftyJSON

class QRScanner: UIViewController, QRCodeReaderViewControllerDelegate, UIGestureRecognizerDelegate {

    @IBOutlet weak var promoCodeTF: UITextField!
    @IBOutlet weak var addPromoCodeBtn: UIButton!
    
    @IBOutlet weak var previewView: QRCodeReaderView! {
        didSet {
            previewView.setupComponents(showCancelButton: false, showSwitchCameraButton: false, showTorchButton: false, showOverlayView: true, reader: reader)
        }
    }
    
    lazy var reader: QRCodeReader = QRCodeReader()
    lazy var readerVC: QRCodeReaderViewController = {
        let builder = QRCodeReaderViewControllerBuilder {
            $0.reader          = QRCodeReader(metadataObjectTypes: [.qr], captureDevicePosition: .back)
            $0.showTorchButton = true
            
            $0.reader.stopScanningWhenCodeIsFound = false
        }
        
        return QRCodeReaderViewController(builder: builder)
    }()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        addPromoCodeBtn.layer.cornerRadius = 15
        
        promoCodeTF.delegate = self
        
        guard checkScanPermissions(), !reader.isRunning else { return }

        reader.didFindCode = { result in
            print("Completion with result: \(result.value) of type \(result.metadataType)")
            self.checkPromoCode(code: result.value)
        }
        
        reader.startScanning()
        
        let hideKeyboard = UITapGestureRecognizer(target: self, action: #selector(self.hideKeyboard(_:)))
        hideKeyboard.numberOfTapsRequired = 1
        self.view.addGestureRecognizer(hideKeyboard)
    }
    
    @objc func hideKeyboard(_ recognizer: UIGestureRecognizer){
        view.endEditing(true)
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func checkPromoCode(code: String){
        
        let url = Consts.PROMO_CODE + code + "/"
        Alamofire.request(url).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let discount = json["discount"].int{
                    
                    let codeID = json["id"].intValue
                    self.addPromoToUser(codeID: codeID, discount: discount)
                    
                    let alert = UIAlertController(
                        title: "Promo Code",
                        message: "\(discount)% discount",
                        preferredStyle: .alert
                    )
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
//                    self.present(alert, animated: true, completion: nil)
                    
                }
                else if let errors = json["errors"].string{
                    
                    let alert = UIAlertController(
                        title: "Promo Code",
                        message: errors,
                        preferredStyle: .alert
                    )
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true, completion: nil)
                    
                }
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    func addPromoToUser(codeID: Int, discount: Int){
        
        let url = Consts.PROMO_USER + "\(UserDefaults.standard.integer(forKey: "id"))/"
        
        let params: Parameters = [
            "promoCode": codeID
        ]
        
        
        let headers: HTTPHeaders = [
            "Accept": "application/json",
            "Content-Type" :"application/json"
        ]
        
        var request = URLRequest(url: URL(string: url)!)
        request.httpMethod = HTTPMethod.post.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let postString = "{\"promoCode\":\"\(codeID)\"}"
        
        print(postString)
        
        request.httpBody = postString.data(using: .utf8)
        
        Alamofire.request(request).responseJSON { (response) in
            
            print(response)
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                if let created = json["created"].string{
                    
                    print(created)
                    if created.toBool(){
                        
                        
                        let alert = UIAlertController(
                            title: "QRCodeReader",
                            message: "Promo Code is added successfully. /n \(discount)% discount",
                            preferredStyle: .alert
                        )
                        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: { (action) in
                            
                            
                            _ = self.navigationController?.popViewController(animated: true)
                            
                        }))
                        
                        self.present(alert, animated: true, completion: nil)
                    }
                    else{
                        let alert = UIAlertController(
                            title: "QRCodeReader",
                            message: "You have already taken this promotion!",
                            preferredStyle: .alert
                        )
                        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                        
                        self.present(alert, animated: true, completion: nil)
                    }
                    
                }
                else {
                    let alert = UIAlertController(
                        title: "QRCodeReader",
                        message: "You have already taken this promotion!",
                        preferredStyle: .alert
                    )
                    alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                    
                    self.present(alert, animated: true, completion: nil)
                    
                }
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
            
        }
        
    }
    
    @IBAction func addPromoBtnOnClick(_ sender: Any) {
        
        if (promoCodeTF.text?.isEmpty)!{
            
            let alert = UIAlertController(
                title: "Add Promotion",
                message: "Please enter a promo code",
                preferredStyle: .alert
            )
            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            
            self.present(alert, animated: true, completion: nil)
            
            
            return
        }
        
        self.checkPromoCode(code: promoCodeTF.text!)
        
    }
    
    private func checkScanPermissions() -> Bool {
        do {
            return try QRCodeReader.supportsMetadataObjectTypes()
        } catch let error as NSError {
            let alert: UIAlertController
            
            switch error.code {
            case -11852:
                alert = UIAlertController(title: "Error", message: "This app is not authorized to use Back Camera.", preferredStyle: .alert)
                
                alert.addAction(UIAlertAction(title: "Setting", style: .default, handler: { (_) in
                    DispatchQueue.main.async {
                        if let settingsURL = URL(string: UIApplicationOpenSettingsURLString) {
                            UIApplication.shared.openURL(settingsURL)
                        }
                    }
                }))
                
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            default:
                alert = UIAlertController(title: "Error", message: "Reader not supported by the current device", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            }
            
            present(alert, animated: true, completion: nil)
            
            return false
        }
    }
    
    @IBAction func scanInModalAction(_ sender: AnyObject) {
        guard checkScanPermissions() else { return }
        
        readerVC.modalPresentationStyle = .formSheet
        readerVC.delegate               = self
        
        readerVC.completionBlock = { (result: QRCodeReaderResult?) in
            if let result = result {
                print("Completion with result: \(result.value) of type \(result.metadataType)")
            }
        }
        
        present(readerVC, animated: true, completion: nil)
    }
    
    @IBAction func scanInPreviewAction(_ sender: Any) {
        guard checkScanPermissions(), !reader.isRunning else { return }
        
        reader.didFindCode = { result in
            print("Completion with result: \(result.value) of type \(result.metadataType)")
        }
        
        reader.startScanning()
    }
    
    // MARK: - QRCodeReader Delegate Methods
    func reader(_ reader: QRCodeReaderViewController, didScanResult result: QRCodeReaderResult) {
        reader.stopScanning()
        
        self.checkPromoCode(code: result.value)
        
        dismiss(animated: true) { [weak self] in
            let alert = UIAlertController(
                title: "QRCodeReader",
                message: String (format:"%@ (of type %@)", result.value, result.metadataType),
                preferredStyle: .alert
            )
            alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
            
            self?.present(alert, animated: true, completion: nil)
        }
    }
    
    func reader(_ reader: QRCodeReaderViewController, didSwitchCamera newCaptureDevice: AVCaptureDeviceInput) {
        print("Switching capturing to: \(newCaptureDevice.device.localizedName)")
    }
    
    func readerDidCancel(_ reader: QRCodeReaderViewController) {
        reader.stopScanning()
        
        dismiss(animated: true, completion: nil)
    }

}

extension QRScanner: UITextFieldDelegate{
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.becomeFirstResponder()
        
        let keyboardHeight : CGFloat = 250
        
        UIView.beginAnimations( "animateView", context: nil)
        
        var frame : CGRect = self.view.frame
        
        frame.origin.y = -keyboardHeight
        self.view.frame = frame
        UIView.commitAnimations()
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        
        UIView.beginAnimations( "animateView", context: nil)
        var frame : CGRect = self.view.frame
        frame.origin.y = 0
        self.view.frame = frame
        UIView.commitAnimations()
    }
    
}
