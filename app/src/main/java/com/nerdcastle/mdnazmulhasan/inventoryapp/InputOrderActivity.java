package com.nerdcastle.mdnazmulhasan.inventoryapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;

public class InputOrderActivity extends Activity {
    int i = 0;
    List<EditText> allEds = new ArrayList<EditText>();
    List<String> allEdsData = new ArrayList<>();
    String brand;
    String id;
    String url;
    String data;
    String quantity;
    EditText et;
    JSONObject productData;
    String productId;
    String brandId;
    JSONObject submittedData=new JSONObject();
    JSONArray totalData;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_order);
        brand=getIntent().getStringExtra("brandName");
        id=getIntent().getStringExtra("id");
        createOrderWindow();

    }

    private void createOrderWindow() {
        data="brandId="+id;
        url = "http://dotnet.nerdcastlebd.com/Bazar/api/products?"+data;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                createDynamicForm(response);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(request);
    }

    private void createDynamicForm(JSONArray response) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.mainlayout);
        TextView brandName = new TextView(getApplicationContext());
        brandName.setTextSize(20.0f);
        brandName.setText("Brand" + ": " + brand);
        brandName.setTextColor(BLUE);
        brandName.setGravity(Gravity.CLIP_HORIZONTAL);
        ll.addView(brandName);
        for (int i = 0; i < response.length(); i++) {
            TextView tv = new TextView(getApplicationContext());
            try {
                tv.setText(response.getJSONObject(i).getString("ProductName"));
                tv.setTextColor(BLACK);
                ll.addView(tv);
                et = new EditText(getApplicationContext());
                et.setTextColor(BLACK);
                allEds.add(et);
                et.setId(i);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setTag(response.getJSONObject(i));
                et.setBackgroundResource(R.drawable.rounded_edittext);
                ll.addView(et);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Button submit_btn = new Button(getApplicationContext());
        submit_btn.setText("Submit");
        ll.addView(submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSubmit();
            }
        });
    }

    private void orderSubmit() {
        totalData=new JSONArray();
        String url="http://dotnet.nerdcastlebd.com/Bazar/api/orders";

        for(int i=0; i < allEds.size(); i++){
            quantity= allEds.get(i).getText().toString();
            try {
                if(quantity.length()!=0){
                    productData= (JSONObject) et.getTag();
                    productId=productData.getString("ProductId");
                    brandId=productData.getString("BrandId");
                    submittedData.put("SubmittedQuantity",quantity);
                    submittedData.put("ProductId",productId);
                    submittedData.put("BrandId",brandId);
                    totalData.put(submittedData);
                }
                Toast.makeText(getApplicationContext(), totalData.toString(), Toast.LENGTH_LONG).show();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, url, totalData, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}