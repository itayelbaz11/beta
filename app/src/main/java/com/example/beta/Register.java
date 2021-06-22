package com.example.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import static com.example.beta.FBref.refAuth;
import static com.example.beta.FBref.refUsers;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText Email,PassWord,Name;
    String name,uid,password,email;
    User userdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name=(EditText) findViewById(R.id.nameR);
        Email=(EditText) findViewById(R.id.emailR);
        PassWord=(EditText) findViewById(R.id.passwordR);

        mAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }


    /**
     * REGISTERING with e-mail and password to the firbase authentication
     * it also saves the users information in the realtime database
     * @param view
     */
    public void register(View view) {

        name = Name.getText().toString();
        email = Email.getText().toString();
        password = PassWord.getText().toString();

        if (Name != null && !Name.equals("") && Email != null && !Email.equals("") && PassWord != null && !PassWord.equals("")) {
            final ProgressDialog pd = ProgressDialog.show(this, "Register", "Registering...", true);
            refAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pd.dismiss();
                            if (task.isSuccessful()) {
                                Log.d("MainActivity", "createUserWithEmail:success");
                                FirebaseUser user = refAuth.getCurrentUser();
                                uid = user.getUid();
                                userdb = new User(name, email, uid, true);
                                refUsers.child(uid).setValue(userdb);
                                Toast.makeText(Register.this, "Successful registration", Toast.LENGTH_SHORT).show();
                                Intent si = new Intent(Register.this, Login.class);
                                si.putExtra("newuser", true);
                                startActivity(si);
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(Register.this, "User with e-mail already exist!", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(Register.this, "User create failed.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }
        else{
            Toast.makeText(Register.this, "please enter email password and name", Toast.LENGTH_LONG).show();
        }
    }

}
