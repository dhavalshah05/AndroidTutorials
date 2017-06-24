package com.itgosolutions.tutorial.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created for testing purposes only
 */
public class GsonDemo {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        User dhaval = new User();
        dhaval.setId(1);
        dhaval.setName("Dhaval");
        dhaval.getEmails().add("shahdhaval4674@gmail.com");
        dhaval.getEmails().add("dhavalshah4674@gmail.com");
        dhaval.getAddresses().put("address1", "Kalol");
        dhaval.getAddresses().put("address2", "Ahmedabad");

        User niral = new User();
        niral.setId(101);
        niral.setName("Niral");

        dhaval.getFriends().add(niral);


        String jsonString = gson.toJson(dhaval);
        System.out.println("From Object To Json String");
        System.out.println(jsonString);
        System.out.println(" ");

        System.out.println("From JsonString to Object");

        User user = gson.fromJson(jsonString, User.class);
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getEmails());
        System.out.println(user.getAddresses());
        System.out.println(user.getFriends());


        System.out.println(" ");
        System.out.println("From List<User> to String");

        java.util.List<User> userList = new ArrayList<>();
        userList.add(dhaval);
        userList.add(niral);

        String userListString = gson.toJson(userList);
        System.out.println(userListString);

        System.out.println(" ");

        System.out.println("From ListString to List<User>");

        Type type = new TypeToken<List<User>>() {
        }.getType();

        List<User> users = gson.fromJson(userListString, type);

        System.out.println(users.size());


    }


    private static class User {

        private long id;
        private String name;

        private java.util.List<String> emails = new ArrayList<>();

        private Map<String, String> addresses = new HashMap<>();

        private java.util.List<User> friends = new ArrayList<>();

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.util.List<String> getEmails() {
            return emails;
        }

        public void setEmails(java.util.List<String> emails) {
            this.emails = emails;
        }

        public Map<String, String> getAddresses() {
            return addresses;
        }

        public void setAddresses(Map<String, String> addresses) {
            this.addresses = addresses;
        }

        public java.util.List<User> getFriends() {
            return friends;
        }

        public void setFriends(java.util.List<User> friends) {
            this.friends = friends;
        }


    }

}
