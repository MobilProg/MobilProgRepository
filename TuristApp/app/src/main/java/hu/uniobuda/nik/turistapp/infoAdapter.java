package hu.uniobuda.nik.turistapp;

/**
 * Created by András on 2016.04.22..
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Trujas on 05/08/2015.
 */
public class infoAdapter extends RecyclerView.Adapter<infoAdapter.informacioViewHolder>{
    private ArrayList<informaciok> item;

    public infoAdapter(ArrayList<informaciok> item) {
        this.item = item;
    }

    @Override
    public informacioViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycinfok,viewGroup,false);
        informacioViewHolder inform= new informacioViewHolder(v);
        return inform;
    }

    @Override
    public void onBindViewHolder(informacioViewHolder informacioViewHolder, int i) {
        informacioViewHolder.nev.setText(item.get(i).getNev_p());
        informacioViewHolder.leiras.setText(item.get(i).getLeiras_p());
        informacioViewHolder.image.setImageResource(item.get(i).getImage_p());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class informacioViewHolder extends RecyclerView.ViewHolder{
        TextView nev, leiras;
        ImageView image;

        public informacioViewHolder(View itemView) {
            super(itemView);

            nev =(TextView)itemView.findViewById(R.id.lblNev);
            leiras =(TextView)itemView.findViewById(R.id.lblLeiras);
            image =(ImageView)itemView.findViewById(R.id.imgInfo);

        }
    }

}
