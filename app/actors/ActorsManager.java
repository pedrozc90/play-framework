package actors;

import actors.files.FileDispatcherActor;
import akka.actor.ActorRef;
import domain.jobs.Job;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class ActorsManager {

    private final Logger.ALogger logger = Logger.of(ActorsManager.class);

    private final Provider<ActorRef> supervisorActorProvider;

    @Inject
    public ActorsManager(@Named("SupervisorActor") final Provider<ActorRef> supervisorActorProvider) {
        this.supervisorActorProvider = supervisorActorProvider;
    }

    public void queue(final Job job) {
        logger.info("Queueing job {}", job.getId());
        final ActorRef supervisor = supervisorActorProvider.get();
        final FileDispatcherActor.Enqueue enqueue = new FileDispatcherActor.Enqueue(job.getId());
        final SupervisorActor.Forward forward = new SupervisorActor.Forward(FileDispatcherActor.class, enqueue);
        supervisor.tell(forward, ActorRef.noSender());
    }

}
