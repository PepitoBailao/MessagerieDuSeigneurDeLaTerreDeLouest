package app.java.messagerieduseigneurdelaterredelouest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chat extends AppCompatActivity {

    String reciverUid, reciverName, SenderUid;
    TextView reciverNName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        reciverName = getIntent().getStringExtra("name");
        reciverUid = getIntent().getStringExtra("uid");

        reciverNName = findViewById(R.id.nom);
        reciverNName.setText(" "+ reciverName);
    }
}