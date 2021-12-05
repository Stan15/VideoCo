package org.videoco.models.users;

public enum AdminStatus {
    SYSTEM_ADMIN(2), ADMIN(1), NON_ADMIN(0);
    public final int level;

    AdminStatus(int level) {
        this.level = level;
    }
}
