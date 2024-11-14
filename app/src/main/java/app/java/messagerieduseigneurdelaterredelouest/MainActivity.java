package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth; // déclaration d'une variable "auth" qui est de type "FirebaseAuth" qui sert à gérer l'authentification

    //Partie Logout
    ImageView imglogout;
    imglogout=findViewById(R.id.logoutimg);

    imglogout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish();
        }
    });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance(); // initialisation de l'objet "auth" pour faire des vérifications
        //auth.signOut();
        if (auth.getCurrentUser() == null){ //si un utilisateur n'est pas connecté
            Intent intent = new Intent(MainActivity.this, registration.class); // création d'un intent pour le rediriger vers la page de connexion
            startActivity(intent); // envoie de l'utilisateur vers la page de connexion.
            finish();
        }
    }
}