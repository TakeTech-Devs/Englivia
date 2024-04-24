package com.englivia.quiz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.englivia.quiz.Constant;
import com.englivia.quiz.R;
import com.englivia.quiz.helper.ApiConfig;
import com.englivia.quiz.helper.Utils;
import com.englivia.quiz.model.Category;
import com.englivia.quiz.model.Learn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LearningZoneActivity extends AppCompatActivity {


    public ProgressBar prgLoading;

    public String id;
    public Toolbar toolbar;
    TextView tvStartGame;
    ArrayList<Learn> learnArrayList;//=new ArrayList<>();
    FloatingActionButton bck,nxt;
    TextView tv,tv1,tv4;
    ImageView img;
    int count=0;
    WebView webView;


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_privacy_policy);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setVisibility(View.GONE);
        id = getIntent().getStringExtra("id");
        prgLoading = findViewById(R.id.prgLoading);
        tvStartGame = findViewById(R.id.tvStartGame);
        tvStartGame.setVisibility(View.GONE);
        tv=findViewById(R.id.tv);
        tv1=findViewById(R.id.tv1);tv4=findViewById(R.id.tv4);
        img=findViewById(R.id.img);
        bck=findViewById(R.id.bck);
        nxt=findViewById(R.id.nxt);
        webView=findViewById(R.id.webView1);
        webView.setVisibility(View.GONE);

        getMainCategoryFromJson1();
        try{
        //    tv.setText(learnArrayList.get(0).getDetail());

        }catch (Exception e){
            e.printStackTrace();
        }

        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(learnArrayList.size()>1){
                    if(count!=learnArrayList.size()-1) {
                        Log.e("count",String.valueOf(count));
                        Log.e("size",String.valueOf(learnArrayList.size()));
                        count = count + 1;
                        tv.setText(learnArrayList.get(count).getDetail());
                        tv1.setText(learnArrayList.get(count).getHeading());
                        tv4.setText(learnArrayList.get(count).getHeading_meaning());
                        Glide.with(LearningZoneActivity.this).load("https://cl.englivia.com/images/category/"+learnArrayList.get(count).getImage()).into(img);
                    }
                    else {
                        Toast.makeText(LearningZoneActivity.this,"No New Page",Toast.LENGTH_LONG).show();

                    }

                }
                else {
                    Toast.makeText(LearningZoneActivity.this,"No New Page",Toast.LENGTH_LONG).show();
                }
            }
        });
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(learnArrayList.size()>1){
                    if(count!=0) {
                        count = count - 1;
                        tv.setText(learnArrayList.get(count).getDetail());
                        tv1.setText(learnArrayList.get(count).getHeading());
                        tv4.setText(learnArrayList.get(count).getHeading_meaning());
                        Glide.with(LearningZoneActivity.this).load("https://cl.englivia.com/images/category/"+learnArrayList.get(count).getImage()).into(img);

                    }
                    else {
                        Toast.makeText(LearningZoneActivity.this,"No Previous Page",Toast.LENGTH_LONG).show();

                    }

                }
                else {
                    Toast.makeText(LearningZoneActivity.this,"No Previous Page",Toast.LENGTH_LONG).show();
                }
            }
        });


        try {


            getSupportActionBar().setTitle(Constant.cate_name);
            /*GetPrivacyAndTerms();*/
            if (Utils.isNetworkAvailable(this)) {
                if (!prgLoading.isShown()) {
                    prgLoading.setVisibility(View.VISIBLE);
                }

            } else {
                //setSnackBar();
            }
            prgLoading.setVisibility(View.GONE);
            tvStartGame.setOnClickListener(v -> {
                System.out.println("==== learn id "+id);
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putExtra("fromQue", "learning");
                intent.putExtra("learning_id", id);
                startActivity(intent);

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        Utils.displayInterstitial(this);
    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }

    public void getMainCategoryFromJson1() {
        // progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("get_details_by_learning", "1");
        params.put(Constant.LEARNING_ID,  id);

        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    learnArrayList=new ArrayList<>();


                // categoryList = new ArrayList<>();
                JSONObject o = new JSONObject(response);
                    Log.e("thi",response);

                    JSONArray a = o.getJSONArray("data");
                    Log.e("thi", String.valueOf(a));

                    for (int i = 0; i < a.length(); i++) {
                        JSONObject object = a.getJSONObject(i);
                        Learn learn=new Learn(object.getString("id"),object.getString("learning_id"),object.getString("detail"),object.getString("image"),object.getString("headline"),object.getString("headline_meaning"));
                    learnArrayList.add(learn);
                    Log.e("thi",object.getString("detail"));
                }
                    tv.setText(learnArrayList.get(0).getDetail());
                    tv1.setText(learnArrayList.get(0).getHeading());
                    tv4.setText(learnArrayList.get(0).getHeading_meaning());
                    Glide.with(LearningZoneActivity.this).load("https://cl.englivia.com/images/category/"+learnArrayList.get(0).getImage()).into(img);


                    //       String error = jsonObject.getString(Constant.ERROR);
/*
                    if (error.equalsIgnoreCase("false")) {
                        empty_msg.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category category = new Category();
                            JSONObject object = jsonArray.getJSONObject(i);
                            category.setId(object.getString(Constant.ID));
                            category.setName(object.getString(Constant.Title));
                            category.setMessage(object.getString(Constant.Details));
                            category.setTtlQues(object.getString(Constant.NO_OF_CATE));


                       *//*     if(Constant.IN_APP_MODE.equals("1")) {
                                if (i != 0 && i % 5 == 0) {
                                    categoryList.add(new Category(true));
                                }
                            }*//*
                            categoryList.add(category);
                        }

                        adapter = new LearningChapterActivity.CategoryAdapter(activity, categoryList);
                        recyclerView.setAdapter(adapter);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        empty_msg.setText(getString(R.string.no_category));
                        progressBar.setVisibility(View.GONE);
                        empty_msg.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        if (adapter != null) {
                            adapter = new LearningChapterActivity.CategoryAdapter(activity, categoryList);
                            recyclerView.setAdapter(adapter);
                        }
                    }*/
            }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }, params);


    }

}