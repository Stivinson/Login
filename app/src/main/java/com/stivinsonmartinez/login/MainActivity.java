package com.stivinsonmartinez.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private static final String TABLE = "contacts";
    private static final String COLUMN_PRODUCTNAME ="name" ;
    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    ImageView contactImageImgView;
    boolean flag=false;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    Uri imageUri = Uri.parse("android.resource://org.intracode.contactmanager/drawable/facebook.png");
    DatabaseHandler dbHandler;
    SQLiteDatabase BD;

    //Google
    private static final String TAG="MainActivity";
    private static final int RC_SIGN_IN=9001;
    private GoogleApiClient mGoogleApiClient;

    //Facebook
    /*private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // FacebookSdk.sdkInitialize(getApplicationContext());

       // loginButton.setReadPermissions("public_profile email");
       // callbackManager = CallbackManager.Factory.create();
        fromstart();
        nameTxt = (EditText) findViewById(R.id.et_name);
        phoneTxt = (EditText) findViewById(R.id.et_password);
        emailTxt = (EditText) findViewById(R.id.et_password);
        addressTxt = (EditText) findViewById(R.id.et_password);

        //google
        findViewById(R.id.id_sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //facebook

      /*  info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile email");*/

       /* if(AccessToken.getCurrentAccessToken()!=null){
            RequestData();
        }*/


       /* loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken()!=null) {
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });*/

        //contactListView = (ListView) findViewById(R.id.listView);
        //contactImageImgView = (ImageView) findViewById(R.id.imgViewContactImage);
        dbHandler = new DatabaseHandler(getApplicationContext());

    }

    /*private void RequestData() {
        GraphRequest request= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                if (json != null) {
                    Intent intent = new Intent(MainActivity.this,EnRutaActivity.class);
                    startActivity(intent);
                }
            }
        });
        Bundle parameters= new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }*/

    private void fromstart() {
        DatabaseHandler help = new DatabaseHandler(this);
        BD=help.getWritableDatabase();
    }


    public void registro (View v){
        Intent intent = new Intent(this,RegistroActivity.class);
        startActivity(intent);
    }
    public void start (View v){

        String[] columna={"name","phone"};
        String[] selectionArgs={nameTxt.getText().toString(),phoneTxt.getText().toString()};
        String selection="Select * FROM " + TABLE + " WHERE " + "name" + " =  \"" + nameTxt.getText().toString() + "\"";
        Cursor c=BD.rawQuery(selection,null);
        String selection2="Select * FROM " + TABLE + " WHERE " + "phone" + " =  \"" + phoneTxt.getText().toString() + "\"";
        Cursor c2=BD.rawQuery(selection2,null);
        String resultado="";
        String resultado2="";
        if (c.moveToFirst()==true && c2.moveToFirst()==true){
            do {
                for (int i =0; i<c.getColumnCount();i++){
                    resultado=resultado+c.getString(i);
                    resultado2=resultado2+c2.getString(i);
                }
            }while (c.moveToNext()==true && c2.moveToNext()==true);
            resultado="Bienvenido "+nameTxt.getText();
            Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();
            SharedPreferences setting = getSharedPreferences("Mipreferencia", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = setting.edit();
            editor.putString("name",nameTxt.getText().toString());
            editor.putString("phone",phoneTxt.getText().toString());
            editor.commit();
            Intent intent = new Intent(this,EnRutaActivity.class);
            startActivity(intent);
        }else{
            resultado="No esta registrado, verifque  que si este registrado o registrese";
            Toast.makeText(this,resultado.toString(),Toast.LENGTH_LONG).show();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode== RC_SIGN_IN){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if(result.isSuccess()){
           // GoogleSignInAccount acct = result.getSignInAccount();
           // mStatusUser.setText(getString(R.string.signed_in_fmt,acct.getDisplayName()));
           // mStatusEmail.setText(getString(R.string.signed_in_fmt2,acct.getEmail()));
            updateUI(true);

        }else{
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            Toast.makeText(MainActivity.this,"In",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,EnRutaActivity.class);
            startActivity(intent);
            //findViewById(R.id.id_sign_in_button).setVisibility(View.GONE);
           // findViewById(R.id.id_sign_out_button).setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(MainActivity.this,"In",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,EnRutaActivity.class);
            startActivity(intent);
            //Put into status view...
           // mStatusUser.setText(R.string.signed_out);
            //mStatusEmail.setText(R.string.signed_out2);
            //findViewById(R.id.id_sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.id_sign_out_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.id_sign_in_button: signIn();
                                        break;
            //case R.id.b_facebook: break;

            default: break;
        }

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG,"onConnectionFailded:" + connectionResult);

    }
}
