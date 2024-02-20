package com.example.studentmarketplacebrighton;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CALL_PHONE;

public class SecondActivity extends AppCompatActivity {
    ImageView image;
    TextView name;
    TextView description;
    TextView seller;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        image = findViewById(R.id.image);
        image.setImageBitmap(MainActivity.myBitmap[getIntent().getIntExtra("position",1)]);

        name = findViewById(R.id.textViewName);
        String nameMsg = getIntent().getStringExtra("itemName");
        String priceMsg = getIntent().getStringExtra("itemPrice");
        name.setText(String.format("%s £%s", nameMsg, priceMsg));

        description = findViewById(R.id.textViewDescription);
        String descriptionMsg = getIntent().getStringExtra("itemDescription");
        description.setText(descriptionMsg);

        seller = findViewById(R.id.textViewSeller);
        String sellerNameMsg = getIntent().getStringExtra("sellerName");
        String sellerEmailMsg = getIntent().getStringExtra("sellerEmail");
        String sellerPhoneNumberMsg = getIntent().getStringExtra("sellerPhoneNumber");
        String sellerLocationMsg = getIntent().getStringExtra("sellerLocation");
        seller.setText(String.format("---Seller contact details---\nName:%s\nEmail: %s\nPhone Number: %s\nLocation: %s", sellerNameMsg, sellerEmailMsg, sellerPhoneNumberMsg, sellerLocationMsg));
    }

    public void buttonEmailClick(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getIntent().getStringExtra("sellerEmail")});
        i.putExtra(Intent.EXTRA_SUBJECT, "Buying item " + getIntent().getStringExtra("itemName"));
        i.putExtra(Intent.EXTRA_TEXT   , "Hi, I am interested in buying your " + getIntent().getStringExtra("itemName") + " that you listed on the Student Market Brighton for £" + getIntent().getStringExtra("itemPrice"));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SecondActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonCallClick(View view) {
        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + getIntent().getStringExtra("sellerPhoneNumber")));
        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(i);
        } else {
            requestPermissions(new String[]{CALL_PHONE}, 1);
        }
    }

}
