package expense.manager.gateway;

import jakarta.ws.rs.core.HttpHeaders;
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
        return builder.routes()
                .route("currency-service", p -> p.path("/currencies/**")
                        .filters(f -> {
                            f.tokenRelay();
                            f.removeRequestHeader(HttpHeaders.COOKIE);
                            return f;
                        })
                        .uri("lb://CURRENCY-SERVICE"))
                .route("wallet-service", p -> p.path("/wallets/**")
                        .filters(f -> {
                            f.tokenRelay();
                            f.removeRequestHeader(HttpHeaders.COOKIE);
                            return f;
                        })
                        .uri("lb://WALLET-SERVICE"))
                .route(p -> p.path("/swagger-ui/**")
                        .uri("lb://CURRENCY-SERVICE"))
                .route(p -> p.path("/openapi/wallets-doc/**")
                        .or()
                        .query("urls.primaryName", "^wallet")
                        .uri("lb://WALLET-SERVICE"))
                .route(p -> p.path("/openapi/currencies-doc/**")
                        .or()
                        .query("urls.primaryName", "^currency")
                        .uri("lb://CURRENCY-SERVICE"))
                .build();
    }

}