# Hospital Management System

A desktop application built with Java and Swing that handles day-to-day hospital operations — managing patients, doctors, appointments, and bed allocation across three user roles.

---

## What it does

The system gives each role a tailored interface:

**Admin** — full access to everything. Can register and remove patients and doctors, schedule or cancel appointments, manage bed inventory, and see a live analytics summary.

**Doctor** — sees their own appointment schedule, assigned patients, and any notifications sent to them. Can record a diagnosis, prescription, and notes when completing an appointment.

**Patient** — read-only view of their own appointments (including diagnosis and prescription once filled), admission history, and notifications.

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI framework | Java Swing |
| Data storage | JSON files via Gson 2.10 |
| Concurrency | `ReentrantReadWriteLock` |

No external database, no web server, no framework. The application is entirely self-contained and runs on any machine with a JDK installed.

---

## Project structure

```
src/com/hospital/
├── HospitalApp.java              Entry point
├── model/                        Data classes (User, Doctor, Patient, etc.)
├── data/                         File I/O — generic JSON repository + type adapters
├── service/                      Business logic (auth, scheduling, validation)
├── ui/                           Swing frames and custom components
├── util/                         ID generation, validation, data seeding
└── exception/                    Domain-specific exceptions
```

The application follows a three-layer architecture: the UI layer calls service methods, services call repositories, and repositories handle all file reading and writing. The UI never touches a file directly.

---

## Running the project

**Prerequisites:** JDK 21, Gson 2.10.1 jar (included in `lib/`)

```bash
# Compile
javac -cp "lib/gson-2.10.1.jar" -d out $(find src -name "*.java")

# Run
java -cp "out:lib/gson-2.10.1.jar" com.hospital.HospitalApp
# Windows:
java -cp "out;lib/gson-2.10.1.jar" com.hospital.HospitalApp
```

On first run, `DataInitializer` creates the `data/` directory and seeds it with a default admin account, three doctors, two patients, and ten beds so you can log in immediately.

---

## Default login credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Doctor | `doctor1` | `doc123` |
| Patient | `patient1` | `pat123` |

---

## Data storage

All records are stored as JSON files in the `data/` directory:

```
data/
├── users.json
├── patients.json
├── appointments.json
├── beds.json
├── admissions.json
└── notifications.json
```

Gson handles serialisation and deserialisation. A custom `UserDeserializer` is registered so that the abstract `User` class resolves to the correct concrete type (`Admin`, `Doctor`, or `Patient`) when loading from file. `LocalDate` and `LocalDateTime` fields use dedicated type adapters.

File access is guarded by a `ReentrantReadWriteLock` — multiple reads can proceed concurrently, but writes are exclusive.

---

## OOP concepts

| Concept | Where it appears |
|---|---|
| Inheritance | `Admin`, `Doctor`, `Patient` extend abstract `User` |
| Encapsulation | All model fields are private; accessed through getters and setters |
| Polymorphism | `getDisplayInfo()` is overridden in each subclass; `instanceof` used for role-based routing |
| Abstraction | `User` and `JsonRepository<T>` are abstract; they define contracts that subclasses fulfil |
| Generics | `JsonRepository<T>` provides CRUD for any entity type |

---

## Input validation

Patient and doctor forms enforce:
- Username: 4–20 characters, letters/digits/underscores only
- Password: minimum 6 characters
- Email: basic format check (`user@domain.tld`)
- Phone and emergency contact: exactly 10 digits (enforced at keystroke level via `DocumentFilter`)
- Age: 0–150
- Experience: 0–60 years

---

## Known limitations

- Passwords are stored as plain text. Acceptable for an academic project; in production these would be hashed with BCrypt.
- JSON file storage does not support complex queries or large datasets. A proper database (SQLite, PostgreSQL) would be the next step for a production version.
- The application is single-user and local-only — there is no network layer.
