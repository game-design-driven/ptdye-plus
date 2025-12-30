# PTDye Plus

![Version](https://img.shields.io/github/v/release/jasperalani/ptdye-plus)
![Minecraft](https://img.shields.io/modrinth/game-versions/ikDjkgLu?label=Avilable+for)
![Forge](https://img.shields.io/badge/Forge-47.4.10-darkblue)
<br>
[![Modrinth](https://img.shields.io/badge/Find%20us%20on-Modrinth-green)](https://modrinth.com/mod/ptdye-plus)
[![CurseForge](https://img.shields.io/badge/Find%20us%20on-CurseForge-orange)](https://legacy.curseforge.com/minecraft/mc-mods/ptdye-plus)
[![CPTD](https://img.shields.io/badge/Create-Prepare%20to%20Dye-yellow)](https://modrinth.com/modpack/create-prepare-to-dye)

[![Discord](https://img.shields.io/badge/Discord-blue)](https://discord.gg/v8cZ83kTPY)

This is a specialized Java mod built specifically to power the **Create: Prepare to Dye** (PTD) modpack. While KubeJS
handles a lot of our heavy lifting, some features and bug fixes require deeper "under-the-hood" changes that only a
dedicated Java mod can provide.

**Note**: This mod is built and tested strictly for the PTD environment. If you use it in other packs, things might get
weird.

Key Features:

- Ponder Blocks that the player is looking at.

### How to setup dev environment (IntelliJ)

Any key shortcuts below assume you are using the default `Windows` keymap for IntelliJ.

#### 1. Environment Initialization

1. Update IntelliJ to latest version
2. Clone the git repo, and open the root directory in IntelliJ
3. Ensure you have JDK 17 installed and selected in `File > Project Structure`. If you don't have it, IntelliJ can
   download it for you directly from that menu
4. Wait for project to finish the initial loading, you can check the progress in the bottom right.
    - If the project doesn't load automatically: Open the Gradle window (Elephant icon on the right) and click the
      `Sync All Gradle Projects` icon (Refresh button at the top)

#### 2. Generating Run Configurations

Minecraft mods require a specific launch setups. This task must be repeated if `build.gradle` changes, such as adding
new mod dependencies.

1. Open the Gradle window (Elephant icon on the right), expand the `ptdye-plus` project
2. Navigate to `Tasks > forgegradle runs`
3. Double click `genIntelliJRuns`
4. Once the terminal shows "BUILD SUCCESSFUL" click the `Sync All Gradle Projects` icon (Refresh button at the top)

#### 3. Running & Developing

Once the sync is finished, look at the top of the window, in the Title Bar. Click the run configuration dropdown and
select `runClient`.

If you want to test if the game launches, hit the Play icon (or `Shift + F10`).

For actual coding, it's much better to use the Bug icon (or `Shift + F9`) to start in Debug mode. This lets you hotswap
load your code.
While the game is running in Debug, you can change your code and press `Ctrl + F9`. This compiles the changes and pushes
it to the running client immediately. When IntelliJ asks if you want to `Reload changed classes`, click **Yes**.

**Note**: Hotswapping is great for fixing logic, or changing values, but if you add brand-new items or blocks, you'll
still need to restart the client to see those changes.

#### 4. DevAuth (Optional: Account Login)

By default, the development environment runs in offline mode, which means you cannot join online-mode servers, or test
features that need an online account. By default, you'd be 'logged in' as 'Dev', which is perfectly fine for most tasks.

To use your actual Minecraft account and skin:

1. Create a file in the project root named `local.properties` (this file is ignored by Git)
2. Add the line: `useDevAuth=true`
3. Sync Gradle and run `genIntelliJruns` again to apply the change
4. On your next launch, check the **IntelliJ Console**. It will provide a Microsoft link to authenticated your account
5. Once authenticated, your dev-client will permanently use your real UUID and skin

You can understand more about this mod by checking out the readme for
it [here](https://github.com/DJtheRedstoner/DevAuth?tab=readme-ov-file#how-it-works), where it goes into detail about
where your tokens are stored, how to revoke them, and other security nuances.
