# Drawing App – Design Patterns & AOP Exam Project

This project is a small **drawing application** used as an exam/TP to practice:

- Object-oriented design  
- GoF design patterns (Composite, Strategy, Observer, Facade-like root, etc.)  
- A simple security model (roles & users)  
- Aspect-Oriented Programming (logging + security via annotations)

The code is written in **Java** and organized so the patterns are easy to see and to export as UML diagrams.

---

## 1. Project Structure

Main package root: `net.saadbr.dessin`

```text
src/
└── main/
    └── java/
        └── net/
            └── saadbr/
                └── dessin/
                    ├── DemoApp.java              # Demo / entry point
                    ├── model/
                    │   ├── Point.java
                    │   ├── Figure.java
                    │   ├── Cercle.java
                    │   ├── Rectangle.java
                    │   ├── GroupeFigures.java
                    │   ├── Parametrage.java
                    │   └── Dessin.java
                    ├── strategy/
                    │   ├── TraitementFiguresStrategy.java
                    │   ├── AffichageFiguresStrategy.java
                    │   ├── CalculSurfaceTotaleStrategy.java
                    │   └── CalculPerimetreTotaleStrategy.java
                    ├── security/
                    │   ├── Role.java
                    │   ├── User.java
                    │   ├── SecurityContext.java
                    │   └── Secured.java
                    └── aop/
                        ├── LoggingAspect.java
                        └── SecurityAspect.java
```

You can generate UML class diagrams directly from these packages in IntelliJ.

---

## 2. Domain Model

### 2.1 Core concepts

- **Point** – simple `(x, y)` coordinate.
- **Figure** – root interface for all drawable shapes.
- **Cercle** – concrete figure defined by a center point, radius and current `Parametrage`.
- **Rectangle** – concrete figure defined by top-left corner, width, height and `Parametrage`.
- **GroupeFigures** – a figure that contains other figures (a composite of figures).

All figures typically expose:

- `dessiner()` – prints the figure with its current style.
- `calculerSurface()` – returns area.
- `calculerPerimetre()` – returns perimeter.

### 2.2 Parametrage (global drawing settings)

`Parametrage` represents global drawing parameters:

- `epaisseurContour` – line thickness  
- `couleurContour`  
- `couleurRemplissage`

Whenever one of these values changes, `Parametrage` notifies all registered observers (figures) so they can react to the new style (for example, next `dessiner()` calls reflect the new colors).

### 2.3 Dessin (root aggregate)

`Dessin` is the **root object** of the model:

- Has a name (`nom`)  
- Holds a `Parametrage`  
- Contains a list of `Figure` (`Cercle`, `Rectangle`, `GroupeFigures`, …)  
- Exposes operations:
  - `ajouterFigure(Figure f)`
  - `supprimerFigure(Figure f)`
  - `afficherFigures()`
  - `traiter()` – apply the current processing strategy on all figures
  - `serialiserDansFichier(String fileName)` – persist the drawing as a binary file
  - `static Dessin deserialiserDepuisFichier(String fileName)` – load a drawing from file

---

## 3. Design Patterns Used

### 3.1 Composite (drawing hierarchy)

**Goal:** treat simple figures and groups of figures in a uniform way.

- `Figure` – component interface  
- `Cercle`, `Rectangle` – leaf components  
- `GroupeFigures` – composite that holds a `List<Figure>` and also implements `Figure`

Client code (and `Dessin`) can work with a `Figure` without caring whether it is a single shape or a group.

---

### 3.2 Observer (Parametrage → Figures)

**Goal:** automatically propagate parameter changes to all figures, with low coupling.

- **Subject:** `Parametrage`  
  Holds a list of observers and exposes:
  - `ajouterObserver(...)`
  - `supprimerObserver(...)`
  - `notifierObservers()`

- **Observers:** each figure (or a helper) implements an observer interface and updates its internal style when notified.

Whenever a setter on `Parametrage` is called (`setEpaisseurContour`, `setCouleurContour`, `setCouleurRemplissage`), it calls `notifierObservers()`, and figures update their parameters.

---

### 3.3 Strategy (processing operations on the drawing)

**Goal:** plug different “treatments” of a drawing without changing `Dessin`.

- `TraitementFiguresStrategy` – strategy interface:

  ```java
  public interface TraitementFiguresStrategy {
      void traiter(List<Figure> figures);
  }
  ```

- `AffichageFiguresStrategy` – prints all figures (delegates to `dessiner()`).
- `CalculSurfaceTotaleStrategy` – computes the total area of all figures.
- `CalculPerimetreTotaleStrategy` – computes the total perimeter of all figures.

`Dessin` contains:

```java
private transient TraitementFiguresStrategy traitementFiguresStrategy;
```

and

```java
public void traiter() {
    if (traitementFiguresStrategy == null) {
        System.out.println("No strategy defined for drawing " + nom);
        return;
    }
    traitementFiguresStrategy.traiter(figures);
}
```

Example usage in `DemoApp`:

```java
dessin.setTraitementFiguresStrategy(new AffichageFiguresStrategy());
dessin.traiter();

dessin.setTraitementFiguresStrategy(new CalculSurfaceTotaleStrategy());
dessin.traiter();

dessin.setTraitementFiguresStrategy(new CalculPerimetreTotaleStrategy());
dessin.traiter();
```

---

