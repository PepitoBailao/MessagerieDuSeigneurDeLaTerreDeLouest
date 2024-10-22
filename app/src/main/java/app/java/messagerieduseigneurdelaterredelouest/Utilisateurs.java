package app.java.messagerieduseigneurdelaterredelouest;

public class Utilisateurs {
    String mail, userName, password, userId, lastMessage;

    // Constructeur sans "re_password", car ce champ n'a pas besoin d'être stocké dans la base de données
    public Utilisateurs(String userId, String name, String email, String password) {
        this.userId = userId;
        this.userName = name;
        this.mail = email;
        this.password = password;
    }

    // Constructeur vide nécessaire pour Firebase
    public Utilisateurs() {
    }


    // Getters et setters
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
