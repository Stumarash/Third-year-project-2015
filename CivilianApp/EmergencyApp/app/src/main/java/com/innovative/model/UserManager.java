//package container for class
package com.innovative.model;
//imports for the class
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

    private static final String userType = "Civilian";
    private static String url = "http://192.168.43.21:2525/UserDatabaseHost/UserDataService.svc/";//the url used to connect
    private String userResponse;
    public static String responseToUser;
    private InputStream stream = null;

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

        boolean status = false; //setting the status of the login false
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

    /**
     * registering the civilian
     * @param name the civilian name
     * @param last_name the civilian last name
     * @param dateOfBirth the civilian date of birth
     * @param sex the civilian sex
     * @param email_acc the civilian email account
     * @param cellNo the civilian cellphone number
     * @param nextKinName the civilian next of kin name
     * @param nextKinNum the civilian next of kin number
     * @param usern the civilian username
     * @param pass the civilian passwird
     * @param cond the civilian medical condition
     * @param nextkinEmail the civilian next of kin email
     * @return status the status of the registration
     */
    public boolean registerUser(String name,String last_name,String dateOfBirth,String sex,String email_acc,int cellNo,String nextKinName,int nextKinNum,String usern,String pass,String cond,String nextkinEmail){

        boolean status = false;


        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url+"addUserDetail");

            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("last_name", last_name);
            obj.put("dateOfBirth", dateOfBirth);
            obj.put("sex", sex);
            obj.put("email_acc", email_acc);
            obj.put("cellNo", cellNo);
            obj.put("nextKinName", nextKinName);
            obj.put("nextKinNum", nextKinNum);
            obj.put("usern", usern);
            obj.put("pass", pass);
            obj.put("cond", cond);
            obj.put("nextkinEmail",nextkinEmail);
            try {
                httpPost.setEntity(new StringEntity(obj.toString(), "UTF-8"));

                HttpResponse response;
                try {
                    response = httpClient.execute(httpPost);


                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity httpEntity = response.getEntity();
                        stream = httpEntity.getContent();
                        setUserResponse(convertStreamToString(stream));

                        if (getUserResponse().contains(usern)) {
                            status = true;
                        } else {
                            status = false;
                            responseToUser = "Username taken please choose a different one";
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
     * method to get the list of numbers and emergencies from database
     * @param uri the uri address
     * @return jsonArray, a list of the numbers or emergencies
     */
    public JSONArray getAll(String uri){

        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url+uri);
        JSONArray jsonArray = null;
        try {
            HttpResponse httpResponse = client.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                stream = entity.getContent();
                setUserResponse(convertStreamToString(stream));
                try {
                    jsonArray = new JSONArray(getUserResponse());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }



}
