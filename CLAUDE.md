# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PZMod (Projekt Zespołowy Mod) is a Minecraft mod for NeoForge 1.21.1 using Java 21. The mod ID is `pz_mod` with package `pl.pzmod`.

## Build Commands

```bash
# Build the mod
./gradlew build

# Run Minecraft client with the mod
./gradlew runClient

# Run dedicated server
./gradlew runServer

# Run data generators (generates resources to src/generated/resources)
./gradlew runData

# Run game tests
./gradlew runGameTestServer
```

## Architecture

### Entry Points
- `PZMod` - Main mod class, handles common setup and registry initialization
- `PZModClient` - Client-side initialization (annotated with `@Mod(dist = Dist.CLIENT)`)

### Registry System
All registries are in `pl.pzmod.registries`:
- `PZItems` - Item registration using `DeferredRegister.Items`, also registers NeoForge capabilities via `@SubscribeEvent` on `RegisterCapabilitiesEvent`
- `PZDataComponents` - Data components for persistent item data (energy, fluids, items)

### Capability System
The mod implements a layered capability architecture for energy, fluid, and item handling:

1. **Holder Interfaces** (`capabilities/<type>/I<Type>Holder.java`) - Define configuration methods items must implement (e.g., `getEnergyCapacity()`)

2. **Base Resolver** (`CapabilityResolver<HOLDER, CONTEXT, ATTACHED>`) - Generic abstract class handling data component read/write and insert/extract permissions

3. **Type-specific Resolvers** (`<Type>CapabilityResolver`) - Extend base resolver, implement NeoForge capability interfaces (e.g., `IEnergyStorage`)

4. **ItemStack Resolvers** (`ItemStack<Type>CapabilityResolver`) - Concrete implementations for ItemStack context, query the item for configuration

### Item Base Class
`PZItem` extends `Item` and implements all holder interfaces with default (disabled) values. Custom items extend this and override relevant methods to enable capabilities.

### Data Storage
Persistent item data uses DataComponents in `pl.pzmod.data.containers`:
- `AttachedEnergy` - Stores energy as integer
- `AttachedItems` - Stores inventory contents
- `AttachedFluids` - Stores fluid tank contents

Each has a `Codec` for persistence and `StreamCodec` for network sync.

## Key Patterns

- Items define their capability parameters (capacity, slots, etc.) by overriding holder interface methods
- Capability resolvers are instantiated per-capability query, not stored on items
- Use `DeferredRegister` for all registry objects
- Client-side code must be isolated in classes annotated with `dist = Dist.CLIENT`