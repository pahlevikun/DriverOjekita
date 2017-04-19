package com.wensoft.driverojekita.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.pojo.History;
import com.wensoft.driverojekita.pojo.Order;

import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class HistoryAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<History> categoryItems;

    public HistoryAdapter(Activity activity, List<History> categoryItems) {
        this.activity = activity;
        this.categoryItems = categoryItems;
    }

    @Override
    public int getCount() {
        return categoryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return categoryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.adapter_history, null);

        TextView title = (TextView) convertView.findViewById(R.id.address);
        TextView invoice = (TextView) convertView.findViewById(R.id.invoice);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivorder);

        // getting movie data for the row
        History m = categoryItems.get(position);
        if (m.getOrderType().equals("1")){
            imageView.setImageResource(R.drawable.ic_motor);
        }else if(m.getOrderType().equals("2")){
            imageView.setImageResource(R.drawable.ic_mobil);
        }else{
            imageView.setImageResource(R.drawable.ic_food);
        }
        if(m.getStatus().equals("0")){
            status.setText("Belum Diambil");
        }else if (m.getStatus().equals("1")){
            status.setText("Sudah Diambil");
        }else {
            status.setText("Selesai");
        }
        invoice.setText(m.getIdUser());
        title.setText(m.getAddress());
        return convertView;
    }
}
