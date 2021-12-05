package org.videoco.models.users;

import org.videoco.controllers.users.UserController;
import org.videoco.controllers.users.UserType;
import org.videoco.models.Model;

public abstract class UserModel extends Model {
    public String name;
    public UserType type;
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
        if (name==null) return;
        this.name = String.join(" ", name.strip().toLowerCase().split("\s"));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email==null) return;
        this.email = email.strip().toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
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

    public abstract UserController createController();
}