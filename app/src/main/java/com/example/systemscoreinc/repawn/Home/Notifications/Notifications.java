package com.example.systemscoreinc.repawn.Home.Notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.Home.RePawners.RePawnerList;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifications extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    List<Notifications_List> new_list;
    List<Notifications_List> old_list;
    Notifications_Adapter nadpt;
    Notifications_Adapter oadpt;
    Context context;
    Bundle extra;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl() + "database.php";
    RecyclerView new_view, checked_view;
    Intent i;
    RequestQueue rq;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        i = getIntent();
        context = this;
        session = new Session(this);
        rq = Volley.newRequestQueue(context);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        getSupportActionBar().setTitle("Notifications");
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        context = this;
        extra = getIntent().getExtras();
        // popRecycle();
    }


    public void getNew_Notif(View rootView) {
        new_list = (ArrayList<Notifications_List>) i.getSerializableExtra("new_notifs");
        nadpt = new Notifications_Adapter(context, new_list);
        new_view = rootView.findViewById(R.id.notif_view);
        new_view.setHasFixedSize(true);
        new_view.setLayoutManager(new GridLayoutManager(context, 1));
        new_view.setAdapter(nadpt);
    }

    public void getOld_Notif(View rootView) {
        oadpt = new Notifications_Adapter(context, old_list);
        checked_view = rootView.findViewById(R.id.notif_view);
        checked_view.setHasFixedSize(true);
        checked_view.setLayoutManager(new GridLayoutManager(context, 1));
        checked_view.setAdapter(oadpt);
        StringRequest preq = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                //extracting json array from response string
                JSONArray cats = jsonObject.getJSONArray("old_notifs");
                Log.e("jsondata", String.valueOf(cats));
                if (cats.length() > 0) {
                    for (int i = 0; i < cats.length(); i++) {
                        JSONObject cc = cats.getJSONObject(i);
                        Notifications_List notif = new Notifications_List(cc.getInt("Notification_ID"), cc.getInt("Link_ID"), cc.getInt("Type"),
                                cc.getInt("checked"), cc.getString("Date_Posted"), cc.getString("image"),
                                cc.getString("message"));
                        old_list.add(notif);
                    }
                    oadpt.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("old_notifs", "1");
                params.put("user_id", String.valueOf(session.getID()));
                return params;
            }
        };
        rq.add(preq);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new New_Notifications(), "NEW");
        //   adapter.addFragment(new Checked_Notifications(), "CHECKED");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
