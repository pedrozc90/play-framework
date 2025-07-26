package actors.base;

import actors.messages.Heartbeat;
import actors.messages.HeartbeatAck;
import actors.messages.Init;
import actors.messages.Tick;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.japi.pf.UnitPFBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public abstract class BaseActor extends AbstractLoggingActor {

    public BaseActor() {
        receive(createReceive());
    }

    protected PartialFunction<Object, BoxedUnit> createReceive() {
        return defaultReceive()
            .matchAny((o)-> unknownHandler(o))
            .build();
    }

    protected UnitPFBuilder<Object> defaultReceive() {
        return ReceiveBuilder
            .match(Init.class, (o) -> onInit(o))
            .match(Tick.class, (o) -> onTick(o))
            .match(Heartbeat.class, (o) -> onHeartbeat(o))
            .match(HeartbeatAck.class, (o) -> onHeartbeatAck(o));
    }

    @Override
    public void preStart() throws Exception {
        log().info("{} started", self().path());
        self().tell(new Init(), self());
    }

    @Override
    public void postStop() throws Exception {
        log().info("{} stopped", self().path());
    }

    protected void onInit(final Init o) {

    }

    protected void onTick(final Tick o) {

    }

    protected void onHeartbeat(final Heartbeat obj) {
        log().info("Heartbeat received");
    }

    protected void onHeartbeatAck(final HeartbeatAck obj) {
        final ActorRef parent = context().parent();
        parent.tell(new HeartbeatAck(self()), self());
    }

    protected void unknownHandler(final Object obj) {
        log().error("Receive unknown message: {}", obj);
    }

}
