import com.haulcompliance.inspections.config.runMigrations
import com.haulcompliance.inspections.presenter.http.Container
import com.haulcompliance.inspections.presenter.http.configureRoutes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    runMigrations(Container.connection, Container.logger)

    configureRoutes()
        .asServer(Netty(8080))
        .start()
}
