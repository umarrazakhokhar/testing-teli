package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {
    EditText EditTextId,EditTextPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        EditTextId=findViewById(R.id.EditText_id);
        EditTextPass=findViewById(R.id.EditText_pass);

       Button btnLogin=findViewById(R.id.btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id=EditTextId.getText().toString();
                final String passs=EditTextPass.getText().toString();
                //Toast.makeText(AdminActivity.this,"Login Not",Toast.LENGTH_SHORT).show();


                if(id.equals("63ashfaque") && passs.equals("14106321"))
                {
                    Toast.makeText(AdminActivity.this,"Login Sussesful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminActivity.this, AdminShowActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(AdminActivity.this,"Login Not",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
