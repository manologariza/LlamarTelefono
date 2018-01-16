package com.example.dell.llamartelefono;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    private EditText etNumeroTelefono;
    private Button btnLlamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLlamar = (Button) findViewById(R.id.btnLlamar);
        etNumeroTelefono = (EditText) findViewById(R.id.etNumeroTelefono);

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeroTelefono = etNumeroTelefono.getText().toString();

                if (!TextUtils.isEmpty(numeroTelefono)) { //Si se ha escrito un número, trataremos de llamarlo
                    if (comprobarPermiso(Manifest.permission.CALL_PHONE)) {
                        String dial = "tel:" + numeroTelefono; //Se tiene que poner literalmente esto
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    } else {
                        Toast.makeText(MainActivity.this, "Permiso de llamada denegado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Escriba número de teléfono", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (comprobarPermiso(Manifest.permission.CALL_PHONE)) {
            btnLlamar.setEnabled(true);
        } else {
            btnLlamar.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }
    }


    private boolean comprobarPermiso(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    btnLlamar.setEnabled(true);
                    Toast.makeText(this, "Para llamar al número indicado, pulse el botón de llamada", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}
