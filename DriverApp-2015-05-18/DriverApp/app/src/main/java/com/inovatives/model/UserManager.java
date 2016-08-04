//package container for class
package com.inovatives.model;
//imports for the class
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Class handling the user interaction with the webservice
 * @author Dokes
 */
public class UserManager {

    //instance variables

    private static final String userType = "A";
    private static String url = "http://192.168.43.34:2525/UserDatabaseHost/UserDataService.svc/";//the url used to connect
    private String userResponse;
    public static String responseToUser;
    private InputStream stream = null;
    private Boolean status;  //setting the status of the login false

    /**
     * method getting the user response
     * @return userResponse the response from he the user
     */
    public String getUserResponse() {
        return userResponse;
    }

    /**
     * method setting the user response
     * @param userResponse the response
     */
    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    /**
     * method for user login
     * @param username the username
     * @param password the user password
     * @return status the status of the login method
     */
    public boolean login(String username, String password){


        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();//creating an http client
            HttpPost httpPost = new HttpPost(url + "validate"); // the post to the http client

            httpPost.setHeader("Content-type", "application/json");//setting headers for the post
            httpPost.setHeader("Accept", "application/json");
            JSONObject obj = new JSONObject();
            obj.put("type", userType);
            obj.put("UseriD", username);
            obj.put("password", password);
            httpPost.setEntity(new StringEntity(obj.toString(), "UTF-8"));//adding parameters to the post

            HttpResponse response = httpClient.execute(httpPost);//getting the response from the server

            //checking the response and handling it appropriately
           if(response.getStatusLine().getStatusCode() == 200) {
               HttpEntity httpEntity = response.getEntity();
               stream = httpEntity.getContent();
               setUserResponse(convertStreamToString(stream));

               if(getUserResponse().contains(username)){
                   status = true;
               }else{
                   status = false;
                   responseToUser = "Password or username incorrect";
               }
           }else{
               status = false;
               responseToUser = "Error connecting to server, please check your connection and try again";
           }

        } catch ( JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status;//returning the status to the validate method
    }

    /**
     * Method converting the input to a string
     * @param is the stream inserted
     * @return sb the string built from the input stream
     */
    public static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public Boolean vehicleRegistration(String dr_id, String stat, String reg_no, String vehicle){

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url+"VehiclePOST");

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            JSONObject obj = new JSONObject();
            obj.put("dr_id", dr_id);
            obj.put("status", stat);
            obj.put("reg_no", reg_no);
            obj.put("vehicle", vehicle);

            try {
                httpPost.setEntity(new StringEntity(obj.toString(), "UTF-8"));

                HttpResponse response;
                try {
                    response = httpClient.execute(httpPost);


                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity httpEntity = response.getEntity();
                        stream = httpEntity.getContent();
                        setUserResponse(convertStreamToString(stream));

                        if (getUserResponse().contains("allowed")) {
                            status = true;
                            responseToUser = "Successful";
                        } else {
                            status = false;
                            responseToUser = "Vehicle and registration don't match, please verify your vehicle";
                        }
                    }else {
                        status = false;
                        responseToUser = "Error connecting to server, please check your connection and try again";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * method to get the emergency from database
     * @param uri the uri address
     * @return jsonArray, a list of the numbers or emergencies
     */
    public String getEmergency(String uri){

        String uncut = "";
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url+uri);

        try {
            HttpResponse httpResponse = client.execute(httpGet);

            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                stream = entity.getContent();
                uncut = convertStreamToString(stream);
                status = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uncut;
    }
}
