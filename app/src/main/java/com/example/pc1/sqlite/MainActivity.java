package com.example.pc1.sqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private TextView tvResultadoLogin;
    private EditText etUsuario,etContrasenia;
    private Button btnEntrar,btnRegistro;
    private String usuario,contrasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResultadoLogin=(TextView)findViewById(R.id.tvResultadoLogin);
        etUsuario=(EditText)findViewById(R.id.etUsuarioMainA);
        etContrasenia=(EditText)findViewById(R.id.etContraseniaMainA);
        btnEntrar=(Button)findViewById(R.id.btnEntrarMainA);
        btnRegistro=(Button)findViewById(R.id.btnRegistroMainA);
    }

    public void onClick(View view)
    {
        int id=view.getId();
        switch( id )
        {
            case R.id.btnRegistroMainA:
                Registrarse();
                break;
            case R.id.btnEntrarMainA:
                new LoginConsulta().execute(etUsuario.getText().toString(),etContrasenia.getText().toString());
                break;
            default:
                break;
        }
    }

    private void resultadoLogin(Boolean resultado)
    {
        if( resultado )
        {
            startActivity(new Intent(MainActivity.this,Activity2Menu.class));
        }
        else
        {
            tvResultadoLogin.setText(getString(R.string.passOusuarioIncorrectos));
        }
    }

    private void Registrarse()
    {
        startActivity(new Intent(MainActivity.this,RegistroUsuario.class));
    }

    private class LoginConsulta extends AsyncTask<String,Integer,Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings)
        {
            String usuario=strings[0],
                   contrasenia=strings[1];
            return consultaLogin(usuario,contrasenia);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            MainActivity.this.resultadoLogin(aBoolean);
        }
    }

    private Boolean consultaLogin(String usuario,String contrasenia)
    {
        HelperSQLite helper=new HelperSQLite(MainActivity.this);
        SQLiteDatabase db=helper.getReadableDatabase();

        if( db == null )
        {
            return false;
        }
        else
        {
            String usuarioDB,contraseniaDB;
            //Leemos de la base datos
            Cursor cursor=db.rawQuery(HelperSQLite.seletFrom(usuario),null);

            if( cursor.moveToFirst() )
            {
                usuarioDB=cursor.getString(3);
                contraseniaDB=cursor.getString(4);
                if( usuario.equals(usuarioDB) && contrasenia.equals(contraseniaDB) )
                    return true;
                else
                    return false;
            }
            else
            {
                return  false;
            }


        }
    }

}
