package com.mycompany.controllers;

import com.mycompany.entityclasses.Users;
import com.mycompany.entityclasses.Volunteer;
import com.mycompany.entityclasses.Organization;
import com.mycompany.entityclasses.Constants;
import com.mycompany.entityclasses.Photo;
import com.mycompany.controllers.util.JsfUtil;
import com.mycompany.controllers.util.JsfUtil.PersistAction;
import com.mycompany.sessionbeans.UsersFacade;
import com.mycompany.sessionbeans.VolunteerFacade;
import com.mycompany.sessionbeans.OrganizationFacade;
import com.mycompany.sessionbeans.PhotoFacade;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;
import java.io.*;
import java.net.URL;

/**
 *
 * @author Andres
 */

@Named(value = "VolunteerController")
@SessionScoped
public class VolunteerController implements Serializable {
//public class UsersController implements Serializable {  Could be called AccountController Later!
    
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private final String ExampleZipCodeAPI = "https://www.zipcodeapi.com/rest/s9oXbWODlgevBgpUCBlFQKi0ytTGunSc8sycGwSHvJyvc5Ruo8x7jcRIFOvZ7CiK/radius.<result_format>/<zip_code>/<radius>/<distance_unit>?minimal";
    private final String ZipCodeAPI1 = "https://www.zipcodeapi.com/rest/s9oXbWODlgevBgpUCBlFQKi0ytTGunSc8sycGwSHvJyvc5Ruo8x7jcRIFOvZ7CiK/radius.json/";
    private final String ZipCodeAPI2 = "/miles?minimal";
    
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String mission;
    private String website;
    private String address;
    private String city;
    private String userRole;
    private int userRoleID;
    private Character active;
    private int stateID;
    private String state;
    private String zipCode;
    
    private String searchFirstNameField = null;
    private String searchLastNameField = null;
    private String searchZipCodeField = null;
    private int searchZipCodeRadiusField;
    private int searchVolunteeringAreaField;
    private String searchActiveField = null;
    private String visible = "hidden";
    
    private Map<String, Object> zipCodeRadiuses;
    private Map<String, Object> statesNames;
    private Map<String, Object> statesAbbrv;
    private Map<String, Object> volunteeringAreas;

    private String statusMessage;

    private Volunteer selectedVolunteer;
    private List<Volunteer> volunteers;
    private List<Volunteer> volunteersButMe;

    /*
    The @EJB annotation implies that the EJB container will perform an injection of the object
    reference of the <file_name>Facade object into <file_name>Facade when it is created at runtime.
     */
    @EJB
    private VolunteerFacade volunteerFacade;
    
    @EJB
    private PhotoFacade photoFacade;
    
    // Constructor method instantiating an instance of UserController
    public VolunteerController() {
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }
    
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    public int getUserRoleID() {
        return userRoleID;
    }

    public void setUserRoleID(int userRoleID) {
        this.userRoleID = userRoleID;
    }
    
    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public Character getActive() {
        return active;
    }

