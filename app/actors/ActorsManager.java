package actors;

import actors.files.FileDispatcherActor;
import akka.actor.ActorRef;
import com.google.inject.name.Named;
import domain.jobs.Job;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActorsManager {

    private final Logger.ALogger logger = Logger.of(ActorsManager.class);

    private final ActorRef supervisor;

    @Inject
    public ActorsManager(@Named("SupervisorActor") final ActorRef supervisor) {
        this.supervisor = supervisor;
    }

    public void queue(final Job job) {
        logger.info("Queueing job {}", job.getId());
        final FileDispatcherActor.Enqueue enqueue = new FileDispatcherActor.Enqueue(job.getId());
        final SupervisorActor.Forward forward = new SupervisorActor.Forward(FileDispatcherActor.class, enqueue);
        supervisor.tell(forward, ActorRef.noSender());
    }

}
