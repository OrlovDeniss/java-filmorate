package ru.yandex.practicum.filmorate.model.user.enums;

public enum EventType {
    LIKE("LIKE"),
    REVIEW("REVIEW"),
    FRIEND("FRIEND");

    private final String eventTypeName;

    EventType(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public static EventType fromString(String str) {
        for (EventType type : EventType.values()) {
            if (type.getEventTypeName().equalsIgnoreCase(str)) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Событие: " + eventTypeName;
    }
}
