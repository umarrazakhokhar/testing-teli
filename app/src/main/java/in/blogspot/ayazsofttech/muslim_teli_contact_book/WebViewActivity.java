package in.blogspot.ayazsofttech.muslim_teli_contact_book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Bundle bundle = getIntent().getExtras();
        String Website_link=bundle.getString("Website_link");


        if (AppStatus.getInstance(this).isOnline()) {

            webView = (WebView) findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(Website_link);
            webView.setWebViewClient(new WebViewClient());
            /**
             * Enabling zoom-in controls
             * */
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.setHorizontalScrollBarEnabled(false);

            pDialog = new ProgressDialog(WebViewActivity.this,R.style.MyDialogTheme);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
        }
        else
        {
            new AlertDialog.Builder(WebViewActivity.this,R.style.MyDialogTheme)
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

        listener();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if ( AppStatus.getInstance(WebViewActivity.this).isOnline())
        {
            if(webView.canGoBack())
            {
                webView.goBack();
            }
            else {

                super.onBackPressed();
                finish();
            }
        }
        else {

            super.onBackPressed();
            finish();
        }
    }

    private void listener() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
    }
}