### 3.4 Serialization (drawing persistence)

`Dessin` implements `Serializable` and provides:

- `serialiserDansFichier(String fileName)` – saves the drawing using `ObjectOutputStream`.
- `deserialiserDepuisFichier(String fileName)` – loads a drawing using `ObjectInputStream`.

The strategy field is marked `transient` and can be reconfigured after deserialization if needed.

---

## 4. Security Model & AOP

The project also demonstrates how to express cross-cutting concerns (logging and authorization) using annotations and aspects.

### 4.1 Roles, users and security context

Package: `net.saadbr.dessin.security`

- `Role` – enum of roles, typically `USER` and `ADMIN`.
- `User` – simple user object with:
  - `username`
  - `password`
  - `Set<Role> roles`
- `SecurityContext` – static utility based on `ThreadLocal<User>`:
  - `setCurrentUser(User u)`
  - `getCurrentUser()`
  - `clear()`
  - `isAuthenticated()`

The application (e.g. `DemoApp`) sets the current user at the beginning:

```java
User admin = new User("saad", "1234", Set.of(Role.ADMIN, Role.USER));
SecurityContext.setCurrentUser(admin);
```

### 4.2 `@Secured` annotation

`Secured` is a custom annotation used to declare required roles on methods or classes:

```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Secured {
    Role[] value();
}
```

`Dessin` methods are annotated, for example:

```java
@Secured({Role.USER, Role.ADMIN})
public void ajouterFigure(Figure figure) { ... }

@Secured(Role.ADMIN)
public void serialiserDansFichier(String fileName) { ... }
```

This expresses the security policy declaratively.

### 4.3 AOP Aspects

Package: `net.saadbr.dessin.aop`

#### LoggingAspect

Aspect that logs every call to a method of `Dessin`:

```java
@Aspect
public class LoggingAspect {

    @Around("execution(* net.saadbr.dessin.model.Dessin.*(..))")
    public Object logAroundDessinMethods(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();
        System.out.println("[LOG] Begin: " + methodName);
        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            System.out.println("[LOG] End: " + methodName + " (" + duration + " ms)");
            return result;
        } catch (Throwable ex) {
            System.out.println("[LOG] Exception in " + methodName + " -> " + ex.getMessage());
            throw ex;
        }
    }
}
```

#### SecurityAspect

Aspect that enforces the `@Secured` annotation by checking the current user’s roles:

```java
@Aspect
public class SecurityAspect {

    @Before("@annotation(secured)")
    public void checkSecurity(JoinPoint joinPoint, Secured secured) {
        User user = SecurityContext.getCurrentUser();

        if (user == null) {
            throw new SecurityException("Access denied: no authenticated user");
        }

        Role[] requiredRoles = secured.value();
        for (Role role : requiredRoles) {
            if (user.hasRole(role)) {
                return; // OK
            }
        }

        throw new SecurityException(
            "Access denied for user " + user.getUsername() +
            " on " + joinPoint.getSignature().toShortString()
        );
    }
}
```

This moves security checks **out of the business code** and into a reusable aspect, which is the goal of AOP.

> Note: depending on the environment, AspectJ weaving (load-time or compile-time) can be enabled to actually run these aspects. For the exam, the important part is to show the pattern and the separation of concerns.

---

## 5. Demo Application

`DemoApp` is a simple console application that:

1. Creates a user and puts it into the `SecurityContext`.
2. Creates a `Parametrage` and a `Dessin`.
3. Adds a few figures (`Cercle`, `Rectangle`) to the drawing.
4. Runs different strategies:
   - display all figures,
   - compute total area,
   - compute total perimeter.
5. Serializes the drawing to `dessin.bin`.

Example output (without AOP logs):

```text
Affichage de toutes les figures :
Cercle{...}
Rectangle{...}
Surface totale du dessin = ...
Périmètre total du dessin = ...
Dessin sérialisé dans le fichier : dessin.bin
```

---

## 6. How to Run

### Requirements

- Java 17+  
- Maven (if you want to build via CLI)  
- IntelliJ IDEA (recommended) or any Java IDE

### Build

From the project root:

```bash
mvn clean compile
```

### Run from IntelliJ

1. Open the project in IntelliJ.
2. Create a run configuration for the main class:
   - `Main class`: `net.saadbr.dessin.DemoApp`
3. Run it: you should see console output similar to the example above.

> If you want to actually weave the aspects and see `[LOG] ...` messages, you can integrate AspectJ (either via the `aspectj-maven-plugin` for compile-time weaving or via `aspectjweaver` as a Java agent). For the exam, the code of the aspects + annotations is usually enough.

---

## 7. Summary of Patterns

- **Composite** – `Figure`, `Cercle`, `Rectangle`, `GroupeFigures`  
  Treat individual figures and groups uniformly.

- **Observer** – `Parametrage` (subject) and registered figures (observers)  
  Figures are notified when drawing parameters change.

- **Strategy** – `TraitementFiguresStrategy` + its implementations  
  `Dessin.traiter()` delegates to a pluggable strategy that can be changed at runtime.

- **Facade-like root & serialization** – `Dessin`  
  Acts as the façade/root aggregate that gathers the model operations and persistence.

- **Security with annotations + AOP** – `@Secured`, `SecurityAspect`, `LoggingAspect`  
  Cross-cutting concerns (authorization & logging) are expressed outside the core model.
