package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchByActivity extends AppCompatActivity {

    DatabaseHandler_Contact databaseHandler;
    EditText EditText_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by);

        TextView textView_Search=findViewById(R.id.search);
        EditText_Search=findViewById(R.id.EditText_Search);
        TextInputLayout textInputLayout = (TextInputLayout)findViewById(R.id.text_input_layout);


        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {

            String SearchBy=bundle.getString("SearchBy");
            //Toast.makeText(this,""+SearchBy,Toast.LENGTH_SHORT).show();
            // Loading spinner data from database
            textView_Search.setText("Search By "+SearchBy);
            textInputLayout.setHint("Search By "+SearchBy);
            loadSpinnerData(SearchBy);

        }

        //SearchBy();


    }




    private void loadSpinnerData(String SearchBy) {
        final Spinner spinner_city = findViewById(R.id.spinner);
        databaseHandler = new DatabaseHandler_Contact(this);
        List<String> list = new ArrayList<String>();
        if(SearchBy.equals("SurName"))
        {
            Cursor cursor=databaseHandler.SelectData_suranme_group();
            while (cursor.moveToNext())
            {
                String str=cursor.getString(0);
                //Toast.makeText(SearchByActivity.this,str, Toast.LENGTH_SHORT).show();
                if(str!=null)
                {
                    list.add(""+str);
                }


            }
        }

        if(SearchBy.equals("City"))
        {
            Cursor cursor=databaseHandler.SelectData_city_group();
            while (cursor.moveToNext())
            {
                String str=cursor.getString(0);
                //Toast.makeText(SearchByActivity.this,str, Toast.LENGTH_SHORT).show();
                if(str!=null)
                {
                    list.add(""+str);
                }
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_city.setAdapter(dataAdapter);

        EditText_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { spinner_city.performClick();
            }
        });
        EditText_Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                    //Toast.makeText(SearchByActivity.this, item.toString(),Toast.LENGTH_SHORT).show();
                    //CityText.setText(item.toString());
                    SearchBy(item.toString());
                    EditText_Search.setText(item.toString());
                }
                //Toast.makeText(SearchByActivity.this, "Selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void SearchBy(String str) {

        ////////////Display the custome listview start//////

        databaseHandler= new DatabaseHandler_Contact(this);
        ListView listView= (ListView) findViewById(R.id.listview);

        Cursor cursor=databaseHandler.SelectData_SearchBy(str);

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

                        Intent intent=new Intent(SearchByActivity.this, Profile_Activity.class);
                        intent.putExtra("Mobile_Number",(db_mobile[position]));
                        startActivity(intent);

                    }
                }
        );
        //////////////Display the custome listview end
    }


}
