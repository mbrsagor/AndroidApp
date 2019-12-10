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

public class Education extends Fragment  {

    //Define element ID
    private String url;
    private ProgressDialog pDialog;
    private ListView listView;
    private List<MyModel> myModels = new ArrayList<MyModel>();
    private CustomListAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private Config config;
    private AdView adView;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.education,container,false);

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getContext(),myModels);
        listView.setAdapter(adapter);

        //ProgressDialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        config = new Config();
        url = config.url_link+"&cat=43";

        //Admob
        MobileAds.initialize(getContext(), "ca-app-pub-3891111394050528/3779562567");
        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //JsonArrayRequest
        fetchData();

        //SwipeRefresh
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
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


        return view;
    }

    public void fetchData() {

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pDialog.dismiss();

                for (int i=0; i<response.length(); i++){
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
                adapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity().getApplication(),Singlepage.class);
                        String postID = myModels.get(position).getPostID();
                        intent.putExtra("KEY",postID);
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

        MySingleTon.getInstance(getContext()).addToRequestMethod(jsonRequest);

    }

}
