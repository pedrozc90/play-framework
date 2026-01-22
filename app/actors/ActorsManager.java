package actors;

import actors.files.FileDispatcherActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import domain.jobs.Job;
import play.Logger;
import play.libs.Akka;

public class ActorsManager {

    private final Logger.ALogger logger = Logger.of(ActorsManager.class);

    private ActorRef supervisor;

    private static ActorsManager instance;

    public static ActorsManager getInstance() {
        if (instance == null) {
            instance = new ActorsManager();
        }
        return instance;
    }

    public void init() {
        final ActorSystem system = Akka.system();
        supervisor = createSupervisorActor(system);
    }

    private ActorRef createSupervisorActor(final ActorSystem system) {
        return system.actorOf(SupervisorActor.props(), "SupervisorActor");
    }

    public void queue(final Job job) {
        final FileDispatcherActor.Enqueue enqueue = new FileDispatcherActor.Enqueue(job.getId());
        final SupervisorActor.Forward forward = new SupervisorActor.Forward(FileDispatcherActor.class, enqueue);
        supervisor.tell(forward, ActorRef.noSender());
    }

}
