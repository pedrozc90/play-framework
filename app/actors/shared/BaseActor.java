package actors.shared;

import actors.messages.Heartbeat;
import actors.messages.HeartbeatAck;
import actors.messages.Init;
import akka.actor.AbstractLoggingActor;
import akka.actor.Terminated;
import play.Logger;

public abstract class BaseActor extends AbstractLoggingActor {

    protected final Logger.ALogger logger;

    protected BaseActor() {
        logger = Logger.of(getClass());
    }

    @Override
    public void preStart() {
        logger.debug("Actor started");
        // send message to itself to initialize it
        self().tell(new Init(), self());
    }

    @Override
    public void postStop() {
        logger.debug("Actor stopped");
    }

    protected void onInit(final Init obj) {
        logger.info("Init message received: {}", obj);
    }

    protected void onHeartbeat(final Heartbeat obj) {
        logger.info("Heartbeat received: {}", obj);
    }

    protected void onHeartbeatAck(final HeartbeatAck obj) {
        logger.info("HeartbeatAck received: {}", obj);
    }

    protected void onTerminated(final Terminated obj) {
        logger.info("Terminated message received: {}", obj);
    }

    protected void onUnknownMessage(final Object obj) {
        logger.info("Unknown message received: {}", obj);
    }

}
