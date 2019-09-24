package com.example.systemscoreinc.repawn.Items;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.valdesekamdem.library.mdtoast.MDToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Edit_Pawned_Info extends AppCompatActivity {

    private EditText pname, pdesc, pprice;
    private MaterialButton editbutton, catbutton;
    private AutoCompleteTextView pcat;
    private RadioGroup preserve;
    private TextInputLayout pname_layout;
    Toolbar toolbar;
    Bundle extras;
    Bitmap bimage, breceipt;
    String simage, sreceipt;
    Uri filePath;
    Context context;
    ImageView product_image, receipt_image;
    LinearLayout edit_layout;
    RequestQueue rq;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl() + "edit_pawned.php";
    int pid;
    String Spname, Simage, Sreceipt;
    long price;
    ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pawned);
        extras = getIntent().getExtras();
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Pawned ");
        rq = Volley.newRequestQueue(context);
        get_categories();
        this_stuff();
        get_extra();
        toolbar.setNavigationOnClickListener(v -> {
            // perform whatever you want on back arrow click

            finish();
        });

        editbutton.setOnClickListener(view -> {
            android.support.v7.app.AlertDialog onconf = new android.support.v7.app.AlertDialog.Builder(context, R.style.RePawnDialog)
                    .setTitle("Save changes?")
                    .setPositiveButton("CONFIRM", (dialog, which) -> editall())
                    .setNegativeButton("CANCEL", (dialog, which) -> {

                    })
                    .create();
            onconf.show();


        });


    }

    public void get_categories() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                JSONArray item_array = object.getJSONArray("categories");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        //extracting json array from response string
                        JSONObject item = item_array.getJSONObject(i);
                        categories.add(item.getString("Category_name"));
                    }
                }
                ArrayAdapter<String> adapter;
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
                pcat.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("get_categories", "1");
                return params;
            }
        };
        rq.add(request);
    }

    private void get_extra() {
        Spname = extras.getString("pname");
        pname.setText(Spname);
        pcat.setText(extras.getString("pcat"));
        pdesc.setText(extras.getString("pdesc"));
        price = extras.getLong("pprice");
        pprice.setText(String.valueOf(price));
        //   String  pawned_id=Integer.toString(extras.getInt("Pawned_ID"));
        int choosen = extras.getInt("reservable");
        pid = extras.getInt("pid");
        if (choosen == 1) {
            preserve.check(R.id.yes);
        } else {
            preserve.check(R.id.no);
        }

        Simage = extras.getString("item_image");
        Log.e("image", Simage);
        Sreceipt = extras.getString("receipt");
//        Picasso.get()
//                .load(ip.getUrl_image() + Simage)
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .into(product_image);
//        Glide.with(this)
//                .asBitmap()
//                .load(ip.getUrl_image()+Simage)
//                .skipMemoryCache(true)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        bimage = resource;
//                        product_image.setImageBitmap(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });
        Picasso.get()
                .load(ip.getUrl_image() + Simage)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(new Target() {

                    @Override
                    public void onBitmapLoaded(final Bitmap bmap, Picasso.LoadedFrom from) {
                        /* Save the bitmap or do something with it here */
                        bimage = bmap;
                        // Set it in the ImageView
                        product_image.setImageBitmap(bmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
        Glide.with(this)
                .asBitmap()
                .load(ip.getUrl_image()+Sreceipt)
                .skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        breceipt = resource;
                        receipt_image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

    private void editall() {
        if (breceipt == null || bimage == null) {
            MDToast.makeText(context, "Please input all images", MDToast.LENGTH_LONG, MDToast.TYPE_ERROR).show();
        } else {
            simage = imageToString(bimage);
            sreceipt = imageToString(breceipt);
            int reserve_id = preserve.getCheckedRadioButtonId();
            String reserve;
            if (reserve_id == R.id.yes)
                reserve = "1";
            else
                reserve = "0";
            final String reserve_pin = reserve;
            boolean noErrors = true;
            final List<TextInputLayout> textInputLayouts = Utils.findViewsWithType(
                    edit_layout, TextInputLayout.class);
            for (TextInputLayout textInputLayout : textInputLayouts) {
                String editTextString = textInputLayout.getEditText().getText().toString();
                if (editTextString.isEmpty()) {
                    textInputLayout.setError(getResources().getString(R.string.error_empty));
                    noErrors = false;
                } else {
                    textInputLayout.setError(null);
                }
            }
            if (!pprice.getText().toString().equals("")) {
                price = Long.valueOf(pprice.getText().toString());
            }
            if (pdesc.getText().toString().equals("")) {
                pdesc.setError("Field should not be empty");
            }
            if (price <= 0) {
                MDToast mdToast = MDToast.makeText(context, "input valid price", MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
                noErrors = false;
            }
            if (noErrors) {

                StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
                    if (response.equals("1")) {
                        pname_layout.setError("name's already been used");
                        //    Toast.makeText(context, pid+"", Toast.LENGTH_SHORT).show();
                    } else
                        MDToast.makeText(context,response,MDToast.LENGTH_LONG,MDToast.TYPE_SUCCESS).show();
                       startActivity(new Intent(Edit_Pawned_Info.this, Pawned.class));
                    finish();
                }, error -> {

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("product_name", pname.getText().toString());
                        params.put("description", pdesc.getText().toString());
                        params.put("update_product", "1");
                        params.put("category", pcat.getText().toString());
                        params.put("res", reserve_pin);
                        params.put("product_id", String.valueOf(pid));
                        params.put("price", String.valueOf(price));
                        params.put("image", simage);
                        params.put("receipt", sreceipt);

                        return params;
                    }
                };
                rq.add(request);


            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            filePath = data.getData();
            if (requestCode == 1) {

                //Get image
                try {
                    bimage = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    product_image.setImageBitmap(bimage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    breceipt = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    receipt_image.setImageBitmap(breceipt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void pickImage(int req) {
        Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoIntent.setType("image/*");
        photoIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photoIntent, "Complete action using"), req);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void this_stuff() {
        editbutton = this.findViewById(R.id.done_add);
        pname = this.findViewById(R.id.pname_edit);
        pcat = this.findViewById(R.id.cat_edit);

        pdesc = this.findViewById(R.id.pdesc_edit);
        pprice = this.findViewById(R.id.pprice_edit);
        pprice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        preserve = this.findViewById(R.id.p_reservable);
        pname_layout = this.findViewById(R.id.pname_input);
        edit_layout = this.findViewById(R.id.edit_layout);
        product_image = this.findViewById(R.id.product_image);
        receipt_image = this.findViewById(R.id.receipt_image);
        product_image.setOnClickListener(view -> pickImage(1));
        receipt_image.setOnClickListener(view -> pickImage(2));

    }
}
