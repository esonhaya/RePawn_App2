package com.example.systemscoreinc.repawn.Orders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.systemscoreinc.repawn.Config.Config;
import com.example.systemscoreinc.repawn.IpConfig;
import com.example.systemscoreinc.repawn.Items.ConfirmationActivity;
import com.example.systemscoreinc.repawn.Pawnshop.Pawnshop_Page;
import com.example.systemscoreinc.repawn.Profile_Related.RePawner_Profile;
import com.example.systemscoreinc.repawn.R;
import com.example.systemscoreinc.repawn.Session;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order_Info extends AppCompatActivity {
    TextView date_accepted, buyer_name, payment_type, reservation_start_date, reservation_end_date, date_order_ended,
            paypal_id, pay_amount, Date_paid;
    TextView product_name, item_price, item_desc, item_category, seller_name, pending_message, date_sent,
            reservation_sent, reservation_payment;
    LinearLayout order_layout, payment_layout, order_info_layout, reserve_info_layout, receipt_layout;
    Context context;
    Session session;
    RequestQueue rq;
    ImageView product_image, product_receipt;
    Button buyer_confirm, change_payment, cancel_request, pay_pal, order_now;
    int rid, req_id, seller_id, cancelled, details_id;
    Order_Adapter oa;
    List<Order_List> ol;
    RecyclerView rv;
    IpConfig ip = new IpConfig();
    String url = ip.getUrl() + "order_info.php";
    Bundle extra;
    String product_id, request_type, request_status, order_details_id, spayment_type = "manual", reserve_id;
    Long price;
    String descrip, category, type, Sproduct_name, Sseller_name;
    int seller_confirm, buyer_confirmation;
    int already_paid, seller_confirmed;
    String receipt;
    int choice, selected;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId(Config.PAYPAL_CLIENT__ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__info);
        context = this;
        session = new Session(this);
        rid = session.getID();
        rq = Volley.newRequestQueue(context);
        extra = getIntent().getExtras();
        spayment_type = extra.getString("payment_type");
        product_id = extra.getString("product_id");
        request_type = extra.getString("request_type");
        request_status = extra.getString("request_status");
        //   seller_id = extra.getInt("seller_id");
        //  Log.e("seller", String.valueOf(seller_id));
        type = extra.getString("type");
        req_id = extra.getInt("request_id");
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
//        price = Float.valueOf(pprice.getText().toString());
        declare_this();
        visible_what();
        get_seller_id();
//        get_order_details();
//        get_reserve_details();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void get_seller_id() {
        StringRequest req = new StringRequest(Request.Method.POST, url, response -> {
            seller_id = Integer.valueOf(response.trim());
        }, error -> {

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("get_seller", "1");
                params.put("pid", String.valueOf(product_id));
                params.put("type", type);


                return params;
            }
        };
        rq.add(req);
    }

    public void visible_what() {
        product_info();
        if (type.equals("pawned")) {
            receipt_layout.setVisibility(View.VISIBLE);
        }
        if (request_status.equals("pending")) {
            pending_message.setVisibility(View.VISIBLE);
            change_payment.setVisibility(View.VISIBLE);
            get_order_details();
            get_reserve_details();
        } else if (request_status.equals("accepted")) {
            if (request_type.equals("order")) {
                get_order_details();

            } else {
                get_reserve_details();
            }
        } else {
            pending_message.setText("Declined Request");
            pending_message.setVisibility(View.VISIBLE);
        }

    }

    private void declare_this() {
        //product_name,item_price,item_desc,item_category,seller_name;
        product_name = this.findViewById(R.id.product_name);
        item_price = this.findViewById(R.id.item_price);
        item_desc = this.findViewById(R.id.item_desc);
        item_category = this.findViewById(R.id.item_category);
        seller_name = this.findViewById(R.id.seller_name);
        order_layout = this.findViewById(R.id.order_layout);
        order_info_layout = this.findViewById(R.id.order_info_layout);
        payment_layout = this.findViewById(R.id.payment_layout);
        reserve_info_layout = this.findViewById(R.id.reserve_info_layout);
        pending_message = this.findViewById(R.id.pending_message);
        //textviews
        date_accepted = this.findViewById(R.id.date_accepted);
        date_order_ended = this.findViewById(R.id.date_order_ended);
        payment_type = this.findViewById(R.id.payment_type);
        reservation_start_date = this.findViewById(R.id.reservation_start_date);
        reservation_end_date = this.findViewById(R.id.reservation_end_date);
        paypal_id = this.findViewById(R.id.paypal_id);
        pay_amount = this.findViewById(R.id.pay_amount);
        Date_paid = this.findViewById(R.id.Date_Paid);
        product_image = this.findViewById(R.id.product_image);
        product_receipt = this.findViewById(R.id.product_receipt);
        buyer_confirm = this.findViewById(R.id.buyer_confirm);
        change_payment = this.findViewById(R.id.change_payment);
        cancel_request = this.findViewById(R.id.cancel_request);
        receipt_layout = this.findViewById(R.id.receipt_layout);
        pay_pal = this.findViewById(R.id.pay_pal);
        date_sent = this.findViewById(R.id.date_sent);
        reservation_sent = this.findViewById(R.id.reservation_sent);
        reservation_payment = this.findViewById(R.id.reservation_payment);
        pay_pal.setOnClickListener(view -> pay_now());
        order_now = this.findViewById(R.id.order_now);
        order_now.setOnClickListener(view -> order_now());
        if (type.equals("pawned")) {
            get_receipt();
            Picasso.get()
                    .load(ip.getUrl_image() + receipt)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .fit()
                    .into(product_receipt);
        } else {
            product_receipt.setVisibility(View.GONE);
        }
        seller_name.setOnClickListener(view -> {
            if (type.equals("pawned")) {
                Intent to_prof = new Intent(Order_Info.this, RePawner_Profile.class);
                to_prof.putExtra("user_id", seller_id);
                startActivity(to_prof);
            } else {
                Intent to_prof = new Intent(Order_Info.this, Pawnshop_Page.class);
                to_prof.putExtra("user_id", seller_id);
                startActivity(to_prof);
            }
        });
        buyer_confirm.setOnClickListener(view -> confirm_buyer());
        change_payment.setOnClickListener(view -> change_payment());
        cancel_request.setOnClickListener(view -> {
            android.support.v7.app.AlertDialog prom = new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setTitle("Are you sure you want to cancel?")
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        if (request_type.equals("order")) {
                            cancel_order();
                        } else {
                            cancel_reserve();
                        }

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                    })
                    .create();
            prom.show();
        });
    }

    public void order_now() {
        android.support.v7.app.AlertDialog prom = new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Are you sure you want to order this item?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    order_confirm();

                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                })
                .create();
        prom.show();

    }

    private void order_confirm() {

        StringRequest req = new StringRequest(Request.Method.POST, url, response -> {
            MDToast.makeText(context, response, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
            Intent to_orders = new Intent(context, Orders.class);
            startActivity(to_orders);
        }, error -> {

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_now", "1");
                params.put("rid", String.valueOf(req_id));
                params.put("pid", String.valueOf(product_id));
                params.put("uid", String.valueOf(session.getID()));
                params.put("payment_type", spayment_type);


                return params;
            }
        };
        rq.add(req);
    }

    public void get_receipt() {
        StringRequest req = new StringRequest(Request.Method.POST, url, response ->
                receipt = response.trim(), error -> {

        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("get_receipt", "1");
                params.put("pid", String.valueOf(product_id));


                return params;
            }
        };
        rq.add(req);
    }

    public void pay_now() {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(price), "PHP", "Payment for " + Sproduct_name + " to " + Sseller_name + "",
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void cancel_order() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            if (!response.equals("1")) {
                startActivity(new Intent(Order_Info.this, Orders.class));
            } else {
                MDToast.makeText(context, response, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();

            }

        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("odi", String.valueOf(order_details_id));
                params.put("pid", product_id);
                params.put("cancel_order", "1");
                return params;
            }
        };
        rq.add(request);
    }

    public void cancel_reserve() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            MDToast.makeText(context, response, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
            startActivity(new Intent(Order_Info.this, Orders.class));


        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rid", String.valueOf(req_id));
                params.put("pid", product_id);
                params.put("cancel_reserve", "1");
                return params;
            }
        };
        rq.add(request);
    }

    public void change_payment() {
        selected = 1;
        String[] choices = {"Paypal", "Manual"};
        if (spayment_type.equals("paypal")) {
            selected = 0;
        }
        Log.e("payment", spayment_type);
        android.support.v7.app.AlertDialog prom = new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Payment Option")
                .setSingleChoiceItems(choices, selected, (dialogInterface, selectedIndex) -> choice = selectedIndex)
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (choice != selected) {
                        update_ptype();
                    }

                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .create();
        prom.show();
    }

    public void update_ptype() {
        if (choice == 0) {
            spayment_type = "paypal";
        } else {
            spayment_type = "manual";
        }
        Log.e("payment type(new)", spayment_type);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            MDToast.makeText(context, response, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show();
            restart();
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rdi", String.valueOf(details_id));
                params.put("req_type", request_type);
                params.put("ptype", spayment_type);
                params.put("update_payment", "1");
                return params;
            }
        };
        rq.add(request);
    }

    private void restart() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
        overridePendingTransition(0, 0);

    }

    public void confirm_buyer() {
        android.support.v7.app.AlertDialog prom = new android.support.v7.app.AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                .setTitle("Are you sure you have received the item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    confirm_yes();

                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .create();
        prom.show();

    }

    public void confirm_yes() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response ->
                MDToast.makeText(context, response, MDToast.LENGTH_LONG, MDToast.TYPE_SUCCESS).show(), error -> {
            restart();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("odi", String.valueOf(order_details_id));
                params.put("pid", product_id);
                params.put("confirm_transaction_buyer", "1");
                return params;
            }
        };
        rq.add(request);
    }

    public void get_payment_details() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                JSONArray item_array = pawnedobject.getJSONArray("payment_info");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject info = item_array.getJSONObject(i);
                        paypal_id.append(info.getString("Paypal_Payment_ID"));
                        pay_amount.append(info.getString("Amount"));
                        String da = info.getString("Date_Paid");
                        Date_paid.append(date_formatter(da));
                        if (seller_confirm == 1 && buyer_confirmation == 0) {
                            buyer_confirm.setVisibility(View.VISIBLE);
                        }
                        if (seller_confirm == 1 && buyer_confirmation == 1) {

                        }
                    }
                    pay_pal.setVisibility(View.GONE);
                    payment_layout.setVisibility(View.VISIBLE);
                } else {
                    cancel_request.setVisibility(View.VISIBLE);
                    //   payment_layout.setVisibility(View.GONE);
                    pay_pal.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oid", String.valueOf(order_details_id));
                params.put("get_payment_info", "1");
                return params;
            }
        };
        rq.add(request);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void get_reserve_details() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                //extracting json array from response string
                JSONArray item_array = pawnedobject.getJSONArray("reservation_info");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject info = item_array.getJSONObject(i);
                        String ds = info.getString("Date_Sent");
                        String da = info.getString("Date_Accepted");
                        if (ds.isEmpty()) {
                            reservation_sent.append("not yet");
                        } else {
                            reservation_sent.append(date_formatter(ds));
                        }
                        if (da.isEmpty()) {
                            reservation_start_date.append("not yet");
                        } else {
                            reservation_start_date.append(date_formatter(da));
                        }
                        if (info.getString("Date_End").isEmpty()) {

                            date_order_ended.append("not yet");
                        } else {
                            da = info.getString("Date_End");
                            date_order_ended.append(date_formatter(da));
                        }
                        String stat = info.getString("Status");
                        spayment_type = info.getString("Payment_Type");
                        reservation_payment.append(spayment_type);
                        reserve_id = info.getString("Reservation_Details_ID");
                        details_id = Integer.valueOf(reserve_id);
                        int cancelled = info.getInt("cancelled");
                        if (cancelled == 1) {
                            pending_message.setText("Cancelled");
                            pending_message.setVisibility(View.VISIBLE);
                        } else {
                            if (stat.equals("accepted")) {
                                order_now.setVisibility(View.VISIBLE);
                            }
                            cancel_request.setVisibility(View.VISIBLE);
                        }
                    }
                    reserve_info_layout.setVisibility(View.VISIBLE);
                } else {
                    reserve_info_layout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rid", String.valueOf(req_id));
                params.put("get_reservation_info", "1");
                return params;
            }
        };
        rq.add(request);
    }


    private void product_info() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                //extracting json array from response string
                JSONArray item_array = pawnedobject.getJSONArray("product_info");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject info = item_array.getJSONObject(i);
                        Picasso.get()
                                .load(ip.getUrl_image() + info.getString("product_image"))
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .fit()
                                .into(product_image);
                        if (type.equals("pawned")) {
                            Picasso.get()
                                    .load(ip.getUrl_image() + info.getString("product_receipt"))
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .fit()
                                    .into(product_receipt);
                        }
                        price = info.getLong("Product_price");
                        Sproduct_name = info.getString("Product_name");
                        product_name.setText(Sproduct_name);
                        item_price.append(String.valueOf(price));
                        descrip = info.getString("Product_description");
                        category = info.getString("Category_name");
                        item_desc.setText(descrip);
                        item_category.setText(category);
                        Sseller_name = info.getString("seller_name");
                        seller_name.setText(Sseller_name);


                    }

                } else {
                    order_info_layout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pid", product_id);
                params.put("product_info", "1");
                return params;
            }
        };
        rq.add(request);

    }


    private void get_order_details() {
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.e("response", response);
                JSONObject pawnedobject = new JSONObject(response);
                //extracting json array from response string
                JSONArray item_array = pawnedobject.getJSONArray("order_info");
                if (item_array.length() > 0) {
                    for (int i = 0; i < item_array.length(); i++) {
                        JSONObject info = item_array.getJSONObject(i);
                        String ds = info.getString("Date_Sent");
                        String da = info.getString("Date_Accepted");
                        if (ds.isEmpty()) {
                            date_sent.append("not yet");
                        } else {
                            date_sent.append(date_formatter(ds));
                        }
                        if (da.isEmpty()) {
                            date_accepted.append("not yet");
                        } else {
                            date_accepted.append(date_formatter(da));
                        }
                        if (info.getString("Date_End").isEmpty()) {

                            date_order_ended.append("not yet");
                        } else {
                            da = info.getString("Date_End");
                            date_order_ended.append(date_formatter(da));

                        }

                        spayment_type = info.getString("Payment_Type");
                        order_details_id = info.getString("Order_Details_ID");
                        details_id = Integer.valueOf(order_details_id);
                        seller_confirm = info.getInt("Seller_confirmation");
                        buyer_confirmation = info.getInt("Buyer_confirmation");
                        if (seller_confirm == 1 && spayment_type.equals("manual")) {
                            buyer_confirm.setVisibility(View.VISIBLE);
                        }
                        if (seller_confirm == 0 && spayment_type.equals("manual")) {
                            cancel_request.setVisibility(View.VISIBLE);
                        }
                        String stat = info.getString("Status");
                        cancelled = info.getInt("cancelled");
                        if (cancelled == 1) {
                            pending_message.setText("Cancelled");
                            pending_message.setVisibility(View.VISIBLE);
                            cancel_request.setVisibility(View.GONE);
                        } else {
                            if (stat.equals("accepted")) {
                                if (spayment_type.equals("paypal")) {
                                    get_payment_details();
                                }
                                if (spayment_type.equals("manual")) {
                                    buyer_confirm.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        payment_type.append(spayment_type);
                    }
                    order_layout.setVisibility(View.VISIBLE);
                } else {
                    order_layout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rid", String.valueOf(req_id));
                params.put("get_order_info", "1");
                return params;
            }
        };
        rq.add(request);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    String paymentDetails = confirm.toJSONObject().toString(4);
                    Log.i("paymentExample", paymentDetails);

                    //Starting a new activity for the payment details and also putting the payment details with intent
                    startActivity(new Intent(this, ConfirmationActivity.class)
                            .putExtra("PaymentDetails", paymentDetails)
                            .putExtra("PaymentAmount", price)
                            .putExtra("pay_order", "1")
                            .putExtra("oid", order_details_id));

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }


    public String date_formatter(String sdate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm ");
        return convetDateFormat.format(date);
    }
}
