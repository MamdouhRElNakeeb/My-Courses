//
//  CentersMapVC.swift
//  My Courses
//
//  Created by Mamdouh El Nakeeb on 3/12/18.
//  Copyright © 2018 My Courses. All rights reserved.
//

import UIKit
import GoogleMaps
import Alamofire
import SwiftyJSON

class CentersMapVC: UIViewController {

    var mapView = GMSMapView()
    
    var locationManager = CLLocationManager()
    var currentLocation: CLLocation?
    var zoomLevel: Float = 14.0
    
    var items = Array<Center>()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        getLocationPermision()
    
    }
    
    func getData() {
        
        
        Alamofire.request(Consts.CENTERS, method: .get).responseJSON{
            
            response in
            
            switch response.result{
            case .success(let value):
                let json = JSON(value)
                print("JSON: \(json)")
                
                for (key, subJson):(String, JSON) in json{
                    var id = 0
                    var lat = 0.0
                    var lon = 0.0
                    
                    let name = subJson["centreName"].stringValue
                    let info = subJson["info"].stringValue
                    let address = subJson["address"].stringValue
                    
                    if let centerId = subJson["id"].int{
                        id = centerId
                    }
                    
                    if let latitude = subJson["lat"].double{
                        lat = latitude
                    }
                    
                    if let longitude = subJson["lon"].double{
                        lon = longitude
                    }
                    
                    let img = subJson["image"].stringValue
                    
                    self.items.append(Center.init(id: id, name: name, info: info, address: address, latitude: lat, longitude: lon, img: img))
                }
                
                break
                
            case .failure(let error):
                print(error)
                break
            }
        }
    }
    
    
    func getLocationPermision () {
        // Ask for Authorisation from the User.
        self.locationManager.requestAlwaysAuthorization()
        
        // For use in foreground
        self.locationManager.requestWhenInUseAuthorization()
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.requestAlwaysAuthorization()
            locationManager.distanceFilter = 50
            locationManager.startUpdatingLocation()
            locationManager.startMonitoringSignificantLocationChanges()
            locationManager.delegate = self
            
            setupMap()
        }
        
    }
    
    func setupMap(){
        // Cairo Coordinates
        var camera = GMSCameraPosition.camera(withLatitude: 30.0444,
                                              longitude: 31.2357,
                                              zoom: zoomLevel)
        
        if currentLocation != nil {
            
            camera = GMSCameraPosition.camera(withLatitude: (currentLocation?.coordinate.latitude)!,
                                              longitude: (currentLocation?.coordinate.longitude)!,
                                                  zoom: zoomLevel)
            
        }
        
        mapView = GMSMapView.map(withFrame: view.bounds, camera: camera)
        
        self.view.addSubview(mapView)
        
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        mapView.settings.compassButton = true
        mapView.settings.scrollGestures = true
        mapView.settings.zoomGestures = true
        
        
        mapView.animate(to: camera)
    }
}

extension CentersMapVC: CLLocationManagerDelegate {
    
    // Handle incoming location events.
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location: CLLocation = locations.last!
        currentLocation = location
        
        print("Location: \(location)")
        
        let camera = GMSCameraPosition.camera(withLatitude: (currentLocation?.coordinate.latitude)!,
                                              longitude: (currentLocation?.coordinate.longitude)!,
                                              zoom: zoomLevel)
        
        mapView.camera = camera
        mapView.animate(to: camera)
        
        
        locationManager.stopUpdatingLocation()
        
    }
    
    // Handle authorization for the location manager.
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .restricted:
            print("Location access was restricted.")
        case .denied:
            print("User denied access to location.")
            // Display the map using the default location.
            mapView.isHidden = false
        case .notDetermined:
            print("Location status not determined.")
        case .authorizedAlways: fallthrough
        case .authorizedWhenInUse:
            print("Location status is OK.")
        }
    }
    
    // Handle location manager errors.
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        locationManager.stopUpdatingLocation()
        print("Error: \(error)")
    }
}
