# Skybreaker: Orbital Arsenal

A high-tech orbital strike and weaponry mod for Minecraft Fabric (supporting Minecraft 26.3 / 1.21.4).

## Features
- **Orbital Railgun**: Target up to 128 blocks away with an energy projectile and shockwave.
- **Satellite Laser**: Paint a location and call down a focused energy beam.
- **Meteor Barrage**: Designate an impact zone and summon a rain of fiery meteors.

## Building the .jar Automatically via GitHub Actions
Since local compilation requires internet access and Java development tools, this repository comes with a preconfigured GitHub Actions workflow:

1. Create a new public or private repository on GitHub.
2. Upload all the files from this project into your repository.
3. Go to the **Actions** tab in your GitHub repository.
4. Click on **Build Minecraft Mod** and select **Run workflow**.
5. Once the build completes successfully (usually in under 2 minutes), download the compiled `.jar` artifact from the workflow run page.
6. Place `skybreaker-26.3.jar` directly into your SKLauncher Fabric instance's `mods` folder and launch Minecraft!
