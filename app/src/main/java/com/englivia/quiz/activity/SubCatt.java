package com.englivia.quiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.englivia.quiz.Constant;
import com.englivia.quiz.R;
import com.englivia.quiz.helper.Utils;
import com.englivia.quiz.model.Level;

import java.util.List;

public class SubCatt extends AppCompatActivity {
    public Toolbar toolbar;
    public static String fromQue;
    CardView cardquiz1,cardquiz,cardquiz2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catt);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.select_level));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fromQue = getIntent().getStringExtra("fromQue");
        cardquiz1=findViewById(R.id.cardQuiz1);
        cardquiz=findViewById(R.id.cardQuiz);
        cardquiz2=findViewById(R.id.cardQuiz2);
        cardquiz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCatt.this, LearningChapterActivity.class);
                intent.putExtra("key", "1");
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("message", getIntent().getStringExtra("message"));
                startActivity(intent);
            }
        });
        cardquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCatt.this, LearningChapterActivity.class);
                intent.putExtra("key", "0");
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("message", getIntent().getStringExtra("message"));
                startActivity(intent);
            }
        });

        cardquiz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCatt.this, LearningChapterActivity.class);
                intent.putExtra("key", "e");
                intent.putExtra("title", getIntent().getStringExtra("title"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("message", getIntent().getStringExtra("message"));
                startActivity(intent);
            }
        });

        Utils.showBannerAds(SubCatt.this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}