package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView imglogout;
    ImageView imgchat;
    RecyclerView mainUserRecyclerView;
    UtilisateurAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Utilisateurs> utilisateursArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseReference reference = database.getReference().child("user");

        utilisateursArrayList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                utilisateursArrayList.clear(); // Efface les anciennes données
                Log.d("testttttt", "Snapshot size: " + snapshot.getChildrenCount());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Utilisateurs utilisateurs = dataSnapshot.getValue(Utilisateurs.class);
                    if (utilisateurs != null) {
                        utilisateursArrayList.add(utilisateurs);
                        Log.d("MainActivity", "UserName: " + utilisateurs.getUserName());
                    } else {
                        Log.e("MainActivity", "Utilisateur null !");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Erreur Firebase : " + error.getMessage());
            }
        });

        Log.d("MainActivity", "utilisateursArrayList size: " + utilisateursArrayList.size());

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UtilisateurAdapter(MainActivity.this, utilisateursArrayList);
        mainUserRecyclerView.setAdapter(adapter);

        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, registration.class);
            startActivity(intent);
            finish();
        }

        imglogout = findViewById(R.id.logoutimg);
        imglogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
            }
        });

        imgchat = findViewById(R.id.chatimg);
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chat.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
