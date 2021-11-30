package org.videoco.models;

import org.videoco.controllers.Controller;

public interface Model {
    String getDatabaseKey();
    Controller createController();
}
