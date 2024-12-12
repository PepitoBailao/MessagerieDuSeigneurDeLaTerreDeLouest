package app.java.messagerietkp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference messagesRef;
    private ArrayList<Message> messages;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesRef = FirebaseDatabase.getInstance().getReference("messages");
        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, messages);

        EditText messageInput = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendMessageButton);
        ListView messageListView = findViewById(R.id.messageListView);
        ImageView logoutImg = findViewById(R.id.logoutimg);

        messageListView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String messageId = messagesRef.push().getKey();

                if (messageId != null) {
                    Message message = new Message(messageId, messageText, 0, userId);
                    messagesRef.child(messageId).setValue(message)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Le fluxBiz de mes couilles est partis ! ", Toast.LENGTH_SHORT).show();
                                messageInput.setText("");
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "C'est l'envoi du message qui est un échec ou toi ?", Toast.LENGTH_SHORT).show());
                }
            } else {
                Toast.makeText(this, "Veuillez entrer un message.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutImg.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Ca dégage !", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        messagesRef.orderByChild("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Erreur lors du chargement des messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
