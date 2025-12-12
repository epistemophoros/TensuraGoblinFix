# Tensura Goblin Fix

A Minecraft Forge mod that fixes the `ArrayIndexOutOfBoundsException` crash when rendering Tensura goblins with invalid Clothing values.

## The Problem

The Tensura mod can crash with the following error when a goblin entity has an invalid clothing ID (e.g., -1):

```
java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 6
    at com.github.manasmods.tensura.entity.variant.GoblinVariant$Clothing.byId(GoblinVariant.java)
```

## The Fix

This mod uses Mixin to intercept the `byId` method and clamp invalid clothing IDs to a valid range (0-5), preventing the crash.

## Requirements

- Minecraft **1.19.2**
- Minecraft Forge **43.x+**
- [Tensura Mod](https://www.curseforge.com/minecraft/mc-mods/tensura)

## Installation

1. Download the latest `.jar` from [Releases](https://github.com/epistemophoros/TensuraGoblinFix/releases)
2. Place in your `mods` folder
3. Launch the game

## License

MIT

