package com.gakdevelopers.itsafact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.banner_ad));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CardData[] cardData = new CardData[] {
                new CardData("Random", R.drawable.random),
                new CardData("Healthy", R.drawable.healthy),
                new CardData("Science", R.drawable.science),
                new CardData("Historical", R.drawable.historical),
                new CardData("Creepy", R.drawable.creepy),
                new CardData("Animals", R.drawable.animals),
        };

        CardAdapter cardAdapter = new CardAdapter(cardData, MainActivity.this);
        recyclerView.setAdapter(cardAdapter);

    }
}