# VillageWar

> **Build your village. Train your army. Conquer the realm.**

![CI](https://img.shields.io/badge/build-passing-brightgreen)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21.0.2-purple.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/build-Maven-blue.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/tests-28%20passing-brightgreen.svg)](https://github.com/VivekSalwan2000/Game-War-Advanced-OOPs)
[![Status](https://img.shields.io/badge/status-production--ready-success.svg)](https://github.com/VivekSalwan2000/Game-War-Advanced-OOPs)

A **multithreaded TCP client–server strategy game** with a polished **JavaFX desktop client** — a *Clash of Clans*–style village builder where players construct resource buildings, recruit workers and troops, upgrade their village hall, and battle AI-generated enemies over a reliable network protocol.

---

## 📖 Project Overview

**VillageWar** is a portfolio-grade Java desktop application that combines a rich domain model, network programming, and a modern JavaFX UI into a fully playable strategy game.

| | |
|---|---|
| **What it does** | Lets multiple players connect to a central server, each managing their own village — building structures, recruiting units, upgrading, and engaging in combat. |
| **Why it exists** | Demonstrates advanced OOP, design patterns, concurrency, and client–server architecture in pure Java — without Spring, Hibernate, or other heavy frameworks. |
| **Problem it solves** | Provides a complete, end-to-end reference for building a networked game with authoritative server state, a modular domain layer, and a responsive desktop frontend. |
| **Who it's for** | Recruiters evaluating Java/OOP skills, open-source contributors exploring game architecture, and developers learning JavaFX + TCP networking. |

---

## ⚡ Features

### 🖥️ JavaFX Desktop Client
- Dark-themed UI with FXML layouts and custom CSS
- **Main Menu** → **Connect Dialog** → **Village Dashboard** navigation flow
- Interactive village map with emoji tiles, tooltips, and live stats
- Modal dialogs for **building placement** (7 types) and **worker/troop recruitment** (7 types)
- Dedicated **Combat screen** for attack and defense scenarios with outcome display

### 🌐 Networking & Server
- Multi-client **TCP server** with thread-per-connection cached pool
- Authoritative **server-side game state** per connected client
- Typed request protocol via `RequestType` enum and Java object serialization
- Blocking I/O with ordered, reliable message delivery

### 🏰 Gameplay
- Construct farms, mines, lumber mills, and defensive structures (archer towers, cannons, catapults)
- Recruit farmers, miners, builders, collectors, soldiers, knights, and archers
- Upgrade village hall to unlock higher-level capabilities
- Attack randomly generated enemy villages and defend against random armies
- **Data-driven balance** via `levels.properties` (costs, limits, stats)

### 🧱 Architecture & Engineering
- **MVC** for village domain; **MVVM-style** separation in the JavaFX layer
- **Abstract Factory** for entity creation; **Adapter** layer for combat integration
- Concurrent entity factories with `Future`-based task awaiting (no busy-wait loops)
- **28 JUnit 5 tests** covering factories, controllers, combat adapters, and game engine
- **GitHub Actions CI** — JDK 17/21 matrix, `mvn verify`, artifact upload
- Runnable fat-jar (`villagewar.jar`) plus **console client** preserved for headless testing

---

## 🛠️ Tech Stack

| Category | Technology | Version |
|---|---|---|
| Language | Java | 17 |
| UI Framework | JavaFX (OpenJFX) | 21.0.2 |
| Build Tool | Apache Maven | 3.9+ |
| Testing | JUnit Jupiter | 5.10.2 |
| Networking | Java TCP / Object Streams | JDK built-in |
| Concurrency | `ExecutorService`, `AtomicInteger`, `Future` | JDK built-in |
| CI/CD | GitHub Actions | JDK 17 & 21 matrix |
| Packaging | Maven Shade Plugin | Fat JAR |

---

## 📁 Project Structure

```
Game-War-Advanced-OOPs/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/villagewar/ui/          # JavaFX frontend
│   │   │   │   ├── controller/             # FXML scene controllers
│   │   │   │   ├── service/                # GameClientService (async TCP)
│   │   │   │   ├── viewmodel/              # Observable UI state
│   │   │   │   └── util/                   # FXML loader, map nodes, alerts
│   │   │   ├── connection/                 # TCP server, client, protocol
│   │   │   │   ├── Server/                 # MultiClientServer, ClientHandler
│   │   │   │   ├── Clients/                # Client, RequestType
│   │   │   │   └── protocols/              # Protocol dispatch, ServerState
│   │   │   ├── Controllers/                # VillageController (domain MVC)
│   │   │   ├── Models/                     # Village model
│   │   │   ├── Views/                      # VillageView
│   │   │   ├── VillageElements/            # Buildings, workers, troops, map
│   │   │   ├── AbstractFactory/            # Entity creation factories
│   │   │   ├── ConcurrentAbstractFactory/  # Thread-safe factory variants
│   │   │   ├── ChallengeDecision/          # Combat arbiter engine
│   │   │   ├── Utility/                    # Adapter layer for combat
│   │   │   ├── Game/                       # GameEngine, AttackManager, ArmyUnit
│   │   │   └── concurrency/                # VillageControllerGenerator
│   │   └── resources/
│   │       ├── javafx/
│   │       │   ├── fxml/                   # 6 FXML layout files
│   │       │   └── css/villagewar.css      # Dark theme stylesheet
│   │       └── levels.properties           # Game balance configuration
│   └── test/java/                          # 28 JUnit 5 tests (5 test classes)
├── docs/
│   ├── architecture.md
│   ├── design-patterns.md
│   ├── networking.md
│   └── javafx-architecture.md
├── .github/workflows/ci.yml
└── pom.xml
```

---

## 🚀 Installation & Setup

### Prerequisites

| Requirement | Minimum |
|---|---|
| JDK | 17 or higher ([Eclipse Temurin](https://adoptium.net/) recommended) |
| Maven | 3.9+ |
| OS | macOS, Linux, or Windows |

Verify your environment:

```bash
java -version   # openjdk 17+ or 21+
mvn -version    # Apache Maven 3.9+
```

### Clone & Build

```bash
git clone https://github.com/VivekSalwan2000/Game-War-Advanced-OOPs.git
cd Game-War-Advanced-OOPs
mvn clean install
```

This compiles the project, runs all tests, and produces `target/villagewar.jar`.

### Run the Application

You need **two terminal windows** — server first, then client.

**Terminal 1 — Start the server:**

```bash
java -cp target/villagewar.jar connection.ServerRunner 8080
```

Expected output: `Server Is Running...`

**Terminal 2 — Launch the JavaFX client:**

```bash
mvn javafx:run
```

> **Note:** Use `mvn javafx:run` for the GUI. The shaded JAR's default entry point is the server, not the JavaFX launcher.

### Optional — Console Client

```bash
java -cp target/villagewar.jar connection.Client1Runner 8080 localhost
```

Follow the numbered text menu for headless testing alongside the GUI client.

---

## 🖥️ Application Usage

### Navigation Flow

```
Main Menu  →  Connect (host / port)  →  Dashboard  →  Actions
                                              ├── Add Building…
                                              ├── Recruit Worker…
                                              ├── Upgrade Village
                                              ├── Attack Village  →  Combat Screen
                                              ├── Defend Village  →  Combat Screen
                                              ├── Refresh
                                              └── Disconnect
```

### Getting Started In-Game

1. **Connect** — Enter `localhost` and port `8080` (must match the running server).
2. **Create Village** — Initializes your settlement with a Village Hall and starting resources (Level 1, 2000 gold/iron/wood, 500 food).
3. **Build** — Open **Add Building…** and place farms, mines, mills, or defensive structures. Each costs 100 lumber, iron, and gold.
4. **Recruit** — Open **Recruit Worker…** to hire farmers, miners, builders, collectors, or military units. Add farms first to increase population capacity.
5. **Upgrade** — Click **Upgrade Village** to level up your Village Hall (costs 100 of each resource).
6. **Combat** — Recruit at least one troop, then use **Attack Village** or **Defend Village** to see battle outcomes and loot details.
7. **Refresh** — Sync the latest village state from the server at any time.
8. **Disconnect** — Return to the main menu without stopping the server.

### Starting Village Stats

| Stat | Value |
|---|---|
| Level | 1 |
| Gold / Iron / Wood | 2,000 each |
| Food | 500 |
| Max Population | 5 |
| Buildings | Village Hall |

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Full Build Verification

```bash
mvn verify
```

This runs the complete test suite and packages the fat JAR.

### Test Coverage

| Test Class | Focus |
|---|---|
| `FactoryTest` | Abstract Factory entity creation |
| `VillageControllerTest` | Village CRUD and upgrade logic |
| `CombatAdapterTest` | Adapter layer for combat integration |
| `GameEngineTest` | Random village/army generation |
| `CollectedResourcesTest` | Resource collection mechanics |

**28 tests · 0 failures · JUnit 5 · Runs in CI on every push**

---

## 🧱 Architecture Overview

VillageWar uses a **layered architecture** with clear separation between networking, domain logic, and presentation.

```
┌─────────────────┐     ┌─────────────────┐
│  JavaFX Client  │     │  Console Client │
│  (MVVM + FXML)  │     │  (text menu)    │
└────────┬────────┘     └────────┬────────┘
         │                       │
         └───────────┬───────────┘
                     │  TCP / ObjectStream
              ┌──────▼──────┐
              │ ClientHandler│  (one thread per connection)
              └──────┬──────┘
              ┌──────▼──────┐
              │   Protocol   │  (RequestType dispatch)
              └──────┬──────┘
              ┌──────▼──────────┐
              │ VillageController│  (MVC Controller)
              └──────┬──────────┘
         ┌───────────┼───────────┐
    ┌────▼────┐ ┌────▼────┐ ┌───▼──────────────┐
    │ Village │ │ GameEng │ │ AbstractFactory  │
    │ (Model) │ │  ine    │ │ + Adapters       │
    └─────────┘ └─────────┘ └──────────────────┘
```

### Design Patterns

| Pattern | Where | Purpose |
|---|---|---|
| **MVC** | `Models` / `Views` / `Controllers` | Domain state and presentation separation |
| **MVVM** | `viewmodel` + `controller` + `service` | JavaFX observable state, off-UI-thread networking |
| **Abstract Factory** | `AbstractFactory.*` | Type-safe creation of buildings, workers, defences |
| **Adapter** | `Utility.*Adapter` | Bridge domain entities to the combat arbiter engine |
| **Enum Dispatch** | `RequestType` + `Protocol` | Typed network request routing |

### Key Interaction — JavaFX Action Flow

1. User clicks a button in an FXML **Controller**.
2. Controller delegates to `GameClientService` (background executor).
3. Service calls the shared `Client` TCP API.
4. Server `Protocol` dispatches to `VillageController`.
5. Updated `Village` state returns over the socket.
6. Service pushes results to **ViewModel** via `Platform.runLater`.
7. UI rebinds automatically through JavaFX properties.

Deep-dive documentation: [`docs/architecture.md`](docs/architecture.md) · [`docs/javafx-architecture.md`](docs/javafx-architecture.md) · [`docs/design-patterns.md`](docs/design-patterns.md) · [`docs/networking.md`](docs/networking.md)

---

## 🚀 Future Improvements

- [ ] Replace Java serialization with a **versioned JSON or Protobuf protocol**
- [ ] Decompose `Protocol` dispatch into the **Command pattern** (one handler per request)
- [ ] Apply combat **loot to village treasury** after battle resolution
- [ ] Per-building upgrade UI (currently village-hall upgrade only)
- [ ] **Persistence layer** (SQLite/H2) behind the existing database hook
- [ ] Graceful server shutdown and disconnected-client cleanup
- [ ] **Docker** image for one-command server deployment
- [ ] Spatial village map with grid placement instead of grouped tiles
- [ ] Animated combat sequences and sound effects in the JavaFX client
- [ ] Headless JavaFX smoke tests in CI

---

## 👨‍💻 Author & Credits

**Vivek Salwan**

[![GitHub](https://img.shields.io/badge/GitHub-VivekSalwan2000-181717?logo=github)](https://github.com/VivekSalwan2000)
[![Repository](https://img.shields.io/badge/repo-Game--War--Advanced--OOPs-blue?logo=github)](https://github.com/VivekSalwan2000/Game-War-Advanced-OOPs)

Built as an advanced OOP portfolio project demonstrating Java, JavaFX, Maven, TCP networking, design patterns, and concurrent server architecture.

### Third-Party Components

- **OpenJFX** — JavaFX UI framework ([openjfx.io](https://openjfx.io/))
- **ChallengeDecision / Arbitrer** — Integrated combat resolution engine
- **JUnit 5** — Unit testing framework

---

## 📄 License

Educational / portfolio project. See repository for usage terms.

---

<p align="center">
  <sub>Built with ☕ Java · 🎨 JavaFX · 📦 Maven</sub>
</p>
