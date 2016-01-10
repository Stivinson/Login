package com.stivinsonmartinez.login;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class EnRutaActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN=9001;
    private GoogleApiClient mGoogleApiClient;

    ListView listMenu;
    int opc=0;
    private Lista_Menu[] menu=
            new Lista_Menu[]{
                    new Lista_Menu(R.drawable.underground,"Sistema Metro"),
                    new Lista_Menu(R.drawable.bus48,"Rutas medellin"),
                    new Lista_Menu(R.drawable.bike15,"Encicla"),
                    new Lista_Menu(R.drawable.globe_50,"Noticias"),
                    new Lista_Menu(R.drawable.logoenruta,"Acerca de ...")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        Adapter adapter=new Adapter(this,menu);
        listMenu=(ListView)findViewById(R.id.listview);
        listMenu.setAdapter(adapter);

        //google

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    public void signout (View v){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                }
        );
    }

    private void updateUI(boolean b) {
            if (b) {

            } else {
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
            }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    public class Adapter extends ArrayAdapter<Lista_Menu> {

        public Adapter(Context context, Lista_Menu[] datos) {
            super(context, R.layout.activity_main, datos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.activity_ruta2, null);


            ImageView imagen = (ImageView) item.findViewById(R.id.imagen);
            imagen.setImageResource(menu[position].getIdImagen());

            TextView informacion = (TextView) item.findViewById(R.id.tinformacion);
            informacion.setText(menu[position].getOpcion());

            return item;
        }
    }
}
