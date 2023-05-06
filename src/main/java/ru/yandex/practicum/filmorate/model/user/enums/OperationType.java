package ru.yandex.practicum.filmorate.model.user.enums;

public enum OperationType {

    REMOVE("REMOVE"),
    ADD("ADD"),
    UPDATE("UPDATE");

    private final String operationName;

    OperationType(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public static OperationType fromString(String str) {
        for (OperationType type : OperationType.values()) {
            if (type.getOperationName().equalsIgnoreCase(str)) return type;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Тип операции: " + operationName;
    }
}
