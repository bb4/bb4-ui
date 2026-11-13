# Issue #23 Finish println cleanup Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Remove the two remaining production `println` call sites so issue #23 acceptance criteria are fully met.

**Architecture:** Deletion-only cleanup. No new logging. Prior work already routes library warnings/debug through `UiLog`; this plan only removes a demo tip and deprecated applet help printing.

**Tech Stack:** Scala library (`bb4-ui`), Gradle (`./gradlew build`)

## Global Constraints

- Do not change `Log.scala` or `UiLog` behavior.
- Do not change warning/error meaning for existing `UiLog` call sites.
- Do not touch commented-out `println`s or `scala-test` demos.
- Spec: `docs/superpowers/specs/2026-11-13-issue-23-println-cleanup-design.md`

## File map

| File | Responsibility |
|------|----------------|
| `scala-source/com/barrybecker4/ui/application/ApplicationFrame.scala` | Remove tip `println` from `main`; keep smoke frame |
| `scala-source/com/barrybecker4/ui/application/ApplicationApplet.scala` | Remove entire `-help` block |

---

### Task 1: Remove remaining production printlns

**Files:**
- Modify: `scala-source/com/barrybecker4/ui/application/ApplicationFrame.scala`
- Modify: `scala-source/com/barrybecker4/ui/application/ApplicationApplet.scala`

**Interfaces:**
- Consumes: none
- Produces: no new APIs; `ApplicationFrame.main` still constructs a frame; `ApplicationApplet` still parses `-locale`

- [ ] **Step 1: Edit `ApplicationFrame.main`**

Replace the `main` body so it only constructs the frame:

```scala
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    new ApplicationFrame("Test Frame")
  }
```

- [ ] **Step 2: Edit `ApplicationApplet` constructor**

In the `if (args.length > 0)` block, delete the entire help branch. Resulting block should be:

```scala
  if (args.length > 0) {
    val options = new CommandLineOptions(args)
    if (options.contains("locale")) { // then a locale has been specified
      localeName = options.getValueForOption("locale", "ENGLISH")
    }
  }
```

- [ ] **Step 3: Verify inventory**

Run:

```bash
rg '\b(println|print)\(|System\.(out|err)\.(print|println)\(' scala-source --glob '*.scala'
```

Expected active hits only in:
- `scala-source/com/barrybecker4/ui/util/Log.scala` (logging infrastructure)
- `scala-source/com/barrybecker4/ui/util/UiLog.scala` (stderr fallback)
- Commented lines in `ImageListRenderer.scala`, `AbstractFunctionRenderer.scala`, `GenericFileFilter.scala`

No hits in `ApplicationFrame.scala` or `ApplicationApplet.scala`.

- [ ] **Step 4: Build**

Run: `./gradlew build`  
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add \
  scala-source/com/barrybecker4/ui/application/ApplicationFrame.scala \
  scala-source/com/barrybecker4/ui/application/ApplicationApplet.scala
git commit -m "$(cat <<'EOF'
Remove remaining production println call sites (#23).

Drop the ApplicationFrame tip message and deprecated applet -help usage print.
EOF
)"
```

---

## Spec coverage check

| Spec requirement | Task |
|------------------|------|
| Remove `ApplicationFrame` tip println; keep main | Task 1 Step 1 |
| Remove entire applet help block | Task 1 Step 2 |
| Leave Log/UiLog/demos/comments alone | Global constraints + inventory check |
| `./gradlew build` | Task 1 Step 4 |
| Note behavior in PR | Handled when opening PR (not a code task) |
