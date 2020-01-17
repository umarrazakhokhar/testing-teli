package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class AdminShowActivity extends AppCompatActivity {

    private EditText SearchText;
    DatabaseHandler_Contact databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show);

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

       Button add_new_record =findViewById(R.id.add_new_record);
        add_new_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminShowActivity.this,AddContactProfileActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SearchingFunction("");


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

                        Intent intent = new Intent(AdminShowActivity.this,Admin_ADD_Activity.class);
                        intent.putExtra("Edit_Mobile",(db_mobile[position]));
                        startActivity(intent);

                    }
                }
        );
        //////////////Display the custome listview end
    }

}
