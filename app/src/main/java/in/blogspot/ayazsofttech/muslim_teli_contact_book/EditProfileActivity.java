package in.blogspot.ayazsofttech.muslim_teli_contact_book;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

public class EditProfileActivity extends AppCompatActivity {

    EditText Name, Mobile, Surname,FatherName,HomeTown,Category;

    String Mobile_Number;
    int abc;

    AutoCompleteTextView State, City,District;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Name = findViewById(R.id.EditText_Name);
        Mobile = findViewById(R.id.EditText_Mobile);
        FatherName = findViewById(R.id.EditText_FatherName);
        Surname = findViewById(R.id.EditText_SurName);
        District = findViewById(R.id.EditText_District);
        City = findViewById(R.id.EditText_City);
        State= findViewById(R.id.EditText_State);
        HomeTown = findViewById(R.id.EditText_HomeTown);
        Category = findViewById(R.id.EditText_Category);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {

            Mobile_Number=bundle.getString("Edit_Mobile");
            Mobile.setText(Mobile_Number);

        }

        if ( !AppStatus.getInstance(EditProfileActivity.this).isOnline()) {
            new AlertDialog.Builder(EditProfileActivity.this,R.style.MyDialogTheme)
                    .setTitle("No Connection")
                    .setMessage("No Internet connection found. check your connection or try again.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            onBackPressed();
                        }
                    })
                    .setCancelable(false)
                    .create().show();
        }


        //alertMessageBox();


        final DatabaseHandler_Contact databaseHandler = new DatabaseHandler_Contact(this);
        Cursor cursor = databaseHandler.SelectData_Where_Mobile(Mobile_Number);
        abc=cursor.getCount();

        if(cursor.getCount() != 0)
        {
           // Toast.makeText(EditProfileActivity.this,"cursor = "+cursor.getCount(),Toast.LENGTH_LONG).show();

            while (cursor.moveToNext()) {
                String db_mobile = cursor.getString(0);
                String db_name = cursor.getString(1);
                String db_surname = cursor.getString(2);
                String db_father = cursor.getString(3);
                String db_category = cursor.getString(4);
                String db_district = cursor.getString(5);
                String db_city = cursor.getString(6);
                String db_state = cursor.getString(7);
                String db_hometown = cursor.getString(8);

              //  Toast.makeText(EditProfileActivity.this,"EditProfile:-M "+db_mobile+" N "+db_name+" F "+db_father+" S "+db_surname+" C "+db_city,Toast.LENGTH_LONG).show();

                Mobile.setText(cursor.getString(0));
                Name.setText(cursor.getString(1));
                Surname.setText(cursor.getString(2));
                FatherName.setText(cursor.getString(3));
                Category.setText(cursor.getString(4));
                District.setText(cursor.getString(5));
                City.setText(cursor.getString(6));
                State.setText(cursor.getString(7));
                HomeTown.setText(cursor.getString(8));

            }

        }



        // Spinner for Surname
        Spinner_Surname(Surname.getText().toString());

        //Spiner for Spinner_Category
        Spinner_Category(Category.getText().toString());

        String[] state_array =getResources().getStringArray(R.array.state);;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,state_array);
        //Getting the instance of AutoCompleteTextView
        State.setThreshold(1);//will start working from first character
        State.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


        String[] District_array =getResources().getStringArray(R.array.district);;
        ArrayAdapter<String> adapter_District = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,District_array);
        //Getting the instance of AutoCompleteTextView
        District.setThreshold(1);//will start working from first character
        District.setAdapter(adapter_District);//setting the adapter data into the AutoCompleteTextView

        String[] City_array =getResources().getStringArray(R.array.district);;
        ArrayAdapter<String> adapter_City = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,City_array);
        //Getting the instance of AutoCompleteTextView
        City.setThreshold(1);//will start working from first character
        City.setAdapter(adapter_City);//setting the adapter data into the AutoCompleteTextView






       Button btn_save= findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String mMobile=Mobile.getText().toString();
                final String mName=Name.getText().toString();
                final String mFatherName=FatherName.getText().toString();
                final String mSurname=Surname.getText().toString();
                final String mDistrict=District.getText().toString();
                final String mCity=City.getText().toString();
                final String mState=State.getText().toString();
                final String mHomeTown=HomeTown.getText().toString();
                final String mCategory=Category.getText().toString();


                if(mName.isEmpty()){
                    Name.setError("Please Enter Your Name");
                    Name.requestFocus();
                    return;
                }
                else if(mFatherName.isEmpty()){
                    FatherName.setError("Please Enter Your Father Name");
                    FatherName.requestFocus();
                    return;
                }
                else if(mState.isEmpty()){
                    State.setError("Please Enter Your State");
                    State.requestFocus();
                    return;
                }
                else if(mDistrict.isEmpty()){
                    District.setError("Please Enter Your District");
                    District.requestFocus();
                    return;
                }
                else if(mCity.isEmpty()){
                    City.setError("Please Enter Your City");
                    City.requestFocus();
                    return;
                }
                else
                {

                    new AlertDialog.Builder(EditProfileActivity.this,R.style.MyDialogTheme)
                            .setTitle("Save Or Quit !!!!")
                            .setMessage("Do You Want To Save Changes")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    boolean result = false;
                                    if(abc==0)
                                    {
                                        result = databaseHandler.insertData1(mMobile, mName, mSurname, mFatherName, mCategory, mDistrict, mCity, mState, mHomeTown);
                                    }
                                    else
                                    {
                                        result = databaseHandler.UpdateData(mMobile, mName, mSurname, mFatherName, mCategory, mDistrict, mCity, mState, mHomeTown);
                                    }
                                    if(result == true)
                                    {
                                        add_on_firebase(mMobile,mName,mSurname,mFatherName,mCategory,mDistrict,mCity,mState,mHomeTown);
                                        Toast.makeText(EditProfileActivity.this,"Successfully Added SQLite",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(EditProfileActivity.this,MainActivity.class);
                                        intent.putExtra("Verify_Mobile",mMobile);
                                        intent.putExtra("Name_Bundle",mName);
                                        finish();
                                        startActivity(intent);
                                    }
                                    else
                                    { Toast.makeText(EditProfileActivity.this,"Not ADD", Toast.LENGTH_LONG).show(); }

                                }
                            }).create().show();

                }

            }
        });


    }

    private void alertMessageBox() {
        new AlertDialog.Builder(EditProfileActivity.this)
                .setTitle("Please Fill The Form In English.")
                .setMessage("Please Fill The Form In English.\n\nकृपया अंग्रेजी में फॉर्म भरें |\n\n In English ( Name, Father Name, City and Home Town)")

                .setPositiveButton(android.R.string.ok,null)

                .create().show();
    }

    private void add_on_firebase(String mMobile, String mName, String mSurname, String mFatherName, String mCategory, String mDistrict, String mCity, String mState, String mHomeTown) {

        DatabaseReference mFirebaseDatabase;

        //Toast.makeText(EditProfileActivity.this,"EditProfile add:-M "+mMobile+" N "+mName+" F "+mFatherName+" S "+mSurname+" C "+mCity,Toast.LENGTH_LONG).show();

        mFirebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Muslim_Teli_Contact_Book").child("Contact_Number");

        mFirebaseDatabase.child(mMobile).child("mobile").setValue(mMobile);
        mFirebaseDatabase.child(mMobile).child("name").setValue(mName);
        mFirebaseDatabase.child(mMobile).child("surname").setValue(mSurname);
        mFirebaseDatabase.child(mMobile).child("father").setValue(mFatherName);
        mFirebaseDatabase.child(mMobile).child("category").setValue(mCategory);
        mFirebaseDatabase.child(mMobile).child("district").setValue(mDistrict);
        mFirebaseDatabase.child(mMobile).child("city").setValue(mCity);
        mFirebaseDatabase.child(mMobile).child("state").setValue(mState);
        mFirebaseDatabase.child(mMobile).child("hometown").setValue(mHomeTown);

        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               //Toast.makeText(EditProfileActivity.this,"Successfully Store on Firebase",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Toast.makeText(EditProfileActivity.this,"Successfully Change",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
               // Toast.makeText(EditProfileActivity.this,"onChildRemoved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

               // Toast.makeText(EditProfileActivity.this,"onCancelled",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();
        String name=Name.getText().toString();
        if(name.equals(""))
        {

            new AlertDialog.Builder(EditProfileActivity.this)
                    .setTitle("Exit Or Quit !!")
                    .setMessage("Please Fill The Form")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            onBackPressed();

                        }
                    })

                    .create().show();
        }
        else
        {
            super.onBackPressed();

        }



    }


    private void Spinner_Surname(String str) {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(str);
        if(spinnerPosition>0){
            //Toast.makeText(EditProfileActivity.this,"IF spinnerPosition"+spinnerPosition, Toast.LENGTH_LONG).show();
            //set the default according to value
            spinner.setSelection(spinnerPosition);
        }
        else
        {   //Toast.makeText(EditProfileActivity.this,"Else spinnerPosition"+spinnerPosition, Toast.LENGTH_LONG).show();
            //set the default according to value
            spinner.setSelection(0);
        }

        Surname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { spinner.performClick();
            }
        });
        Surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) { spinner.performClick();}
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                   // Toast.makeText(EditProfileActivity.this, item.toString(),Toast.LENGTH_SHORT).show();
                    Surname.setText(item.toString());
                }
                //Toast.makeText(EditProfileActivity.this, "Selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void Spinner_Category(String str) {
        final Spinner spinner_category = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter myAdap = (ArrayAdapter) spinner_category.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(str);
        if(spinnerPosition>0){
            //Toast.makeText(EditProfileActivity.this,"IF spinnerPosition"+spinnerPosition, Toast.LENGTH_LONG).show();
            //set the default according to value
            spinner_category.setSelection(spinnerPosition);
        }
        else
        {   //Toast.makeText(EditProfileActivity.this,"Else spinnerPosition"+spinnerPosition, Toast.LENGTH_LONG).show();
            //set the default according to value
            spinner_category.setSelection(0);
        }

        Category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { spinner_category.performClick();
            }
        });
        Category.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) { spinner_category.performClick();}
            }
        });

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                   // Toast.makeText(EditProfileActivity.this, item.toString(),Toast.LENGTH_SHORT).show();
                    Category.setText(item.toString());
                }
                //Toast.makeText(EditProfileActivity.this, "Selected", Toast.LENGTH_SHORT).show();

            }



            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }



}
