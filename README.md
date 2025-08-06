# quarkflix-cdi-interceptor

## What are CDI Interceptors?

In a nutshell, CDI (Contexts and Dependency Injection) interceptors allow you to add cross-cutting logic 
(like logging, security checks, or transaction management) to your business methods in a clean and reusable way. 
They work by "intercepting" method calls, allowing you to execute code before, after, or around the actual method execution. 
This is a core concept of Aspect-Oriented Programming (AOP).

Github: -> https://github.com/myfear/ejq_substack_articles/tree/main/quarkflix-guards?utm_source=substack&utm_medium=email


## run

-> mvn quarkus:dev

audit log -> view links and watch console log output:
+ http://localhost:8080/stream/play?user=Alice&movie=Matrix
+ http://localhost:8080/stream/account?user=Bob
+ http://localhost:8080/stream/browse?user=Charlie

age verifier -> view links and watch console log output:
+ http://localhost:8080/stream/maturePlay?user=MinorTom&age=16&movie=Hostel = DENIED
+ http://localhost:8080/stream/maturePlay?user=AdultAnna&age=22&movie=Hostel = OK


monitoring -> view links and watch console log output:
+ http://localhost:8080/stream/continueWatching?user=Binger

vip user -> view links and watch console log output:
+ http://localhost:8080/stream/premiere?user=Victor&age=25&isVip=true&movie=NewHit = Access
+ http://localhost:8080/stream/premiere?user=Rita&age=25&isVip=false&movie=NewHit = non VIP - Access Denied
