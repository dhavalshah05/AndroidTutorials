package com.itgosolutions.tutorial.contacts;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

final class ContactUtils {


    static List<Contact> parseContacts(Cursor cursor, ContentResolver contentResolver) {
        List<Contact> contacts = new ArrayList<>();

        if (cursor.getCount() == 0) {
            return contacts;
        }

        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        //int lookupColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
        int hasNumberColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        while (cursor.moveToNext()) {
            String contactName = cursor.getString(nameColumnIndex);
            String id = cursor.getString(idColumnIndex);
            //String lookup = cursor.getString(lookupColumnIndex);
            int hasNumber = cursor.getInt(hasNumberColumnIndex);

            if (hasNumber > 0) {
                List<String> numbers = getContactNumbers(id, contentResolver);
                for (String number : numbers) {
                    Contact contact = new Contact(id, contactName, number);
                    contacts.add(contact);
                }
            }
        }

        cursor.close();
        return contacts;
    }

    private static List<String> getContactNumbers(String id, ContentResolver contentResolver) {
        List<String> numbers = new ArrayList<>();

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id},
                null
        );

        int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        while (cursor.moveToNext()) {
            String number = parseNumber(cursor, numberColumnIndex);
            if (!numbers.contains(number))
                numbers.add(number);
        }
        cursor.close();
        return numbers;
    }

    private static String parseNumber(Cursor cursor, int index){
        return cursor.getString(index).replaceAll("\\s+", "").replaceAll("-", "");
    }
}
