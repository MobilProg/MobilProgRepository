package hu.uniobuda.nik.turistapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class jelekActivity extends AppCompatActivity {
    private RecyclerView ujrahasznosito;
    private RecyclerView.LayoutManager lmanager;
    private RecyclerView.Adapter adapterke;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jelek_main);
        ArrayList<informaciok> adatok = new ArrayList<informaciok>();
        adatok.add(new informaciok(R.drawable.kek, "Fontossági sorrendben", "a kék sávjelzés az országos jelentőségű, a piros sávjelzést legalább egy hegységen átvezető utak jelzése, a sárga és a zöld sávjelzést pedig az előbbieken kívül, helyi jelentőségű, hegységi vándorutakat jelzi"));
        adatok.add(new informaciok(R.drawable.piros, "Sáv jelzés", "a fontosabb kiindulási pontoktól (pl. vasút, hajó és autóbusz megállóhelyektől) más hasonlóan fontos pontokig terjedő és a hegységeken (tájegységeken) keresztül vezető turistautak jelzésére szolgál."));
        adatok.add(new informaciok(R.drawable.kereszt, "Kereszt jelzés", "olyan összekötő (ill. rövidítő) utat jelez, amely általában sávjelzéstől sávjelzésig vezet. Ezt a fajta jelzést valamilyen fontosabb ponthoz (pl.megállóhelyhez) vezető útjelzésére is alkalmazhatják "));
        adatok.add(new informaciok(R.drawable.negyzet, "Négyzet jelzés", " általában a sávjelzésekből kiinduló és szálláshelyre, lakott településre vezető, ill. az azoktól jövő utakat jelöli"));

        adatok.add(new informaciok(R.drawable.kor, "Kör jelzés", " forrásokhoz (kutakhoz, ill. egyéb ivóvízvételi helyhez) vezető, ill. az azoktól jövő rövid (legfeljebb 2 km) utakat jelöl"));
        adatok.add(new informaciok(R.drawable.haromszog, "Háromszög jelzés", "a hegytetőkre (csúcsokra), kilátópontokhoz (kilátótornyokhoz) vezető utakat jelölik."));

        adatok.add(new informaciok(R.drawable.lalak, "L alakú jelzés", "a romokhoz, műemlékekhez, földvársáncokhoz vezető, ill. az azoktól jövő rövid (legfeljebb 2 km) utakat jelzi."));
        adatok.add(new informaciok(R.drawable.omega, "Omega jelzés", "a barlangokhoz vezető rövid (legfeljebb 2 km) utakat jelölik. Ez a jelzés is többnyire más, azonos színű jelzésből indul ki "));
        adatok.add(new informaciok(R.drawable.vissza, "Körséta jelzés", "rövidebb kirándulásokra alkalmas, a kiindulópontba (pl. parkolóhely) jelzésváltás nélkül visszavezető útvonalakat jelölik"));


        ujrahasznosito = (RecyclerView) findViewById(R.id.ujrahasznos);
        lmanager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ujrahasznosito.setLayoutManager(lmanager);

        adapterke = new infoAdapter(adatok);
        ujrahasznosito.setAdapter(adapterke);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
