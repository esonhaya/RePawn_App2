package com.example.systemscoreinc.repawn.Orders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order_Adapter extends RecyclerView.Adapter<Order_ViewHolder> {
    private List<Order_List> mDataset;
    private Context Ctx;
    IpConfig ip = new IpConfig();

    public Order_Adapter(Context context, List<Order_List> myDataset) {
        mDataset = myDataset;
        Ctx = context;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public Order_ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View layoutView = LayoutInflater.from(Ctx).inflate(R.layout.my_order_card, parent, false);
        return new Order_ViewHolder(layoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull Order_ViewHolder holder, final int position) {
        final Order_List item = mDataset.get(position);
        holder.product_name.setText(item.getProduct_name());
        Picasso.get()
                .load(ip.getUrl_image() + item.getImage_url())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .into(holder.product_image);
        String rtype = item.getRequest_type();
        if (rtype.equals("order")) {
            holder.item_ribbon.setBackgroundColor(ContextCompat.getColor(Ctx, R.color.colorPrimaryDark));
        } else {
            holder.item_ribbon.setBackgroundColor(Color.BLUE);
        }
        holder.request_price.setText("₱ " + String.format("%.2f", Double.valueOf(item.getProduct_price())));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(item.getDate_sent());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MM/dd/yyyy ");
        String sdate = convetDateFormat.format(date);
        holder.date_sent.setText(sdate);
        if (item.getCancelled() == 1) {
            holder.request_status.setText("cancelled");
            holder.request_status.setTextColor(ContextCompat.getColor(Ctx, R.color.colorError));
        }
        if (!item.getDate_end().isEmpty() && rtype.equals("order")) {
            holder.request_status.setText("Successful");
            holder.request_status.setTextColor(ContextCompat.getColor(Ctx, R.color.colorPrimaryDark));
        }
        if (item.getDate_end().isEmpty()&&item.getCancelled()==0) {
            holder.request_status.setText(item.getStatus());
        }
        holder.linearLayout.setOnClickListener(view -> {
            Intent pinfo = new Intent(Ctx, Order_Info.class);
            pinfo.putExtra("product_id", item.getProduct_id());
            pinfo.putExtra("request_id", item.getOrder_id());
            pinfo.putExtra("request_type", item.getRequest_type());
            pinfo.putExtra("request_status", item.getStatus());
            pinfo.putExtra("payment_type", item.getPayment_type());
            pinfo.putExtra("type", item.getProduct_type());
            Ctx.startActivity(pinfo);

        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
