package me.shagor.epathagarcom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
  Created by shagor on 10/17/2017.
 */

public class CustomListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<MyModel> modelList;
    private ImageLoader imageLoader;
    private int lastPosition = -1;
    private Context context;

    //Constractor
    public CustomListAdapter(Context context, List<MyModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_jobcurciular,null);

        if (imageLoader == null)
            imageLoader = MySingleTon.getInstance(context).getmImageLoader();

        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        thumbnail.setDefaultImageResId(R.drawable.default_img);
        thumbnail.setErrorImageResId(R.drawable.default_img);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        MyModel model = modelList.get(position);
        thumbnail.setImageUrl(model.getThumbnail(),imageLoader);
        id.setText(model.getPostID());
        title.setText(model.getTitle());
        date.setText(model.getDate());
        // This class use animation
        Animation animation = AnimationUtils.loadAnimation(convertView.getContext(),(position > lastPosition) ?R.anim.up_from_bottom:R.anim.down_from_top);
        convertView.startAnimation(animation);

        return convertView;
    }
}