package com.example.sql;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText idd;
    private EditText nombre;
    private EditText precio;
    private Button guardar;
    private Button update;
    private Button delete;
    private Button volver;
    private ListView lv1;
    SQLiteDatabase db;

    ArrayList<String> lista = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        idd = (EditText) findViewById(R.id.id);
        nombre = (EditText) findViewById(R.id.bebida);
        precio = (EditText) findViewById(R.id.precio);
        guardar = (Button) findViewById(R.id.bt1);
        update = (Button) findViewById(R.id.bt2);
        volver = (Button) findViewById(R.id.bt3);
        delete = (Button) findViewById(R.id.bt4);
        lv1 = (ListView) findViewById(R.id.lv1);

        db = openOrCreateDatabase("almacen", Context.MODE_PRIVATE,null);
        String sql ="CREATE TABLE IF NOT EXISTS bebidas (id INTEGER PRIMARY KEY AUTOINCREMENT,"+"nombre TEXT, precio TEXT);";
        db.execSQL(sql);




        llenarListview();


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues vc = new ContentValues();

                vc.put("nombre", nombre.getText().toString());
                vc.put("precio", precio.getText().toString());

                db.insert("bebidas", null, vc);

                llenarListview();
                idd.setText("");
                nombre.setText("");
                precio.setText("");

                Toast.makeText(getApplicationContext(),"Guardado Correctamente", Toast.LENGTH_SHORT).show();
            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete("bebidas", "id=?", new String[]{idd.getText().toString()});
                llenarListview();
                idd.setText("");
                nombre.setText("");
                precio.setText("");
                Toast.makeText(getApplicationContext(),"Eliminado Correctamente", Toast.LENGTH_SHORT).show();}
        });

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idd.setText("");
                nombre.setText("");
                precio.setText("");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues vc = new ContentValues();
                vc.put("nombre", nombre.getText().toString());
                vc.put("precio", precio.getText().toString());

                db.update("bebidas",vc,"id=?",new String[]{idd.getText().toString()});


                idd.setText("");
                nombre.setText("");
                precio.setText("");

                Toast.makeText(getApplicationContext(),"Actualizado Correctamente", Toast.LENGTH_SHORT).show();

                llenarListview();
            }
        });}


        private void llenarListview(){
            try {
                db = openOrCreateDatabase("almacen", Context.MODE_PRIVATE,null);
                Cursor c = db.query("bebidas",null,null,null,null,null,"id DESC",null);
                int nreg = c.getCount();
                lv1 = findViewById(R.id.lv1);

                if(nreg != 0){
                    c.moveToFirst();
                    lista.clear();

                    do {
                        lista.add(c.getString(0).toString()+"\n"+
                                c.getString(1).toString()+"\n"+
                                c.getString(2).toString());
                    }while (c.moveToNext());
                }
            }catch (Exception e){

                e.toString();
            }
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista);

            lv1.setAdapter(adapter);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView z, View view, int position, long id) {

                    String itemSeleccionado = (String) z.getItemAtPosition(position);
                    String[] textoSeparado = itemSeleccionado.split("\n");

                    idd =findViewById(R.id.id);
                    nombre = findViewById(R.id.bebida);
                    precio = findViewById(R.id.precio);


                    idd.setText(textoSeparado[0].trim());
                    nombre.setText(textoSeparado[1].trim());
                    precio.setText(textoSeparado[2].trim());
                }
            });





}}