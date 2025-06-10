package actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.Logger;
import play.libs.Akka;
import repositories.PurchaseOrderRepository;

public class ActorManager {

    private static final Logger.ALogger logger = Logger.of(ActorManager.class);

    private static ActorRef schedulerActor;

    public static void init() {
        final ActorSystem system = Akka.system();

        final PurchaseOrderRepository repository = new PurchaseOrderRepository();

        // Create the scheduler actor with supervision strategy
        schedulerActor = system.actorOf(SchedulerActor.props(repository), "SchedulerActor");

        // Start the scheduler by sending initial message
        schedulerActor.tell(SchedulerActor.CheckOrders.INSTANCE, null);

        logger.info("ActorManager initialized with scheduler");
    }

    public static ActorRef getSchedulerActor() {
        return schedulerActor;
    }

}
