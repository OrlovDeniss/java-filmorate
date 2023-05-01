package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.AbstractEntity;
import ru.yandex.practicum.filmorate.model.user.enums.EventType;
import ru.yandex.practicum.filmorate.model.user.enums.OperationType;

@Builder
@Data
public class Feed extends AbstractEntity {

    private Long timestamp;
    private Long userId;
    private EventType eventType;
    private OperationType operation;
    private Long entityId;

    @JsonProperty("eventId")
    @Override
    public Long getId() {
        return super.getId();
    }
}
