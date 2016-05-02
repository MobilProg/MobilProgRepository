package hu.uniobuda.nik.turistapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bence on 2016.04.01..
 */
public class DBHandler {
    public  final  static String DB_NAME="adatbazis";
    public final  static  int DB_VERSION=1;


    private DBOpenHelper helper; //
    private Context context;

    public DBHandler(Context context) {
        this.context = context;
        this.helper=new DBOpenHelper(context);
    }

    public long addHelyek(String cim, String gps, String kep)
    {
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("cim", cim);// az első string a mezők a másodikok az adatok amiket beleírunk majd
        cv.put("kep", kep);
        cv.put("gps", gps);
        long id= db.insert("helyek",null,cv);
        db.close();
        return  id;

    }
    public Cursor loadHelyek()
    {
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.query("helyek", null, null, null, null, null, null);
        cursor.moveToFirst();
        db.close();
        return cursor;
    }


    public void  deleteHelyek (Integer id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM helyek WHERE _id ="+id+";");

    }


    // open helper implamentations
    public class DBOpenHelper extends SQLiteOpenHelper
    {

        public DBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE helyek (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cim VARCHAR(255)," + "kep VARCHAR(255),"+"gps VARCHAR(255)"+
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS helyek");
            onCreate(db);

        }
    }
}