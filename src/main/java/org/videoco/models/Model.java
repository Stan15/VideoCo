package org.videoco.models;

import javafx.beans.value.ObservableValue;
import org.videoco.controllers.Controller;
import org.videoco.models.users.EmployeeModel;

public abstract class Model {
    protected String id;
    public void setID(String id) {
        this.id = id;
    }
    public String getID() {
        return this.id;
    }
    public abstract String getDatabaseKey();

    @Override
    public boolean equals(Object obj) {
        if (obj==null) return false;
        Model other = (Model) obj;
        return this.getDatabaseKey().equals(other.getDatabaseKey());
    }
}
