package hello;


import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
//import static org.springframework.test.web.client.ExpectedCount.times;

public class Resilience4jTry1Test {

//    RemoteServiceImpl service = new RemoteServiceImpl();

//    Resilience4jTry1 rt1 = new Resilience4jTry1();

//    RemoteServiceImpl service = rt1.getService();

//    Function<Integer, Integer> decorated = rt1.resilience4jtry1();

    RemoteServiceImpl service = mock(RemoteServiceImpl.class);

    Function<Integer, Integer> decorated;

    @Before
    public void setUp() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(20)
                .ringBufferSizeInClosedState(5)
                .build();


        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("my");

        decorated = CircuitBreaker
                .decorateFunction(circuitBreaker, service::process);
    }

    @Test
    public void resilience4jTry1Test1() {
        when(service.process(any(Integer.class))).thenThrow(new RuntimeException());

        for (int i = 0; i < 10; i++) {
            try {
                decorated.apply(i);
            } catch (Exception ignore) {}
        }

        verify(service, times(5)).process(any(Integer.class));
    }
}
