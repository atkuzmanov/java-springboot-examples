package hello;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.util.function.Function;

/**
 * https://www.baeldung.com/resilience4j
 */
public class Resilience4jTry1 {

    public RemoteServiceImpl getService() {
        return service;
    }

    //    public Resilience4jTry1() {
//        System.out.println("<<>> decorated.apply: " + decorated.apply(1));
//    }
    RemoteServiceImpl service = new RemoteServiceImpl();

    public Function<Integer, Integer> resilience4jtry1() {


        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(20)
                .ringBufferSizeInClosedState(5)
                .build();


        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("my");
        Function<Integer, Integer> decorated = CircuitBreaker
                .decorateFunction(circuitBreaker, service::process);

        return decorated;
    }
}

interface RemoteService {
    int process(int i);
}

class RemoteServiceImpl implements RemoteService {

    @Override
    public int process(int i) {
//        i = i + 1;
//        i++;
//        ++i;
//        i = i+(i-(i-1));
//        i -= -(i / i); // Assuming i!=0
//        i -= i / i; // Assuming i!=0
        i -= -1;
        System.out.println("<<>> i: " + i);
        return i;
    }
}

