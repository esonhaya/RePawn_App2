package com.example.systemscoreinc.repawn.Items.Reservation_History;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.Profile_Related.RePawner_Profile;
import com.example.systemscoreinc.repawn.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Adapter used to show a simple grid of products.
 */
public class Reservation_History_Adapter extends RecyclerView.Adapter<Reservation_History_ViewHolder> {
    private List<Reservation_History_List> mDataset;
    private Context Ctx;
    IpConfig ip = new IpConfig();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public Reservation_History_Adapter(Context context, List<Reservation_History_List> myDataset) {
        mDataset = myDataset;
        Ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public Reservation_History_ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View layoutView = LayoutInflater.from(Ctx).inflate(R.layout.reserve_card, parent, false);
        return new Reservation_History_ViewHolder(layoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull Reservation_History_ViewHolder holder, final int position) {
        final Reservation_History_List list = mDataset.get(position);
        holder.orderer_name.append(list.getBuyer_name());
        holder.date_sent.append(list.getDate_Started());
        holder.date_accepted.append(list.getDate_Accepted());
        holder.date_ended.append(list.getDate_End());
        holder.payment_type.append(list.getPayment_type());
        holder.orderer_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_prof = new Intent(Ctx, RePawner_Profile.class);
                to_prof.putExtra("user_id", list.getBuyer_id());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String convert_date(String sdate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        sdate = convetDateFormat.format(date);
        return sdate;
    }
}