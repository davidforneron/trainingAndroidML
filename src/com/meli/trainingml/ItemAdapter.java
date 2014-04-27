package com.meli.trainingml;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.meli.trainingml.items.Item;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter{
	private LayoutInflater inflater;
    private List<Item> items;
    private ViewHolder holder;
    private final static String LOGTAG = ItemAdapter.class.getSimpleName();



    public ItemAdapter(Activity activity, List<Item> items) {
        this.items = items;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_row, null);

            holder = new ViewHolder();
            holder.textTitle = (TextView) vi.findViewById(R.id.textTitle); // Title
            holder.textPrice = (TextView) vi.findViewById(R.id.textPrice); // Price
            holder.imageThumbnail = (ImageView) vi.findViewById(R.id.imgPreview); // Preview image
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        Item item = items.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textPrice.setText(item.getPrice());
        holder.imageThumbnail.setImageBitmap(item.getThumbnail());
            
        return vi;
    }

    /*
     * 
     * */
    static class ViewHolder {

        TextView textTitle;
        TextView textPrice;
        ImageView imageThumbnail;
    }

}
