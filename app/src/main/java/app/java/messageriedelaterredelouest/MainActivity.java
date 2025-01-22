package app.java.messageriedelaterredelouest;

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

// Définition de la classe principale qui hérite de AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // Liste pour stocker les messages
    private ArrayList<Message> messages = new ArrayList<>();
    // Adaptateur personnalisé pour afficher les messages dans une ListView
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Appel de la méthode parent pour initialiser l'activité
        setContentView(R.layout.activity_main); // Définir le layout associé à cette activité

        // Initialisation des vues depuis le fichier XML
        EditText messageInput = findViewById(R.id.messageInput); // Champ de saisie pour les messages
        Button sendButton = findViewById(R.id.sendMessageButton); // Bouton pour envoyer un message
        ListView messageListView = findViewById(R.id.messageListView); // Liste pour afficher les messages
        ImageView logoutImg = findViewById(R.id.logoutimg); // Icône pour la déconnexion

        // Initialisation de l'adaptateur avec la liste de messages
        adapter = new MessageAdapter(this, messages);
        messageListView.setAdapter(adapter); // Associer l'adaptateur à la ListView

        // Gestion de l'envoi d'un message lorsque le bouton est cliqué
        sendButton.setOnClickListener(v -> {
            // Récupérer le texte du champ de saisie
            String messageText = messageInput.getText().toString().trim();
            // Vérifier si le champ est vide
            if (messageText.isEmpty()) {
                Toast.makeText(this, "Mets un message non ?", Toast.LENGTH_SHORT).show();
                return; // Sortir si aucun texte n'est saisi
            }

            // Récupérer l'adresse e-mail de l'utilisateur connecté
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Récupérer l'e-mail
            if (userEmail == null) { // Vérifier si l'e-mail est nul
                Toast.makeText(this, "Erreur : Impossible de récupérer l'adresse e-mail.", Toast.LENGTH_SHORT).show();
                return; // Sortir en cas d'erreur
            }

            // Générer un ID unique pour le message dans la base de données Firebase
            String messageId = FirebaseDatabase.getInstance().getReference("messages").push().getKey();

            if (messageId != null) { // Vérifier si l'ID est valide
                // Créer un nouvel objet Message avec l'ID, le texte, les likes (0), et l'e-mail de l'utilisateur
                Message message = new Message(messageId, messageText, 0, userEmail);
                // Ajouter le message à la base de données sous le chemin "messages"
                FirebaseDatabase.getInstance().getReference("messages")
                        .child(messageId) // Ajouter avec l'ID unique
                        .setValue(message) // Définir les valeurs du message
                        .addOnSuccessListener(aVoid -> { // Gestion de la réussite de l'envoi
                            Toast.makeText(this, "Message envoyé !", Toast.LENGTH_SHORT).show();
                            messageInput.setText(""); // Effacer le champ de saisie après l'envoi
                        })
                        .addOnFailureListener(e -> // Gestion des erreurs d'envoi
                                Toast.makeText(this, "Erreur d'envoi du message.", Toast.LENGTH_SHORT).show());
            }
        });

        // Gestion de la déconnexion lorsque l'icône est cliquée
        logoutImg.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Déconnexion de l'utilisateur
            Toast.makeText(this, "Déconnecté.", Toast.LENGTH_SHORT).show(); // Afficher un message
            startActivity(new Intent(this, LoginActivity.class)); // Rediriger vers l'écran de connexion
            finish(); // Fermer l'activité actuelle
        });

        // Charger les messages existants depuis la base de données
        loadMessages();
    }

    // Méthode pour charger les messages depuis Firebase
    private void loadMessages() {
        // Requête Firebase pour récupérer les messages triés par nombre de likes
        FirebaseDatabase.getInstance().getReference("messages")
                .orderByChild("likes") // Trier par le champ "likes"
                .addValueEventListener(new ValueEventListener() { // Ajouter un écouteur d'événements
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        messages.clear(); // Effacer la liste locale avant d'ajouter les nouveaux messages
                        for (DataSnapshot data : snapshot.getChildren()) { // Parcourir chaque noeud
                            Message message = data.getValue(Message.class); // Convertir les données en objet Message
                            if (message != null) { // Ajouter le message à la liste locale
                                messages.add(0, message); // Ajouter au début de la liste
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notifier l'adaptateur pour mettre à jour l'affichage
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Afficher un message en cas d'erreur de lecture des données
                        Toast.makeText(MainActivity.this, "Erreur de chargement des messages.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
