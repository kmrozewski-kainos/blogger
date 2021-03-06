import configuration.BloggerAppConfiguration;
import configuration.BloggerModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import ru.vyarus.dropwizard.guice.GuiceBundle;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class BloggerApp extends Application<BloggerAppConfiguration> {

	private GuiceBundle<BloggerAppConfiguration> guiceBundle;

	private BloggerModule bloggerModule = new BloggerModule();

	@Override
	public void initialize(Bootstrap<BloggerAppConfiguration> bootstrap) {
		guiceBundle = GuiceBundle.<BloggerAppConfiguration>builder()
				.modules(bloggerModule)
				.enableAutoConfig("main") // autoscan
				.searchCommands()
				.build();

		bootstrap.addBundle(guiceBundle);
	}

	@Override
	public void run(BloggerAppConfiguration configuration, Environment environment) throws Exception {
		enableCors(environment);
	}

	public static void main(String[] args) {
		try {
			new BloggerApp().run(args);
		} catch (Exception e) {
			throw new RuntimeException("Application failed!", e);
		}
	}

	private void enableCors(Environment environment) {
		final FilterRegistration.Dynamic cors =
				environment.servlets().addFilter("CORS", CrossOriginFilter.class);

		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "*");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}
}
