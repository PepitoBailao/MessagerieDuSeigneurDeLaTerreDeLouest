package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;  // Importer View
import android.widget.Button;  // Importer Button
import android.widget.EditText;  // Importer EditText
import android.widget.TextView;  // Importer TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;  // Importer FirebaseAuth
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.FirebaseDatabaseKtxRegistrar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class registration extends AppCompatActivity {
    TextView loginbutton;
    EditText rg_username, rg_password, rg_emailaddress, rg_repassword;
    Button rg_signupbutton;
    FirebaseAuth Auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialisation des éléments de l'interface
        database = FirebaseDatabase.getInstance();
        loginbutton = findViewById(R.id.loginbutton);
        rg_username = findViewById(R.id.rgusername);
        rg_password = findViewById(R.id.rgpassword);
        rg_emailaddress = findViewById(R.id.rgemailaddress);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_signupbutton = findViewById(R.id.signupbutton);

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

        rg_signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = rg_username.getText().toString();
                String email = rg_emailaddress.getText().toString();
                String password = rg_password.getText().toString();
                String re_password = rg_repassword.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(password) || TextUtils.isEmpty(re_password)){
                    Toast.makeText(registration.this, "Entrez des informations valide", Toast.LENGTH_SHORT).show();
                }else if (!email.matches(emailPattern)){
                    rg_emailaddress.setError("Email invalide");
                } else if (password.length()<4) {
                    rg_password.setError("Le mot de passe doit faire plus de 4 caractères");
                } else if (!password.equals(re_password)) {
                    rg_password.setError("Le mot de passe ne correspond pas");
                }else {
                    Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                Utilisateurs utilisateurs = new Utilisateurs(id, name, email, password);
                                reference.setValue(utilisateurs).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Intent intent = new Intent(registration.this, login.class);
                                            Toast.makeText(registration.this, "Compte crée", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(registration.this, "Erreur lors de la création de l'utilisateur", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
