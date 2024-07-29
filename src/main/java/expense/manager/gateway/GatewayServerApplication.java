package expense.manager.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class GatewayServerApplication {

	public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes().route(p -> p.path("/currencies/**").uri("lb://CURRENCY-SERVICE"))
				.route(p -> p.path("/wallets/**")
						//.filters(f -> f.rewritePath("/currencies/(?<segment>.*)", "/${segment}"))
						.uri("lb://WALLET-SERVICE"))
				.route(p -> p.path("/swagger-ui/**")
						//.filters(f -> f.rewritePath("/currencies/(?<segment>.*)", "/${segment}"))
						.uri("lb://CURRENCY-SERVICE"))
				.route(p -> p.path("/openapi/wallets-doc/**").or().query("urls.primaryName", "^wallet")
						.uri("lb://WALLET-SERVICE"))
				.route(p -> p.path("/openapi/currencies-doc/**").or().query("urls.primaryName", "^currency")
						.uri("lb://CURRENCY-SERVICE"))
				.build();
	}

}