package com.itgosolutions.tutorial.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itgosolutions.tutorial.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactHolder> {

    private List<Contact> mContacts = new ArrayList<>();

    public ContactsAdapter(List<Contact> contacts) {
        mContacts = contacts;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        Contact contact = mContacts.get(position);
        holder.setData(contact);
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder{

        private TextView tvName, tvNumber;

        public ContactHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.text_view_name);
            tvNumber = itemView.findViewById(R.id.text_view_number);
        }

        public void setData(Contact contact){
            tvName.setText(contact.getName());
            tvNumber.setText(contact.getNumber());
        }
    }


}
