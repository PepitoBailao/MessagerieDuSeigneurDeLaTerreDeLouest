// package où est stockée cette classe
package app.java.messageriedelaterredelouest;

// imports nécessaires pour gérer l'interface et firebase
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

// classe qui sert à adapter les messages pour les afficher dans une liste
public class MessageAdapter extends android.widget.BaseAdapter {

    private Context context; // contexte de l'activité
    private List<Message> messages; // liste de messages à afficher

    // constructeur pour passer le contexte et les messages à l'adaptateur
    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    // retourne le nombre total de messages dans la liste
    @Override
    public int getCount() {
        return messages.size();
    }

    // retourne un message spécifique en fonction de sa position dans la liste
    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    // retourne l'id de l'élément (ici, on utilise la position comme id)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // méthode principale pour afficher chaque élément (message) dans la liste
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // si la vue est null, on la crée en utilisant le layout "message_item"
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        }

        // récupérer le message actuel en fonction de sa position
        Message message = messages.get(position);

        // on récupère les éléments de l'interface graphique du message
        TextView usernameText = convertView.findViewById(R.id.usernameText); // affiche le nom ou l'email de l'utilisateur
        TextView messageText = convertView.findViewById(R.id.messageText); // affiche le texte du message
        TextView likeCount = convertView.findViewById(R.id.likeCount); // affiche le nombre de likes
        Button likeButton = convertView.findViewById(R.id.likeButton); // bouton pour liker

        // on remplit les éléments avec les données du message
        usernameText.setText(message.getUsername()); // définit le texte pour l'username
        messageText.setText(message.getText()); // définit le texte du message
        likeCount.setText(String.valueOf(message.getLikes())); // affiche le nombre de likes

        // quand on clique sur le bouton "like", on appelle une méthode pour gérer les likes
        likeButton.setOnClickListener(v -> toggleLike(message, likeCount));

        // retourne la vue complète pour ce message
        return convertView;
    }

    // méthode pour ajouter ou retirer un "like" à un message
    private void toggleLike(Message message, TextView likeCount) {
        // récupère l'id de l'utilisateur actuel
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // si la liste des likes est vide, on en crée une nouvelle
        if (message.getLikedBy() == null) {
            message.setLikedBy(new java.util.HashMap<>());
        }

        // vérifie si l'utilisateur a déjà liké ce message
        boolean hasLiked = message.getLikedBy().containsKey(currentUserId);

        if (hasLiked) {
            // si l'utilisateur a déjà liké, on enlève son like
            message.getLikedBy().remove(currentUserId);
            message.setLikes(message.getLikes() - 1); // réduit le compteur de likes
        } else {
            // sinon, on ajoute un like pour cet utilisateur
            message.getLikedBy().put(currentUserId, true);
            message.setLikes(message.getLikes() + 1); // augmente le compteur de likes
        }

        // on met à jour le message dans firebase
        FirebaseDatabase.getInstance().getReference("messages")
                .child(message.getId()) // cherche le message par son id
                .setValue(message); // sauvegarde le message avec les nouvelles valeurs

        // on met à jour l'affichage du compteur de likes
        likeCount.setText(String.valueOf(message.getLikes()));
    }
}
