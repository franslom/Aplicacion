package com.example.francisuni.franblocdenotas;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import java.net.CookieManager;

/**
 * Created by Franci Suni on 04/11/15.
 */
public class AgregarNota extends ActionBarActivity {

    String type,getTitle;
    Button Add;
    EditText TITLE,CONTENT;
    private static final int SALIR= Menu.FIRST;
    AdaptadorBD DB;
    private MyService mMusica;


    private ServiceConnection mMusicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.MusicBinder binder = (MyService.MusicBinder) service;
            mMusica = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //mMusicBound = false;
        }
    };
















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_nota);

       // mMusica.playMusic();


        Add=(Button)findViewById(R.id.button_Add);
        TITLE=(EditText)findViewById(R.id.editText_Titulo);
        CONTENT=(EditText)findViewById(R.id.editText_Nota);
        Bundle bundle=this.getIntent().getExtras();
        String content;
        getTitle=bundle.getString("title");
        content=bundle.getString("content");
        type=bundle.getString("type");

        if (type.equals("add")){
            Add.setText("Add nota");

        }else {
            if (type.equals("edit")){
                TITLE.setText(getTitle);
                CONTENT.setText(content);
                Add.setText("Update nota");
            }
        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUpdateNotes();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu);

        menu.add(1, SALIR, 0, R.string.menu_salir);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        switch (id){
            case SALIR:
                CookieSyncManager.createInstance(this);
                CookieManager cookieManager= CookieManager.getInstance();
                cookieManager.removeAllCookie();
                Intent intent=new Intent(AgregarNota.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addUpdateNotes(){
        DB=new AdaptadorBD(this);
        String title,content,msj;
        title=TITLE.getText().toString();
        content=CONTENT.getText().toString();
        if (type.equals("add")){
            if (title.equals("")){
                msj="Ingrese un titulo";
                TITLE.requestFocus();
                Mensaje(msj);
            }else {
                if (content.equals("")){
                    msj="Ingrese una nota";
                    CONTENT.requestFocus();
                    Mensaje(msj);
                }else {
                   Cursor c=DB.getNote(title);
                    String gettitle="";
                    if (c.moveToFirst()){
                        do{
                            gettitle=c.getString(1);
                        }while (c.moveToNext());
                    }
                    if (gettitle.equals(title)){
                        TITLE.requestFocus();
                        msj="El titulo de la nota ya existe";
                        Mensaje(msj);
                    }else {
                        DB.addNote(title,content);
                        actividad(title,content);
                    }
                }

            }
        }else {
            if (type.equals("edit")){
                Add.setText("Actualizar nota");
                if (type.equals("")){
                    msj="Ingrese un title";
                    TITLE.requestFocus();
                    Mensaje(msj);
                }else {
                    if (content.equals("")){
                        msj="Ingrese la nota";
                        CONTENT.requestFocus();
                        Mensaje(msj);
                    }else {
                        DB.updateNote(title,content,getTitle);
                        actividad(title,content);
                    }
                }
            }
        }
    }

    public void Mensaje(String msj){
        Toast toast=Toast.makeText(this, msj, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    public void actividad(String title, String content){
        Intent intent= new Intent(AgregarNota.this, VerNota.class);
        intent.putExtra("title",title);
        intent.putExtra("content",content);
        startActivity(intent);

    }




}
