package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity {

    DatabaseHandler_Ali databaseHandler;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    ProgressDialog progressdialog;
    int status = 0;
    Handler handler = new Handler();


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();

        final String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());





        final FloatingActionButton mfab_update= (FloatingActionButton) findViewById(R.id.fab_update);
        mfab_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebase_online_A_Hazrat_Ali_Quotes();

               CreateProgressDialog();
               ShowProgressDialog();
                mfab_update.setVisibility(View.GONE);

            }
        });



        if ( !AppStatus.getInstance(NotificationActivity.this).isOnline()) {
            new AlertDialog.Builder(NotificationActivity.this,R.style.MyDialogTheme)
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







        if (! AppStatus.getInstance(this).isOnline()) {



            mfab_update.setVisibility(View.GONE);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //declare array index

    public void display_LinkView()
    {
        ////////////Display the custome listview start//////

        ListView listView= (ListView) findViewById(R.id.listview);

        Cursor cursor=databaseHandler.SelectData();
        final String[] mMessages =new String[cursor.getCount()];
        final String[] mkeys =new String[cursor.getCount()];
        int n=0;



        while (cursor.moveToNext())
        {
            // mMessages[Integer.parseInt(cursor.getString(0))]=cursor.getString(1);
            String string_key = cursor.getString(1);
            mkeys[n]=cursor.getString(1);
            String string = cursor.getString(1);
            mMessages[n]=string;//we add the message
            // Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
            n++;

        }

        CustomListview_hazrat_ali customListview_hazrat_ali=new CustomListview_hazrat_ali(this,mMessages);
        listView.setAdapter(customListview_hazrat_ali);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(Hazrat_Ali.this,mMessages[position],Toast.LENGTH_SHORT).show();

                        Intent myIntent=new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");

                        String shareSub="Hazrat ALI";
                        String shareBody=mMessages[position]+"\n\n" +
                                getString(R.string.app_name) +
                                "\n\nhttps://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName(); ;


                        myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                        myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                        startActivity(Intent.createChooser(myIntent,"Share Using"));

                    }
                }
        );
        //////////////Display the custome listview end
    }

    //display_LinkView link list end

    /////////////Online firebase database Update start/////////////////


    private DatabaseReference mDatabase;
    public void firebase_online_A_Hazrat_Ali_Quotes()
    {
        databaseHandler=new DatabaseHandler_Ali(this);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Muslim_Teli_Contact_Book").child("A_Hazrat_Ali_Quotes").child("Messages");


        //  mDatabase= FirebaseDatabase.getInstance().getReference().child("calander_testing").child("event_testing");


        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        // DatabaseReference myRef = database.getReference().child("Calender_Table").child("Events");

        // myRef.setValue("Hello, World!");

        mDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                String value2 = dataSnapshot.getValue(String.class);
                Log.d("tag", "Value is: " + value2);

                String key=dataSnapshot.getKey();
                String value=dataSnapshot.getValue(String.class);
                //mkeys.add(key);

                //boolean result=databaseHandler.insertData(2017,"ereewrt");
                boolean result=databaseHandler.insertData(key,value);
                if(result == true)
                {
                   // Toast.makeText(NotificationActivity.this,"DATA INSERTED IN MOBILE",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(NotificationActivity.this,"aaaa", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                String value=dataSnapshot.getValue(String.class);
                String key=dataSnapshot.getKey();

                databaseHandler.UpdateData(key,value);

                Toast.makeText(NotificationActivity.this,"Some Data will Update",Toast.LENGTH_SHORT).show();

                ///////////////Refresh the page//
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String key = dataSnapshot.getKey();

                Toast.makeText(NotificationActivity.this,"Unwanted data will delete",Toast.LENGTH_SHORT).show();

                databaseHandler.DeleteData(key);

                ///////////////Refresh the page//
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(NotificationActivity.this,"Error data will delete",Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void ShowProgressDialog(){
        status = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){

                    status +=1;

                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();
                                display_LinkView();

                            }
                        }
                    });
                }
            }
        }).start();

    }

    private void CreateProgressDialog() {

        progressdialog = new ProgressDialog(NotificationActivity.this,R.style.MyDialogTheme);
        progressdialog.setIndeterminate(false);
        progressdialog.setTitle("Wait For Notification...");
        //progressdialog.setMessage("Contact Number Downloading...");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
        progressdialog.show();

    }


}


