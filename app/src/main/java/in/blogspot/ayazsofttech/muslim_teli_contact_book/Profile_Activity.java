package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Profile_Activity extends AppCompatActivity {

    // DatabaseHandler_Contact databaseHandler2;
    TextView Name, Mobile, Surname, City,FatherName,District,State,HomeTown,Category;

    String mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        Name = findViewById(R.id.TextView_Name);
        Mobile = findViewById(R.id.TextView_Mobile);
        FatherName = findViewById(R.id.TextView_FatherName);
        Surname = findViewById(R.id.TextView_SurName);
        District = findViewById(R.id.TextView_District);
        City = findViewById(R.id.TextView_City);
        State= findViewById(R.id.TextView_State);
        HomeTown = findViewById(R.id.TextView_HomeTown);
        Category = findViewById(R.id.TextView_Category);


        Bundle bundle = getIntent().getExtras();
        String Mobile_Number = bundle.getString("Mobile_Number");

        //Toast.makeText(this, Mobile_Number, Toast.LENGTH_SHORT).show();

        DatabaseHandler_Contact databaseHandler = new DatabaseHandler_Contact(this);
        Cursor cursor = databaseHandler.SelectData_Where_Mobile(Mobile_Number);


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

            mobile=(cursor.getString(0));


            Mobile.setText(cursor.getString(0).substring(0,7)+"...");
            Name.setText(cursor.getString(1));
            Surname.setText(cursor.getString(2));
            FatherName.setText(cursor.getString(3));
            Category.setText(cursor.getString(4));
            District.setText(cursor.getString(5));
            City.setText(cursor.getString(6));
            State.setText(cursor.getString(7));
            HomeTown.setText(cursor.getString(8));


        }


        FloatingActionButton mfab_Call= (FloatingActionButton) findViewById(R.id.fab_Call);
        mfab_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number ="+91"+mobile;
                //Toast.makeText(Profile_Activity.this, number, Toast.LENGTH_SHORT).show();
                try
                {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);

                } catch (Exception e) {
                    Toast.makeText(Profile_Activity.this, "CALLING NOT WORKING "+e, Toast.LENGTH_SHORT).show();
                    Log.d("ashu", String.valueOf(e));
                }

            }
        });

        FloatingActionButton mfab_whatsapp= (FloatingActionButton) findViewById(R.id.fab_whatsapp);
        mfab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String number = "91"+mobile;
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");//phone number without "+" prefix
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Toast.makeText(Profile_Activity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();

                }

            }
        });

        FloatingActionButton mfab_Message= (FloatingActionButton) findViewById(R.id.fab_Message);
        mfab_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = "+91"+mobile;

                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+ number)));

                } catch (Exception e) {
                    Toast.makeText(Profile_Activity.this, "MESSAGE NOT WORKING "+e, Toast.LENGTH_SHORT).show();
                    Log.d("ashu", String.valueOf(e));
                }

            }
        });

        FloatingActionButton mfab_share= (FloatingActionButton) findViewById(R.id.fab_share);
        mfab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String teli_share="Name :- "+Name.getText().toString()+"\n"+
                                  "Father Name :- "+FatherName.getText().toString()+"\n"+
                                    "Surname :- "+Surname.getText().toString()+"\n"+
                                    "Mobile No :- "+mobile+"\n"+
                                     "City :- "+City.getText().toString()+"\n"+
                                    "District :- "+District.getText().toString()+"\n";

                final String appPackageName =getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
                final String AppName=getString(R.string.app_name);

                Toast.makeText(Profile_Activity.this,"Share",Toast.LENGTH_SHORT).show();
                Intent myIntent=new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareSub=AppName;

                String shareBody=teli_share+"\n\n https://play.google.com/store/apps/details?id=" + appPackageName;

                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent,"Share Using"));

            }
        });

        FloatingActionButton mfab_group_chart= (FloatingActionButton) findViewById(R.id.fab_group_chart);
        mfab_group_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/joinchat/Ni3fahddRs1KjZKGbHQWGg"));
                startActivity(telegram);

            }
        });

        FloatingActionButton mfab_map= (FloatingActionButton) findViewById(R.id.fab_map);
        mfab_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "https://www.google.co.in/maps/place/"+City.getText().toString()+","+District.getText().toString()+","+State.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



}
