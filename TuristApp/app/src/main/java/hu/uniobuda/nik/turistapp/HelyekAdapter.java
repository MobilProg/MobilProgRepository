package hu.uniobuda.nik.turistapp;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Bence on 2016.04.04..
 */
public class HelyekAdapter extends BaseAdapter {
    private ArrayList<Helyek> items;


    public HelyekAdapter(ArrayList<Helyek> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Helyek helyek=items.get(position);
        ViewHolder holder;

        if(convertView==null)
        {
            convertView=View.inflate(parent.getContext(),R.layout.listitem_helyek,null);
            holder=new ViewHolder();
            holder.cim=(AppCompatTextView) convertView.findViewById(R.id.lista_cim);
            holder.id=(AppCompatTextView) convertView.findViewById(R.id.lista_id);
            holder.kep=(AppCompatImageView) convertView.findViewById(R.id.lista_kep);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }
        String segitseg=String.valueOf(helyek.getId());

        holder.id.setText(segitseg);
        holder.cim.setText(helyek.getCim());

        holder.kep.setImageResource(Integer.parseInt(helyek.getKep()));



        return  convertView;

    }

    class ViewHolder
    {
        AppCompatTextView cim, id;
        AppCompatImageView kep;


    }
}

