// package où est située la classe
package app.java.messageriedelaterredelouest;

// import pour utiliser une map (clé-valeur)
import java.util.HashMap;
import java.util.Map;

// classe pour représenter un message
public class Message {
    // propriétés d'un message
    private String id; // identifiant unique du message
    private String text; // contenu du message
    private int likes; // nombre de "j'aime" sur le message
    private String username; // nom de l'utilisateur qui a envoyé le message
    private Map<String, Boolean> likedBy; // liste des utilisateurs qui ont aimé, avec "true" pour chaque utilisateur

    // constructeur vide (nécessaire pour firebase)
    public Message() {}

    // constructeur avec des paramètres, pour créer un message plus facilement
    public Message(String id, String text, int likes, String username) {
        this.id = id; // on attribue l'id
        this.text = text; // on attribue le texte
        this.likes = likes; // on attribue le nombre de likes
        this.username = username; // on attribue le nom d'utilisateur
        this.likedBy = new HashMap<>(); // on initialise la map des likes comme une nouvelle hashmap
    }

    // getter pour obtenir l'id du message
    public String getId() {
        return id;
    }

    // setter pour changer l'id du message
    public void setId(String id) {
        this.id = id;
    }

    // getter pour obtenir le texte du message
    public String getText() {
        return text;
    }

    // setter pour modifier le texte du message
    public void setText(String text) {
        this.text = text;
    }

    // getter pour obtenir le nombre de likes
    public int getLikes() {
        return likes;
    }

    // setter pour changer le nombre de likes
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // getter pour obtenir le nom d'utilisateur
    public String getUsername() {
        return username;
    }

    // setter pour modifier le nom d'utilisateur
    public void setUsername(String username) {
        this.username = username;
    }

    // getter pour obtenir la liste des utilisateurs qui ont aimé le message
    public Map<String, Boolean> getLikedBy() {
        // si la liste n'existe pas encore, on en crée une nouvelle
        if (likedBy == null) {
            likedBy = new HashMap<>();
        }
        return likedBy;
    }

    // setter pour remplacer toute la liste des utilisateurs qui ont aimé
    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }
}
