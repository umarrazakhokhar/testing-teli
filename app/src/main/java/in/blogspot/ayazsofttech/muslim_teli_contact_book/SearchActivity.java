package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText NameText,FatherNameText,SurNameText,MobileText,CityText;
    DatabaseHandler_Contact databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchingFunction("","","","",""); //default searching

        NameText = (EditText)findViewById(R.id.EditText_Name);
        FatherNameText = (EditText)findViewById(R.id.EditText_FatherName);
        SurNameText = (EditText)findViewById(R.id.EditText_SurName);
        MobileText = (EditText)findViewById(R.id.EditText_Mobile);
        CityText = (EditText)findViewById(R.id.EditText_City);

        addListenerOnSpinnerItemSelection();//spinner add

        Button btn_Search=(Button)findViewById(R.id.btn_Search);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SearchActivity.this,"Please Wait...",Toast.LENGTH_SHORT).show();

                String mNameText=NameText.getText().toString();
                String mFatherNameText=FatherNameText.getText().toString();
                String mSurNameText=SurNameText.getText().toString();
                String mMobileText=MobileText.getText().toString();
                String mCityText=CityText.getText().toString();


                SearchingFunction(mMobileText,mNameText,mFatherNameText,mSurNameText,mCityText);

            }
        });


        // Loading spinner data from database  
        loadSpinnerData();

        // Spinner for Surname
        Spinner_Surname(SurNameText.getText().toString());

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

        SurNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { spinner.performClick();
            }
        });
        SurNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                    SurNameText.setText(item.toString());
                }
                //Toast.makeText(EditProfileActivity.this, "Selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void loadSpinnerData() {
        final Spinner spinner_city = findViewById(R.id.spinner_city);
        databaseHandler = new DatabaseHandler_Contact(this);
        List<String> list = new ArrayList<String>();
        // List<String> labels = (List<String>) databaseHandler.SelectData();

        Cursor cursor=databaseHandler.SelectData_city_group();
        int n=0;

        while (cursor.moveToNext())
        {
            //list.add(cursor.getString(0));

            String str=cursor.getString(0);
            //Toast.makeText(SearchByActivity.this,str, Toast.LENGTH_SHORT).show();
            if(str!=null)
            {
                list.add(""+str);
            }

        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_city.setAdapter(dataAdapter);

        CityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { spinner_city.performClick();
            }
        });
        CityText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) { spinner_city.performClick();}
            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                    //Toast.makeText(SearchActivity.this, item.toString(),Toast.LENGTH_SHORT).show();
                    CityText.setText(item.toString());
                }
                //Toast.makeText(EditProfileActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

    }



    private void SearchingFunction(String mMobileText, String mNameText, String mFatherNameText, String mSurNameText, String mCityText) {

        ////////////Display the custome listview start//////

        databaseHandler= new DatabaseHandler_Contact(this);
        ListView listView= (ListView) findViewById(R.id.listview);

        Cursor cursor=databaseHandler.SelectData_Where_search(mMobileText,mNameText,mFatherNameText,mSurNameText,mCityText);

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
        //Custom_List_row custom_list_row=new Custom_List_row(this,"","","","","","","");
        listView.setAdapter(custom_list_row);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(MainActivity.this,db_mobile[position]+db_name[position]+db_father[position]+db_surname[position]+db_city[position],Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(SearchActivity.this, Profile_Activity.class);
                        intent.putExtra("Mobile_Number",(db_mobile[position]));
                        startActivity(intent);

                    }
                }
        );
        //////////////Display the custome listview end
    }

    private void addListenerOnSpinnerItemSelection() {

        // Spinner element
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                    Toast.makeText(SearchActivity.this, item.toString(),Toast.LENGTH_SHORT).show();
                    SurNameText.setText(item.toString());
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
