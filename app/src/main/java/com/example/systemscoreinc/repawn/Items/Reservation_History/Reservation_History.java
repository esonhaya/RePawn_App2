package com.example.systemscoreinc.repawn.Items.Reservation_History;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.Items.Order_History.Order_History_Adapter;
import com.example.systemscoreinc.repawn.Items.Order_History.Order_History_List;
import com.example.systemscoreinc.repawn.R;

import java.util.ArrayList;
import java.util.List;

public class Reservation_History extends AppCompatActivity {
    Context context;
    Bundle extras;
    //int pid;
    List<Reservation_History_List> ohl = new ArrayList<>();
    Reservation_History_Adapter oha;
    RequestQueue rq;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_history);
        context = this;
        rq = Volley.newRequestQueue(context);
        extras = getIntent().getExtras();
        // pid = extras.getInt("pid");
        ohl = (ArrayList<Reservation_History_List>) getIntent().getSerializableExtra("reserve_list");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reservation History");
        declarestuffs();

        toolbar.setNavigationOnClickListener(v -> {
            // perform whatever you want on back arrow click
            finish();
        });
    }

    private void declarestuffs() {

        final RecyclerView promview = findViewById(R.id.reserve_view);
        promview.setHasFixedSize(true);
        GridLayoutManager historylayout = new GridLayoutManager(this, 1);
        promview.setLayoutManager(historylayout);
        oha = new Reservation_History_Adapter(context, ohl);
        promview.setAdapter(oha);

    }

}