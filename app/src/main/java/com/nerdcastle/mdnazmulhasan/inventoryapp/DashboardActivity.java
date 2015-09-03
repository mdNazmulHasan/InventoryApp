package com.nerdcastle.mdnazmulhasan.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class DashboardActivity extends AppCompatActivity {
    ArrayList<String>orderList=new ArrayList<>();
    ListView listView;
    int dataUpto;
    boolean more=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //View header = getLayoutInflater().inflate(R.layout.header, null);
         listView = (ListView) findViewById(R.id.listView);
        //listView.addHeaderView(header);
        dataRequest();

    }

    public void dataRequest() {


        String url="http://dotnet.nerdcastlebd.com/Bazar/api/orders";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(more){
                    dataUpto=response.length();
                }
                else{
                    dataUpto=5;
                }

                try {
                    for(int i=0;i<dataUpto;i++){
                        JSONObject singleData= response.getJSONObject(i);
                        String orderNo=singleData.getString("OrderNo");
                        String date=singleData.getString("Date");
                        String status=singleData.getString("Status");
                        String singleOrder=orderNo+"      "+date+"      "+status;
                        orderList.add(singleOrder);
                    }
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<>(getApplicationContext(), R.layout.each_row_dashboard,R.id.orderTextView,orderList);
                    listView.setAdapter(itemsAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
    public void moreData(View view){
        more=true;
        dataRequest();

    }

}
