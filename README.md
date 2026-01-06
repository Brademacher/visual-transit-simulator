# Transit System Simulator

A Java-based transit system simulation focused on object-oriented design, refactoring,
design patterns, and automated testing.

This project models a public transit system with vehicles, routes, stops, and passengers,
and includes a web-based visualization layer to observe the simulation in action.

---

## Overview

The simulator represents a dynamic transit environment where buses and trains move along
routes, pick up and drop off passengers, and report state updates through a web interface.
The codebase emphasizes clean object-oriented structure, extensibility, and testability.

A significant portion of the work involved refactoring an existing codebase to improve
modularity and maintainability, extending behavior using design patterns without
modifying core classes, and writing unit and integration tests.

---

## Technical Focus

- Object-Oriented Programming (encapsulation, inheritance, polymorphism)
- Refactoring for separation of concerns and maintainability
- Design patterns, including the **Decorator pattern**
- Automated unit and integration testing with **JUnit** and **Mockito**
- Build and dependency management using **Gradle**

---

## Key Contributions

- Refactored core simulation components to improve modularity and readability
- Applied the Decorator pattern to extend vehicle behavior without altering base classes
- Implemented and maintained unit tests for model and webserver logic
- Worked with Gradle to manage builds and run tests consistently
- Integrated simulation logic with a web-based visualization layer

---

## Project Structure
app/
src/main/java/
model/ # Core simulation logic (vehicles, routes, passengers, etc.)
webserver/ # Command handling and simulation/web interface logic
src/test/java/ # Unit tests for model and webserver components
src/main/webapp # Visualization assets (HTML/JS)
gradle/ # Gradle wrapper and configuration
images/ # Screenshots and diagrams

---

## Running the Project

### Prerequisites
- Java (JDK 17 or compatible)
- No global Gradle installation required (uses Gradle Wrapper)

### Build and Test
From the project root:

### bash
./gradlew build
(On Windows, use gradlew.bat build)

To start the visualization module, open a browser and paste 
this link http://localhost:7777/project/web_graphics/project.html in its search bar.

## Notes

This project was originally developed as part of a university software engineering course.
This repository is a cleaned, standalone mirror created for portfolio and interview purposes.
Course-specific infrastructure and artifacts have been removed.

## Resources
* [A Guide to the Java API for WebSocket](https://www.baeldung.com/java-websockets)
* [JSON objects](https://www.w3schools.com/js/js_json_objects.asp)
* [Google Maps](http://maps.google.com)
* [Reading and Writing CSVs in Java](https://stackabuse.com/reading-and-writing-csvs-in-java)
* [RGBA format](https://www.w3schools.com/css/css_colors_rgb.asp)
