package hu.uniobuda.nik.turistapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //---helymeghatározás
    private GoogleMap mMap;
    private LocationManager locationManager;
    LatLng aktualis_poz;
    boolean helyzet_lekeres_aktiv = false;
    Marker mCurrLocation; // ahoz kell hogy mutassuk  a saját poziciónkat

    //morse kódhoz
    private String nyersszoveg; // Morse kódhoz a nyers bekért szöveg
    private String morsekod; // az átalakítot nyers szöveg
    private int index = 0; // morse kod index;
    private int segedformorse = 0; // ahoz kell mivel pl a "-" karakter 3 időgységet jelent a morzében ezt ezzel mérem
    Timer timer;// morzéhoz egy időzitő

    private Camera camera; // a vilogtatáshoz kell
    private boolean isFlashOn; // a vilogtatáshoz kell
    private boolean hasFlash; // a vilogtatáshoz kell
    Camera.Parameters params; // a vilogtatáshoz kell

    // adatbázishoz
    DBHandler dbHandler;//adatbázis
    EditText ciminput; //adatbázis felugró ablaka használja majd
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //------------------------------------------------------------------------------------------//
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.CAMERA},10);

            return;
        }*/
        getCamera();//////EZZEl INDITOm  AKEMRÁT LEHET NEM ITT KÉNE MJAD

        dbHandler = new DBHandler(this);// adatbázis meghívása

        FloatingActionButton morsebutton = (FloatingActionButton) findViewById(R.id.morsegomb); /// morse gomb

        FloatingActionButton mylocationbutton = (FloatingActionButton) findViewById(R.id.mylocationgomb); ///gps lekérés gomb

        FloatingActionButton addhelyek = (FloatingActionButton) findViewById(R.id.helyhozzaadasgomb); // helyek hozzáadása

        AppCompatImageButton helyeklistázása = (AppCompatImageButton) findViewById(R.id.helyeklistazasagomb); // helyek kilistázása gomb

        AppCompatImageButton turistajelek=(AppCompatImageButton) findViewById(R.id.turistajelzesekgomb); // turristajelzések gomb

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE); // pozicióhoz kell

        AppCompatImageButton segelyhivo = (AppCompatImageButton) findViewById(R.id.segelyhivo);

        //--------------------------------------------GOMB KEZELŐK----------------------------------------------//
        turistajelek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getBaseContext(), "TESZT", Toast.LENGTH_SHORT).show(); // ezt töröld majd ki és itt nyisd meg majd a turista jelzések ablakot
                Intent intent=new Intent(MapsActivity.this,jelekActivity.class);
                startActivity(intent);
            }
        });

        segelyhivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogHivas();


            }
        });


        addhelyek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMentes();
            }
        });

        helyeklistázása.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapsActivity.this, Kedvencek.class);
                startActivity(intent);

            }
        });

        morsebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogMorse();

                // ADatbázis feltöltés
                int id=getResources().getIdentifier("bazilika","drawable",getPackageName());
                dbHandler.addHelyek("Szent István Bazilika","47.5011151657#19.0531965145",Integer.toString(id));
                int id2=getResources().getIdentifier("parlament","drawable",getPackageName());
                dbHandler.addHelyek("Parlament","47.5071217#19.045669",Integer.toString(id2));
                int id3=getResources().getIdentifier("citadella","drawable",getPackageName());
                dbHandler.addHelyek("Citadella","47.4869897#19.046548",Integer.toString(id3));
                int id4=getResources().getIdentifier("szabadsagter","drawable",getPackageName());
                dbHandler.addHelyek("Szabadság tér","47.5036587#19.050836",Integer.toString(id4));


            }
        });

        mylocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helyzet_lekerese();
            }
        });
        //------------------------------------------------------------------------------------------//

        // Obtain the SupportMapFragment and get notified when the map is ready to be used. //Térképhez kell
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //-------------------------------------------------------------------------------------------//
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //-------------------------------------------------------------------------------------------//

    //----------------------Vegye dolgok--------------------------------------------////

    //--------------------------------Saját pozicióm-----------------------------------------------------------//
    private void helyzet_lekerese() // itt kérem le az aktuális poziciónkat
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},10);

            return;
        }
        if (helyzet_lekeres_aktiv == false) // ha megynomom a gombot akkor odaugrik az aktuális poziciónkra ha megnyomom mégegyszer akkor hagy minket barangolni
        {


            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isGPSEnabled && isNetworkEnabled) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener); // 0 perc mulva viszaál a heyére vagy 10 méter változása esetén
                helyzet_lekeres_aktiv = true;
            } else {
                Toast.makeText(getBaseContext(), "Nincs GPS vagy Internet kapcsolat!", Toast.LENGTH_SHORT).show();
            }
        } else {
            helyzet_lekeres_aktiv = false;
            locationManager.removeUpdates(locationListener);
        }
    }
    //-------------------------------------------------------------------------------------------//
    //  Telefonálás

    private void showDialogHivas() // segélyhívás ablaka
    {

        final Button hiv = new Button(this);
        TextView text1 = new TextView(this);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Segélyhívás");
        text1.setText("Biztos benne, hogy segítséget szeretne hívni?");



        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(text1);


        builder.setView(ll);




        builder.setPositiveButton("Igen", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                SegelyHivas();
            }
        });
        builder.setNegativeButton("Nem", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void SegelyHivas()
    {
        String phonenumber = "111111"; //ide jöhet valami segélyhívó
        if (!phonenumber.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(MapsActivity.this,new String[]{ Manifest.permission.CALL_PHONE}, 12);
                return;
            }
            startActivity(new Intent(
                    Intent.ACTION_CALL,
                    Uri.parse("tel:" + Uri.encode(phonenumber))
            ));
        }
    }




    //------------------------------Hely mentése-------------------------------------------------------------//
    private void showDialogMentes() // Adatbázis uj hely hozzáadaás ablaka
    {
        ciminput = new EditText(this); // textbox a dialog ablakon
        final Button kep = new Button(this);
        TextView text1 = new TextView(this);
        TextView text2 = new TextView(this);
        kep.setText("Kép Beszúrása");
        final Button kep2 = new Button(this);
        kep2.setText("Kép készítése");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hely Mentése");
        text1.setText("Kérem írja be a hely címét amivel el szeretné menteni a kedvencek közé!");
        builder.setView(ciminput);
        text2.setText("Ha szeretne itt tud képet hozáadni a helyhez!");
        builder.setView(kep);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(text1);
        ll.addView(ciminput);
        ll.addView(text2);
        ll.addView(kep);
        ll.addView(kep2);
        builder.setView(ll);

       kep.setOnClickListener(new View.OnClickListener() {   //ha rákattintunk a gombra, akkor megnyílik a galéria
           @Override
           public void onClick(View v) {
               Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(i, SELECTED_PICTURE);

           }
       });

        kep2.setOnClickListener(new View.OnClickListener() {   //ha rákattintunk a gombra, akkor megnyílik a kamera app
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       //         File file = getFile();
        //        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);
            }
        });


        builder.setPositiveButton("Küldés", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                addHelyek();
            }
        });
        builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });
        builder.show();
    }
    static final int CAM_REQUEST = 1313;



    public void addHelyek()// uj hely hozzáadása az adatbázishoz
    {
        //47.5011151657#19.0531965145"
        //int id=getResources().getIdentifier("bazilika","drawable",getPackageName());

        try{
        if(aktualis_poz.latitude != 0){
        String helyzetem=aktualis_poz.latitude+"#"+aktualis_poz.longitude; /// ezt
        int idseged=getResources().getIdentifier("ybpker","drawable",getPackageName()); // eztt
        String name = ciminput.getText().toString().trim();
        if (!name.isEmpty()) {
            long id = dbHandler.addHelyek(name, helyzetem, Integer.toString(idseged)); // varia
            lekeres();// ez
            Log.i("DB", "Hely id: " + id);
            //loadUser();
            }
        }} catch(Exception e) {


                Toast.makeText(getBaseContext(), "Fejlesztés alatt", Toast.LENGTH_SHORT).show();

        }
    }
    //-------------------------------------------------------------------------------------------//

   private static final int SELECTED_PICTURE=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK)
                {
                    Uri uri = data.getData();
                    String[] projection={MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int  columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath=cursor.getString(columnIndex);
                    cursor.close();


                    Bitmap kivalasztottkep = BitmapFactory.decodeFile(filePath);

                    Drawable d = new BitmapDrawable(kivalasztottkep);
                }
                break;

             case CAM_REQUEST:
                 if(requestCode==RESULT_OK) {
                     Bitmap kivkep = (Bitmap) data.getExtras().get("data");
                 }
                break;

            default:
                break;
        }

    }



    //--------------------------MORSe kód része-----------------------------------------------------------------//
    private void showDialogMorse() // morse kodolo dialog ablak
    {
        final EditText input = new EditText(this); // textbox a dialog ablakon
        input.setAllCaps(true);


        // itt állítom be hogy csak betűt fogadjon el
        input.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals("")) { // for backspace

                    return source;
                }
                if (source.toString().matches("[a-zA-Z ]+")) {

                    return source;
                }
                return "";// ha valaki rossz karaktert ír be törli a szöveget
            }
        }});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Morse kód");
        builder.setMessage("Kérem írja be a szöveget amit morse kódban szeretne elküldeni! Csak angol ABC!");
        builder.setView(input);
        builder.setPositiveButton("Küldés", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().toUpperCase(); // nagybetűkké alakítom
                // nyersszoveg.setValue(value); // mItem is a member variable in your Activity
                nyersszoveg = value; // a bekért szoveg elmentése a nyers szoveg változóba (LEHET NEM KELL MAJD
                dialog.dismiss(); // dialog bezárása
                //--------------------------------------------
                Morse morse = new Morse(nyersszoveg); // morse osztály hívása
                morsekod = morse.atalakaitas(); // ő végzi a morse kódra alakítást
                Morse_vilogtatas(); // ez végzi majd a leden való vilogtatást

                //teszt(); // tesztelésre szolgál csak
            }
        });
        builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void Morse_vilogtatas() {
        //itt adom meg hogy R=rövid 1 vilogás H=hosszú 3 időegységig világít és így tovább
        timer = new Timer(); // az időzitő a morzéhez hogy milyen gyorsan vilogjon.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (index < morsekod.length()) {
                    switch (morsekod.charAt(index)) {
                        case 'R':
                           // Log.i("szoveg", "."); //teszteléshez van csak
                            index++;
                            turnOnFlash();
                            break;
                        case 'H':
                            turnOnFlash();
                            if (segedformorse == 2) {
                                index++;
                                segedformorse = 0;
                              //  Log.i("szoveg", "-");
                            } else {
                                segedformorse++;
                            }
                            break;
                        case 'K':
                            turnOffFlash();
                            index++;
                            break;
                        case 'B':
                            turnOffFlash();
                            if (segedformorse == 2) {
                                index++;
                                segedformorse = 0;
                            } else {
                                segedformorse++;
                            }
                            break;
                        case 'V':
                            turnOffFlash();
                            if (segedformorse == 6) {
                                index++;
                                segedformorse = 0;
                              //  Log.i("szoveg", " ");
                            } else {
                                segedformorse++;
                            }
                            break;
                    }
                } else {
                    index = 0;
                    timer.cancel();
                    turnOffFlash();
                }
            }
        }, 0, 350);
    }
    // kamera inditása
    private void getCamera() {

        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (Exception e) {

            }
        }
    }
    // led bekapcsolása
    private void turnOnFlash() {

        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }
    // led kikapcsolása
    private void turnOffFlash() {

        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }
    //-------------------------------------------------------------------------------------------//

    //Keresés

    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location !=null  || !location.equals(""))
        {
            try {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker").title(location)

                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); //itt beállítom a marker helyét, címét, színét
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }catch (Exception e)
            {
                Toast.makeText(getBaseContext(), "Írjon be egy helyet", Toast.LENGTH_SHORT).show();
            }
        }


    }






    //------------------Térkép pozició lekérése------------------------------------------------------//
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //saját pozíciom lekérése
            aktualis_poz = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aktualis_poz, 14));
            //ha már lekértem a poziciómat de megváltozot az előző jelzést le kell szedni a térképről
            if (mCurrLocation != null) {
                mCurrLocation.remove();
            }
            //kék színű marker adása a térképhez
            MarkerOptions markeroption=new MarkerOptions();
            markeroption.title("Saját pozícíom!");
            markeroption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markeroption.position((aktualis_poz));
            mCurrLocation= mMap.addMarker(markeroption);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       lekeres();
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void lekeres()
    {
        Cursor cursor = dbHandler.loadHelyek();
        while (!cursor.isAfterLast()) {
           /* sb.append(cursor.getInt(0)).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("cim"))).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("kep"))).append(" - ")
                    .append(cursor.getString(cursor.getColumnIndex("gps"))).append("\n");// lehet indexel vagy colum name-el*/
            String cim =cursor.getString(cursor.getColumnIndex("cim"));
            String kep=cursor.getString(cursor.getColumnIndex("kep"));
            String gps=cursor.getString(cursor.getColumnIndex("gps"));
            //int kepint=(Integer.parseInt(kep));
            String [] darabok=gps.split("#");
            Double Lat=Double.parseDouble(darabok[0]);
            Double Lng=Double.parseDouble(darabok[1]);
            LatLng helypoz=new LatLng(Lat,Lng);

            Log.i("DB", "Hely id: " + cim+Lat+" "+ Lng+"\n");
            cursor.moveToNext();


            mMap.addMarker(new MarkerOptions().position(helypoz).title(cim));
        }

    }
    //------------------------------------------------------------------------------------------//

    //------------------------------------------------------------------------------------------//
    @Override
    protected void onResume() {
        super.onResume();
    }
    //------------------------------------------------------------------------------------------//

    //------------------------------------------------------------------------------------------//
    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        getCamera();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hu.uniobuda.nik.turistapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    //------------------------------------------------------------------------------------------//

    //------------------------------------------------------------------------------------------//
    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hu.uniobuda.nik.turistapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // camera leállítása
        if (camera != null) {
            camera.release();
            camera = null;
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }
}
