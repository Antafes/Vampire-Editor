# Vampire Editor

This is a character generator for Vampires: Dark Ages.
The editor will be in English and German, but as of now some parts may only have English texts available. After the German version of the 20th anniversary edition is published, I certainly will add all German translations.

## Known Issues

There is a difference between the cost for clan apostate in the German and English version of the anniversary edition.

## Development

### Setup
This tool uses my [MyXML library](https://github.com/Antafes/MyXML) for parsing and writing XML files.
Therefore access to the GitHub Maven registry is needed.
Please follow the steps described in [GitHub Docs](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) for how to set up everything needed to access GitHubs Maven registry.
An example settings file is provided under ``.m2/settings.xml.dist``.
There you just need to replace the ``[[YourUsernameHere]]`` and ``[[YourPasswordHere]]`` blocks in with your GitHub username and secret.
The descriptions are made with linux in mind, for Windows users just replace the `~` with the following path: `C:\Users\[your username]\`

#### Toolchain requirements
- Java: **JDK 25**
- Maven: **3.9+**

You can verify your local setup with:

```bash
java -version
mvn -version
```

The build enforces these minimum versions via Maven Enforcer.

If your IDE supports modules and has something available, modules for the following would be helpful:
- Maven (obviously ^^)
- Lombok (if available Delombok or something of that kind)
- TestNG

To make adjustments on the build pipelines in Concourse CI, you would also need its CLI tool "fly".
This can simply be downloaded from my [CI Server](https://ci.wafriv.de/).
Login to the CI Server is only available for contributors!
The full documentation on Concourse can be found on their [website](https://concourse-ci.org/docs.html).

### Working on the project

Development needs to be done on its own branch derived from the dev branch.
To get your changes merged into dev, a pull request should be created.
After someone reviewed the changes and the CI also told everything is fine, it can be merged.
The merge commit should contain one of the keywords described [here](https://docs.github.com/en/issues/tracking-your-work-with-issues/linking-a-pull-request-to-an-issue).
This ensures that the issue is closed after everything is merged into the master branch.
At that point the work is done and the work branch can be deleted, either during the merge of the PR or manually and the issue is put on "Status check passed".

After everything is finished for one version, a PR is created against the master branch.
To ensure nothing stupid can be added, this PR needs to be approved by a code owner.
At that point the version should also be raised (see [Deployment](#deployment)).

## Dependency Management

### Dependabot

Dependabot is configured (`.github/dependabot.yml`) to scan Maven dependencies weekly (Monday, 06:00 UTC).
It opens pull requests for outdated dependencies and plugins, labels them with `type:dependencies` and `dependencies`, and assigns them to `Antafes`.
The open-pull-request limit is set to **5**.

For Dependabot to access dependencies hosted on GitHub Packages, a repository secret named `DEPENDABOT_GITHUB_ACCESS_TOKEN` must be configured.
The token stored in that secret should be a GitHub personal access token with at least the following scopes:

- `read:packages` – required to authenticate to GitHub Packages and read Maven artifacts
- `repo` – required so Dependabot can work with private repository contents and manifests

If this secret is missing or does not have the required scopes, Dependabot update runs may fail when resolving packages from GitHub registries.

### Concourse Dependency Scan

A scheduled Concourse job (`vampire-editor-dependency-scan`) runs every **Monday at 06:00 UTC**.
It executes the following steps:

1. **`ci/check-dependency-updates.sh`** – Runs `mvn versions:display-dependency-updates` and `mvn versions:display-plugin-updates`, then writes a Markdown report (`dependency-update-report.md`) and a JSON summary (`dependency-update-summary.json`) to the `target` artifact directory.
2. **`ci/create-dependency-issue.sh`** – Reads the produced artifacts and, if updates are found, creates or updates a GitHub tracking issue labelled `type:dependencies`.

The job uses the same Maven image (`maven:3-eclipse-temurin-25`) as the build pipeline to guarantee consistency.

### Issue Creation Logic

- If an open issue with title prefix `Dependency updates:` already exists, a timestamped comment is appended.
- If no such issue is open, a new issue is created with the full update report.
- If no updates are found at all, no issue or comment is created.

### Required Secrets / Variables

The following Concourse variables must be set when applying the pipeline (keep all secrets in Concourse vars — never commit credentials):

| Variable | Purpose |
|---|---|
| `acccess_token` | GitHub personal access token for PR/release resources |
| `github_access_token` | GitHub personal access token with **Issues** write permission (used by dependency scan) |
| `maven_access_token` | GitHub token for the Apache Maven registry (private packages) |
| `private_key` | SSH private key for git repo access |

Add new variables to `ci/variables.yml` (based on `ci/variables.yml.dist`) before applying the pipeline.

### Applying the Pipeline

```bash
fly -t ciwafriv set-pipeline --team vampire_editor -c ci/pipeline.yml -l ci/variables.yml -p vampire_editor
fly -t ciwafriv unpause-pipeline --team vampire_editor -p vampire_editor
```

To manually trigger a dependency scan:

```bash
fly -t ciwafriv trigger-job --team vampire_editor -j vampire_editor/vampire-editor-dependency-scan -w
```

### Troubleshooting

- **API rate limit / auth failures**: Verify that `github_access_token` has the `repo` (Issues write) scope and has not expired.
- **No updates reported but outdated packages exist**: Run `mvn versions:display-dependency-updates` locally to validate. SNAPSHOT or non-release versions may be filtered.
- **False positives (major version bumps)**: Review the reported versions before acting; the scan includes all available versions without distinction between major/minor/patch.
- **`curl` not installed**: The `create-dependency-issue` task installs `curl` at runtime via `apt-get`. If the Concourse worker has no internet access, pre-bake `curl` into a custom image.

## Deployment

If a new version should be released, adjust the version in the VERSION file first and commit it accordingly into the master branch.
The build pipeline will automatically check if the version has been changed and thus will create a new release.

### Windows Executable

To build the Windows executable (`.exe`) locally, a JRE 25 must be placed under `resources/jre` in the project root.
The executable is only built on Windows (the `windows-exe` Maven profile activates automatically).

The JRE can be downloaded from [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=25) — choose the **JRE**, **Windows**, **x64**, **zip** distribution, extract it, and place the contents into `resources/jre` so that `resources/jre/bin/java.exe` exists.

## Copyright and Trademark

<img src="src/main/resources/images/darkPackLogo.png" width="100" />

Portions of the materials are the copyrights and trademarks of Paradox Interactive AB, and are used with permission. 
All rights reserved. 
For more information please visit https://worldofdarkness.com.
