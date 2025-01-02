### Functional Requirements
- ✅ View to list xml imported data 
- ✅ Filter by basic
- ✅ Sort by field
- ✅ View to show details of a inspection

### Non-Functional Requirements
- ✅ Optimize import speed
- ✅ Optimize inspections to filter and order
- ✅ Expose only necessary data


### Architecture
![onion-architecture.png](.docs/onion-architecture.png)

The application follows the onion architecture pattern, all processes flow from outside to inside, presenter to application, application to domain.
The application was split in 4 different layers:
1. **Presentation**: Handles data input and output, no matter whether this is http or any order process trigger, such as queues or command lines
2. **Application**: Handles process logics, orchestrates the process through the domain, loading and storing entities. 
3. **Domain**: Handles business logic by controlling how entities state should change and how these entities are loaded and stored through interfaces 
4. **Infrastructure**: Implements domain interfaces and offers connection to external applications or services. Infrastructure dependencies are configured in the config module and injected into the required interfaces

### Decisions made:
1. Bring file content to the infrastructure and convert to domain objects: Following clean arch and onion architecture, the domain should not depend on vendor data, meaning
   that the vendor could be easily replaced by another vendor, all it requires it fulfilling interface needs 
2. Property naming: Again it is important to isolate the domain from vendor implementation so it could be replaced at any time, also the specifics of the external party stays in their own implementation
   for instance, booleans are either `Y` or `Yes` and `N` or `No`, and in the end the code requires a boolean property
3. Provider IDs as domain IDs: In order to make sure same record is not imported twice, their ID becomes an idempotency key, the table constraints make sure they are ignored.
   It is very unlikely that different providers would collapse, should it happen then a unique index could be added along with the provider
4. Backend stack: The whole implementation could be done with different programming languages, Kotlin has its coroutines that runs on threads and boost parallel processing performance.
   [http4k](https://www.http4k.org/) is a lightweight http framework and helps expose endpoints and handling requests in an easy way.
   It could be easily replaced by other http frameworks with more robust functionalities
5. Mysql database: not an additional context about it, just standard relational database that delivers what was needed 

### Further Improvements
1. Extend vehicles details: Crash data is already imported, but there are no functionalities to show these data, the vehicle list could be clickable and navigate
   to more details about the vehicle
2. Improve error handling: Backend handles some errors but frontend doesn't handle it 
3. Authentication & Authorization: it was possible to see that users might resolve violations and assign to users, the auth system would allow implementing such functionalities
4. Extend driver data: Driver data could be imported and related to existing views
5. Filling missing gaps: The frontend suggestion has data that was not visible in the report, such as facility, it could be gathered somewhere else and showed in the frontend

## Running the application
I'm afraid there is no migration management, so you will have to run a few commands before start trying the app:
```shell
# to start
make up

# to stop
make down
```

### Running tests
Tests might be run by the makefile or the gradle wrapper, however make command will also check linting.
```shell
make test
./gradlew test clean
```


### JDK Environment
```
openjdk 17.0.9 2023-10-17
OpenJDK Runtime Environment GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22)
OpenJDK 64-Bit Server VM GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22, mixed mode, sharing)
```
