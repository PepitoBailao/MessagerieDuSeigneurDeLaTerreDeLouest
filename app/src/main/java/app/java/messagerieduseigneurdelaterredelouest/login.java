package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button button; // Bouton de connexion
    EditText email, password; // Champs de saisie pour l'email et le mot de passe
    FirebaseAuth auth; // Objet pour gérer l'authentification Firebase
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // Modèle de validation pour l'email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des éléments de l'interface
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Logbutton);
        email = findViewById(R.id.editTextLogEmailAddress);
        password = findViewById(R.id.editTextLogPassword);

        // Gestion du clic sur le bouton de connexion
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String pass = password.getText().toString();

                // Vérification des champs
                if (TextUtils.isEmpty(Email)) { // si le champ Email est vide, renvoyer un message
                    Toast.makeText(login.this, "Entrez votre Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) { // si le champ mdp est vide, renvoyer un message
                    Toast.makeText(login.this, "Entrez votre mot de passe", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) { //si l'email rentrée ne correspond pas au pattern, envoyer une erreur
                    email.setError("Email invalide");
                } else if (pass.length() < 4) { // si le mot de passe est plus petit que 4 caractères, renvoyer une erreur
                    password.setError("Mot de passe trop court");
                } else {
                    // Authentification avec Firebase
                    auth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Redirection vers la page principale en cas de succès
                                try {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(login.this, "Erreur d'authentification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
