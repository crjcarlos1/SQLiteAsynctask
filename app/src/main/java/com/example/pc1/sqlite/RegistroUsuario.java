package com.example.pc1.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroUsuario extends AppCompatActivity
{
    private TextView tvResultadoRegistro;
    private EditText etNombres,etApellidos,etEmail,
                     etRepiteEmail,etPassword,etRepetirPassword;
    private Button btnRegistrarse,btnCancelar;
    public Boolean varGlobalBoleana=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        tvResultadoRegistro=(TextView)findViewById(R.id.tvResultadoRegistro);
        etNombres=(EditText)findViewById(R.id.etNombresRegistro);
        etApellidos=(EditText)findViewById(R.id.etApellidosRegistro);
        etEmail=(EditText)findViewById(R.id.etEmailRegistro);
        etRepiteEmail=(EditText)findViewById(R.id.etRepetirEmailRegistro);
        etPassword=(EditText)findViewById(R.id.etPasswordRegistro);
        etRepetirPassword=(EditText)findViewById(R.id.etRepetirPasswordRegistro);
        btnCancelar=(Button)findViewById(R.id.btnCancelar_Registro);
        btnRegistrarse=(Button)findViewById(R.id.btnRegistro_Registro);
    }

    public void onClick(View view )
    {
        switch( view.getId() )
        {
            case R.id.btnRegistro_Registro:
                registralo();
                break;
            case R.id.btnCancelar_Registro:
                cancelarRegistro();
                break;
            default:
                break;
        }
    }

    private void registralo()
    {
        String nombres=etNombres.getText().toString(),
                apellidos=etApellidos.getText().toString(),
                nickname=etEmail.getText().toString(),
                nixkRepetido=etRepiteEmail.getText().toString(),
                contrasenia=etPassword.getText().toString(),
                contraseniaRepetida=etRepetirPassword.getText().toString();

        if( nombres.length()==0 || apellidos.length()==0 || nickname.length()==0
            || nixkRepetido.length()==0 || contrasenia.length()==0 || contraseniaRepetida.length()==0 )
        {
            tvResultadoRegistro.setText(getString(R.string.LlenarCampos));
        }
        else
        {
            if( nickIguales(nickname,nixkRepetido) && passIguales(contrasenia,contraseniaRepetida) )
                new RegistroA().execute(nombres,apellidos,nickname,contrasenia);
            else if( !nickIguales(nickname,nixkRepetido) )
                tvResultadoRegistro.setText(getString(R.string.nicksDiferentes));
            else if( !passIguales(contrasenia,contraseniaRepetida) )
                tvResultadoRegistro.setText(getString(R.string.contraseniasDiferentes));
        }
    }

    private Boolean passIguales(String pass1,String pass2)
    {
        if( pass1.equals(pass2) )
            return true;
        else
            return false;
    }

    private Boolean nickIguales(String nick1,String nick2)
    {
        if( nick1.equals(nick2) )
            return true;
        else
            return false;
    }

    private void seRegistro(Boolean x)
    {
        if( x )
            startActivity(new Intent(RegistroUsuario.this,Activity2Menu.class));
        else
            tvResultadoRegistro.setText(getString(R.string.noSeRegistro));
    }

    private class RegistroA extends AsyncTask<String,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(String... strings)
        {
            String nombres=strings[0],
                   apellidos=strings[1],
                   nickname=strings[2],
                   contrasenia=strings[3];

            HelperSQLite helper=new HelperSQLite(RegistroUsuario.this);
            SQLiteDatabase db=helper.getWritableDatabase();

            if( db == null )
            {
                return false;
            }
            else
            {
                long i;
                ContentValues valores=new ContentValues();
                valores.put(HelperSQLite.campo_nombres,nombres);
                valores.put(HelperSQLite.campo_apellidos,apellidos);
                valores.put(HelperSQLite.campo_correo,nickname);
                valores.put(HelperSQLite.campo_contrasenia,contrasenia);
                i=db.insert(HelperSQLite.nombre_tabla_usuarios,null,valores);
                if( i < 0 )
                    return false;
                else
                    return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            RegistroUsuario.this.seRegistro(aBoolean);
        }
    }

    private void cancelarRegistro()
    {
        startActivity(new Intent(RegistroUsuario.this,MainActivity.class));
    }
    

}
