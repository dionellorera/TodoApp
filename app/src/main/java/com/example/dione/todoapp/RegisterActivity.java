package com.example.dione.todoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dione.todoapp.Entities.User;
import com.orm.SugarContext;

public class RegisterActivity extends AppCompatActivity implements AppCompatButton.OnClickListener {
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputPassword;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redirectIfRegistered();
        initialize();
        SugarContext.init(mContext);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                registerUser();
                break;
            default:
                break;
        }
    }


    //user created methods
    private void initialize(){
        AppCompatButton buttonLogin = (AppCompatButton) findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(this);
        inputFirstName = (EditText) findViewById(R.id.input_first_name);
        inputLastName = (EditText) findViewById(R.id.input_last_name);
        inputPassword = (EditText) findViewById(R.id.input_password);
        mContext = getApplicationContext();
    }
    private void registerUser(){
        if (isInputValid(inputFirstName) && isInputValid(inputLastName) && isInputValid(inputPassword)){
            User user = new User(inputFirstName.getText().toString(),
                        inputLastName.getText().toString(),
                        inputPassword.getText().toString());
            user.save();
            Toast.makeText(mContext, getString(R.string.toast_user_registered), Toast.LENGTH_SHORT).show();
            startThingsListActivity();
        }
    }

    private boolean isInputValid(EditText editText){
        boolean isValid;
        if (editText.getText().toString().isEmpty()){
            isValid=false;
            editText.setError(getString(R.string.field_required));
        }else{
            isValid=true;
            editText.setError(null);
        }
        return isValid;
    }

    private void redirectIfRegistered(){
        User user = User.findById(User.class, 1);
        if (user!=null){
            startThingsListActivity();
        }
    }

    private void startThingsListActivity(){
        startActivity(new Intent(RegisterActivity.this, ThingsListActivity.class));
        finish();
    }


}
