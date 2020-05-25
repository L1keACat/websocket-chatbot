#### Requirements

- **Java 8**
- **Gradle**
- **MongoDB**
- **AngularJS**

#### Build the project

In order to build the project edit _**application.properties**_ file to use your database.

In order to build the project itself run:

```
./gradlew build
```

#### Run the server-side part:

```
./gradlew bootRun
```

#### Run the client-side part:

Go to angular-client folder and run:
```
npm install
```
```
ng serve --open
```
Access http://localhost:4200

#### Used technologies:
- **Java**
- **Gradle**
- **Spring** (Boot, Data MongoDB)
- **WebSocket**
- **MongoDB** (as database)
- **AngularJS** (as client)