package com.wensoft.driverojekita.main.fragments.handle_profil;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.database.DatabaseHandler;

public class ChangeProfilActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPhone;
    private String nama, email, phone;
    private Button btSimpan;
    private DatabaseHandler dataSource;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etNama = (EditText) findViewById(R.id.editTextProfilName);
        etEmail = (EditText) findViewById(R.id.editTextProfilEmail);
        etPhone = (EditText) findViewById(R.id.editTextProfilPhone);
        btSimpan = (Button) findViewById(R.id.buttonUbah);

        dataSource = new DatabaseHandler(getApplicationContext());
        db = dataSource.getWritableDatabase();

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                phone = etPhone.getText().toString().trim();

                if(nama.length()<2&&email.length()<2&&phone.length()<6){
                    Toast.makeText(ChangeProfilActivity.this, "Isi dengan benar!", Toast.LENGTH_SHORT).show();
                }else{
                    db.execSQL("UPDATE fcm SET username ='"+nama+"', email='"+email+"',phone='"+phone+"' WHERE _id=0;");
                    db.close();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
