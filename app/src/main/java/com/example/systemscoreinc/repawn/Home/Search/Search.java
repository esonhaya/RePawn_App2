package com.example.systemscoreinc.repawn.Home.Search;

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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import com.example.systemscoreinc.repawn.Home.Home_Navigation1;
import com.example.systemscoreinc.repawn.Home.Items.All_Items.All_Items_Adapter;
import com.example.systemscoreinc.repawn.Home.Items.Home_Items_Adapter;
import com.example.systemscoreinc.repawn.Home.Pawnshops.All_Pawnshops.All_Pawnshops_Adapter;
import com.example.systemscoreinc.repawn.Home.Pawnshops.PopularList;
import com.example.systemscoreinc.repawn.Home.RePawners.All_RePawners.All_RePawners_Adapter;
import com.example.systemscoreinc.repawn.Home.RePawners.RePawnerList;
import com.example.systemscoreinc.repawn.ItemList;
import com.example.systemscoreinc.repawn.R;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    List<ItemList> itemlist;
    List<RePawnerList> replist;
    List<PopularList> poplist;
    All_Items_Adapter aia;
    All_Pawnshops_Adapter hpa;
    All_RePawners_Adapter ra;
    Home_Items_Adapter hia;
    Context context;
    RecyclerView prod_view;
    RecyclerView rep_view;
    RecyclerView pawn_view;
    Bundle extra;
    Intent i;
    Long min_price, max_price;
    MaterialSpinner rs;
    MaterialSpinner ps;
    Button filter_price;
    EditText min, max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        i = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        getSupportActionBar().setTitle("Search");
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationOnClickListener(v -> {
            // perform whatever you want on back arrow click
            Intent intent = new Intent(Search.this, Home_Navigation1.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        context = this;
        extra = getIntent().getExtras();
        // popRecycle();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onQueryTextSubmit(String query) {
        //     ra.filter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ra.filter(s);
        hpa.filter(s);
        aia.filter(s);
        return false;
    }

    public void getPawnshops(View rootView) {
        poplist = (ArrayList<PopularList>) i.getSerializableExtra("pawnshops");
        Log.e("list", String.valueOf(poplist));
        hpa = new All_Pawnshops_Adapter(context, poplist);
        pawn_view = rootView.findViewById(R.id.pawnshop_view);
        pawn_view.setHasFixedSize(true);
        pawn_view.setLayoutManager(new GridLayoutManager(context, 1));
        pawn_view.setAdapter(hpa);
        ps = rootView.findViewById(R.id.pawnshop_spinner);
        ps.setItems("Sort by Popularity", "Sort by Most Reviewed", "Sort by Most Rated");
        ps.setOnItemSelectedListener((view, position, id, item) -> {

            switch (position) {
                case 0:
                    Collections.sort(poplist, PopularList.Follow_Compare);
                    hpa.notifyDataSetChanged();
                    break;
                case 1:
                    Collections.sort(poplist, PopularList.Most_Reviews);
                    hpa.notifyDataSetChanged();
                    break;
                case 2:
                    Collections.sort(poplist, PopularList.Most_Rated);
                    hpa.notifyDataSetChanged();
                    break;
            }

        });
    }

    public void getProducts(View rootView) {
        itemlist = (ArrayList<ItemList>) i.getSerializableExtra("products");
        Log.e("list", String.valueOf(itemlist));
        aia = new All_Items_Adapter(context, itemlist);
        prod_view = rootView.findViewById(R.id.prod_view);
        prod_view.setHasFixedSize(true);
        prod_view.setLayoutManager(new GridLayoutManager(context, 1));
        prod_view.setAdapter(aia);

        rs = rootView.findViewById(R.id.spinner);
        rs.setItems("Price high to low", "Price low to high");
        rs.setOnItemSelectedListener((view, position, id, item) -> {
            switch (position) {
                case 0:
                    Collections.sort(itemlist, ItemList.PriceH);
                    aia.notifyDataSetChanged();
                    break;
                case 1:
                    Collections.sort(itemlist, ItemList.PriceL);
                    aia.notifyDataSetChanged();
                    break;
            }

        });
        filter_price = rootView.findViewById(R.id.filter_price);
        max = rootView.findViewById(R.id.max_price);
        min = rootView.findViewById(R.id.min_price);
        max.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        min.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        filter_price.setOnClickListener(view -> {
            if (max.getText().toString().isEmpty()) {
                max_price = 0L;

            } else {
                Log.e("max",max.getText().toString());
                max_price = Long.valueOf(max.getText().toString());
            }
            if (min.getText().toString().isEmpty()) {
                min_price = 0L;
            }else {
                min_price = Long.valueOf(min.getText().toString());
            }
            aia.price_filter(min_price, max_price);
        });

    }

    public void getRePawners(View rootView) {
        replist = (ArrayList<RePawnerList>) i.getSerializableExtra("repawners");
        Log.e("rep", String.valueOf(replist));
        ra = new All_RePawners_Adapter(context, replist);
        rep_view = rootView.findViewById(R.id.rep_view);
        rep_view.setHasFixedSize(true);
        rep_view.setLayoutManager(new GridLayoutManager(context, 1));
        rep_view.setAdapter(ra);
        rs = rootView.findViewById(R.id.spinner);
        rs.setItems("Sort by Popularity", "Sort by Most Reviewed", "Sort by Most Rated");
        rs.setOnItemSelectedListener((view, position, id, item) -> {

            switch (position) {
                case 0:
                    Collections.sort(replist, RePawnerList.Follow_Compare);
                    ra.notifyDataSetChanged();
                    break;
                case 1:
                    Collections.sort(replist, RePawnerList.Most_Reviews);
                    ra.notifyDataSetChanged();
                    break;
                case 2:
                    Collections.sort(replist, RePawnerList.Most_Rated);
                    ra.notifyDataSetChanged();
                    break;
            }

        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Products(), "Products");
        adapter.addFragment(new Pawnshops(), "Pawnshops");
        adapter.addFragment(new RePawners(), "RePawners");
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
