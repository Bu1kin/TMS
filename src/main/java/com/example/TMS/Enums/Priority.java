package com.example.TMS.Enums;

public enum Priority {
    Low("Низкий"),
    Medium("Средний"),
    High("Высокий");

    private final String displayedName;

    Priority(String displayedName) {
        this.displayedName = displayedName;
    }

    public String displayedName() {
        return displayedName;
    }
}
