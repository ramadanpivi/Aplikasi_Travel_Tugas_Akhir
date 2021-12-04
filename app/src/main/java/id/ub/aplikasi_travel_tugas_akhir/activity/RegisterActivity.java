package id.ub.aplikasi_travel_tugas_akhir.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import id.ub.aplikasi_travel_tugas_akhir.R;
import id.ub.aplikasi_travel_tugas_akhir.adapter.AlertDialogManager;
import id.ub.aplikasi_travel_tugas_akhir.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    EditText txtName, txtEmail, txtPassword;
    Button btnDaftar, btnKeLogin;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String name, username, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mAuth = FirebaseAuth.getInstance();
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        txtName = findViewById(R.id.reg_nama);
        txtEmail = findViewById(R.id.reg_email);
        txtPassword = findViewById(R.id.reg_password);

        btnDaftar = findViewById(R.id.daftar);
        btnKeLogin = findViewById(R.id.ke_login);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = txtName.getText().toString();
                username = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                try {
                    if (username.trim().length() > 0 && password.trim().length() > 0 && name.trim().length() > 0) {
                        dbHelper.open();
                        dbHelper.Register(username, password, name);
                        firebaseAuthWithGoogle(username, password);
                        finish();
                    }else {
                        validateForm();
                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnKeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void firebaseAuthWithGoogle(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Daftar berhasil", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Daftar gagal, format salah!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setError("Required");
            result = false;
        } else {
            txtName.setError(null);
        }
        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setError("Required");
            result = false;
        } else {
            txtEmail.setError(null);
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            txtPassword.setError("Required");
            result = false;
        } else {
            txtPassword.setError(null);
        }
        return result;
    }

}
