package app.java.messageriedelaterredelouest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends android.widget.BaseAdapter {

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        Message message = messages.get(position);

        // Obtenir les vues
        TextView usernameText = convertView.findViewById(R.id.usernameText);
        TextView messageText = convertView.findViewById(R.id.messageText);
        TextView likeCount = convertView.findViewById(R.id.likeCount);
        Button likeButton = convertView.findViewById(R.id.likeButton);

        // Définir les valeurs
        usernameText.setText(message.getUsername()); // Affiche l'e-mail comme username
        messageText.setText(message.getText());
        likeCount.setText(String.valueOf(message.getLikes()));

        // Gestion des likes
        likeButton.setOnClickListener(v -> toggleLike(message, likeCount));

        return convertView;
    }

    private void toggleLike(Message message, TextView likeCount) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (message.getLikedBy() == null) {
            message.setLikedBy(new java.util.HashMap<>());
        }

        boolean hasLiked = message.getLikedBy().containsKey(currentUserId);

        if (hasLiked) {
            // Unlike
            message.getLikedBy().remove(currentUserId);
            message.setLikes(message.getLikes() - 1);
        } else {
            // Like
            message.getLikedBy().put(currentUserId, true);
            message.setLikes(message.getLikes() + 1);
        }

        // Mettre à jour Firebase
        FirebaseDatabase.getInstance().getReference("messages")
                .child(message.getId())
                .setValue(message);

        likeCount.setText(String.valueOf(message.getLikes()));
    }
}
