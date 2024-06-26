package com.englivia.quiz.activity;

import static android.text.method.TimeKeyListener.CHARACTERS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.englivia.quiz.inappreview.InAppReviewHelper;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.englivia.quiz.R;
import com.englivia.quiz.battle.WaitingRoomActivity;
import com.englivia.quiz.helper.ApiConfig;
import com.englivia.quiz.helper.AppController;
import com.englivia.quiz.Constant;
import com.englivia.quiz.helper.Session;
import com.englivia.quiz.helper.Utils;
import com.facebook.login.LoginManager;


import com.google.firebase.auth.FirebaseAuth;
import com.englivia.quiz.model.Category;
import com.englivia.quiz.model.Language;
import com.englivia.quiz.model.Question;
import com.englivia.quiz.model.Item;
import com.englivia.quiz.model.Room;
import com.englivia.quiz.one_to_one.OneToOneWait;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends DrawerActivity {

    public String type;
    public RelativeLayout lytCategory;
    ImageView imgBack, imgStore;
    TextView imgLanguage;
    public String status = "0";
    public static TextView tvAlert, tvPlay, tvViewAll;
    public Toolbar toolbar;
    RelativeLayout lytContest, lytLearning;
    AlertDialog alertDialog;

    public RecyclerView recyclerView, PlayMode;
    public static TextView tvName, tvScore, tvCoin, tvRank;
    public static CategoryAdapter adapter;
    public ArrayList<Category> categoryList;
    public static ArrayList<Item> arrayList;
    public static String[] iconsName;
    AppCompatActivity activity;
    ProgressDialog mProgressDialog;
    String authId;
    public static CustomAdapter Customadapters;
    public static TextView txtBattleZone, txtPlayZpme, txtQuiZone, tv_play, tvOneToOne, tv_oto_play, txt_random, tv_random_play, txtContestZone, txtContest, tvContest;

    public RecyclerView recyclerView1;
    public ProgressBar progressBar;

    public TextView empty_msg;
    public RelativeLayout layout;
    public ArrayList<Category> categoryList1;
    public SwipeRefreshLayout swipeRefreshLayout;
    public Snackbar snackbar;
    public AlertDialog alertDialog1;
    CategoryAdapter1 adapter1;
    private ShimmerFrameLayout mShimmerViewContainer;

    TextView tvdict;
    String quizType;
    Activity activity1;
    ProgressDialog mProgressDialog1;
    NetworkImageView cateImg;

    String generatedEmail;


    @SuppressLint({"NewApi", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        activity = MainActivity.this;
        getAllWidgets();
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        ShowInitPopup();

        //for google playstore review
        //requestReviewInfo();
        InAppReviewHelper.askUserForReview(this);

      //  generatedEmail = randomEmailGenerator();

        Log.d("Miolon", "onCreate: " + generatedEmail);

        //authId = IdGenerator.g;

        authId = IdGenerator.generateRandomId();


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        type = getIntent().getStringExtra("type");

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
        };
        tvViewAll.setOnClickListener(view -> openCategoryPage(Constant.REGULAR));

      //  tvName.setText(Session.getUserData(Session.NAME, getApplicationContext()));
        imgProfile.setImageUrl(Session.getUserData(Session.PROFILE, getApplicationContext()), imageLoader);
        imgProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        tvRank.setOnClickListener(view -> {
            Intent leaderBoard = new Intent(activity, LeaderboardTabActivity.class);
            startActivity(leaderBoard);
        });

        imgStore.setVisibility(Session.getBoolean(Session.STORE, getApplicationContext()) ? View.VISIBLE : View.GONE);
        imgStore.setOnClickListener(view -> {
            Intent instruction = new Intent(getApplicationContext(), CoinStoreActivity.class);
            startActivity(instruction);
        });
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {

            LanguageDialog(activity);

            if (Session.getFirstNCount(getApplicationContext()) == 0) {
                if (alertDialog != null)
                    alertDialog.show();
            } else {
                getMainCategoryFromJson();
                getMainCategoryFromJson1();
            }
            imgLanguage.setVisibility(View.VISIBLE);
        } else {
            getMainCategoryFromJson();
            getMainCategoryFromJson1();
            imgLanguage.setVisibility(View.GONE);
        }

        imgLanguage.setOnClickListener(view -> {
            if (alertDialog != null)
                alertDialog.show();
        });
        imgBack.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));


        if (type != null && !type.equals("null")) {
            if (type.equals("category")) {
                Constant.TotalLevel = Integer.parseInt(getIntent().getStringExtra("maxLevel"));
                Constant.CATE_ID = getIntent().getStringExtra("cateId");
                if (getIntent().getStringExtra("no_of").equals("0")) {
                    Intent intent = new Intent(activity, LevelActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, SubcategoryActivity.class);
                    startActivity(intent);
                }
            }
        }

        navigationView.getMenu().getItem(3).setActionView(R.layout.cart_count_layout);
        NavigationCartCount();

        if (Utils.isNetworkAvailable(activity)) {
            GetUserStatus();
            GetMark();
            GetUpdate(activity);

        }

        DeleteOldGameRoom();


        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        empty_msg = findViewById(R.id.txtblanklist);
        progressBar = findViewById(R.id.progressBar);
        tvdict=findViewById(R.id.dict);
        cateImg=findViewById(R.id.cateImg);
        cateImg.setVisibility(View.VISIBLE);
        cateImg.setDefaultImageResId(R.drawable.earth);

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        recyclerView1 = findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        getData();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });


        tvdict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,DicSets.class);
                startActivity(i);



            }
        });

        Utils.loadAd(this);

    }




    private int generateRandomNumber() {
        Random random = new Random();
        // Adjust the range of the random number as needed
        int min = 1000;
        int max = 9999;
        return random.nextInt(max - min + 1) + min;
    }

    private void getData() {
        //mShimmerViewContainer.startShimmerAnimation();
        //progressBar.setVisibility(View.VISIBLE);
        if (Utils.isNetworkAvailable(activity)) {
            getMainCategoryFromJson();getMainCategoryFromJson1();
            invalidateOptionsMenu();

        } else {
            setSnackBar();
            mShimmerViewContainer.stopShimmer();
            mShimmerViewContainer.setVisibility(View.GONE);
        }

    }
    public void setSnackBar() {
        snackbar = Snackbar
                .make(findViewById(android.R.id.content), getString(R.string.msg_no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), view -> getData());

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
    public void getMainCategoryFromJson() {
        // progressBar.setVisibility(View.VISIBLE);
        type = "learningzone";//getIntent().getStringExtra("type");
        Map<String, String> params = new HashMap<>();
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            params.put(Constant.GET_CATE_BY_LANG, "1");
            params.put(Constant.LANGUAGE_ID, Session.getCurrentLanguage(getApplicationContext()));
        } else
            params.put(Constant.getCategories, "1");
        if (type.equals(Constant.LEARNINGZONE)) {
            params.put(Constant.type, "2");
        } else {
            params.put(Constant.type, "1");
        }
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    categoryList1 = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println("Response::=" + response);
                    String error = jsonObject.getString(Constant.ERROR);

                    if (error.equalsIgnoreCase("false")) {
                        empty_msg.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category category = new Category();
                            JSONObject object = jsonArray.getJSONObject(i);
                            category.setId(object.getString(Constant.ID));
                            category.setName(object.getString(Constant.CATEGORY_NAME));
                            category.setImage(object.getString(Constant.IMAGE));
                            if (!type.equals(Constant.LEARNINGZONE)) {
                                category.setMaxLevel(object.getString(Constant.MAX_LEVEL));
                                category.setTtlQues(object.getString(Constant.NO_OF_QUES));
                            }
                            category.setNoOfCate(object.getString(Constant.NO_OF_CATE));
                       /*     if(Constant.IN_APP_MODE.equals("1")) {
                                if (i != 0 && i % 5 == 0) {
                                    categoryList.add(new Category(true));
                                }
                            }*/
                            categoryList1.add(category);
                        }

                        adapter1 = new CategoryAdapter1(activity, categoryList1);
                        recyclerView1.setAdapter(adapter1);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } else {

                        empty_msg.setText(getString(R.string.no_category));
                        progressBar.setVisibility(View.GONE);
                        empty_msg.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);

                        if (adapter1 != null) {
                            adapter1 = new CategoryAdapter1(activity, categoryList1);
                            recyclerView1.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);


    }


    private void setAnimation(View viewToAnimate, int position) {
        Context context = activity;
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setDuration(position * 50L + 500);
        viewToAnimate.startAnimation(animation);

    }
    public void getAllWidgets() {
        toolbar = findViewById(R.id.toolBar);
        tvViewAll = findViewById(R.id.tv_view_all);
      //  tvName = findViewById(R.id.tvName);
        imgProfile = findViewById(R.id.imgProfile);
        lytCategory = findViewById(R.id.lytCategory);
        tvScore = findViewById(R.id.tv_score);
        tvCoin = findViewById(R.id.tv_coin);
        tvRank = findViewById(R.id.tv_rank);
        imgBack = findViewById(R.id.img_back_main);
        imgLanguage = findViewById(R.id.imgLanguage);
        imgLanguage.setText("Change"+"\n"+"Language");
        imgStore = findViewById(R.id.imgStore);
        lytContest = findViewById(R.id.lytContest);
        tvAlert = findViewById(R.id.tvAlert);
        tvPlay = findViewById(R.id.tvPlay);
        txtBattleZone = findViewById(R.id.txtBattleZone);
        txtPlayZpme = findViewById(R.id.txtPlayZone);
        txtQuiZone = findViewById(R.id.txtQuiZone);
//        item_title = findViewById(R.id.item_title);
        tv_play = findViewById(R.id.tv_play);
        tvOneToOne = findViewById(R.id.tvOneToOne);
        tv_oto_play = findViewById(R.id.tv_oto_play);
        txt_random = findViewById(R.id.txt_random);
        tv_random_play = findViewById(R.id.tv_random_play);
        txtContestZone = findViewById(R.id.txtContestZone);
        txtContest = findViewById(R.id.txtContest);
        tvContest = findViewById(R.id.tvContest);
        lytLearning = findViewById(R.id.lytLearning);



        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        PlayMode = findViewById(R.id.PlayMode);
        PlayMode.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2, RecyclerView.HORIZONTAL, false));
    }

    public void setDefaultQuiz() {
        iconsName = new String[]{getString(R.string.daily_quiz), getString(R.string.random_quiz), getString(R.string.true_false), getString(R.string.self_challenge), getString(R.string.practice)};
        arrayList = new ArrayList<>();
        for (String s : iconsName) {
            Item itemModel = new Item();
            itemModel.setName(s);
            if (s.equals(getString(R.string.daily_quiz))) {
                if (Session.getBoolean(Session.GETDAILY, activity)) {
                    arrayList.add(itemModel);
                }
            } else {
                arrayList.add(itemModel);
            }


        }
        Customadapters = new CustomAdapter(getApplicationContext(), arrayList);
        PlayMode.setAdapter(Customadapters);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        txtBattleZone.setText(R.string.battle_zone);
        txtPlayZpme.setText(R.string.play_zone);
        tvViewAll.setText(R.string.view_all);
        txtQuiZone.setText(R.string.quiz_zone);
       // item_title.setText(R.string.group_battle);
        tv_play.setText(R.string.play_now);
        tvOneToOne.setText(R.string.one_to_one_battle);
        tv_oto_play.setText(R.string.play_now);
        txt_random.setText(R.string.random_battle);
        tv_random_play.setText(R.string.play_now);
        txtContestZone.setText(R.string.contest_zone);
        txtContest.setText(R.string.contest_play);
        tvContest.setText(R.string.join_now);
        super.onConfigurationChanged(newConfig);
    }


    public void ContestQuiz(View view) {
        Intent intent = new Intent(activity, ContestActivity.class);
        startActivity(intent);
    }

    public void LearningZone(View view) {
        openCategoryPage(Constant.LEARNINGZONE);
    }

    public void RandomBattle(View view) {
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            if (Session.getCurrentLanguage(getApplicationContext()).equals(Constant.D_LANG_ID)) {
                if (alertDialog != null)
                    alertDialog.show();
            } else {

                searchPlayerCall();
            }
        } else {
            searchPlayerCall();
        }

    }

    public void searchPlayerCall() {
        if (Constant.isCateEnable)
            openCategoryPage(Constant.BATTLE);
        else
            startActivity(new Intent(activity, SearchPlayerActivity.class));
    }


    public void GroupBattle(View view) {
        JoinCreateDialog(Constant.MULTIPLAYER_ROOM);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void OneToOneBattle(View view) {
        JoinCreateDialog(Constant.ONE_TO_ONE);
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.viewHolder> {

        Context context;
        ArrayList<Item> arrayList;

        public CustomAdapter(Context context, ArrayList<Item> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.layout_playquiz, viewGroup, false);
            return new viewHolder(view);
        }

        @Override
        public void onBindViewHolder(viewHolder viewHolder, int position) {
            Item item = arrayList.get(position);
            viewHolder.iconName.setText(item.getName());
            viewHolder.noofcate.setText(getString(R.string.play_now));
            viewHolder.cardTitle.setBackgroundResource(Constant.gradientBG[position % 6]);
            viewHolder.cardTitle.setOnClickListener(v -> {
                if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
                    if (Session.getCurrentLanguage(getApplicationContext()).equals(Constant.D_LANG_ID)) {
                        if (alertDialog != null)
                            alertDialog.show();
                    } else {
                        setQuiz(item);
                    }
                    System.out.println("===== avail");
                } else {
                    setQuiz(item);
                    System.out.println("===== avail not");
                }
            });
        }


        public void setQuiz(Item item) {
            if (item.getName().equalsIgnoreCase(getString(R.string.daily_quiz))) {
                DailyRandomQuiz("daily");
            } else if (item.getName().equalsIgnoreCase(getString(R.string.random_quiz))) {
                DailyRandomQuiz("random");
            } else if (item.getName().equalsIgnoreCase(getString(R.string.self_challenge))) {
                startActivity(new Intent(activity, NewSelfChallengeActivity.class));
            } else if (item.getName().equalsIgnoreCase(getString(R.string.true_false))) {
                DailyRandomQuiz("true_false");
            } else if (item.getName().equalsIgnoreCase(getString(R.string.practice))) {
                openCategoryPage(Constant.PRACTICE);
            }
        }


        public void DailyRandomQuiz(String quizType) {
            Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
            intent.putExtra("fromQue", quizType);
            startActivity(intent);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder {

            TextView iconName, noofcate;
            RelativeLayout cardTitle;

            public viewHolder(View itemView) {
                super(itemView);

                iconName = itemView.findViewById(R.id.item_title);
                cardTitle = itemView.findViewById(R.id.cardTitle);
                noofcate = itemView.findViewById(R.id.noofcate);
            }
        }

    }
    public void getMainCategoryFromJson1() {
        // progressBar.setVisibility(View.VISIBLE);

        lytCategory.setVisibility(View.GONE);
        Map<String, String> params = new HashMap<>();
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            params.put(Constant.GET_CATE_BY_LANG, "1");
            params.put(Constant.LANGUAGE_ID, Session.getCurrentLanguage(getApplicationContext()));
        } else
            params.put(Constant.getCategories, "1");
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    categoryList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString(Constant.ERROR);
                    //   System.out.println("=====cate res " + response);
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Category category = new Category();
                            JSONObject object = jsonArray.getJSONObject(i);
                            category.setId(object.getString(Constant.ID));
                            category.setName(object.getString(Constant.CATEGORY_NAME));
                            category.setImage(object.getString(Constant.IMAGE));
                            category.setMaxLevel(object.getString(Constant.MAX_LEVEL));
                            category.setTtlQues(object.getString(Constant.NO_OF_QUES));
                            category.setNoOfCate(object.getString(Constant.NO_OF_CATE));
                            categoryList.add(category);
                        }
                        adapter = new CategoryAdapter(activity, categoryList);
                        recyclerView.setAdapter(adapter);
                        lytCategory.setVisibility(View.VISIBLE);


                    } else {
                        lytCategory.setVisibility(View.GONE);
                        if (adapter != null) {
                            adapter = new CategoryAdapter(activity, categoryList);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);


    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemRowHolder> {

        private final ArrayList<Category> dataList;
        public Context mContext;

        public CategoryAdapter(Context context, ArrayList<Category> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @NonNull
        @Override
        public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category1, parent, false);
            return new ItemRowHolder(v);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ItemRowHolder holder, final int position) {

            final Category category = dataList.get(position);


            holder.text.setText(category.getName());
            holder.image.setImageUrl(category.getImage(), imageLoader);


         //   holder.tvNoOfQue.setText(getString(R.string.que) + category.getTtlQues());
          //  holder.noofcat.setText(getString(R.string.category) + category.getNoOfCate());
            holder.relativeLayout
                    .setOnClickListener(v -> {
                        Constant.CATE_ID = category.getId();
                        Constant.cate_name = category.getName();
                        if (!category.getTtlQues().equals("0")) {
                            if (!category.getNoOfCate().equals("0")) {
                                Intent intent = new Intent(activity, SubcategoryActivity.class);
                                startActivity(intent);
                            } else {
                                if (category.getMaxLevel() == null) {
                                    Constant.TotalLevel = 0;
                                } else if (category.getMaxLevel().equals("null")) {
                                    Constant.TotalLevel = 0;
                                } else {
                                    Constant.TotalLevel = Integer.parseInt(category.getMaxLevel());
                                }
                                Intent intent = new Intent(activity, LevelActivity.class);
                                intent.putExtra("fromQue", "cate");
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(activity, getString(R.string.question_not_available), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public NetworkImageView image;
            public TextView text, tvNoOfQue, noofcat;
            RelativeLayout relativeLayout, cardTitle;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.cateImg);
                text = itemView.findViewById(R.id.item_title);
                relativeLayout = itemView.findViewById(R.id.cat_layout);
              //  tvNoOfQue = itemView.findViewById(R.id.noofque);
               // noofcat = itemView.findViewById(R.id.noofcate);
                cardTitle = itemView.findViewById(R.id.cardTitle);
            }
        }
    }


    public void GetMark() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GetBookmark, "1");
        params.put(Constant.userId, Session.getUserData(Session.USER_ID, activity));
        ApiConfig.RequestToVolley((result, response) -> {
            System.out.println("========search result " + response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean(Constant.ERROR);
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Question question = new Question();
                            JSONObject object = jsonArray.getJSONObject(i);
                            question.setId(Integer.parseInt(object.getString(Constant.ID)));
                            Session.setMark(getApplicationContext(), "question_" + question.getId(), true);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public void GetUpdate(final Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_SYSTEM_CONFIG, "1");
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject obj = new JSONObject(response);

                    System.out.println("Learning::=" + response);
                    boolean error = obj.getBoolean("error");
                    if (!error) {
                        JSONObject jsonobj = obj.getJSONObject("data");
                        Constant.APP_LINK = jsonobj.getString(Constant.KEY_APP_LINK);
                        Constant.MORE_APP_URL = jsonobj.getString(Constant.KEY_MORE_APP);
                        Constant.VERSION_CODE = jsonobj.getString(Constant.KEY_APP_VERSION);
                        Constant.REQUIRED_VERSION = jsonobj.getString(Constant.KEY_APP_VERSION);
                        Constant.DAILY_QUIZ_ON = jsonobj.getString(Constant.DailyQuizText);
                        Constant.CONTEST_ON = jsonobj.getString(Constant.ContestText);
                        Constant.FORCE_UPDATE = jsonobj.getString(Constant.ForceUpdateText);

                        if (jsonobj.has(Constant.LearningZoneMode)){

                            Constant.LEARNINGZONEMODE = jsonobj.getString(Constant.LearningZoneMode);
                            if (Constant.LEARNINGZONEMODE.equals("1")) {
                                lytLearning.setVisibility(View.VISIBLE);
                            } else {
                                lytLearning.setVisibility(View.GONE);
                            }
                        } else Constant.LEARNINGZONEMODE = "0";

                        if (jsonobj.has(Constant.RANDOM_BATTLE_CATE_MODE))
                            Constant.isCateEnable = jsonobj.getString(Constant.RANDOM_BATTLE_CATE_MODE).equals("1");
                        if (jsonobj.has(Constant.GROUP_BATTLE_CATE_MODE))
                            Constant.isGroupCateEnable = jsonobj.getString(Constant.GROUP_BATTLE_CATE_MODE).equals("1");
                        Session.setBoolean(Session.GETDAILY, Constant.DAILY_QUIZ_ON.equals("1"), activity);
                        Session.setBoolean(Session.GETCONTEST, Constant.CONTEST_ON.equals("1"), activity);
                        lytContest.setVisibility(Session.getBoolean(Session.GETCONTEST, activity) ? View.VISIBLE : View.GONE);

                        setDefaultQuiz();
                        String versionName = "";
                        try {
                            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                            versionName = packageInfo.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }

                        if (Constant.FORCE_UPDATE.equals("1")) {
                            if (compareVersion(versionName, Constant.VERSION_CODE) < 0) {
                                OpenBottomDialog(activity);
                            } else if (compareVersion(versionName, Constant.REQUIRED_VERSION) < 0) {
                                OpenBottomDialog(activity);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public static void OpenBottomDialog(final Activity activity) {
        View sheetView = activity.getLayoutInflater().inflate(R.layout.lyt_terms_privacy, null);
        ViewGroup parentViewGroup = (ViewGroup) sheetView.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeAllViews();
        }

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        ImageView imgclose = sheetView.findViewById(R.id.imgclose);
        Button btnNotNow = sheetView.findViewById(R.id.btnNotNow);
        Button btnUpadateNow = sheetView.findViewById(R.id.btnUpdateNow);

        mBottomSheetDialog.setCancelable(false);


        imgclose.setOnClickListener(v -> {
            if (mBottomSheetDialog.isShowing())
                mBottomSheetDialog.dismiss();
        });
        btnNotNow.setOnClickListener(v -> {
            if (mBottomSheetDialog.isShowing())
                mBottomSheetDialog.dismiss();
        });

        btnUpadateNow.setOnClickListener(view -> {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.APP_LINK)));
            System.out.println("Packge Name::=" + Constant.APP_LINK + activity.getPackageName());

        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public static int compareVersion(String version1, String version2) {
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");

        int i = 0;
        while (i < arr1.length || i < arr2.length) {
            if (i < arr1.length && i < arr2.length) {
                if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
                    return -1;
                } else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
                    return 1;
                }
            } else if (i < arr1.length) {
                if (Integer.parseInt(arr1[i]) != 0) {
                    return 1;
                }
            } else {
                if (Integer.parseInt(arr2[i]) != 0) {
                    return -1;
                }
            }

            i++;
        }

        return 0;
    }


    public void LanguageDialog(Activity activity) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater1 = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater1.inflate(R.layout.language_dialog, null);
        dialog.setView(dialogView);
        RecyclerView languageView = dialogView.findViewById(R.id.recyclerView);
        languageView.setLayoutManager(new LinearLayoutManager(activity));
        alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        GetLanguage(languageView, activity, alertDialog);
    }

    public void GetUserStatus() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_USER_BY_ID, "1");
        params.put(Constant.ID, Session.getUserData(Session.USER_ID, getApplicationContext()));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {

                    JSONObject obj = new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    if (!error) {
                        JSONObject jsonobj = obj.getJSONObject("data");
                        if (jsonobj.getString(Constant.status).equals(Constant.DE_ACTIVE)) {
                            Session.clearUserSession(getApplicationContext());
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            Intent intentLogin = new Intent(activity, LoginTabActivity.class);
                            startActivity(intentLogin);
                            finish();
                        } else {
                            Constant.TOTAL_COINS = Integer.parseInt(jsonobj.getString(Constant.COINS));
                            String numberString;
                            if (Math.abs(Integer.parseInt(String.valueOf(Constant.TOTAL_COINS)) / 1000000) > 1) {
                                numberString = (Integer.parseInt(String.valueOf(Constant.TOTAL_COINS)) / 1000000) + "M";

                            } else if (Math.abs(Integer.parseInt(String.valueOf(Constant.TOTAL_COINS)) / 1000) > 1) {
                                numberString = (Integer.parseInt(String.valueOf(Constant.TOTAL_COINS)) / 1000) + "K";

                            } else {
                                numberString = String.valueOf(Constant.TOTAL_COINS);

                            }
                            tvCoin.setText(numberString);
                            tvRank.setText(jsonobj.getString(Constant.GLOBAL_RANK));
                            tvScore.setText(jsonobj.getString(Constant.GLOBAL_SCORE));
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                                String token = task.getResult();
                                if (!token.equals(Session.getUserData(Session.FCM, getApplicationContext()))) {
                                    Utils.postTokenToServer(getApplicationContext(), token);
                                }
                            });
                            // Utils.RemoveGameRoomId(FirebaseAuth.getInstance().getUid());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public void LeaderBoard(View view) {
        Utils.btnClick(view, activity);
        startActivity(new Intent(activity, LeaderboardTabActivity.class));
    }

    public void Logout(View view) {
        Utils.btnClick(view, activity);
        Utils.SignOutWarningDialog(activity);
    }

    public void createGameRoom(Activity activity, String type) {
        String gameCode = Constant.randomAlphaNumeric(5);
        showProgressDialog();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String authId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference dbRef = firebaseDatabase.getReference(type).child(gameCode);
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.isRoomActive, Constant.TRUE);
        map.put(Constant.authId, authId);
        map.put(Constant.isStarted, Constant.FALSE);
        map.put(Constant.IS_JOINED, Constant.FALSE);


        dbRef.setValue(map).addOnSuccessListener(aVoid -> {
            //Do what you need to do
            Toast.makeText(activity, getString(R.string.room_created_msg), Toast.LENGTH_SHORT).show();

            //join user detail
            HashMap<String, String> joinMap = new HashMap<>();
            joinMap.put(Constant.UID, authId);
            joinMap.put(Constant.IMAGE, Session.getUserData(Session.PROFILE, activity));
            joinMap.put(Constant.NAME, Session.getUserData(Session.NAME, activity));
            joinMap.put(Constant.IS_JOINED, Constant.TRUE);
            joinMap.put(Constant.USER_ID, Session.getUserData(Session.USER_ID, activity));
            dbRef.child(Constant.joinUser).child(authId).setValue(joinMap);
            sendRoomDataOnServer(gameCode, type);
        });
    }

    public void sendRoomDataOnServer(final String roomId, String type) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.CREATE_ROOM, "1");
        params.put(Constant.userId, Session.getUserData(Session.USER_ID, activity));
        params.put(Constant.ROOM_ID, roomId);
        params.put(Constant.roomType, "private");
        params.put(Constant.NO_OF_QUES, "10");
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext()))
            params.put(Constant.LANGUAGE_ID, Session.getUserData(Session.LANGUAGE, activity));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    hideProgressDialog();

                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean(Constant.ERROR);
                    if (!error) {
                        Intent intent;
                        if (type.equalsIgnoreCase(Constant.MULTIPLAYER_ROOM))
                            intent = new Intent(activity, WaitingRoomActivity.class);
                        else
                            intent = new Intent(activity, OneToOneWait.class);
                        intent.putExtra("from", "private");
                        intent.putExtra("roomKey", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        intent.putExtra("roomId", roomId);
                        intent.putExtra("type", "regular");
                        startActivity(intent);
                    } else
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public void NavigationCartCount() {

        View viewCount = navigationView.getMenu().getItem(3).setActionView(R.layout.cart_count_layout).getActionView();
        TextView tvCount = viewCount.findViewById(R.id.tvCount);
        if (Session.getNCount(getApplicationContext()) == 0) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
        }
        tvCount.setText(String.valueOf(Session.getNCount(getApplicationContext())));
    }

    public void JoinCreateDialog(String type) {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_join_create_battle, null);
        dialog.setView(dialogView);

        TextView tvTitle = dialogView.findViewById(R.id.tvTitle);
        TextView tvCreate = dialogView.findViewById(R.id.tvCreate);
        TextView tvJoin = dialogView.findViewById(R.id.tvJoin);
        if (type.equalsIgnoreCase(Constant.MULTIPLAYER_ROOM))
            tvTitle.setText(activity.getResources().getString(R.string.group_battle));
        else
            tvTitle.setText(activity.getResources().getString(R.string.one_to_one_battle));
        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

        tvCreate.setOnClickListener(view -> {
            alertDialog.dismiss();

            if (type.equals(Constant.ONE_TO_ONE)) {
                createGameRoom(activity, type);
            } else {
                if (Constant.isGroupCateEnable) {
                    openCategoryPage(Constant.MULTIPLAYER_ROOM);
                } else
                    createGameRoom(activity, type);
            }

        });
        tvJoin.setOnClickListener(view -> {
            alertDialog.dismiss();
            JoinRoom(type);
        });

    }

    public void GetLanguage(final RecyclerView languageView, final Context context, final AlertDialog alertDialog) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_LANGUAGES, "1");
        ApiConfig.RequestToVolley((result, response) -> {

            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean(Constant.ERROR);
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        ArrayList<Language> languageList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Language language = new Language();
                            JSONObject object = jsonArray.getJSONObject(i);
                            language.setId(object.getString(Constant.ID));
                            language.setLanguage(object.getString(Constant.LANGUAGE));
                            languageList.add(language);
                        }
                        Log.e("listSize", String.valueOf(languageList.size()));
                        if (languageList.size() == 1) {
                            Session.setCurrentLanguage(languageList.get(0).getId(), context);
                            Session.setBoolean(Session.IS_FIRST_TIME, true, context);
                        }

                        LanguageAdapter languageAdapter = new LanguageAdapter(context, languageList, alertDialog);
                        languageView.setAdapter(languageAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    public class LanguageAdapter extends RecyclerView.Adapter<MainActivity.LanguageAdapter.ItemRowHolder> {
        private final ArrayList<Language> dataList;
        private final Context mContext;
        AlertDialog alertDialog;

        public LanguageAdapter(Context context, ArrayList<Language> dataList, AlertDialog alertDialog) {
            this.dataList = dataList;
            this.mContext = context;
            this.alertDialog = alertDialog;
        }

        @NonNull
        @Override
        public MainActivity.LanguageAdapter.ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_layout, parent, false);
            return new MainActivity.LanguageAdapter.ItemRowHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.LanguageAdapter.ItemRowHolder holder, final int position) {

            final Language language = dataList.get(position);
            final MainActivity.LanguageAdapter.ItemRowHolder itemRowHolder = holder;
            itemRowHolder.tvLanguage.setText(language.getLanguage());
            if (Session.getCurrentLanguage(mContext).equals(language.getId())) {
                itemRowHolder.radio.setImageResource(R.drawable.ic_radio_check);
            } else {
                itemRowHolder.radio.setImageResource(R.drawable.ic_radio_unchecked);
            }
            itemRowHolder.radio.setOnClickListener(view -> {
                itemRowHolder.radio.setImageResource(R.drawable.ic_radio_check);
                Session.setCurrentLanguage(language.getId(), mContext);
                Session.setBoolean(Session.IS_FIRST_TIME, true, mContext);
                Session.setInt((Session.IS_FIRST_COUNT), (Session.getFirstNCount(getApplicationContext())+1), getApplicationContext());
                notifyDataSetChanged();
                getMainCategoryFromJson();
                getMainCategoryFromJson1();
                alertDialog.dismiss();

            });

        }

        @Override
        public int getItemCount() {
            return (dataList.size());
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public ImageView radio;
            public TextView tvLanguage;


            public ItemRowHolder(View itemView) {
                super(itemView);
                radio = itemView.findViewById(R.id.radio);
                tvLanguage = itemView.findViewById(R.id.tvLanguage);
            }

        }

    }

    private class CategoryAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ArrayList<Category> dataList;
        public Context mContext;
        final int MENU_ITEM_VIEW_TYPE = 0;
        final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

        public CategoryAdapter1(Context context, ArrayList<Category> dataList) {
            this.dataList = dataList;
            this.mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                default:
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category1, parent, false);
                    return new CategoryAdapter1.ItemRowHolder(v);
            }

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, final int position) {
            DrawerActivity.Locallanguage(MainActivity.this);
            int viewType = getItemViewType(position);
            switch (viewType) {

                case MENU_ITEM_VIEW_TYPE:
                    final CategoryAdapter1.ItemRowHolder holder = (CategoryAdapter1.ItemRowHolder) holder1;
                    final Category category = dataList.get(position);
                ////    holder.nameLyt.setBackgroundResource(Constant.colorBg[position % 6]);
                  //  holder.imgCircles.setColorFilter(ContextCompat.getColor(activity, Constant.colors[position % 6]));
                    holder.text.setText(category.getName());
                //    holder.noofque.setText(type.equals(Constant.LEARNINGZONE) ? getString(R.string.avail_cate) + category.getNoOfCate() : getString(R.string.que) + category.getTtlQues());
                    holder.image.setDefaultImageResId(R.drawable.ic_launcher);
                    holder.image.setImageUrl(category.getImage(), imageLoader);
                    setAnimation(holder.text, position);
                    holder.relativeLayout.setOnClickListener(v -> {
                        Constant.CATE_ID = category.getId();
                        Constant.cate_name = category.getName();
                        if (!type.equals(Constant.LEARNINGZONE)) {
                            if (!category.getTtlQues().equals("0")) {
                                if (type.equals(Constant.REGULAR)) {
                                    if (!category.getNoOfCate().equals("0")) {
                                        Intent intent = null;
                                        intent = new Intent(activity, SubCatt.class);
                                        startActivity(intent);
                                    } else {
                                        if (category.getMaxLevel() == null) {
                                            Constant.TotalLevel = 0;
                                        } else if (category.getMaxLevel().equals("null")) {
                                            Constant.TotalLevel = 0;
                                        } else {
                                            Constant.TotalLevel = Integer.parseInt(category.getMaxLevel());
                                        }
                                        Intent intent = new Intent(activity, SubCatt.class);
                                        intent.putExtra("fromQue", "cate");
                                        startActivity(intent);
                                    }
                                } else {
                                    Intent intent;
                                    if (type.equals(Constant.PRACTICE)) {
                                        intent = new Intent(activity, PracticeQuiz.class);
                                        startActivity(intent);
                                    } else if (type.equals(Constant.MULTIPLAYER_ROOM)) {
                                        createGameRoom(activity);

                                    } else if (type.equals(Constant.BATTLE)) {
                                        intent = new Intent(activity, SearchPlayerActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Toast.makeText(activity, getString(R.string.question_not_available), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(activity, SubCatt.class);
                            intent.putExtra("id", category.getId());
                            intent.putExtra("title", category.getName());
                            intent.putExtra("message", category.getMessage());
                            startActivity(intent);
                        }

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
            if (categoryList1.get(position).isAdsShow()) {
                return UNIFIED_NATIVE_AD_VIEW_TYPE;
            }
            return MENU_ITEM_VIEW_TYPE;
        }

        public class ItemRowHolder extends RecyclerView.ViewHolder {
            public NetworkImageView image;
            public TextView text, noofque;
            RelativeLayout relativeLayout, nameLyt;
            ImageView imgCircles;

            public ItemRowHolder(View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.cateImg);
                imgCircles = itemView.findViewById(R.id.imgCircles);
                text = itemView.findViewById(R.id.item_title);
                relativeLayout = itemView.findViewById(R.id.cat_layout);
                nameLyt = itemView.findViewById(R.id.nameLyt);
                noofque = itemView.findViewById(R.id.noofque);
            }
        }

    }

    public void JoinRoom(String type) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetTheme);
        View bottomView = getLayoutInflater().inflate(R.layout.join_room_dialog, null);
        bottomSheetDialog.setContentView(bottomView);
        ImageView imgClose = bottomView.findViewById(R.id.imgClose);
        TextView tvJoinRoom = bottomView.findViewById(R.id.tvJoinRoom);
        TextView tvAlert = bottomView.findViewById(R.id.tvAlert);
        EditText edtGameCode = bottomView.findViewById(R.id.edtGameCode);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        tvJoinRoom.setOnClickListener(v -> {
            String code = edtGameCode.getText().toString().trim();
            if (code.isEmpty()) {
                tvAlert.setText(getString(R.string.enter_code));
            } else if (code.length() != 5) {
                Toast.makeText(activity, getString(R.string.game_code_alert), Toast.LENGTH_SHORT).show();
            } else {
                DatabaseReference dbRef;
                dbRef = rootRef.child(type).child(code);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // run some code
                            Room room = snapshot.getValue(Room.class);
                            assert room != null;
                            if (type.equalsIgnoreCase(Constant.MULTIPLAYER_ROOM)) {
                                joinGroupBattle(room, dbRef, code);
                            } else {
                                joinOneToOne(room, dbRef, code);
                            }
                            bottomSheetDialog.cancel();

                        } else {
                            Toast.makeText(activity, getString(R.string.gameroom_code_alert), Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        imgClose.setOnClickListener(view1 -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        assert bottomSheet != null;
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void joinGroupBattle(Room room, DatabaseReference dbRef, String code) {
        if (room.getIsRoomActive().equals(Constant.TRUE) && room.getIsStarted().equals(Constant.FALSE)) {
            dbRef.child(Constant.joinUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // get total available quest
                    int size = (int) dataSnapshot.getChildrenCount();

                    if (size < Constant.JoinMember) {
                        Intent intent;
                        intent = new Intent(activity, WaitingRoomActivity.class);
                        intent.putExtra("from", "private");
                        intent.putExtra("roomKey", room.getAuthId());
                        intent.putExtra("roomId", code);
                        intent.putExtra("type", "invite");
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity, getString(R.string.gameroom_full), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            Toast.makeText(activity, getString(R.string.game_deactive_alert), Toast.LENGTH_SHORT).show();
        }
    }

    public void joinOneToOne(Room room, DatabaseReference dbRef, String code) {
        if (room.getIsRoomActive().equals(Constant.TRUE) && room.getIsStarted().equals(Constant.FALSE)) {
            dbRef.child(Constant.joinUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // get total available quest
                    int size = (int) dataSnapshot.getChildrenCount();
                    if (size == 1) {
                        Intent intent;
                        intent = new Intent(activity, OneToOneWait.class);
                        intent.putExtra("from", "private");
                        intent.putExtra("roomKey", room.getAuthId());
                        intent.putExtra("roomId", code);
                        intent.putExtra("type", "invite");
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity, getString(R.string.gameroom_full), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            Toast.makeText(activity, getString(R.string.game_deactive_alert), Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteOldGameRoom() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference dbRef = rootRef.child(Constant.MULTIPLAYER_ROOM);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get total available quest
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.exists()) {
                        if (ds.getKey() != null)
                            if (dataSnapshot.child(ds.getKey()).child(Constant.authId).exists()) {
                                String ownerRoomIds = dataSnapshot.child(ds.getKey()).child(Constant.authId).getValue().toString();
                                if (ownerRoomIds.equalsIgnoreCase(authId)) {
                                    dbRef.child(ds.getKey()).removeValue();
                                }
                            }/*else{
                                dbRef.child(ds.getKey()).removeValue();
                            }*/
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference dbRefs = rootRef.child(Constant.ONE_TO_ONE);
        dbRefs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get total available quest
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.exists()) {
                        if (ds.getKey() != null)
                            if (dataSnapshot.child(ds.getKey()).child(Constant.authId).exists()) {
                                String ownerRoomIds = dataSnapshot.child(ds.getKey()).child(Constant.authId).getValue().toString();
                                if (ownerRoomIds.equalsIgnoreCase(authId)) {
                                    dbRefs.child(ds.getKey()).removeValue();
                                }
                            }/*else{
                                dbRef.child(ds.getKey()).removeValue();
                            }*/
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void openCategoryPage(String type) {
        startActivity(new Intent(activity, CategoryActivity.class)
                .putExtra(Constant.QUIZ_TYPE, type));
    }

    @Override
    protected void onPause() {
        AppController.StopSound();
        super.onPause();
        AppController.StopSound();
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.playSound();
        DeleteOldGameRoom();
        onConfigurationChanged(SettingActivity.config);
        if (Utils.isNetworkAvailable(activity)) {
            Utils.GetSystemConfig(getApplicationContext());
            GetUserStatus();
            invalidateOptionsMenu();
            if (Session.isLogin(activity)) {
                NavigationCartCount();
            }
        }
        mShimmerViewContainer.startShimmer();
        Utils.CheckBgMusic(activity);
    }
    public void createGameRoom(Activity activity) {

        String gameCode = Constant.randomAlphaNumeric(5);
        showProgressDialog();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String authId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference dbRef = firebaseDatabase.getReference(Constant.MULTIPLAYER_ROOM).child(gameCode);
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.isRoomActive, Constant.TRUE);
        map.put(Constant.authId, authId);
        map.put(Constant.isStarted, Constant.FALSE);

        dbRef.setValue(map).addOnSuccessListener(aVoid -> {
            //Do what you need to do
            Toast.makeText(activity, getString(R.string.room_created_msg), Toast.LENGTH_SHORT).show();
            //room userDetail
            HashMap<String, String> map1 = new HashMap<>();
            map1.put(Constant.IMAGE, Session.getUserData(Session.PROFILE, activity));
            map1.put(Constant.NAME, Session.getUserData(Session.NAME, activity));
            map1.put(Constant.USER_ID, Session.getUserData(Session.USER_ID, activity));
            dbRef.child(Constant.roomUser).setValue(map1);
            //join user detail
            HashMap<String, String> joinMap = new HashMap<>();
            joinMap.put(Constant.UID, authId);
            joinMap.put(Constant.IMAGE, Session.getUserData(Session.PROFILE, activity));
            joinMap.put(Constant.NAME, Session.getUserData(Session.NAME, activity));
            joinMap.put(Constant.IS_JOINED, "true");
            joinMap.put(Constant.USER_ID, Session.getUserData(Session.USER_ID, activity));
            dbRef.child(Constant.joinUser).child(authId).setValue(joinMap);
            sendRoomDataOnServer(gameCode);
        });


    }

    public void sendRoomDataOnServer(final String roomId) {
        Map<String, String> params = new HashMap<>();
        params.put("create_room", "1");
        params.put(Constant.userId, Session.getUserData(Session.USER_ID, activity));
        params.put("room_id", roomId);
        params.put("room_type", "private");
        if (Constant.isGroupCateEnable)
            params.put("category", Constant.CATE_ID);
        params.put("no_of_que", "10");
        if (Session.getBoolean(Session.LANG_MODE, getApplicationContext())) {
            params.put("language_id", Session.getUserData(Session.LANGUAGE, activity));
        }
        System.out.println("====== create room " + params.toString());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    hideProgressDialog();

                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean(Constant.ERROR);
                    if (!error) {
                        Intent intent = new Intent(activity, WaitingRoomActivity.class);
                        intent.putExtra("from", "private");
                        intent.putExtra("roomKey", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        intent.putExtra("roomId", roomId);
                        intent.putExtra("type", "regular");
                        startActivity(intent);
                    } else
                        Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    private void rateAppClick() {
        try {


            Intent intent = new Intent(activity, SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.open_next, R.anim.close_next);

        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.APP_LINK)));
        }
        finally {
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        rateAppClick();


                         // Close the activity
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void ShowInitPopup(){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.init_popup_dialog, null);
        ImageButton closeButton = dialogView.findViewById(R.id.closeButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        closeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}