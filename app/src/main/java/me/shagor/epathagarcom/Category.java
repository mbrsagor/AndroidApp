package me.shagor.epathagarcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity {

    private static String url;
    private ListView listView;
    private CustomListAdapter adapter;
    private List<MyModel> myModels = new ArrayList<MyModel>();
    private ProgressDialog pDialog;
    private AdView adView;
    private SwipeRefreshLayout refreshLayout;
    private String catID;
    private Config config;
    private String appName;
    String cat_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView_job);
        adapter = new CustomListAdapter(getApplicationContext(),myModels);
        listView.setAdapter(adapter);


        catID = getIntent().getStringExtra("catId");
        appName = getIntent().getExtras().getString("app_name");
        setTitle(appName);

        config = new Config();
        url = config.url_link;
        cat_url = url+"&cat="+catID;

        //ProgressDialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        //Admob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //JsonRequest
        fetchData();

        //SwipeRefresh
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setDistanceToTriggerSync(20);
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        fetchData();
                    }
                },200);
            }
        });

    }


    public void fetchData() {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(cat_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();

                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        MyModel model = new MyModel();
                        model.setThumbnail(obj.getString("image"));
                        model.setPostID(obj.getString("postId"));
                        model.setTitle(obj.getString("title").toString());
                        model.setDate(obj.getString("date"));

                        myModels.add(model);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //ListView Data show & Get data Single page
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),Singlepage.class);
                        String postID = myModels.get(position).getPostID();
                        intent.putExtra("KEY",postID);
                        intent.putExtra("app_name", appName);
                        startActivity(intent);
                    }
                });

                // ScrollListener
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        int topRowVerticalPosition =
                                (listView == null || listView.getChildCount() == 0) ?
                                        0 : listView.getChildAt(0).getTop();
                        refreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                    }
                });


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
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_dev) {
            Intent intent = new Intent(getApplicationContext(),Aboutus.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        }

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }

        return true;
    }


}
