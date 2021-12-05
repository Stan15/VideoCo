package org.videoco.factories;

import org.videoco.models.Model;

public abstract class Factory {
    public Factory() {}
    public Factory(Model model) {
        this.copy(model);
    }
    public abstract void copy(Model model);
    public abstract void setID(String id);
    public abstract Model createModel();
    public abstract String findErrorInRequiredFields();
    public abstract String getDatabaseKeyField();
}
