package com.improveresponse.emergencyapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.improveresponse.model.UserManager;

/**
 * Login class handling the logging in of  civilians.
 * @author Dokes
 */

public class LoginScreen extends Activity implements TextWatcher {

    // instance variables of the ui components
    private EditText txtUserName,txtPassword;
    private static String username, password;
    private ProgressBar progressBar;
    private TextView header;
    protected static final String PREFS_NAME= "Prefs";
    private static final String PREFS_USERNAME= "User";
    private static final String PREFS_PASS= "Pass";
    private SharedPreferences pref;

    // created when the activity is first started
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //linking the ui components with the background code
        txtUserName = (EditText)findViewById(R.id.username_textbox);
        txtPassword = (EditText)findViewById(R.id.password_textbox);
        header = (TextView)findViewById(R.id.login_screen_header);
        progressBar = (ProgressBar)findViewById(R.id.loginProgress);

        // initializing the components

        txtUserName.addTextChangedListener(this);
        progressBar.setVisibility(View.GONE);

        pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
    }

    // method to login
    public void cmdLogin_Click(View view){
        progressBar.setIndeterminate(true);


        username= txtUserName.getText().toString().trim();
        password =txtPassword.getText().toString().trim();

        if(!username.isEmpty() && !password.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            new LoginTask().execute(new UserManager());
        }else{
            Toast.makeText(getApplicationContext(),"Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

    }

    // button to go to registration screen
    public void goRegister(View view){
      Intent move = new Intent(this, RegistrationScreen.class);
       startActivity(move);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_screen, menu);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = s.length();
        if(count > 10){
            txtUserName.setTextColor(Color.RED);
        }
    }

    public void makeVisible(View view) {
        if(txtPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
            txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else{
            txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    /**
     * inner class handling the async task
     */
   private  class LoginTask extends AsyncTask<UserManager,Long,Boolean>{

       @Override
       protected Boolean doInBackground(UserManager... params) {
           //this is executed in background thread

           return params[0].login(username,password);
       }

        @Override
        protected void onPreExecute() {

            pref.edit().clear().apply();
        }

        @Override
       protected void onPostExecute(Boolean s) {
            progressBar.setVisibility(View.GONE);
            if(s){
                getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit()
                        .putString(PREFS_USERNAME,username)
                        .putString(PREFS_PASS,password)
                        .apply();
                Intent outside = new Intent(getApplicationContext(),EmergencyChoice.class);
                outside.putExtra("User",username);
                startActivity(outside);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),UserManager.responseToUser, Toast.LENGTH_SHORT).show();
            }
       }
   }


}
