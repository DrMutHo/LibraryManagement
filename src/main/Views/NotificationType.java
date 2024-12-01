package main.Views;

public enum NotificationType {
    ReturnReceiptIssued,
    OverdueAlert,
    BorrowRequestConfirmed,
    ReturnReminder,
    BorrowReceiptIssued,
    PurchaseReceiptIssued,
    BookAvailable,
    BorrowConfirmed,
    ReportBug,
    UNKNOWN;

    public static NotificationType fromString(String type) {
        for (NotificationType nt : NotificationType.values()) {
            if (nt.name().equalsIgnoreCase(type)) {
                return nt;
            }
        }
        System.err.println("Unknown NotificationType: " + type + ", defaulting to UNKNOWN");
        return UNKNOWN;
    }
}
