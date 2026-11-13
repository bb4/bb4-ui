# Issue #23 — Finish production println cleanup

Date: 2026-11-13  
Issue: https://github.com/bb4/bb4-ui/issues/23

## Problem

Ad-hoc production `println`/`print` calls should be replaced with intentional logging or removed. Most representative warning/debug sites were already routed through `UiLog` in commit `98fec53`. Two active production call sites remain outside logging infrastructure.

## Already done (out of scope for new work)

- Warning/debug sites in `ImageListPanel`, `LegendSynchronizer`, `NumberInput`, `ComplexNumberInput`, `ImageUtil`, `GUIUtil`, `HistogramModel`, and `MultipleFunctionRenderer` use `UiLog`.
- `Log.scala` left unchanged (logging facade).
- `UiLog` keeps stderr fallback when `AppContext` is not initialized.
- Demo/`scala-test` drivers and commented-out prints excluded from cleanup tally.
- `DomEcho` removed from this repo (moved to bb4-xml).

## Remaining changes

### 1. `ApplicationFrame.main`

- **Remove** the tip `println("bb4-ui is meant to be used as a UI library...")`.
- **Keep** `new ApplicationFrame("Test Frame")` so the smoke entrypoint still opens a frame.

### 2. `ApplicationApplet` help handling

- **Remove** the entire `if (options.contains("help")) { println("Usage: ...") }` block.
- Leave locale option parsing unchanged.

## Non-goals

- No changes to `Log.scala` or `UiLog` behavior.
- No migration of commented-out `println` lines.
- No changes under `scala-test`.
- No new logging facade or SLF4J/etc. introduction.

## Behavior notes (for PR)

- Warning/error meaning for library diagnostics is unchanged (already on `UiLog`).
- Applet `-help` no longer prints usage text (intentional removal).
- `ApplicationFrame` main no longer prints the library tip; it still shows a test frame.

## Verification

- `./gradlew build`
- Grep inventory: no remaining active production `println`/`print`/`System.*.println` outside `Log.scala` and `UiLog`’s intentional stderr fallback.
