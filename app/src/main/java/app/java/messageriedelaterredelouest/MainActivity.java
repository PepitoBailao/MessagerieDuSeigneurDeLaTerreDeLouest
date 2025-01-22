// package de l'application, comme une boîte pour organiser ton code
package app.java.messageriedelaterredelouest;

// quelques imports pour utiliser les fonctions d'android et firebase
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// c'est la classe principale de l'activité
public class MainActivity extends AppCompatActivity {

    // liste pour stocker tous les messages
    private ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter adapter; // adaptateur pour connecter les messages à la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // appel à la méthode de base
        setContentView(R.layout.activity_main); // connecte l'activité à son interface graphique

        // on initialise les champs et boutons de l'interface
        EditText messageInput = findViewById(R.id.messageInput); // champ pour écrire un message
        Button sendButton = findViewById(R.id.sendMessageButton); // bouton pour envoyer
        ListView messageListView = findViewById(R.id.messageListView); // liste pour afficher les messages
        ImageView logoutImg = findViewById(R.id.logoutimg); // icône pour se déconnecter

        // on configure l'adaptateur pour afficher les messages dans la liste
        adapter = new MessageAdapter(this, messages);
        messageListView.setAdapter(adapter);

        // action quand on clique sur le bouton d'envoi
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim(); // récupère le texte
            if (messageText.isEmpty()) { // si le champ est vide, on prévient l'utilisateur
                Toast.makeText(this, "mets un message non ?", Toast.LENGTH_SHORT).show();
                return;
            }

            // récupère l'email de l'utilisateur actuel
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            if (userEmail == null) { // si on n'arrive pas à récupérer l'email
                Toast.makeText(this, "erreur : impossible de récupérer l'adresse e-mail.", Toast.LENGTH_SHORT).show();
                return;
            }

            // crée un identifiant unique pour le message
            String messageId = FirebaseDatabase.getInstance().getReference("messages").push().getKey();

            // si l'identifiant est valide
            if (messageId != null) {
                // on crée un nouvel objet message
                Message message = new Message(messageId, messageText, 0, userEmail);

                // on l'enregistre dans la base de données firebase
                FirebaseDatabase.getInstance().getReference("messages")
                        .child(messageId)
                        .setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "message envoyé !", Toast.LENGTH_SHORT).show();
                            messageInput.setText(""); // vide le champ après l'envoi
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "erreur d'envoi du message.", Toast.LENGTH_SHORT).show());
            }
        });

        // action quand on clique sur l'icône de déconnexion
        logoutImg.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // déconnecte l'utilisateur
            Toast.makeText(this, "déconnecté.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class)); // renvoie à l'écran de connexion
            finish(); // ferme cette activité
        });

        // charge les messages de la base de données
        loadMessages();
    }

    private void loadMessages() {
        // connecte à firebase et récupère les messages triés par "likes"
        FirebaseDatabase.getInstance().getReference("messages")
                .orderByChild("likes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        messages.clear(); // vide la liste actuelle
                        for (DataSnapshot data : snapshot.getChildren()) { // parcourt chaque message
                            Message message = data.getValue(Message.class); // convertit les données en objet message
                            if (message != null) { // si le message est valide
                                messages.add(0, message); // ajoute à la liste (au début)
                            }
                        }
                        adapter.notifyDataSetChanged(); // dit à l'adaptateur que la liste a changé
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MainActivity.this, "erreur de chargement des messages.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
