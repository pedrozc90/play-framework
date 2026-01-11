import actors.SupervisorActor;
import com.google.inject.AbstractModule;
import core.play.filters.Filters;
import play.Logger;
import play.libs.akka.AkkaGuiceSupport;

// A Module is needed to register bindings
public class ApplicationModule extends AbstractModule implements AkkaGuiceSupport {

    private final Logger.ALogger logger = Logger.of(ApplicationModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring module");
        bind(ApplicationStart.class).asEagerSingleton();
        bind(Filters.class).asEagerSingleton();

        // actors
        bindActor(SupervisorActor.class, "SupervisorActor");
    }

}
