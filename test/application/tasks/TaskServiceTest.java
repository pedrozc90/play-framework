package application.tasks;

import core.exceptions.AppException;
import core.utils.http.HttpStatus;
import domain.tasks.TaskStatus;
import org.junit.Test;
import play.db.jpa.JPA;
import play.libs.F;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Regression coverage: TaskService.update() must reject a missing task with an
 * AppException instead of throwing a NullPointerException. Requires a reachable
 * Postgres with evolutions applied (e.g. `docker-compose up -d db`).
 */
public class TaskServiceTest extends WithApplication {

    private final TaskService service = TaskService.getInstance();

    @Test
    public void updatingMissingTaskThrowsInsteadOfNpe() throws Throwable {
        JPA.withTransaction(new F.Function0<Void>() {
            @Override
            public Void apply() throws Throwable {
                try {
                    service.update(-1L, TaskStatus.DONE);
                    fail("Updating a non-existent task should throw AppException");
                } catch (AppException e) {
                    assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
                }
                return null;
            }
        });
    }

}
