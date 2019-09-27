package com.example.systemscoreinc.repawn.Home.Items.All_Items;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.systemscoreinc.repawn.Home.Pawnshops.PopularList;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.ItemList;
import com.example.systemscoreinc.repawn.Pawned_Info.Pawned_Info;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class All_Items_Adapter extends RecyclerView.Adapter<All_Items_ViewHolder> {
    private List<ItemList> mDataset;
    private Context Ctx;
    private Session session;
    IpConfig ip = new IpConfig();
    private ArrayList<ItemList> arraylist;
// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public All_Items_Adapter(Context context, List<ItemList> myDataset) {
        mDataset = myDataset;
        Ctx = context;
        session = new Session(Ctx);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(mDataset);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public All_Items_ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View layoutView = LayoutInflater.from(Ctx).inflate(R.layout.all_items_adapter, parent, false);
        return new All_Items_ViewHolder(layoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull All_Items_ViewHolder holder, final int position) {

        final ItemList ppllist = mDataset.get(position);
        if (ppllist.getItem_type().equals("pawned")) {
            holder.SellName.setTextColor(Color.parseColor("#33ccff"));
        } else {
            holder.SellName.setTextColor(Color.parseColor("#FF8C00"));

        }
        Picasso.get()
                .load(ip.getUrl_image() + ppllist.getItem_image())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit()
                .into(holder.item_image);
        holder.ProdName.setText(ppllist.getItem_name());
        holder.SellName.setText(ppllist.getSeller_name());
        holder.ItemPrice.setText("₱ " + ppllist.getPrice());

        holder.linearLayout.setOnClickListener(v -> {

            Intent to_item = new Intent(Ctx, Pawned_Info.class);
            to_item.putExtra("item", ppllist);
            Ctx.startActivity(to_item);

        });

        //  holder.productTitle.setText(); // Here's your value

        // Return the size of your dataset (invoked by the layout manager)
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mDataset.clear();
        if (charText.length() == 0) {
            mDataset.addAll(arraylist);
        } else {
            for (ItemList wp : arraylist) {
                if (wp.getItem_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mDataset.add(wp);
                }
                if (wp.getCat_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mDataset.add(wp);
                }
                if (wp.getSeller_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mDataset.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void price_filter(Long min, Long max) {
        mDataset.clear();
        if (min == 0 && max == 0) {
            mDataset.addAll(arraylist);
        } else {
            if (max == 0 && min > 0) {
                for (ItemList wp : arraylist) {
                    if (wp.getPrice() >= min) {
                        mDataset.add(wp);
                    }
                }
            }
            if (max > 0 && min == 0) {
                for (ItemList wp : arraylist) {
                    if (wp.getPrice() < max) {
                        mDataset.add(wp);
                    }
                }
            }
            if (max > min && min != 0) {
                for (ItemList wp : arraylist) {
                    if (wp.getPrice() <= max && wp.getPrice() >= min) {
                        mDataset.add(wp);
                    }
                }
            }
            if (min > max && max != 0) {
                MDToast.makeText(Ctx, "Please check both prices", MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}