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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        EditText messageInput = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendMessageButton);
        ListView messageListView = findViewById(R.id.messageListView);
        ImageView logoutImg = findViewById(R.id.logoutimg);

        adapter = new MessageAdapter(this, messages);
        messageListView.setAdapter(adapter);

        // Envoi d'un message
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (messageText.isEmpty()) {
                Toast.makeText(this, "Mets un message non ?", Toast.LENGTH_SHORT).show();
                return;
            }

            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Récupérer l'e-mail
            if (userEmail == null) {
                Toast.makeText(this, "Erreur : Impossible de récupérer l'adresse e-mail.", Toast.LENGTH_SHORT).show();
                return;
            }

            String messageId = FirebaseDatabase.getInstance().getReference("messages").push().getKey();

            if (messageId != null) {
                Message message = new Message(messageId, messageText, 0, userEmail); // Ajouter l'e-mail comme username
                FirebaseDatabase.getInstance().getReference("messages")
                        .child(messageId)
                        .setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Message envoyé !", Toast.LENGTH_SHORT).show();
                            messageInput.setText("");
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Erreur d'envoi du message.", Toast.LENGTH_SHORT).show());
            }
        });

        // Gestion de la déconnexion
        logoutImg.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Déconnecté.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Chargement des messages
        loadMessages();
    }

    private void loadMessages() {
        FirebaseDatabase.getInstance().getReference("messages")
                .orderByChild("likes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Message message = data.getValue(Message.class);
                            if (message != null) {
                                messages.add(0, message);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Erreur de chargement des messages.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
