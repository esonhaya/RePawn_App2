package com.example.systemscoreinc.repawn.Profile_Related;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;


public class Subscription_Info extends AppCompatActivity{
    Session session;
    Context context;
    Bundle extra;
    RequestQueue rq;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        session = new Session(this);
        context = this;
        extra = getIntent().getExtras();
        rq = Volley.newRequestQueue(context);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subscription Info");

        toolbar.setNavigationOnClickListener(v -> {
            // perform whatever you want on back arrow click
            finish();
        });

    }
}
