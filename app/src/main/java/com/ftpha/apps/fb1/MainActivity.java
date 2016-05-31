package com.ftpha.apps.fb1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 747;
    private TextView mTV;
    private EditText mName;
    private EditText mMsg;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefN;
    DatabaseReference myRefM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTV = (TextView) findViewById(R.id.helloText);
        mName = (EditText) findViewById(R.id.txtName);
        mMsg = (EditText) findViewById(R.id.txtMsg);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRefN = database.getReference("Name");
        myRefM = database.getReference("Msg");

        // Read from the database

        myRefN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String iName = dataSnapshot.getValue(String.class);
                mName.setText(iName);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FT747  ", "Failed to read value.", error.toException());
            }
        });
        myRefM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String iMsg = dataSnapshot.getValue(String.class);
                mMsg.setText(iMsg);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FT747  ", "Failed to read value.", error.toException());
            }
        });




        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            String uN;
            String uN1 = auth.getCurrentUser().getDisplayName();
            String uN2 = auth.getCurrentUser().getEmail();
            if(uN1 == null){
                uN = uN2;
            }else{
                uN = uN1;
            }
            mTV.setText("Hello, " + uN);
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER
                                    //AuthUI.FACEBOOK_PROVIDER
                            )
                            .build(),
                    RC_SIGN_IN);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "YES   :)",Toast.LENGTH_LONG).show();
                finish();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                Toast.makeText(this, "Not signed in",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void onLogout(View view) {

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            //user is now signed out
                            Toast.makeText(MainActivity.this, "Logged Out",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });

    }

    public void onSave(View view) {
        myRefN.setValue(mName.getText().toString());
        myRefM.setValue(mMsg.getText().toString());
    }
}
