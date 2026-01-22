package actors.messages;

import akka.actor.ActorRef;
import lombok.Data;

@Data
public class HeartbeatAck {

    private final Class<?> clazz;
    private final ActorRef actor;

}
