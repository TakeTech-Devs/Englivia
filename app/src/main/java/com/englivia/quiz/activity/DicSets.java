package com.englivia.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.englivia.quiz.Constant;
import com.englivia.quiz.R;
import com.englivia.quiz.helper.ApiConfig;
import com.englivia.quiz.helper.AppController;
import com.englivia.quiz.helper.Session;
import com.englivia.quiz.helper.Utils;
import com.englivia.quiz.model.Category;
import com.englivia.quiz.model.Sets;
import com.englivia.quiz.model.SubCategory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.storage.network.ListNetworkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DicSets extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Sets> categoryList1=new ArrayList<>();
    Activity activity;
    CategoryAdapter1 adapter1;
    public TextView empty_msg;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_dic_sets);
        recyclerView=findViewById(R.id.recycle);
        empty_msg = findViewById(R.id.txtblanklist);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        activity=DicSets.this;
        getData();

    }
    private void getData() {
        //mShimmerViewContainer.startShimmerAnimation();
        //progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(activity)) {
            getMainCategoryFromJson1();
            invalidateOptionsMenu();

        } else {


        }


        Utils.showBannerAds(DicSets.this);

    }


    public void getMainCategoryFromJson1() {
        // progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            params.put("get_sets_by_language", "1");
            params.put(Constant.LANGUAGE_ID, Session.getCurrentLanguage(getApplicationContext()));
        } else
            params.put(Constant.getCategories, "1");

        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                Log.e("Response::=",  response);

                try {
                    categoryList1 = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Response::=",  response);
//                    String error = jsonObject.getString(Constant.ERROR);

                  //  if (error.equalsIgnoreCase("false")) {
                        empty_msg.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Sets category = new Sets();
                            JSONObject object = jsonArray.getJSONObject(i);
                            category.setId(object.getString(Constant.ID));
                            category.setLanguage_id(object.getString("language_id"));
                            category.setTitle(object.getString("title"));

                          //  category.setNoOfCate(object.getString(Constant.NO_OF_CATE));
                       /*     if(Constant.IN_APP_MODE.equals("1")) {
                                if (i != 0 && i % 5 == 0) {
                                    categoryList.add(new Category(true));
                                }
                            }*/
                            categoryList1.add(category);
                        }
                    Collections.sort(categoryList1, new Sets.Sortbyroll());
                    Collections.reverse(categoryList1);

                        adapter1 = new CategoryAdapter1(activity, categoryList1);
                        recyclerView.setAdapter(adapter1);

                        progressBar.setVisibility(View.GONE);

                   // } else {


                   // }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);


    }
    private class CategoryAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ArrayList<Sets> dataList;
        public Context mContext;
        final int MENU_ITEM_VIEW_TYPE = 0;
        final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

        public CategoryAdapter1(Context context, ArrayList<Sets> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                default:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category2, parent, false);
                    return new CategoryAdapter1.ItemRowHolder(v);
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
            DrawerActivity.Locallanguage(DicSets.this);
            int viewType = getItemViewType(position);
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                    final CategoryAdapter1.ItemRowHolder holder = (CategoryAdapter1.ItemRowHolder) holder1;
                    final Sets category = dataList.get(position);
                    ////    holder.nameLyt.setBackgroundResource(Constant.colorBg[position % 6]);
                    //  holder.imgCircles.setColorFilter(ContextCompat.getColor(activity, Constant.colors[position % 6]));
                    holder.text.setText(category.getTitle());
                    //    holder.noofque.setText(type.equals(Constant.LEARNINGZONE) ? getString(R.string.avail_cate) + category.getNoOfCate() : getString(R.string.que) + category.getTtlQues());



                    holder.relativeLayout.setOnClickListener(v -> {
                     //   getMainCategoryFromJson(category.getId());
                        Constant.CATE_ID = category.getId();
                        Intent intent = null;
                        intent = new Intent(activity, Dictionary.class);
                        intent.putExtra("id",category.getId());
                        startActivity(intent);


                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {

            return MENU_ITEM_VIEW_TYPE;
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public NetworkImageView image;
            public TextView text, noofque;
            RelativeLayout relativeLayout, nameLyt;
            ImageView imgCircles;

            public ItemRowHolder(View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.item_title);
                relativeLayout = itemView.findViewById(R.id.cat_layout);

            }
        }

    }

}