package com.meli.trainingml;

import java.util.List;
import com.meli.trainingml.items.Item;
import android.app.Activity;
import android.content.Context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_item_row, null);

            holder = new ViewHolder();
            holder.textTitle = (TextView) vi.findViewById(R.id.textTitle); // Title
            holder.textPrice = (TextView) vi.findViewById(R.id.textPrice); // Price
            holder.textAddress = (TextView) vi.findViewById(R.id.textAddress); // Address
            holder.textCondition = (TextView) vi.findViewById(R.id.textCondition); // Condition
            holder.imageThumbnail = (ImageView) vi.findViewById(R.id.imgThumbnail); // Preview image
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        Item item = items.get(position);
        holder.textTitle.setText(item.getTitle());
        holder.textPrice.setText(item.getPrice());
        holder.imageThumbnail.setImageBitmap(item.getThumbnail());
        if(holder.textAddress != null) {
        	holder.textAddress.setText(item.getAddress());
        }
        if(holder.textCondition != null) {
        	holder.textCondition.setText(item.getCondition());
        }
        return vi;
    }
    
    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * 
     * */
    static class ViewHolder {

        TextView textTitle;
        TextView textPrice;
        TextView textAddress;
        TextView textCondition;
        ImageView imageThumbnail;
    }

}
