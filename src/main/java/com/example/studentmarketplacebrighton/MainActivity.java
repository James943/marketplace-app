package com.example.studentmarketplacebrighton;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ListActivity {
    int[] itemIDs;
    String[] itemNames;
    String[] itemPrices;
    String[] itemDescriptions;
    String[] sellerNames;
    String[] sellerEmails;
    String[] sellerPhoneNumbers;
    String[] sellerLocations;
    public static Bitmap myBitmap[];

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting header
        TextView textView = new TextView(this);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setText("Items for sale");

        listView = findViewById(android.R.id.list);
        listView.addHeaderView(textView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                if (position !=0) {
                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putInt("itemID", itemIDs[position - 1]);
                    bundle.putString("itemName", itemNames[position - 1]);
                    bundle.putString("itemPrice", itemPrices[position - 1]);
                    bundle.putString("itemDescription", itemDescriptions[position - 1]);
                    bundle.putString("sellerName", sellerNames[position - 1]);
                    bundle.putString("sellerEmail", sellerEmails[position - 1]);
                    bundle.putString("sellerPhoneNumber", sellerPhoneNumbers[position - 1]);
                    bundle.putString("sellerLocation", sellerLocations[position - 1]);
                    bundle.putInt("position", position - 1);
                    //Add the bundle to the intent
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        // Request a string response from the web page
        StringRequest request = new StringRequest(Request.Method.GET, "https://jn494.brighton.domains/ci601/items.php",
                new Response.Listener()
                {
                    @Override
                    public void onResponse(Object response)
                    {
                        try {
                            JSONArray array = new JSONArray(response.toString());
                            itemIDs = new int[array.length()];
                            itemNames = new String[array.length()];
                            itemPrices = new String[array.length()];
                            itemDescriptions = new String[array.length()];
                            sellerNames = new String[array.length()];
                            sellerEmails = new String[array.length()];
                            sellerPhoneNumbers = new String[array.length()];
                            sellerLocations = new String[array.length()];

                            for (int i=0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                itemIDs[i] = object.getInt("itemID");
                                itemNames[i] = object.getString("itemName");
                                itemPrices[i] = object.getString("price");
                                itemDescriptions[i] = object.getString("description");
                                sellerNames[i] = object.getString("firstName") + " " + object.getString("lastName");
                                sellerEmails[i] = object.getString("email");
                                sellerPhoneNumbers[i] = object.getString("phoneNumber");
                                sellerLocations[i] = object.getString("location");
                            }
                            // Show the names and prices
                            CustomItemList customItemList = new CustomItemList(MainActivity.this, itemNames, itemPrices);
                            listView.setAdapter(customItemList);

                            downloadImages(array.length());
                        } catch (Exception e) { System.out.println("Error!!!" + e); }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

        // Initialise the RequestQueue & add the above request to the queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    public void downloadImages(int numberOfItems) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    myBitmap = new Bitmap[numberOfItems];
                    for (int i=0; i<numberOfItems; i++) {
                        URL url = new URL("https://jn494.brighton.domains/ci601/Images/item" + itemIDs[i] + ".jpg");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        myBitmap[i] = BitmapFactory.decodeStream(input);
                    }
                } catch (IOException e) { }
                // Show the new images
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     CustomItemList customItemList = new CustomItemList(MainActivity.this, itemNames, itemPrices);
                     listView.setAdapter(customItemList);
                    }
                });
            }
        }).start();
    }

}