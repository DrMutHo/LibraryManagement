package main.Views;

public enum RecipientType {
    Admin,
    Client;

    public static RecipientType fromString(String type) {
        for (RecipientType rt : RecipientType.values()) {
            if (rt.name().equalsIgnoreCase(type)) {
                return rt;
            }
        }
        throw new IllegalArgumentException("Invalid RecipientType: " + type);
    }
}
