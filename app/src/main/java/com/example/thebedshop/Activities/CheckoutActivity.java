package com.example.thebedshop.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thebedshop.Models.CartItem;
import com.example.thebedshop.Models.OrderItem;
import com.example.thebedshop.Models.CustomOrders;
import com.example.thebedshop.Models.Session;
import com.example.thebedshop.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    TextView TotalValue;
    EditText cardNumber;
    EditText cardHolderName;
    EditText address;
    EditText expiryDate;
    Button paySecureBtn;

    final String urlProductOrder = "http://10.0.3.2:8080/customOrders/add";

    Session session;
    List<CartItem> carts;
    double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        TotalValue = (TextView) findViewById(R.id.totalValue);
        paySecureBtn = (Button) findViewById(R.id.payBtn);
        cardNumber = findViewById(R.id.cardNumber);
        cardHolderName= findViewById(R.id.cardHolderName);
        address= findViewById(R.id.address);
        expiryDate= findViewById(R.id.expiryDate);

        Bundle bundle = getIntent().getExtras();
        carts = (ArrayList<CartItem>) bundle.getSerializable("cartArr");
        total = calTotal(carts);
        TotalValue.setText("Rs. "+Double.toString(total));


        session = new Session(this);

        paySecureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<OrderItem> productOrderItems = new ArrayList<OrderItem>();
                final CustomOrders customOrders = new CustomOrders();
                customOrders.setAddress(address.getText().toString());
                customOrders.setTotalAmount(total);
                customOrders.setEmail(session.getUserEmail()); // user hardcoded to 1 .......................................................

                for(CartItem cart:carts){
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cart.getProduct());
                    orderItem.setAmount(cart.getAmount());
                    productOrderItems.add(orderItem);
                }

                customOrders.setOrderItems(productOrderItems);

                Gson gson = new Gson();
                String Jsonorder = gson.toJson(customOrders);

                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(Jsonorder);

                JSONObject orderObject = null;
                try{
                    orderObject = new JSONObject(json.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                RequestQueue queue = Volley.newRequestQueue(CheckoutActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlProductOrder, orderObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println(response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(error.toString());
                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams()  {
                        Map<String, String> cartMap = new HashMap<>();
                        cartMap.put("userId", "1");
                        cartMap.put("address", customOrders.getAddress());
                        cartMap.put("totalAmount", Double.toString(customOrders.getTotalAmount()));
                        cartMap.put("productOrderItem", customOrders.getOrderItems().toString());

                        return cartMap;
                    }
                };

                queue.add(jsonObjectRequest);


            }
        });

    }

    public double calTotal(List<CartItem> cartArr){
        double total = 0;
        for(CartItem cart:cartArr){
            total += (cart.getProduct().getPrice() * cart.getAmount());
        }

        return total;
    }

}
