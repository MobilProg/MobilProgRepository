package hu.uniobuda.nik.turistapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Kedvencek extends AppCompatActivity {

    DBHandler dbHandler;//adatbázis
    ListView kedvencek; // EZT MAJD LISTÁVÁ KELL TENNI
    private HelyekAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kedvencek);

        dbHandler = new DBHandler(this);// adatbázis meghívása
        kedvencek=(ListView) findViewById(R.id.kedvenceklista); // list inicializálása
        loadHelyek();// helyek betöltése

    }

    public void loadHelyek()// helyek lekérdezése az adatbázisból
    {


        Cursor cursor = dbHandler.loadHelyek();
        StringBuilder sb = new StringBuilder();
        Helyek helyek;
        final ArrayList<Helyek> mindenhelyek=new ArrayList<Helyek>();

        while (!cursor.isAfterLast()) {
           /* sb.append(cursor.getInt(0)).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("cim"))).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("kep"))).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("gps"))).append("\n");// lehet indexel vagy colum name-el*/
            helyek=new Helyek(cursor.getString(cursor.getColumnIndex("cim")),
                    cursor.getString(cursor.getColumnIndex("kep")),
                    cursor.getString(cursor.getColumnIndex("gps")),
                    (cursor.getInt(0)));

            mindenhelyek.add(helyek);
            cursor.moveToNext();


        }
        adapter=new HelyekAdapter(mindenhelyek);
        kedvencek=(ListView) findViewById(R.id.kedvenceklista);
        kedvencek.setAdapter(adapter);
// HOSZAN NYOMVA LEHET TÖRLNI
        kedvencek.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Helyek torlendo = mindenhelyek.get(position);
                int torlendoindex = torlendo.getId();
                dbHandler.deleteHelyek(torlendoindex);
                mindenhelyek.remove(position);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

    }

    public void Torles()
    {

        Cursor cursor = dbHandler.loadHelyek();
        if(!cursor.isAfterLast()) {
            int torlendo = cursor.getInt(0);
            dbHandler.deleteHelyek(torlendo);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Törlés sikertelen! (nincs ilyen elem)", Toast.LENGTH_SHORT).show();

        }

        loadHelyek();

    }




}
