package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ActorManager {

    private static final Logger.ALogger logger = Logger.of(ActorManager.class);

    private static Map<Class<?>, ActorRef> actors = new HashMap<>();

    public static void init() {
        final ActorSystem system = Akka.system();

        final FiniteDuration delay = Duration.create(10, TimeUnit.SECONDS);
        system.scheduler().scheduleOnce(delay, () -> createActors(system), system.dispatcher());
    }

    private static void createActors(final ActorSystem system) {
        initSchedulerActor(system);
        // initSingleProcessorActor(system);
        logger.info("ActorManager initialized with scheduler");
    }

    private static void initSchedulerActor(final ActorSystem system) {
        // Create the scheduler actor with supervision strategy
        final ActorRef ref = system.actorOf(SchedulerActor.props(), "SchedulerActor");

        // Start the scheduler by sending initial message
        ref.tell(new SchedulerActor.ResumeOngoingOrders(), ActorRef.noSender());

        actors.put(SchedulerActor.class, ref);
    }

    private static void initSingleProcessorActor(final ActorSystem system) {
        // Create the scheduler actor with supervision strategy
        final ActorRef ref = system.actorOf(SingleOrderProcessorActor.props(), "SingleOrderProcessorActor");

        // Start the scheduler by sending initial message
        ref.tell(new SingleOrderProcessorActor.Tick(), ActorRef.noSender());

        actors.put(SchedulerActor.class, ref);
    }

    public static ActorRef actor(final Class<?> clazz) {
        return actors.get(clazz);
    }

}
