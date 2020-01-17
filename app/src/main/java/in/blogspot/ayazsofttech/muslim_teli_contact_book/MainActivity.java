package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHandler_Contact databaseHandler;
    private DatabaseReference mFirebaseDatabase;
    private EditText SearchText;


    ProgressDialog progressdialog;
    int status = 0;
    Handler handler = new Handler();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = sharedPreferences.edit();

        if ( AppStatus.getInstance(MainActivity.this).isOnline()) {
            update_firebase_online();
        }
       // firebase_online();


                Bundle bundle = getIntent().getExtras();
                if (bundle!=null) {

                   // Toast.makeText(this,"Bundle:-Verify="+bundle.getString("Verify_Mobile")+";No of record= "+bundle.getInt("No_of_record"),Toast.LENGTH_SHORT).show();

                    String Verify_Mobile=bundle.getString("Verify_Mobile");
                    if(!Verify_Mobile.equals(""))
                    {
                        editor.putString("mobile",Verify_Mobile ).commit();
                    }

                    //retutn for editProfileActivity



                }

        // Last Update Date Start
        TextView last_update=findViewById(R.id.last_update);
        last_update.setText("Last updated on "+sharedPreferences.getString("Date", ""));
        // Last Update Date End

        ////////////Display the custome listview start//////

        SearchText = (EditText)findViewById(R.id.EditText_Search);

        SearchingFunction(""); //default searching


        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Toast.makeText(SearchActivity.this,"beforeTextChanged",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Toast.makeText(SearchActivity.this,"onTextChanged"+s,Toast.LENGTH_SHORT).show();
                if(!s.equals(""))
                {
                    SearchingFunction(String.valueOf(s));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(SearchActivity.this,"afterTextChanged",Toast.LENGTH_SHORT).show();
            }
        });
        //////////////Display the custome listview end


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this,R.style.MyDialogTheme)
                    .setTitle("Share Or Quit !!!!")
                    .setMessage("Meri Auqaat Is Qabil Tu Nahe Ke Me Jannat Mangon Ya Rab Bus Itni Si Arz He Ke Mujhey Jahannum Se Bacha Lena.")
                    .setNegativeButton(android.R.string.no, null)

                    .setNeutralButton("Share", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            final String appPackageName =getApplicationContext().getPackageName();// getPackageName() from Context or Activity object
                            final String AppName=getString(R.string.app_name);
                            Intent myIntent=new Intent(Intent.ACTION_SEND);
                            myIntent.setType("text/plain");
                            String shareSub=AppName;
                            String teli_share="आली जनाब अस्सलामु अलैकुम।\n" +
                                    "\n" +
                                    "मोहतरम बुजुर्गो, भाइयों व मेरे अजीज दोस्तों | \n" +
                                    "\n" +
                                    "यह ऐप मुस्लिम तेली कांटेक्ट बुक( Muslim Teli Contact Book App ) मुस्लिम तेली जमात के लिए निकाली गई है | \n" +
                                    "\n" +
                                    "All India मुस्लिम तेली 53 के नंबर की *App आप की खिदमत में पेश है कबूल कीजिये | \n" +
                                    "\n" +
                                    "App बनाने का सबब यह है कि दूरदराज के शहरों व  गांव में रहने वाले बिरादरी के लोगों को एक दूसरे से ज्यादा से ज्यादा आपस में जोड़ सकुं | \n" +
                                    "\n" +
                                    "इसी  मकसद से मैने एक  छोटी सी कोशिश की है | \n" +
                                    "\n" +
                                    "अगर हमारी मुस्लिम तेली कांटेक्ट बुक ऐप ( Muslim Teli Contact Book App ) बनाने में किसी भी प्रकार की गलती रह गई हो तो मेहरबानी करके सुधार वाले | \n" +
                                    "\n" +
                                    "इस App में मुझसे जो भी गलती हो गई हो तो मैं माफी चाहता हूं\n" +
                                    "\n" +
                                    "इस App में आपकी जानकारी:-\n" +
                                    "1. आप का नाम,\n" +
                                    "2. आप के वालिद का नाम,\n" +
                                    "3. आप की गोत,\n" +
                                    "4. जिला शहर और  राज्य,\n" +
                                    "5. आप का मोबाइल नंबर,\n" +
                                    "6. आप का मूल निवास ( कहां वाले  )\n" +
                                    "\n" +
                                    "इस App में आप किसी का भी नंबर देख सकते हो और साथ ही साथ उसे Call, Message या Whatsapp भी कर सकते हो। \n" +
                                    "\n" +
                                    "अपने मुस्लिम तेली भाई और दोस्त के साथ शेयर करें,\n" +
                                    "आपका बहुत-बहुत शुक्रिया।\n" +
                                    "\n" +
                                    "\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47 Link नीचे दी गई है  \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\n" +
                                    "\n" ;

                            String shareBody=AppName+"\n\nMeri Auqaat Is Qabil Tu Nahe Ke Me Jannat Mangon Ya Rab Bus Itni Si Arz He Ke Mujhey Jahannum Se Bacha Lena.\n\n"
                                    +teli_share+
                                    "\nhttps://play.google.com/store/apps/details?id=" + appPackageName;

                            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                            startActivity(Intent.createChooser(myIntent,"Share Using")); }
                    })

                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();


                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity ayazsofttech AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_notification) {

            Intent intent = new Intent(this,NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,EditProfileActivity.class);
            intent.putExtra("Edit_Mobile",sharedPreferences.getString("mobile","" ));
            startActivity(intent);
        }
        if (id == R.id.nav_add_contact) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,AddContactProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_search_by_surname) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,SearchByActivity.class);
            intent.putExtra("SearchBy","SurName");
            startActivity(intent);
        }
        if (id == R.id.nav_search_by_city) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,SearchByActivity.class);
            intent.putExtra("SearchBy","City");
            startActivity(intent);
        }
        if (id == R.id.nav_advanced_Search) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_Contactus) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,ContactUsActivity.class);
            startActivity(intent);
        }


        if(id==R.id.nav_rating)
        {
            Toast.makeText(MainActivity.this,"Rating",Toast.LENGTH_SHORT).show();
            final String appPackageName =getApplicationContext().getPackageName();// getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

            return true;
        }

        if(id==R.id.nav_more_app_play_store)
        {
            Toast.makeText(MainActivity.this,"More App",Toast.LENGTH_SHORT).show();

            final String appPackageName ="https://play.google.com/store/apps/developer?id=AYAZ+soft+tech";  // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
            }

            return true;
        }

        if(id==R.id.nav_Privacy_Policy)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ayazsofttech.blogspot.com/p/privacy-policy-of-ayaz-soft-tech-this.html"));
            startActivity(intent);
            return true;
        }
        if (id == R.id.nav_al_quran_ul_karim) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("Website_link","https://www.dawateislami.net/quran");
            startActivity(intent);
        }

        if (id == R.id.nav_al_quran_hindi) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("Website_link","https://drive.google.com/open?id=1omF9fRhp8qyqCgHJqvSEZpRFOIL-J30N");
            startActivity(intent);
        }
        if (id == R.id.nav_quran) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("Website_link","https://quran.com/");
            startActivity(intent);
        }
        if (id == R.id.nav_al_quran_english) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("Website_link","https://al-quran.info");
            startActivity(intent);
        }
        if (id == R.id.nav_al_quran_audio) {
            // Handle the camera action sharedPreferences.getString("name","" )
            Intent intent = new Intent(this,WebViewActivity.class);
            intent.putExtra("Website_link","https://quranonline.net/");
            startActivity(intent);
        }
        if (id == R.id.nav_share) {
            final String appPackageName =getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
            final String AppName=getString(R.string.app_name);

            Toast.makeText(MainActivity.this,"Share",Toast.LENGTH_SHORT).show();
            Intent myIntent=new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareSub=AppName;

            String teli_share="आली जनाब अस्सलामु अलैकुम।\n" +
                    "\n" +
                    "मोहतरम बुजुर्गो, भाइयों व मेरे अजीज दोस्तों | \n" +
                    "\n" +
                    "यह ऐप मुस्लिम तेली कांटेक्ट बुक( Muslim Teli Contact Book App ) मुस्लिम तेली जमात के लिए निकाली गई है | \n" +
                    "\n" +
                    "All India मुस्लिम तेली 53 के नंबर की *App आप की खिदमत में पेश है कबूल कीजिये | \n" +
                    "\n" +
                    "App बनाने का सबब यह है कि दूरदराज के शहरों व  गांव में रहने वाले बिरादरी के लोगों को एक दूसरे से ज्यादा से ज्यादा आपस में जोड़ सकुं | \n" +
                    "\n" +
                    "इसी  मकसद से मैने एक  छोटी सी कोशिश की है | \n" +
                    "\n" +
                    "अगर हमारी मुस्लिम तेली कांटेक्ट बुक ऐप ( Muslim Teli Contact Book App ) बनाने में किसी भी प्रकार की गलती रह गई हो तो मेहरबानी करके सुधार वाले | \n" +
                    "\n" +
                    "इस App में मुझसे जो भी गलती हो गई हो तो मैं माफी चाहता हूं\n" +
                    "\n" +
                    "इस App में आपकी जानकारी:-\n" +
                    "1. आप का नाम,\n" +
                    "2. आप के वालिद का नाम,\n" +
                    "3. आप की गोत,\n" +
                    "4. जिला शहर और  राज्य,\n" +
                    "5. आप का मोबाइल नंबर,\n" +
                    "6. आप का मूल निवास ( कहां वाले  )\n" +
                    "\n" +
                    "इस App में आप किसी का भी नंबर देख सकते हो और साथ ही साथ उसे Call, Message या Whatsapp भी कर सकते हो। \n" +
                    "\n" +
                    "अपने मुस्लिम तेली भाई और दोस्त के साथ शेयर करें,\n" +
                    "आपका बहुत-बहुत शुक्रिया।\n" +
                    "\n" +
                    "\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47 Link नीचे दी गई है  \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\uD83D\uDC47\n" +
                    "\n" ;

            String shareBody=teli_share+"\n\n https://play.google.com/store/apps/details?id=" + appPackageName;

            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(myIntent,"Share Using"));

        }
        if (id == R.id.nav_telegram) {

            Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/muslim_teli"));
            startActivity(telegram);

        }
        if (id == R.id.nav_telegram_group) {

            Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/joinchat/Ni3fahddRs1KjZKGbHQWGg"));
            startActivity(telegram);

        }
        if (id == R.id.nav_Logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    private void ShowProgressDialog(){
        status = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(status < 100){

                    status +=1;

                    try{
                        Thread.sleep(300);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            progressdialog.setProgress(status);

                            if(status == 100){

                                progressdialog.dismiss();

                                Cursor cursor=databaseHandler.SelectData_Where_Mobile(sharedPreferences.getString("mobile","1" ));
                                int count=cursor.getCount();
                                if(count>0)
                                {
                                    onStart_Ashu();
                                }
                                else {
                                    new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                                            .setTitle("Please Fill The Form In English.")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                                                    intent.putExtra("Edit_Mobile", sharedPreferences.getString("mobile", ""));
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            })
                                            .setCancelable(false)
                                            .create().show();
                                }
                            }
                        }
                    });
                }
            }
        }).start();

    }

    private void CreateProgressDialog() {

        progressdialog = new ProgressDialog(MainActivity.this,R.style.MyDialogTheme);
        progressdialog.setIndeterminate(false);
        progressdialog.setTitle("Wait For Sync Contacts...");
        //progressdialog.setMessage("Contact Number Downloading...");
        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressdialog.setCancelable(false);
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.setMax(100);
        progressdialog.show();

    }

    private void update_firebase_online() {

        final String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        if(sharedPreferences.getString("Date", "").equals(date))
        {
            //Toast.makeText(MainActivity.this,"Up To Date",Toast.LENGTH_SHORT).show();
           /* if(sharedPreferences.getString("name", "").equals(""))
            {
                Intent intent = new Intent(MainActivity.this,EditProfileActivity.class);
                intent.putExtra("Edit_Mobile",sharedPreferences.getString("mobile","" ));
                finish();
                startActivity(intent);
            }*/

        }
        else
        {
            //Toast.makeText(MainActivity.this,"Please Wait...",Toast.LENGTH_SHORT).show();

            CreateProgressDialog();
            ShowProgressDialog();
            editor.putString("Date",date).commit();
            firebase_online();

        }


    }


    @Override
    protected void onStart() {
        super.onStart();

        onStart_Ashu();

    }

    private void onStart_Ashu() {
        SearchingFunction(""); //default searching

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        TextView Profile_Name=header.findViewById(R.id.Profile_Name);
        TextView Profile_Mobile=header.findViewById(R.id.Profile_Mobile);

        Cursor cursor2=databaseHandler.SelectData_Where_Mobile(sharedPreferences.getString("mobile","1" ));
        while (cursor2.moveToNext())
        {
            editor.putString("mobile", cursor2.getString(0)).commit();
            editor.putString("name", cursor2.getString(1)).commit();
            editor.putString("father", cursor2.getString(2)).commit();
            editor.putString("surname", cursor2.getString(3)).commit();
            editor.putString("city", cursor2.getString(4)).commit();

            // Toast.makeText(this,sharedPreferences.getString("mobile","" )+sharedPreferences.getString("name","" ),Toast.LENGTH_SHORT).show();

            Profile_Mobile.setText(cursor2.getString(0));
            Profile_Name.setText(cursor2.getString(1));

        }
    }




    private void SearchingFunction( String s) {

        ////////////Display the custome listview start//////

        databaseHandler= new DatabaseHandler_Contact(this);
        ListView listView= (ListView) findViewById(R.id.listview);

        Cursor cursor=databaseHandler.SelectData_Where_All(s);

        final String[] db_mobile =new String[cursor.getCount()];
        final String[] db_name =new String[cursor.getCount()];
        final String[] db_father =new String[cursor.getCount()];
        final String[] db_surname =new String[cursor.getCount()];
        final String[] db_city =new String[cursor.getCount()];
        final String[] db_state =new String[cursor.getCount()];
        final String[] db_Verified =new String[cursor.getCount()];
        int n=0;

        //Total contact number
        TextView total_contacts=findViewById(R.id.total_contacts);
        total_contacts.setText("Total Contact :=> "+cursor.getCount());

        while (cursor.moveToNext())
        {
            db_mobile[n]=cursor.getString(0);
            db_name[n]=cursor.getString(1);
            db_surname[n]=cursor.getString(2);
            db_father[n]=cursor.getString(3);
            db_city[n]=cursor.getString(6);
            db_state[n] = cursor.getString(7);
            db_Verified[n]="";

            n++;

        }

        //CustomListview customListview=new CustomListview(this,db_name,db_mobile);

        Custom_List_row custom_list_row=new Custom_List_row(this,db_mobile,db_name,db_surname,db_father,db_city,db_state,db_Verified);
        //Custom_List_row custom_list_row=new Custom_List_row(this,"ashu","ayyub","Nagpur","8793215893","Verified");
        listView.setAdapter(custom_list_row);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(MainActivity.this,db_mobile[position]+db_name[position]+db_father[position]+db_surname[position]+db_city[position],Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this, Profile_Activity.class);
                        intent.putExtra("Mobile_Number",(db_mobile[position]));
                        startActivity(intent);

                    }
                }
        );
        //////////////Display the custome listview end
    }


    ////////////Online firebase database start/////////////////
    public void firebase_online()
    {

        //Toast.makeText(MainActivity.this,"Please Wait....", Toast.LENGTH_LONG).show();
        databaseHandler=new DatabaseHandler_Contact(this);

        databaseHandler.TRUNCATE_table(); //delete the all record in the Sqlite

        mFirebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Muslim_Teli_Contact_Book").child("Contact_Number");

        mFirebaseDatabase.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                //String key=dataSnapshot.getKey();
                //Toast.makeText(MainActivity.this,key,Toast.LENGTH_SHORT).show();

                String  mobile = dataSnapshot.child("mobile").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                String surname = dataSnapshot.child("surname").getValue(String.class);
                String father = dataSnapshot.child("father").getValue(String.class);
                String category = dataSnapshot.child("category").getValue(String.class);
                String district = dataSnapshot.child("district").getValue(String.class);
                String city = dataSnapshot.child("city").getValue(String.class);
                String state = dataSnapshot.child("state").getValue(String.class);
                String hometown = dataSnapshot.child("hometown").getValue(String.class);

                /*
                Toast.makeText(MainActivity.this,"mobile="+mobile,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"name="+name,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"father="+father,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"surname="+surname,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"city="+city,Toast.LENGTH_SHORT).show();
                */

                boolean result=databaseHandler.insertData1(mobile,name,surname,father,category,district,city,state,hometown);

                if(result == true)
                {
                    //Toast.makeText(MainActivity.this,"DATA INSERTED IN MOBILE",Toast.LENGTH_SHORT).show();
                }
                else
                {
                  // Toast.makeText(MainActivity.this,"DATA NOT INSERTED IN MOBILE", Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                //databaseHandler.UpdateData(key,value);

               // Toast.makeText(MainActivity.this,"Some Data will Update",Toast.LENGTH_SHORT).show();

                ///////////////Refresh the page//
                /* Intent intent = getIntent();
                finish();
                startActivity(intent);*/
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
               // Toast.makeText(MainActivity.this,"Unwanted data will delete",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

              //  Toast.makeText(MainActivity.this,"Error data will delete",Toast.LENGTH_SHORT).show();

            }
        });


    }
    ////////////online firebase database end//////////


}
