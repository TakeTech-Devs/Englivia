package com.englivia.quiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.englivia.quiz.Constant;
import com.englivia.quiz.R;
import com.englivia.quiz.helper.ApiConfig;
import com.englivia.quiz.helper.AppController;
import com.englivia.quiz.helper.Session;
import com.englivia.quiz.helper.Utils;
import com.englivia.quiz.model.Dict;
import com.englivia.quiz.model.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dictionary extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Dict> categoryList1=new ArrayList<>();
    Activity activity;
    CategoryAdapter1 adapter1;
    public TextView empty_msg;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_dictionary);
        recyclerView=findViewById(R.id.recycle);
        empty_msg = findViewById(R.id.txtblanklist);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activity=Dictionary.this;
        getData();
        Utils.displayInterstitial(this);
    }
    private void getData() {
        //mShimmerViewContainer.startShimmerAnimation();
        //progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(activity)) {
            getMainCategoryFromJson(getIntent().getStringExtra("id"));
            invalidateOptionsMenu();

        } else {


        }

        Utils.showBannerAds(Dictionary.this);

    }

    public void getMainCategoryFromJson(String a) {
        // progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            params.put("get_data_by_sets", "1");
            params.put("dictionary_id",a);// Session.getCurrentLanguage(getApplicationContext()));
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
                        Dict category = new Dict();
                        JSONObject object = jsonArray.getJSONObject(i);
                        category.setId(object.getString(Constant.ID));
                        category.setMeaning(object.getString("meaning"));
                        category.setWord(object.getString("word"));
                        category.setNo(i+1+".");
                                //  category.setNoOfCate(object.getString(Constant.NO_OF_CATE));

                        categoryList1.add(category);
                    }

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
        ArrayList<Dict> dataList;
        public Context mContext;
        final int MENU_ITEM_VIEW_TYPE = 0;
        final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

        public CategoryAdapter1(Context context, ArrayList<Dict> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                default:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category3, parent, false);
                    return new CategoryAdapter1.ItemRowHolder(v);
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
            DrawerActivity.Locallanguage(activity);
            int viewType = getItemViewType(position);
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                    final CategoryAdapter1.ItemRowHolder holder = (CategoryAdapter1.ItemRowHolder) holder1;
                    final Dict category = dataList.get(position);
                    ////    holder.nameLyt.setBackgroundResource(Constant.colorBg[position % 6]);
                    //  holder.imgCircles.setColorFilter(ContextCompat.getColor(activity, Constant.colors[position % 6]));
                    holder.text.setText(category.getWord());
                    holder.text1.setText(category.getMeaning());
                    holder.text2.setText(category.getNo());
                    //    holder.noofque.setText(type.equals(Constant.LEARNINGZONE) ? getString(R.string.avail_cate) + category.getNoOfCate() : getString(R.string.que) + category.getTtlQues());




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
            public TextView text,text1,text2, noofque;
            RelativeLayout relativeLayout, nameLyt;
            ImageView imgCircles;

            public ItemRowHolder(View itemView) {
                super(itemView);

                text = itemView.findViewById(R.id.item_title);
                text1 = itemView.findViewById(R.id.item_title1);
                text2 = itemView.findViewById(R.id.item_title2);

                relativeLayout = itemView.findViewById(R.id.cat_layout);

            }
        }

    }
}