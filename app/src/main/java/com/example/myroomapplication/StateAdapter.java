package com.example.myroomapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder>{
    private final LayoutInflater inflater;

    private List<User> users;
    private List<Contacts> contacts;
    private List<Other> other;

    private View.OnClickListener listener;


    StateAdapter(Context context, List<User> users, List<Contacts> contacts, List<Other> other) {
        this.users = users;
        this.contacts = contacts;
        this.other = other;

        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        view.setOnClickListener(listener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, int position) {
        if (holder.itemView.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Contacts contact = contacts.get(position);
            Other places = other.get(position);
            holder.phoneView.setText(contact.phoneNumber);
            holder.emailView.setText(contact.email);
            holder.studyView.setText(places.studyPlace);
            holder.workView.setText(places.workPlace);
        }
        User state = users.get(position);
        holder.itemView.setId(position);
        holder.firstView.setText(state.firstName);
        holder.secondView.setText(state.lastName);
        holder.patrView.setText(state.patronymicName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView firstView, secondView, patrView, phoneView, emailView, studyView, workView;

        ViewHolder(View v){
            super(v);
            firstView = v.findViewById(R.id.name);
            secondView = v.findViewById(R.id.surname);
            patrView = v.findViewById(R.id.patronymic);
            phoneView = v.findViewById(R.id.phone);
            emailView = v.findViewById(R.id.email);
            studyView = v.findViewById(R.id.study);
            workView = v.findViewById(R.id.work);
        }
    }

    public void setOnClickListener(View.OnClickListener l) {
        listener = l;
    }

    public void setNewStates(List<User> users, List<Contacts> contacts, List<Other> other) {
        this.users = users;
        this.contacts = contacts;
        this.other = other;
    }

    public User getKthUser(int k) {
        return users.get(k);
    }

    public Contacts getKthContacts(int k) {
        return contacts.get(k);
    }

    public Other getKthOtherInfo(int k) {
        return other.get(k);
    }



}
