package expense.manager.gateway.filters;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    private Tracer tracer;


    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Span span = tracer.currentSpan();
            if (span != null) {
                String traceId = span.context().traceId();
                logger.info("setting trace id response. {}",
                        traceId);
                exchange.getResponse().getHeaders().add("X-Trace-Id", traceId);
            }
        }));
    }

}
