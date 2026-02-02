## Bluemap b1.7 fork to support later versions

Build with `gradlew build`

Requires atleast Java 11.

If using 1.2 or above, add `worldtype: "mca"` to your world configs. This will be automated in the future

Original project can be found [here](https://github.com/BlueMap-Minecraft/BlueMap)

## Supported & Tested CraftBukkit Versions:

- **b1.7** - Tested CB#945 <sub>requires bukkit-legacy</sub>
- **b1.7.2** - Tested CB#967 <sub>requires bukkit-legacy</sub>
- **b1.7.3** - Tested CB#1060 and CB#1092 <sub>requires bukkit-legacy</sub>
- **b1.8.1** - Tested CB#1464 and CB#1337 <sub>requires bukkit-legacy</sub>
- **1.0** - Tested CB#1712 <sub>requires bukkit-legacy</sub>
- **1.1** - Tested R6 and R5
- **1.2.5** - Tested R5.0 and R5.1-SNAPSHOT
- **1.3.2** - Tested R3.0

You should use [Uberbukkit](https://github.com/Moresteck/uberbukkit) if you want this to work with versions before beta 1.7

If a version isnt listed here, I haven't updated this to support that version.

I will probably only go up to 1.11 or 1.12.

## To get the Beta look:

Make sure you generate the default BlueMap configuration first and accept the Minecraft EULA.

1. Get `Programmer's Art` as a resource pack

- https://resources.download.minecraft.net/6d/6d7a7a95b58a31716699d63b56f9ffff0e89b0a6
- rename it to `01_programmers_art.zip`
- move it to directory `config/resourcepacks/`, relative to your bluemap jar

2. Get `Golden Days` resource pack (I recommend 1.19.4-base, v1.8.2) (OPTIONAL)

- https://github.com/PoeticRainbow/golden-days/releases/tag/1.8.2
- rename it to `02_golden_days.zip`
- move it to directory `config/resourcepacks/`, relative to your bluemap jar

3. Get [Neo-Beta datapack](https://github.com/SkyDeckAGoGo/neo-beta-datapack)

- open the archive & extract files from directory `data/minecraft/worldgen/biome/`
- edit the golden days resourcepack, add the following path: `assets/minecraft/worldgen/biome/`
- put all biome json files into the resourcepack zip

Why?

1. is for texture base
2. overrides modern textures with the old ones
3. makes sure all biomes appear correct
