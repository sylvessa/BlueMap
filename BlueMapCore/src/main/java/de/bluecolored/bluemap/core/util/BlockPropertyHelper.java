package de.bluecolored.bluemap.core.util;

import de.bluecolored.bluemap.core.logger.Logger;
import de.bluecolored.bluemap.core.util.BlockID;
import de.bluecolored.bluemap.core.world.BlockState;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.StringTag;

import java.util.Map;

public final class BlockPropertyHelper {

	public static BlockState applySpecialProperties(int blockId, int meta, int x, int y, int z,
													int[][][][] neighbors, BlockState baseState,
													CompoundTag tileEntity) {

		Map<String, String> props = baseState.getProperties();

		int[][] below = new int[3][3];
		int[][] same  = new int[3][3];
		int[][] above = new int[3][3];

		int[][] belowMeta = new int[3][3];
		int[][] sameMeta  = new int[3][3];
		int[][] aboveMeta = new int[3][3];

		for (int dz = 0; dz < 3; dz++) {
			for (int dx = 0; dx < 3; dx++) {
				below[dz][dx] = neighbors[0][dz][dx][0];
				same[dz][dx]  = neighbors[1][dz][dx][0];
				above[dz][dx] = neighbors[2][dz][dx][0];

				belowMeta[dz][dx] = neighbors[0][dz][dx][1];
				sameMeta[dz][dx]  = neighbors[1][dz][dx][1];
				aboveMeta[dz][dx] = neighbors[2][dz][dx][1];
			}
		}

		switch (blockId) {
			case 2: // grass block snowy
				int aboveBlock = above[1][1];
				props.put("snowy", (aboveBlock == 78 || aboveBlock == 80) ? "true" : "false");
				break;

			case 51: // fire
				props.put("up",   above[1][1] > 0 ? "true" : "false");
				props.put("west", same[1][0] > 0 ? "true" : "false");
				props.put("east", same[1][2] > 0 ? "true" : "false");
				props.put("north",same[0][1] > 0 ? "true" : "false");
				props.put("south",same[2][1] > 0 ? "true" : "false");
				break;

			case 54: // chests
				int west  = same[1][0];
				int east  = same[1][2];
				int north = same[0][1];
				int south = same[2][1];

				if (west == 54) {
					props.put("facing", "north"); props.put("type", "right");
				} else if (east == 54) {
					props.put("facing", "north"); props.put("type", "left");
				} else if (north == 54) {
					props.put("facing", "west"); props.put("type", "left");
				} else if (south == 54) {
					props.put("facing", "west"); props.put("type", "right");
				} else {
					props.put("type", "single");
					if (BlockID.isOpaque(north)) props.put("facing", "south");
					else if (BlockID.isOpaque(west)) props.put("facing", "east");
					else if (BlockID.isOpaque(south)) props.put("facing", "north");
					else if (BlockID.isOpaque(east)) props.put("facing", "west");
					else props.put("facing", "south");
				}
				break;

			case 55: // redstone wire
				// TODO: finish this
				// some stuff like corner wires or ones that step up seem to not work properly
				props.put("power", String.valueOf(meta));
				props.put("north", getRedstoneSide(same[0][1], above[0][1]));
				props.put("east",  getRedstoneSide(same[1][2], above[1][2]));
				props.put("south", getRedstoneSide(same[2][1], above[2][1]));
				props.put("west",  getRedstoneSide(same[1][0], above[1][0]));
				break;

			case 63: // signs TODO
//				for (int i = 0; i < tileentities.size(); i++) {
//
//            		Tag<?> tag = tileentities.get(i);
//
//            		if (!(tag instanceof CompoundTag))
//            			continue;
//
//            		CompoundTag tileentity = (CompoundTag) tag;
//
//            		int tx = tileentity.getInt("x");
//            		int ty = tileentity.getInt("y");
//            		int tz = tileentity.getInt("z");
//
//            		if (tx != ox || ty != y || tz != oz)
//            			continue;
//
//            		if (!"Sign".equals(tileentity.getString("id")))
//        				break;
//
//            		String line1 = ((StringTag)tileentity.get("Text1")).getValue();
//            		String line2 = ((StringTag)tileentity.get("Text2")).getValue();
//            		String line3 = ((StringTag)tileentity.get("Text3")).getValue();
//            		String line4 = ((StringTag)tileentity.get("Text4")).getValue();
//            	}
				break;

			case 64: case 71: // doors
//				props.put("hinge", "left");
//				props.put("powered", "false");
//				props.put("half", (meta < 8) ? "lower" : "upper");
//				meta %= 8;
//				props.put("open", (meta < 4) ? "false" : "true");
//				meta %= 4;
//				switch (meta) {
//					case 0: props.put("facing", "east"); break;
//					case 1: props.put("facing", "south"); break;
//					case 2: props.put("facing", "west"); break;
//					default: props.put("facing", "north"); break;
//				}
				boolean isTop = (meta & 0x8) != 0;

				if (isTop) {
					props.put("half", "upper");

					props.put("hinge", (meta & 0x1) != 0 ? "left" : "right");
					props.put("powered", "false");

					if (below[1][1] == blockId) {
						int bottomMeta = belowMeta[1][1];

						props.put("open", (bottomMeta & 0x4) != 0 ? "true" : "false");

						switch (bottomMeta & 0x3) {
							case 0: props.put("facing", "west"); break;
							case 1: props.put("facing", "north"); break;
							case 2: props.put("facing", "east"); break;
							case 3: props.put("facing", "south"); break;
						}
					}
				} else {
					props.put("half", "lower");

					props.put("open", (meta & 0x4) != 0 ? "true" : "false");

					switch (meta & 0x3) {
						case 0: props.put("facing", "west"); break;
						case 1: props.put("facing", "north"); break;
						case 2: props.put("facing", "east"); break;
						case 3: props.put("facing", "south"); break;
					}

					if (above[1][1] == blockId) {
						int topMeta = aboveMeta[1][1];
						props.put("hinge", (topMeta & 0x1) != 0 ? "left" : "right");
						props.put("powered", "false");
					}
				}
				break;

			case 85: // fences
				props.put("west",  same[1][0] == 85 ? "true" : "false");
				props.put("east",  same[1][2] == 85 ? "true" : "false");
				props.put("north", same[0][1] == 85 ? "true" : "false");
				props.put("south", same[2][1] == 85 ? "true" : "false");
				break;

			case 90: // nether portal
				props.put("axis", (same[0][1] == 90 || same[2][1] == 90) ? "x" : "z");
				break;

			case 101: case 102: // glass panes & iron bars
				if (same[1][0] > 0) props.put("west", "true");
				if (same[1][2] > 0) props.put("east", "true");
				if (same[0][1] > 0) props.put("north", "true");
				if (same[2][1] > 0) props.put("south", "true");
				break;

			case 127: // cocoa
				int age = (meta & 0xC) >> 2;
				if (age > 2) age = 2; // hax
				props.put("age", String.valueOf(age));

				int direction = meta & 0x3;
				switch (direction) {
					case 2: props.put("facing", "north"); break;
					case 3: props.put("facing", "east"); break;
					case 0: props.put("facing", "south"); break;
					case 1: props.put("facing", "west"); break;
				}
				break;
			case 130: // ender chest
				switch (meta % 4) {
					case 0: props.put("facing", "north"); break;
					case 1: props.put("facing", "south"); break;
					case 2: props.put("facing", "west"); break;
					default: props.put("facing", "east"); break;
				}
				break;

			case 131: // tripwire
				props.put("attached", "false");
				props.put("disarmed", "false");
				props.put("powered",  (meta & 0x4) != 0 ? "true" : "false");
				props.put("north", (same[0][1] == 131 || same[0][1] == 132) ? "true" : "false");
				props.put("east",  (same[1][2] == 131 || same[1][2] == 132) ? "true" : "false");
				props.put("south", (same[2][1] == 131 || same[2][1] == 132) ? "true" : "false");
				props.put("west",  (same[1][0] == 131 || same[1][0] == 132) ? "true" : "false");
				break;

//			case 132: // tripwire hook
//				props.put("attached", ((meta & 0x4) != 0) ? "true" : "false");
//				props.put("powered",  ((meta & 0x8) != 0) ? "true" : "false");
//
//				break;
//			case 140: // flower pot
//				break;
			case 144: // skull
//				baseState.getProperties().forEach((key, value) -> {
//					Logger.global.logInfo("Skull: " + key + ": " + value);
//				});
				baseState = BlockState.of("minecraft:skeleton_skull", baseState.getProperties());
//				if (meta == 1) {
//					props.put("rotation", "0");
//					if (tileEntity != null && tileEntity.containsKey("Rot")) {
//						int rot = tileEntity.getByte("Rot") & 15;
//						props.put("rotation", String.valueOf(rot));
//					}
//				} else {
//					switch (meta) {
//						case 2: props.put("facing", "north"); break;
//						case 3: props.put("facing", "south"); break;
//						case 4: props.put("facing", "east"); break;
//						case 5: props.put("facing", "west"); break;
//					}
//				}
//
//				if (tileEntity != null && tileEntity.containsKey("SkullType")) {
//					int type = tileEntity.getByte("SkullType");
//					switch (type) {
//						case 0:
//							Logger.global.logInfo("replaced with skeleton skull");
//							break;
//					}
//				}
				break;
			case 145: // anvil
				props.put("facing", (meta & 0x1) == 0 ? "north" : "east");

				if ((meta & 0x4) != 0) props.put("damage", "slightly");
				else if ((meta & 0x8) != 0) props.put("damage", "very");
				else props.put("damage", "undamaged");
				break;
		}

		return BlockState.of(baseState.getFormatted(), props);
	}

	private static boolean canRedstoneConnect(int neighborId) {
		return neighborId == 55 // redstone wire
				|| neighborId == 64 // door
				|| neighborId == 69 // lever
				|| neighborId == 70 || neighborId == 143 // buttons
				|| neighborId == 75 || neighborId == 76 // redstone torch
				|| neighborId == 93 || neighborId == 94; // repeater
	}

	private static String getRedstoneSide(int horizontalNeighbor, int aboveNeighbor) {
		if (canRedstoneConnect(horizontalNeighbor)) return "side";
		if (canRedstoneConnect(aboveNeighbor)) return "up";
		return "none";
	}
}
