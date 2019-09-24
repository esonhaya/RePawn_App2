package com.example.systemscoreinc.repawn.Orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.Home.Home_Navigation1;
import com.example.systemscoreinc.repawn.Home.Items.Home_Items_Adapter;
import com.example.systemscoreinc.repawn.Home.Search.Pawnshops;
import com.example.systemscoreinc.repawn.Home.Search.Products;
import com.example.systemscoreinc.repawn.Home.Search.RePawners;
import com.example.systemscoreinc.repawn.Home.Search.Search;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.ItemList;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders extends AppCompatActivity {
    Context context;
    Session session;
    RequestQueue rq;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int rid;
    Order_Adapter oa;
    Order_Adapter ra;
    List<Order_List> order_list = new ArrayList<>();
    List<Order_List> reserve_list = new ArrayList<>();
    RecyclerView order_view;
    RecyclerView reserve_view;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl() + "orders.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_orders);
        context = this;
        session = new Session(this);
        rid = session.getID();
        rq = Volley.newRequestQueue(context);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setTitle("Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        super.onCreate(savedInstanceState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Orders.this, Home_Navigation1.class));
        }

        return super.onOptionsItemSelected(item);
    }


    public void getOrders(View rootView) {
        oa = new Order_Adapter(context, order_list);
        order_view = rootView.findViewById(R.id.order_view);
        order_view.setHasFixedSize(true);
        order_view.setLayoutManager(new GridLayoutManager(context, 1));
        order_view.setAdapter(oa);
        get_orders();
    }
    public void getReservations(View rootView) {
        ra = new Order_Adapter(context, reserve_list);
        reserve_view = rootView.findViewById(R.id.order_view);
        reserve_view.setHasFixedSize(true);
        reserve_view.setLayoutManager(new GridLayoutManager(context, 1));
        reserve_view.setAdapter(ra);
        get_reserves();
    }
    public void get_orders(){
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                //extracting json array from response string
                JSONArray item_array = pawnedobject.getJSONArray("orders");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject item = item_array.getJSONObject(i);
                        Order_List list = new Order_List(item.getInt("Order_Product_ID"),item.getString("product_image"), item.getString("Product_ID"),
                                item.getLong("Product_price"), item.getString("request_type"), item.getString("Product_name")
                                , item.getString("Date_Sent"), item.getString("Product_Type"),item.getString("Status"));
                        order_list.add(list);
                    }
                    oa.notifyDataSetChanged();
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_requests", "1");
                params.put("user_id", String.valueOf(session.getID()));

                return params;
            }
        };
        rq.add(request);
    }
    public void get_reserves(){
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                //extracting json array from response string
                JSONArray item_array = pawnedobject.getJSONArray("orders");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject item = item_array.getJSONObject(i);
                        Order_List list = new Order_List(item.getInt("Reservation_Product_ID"),item.getString("product_image"), item.getString("Product_ID"),
                                item.getLong("Product_price"), item.getString("request_type"), item.getString("Product_name")
                                , item.getString("Date_Sent"), item.getString("Product_Type"),item.getString("Status"));
                        reserve_list.add(list);
                    }
                    ra.notifyDataSetChanged();
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reserve_requests", "1");
                params.put("user_id", String.valueOf(session.getID()));

                return params;
            }
        };
        rq.add(request);
    }

    private void setupViewPager(ViewPager viewPager) {
        Orders.ViewPagerAdapter adapter = new Orders.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Orders(), "Orders");
        adapter.addFragment(new Fragment_Reservations(), "Reservations");
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
