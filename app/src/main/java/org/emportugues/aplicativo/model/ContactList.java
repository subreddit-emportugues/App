package org.emportugues.aplicativo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class ContactList {

    @SerializedName("subreddits")
    @Expose
    private ArrayList<Contact> contacts = new ArrayList<>();

    /**
     * @return The contacts
     */
    public ArrayList<Contact> getContacts() {
        Collections.sort(contacts, new ActivityComparator());
        return contacts;
    }

    /**
     * @param contacts The contacts
     */
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public int getActivity() {
        int total = 0;

        for (Contact contact : contacts) {
            total += contact.getRecentComments() + contact.getRecentSubmissions();
        }

        return total;
    }

}