    public void setActive(Character active) {
        this.active = active;
    }
    
    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }
    
    public String getSearchFirstNameField() {
        return searchFirstNameField;
    }

    public void setSearchFirstNameField(String searchFirstNameField) {
        this.searchFirstNameField = searchFirstNameField;
    }
    
    public String getSearchLastNameField() {
        return searchLastNameField;
    }

    public void setSearchLastNameField(String searchLastNameField) {
        this.searchLastNameField = searchLastNameField;
    }
    
    public String getSearchZipCodeField() {
        return searchZipCodeField;
    }

    public void setSearchZipCodeField(String searchZipCodeField) {
        this.searchZipCodeField = searchZipCodeField;
    }
    
    public int getSearchZipCodeRadiusField() {
        return searchZipCodeRadiusField;
    }

    public void setSearchZipCodeRadiusField(int searchZipCodeRadiusField) {
        this.searchZipCodeRadiusField = searchZipCodeRadiusField;
    }
    
    public String getSearchActiveField() {
        return searchActiveField;
    }

    public void setActiveField(String searchActiveField) {
        this.searchActiveField = searchActiveField;
    }
    
    public int getVolunteeringAreaField() {
        return searchVolunteeringAreaField;
    }

    public void setSearchVolunteeringAreaField(int searchVolunteeringAreaField) {
        this.searchVolunteeringAreaField = searchVolunteeringAreaField;
    }
    
    /*
    private Map<String, Object> security_questions;
        String      int
        ---------   ---
        question1,  0
        question2,  1
        question3,  2
            :
    When the user selects a security question, its number (int) is stored; not its String.
    Later, given the number (int), the security question String is retrieved.
     */
    public Map<String, Object> getZipCodeRadiuses() {

        if (zipCodeRadiuses == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings in the order they were put into the map.
             */
            zipCodeRadiuses = new LinkedHashMap<>();

            for (int i = 0; i < Constants.ZIPCODE_RADIUSES.length; i++) {
                zipCodeRadiuses.put(Constants.ZIPCODE_RADIUSES[i], i);
            }
        }
        return zipCodeRadiuses;
    }
    
    public Map<String, Object> getStatesNames() {

        if (statesNames == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings in the order they were put into the map.
             */
            statesNames = new LinkedHashMap<>();

            for (int i = 0; i < Constants.STATES.length; i++) {
                statesNames.put(Constants.STATES[i], i);
            }
        }
        return statesNames;
    }
    
    public Map<String, Object> getStatesAbbrv() {

        if (statesAbbrv == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings in the order they were put into the map.
             */
            statesAbbrv = new LinkedHashMap<>();

            for (int i = 0; i < Constants.STATES_ABBR.length; i++) {
                statesAbbrv.put(Constants.STATES_ABBR[i], i);
            }
        }
        return statesAbbrv;
    }
    
    public Map<String, Object> getVolunteeringAreas() {

        if (volunteeringAreas == null) {
            /*
            Difference between HashMap and LinkedHashMap:
            HashMap stores key-value pairings in no particular order. Values are retrieved based on their corresponding Keys.
            LinkedHashMap stores and retrieves key-value pairings in the order they were put into the map.
             */
            volunteeringAreas = new LinkedHashMap<>();

            for (int i = 0; i < Constants.VOLUNTEERING_AREA.length; i++) {
                volunteeringAreas.put(Constants.VOLUNTEERING_AREA[i], i);
            }
        }
        return volunteeringAreas;
    }
        
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Volunteer getSelectedVolunteer() {
        return selectedVolunteer;
    }

    public void setSelectedVolunteer(Volunteer selectedVolunteer) {
        this.selectedVolunteer = selectedVolunteer;
    }
    
    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }
    
    public List<Volunteer> getVolunteersButMe() {
        return volunteersButMe;
    }

    public void setVolunteersButMe(List<Volunteer> volunteersButMe) {
        this.volunteersButMe = volunteersButMe;
    }

    // EL in Profile.xhtml invokes this method to obtain the constant value
    public String photoStorageDirectoryName() {
        return Constants.STORAGE_DIRECTORY;
    }

    public String accountPhoto(Volunteer volunteer) {

        /*
        Roommate photo files are not stored in the database. Only the primary key (id) of the
        Roommate's photo is stored in the database.
        
        When Roommate uploads a photo, a thumbnail (small) version of the file is created
        in the saveThumbnail() method of FileManager by using the Scalr.resize method provided
        in the imgscalr (Java Image Scaling Library) imported as imgscalr-lib-4.2.jar

        Both uploaded and thumbnail photo files are named after the primary key (id) of the
        Roommate's photo and are stored in the PizzaHutStorageLocation. For example,
        for the primary key (id) = 25 and file extension = jpeg, the files are named as:
            e.g., 25.jpeg
            e.g., 25_thumbnail.jpeg
         */
        // Obtain a list of photo files (e.g., 25.jpeg and 25_thumbnail.jpeg) associated
        // with the logged-in Roommate whose database primary key is roommate.getId()
        Photo photo = photoFacade.findPhotoByUserID(volunteer.getUserID());

        if (photo == null) {
            // No Roommate photo exists. Return the default Roommate photo image.
            return "defaultPhoto.png";
        }

        /*
        photoList.get(0) returns the object reference of the first Photo object in the list.
        getThumbnailName() message is sent to that Photo object to retrieve its
        thumbnail image file name, e.g., 25_thumbnail.jpeg
         */
        return photo.getThumbnailName();
    }
    
    /*
    ===============================
     Volunteer Methods
    ===============================
     */
    
    // Return True if a roommate is logged in; otherwise, return False
    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userEmail") != null;
    }
    
    // Return True if a roommate is logged in; otherwise, return False
    public boolean isVolunteer() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userRole") == "Volunteer";
    }
    
    // Return True if a roommate is logged in; otherwise, return False
    public boolean isOrganization() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userRole") == "Organization";
    }
    
    public List<Volunteer> searchAllVolunteers() {
        
    //Here you have to write logic to decide which method to call
        // If no search fields then show all
        volunteers = volunteerFacade.findAll();
        return volunteers;
    }
    
    public void searchVolunteers() {

        // Make sure the logged in user is an organization
        if(!isOrganization()) 
            return;        
    
        visible = "visible";
        volunteers = null;
        statusMessage = "";
        
        // Get list of Zip Codes
        List<String> zipCodesList = getZipCodesList();
        
        //Use when testing
        //List<String> zipCodesList = new ArrayList<String>();
        //zipCodesList.add("24060");
        //zipCodesList.add("24061");
        
        // If no search fields then show all
        if(zipCodesList == null && searchFirstNameField == null && searchLastNameField == null) // && searchVolunteeringAreaField == null)
            volunteers = searchAllVolunteers();
        else
            volunteers = volunteerFacade.SearchVolunteers(zipCodesList, searchFirstNameField, searchLastNameField); // , searchVolunteeringAreaField)

    }
  
    public List<String> getZipCodesList () {
        
        /*
            Creating json object and json array to hold desired data 
         
            JSON data use the following notation:
            { }    represents a JavaScript object with its properties as Key:Value pairs
            [ ]    represents Array
            [{ }]  represents an Array of JavaScript objects
            :    separates Key from the Value
        */
        JSONObject resultsObject;
        JSONArray resultsArray;
        String zipCodes = null;
        List<String> zipCodesList = new ArrayList<String>();
        
        // Create the movies list containing movies
        try {
            resultsObject = new JSONObject(readUrlContent(ZipCodeAPI1 + searchZipCodeField + "/" + Constants.ZIPCODE_RADIUSES[searchZipCodeRadiusField] + ZipCodeAPI2));
            resultsArray = resultsObject.getJSONArray("zip_codes");
            
            int index = 0;
            
            if (resultsArray.length() > index) {
                
                while (resultsArray.length() > index) {
                    zipCodesList.add(resultsArray.getString(index++));
                }

            } else {
                statusMessage = "ZipCodeAPI is unreachable!";
                return null;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(VolunteerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return zipCodesList;
    }
    
//    public String getZipCodesList () {
//        
//        /*
//            Creating json object and json array to hold desired data 
//         
//            JSON data use the following notation:
//            { }    represents a JavaScript object with its properties as Key:Value pairs
//            [ ]    represents Array
//            [{ }]  represents an Array of JavaScript objects
//            :    separates Key from the Value
//        */
//        JSONObject resultsObject;
//        JSONArray resultsArray;
//        String zipCodes = null;
//        List<String> zipCodesList = new ArrayList<String>();
//        
//        // Create the movies list containing movies
//        try {
//            resultsObject = new JSONObject(readUrlContent(ZipCodeAPI1 + searchZipCodeField + "/" + Constants.ZIPCODE_RADIUSES[searchZipCodeRadiusField] + ZipCodeAPI2));
//            resultsArray = resultsObject.getJSONArray("zip_codes");
//            
//            int index = 0;
//            
//            if (resultsArray.length() > index) {
//                
//                // Initializes Zip Codes String Builder to the first zip code in the Json Array 
//                StringBuilder zipCodesStringBuilder = new StringBuilder().append(resultsArray.getString(index++));
//                
//                while (resultsArray.length() > index) {
//                    zipCodesStringBuilder.append(",");
//                    zipCodesStringBuilder.append(resultsArray.getString(index++));
//                }
//                zipCodes = zipCodesStringBuilder.toString();
//
//            } else {
//                statusMessage = "ZipCodeAPI is unreachable!";
//                return "";
//            }
//            
//        } catch (Exception ex) {
//            Logger.getLogger(VolunteerController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return zipCodes;
//    }
    
    //Method hides table with data when exiting the page
    public void leaving() {
        searchFirstNameField = null;
        searchLastNameField = null;
        searchZipCodeField = null;
        searchActiveField = null;
        visible = "hidden";
    }
    
    /**
     * Author: Dr. Balci
     * Return the content of a given URL as String 
     * @param webServiceURL to retrieve the JSON data from
     * @return JSON data from the given URL as String
     * @throws Exception
     */
    public String readUrlContent(String webServiceURL) throws Exception {
        BufferedReader reader = null;

        try {
            URL url = new URL(webServiceURL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder buffer = new StringBuilder();

            char[] chars = new char[10240];

            int numberOfCharactersRead;
            
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            return buffer.toString();

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
    

    /*
    ===============================
     Navigation Methods
    ===============================
     */
    
    public String homePageDestination() {

        statusMessage = null;
        if (isLoggedIn()) {
            return showDashboard();
        } else {
            return showIndexPage();
        }
    }

    // Show the Index page
    public String showIndexPage() {

        statusMessage = null;
        return "index?faces-redirect=true";
    }

    // Show the Dashboard page
    public String showDashboard() {

        statusMessage = null;
        if (isLoggedIn()) {
            return "Dashboard?faces-redirect=true";
        } else {
            return showIndexPage();
        }
    }

    public void showVolunteerInfo() {
        statusMessage = null;
        try {
            // CallMethodForVolunteerInfo();
            FacesContext.getCurrentInstance().getExternalContext().redirect("VolunteerInfo.xhtml?faces-redirect=true");
        } 
        catch(IOException e) { 
        
        }
    }

}
