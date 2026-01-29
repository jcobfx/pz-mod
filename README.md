# Projekt Zespołowy Mod — PZMod

A Minecraft mod for **NeoForge 1.21.1** that adds items with advanced energy, fluid, and inventory capabilities.

## Features

- **Battery** — Stores up to 10,000 RF of energy. Left-click to charge, Shift+Left-click to discharge. Displays a visual energy bar.
- **Backpack** — Portable single-slot item storage. Right-click to insert/extract items from your offhand.
- **Big Bucket** — Holds up to 2,000 mB of any fluid. Pick up and place fluid source blocks in the world, with full sound and particle effects.

All items use a layered capability system built on NeoForge's `IEnergyStorage`, `IFluidHandler`, and `IItemHandler` interfaces, with persistent data stored via DataComponents.

## Requirements

- Java 21
- Minecraft 1.21.1
- NeoForge 21.1.213+

## Getting Started

### Clone the repository

```bash
git clone https://github.com/jcobfx/pz-mod.git
cd pz-mod
```

### Build the mod

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`.

### Run Minecraft with the mod

```bash
./gradlew runClient
```

### Run a dedicated server

```bash
./gradlew runServer
```

### Generate resources (data generators)

```bash
./gradlew runData
```

### Run game tests

```bash
./gradlew runGameTestServer
```

## Project Structure

```
src/main/java/pl/pzmod/
├── PZMod.java              # Main mod entry point
├── PZModClient.java         # Client-side initialization
├── registries/
│   ├── PZItems.java         # Item registration & capability binding
│   └── PZDataComponents.java
├── items/
│   ├── PZItem.java          # Base item class (implements holder interfaces)
│   ├── BatteryItem.java
│   ├── BackpackItem.java
│   └── BigBucketItem.java
├── capabilities/
│   ├── CapabilityResolver.java  # Generic base resolver
│   ├── energy/
│   ├── fluid/
│   └── item/
└── data/containers/
    ├── AttachedEnergy.java
    ├── AttachedItems.java
    └── AttachedFluids.java
```

## Architecture

Items declare their capability parameters (capacity, transfer rate, slot count) by overriding methods from holder interfaces (`IEnergyHolder`, `IFluidHolder`, `IItemHolder`). Capability resolvers are created per-query and handle reading/writing data through DataComponents, keeping item classes focused on configuration.

See the [`docs/`](docs/) directory for UML diagrams and detailed module documentation.

## License

[MIT](LICENSE)
