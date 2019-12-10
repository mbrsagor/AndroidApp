package me.shagor.epathagarcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Singlepage extends AppCompatActivity {

    private static String url;
    private TextView single_title,detail,sharelink;
    private ImageView thumbnail;
    private AdView adView;
    private ProgressDialog pDialog;
    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        String appName = getIntent().getExtras().getString("title");
//        SpannableString app_name = new SpannableString(appName);
//        setTitle(app_name);

        single_title = (TextView) findViewById(R.id.single_title);
        detail = (TextView) findViewById(R.id.detail);
        sharelink = (TextView) findViewById(R.id.sharelink);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);

        //ProgressDialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        config = new Config();
        url = config.single_url;

        //Admob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Received Data form Intent
        String received_data = getIntent().getStringExtra("KEY");
        url = url+"&post="+received_data;

        // Fetch Data Method
        fetchData();
    }

    // fetchData form Json
    public void fetchData() {

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();

                for (int i=0; i<response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        single_title.setText(obj.getString("title"));
                        detail.setText(obj.getString("content"));
                        sharelink.setText(obj.getString("permalink"));
                        Picasso.with(getApplicationContext()).load(obj.getString("image")).into(thumbnail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        MySingleTon.getInstance(getApplicationContext()).addToRequestMethod(jsonRequest);
    }


    // Share Top Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.findItem(R.id.action_dev).setVisible(false);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = sharelink.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Epathager");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Where are you want to share?"));
                return true;
        }

        return true;
    }
}
