package teon.movietoday;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class GeneralActivity extends AppCompatActivity {

    String DB_PATH = "/data/data/teon.movietoday/databases/";
    String loc;
    String th;
    String mov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        //資料庫
        AssetManager access = getAssets();
        byte [] buffer = new byte [1024];
        int length;
        InputStream dbInputStream = null;
        OutputStream dbOutputStream = null;
        //move the db to the designated path
        if ((new File(DB_PATH + "movie.sqlite")).exists() == false) {
            File f = new File(DB_PATH);
            if (!f.exists()) {
                f.mkdir();
            }
        }

        try {
            dbInputStream = access.open("movie.sqlite");
            dbOutputStream = new FileOutputStream(DB_PATH + "movie.sqlite");
            while ((length = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer, 0, length);
            }
            dbOutputStream.flush();
            dbOutputStream.close();
            dbInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        Spinner location = (Spinner)findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> aAdapter = ArrayAdapter.createFromResource(
                this, R.array.theater_array, android.R.layout.simple_spinner_item );
        aAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(aAdapter);
        location.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                locationinfo();
                return false;
            }

        });

        Spinner theater = (Spinner)findViewById(R.id.theater_spinner);
        ArrayAdapter<CharSequence> bAdapter = ArrayAdapter.createFromResource(
                this, R.array.theater_array, android.R.layout.simple_spinner_item );
        bAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        theater.setAdapter(bAdapter);

        Spinner movie = (Spinner)findViewById(R.id.movie_spinner);
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(
                this, R.array.movie_array, android.R.layout.simple_spinner_item );
        cAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        movie.setAdapter(cAdapter);


    }

    public void locationinfo(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH+ "movie.sqlite", null);
        //db.execSQL("SELECT * FROM movietime");

        //抓資料
        MyDBHelper helper = new MyDBHelper(this, "movie.sqlite", null, 1);
        //String query = "SELECT DISTINCT `location` AS `_id` FROM movietime;";
        String query = "SELECT MIN(_id) AS _id, location FROM movietime GROUP BY location ORDER BY MIN(_id);";
        Cursor c = db.rawQuery(query,null);
        //Cursor c = db.query(true,"movietime",new String[] {"location","_id"} , null, null, null, null, null,null);

        //spinner
        final Spinner location = (Spinner)findViewById(R.id.location_spinner);
        SimpleCursorAdapter aAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item ,c, new String[] { "location" }, new int[] {android.R.id.text1});
        aAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(aAdapter);
        location.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                location.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                        Cursor cursor = (Cursor)location.getSelectedItem();
                        loc = cursor.getString(cursor.getColumnIndex("location"));
                        Log.e("onItemSelect",loc);
                        theaterinfo();
                    }

                    public void onNothingSelected(AdapterView arg0) {
                        Toast.makeText(GeneralActivity.this, " 您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

        });

    }
    public void theaterinfo(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH+ "movie.sqlite", null);
        //db.execSQL("SELECT * FROM movietime");

        //抓資料
        MyDBHelper helper = new MyDBHelper(this, "movie.sqlite", null, 1);
        //String query = "SELECT DISTINCT `location` AS `_id` FROM movietime;";
        String query = "SELECT MIN(_id) AS _id, theater FROM movietime where location = '"+loc+"' GROUP BY theater ORDER BY MIN(_id);";
        Cursor c = db.rawQuery(query,null);
        //Cursor c = db.query(true,"movietime",new String[] {"location","_id"} , null, null, null, null, null,null);

        //spinner
        final Spinner theater = (Spinner)findViewById(R.id.theater_spinner);
        SimpleCursorAdapter aAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item ,c, new String[] { "theater" }, new int[] {android.R.id.text1});
        aAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        theater.setAdapter(aAdapter);
        theater.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                Cursor cursor = (Cursor)theater.getSelectedItem();
                th = cursor.getString(cursor.getColumnIndex("theater"));
                movieinfo();
            }

            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GeneralActivity.this, " 您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void movieinfo(){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH+ "movie.sqlite", null);
        //db.execSQL("SELECT * FROM movietime");

        //抓資料
        MyDBHelper helper = new MyDBHelper(this, "movie.sqlite", null, 1);
        //String query = "SELECT DISTINCT `location` AS `_id` FROM movietime;";
        String query = "SELECT MIN(_id) AS _id, film_name FROM movietime where theater = '"+th+"' GROUP BY film_name ORDER BY MIN(_id);";
        Cursor c = db.rawQuery(query,null);
        //Cursor c = db.query(true,"movietime",new String[] {"location","_id"} , null, null, null, null, null,null);

        //spinner
        final Spinner movie = (Spinner)findViewById(R.id.movie_spinner);
        SimpleCursorAdapter aAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item ,c, new String[] { "film_name" }, new int[] {android.R.id.text1});
        aAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        movie.setAdapter(aAdapter);
        movie.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id){
                Cursor cursor = (Cursor)movie.getSelectedItem();
                mov = cursor.getString(cursor.getColumnIndex("film_name"));
                Log.e("Location",loc);
                Log.e("Theater",th);
                Log.e("Movie",mov);
            }

            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GeneralActivity.this, " 您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void submit(View view) {
        Intent intent = new Intent(this, GeneralResultActivity.class);
        startActivity(intent);
    }
}
