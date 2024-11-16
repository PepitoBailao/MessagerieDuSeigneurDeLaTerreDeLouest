package app.java.messagerieduseigneurdelaterredelouest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UtilisateurAdapter.viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        public viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
