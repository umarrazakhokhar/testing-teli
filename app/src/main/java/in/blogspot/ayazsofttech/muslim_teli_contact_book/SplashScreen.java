package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        addNotification();
        AnimationS();


        if ( AppStatus.getInstance(SplashScreen.this).isOnline())
        {

           // Toast.makeText(SplashScreen.this,"Online",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    String keyIdentifer  = null;
                    i.putExtra("strName", keyIdentifer );
                    startActivity(i);

                    // close this activity
                    finish();
                    //adDisplay();
                }
            }, SPLASH_TIME_OUT);
        }
        else
        {
            // Toast.makeText(SplashScreen.this,"Online",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent intent= new Intent(SplashScreen.this, NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }




    }


    private void addNotification() {

      String msg="Thank You For Using This ("+getString(R.string.app_name)+" ) App, Please Give The Good Rating 4+ And Good Comment And Share it ......";

      NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Assalam O Alaikum Dear Friends,")
                        .setContentText(msg);

        Intent notificationIntent = new Intent(this, SplashScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }



    //Animation Splash screen
    public  void AnimationS()
    {
       LinearLayout logo=findViewById(R.id.linearlayout);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        logo.startAnimation(myanim);
    }

}