package app.java.messageriedelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    //firebaseAuth, l'authentification des utilisateur
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialisation de FirebaseAuth et association des éléments de l'interface utilisateur
        auth = FirebaseAuth.getInstance();

        //champs pour saisir l'email et le mot de passe
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);

        //boutons pour se connecter ou s'inscrire
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        //configuration des actions pour chaque bouton
        loginButton.setOnClickListener(v -> authenticate(emailField, passwordField, false)); // Connexion
        registerButton.setOnClickListener(v -> authenticate(emailField, passwordField, true)); // Inscription
    }

    /**
     Authentifie l'utilisateur en fonction de l'action choisie (inscription ou connexion)
     Vérifie les informations saisies et utilise Firebase pour traiter la demande
     emailField Champ de saisie de l'email.
     passwordField Champ de saisie du mot de passe.
     isRegister Indique si l'utilisateur s'inscrit (true) ou se connecte (false).
     */

    private void authenticate(EditText emailField, EditText passwordField, boolean isRegister) {
        String email = emailField.getText().toString(); //récupère l'email
        String password = passwordField.getText().toString(); //récupère le mot de passe

        if (isRegister) {
            //inscription d'un nouvel utilisateur avec Firebase
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> AuthResult(task.isSuccessful(), email, isRegister));
        } else {
            //connexion d'un utilisateur existant avec Firebase
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> AuthResult(task.isSuccessful(), email, isRegister));
        }
    }

    /**
     Gère le résultat de l'authentification, que ce soit une inscription ou une connexion.
     Si l'opération réussit, redirige vers l'écran principal. Sinon, affiche un message d'erreur.
     success Indique si l'authentification a réussi.
     email L'email de l'utilisateur.
     isRegister Indique si c'était une inscription ou une connexion.
     */
    private void AuthResult(boolean success, String email, boolean isRegister) {
        if (success) {
            if (isRegister) {
                //enregistre les informations de l'utilisateur dans la base de données Firebase
                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(auth.getCurrentUser().getUid())
                        .setValue(new User(email));
            }
            //passe à l'écran principal après une réussite
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Termine l'activité de connexion
        } else {
            //affiche un message d'erreur si l'opération a échoué
            showToast(isRegister ? "Échec d'inscription !" : "Échec de connexion !");
        }
    }

    /**
     Affiche un message Toast pour informer l'utilisateur en cas de succès ou d'erreur
     message Le texte à afficher.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

/**
 Représente un utilisateur dans la base de données Firebase.
 Contient uniquement l'email pour cet exemple.
 */
class User {
    public String email;

    //constructeur par défaut requis par Firebase
    public User() {}

    //constructeur pour initialiser un utilisateur avec son email
    public User(String email) {
        this.email = email;
    }
}
