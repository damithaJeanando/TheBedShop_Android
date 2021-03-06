package com.example.thebedshop.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.thebedshop.Adapters.CartAdapter;
import com.example.thebedshop.AppSingleton;
import com.example.thebedshop.Models.CartItem;
import com.example.thebedshop.Models.Session;
import com.example.thebedshop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartItemsList;
    private RecyclerView.LayoutManager layout;
    private FloatingActionButton checkoutBtn;
    private TextView emptyText;
    List<CartItem> cartItems;
    Session session;


    String url = "http://10.0.3.2:8080/auth/cart/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        session = new Session(this);
        cartItemsList = findViewById(R.id.cart_list);
        checkoutBtn = findViewById(R.id.btn_checkout);
        emptyText = findViewById(R.id.empty_text);

        cartItemsList.setHasFixedSize(true);
        layout = new LinearLayoutManager(this);
        cartItemsList.setLayoutManager(layout);
        getCartItems();

        checkoutBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cartArr", (Serializable) cartItems);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            getCartItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
        getCartItems();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCartItems();

    }

    public void getCartItems() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url + "userId/"+session.getUserEmail(), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        cartItems = gson.fromJson(response.toString(), new TypeToken<List<CartItem>>(){}.getType());
                        final CartAdapter cartAdapter = new CartAdapter(CartActivity.this, R.layout.cart_item_card, cartItems);
                        cartItemsList.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();
                        if(cartItems.size()<1){
                            emptyText.setText("No items availble in the cart");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error while fetching cart items" + error);
                    }
                }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = session.getUserEmail() + ":" + session.getpassword();
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        AppSingleton.getInstance(CartActivity.this).addToRequestQueue(jsonArrayRequest);


    }
}
