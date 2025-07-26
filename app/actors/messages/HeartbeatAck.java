package actors.messages;

import akka.actor.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeartbeatAck {

    private ActorRef actor;

}
