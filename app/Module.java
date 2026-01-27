import actors.SupervisorActor;
import com.google.inject.AbstractModule;
import play.Logger;
import play.libs.akka.AkkaGuiceSupport;

/**
 * A Module is needed to register bindings
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {

    private final Logger.ALogger logger = Logger.of(Module.class);

    @Override
    protected void configure() {
        logger.info("Configuring module");
        // application entrypoint
        bind(Entrypoint.class).asEagerSingleton();

        // actors
        bindActor(SupervisorActor.class, "SupervisorActor");
    }

}
