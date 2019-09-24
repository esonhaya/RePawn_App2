package com.example.systemscoreinc.repawn.Items.Order_History;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.Items.Promotion_History.Promotion_History_Adapter;
import com.example.systemscoreinc.repawn.Items.Promotion_History.Promotion_History_List;
import com.example.systemscoreinc.repawn.R;

import java.util.ArrayList;
import java.util.List;

public class Order_History extends AppCompatActivity {
    Context context;
    Bundle extras;
    //int pid;
    List<Order_History_List> ohl = new ArrayList<>();
    Order_History_Adapter oha;
    RequestQueue rq;
    IpConfig ip=new IpConfig();
    String url=ip.getUrl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        context = this;
        rq = Volley.newRequestQueue(context);
        extras = getIntent().getExtras();
        // pid = extras.getInt("pid");
       ohl = (ArrayList<Order_History_List>) getIntent().getSerializableExtra("order_list");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");
        declarestuffs();

        toolbar.setNavigationOnClickListener(v -> {
            // perform whatever you want on back arrow click
            finish();
        });
    }

    private void declarestuffs() {

        final RecyclerView promview = findViewById(R.id.order_view);
        promview.setHasFixedSize(true);
        GridLayoutManager historylayout = new GridLayoutManager(this, 1);
        promview.setLayoutManager(historylayout);
        oha = new Order_History_Adapter(context, ohl);
        promview.setAdapter(oha);

    }

}