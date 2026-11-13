# Issue #22 — Review uninitialized field usages for initialization safety

Date: 2026-11-13  
Issue: https://github.com/bb4/bb4-ui/issues/22

## Problem

Production code uses `scala.compiletime.uninitialized` in many fields. Some uses are assigned before any access (often fine); others leave mutable references readable before init. Do **not** blanket-rewrite everything to `Option`.

## Scope

- **In scope:** `scala-source` only (13 files with `uninitialized` on current tree).
- **Out of scope:** `scala-test`; already-migrated sites such as `FrameRecorder` / `ImageListPanel` (use `null` / empty collections).
- **Non-goals:** Broad API changes from reflexive `Option` wrapping; unrelated refactors.

## Policy (priority order)

1. **Assign-once → `val` or init in declaration** — prefer removing the `uninitialized` pattern when the value is set once (constructor / `commonInit` / `showContent`).
2. **Mutable with a natural empty → empty collection / sensible default**.
3. **Absence is real and already checked → `null` (or `0` for widths)** matching existing null/`!= 0` checks; use `Option` only where absence is a genuine state and it clarifies without API churn.
4. **Document** any remaining intentional late-bind if restructuring would fight the framework (e.g. subclass table init).

Preserve throw-on-misuse behavior where that is the API contract (NPE / `assert` on use-before-init).

## Per-file plan

| File | Fields | Plan |
|------|--------|------|
| `TableButton` | `listeners`, `disabledValues` | Init as empty `Set`/`Seq` at declaration; drop redundant `commonInit` assignments for those (keep listener wiring). |
| `SliderGroup` | `labels`, `sliders` | Build arrays in a private helper and assign to `val`s (or equivalent without `uninitialized`). |
| `GradientButton` | gradient colors | Init via `UIManager` defaults at declaration / single init path; no `uninitialized`. |
| `PasswordDialog` | `passwordField`, `okButton` | Construct as `val`s at field init (or locals wired once) so fields are assigned once. |
| `OutputWindow` | `textArea` | Construct `ScrollingTextArea` at field init as `val`. |
| `ContinuousColorLegend` | edit bar / labels | Create panels as `val`s on the `initUI` path without `uninitialized`. |
| `ComplexNumberInput` | `range` | **Bug:** `setAllowedRange(allowedRange)` runs before `range = uninitialized`, wiping the assigned value. Fix: `private var range = allowedRange` (or call setter only after the field exists). |
| `TableColumnMeta` | widths, renderer, editor | Widths → `0`; renderer/editor → `null` (matches existing checks). |
| `AbstractCellEditor` | `value`, `changeEvent` | `null` defaults (lazy `changeEvent` already null-checked; `value` may be read before set — same as Swing). |
| `TableBase` | `table`, `columnMeta` | Keep late-bind for subclass/`initializeTable`; use `null` + existing use-after-init contract (no `Option` on public getters). |
| `ApplicationApplet` | `resizablePanel` | `= null` (already null-checked in `setSize`). |
| `MyJApplet` | `rootPane`, `transferHandler` | Prefer assign-once for `rootPane` if clean; else `null`. `transferHandler = null`. |
| `RangeSliderUI` | `upperThumbRect` | Init `Rectangle()` at declaration so layout/paint never sees unset (still refreshed in `installUI`). |

## Behavior notes

- Public method signatures unchanged.
- Misuse that already fails fast stays fail-fast.
- After the `ComplexNumberInput` fix, `getValue` / range clamping must see the constructor’s `allowedRange`.
- `AbstractCellEditor` / `TableColumnMeta` keep null/`0` semantics callers already rely on.

## Verification

- `./gradlew build`
- Grep: no `uninitialized` under `scala-source`
- Spot-check: `ComplexNumberInput` range after construction; dialogs/tables smoke if practical
