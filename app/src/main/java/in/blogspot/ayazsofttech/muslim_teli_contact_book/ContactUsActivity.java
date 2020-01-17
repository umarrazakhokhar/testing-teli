package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);



        FloatingActionButton mfab_Call= (FloatingActionButton) findViewById(R.id.fab_Call);
        mfab_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = "+918793215893";
                //Toast.makeText(Profile_Activity.this, number, Toast.LENGTH_SHORT).show();
                try
                {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);

                } catch (Exception e) {
                    Toast.makeText(ContactUsActivity.this, "CALLING NOT WORKING "+e, Toast.LENGTH_SHORT).show();
                    Log.d("ashu", String.valueOf(e));
                }

            }
        });

        FloatingActionButton mfab_whatsapp= (FloatingActionButton) findViewById(R.id.fab_whatsapp);
        mfab_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    //Toast.makeText(ContactUsActivity.this,"whats",Toast.LENGTH_SHORT).show();
                    String number ="918793215893";
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");//phone number without "+" prefix
                    startActivity(sendIntent);

                } catch (Exception e) {
                    Toast.makeText(ContactUsActivity.this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();

                }

            }
        });

        FloatingActionButton mfab_Message= (FloatingActionButton) findViewById(R.id.fab_Message);
        mfab_Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = "+918793215893";

                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+ number)));

                } catch (Exception e) {
                    Toast.makeText(ContactUsActivity.this, "MESSAGE NOT WORKING "+e, Toast.LENGTH_SHORT).show();
                    Log.d("ashu", String.valueOf(e));
                }

            }
        });

        FloatingActionButton mfab_admin= (FloatingActionButton) findViewById(R.id.fab_admin);
        mfab_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactUsActivity.this, AdminActivity.class);
                finish();
                startActivity(intent);

            }
        });


    }
}
