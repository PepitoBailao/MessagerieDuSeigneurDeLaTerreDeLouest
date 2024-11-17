package app.java.messagerieduseigneurdelaterredelouest;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UtilisateurAdapter extends RecyclerView.Adapter<UtilisateurAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Utilisateurs> utilisateursArrayList;
    public UtilisateurAdapter(MainActivity mainActivity, ArrayList<Utilisateurs> utilisateursArrayList) {
        this.mainActivity = mainActivity;
        this.utilisateursArrayList = utilisateursArrayList;
    }

    @NonNull
    @Override
    public UtilisateurAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UtilisateurAdapter.viewholder holder, int position) {

        Utilisateurs utilisateurs = utilisateursArrayList.get(position);
        holder.username.setText(utilisateurs.userName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, Chat.class);
                intent.putExtra("name",utilisateurs.getUserName());
                intent.putExtra("uid",utilisateurs.getUserId());
                mainActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return utilisateursArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView username;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }
    }
}
