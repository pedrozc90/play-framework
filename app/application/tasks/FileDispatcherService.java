package application.tasks;

import actors.files.FileProcessorActor;
import application.jobs.JobService;
import domain.files.FileStorage;
import domain.jobs.Job;
import domain.tasks.Task;
import play.Logger;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Queue;

@Singleton
public class FileDispatcherService {

    private final Logger.ALogger logger = Logger.of(FileDispatcherService.class);

    private final JPAApi jpa;
    private final TaskService taskService;
    private final JobService jobService;

    @Inject
    public FileDispatcherService(
        final JPAApi jpa,
        final TaskService taskService,
        final JobService jobService
    ) {
        this.jpa = jpa;
        this.taskService = taskService;
        this.jobService = jobService;
    }

    public void completeJob(final Long jobId, final Queue<FileProcessorActor.Command> queue) {
        jpa.withTransaction((em) -> {
            final Job job = jobService.get(em, jobId);
            job.setStatus(Job.Status.PROCESSING);

            final FileStorage fs = job.getFile();

            final List<Task> tasks = taskService.generateAll(em, job);

            for (Task task : tasks) {
                final FileProcessorActor.Command command = new FileProcessorActor.Command(task.getId(), task.getType(), fs.getContent(), fs.getFilename(), fs.getExtension());
                queue.add(command);
            }

            return null;
        });
    }

}
