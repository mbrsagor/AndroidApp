package me.shagor.epathagarcom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
  Created by shagor on 9/18/2017.
 */

public class JobCircular extends Fragment{
    //Define element ID
    private String url;
    private ProgressDialog pDialog;
    private ListView listView;
    private List<MyModel> myModels = new ArrayList<MyModel>();
    private CustomListAdapter adapter;
    private SwipeRefreshLayout refresh_jobcircular;
    private AdView adView;
    private Config config;
    private boolean checkConnection;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.jobcircular,container,false);

        listView = (ListView) view.findViewById(R.id.listView_job);
        adapter = new CustomListAdapter(getContext(),myModels);
        listView.setAdapter(adapter);
        config = new Config();
        checkConnection = config.isNetworkAvailable(getContext());

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        url = config.url_link+"&cat=66";

        //Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // FetchData Method Call Internet connection
        if (checkConnection) {
            fetchData();
        }else {
            Intent intent = new Intent(getContext(),InternetConnection.class);
            startActivity(intent);
            getActivity().finish();
        }

        // SwipeRefreshLayout
        refresh_jobcircular = (SwipeRefreshLayout) view.findViewById(R.id.refresh_jobcircular);

        refresh_jobcircular.setColorSchemeResources(R.color.colorPrimary);

        refresh_jobcircular.setDistanceToTriggerSync(20);
        refresh_jobcircular.setSize(SwipeRefreshLayout.DEFAULT);
        refresh_jobcircular.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh_jobcircular.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh_jobcircular.setRefreshing(false);
                        fetchData();
                    }
                },200);
            }
        });

        return view;
    }

    // Call json form server
    public void fetchData() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();

                for (int i=0; i<response.length(); i++) {

                    try {

                        JSONObject obj = response.getJSONObject(i);
                        MyModel model = new MyModel();
                        model.setThumbnail(obj.getString("image"));
                        model.setPostID(obj.getString("postId"));
                        model.setTitle(obj.getString("title"));
                        model.setDate(obj.getString("date"));

                        myModels.add(model);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // Show data ListView
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity().getApplication(),Singlepage.class);
                        String PostID = myModels.get(position).getPostID();
                        intent.putExtra("KEY",PostID);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
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
                        refresh_jobcircular.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getNetworkTimeMs();
            }
        });
        // Adding request to request queue
        MySingleTon.getInstance(getContext()).addToRequestMethod(arrayRequest);
    }
}