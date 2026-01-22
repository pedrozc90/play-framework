package web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends AuditEntityDto implements Serializable {

    @JsonProperty(value = "id")
    private final Long id;

    @JsonProperty(value = "email")
    private final String email;

    @JsonProperty(value = "active")
    private final Boolean active;

    public UserDto(
        final UUID uuid,
        final Instant insertedAt,
        final Instant updatedAt,
        final Integer version,
        final Long id,
        final String email,
        final Boolean active
    ) {
        super(uuid, insertedAt, updatedAt, version);
        this.id = id;
        this.email = email;
        this.active = active;
    }

}
