package de.bluecolored.bluemap.core.util;

import de.bluecolored.bluemap.core.util.BlockID;
import de.bluecolored.bluemap.core.world.BlockState;

import java.util.Map;

public final class BlockPropertyHelper {

	public static BlockState applySpecialProperties(int blockId, int meta, int x, int y, int z,
													int[][] neighborIds, BlockState baseState) {

		Map<String, String> props = baseState.getProperties();

		switch (blockId) {
			case 2: // grass block snowy
				int above = neighborIds[1][0];
				props.put("snowy", (above == 78 || above == 80) ? "true" : "false");
				break;

			case 85: // fences
				props.put("west",  neighborIds[0][0] == 85 ? "true" : "false");
				props.put("east",  neighborIds[0][1] == 85 ? "true" : "false");
				props.put("north", neighborIds[0][2] == 85 ? "true" : "false");
				props.put("south", neighborIds[0][3] == 85 ? "true" : "false");
				break;

			case 90: // nether portal
				if (neighborIds[0][0] == 90 || neighborIds[0][1] == 90)
					props.put("axis", "x");
				else
					props.put("axis", "z");
				break;

			case 101: case 102: // glass panes & iron bars
				if (neighborIds[0][0] > 0) props.put("west", "true");
				if (neighborIds[0][1] > 0) props.put("east", "true");
				if (neighborIds[0][2] > 0) props.put("north", "true");
				if (neighborIds[0][3] > 0) props.put("south", "true");
				break;

			case 54: // chests
				int west  = neighborIds[0][0];
				int east  = neighborIds[0][1];
				int north = neighborIds[0][2];
				int south = neighborIds[0][3];

				if (west == 54) {
					int westNorth = neighborIds[0][3]; // north of west
					int northThis = neighborIds[0][3];
					if (BlockID.isOpaque(westNorth) || BlockID.isOpaque(northThis)) {
						props.put("facing", "north");
						props.put("type", "right");
					} else {
						props.put("facing", "south");
						props.put("type", "left");
					}
				} else if (east == 54) {
					int eastNorth = neighborIds[0][3]; // north of east
					int northThis = neighborIds[0][3];
					if (BlockID.isOpaque(eastNorth) || BlockID.isOpaque(northThis)) {
						props.put("facing", "north");
						props.put("type", "left");
					} else {
						props.put("facing", "south");
						props.put("type", "right");
					}
				} else if (north == 54) {
					int northEast = neighborIds[0][1]; // east of north
					int eastThis  = neighborIds[0][1];
					if (BlockID.isOpaque(northEast) || BlockID.isOpaque(eastThis)) {
						props.put("facing", "west");
						props.put("type", "left");
					} else {
						props.put("facing", "east");
						props.put("type", "right");
					}
				} else if (south == 54) {
					int southEast = neighborIds[0][1]; // east of south
					int eastThis  = neighborIds[0][1];
					if (BlockID.isOpaque(southEast) || BlockID.isOpaque(eastThis)) {
						props.put("facing", "west");
						props.put("type", "right");
					} else {
						props.put("facing", "east");
						props.put("type", "left");
					}
				} else {
					props.put("type", "single");
					if (BlockID.isOpaque(north)) props.put("facing", "south");
					else if (BlockID.isOpaque(west)) props.put("facing", "east");
					else if (BlockID.isOpaque(south)) props.put("facing", "north");
					else if (BlockID.isOpaque(east)) props.put("facing", "west");
					else props.put("facing", "south");
				}
				break;

			case 64: case 71: // doors
                props.put("hinge", "left");
                props.put("powered", "false");
                props.put("half", (meta < 8) ? "lower" : "upper");
                meta %= 8;
                props.put("open", (meta < 4) ? "false" : "true");
                meta %= 4;

                switch (meta) {
                    case 0: props.put("facing", "east"); break;
                    case 1: props.put("facing", "south"); break;
                    case 2: props.put("facing", "west"); break;
                    default: props.put("facing", "north"); break;
                }
                break;

			case 51: // fire
				props.put("up", neighborIds[1][0] > 0 ? "true" : "false");
				props.put("west",  neighborIds[0][0] > 0 ? "true" : "false");
				props.put("east",  neighborIds[0][1] > 0 ? "true" : "false");
				props.put("north", neighborIds[0][2] > 0 ? "true" : "false");
				props.put("south", neighborIds[0][3] > 0 ? "true" : "false");
				break;

			case 55: // redstone wire TODO
				break;

			case 63: // signs TODO
				break;
		}

		return BlockState.of(baseState.getFormatted(), props);
	}
}
