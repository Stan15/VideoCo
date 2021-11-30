package org.videoco.models.users;

import org.videoco.models.Model;

public abstract class UserModel implements Model {
    public String name;
    public String id;
    public String type;
    public String email;
    public String password;

    public UserModel(String name, String id, String email, String password) {
        super();
        this.setName(name);
        this.setID(id);
        this.setEmail(email);
        this.setPassword(password);
    }

    public UserModel(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = String.join(" ", name.strip().toLowerCase().split("\s"));
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.strip().toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String getDatabaseKey() {
        return this.getEmail();
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", id=" + id + ", email=" + email + ", password=" + password + "]";
    }
}