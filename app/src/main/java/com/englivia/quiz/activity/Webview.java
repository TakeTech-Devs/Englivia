package com.englivia.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.englivia.quiz.Constant;
import com.englivia.quiz.R;

import org.w3c.dom.Text;

public class Webview extends AppCompatActivity {
    Webview webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView browser = (WebView) findViewById(R.id.webview);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        TextView webViewText = findViewById(R.id.web_view_text);

        if(getIntent().getStringExtra("type").equals("about")){
            //browser.loadUrl("https://englivia.com/app-about-us/");
            webViewText.setText(Constant.about_us_text);

        }
        if(getIntent().getStringExtra("type").equals("terms")){
            //browser.loadUrl("https://englivia.com/terms-and-conditions/");
            webViewText.setText(Constant.terms_and_service_text);

        }
        if(getIntent().getStringExtra("type").equals("privacy")){
            //browser.loadUrl("https://englivia.com/privacy-policy-2/");
            webViewText.setText(Constant.privacy_policy_text);

        }
    }
}