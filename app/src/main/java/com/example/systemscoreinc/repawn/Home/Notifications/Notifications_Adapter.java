package com.example.systemscoreinc.repawn.Home.Notifications;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.Items.Pawned;
import com.example.systemscoreinc.repawn.Orders.Orders;
import com.example.systemscoreinc.repawn.Profile_Related.RePawner_Profile;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Notifications_Adapter extends RecyclerView.Adapter<Notifications_ViewHolder> {
    private List<Notifications_List> mDataset;
    private Context Ctx;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl() + "database.php";
    private ArrayList<Notifications_List> arraylist;
    Intent to_page;
    Session session;
    RequestQueue rq;

    public Notifications_Adapter(Context context, List<Notifications_List> myDataset) {
        mDataset = myDataset;
        Ctx = context;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(mDataset);
        session = new Session(context);
        rq = Volley.newRequestQueue(Ctx);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public Notifications_ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View layoutView = LayoutInflater.from(Ctx).inflate(R.layout.notification_card, parent, false);
        return new Notifications_ViewHolder(layoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull Notifications_ViewHolder holder, final int position) {

        final Notifications_List nlist = mDataset.get(position);
        holder.sell_name.setText(nlist.getMessage());
        Picasso.get()
                .load(ip.getUrl_image() + nlist.getNotif_image())
                .fit()
                .into(holder.sell_image);
        try {
            holder.sell_date.setText(date_ago(nlist.getDate_posted()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.linearLayout.setOnClickListener(v -> {
            if (nlist.getType() == 1) {
                to_page = new Intent(Ctx, RePawner_Profile.class);
                to_page.putExtra("user_id", nlist.getLink_id());
            } else if (nlist.getType() == 2) {
                to_page = new Intent(Ctx, Orders.class);

            } else if (nlist.getType() == 3) {
                to_page = new Intent(Ctx, Orders.class);
            } else if (nlist.getType() == 4) {
                to_page = new Intent(Ctx, Pawned.class);
            } else if (nlist.getType() == 5) {
                to_page = new Intent(Ctx, RePawner_Profile.class);
                to_page.putExtra("user_id", nlist.getLink_id());
            } else if (nlist.getType() == 6) {
                to_page = new Intent(Ctx, RePawner_Profile.class);
                to_page.putExtra("user_id", session.getID());
            }
            check_notification(nlist.getNotif_id());

            Ctx.startActivity(to_page);
        });
    }

    public void check_notification(int notif_id) {
        StringRequest rq = new StringRequest(Request.Method.POST, url, response -> {
        },
                error -> {
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nid", String.valueOf(notif_id));
                params.put("check_notif", "1");
                return super.getParams();
            }
        };

    }

    public String date_ago(String adate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date past = format.parse(adate);
        Date now = new Date();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else {
            return days + " days ago";
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}