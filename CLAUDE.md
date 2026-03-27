# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

This is a pure Java Swing desktop application with no external dependencies. It is built and run directly with `javac` and `java`. Java 17+ is required.

```bash
# Compile
javac -d out -sourcepath src src/Main.java

# Run
java -cp "out;src" Main
```

No test framework is configured.

## Architecture

The application is a 2D solar system simulation rendered in Swing at ~60 FPS via a Swing Timer (16ms interval).

**Entry point:** `Main.java` — initializes the localized UI, creates the `SolarSystem` from `PlanetCatalog`, and opens the main window.

**Core model (`src/core/`):**
- `SolarSystem` — container holding the `Sun` and a list of `Planet` objects; delegates position updates
- `PlanetCatalog` — defines the current planet set (Mercury through Neptune) and their orbital/visual metadata
- `Planet` — immutable planet definition plus current orbital state; exposes live distance, angle, and speed derived from elliptical orbit math
- `SimulationController` — owns the Swing Timer loop; notifies registered tick listeners (observer pattern) on each frame
- `TimeController` — converts real seconds into simulated days and clamps the time speed to `0.5..720` days per second
- `Orbit` — elliptical orbit math, including anomaly solving and sampled path generation for rendering

**UI (`src/ui/`):**
- `MainFrame` — larger split layout that wires tick listeners, default selection, and keyboard shortcuts
- `SolarSystemPanel` — custom-painted viewport with zoom, pan, selection hit testing, and orbital trails
- `SolarSystemRenderer` / `ViewTransform` — rendering helpers for compressed orbit scaling and screen-space drawing
- `InfoPanel` — displays live selected-planet data via `PlanetInfo`
- `TimeControlPanel` — start/pause button, reset view, speed slider, simulated time readout, and language selector

**Utils (`src/utils/`):**
- `LocalizationManager` — wraps Java ResourceBundle for i18n; bundles in `src/assets/messages_{en,zh}.properties`
- `Pair` — generic tuple

## Localization

Two locales: English and Simplified Chinese. Language is switchable at runtime via the UI controls. Resource keys cover UI labels, planet names, dynamic measurements, and units.
