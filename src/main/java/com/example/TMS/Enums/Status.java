package com.example.TMS.Enums;

public enum Status {
    Passed("Пройден"),
    NotPassed("Не пройден"),
    Cancelled("Отменён");

    private final String displayedName;

    Status(String displayedName) {
        this.displayedName = displayedName;
    }

    public String displayedName() {
        return displayedName;
    }
}
