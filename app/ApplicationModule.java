import actors.SupervisorActor;
import com.google.inject.AbstractModule;
import play.Logger;
import play.libs.akka.AkkaGuiceSupport;
import repositories.UserRepository;
import repositories.UserRepositoryImpl;

// A Module is needed to register bindings
public class ApplicationModule extends AbstractModule implements AkkaGuiceSupport {

    private final Logger.ALogger logger = Logger.of(ApplicationModule.class);

    @Override
    protected void configure() {
        logger.info("Configuring bindings...");

        // eager singletons are created immediately at startup
        bind(ApplicationStart.class).asEagerSingleton();

        // interfaces
        bind(UserRepository.class).to(UserRepositoryImpl.class).asEagerSingleton();

        // actors
        bindActor(SupervisorActor.class, "SupervisorActor");
    }

}
