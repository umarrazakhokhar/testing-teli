package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    /* Mobile number */
    private EditText editText_mobile,editText_otp;
    private RelativeLayout container_mobile,container_otp;

    /* OTP code*/
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    //private EditText editText;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


  // Start The Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //start admin

        TextView version=findViewById(R.id.version);

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //end admin

        //Mobile Number Insert

        editText_mobile = findViewById(R.id.editText_mobile);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyLogin", 0);
        editor = sharedPreferences.edit();


       Button btn_continue= (Button) findViewById(R.id.btn_continue);

        container_mobile=findViewById(R.id.container_mobile);
        container_otp=findViewById(R.id.container_otp);
        container_otp.setVisibility(View.GONE);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editText_mobile.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    editText_mobile.setError("Enter a valid mobile");
                    editText_mobile.requestFocus();

                    return;
                }
                else
                {

                    /* Start Mobile number Processing in firebase*/
                    String Mobile_Number=editText_mobile.getText().toString();
                    sendVerificationCode("+91"+Mobile_Number);
                   // Toast.makeText(LoginActivity.this, "+91"+Mobile_Number, Toast.LENGTH_SHORT).show();
                    /* End  Mobile number Processing in firebase*/

                    container_mobile.setVisibility(View.GONE);
                    container_otp.setVisibility(View.VISIBLE);


                    editor.putString("Verify_Mobile",Mobile_Number ).commit();

                   // num_of_child_firebase();
                   // add_on_firebase(editText_mobile.getText().toString()); //create user account
                   // firebase_online();

                }


            }
        });


        //OTP Processing using FireBase

        mAuth = FirebaseAuth.getInstance();

        editText_otp = findViewById(R.id.editText_otp);
        Button btn_submit= (Button) findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressbar);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String code = editText_otp.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText_otp.setError("Enter code...");
                    editText_otp.requestFocus();
                    return;
                }

                //Toast.makeText(LoginActivity.this, "Code otp="+code, Toast.LENGTH_SHORT).show();
                verifyCode(code);

            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        container_mobile.setVisibility(View.VISIBLE);
        container_otp.setVisibility(View.GONE);
    }

    private void verifyCode(String code) {
        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        //signInWithCredential(credential);

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), "Verification Code is wrong, try again", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }



    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("Verify_Mobile",sharedPreferences.getString("Verify_Mobile","1" ));
                            //intent.putExtra("No_of_record",sharedPreferences.getInt("No_of_record",0 ));

                            finish();
                            startActivity(intent);


                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    private void sendVerificationCode(String Mobile_number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Mobile_number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText_otp.setText(code);
                verifyCode(code);
            }

            //signInWithPhoneAuthCredential(credential);
           // signInWithCredential(phoneAuthCredential);
        }


        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            //num_of_child_firebase();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Verify_Mobile",sharedPreferences.getString("Verify_Mobile","1" ));
            //intent.putExtra("No_of_record",sharedPreferences.getInt("No_of_record",0 ));
            startActivity(intent);
            finish();
        }
    }



    public void firebase_online2()
    {
        final DatabaseHandler_Contact databaseHandler;
        DatabaseReference mFirebaseDatabase;
        databaseHandler=new DatabaseHandler_Contact(this);
        mFirebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Muslim_Teli_Contact_Book").child("Contact_Number");
        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {


                String key=dataSnapshot.getKey();
                //Toast.makeText(LoginActivity.this,key,Toast.LENGTH_SHORT).show();


                String  mobile = dataSnapshot.child("mobile").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String father = dataSnapshot.child("father").getValue(String.class);
                String surname = dataSnapshot.child("surname").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);

                /*
                Toast.makeText(MainActivity.this,"mobile="+mobile,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"name="+name,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"father="+father,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"surname="+surname,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"city="+city,Toast.LENGTH_SHORT).show();
                */

                boolean result=databaseHandler.insertData2(mobile,name,father,surname,city);

                if(result == true)
                {
                    // Toast.makeText(MainActivity.this,"DATA INSERTED IN MOBILE",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //Toast.makeText(LoginActivity.this,"DATA NOT INSERTED IN MOBILE", Toast.LENGTH_LONG).show();
                }


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                //databaseHandler.UpdateData(key,value);

                //Toast.makeText(LoginActivity.this,"Some Data will Update",Toast.LENGTH_SHORT).show();

                ///////////////Refresh the page//
                /* Intent intent = getIntent();
                finish();
                startActivity(intent);*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {



                //Toast.makeText(LoginActivity.this,"Unwanted data will delete",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Toast.makeText(LoginActivity.this,"Error data will delete",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void num_of_child_firebase2() {

        DatabaseReference mFirebaseDatabase;

        //mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Muslim_Teli_Contact_Book").child("Contact_Number");
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int num = (int) dataSnapshot.getChildrenCount();

                //Toast.makeText(LoginActivity.this,"Login:-Total Number Of Records "+num,Toast.LENGTH_SHORT).show();

                editor.putInt("No_of_record",num ).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}