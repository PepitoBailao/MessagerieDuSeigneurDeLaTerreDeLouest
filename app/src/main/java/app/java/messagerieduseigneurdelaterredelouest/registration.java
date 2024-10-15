package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;  // Importer View
import android.widget.Button;  // Importer Button
import android.widget.EditText;  // Importer EditText
import android.widget.TextView;  // Importer TextView

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;  // Importer FirebaseAuth

public class registration extends AppCompatActivity {
    TextView loginbutton;
    EditText rg_username, rg_password, rg_emailaddress, rg_repassword;
    Button rg_signupbutton;
    FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialisation des éléments de l'interface
        loginbutton = findViewById(R.id.loginbutton);
        rg_username = findViewById(R.id.rgusername);
        rg_password = findViewById(R.id.rgpassword);
        rg_emailaddress = findViewById(R.id.rgemailaddress);
        rg_repassword = findViewById(R.id.rgrepassword);

        // Initialisation de FirebaseAuth
        Auth = FirebaseAuth.getInstance();

        // Gestion du clic sur le bouton de connexion
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
