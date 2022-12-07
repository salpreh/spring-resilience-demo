# spring-resilience-demo
Basic SpringBoot project showing implementation of resilience patterns from [resilience4j](https://github.com/resilience4j/resilience4j).
The project contains implementation using standalone library configuration and spring integration.
For each pattern there is a controller with two endpoint to execute an example with native and spring version. For each pattern you can also find a dedicated configuration and use case class.

## Implemented patterns
- Fallback
- Circuit breaker
- Rate limiter
- Time limiter
- Bulkhead

## Getting started
Run Application.java within your IDE or execute `mvn spring-boot:run` in a shell.
