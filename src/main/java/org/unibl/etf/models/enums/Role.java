package org.unibl.etf.models.enums;

public enum Role {
    CLIENT("Client"),ADMIN("Admin"),MODERATOR("Moderator");
    private final String status;

    Role(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString()
    {
        return this.status;
    }

    public static Role getByStatus(String status)
    {
        for(var el:Role.values())
            if(el.status.equals(status))
                return el;
        throw new IllegalArgumentException();
    }
}
