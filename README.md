## Fork to Suppooort later verisions (CraftBukkit 1.1 - idk)

This also makes it show newer blocks like stone bricks etc.

I will make a branch or something soon to support some earlier CraftBukkit builds like beta 1.8 and release 1.0.

Build with `gradlew :bukkit:release`

If using 1.2 or above, add `worldtype: "mca"` to your world configs.

Original project can be found [here](https://github.com/BlueMap-Minecraft/BlueMap)

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
