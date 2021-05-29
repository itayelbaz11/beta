package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import static com.example.beta.FBref.refAuth;

public class Login extends AppCompatActivity {

    CheckBox cBstayconnect;
    String email,password;
    boolean stayConnect;

    EditText emailL,passwordL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailL=(EditText) findViewById(R.id.emailL);
        passwordL=(EditText) findViewById(R.id.passwordL);
        cBstayconnect=(CheckBox) findViewById((R.id.cB));

        stayConnect=false;

    }

    /**
     * when the activity starts the phone makes sure if the user is already logged in (stay connected) or not, if he logged in, he is sent to the choosing activity automatically
     */
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
        Boolean isChecked=settings.getBoolean("stayConnect",false);
        Intent si = new Intent(Login.this, choosing.class);
        if (refAuth.getCurrentUser()!=null && isChecked) {
            stayConnect=true;
            si.putExtra("newuser",false);
            startActivity(si);
        }
    }

    /**
     * This method sends the user to the registration activity
     * @param view
     */
    public void register(View view) {
        Intent si=new Intent(this,Register.class);
        startActivity(si);
    }

    /**
     * This method checks with the firbase authentication if the e-mail ad the password are true and if they do, it will enter the user to the app
     * @param view
     */
    public void logIn(View view) {
        email=emailL.getText().toString();
        password=passwordL.getText().toString();

        final ProgressDialog pd=ProgressDialog.show(this,"Login","Connecting...",true);
        if((email!=null)&&(email!="") &&(password!=null)&&(password!="")){
            refAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                SharedPreferences settings=getSharedPreferences("PREFS_NAME",MODE_PRIVATE);
                                SharedPreferences.Editor editor=settings.edit();
                                editor.putBoolean("stayConnect",cBstayconnect.isChecked());
                                editor.commit();
                                Log.d("MainActivity", "signinUserWithEmail:success");
                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent si = new Intent(Login.this,choosing.class);
                                si.putExtra("newuser",false);
                                startActivity(si);
                            } else {
                                Log.d("MainActivity", "signinUserWithEmail:fail");
                                Toast.makeText(Login.this, "e-mail or password are wrong!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
