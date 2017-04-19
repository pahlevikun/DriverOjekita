package com.wensoft.driverojekita.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wensoft.driverojekita.pojo.Order;
import com.wensoft.driverojekita.R;

import java.util.List;

/**
 * Created by farhan on 3/9/17.
 */

public class OrderAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Order> categoryItems;

    public OrderAdapter(Activity activity, List<Order> categoryItems) {
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
            convertView = inflater.inflate(R.layout.adapter_order, null);

        //TextView title = (TextView) convertView.findViewById(R.id.text);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivorder);
        TextView tvBiaya = (TextView) convertView.findViewById(R.id.tvBiaya);
        TextView tvJarak = (TextView) convertView.findViewById(R.id.tvJarak);
        TextView tvAlamatJemput = (TextView) convertView.findViewById(R.id.tvJemput);
        TextView tvAlamatTujuan = (TextView) convertView.findViewById(R.id.tvTujuan);

        // getting movie data for the row
        Order m = categoryItems.get(position);
        if (m.getOrderType().equals("1")){
            imageView.setImageResource(R.drawable.ic_motor);
            //title.setText("Kita Jemput");
        }else if(m.getOrderType().equals("2")){
            imageView.setImageResource(R.drawable.ic_mobil);
            //title.setText("Mobil Kita");
        }else{
            imageView.setImageResource(R.drawable.ic_food);
            //title.setText("Kita Antar");
        }
        tvBiaya.setText("Rp. "+m.getBiaya());
        tvJarak.setText(m.getJarak()+" KM");
        tvAlamatJemput.setText(m.getAlamatJemput());
        tvAlamatTujuan.setText(m.getAlamatTujuan());

        return convertView;
    }
}
