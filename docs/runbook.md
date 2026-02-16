# Tensura Goblin Fix – Runbook

## Deploy

- **Staging / pre-release:** Push to `dev`. CI builds and creates/updates a PR to `main`.
- **Production:** Merge the PR to `main` (or push directly to `main`). CI builds, then the release job:
  - Bumps patch version (or uses `build.gradle` version for first release)
  - Creates a GitHub release with the built JAR
  - Commits version bump on `build.gradle` and `mods.toml` back to `main`

No manual deploy step; releases are created from the JAR produced by the `build` job on that push.

## Rollback

- **Bad release:** Create a new release from a previous tag (e.g. re-run workflow from an older commit or upload an older JAR to a new release). Or delete the bad release and re-release a fixed version.
- **Revert code:** Revert the merge on `main` and push; the next run will release the reverted build. Optionally create a new release tag from the reverted commit.

## Build locally

```bash
./gradlew build
# JAR: build/libs/tensura-goblin-fix-<version>.jar
```

## CI workflow summary

| Branch  | Trigger   | Jobs                    |
|---------|-----------|-------------------------|
| `dev`   | push      | build, create-pr        |
| `main`  | push      | build, release          |
| `main`  | pull_request | build               |

- **Build:** Java 17, Gradle; produces JAR artifact.
- **create-pr:** Only on `dev`; creates or updates PR `dev` → `main`.
- **release:** Only on `main`; creates release, uploads JAR, bumps version and commits.

## Troubleshooting

- **Build fails:** Check Java 17 and Gradle version; run `./gradlew build` locally.
- **Release not created:** Ensure workflow has `contents: write` and runs on push to `main`.
- **Version mismatch:** Release tag is the *next* version; the committed bump is applied after the release.
