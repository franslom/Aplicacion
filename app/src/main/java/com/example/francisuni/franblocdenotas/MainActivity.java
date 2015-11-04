package com.example.francisuni.franblocdenotas;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int ADD=Menu.FIRST;
    private static final int DELETE=Menu.FIRST+1;
    private static final int EXIST=Menu.FIRST+2;
    ListView lista;
    TextView textLista;
    AdaptadorBD DB;
    List<String> item=null;
    String getTitle;

   // private MediaPlayer reproductor1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*reproductor1=MediaPlayer.create(this,R.raw.udo);
        reproductor1.setLooping(true);
        reproductor1.start();*/

        textLista = (TextView)findViewById(R.id.textView_Lista);
        lista=(ListView)findViewById(R.id.listView_Lista);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTitle=(String) lista.getItemAtPosition(position);
                alert("list");
            }
        });
        showNotes();



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        menu.add(1, ADD, 0, R.string.menu_crear);
        menu.add(2,DELETE,0,R.string.menu_borrar_todas);
        menu.add(3, EXIST, 0, R.string.menu_salir);
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case ADD:
                actividad("add");
            return true;
            case DELETE:
                alert("deletes");
                return true;
            case EXIST:
                finish();
                return true;
            default:
                return  super.onOptionsItemSelected(item);

        }
    }

    public void showNotes(){
        DB=new AdaptadorBD(this);
        Cursor c=DB.getNotes();
        item=new ArrayList<String>();
        String title="";
        if (c.moveToFirst()==false){
            textLista.setText("No hay notas");
        }else {
            do {
                title=c.getString(1);
                item.add(title);

            }while (c.moveToNext());
        }
        ArrayAdapter<String> adaptador=
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,item);
        lista.setAdapter(adaptador);
    }

    public String getNote(){
        String type="",content="";
        DB=new AdaptadorBD(this);
        Cursor c= DB.getNote(getTitle);
        if (c.moveToFirst()){
            do {
                content=c.getString(2);
            }while (c.moveToNext());
        }
        return content;
    }

    public void actividad(String act){
        String type="",content="";
        if (act.equals("add")){
            type="add";
            Intent intent=new Intent(MainActivity.this,AgregarNota.class);
            intent.putExtra("type",type);
            startActivity(intent);
        }else {
            if (act.equals("edit")){
                type="edit";
                content=getNote();
                Intent intent=new Intent(MainActivity.this,AgregarNota.class);
                intent.putExtra("type",type);
                intent.putExtra("title",getTitle);
                intent.putExtra("content",content);
                startActivity(intent);
            }else {
                if (act.equals("see")){
                    content=getNote();
                    Intent intent=new Intent(MainActivity.this,VerNota.class);
                    intent.putExtra("title",getTitle);
                    intent.putExtra("content",content);
                    startActivity(intent);
                }
            }
        }
    }

    private void alert(String f){
        AlertDialog alerta;
        alerta=new AlertDialog.Builder(this).create();
        if (f.equals("list")){
            alerta.setTitle("Titulo de la nota: "+getTitle);
            alerta.setMessage("¿Que acción desea realizar?");
            alerta.setButton("Ver nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("see");
                }
            });
            alerta.setButton2("Eliminar nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    delete("delete");
                    Intent intent = getIntent();
                    startActivity(intent);

                }
            });
            alerta.setButton3("Editar nota", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    actividad("edit");
                }
            });
        }else {
            if (f.equals("deletes")){
                alerta.setTitle("Mensaje de Confirmación");
                alerta.setMessage("¿Que acción desea realizar?");
                alerta.setButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alerta.setButton2("Eliminar notas", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete("deletes");
                        Intent intent =getIntent();
                        startActivity(intent);
                    }
                });
            }
        }
        alerta.show();
    }

    private void delete(String f){
        DB=new AdaptadorBD(this);
        if (f.equals("delete")){
            DB.deleteNote(getTitle);
        }else {
            if (f.equals("deletes")){
                DB.deleteNotes();
            }
        }
    }


}
