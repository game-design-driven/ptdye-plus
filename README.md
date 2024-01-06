### Ptdye Plus

![Version](https://img.shields.io/badge/Version-1.2.4-purple)
![Minecraft](https://img.shields.io/badge/Minecraft-1.19.2-blue)
![Forge](https://img.shields.io/badge/Forge-43.3.0-darkblue)
<br>
[![Modrinth](https://img.shields.io/badge/Find%20us%20on-Modrinth-green)](https://modrinth.com/mod/ptdye-plus)
[![CurseForge](https://img.shields.io/badge/Find%20us%20on-CurseForge-orange)](https://legacy.curseforge.com/minecraft/mc-mods/ptdye-plus)
[![CPTD](https://img.shields.io/badge/Create-Prepare%20to%20Dye-yellow)](https://modrinth.com/modpack/create-prepare-to-dye)


[![Discord](https://img.shields.io/badge/Discord-blue)](https://discord.gg/v8cZ83kTPY)

Introducing a Java mod designed exclusively for the [Create: Prepare to Dye](https://modrinth.com/modpack/create-prepare-to-dye) (CPTD) modpack!

This mod includes code for features and bug fixes not achievable with KubeJS, the JavaScript framework used by the CPTD team.

Keep in mind that this mod is primarily tested for use within the CPTD modpack, and using it elsewhere may lead to unexpected behavior.

Key Features:

- Command to access the stonecutter GUI: /openStonecutter
  - By using the "/openStonecutter" command, players can directly access the stonecutter's GUI, making it more convenient to craft devices.
- Key bind to open ponder based on what block the player is looking at, set to B by default
- Adds line to Jade tooltip on blocks that have a ponder indicating which button to press to open ponder

While the mod currently focuses on this stonecutter enhancement, expect future updates for more features to enhance your gameplay. Stay tuned!

<br>

#### For Devs:

- To download gradle dependencies click the `Reload All Gradle Projects` button (refresh/recycle icon) in the top left of the gradle task window (IntelliJ)
- Run the game  by debugging the `runClient` gradle task and run `Build Project` or `CTRL+F9` to hotswap your code changes.