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

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> authenticate(emailField, passwordField, false));
        registerButton.setOnClickListener(v -> authenticate(emailField, passwordField, true));
    }

    private void authenticate(EditText emailField, EditText passwordField, boolean isRegister) {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Rempli tout par contre ! ");
            return;
        }

        if (isRegister && password.length() < 6) {
            showToast("Mec 6 minimum aussi !");
            return;
        }

        if (isRegister) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> handleAuthResult(task.isSuccessful(), email, isRegister));
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> handleAuthResult(task.isSuccessful(), email, isRegister));
        }
    }

    private void handleAuthResult(boolean success, String email, boolean isRegister) {
        if (success) {
            if (isRegister) {
                String userId = auth.getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("users").child(userId).setValue(new User(email));
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            showToast(isRegister ? "Échec d'inscription !? Comment tu fais ?" : "Échec de connexion ! Non là tu force !");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

class User {
    public String email;

    public User() {}

    public User(String email) {
        this.email = email;
    }
}
