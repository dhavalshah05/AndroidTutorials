package com.itgosolutions.tutorial.contacts;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itgosolutions.tutorial.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    private static final String[] PERMISSION_LOAD_CONTACTS = {Manifest.permission.READ_CONTACTS};
    private static final int RESULT_PERMISSION_LOAD_CONTACTS = 15;
    private RecyclerView mRecyclerView;

    public FragmentContacts() {
    }

    public static FragmentContacts newInstance() {
        Bundle args = new Bundle();
        FragmentContacts fragment = new FragmentContacts();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.fragment_contacts_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        loadContacts();
    }


    private void loadContacts() {
        if (canLoadContacts()) {
            getLoaderManager().initLoader(0, null, this);
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(getActivity(), "Permission is required to load contacts", Toast.LENGTH_SHORT).show();
            requestPermissions(netPermissions(PERMISSION_LOAD_CONTACTS), RESULT_PERMISSION_LOAD_CONTACTS);
        } else {
            requestPermissions(netPermissions(PERMISSION_LOAD_CONTACTS), RESULT_PERMISSION_LOAD_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RESULT_PERMISSION_LOAD_CONTACTS) {
            if (canLoadContacts()) {
                getLoaderManager().initLoader(0, null, this);
            } else {
                Toast.makeText(getActivity(), "Please give permission from settings", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case 0:
                ParseContactTask task = new ParseContactTask(getActivity().getContentResolver(), contacts -> {
                    ContactsAdapter adapter = new ContactsAdapter(contacts);
                    mRecyclerView.setAdapter(adapter);
                });
                task.execute(data);
                break;
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private boolean canLoadContacts() {
        return hasPermission(Manifest.permission.READ_CONTACTS);
    }

    public boolean hasPermission(String permissionString) {
        return (ContextCompat.checkSelfPermission(getContext(), permissionString) == PackageManager.PERMISSION_GRANTED);
    }

    public String[] netPermissions(String[] wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String permission : wantedPermissions) {
            if (!hasPermission(permission)) {
                result.add(permission);
            }
        }
        return (result.toArray(new String[result.size()]));
    }

    public interface OnContactsLoadListener {
        void onContactsLoad(List<Contact> contacts);
    }

    private static class ParseContactTask extends AsyncTask<Cursor, Void, List<Contact>> {

        private ContentResolver cr;
        private OnContactsLoadListener mListener;

        ParseContactTask(ContentResolver cr, OnContactsLoadListener listener) {
            this.cr = cr;
            mListener = listener;
        }

        @Override
        protected List<Contact> doInBackground(Cursor... cursors) {
            return ContactUtils.parseContacts(cursors[0], cr);
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);

            if (mListener != null)
                mListener.onContactsLoad(contacts);

            /*
            for (Contact contact : contacts) {
                System.out.println("--------------------------");
                System.out.println("Name: " + contact.getName());
                System.out.println("Number: " + contact.getNumber());
            }
            */
        }

    }
}
