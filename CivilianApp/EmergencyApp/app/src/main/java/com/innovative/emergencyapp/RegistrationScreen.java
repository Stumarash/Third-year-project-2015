package com.innovative.emergencyapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.innovative.model.UserManager;

/**
 * Registration Screen class handling the registration of the civilians
 * @author Dokes
 */
public class RegistrationScreen extends Activity {

    //instance variables
    private EditText fname;
    private EditText lastname;
    private EditText dateOfBirth;

    private EditText email;
    private EditText phone;
    private EditText nextOfKinName;
    private EditText nextOfKinNum;
    private EditText username;
    private EditText password;
    private EditText confirm;
    private EditText condition;
    private String name,last_nam, email_acc,nextkinname,usern,pass,cond,bornDat,passConf,sex,nextKinEmail;
    private int cellNo,nextKinNumb;
    private EditText nextofKinEmail;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);

        //the back capability on actionbar
        ActionBar action = getActionBar();
        if (action != null) {
            action.setDisplayHomeAsUpEnabled(true);
        }


        fname = (EditText) findViewById(R.id.txt_register_name);
        lastname = (EditText) findViewById(R.id.txt_register_surname);
        dateOfBirth = (EditText) findViewById(R.id.txt_register_dateOfBirth);
        email  = (EditText) findViewById(R.id.txt_Email);
        phone = (EditText) findViewById(R.id.txt_register_phoneNum);
        nextOfKinName = (EditText) findViewById(R.id.txt_register_nextKinName);
        nextOfKinNum = (EditText) findViewById(R.id.txt_register_nextKinphoneNum);
        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_password);
        confirm  = (EditText) findViewById(R.id.txt_confirm_password);
        condition =(EditText) findViewById(R.id.txt_register_medicalCondition);
        nextofKinEmail = (EditText)findViewById(R.id.txt_register_nextKinEmail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cmdRegister_Click(View view) {
        progressBar= new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage("Registering...");
        new RegistrationTask().execute(new UserManager());
    }

    public void cmdFemale_Click(View view) {
        sex = "f";
    }

    public void cmdMale_Select(View view) {
        sex = "m";
    }

    /**
     * inner class handling the async task
     */
    private  class RegistrationTask extends AsyncTask<UserManager,Long,Boolean> {

        @Override
        protected Boolean doInBackground(UserManager... params) {
            //this is executed in background thread
            return params[0].registerUser(name, last_nam, bornDat, sex, email_acc, cellNo, nextkinname, nextKinNumb, usern, pass, cond,nextKinEmail);
        }

        @Override
        protected void onPreExecute() {
             progressBar.show();
             name = fname.getText().toString();
             last_nam = lastname.getText().toString();
             email_acc = email.getText().toString();
             cellNo = Integer.parseInt(phone.getText().toString());
             nextkinname = nextOfKinName.getText().toString();
             nextKinNumb = Integer.parseInt(nextOfKinNum.getText().toString());
             usern = username.getText().toString();
             pass = password.getText().toString();
             cond = condition.getText().toString();
             bornDat = dateOfBirth.getText().toString();
            passConf = confirm.getText().toString();
            nextKinEmail = nextofKinEmail.getText().toString();

        }

        @Override
        protected void onPostExecute(Boolean s) {
            progressBar.dismiss();
            if(s){
                Intent outside = new Intent(getApplicationContext(), EmergencyChoice.class);
                outside.putExtra("User",usern);
                startActivity(outside);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),UserManager.responseToUser, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
