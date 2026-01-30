package de.bluecolored.bluemap.core.mca;

import de.bluecolored.bluemap.core.mcr.BlockID;
import de.bluecolored.bluemap.core.world.BlockState;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.logger.Logger;
import de.bluecolored.bluemap.core.world.*;
import net.querz.nbt.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ChunkAnvil12 extends MCAChunk {

	private boolean isGenerated;
	private long inhabitedTime;
	private Section[] sections;
	private byte[] biomes;

	public ChunkAnvil12(MCAWorld world, CompoundTag chunkTag) {
		super(world, chunkTag);

		CompoundTag level = chunkTag.getCompoundTag("Level");
		this.isGenerated = level.getBoolean("TerrainPopulated");
		this.inhabitedTime = level.getLong("InhabitedTime");

		this.sections = new Section[16];

		ListTag<CompoundTag> sectionsTag = (ListTag<CompoundTag>) level.getListTag("Sections");
		if (sectionsTag != null) {
			for (CompoundTag sec : sectionsTag) {
				Section s = new Section(sec, world);
				if (s.y >= 0 && s.y < 16)
					sections[s.y] = s;
			}
		}

		this.biomes = level.getByteArray("Biomes");
		if (biomes == null || biomes.length < 256)
			biomes = new byte[256];
	}

	@Override
	public boolean isGenerated() {
		return isGenerated;
	}

	@Override
	public long getInhabitedTime() {
		return inhabitedTime;
	}

	@Override
	public BlockState getBlockState(int x, int y, int z) {
		int sy = y >> 4;
		if (sy < 0 || sy >= 16) return BlockState.AIR;

		Section s = sections[sy];
		if (s == null) return BlockState.AIR;

		return s.getBlockState(x, y, z, sections, sy);
	}

	@Override
	public LightData getLightData(int x, int y, int z, LightData target) {
		int sy = y >> 4;
		if (sy < 0) return target.set(0, 0);
		if (sy >= 16) return target.set(15, 0);

		Section s = sections[sy];
		if (s == null) return target.set(15, 0);

		return s.getLightData(x, y, z, target);
	}

	@Override
	public String getBiome(int x, int y, int z) {
		x &= 0xF;
		z &= 0xF;
		return LegacyBiomes.idFor(biomes[z * 16 + x] & 0xFF);
	}

	@Override
	public int getWorldSurfaceY(int x, int z) {
		for (int y = 255; y >= 0; y--) {
			if (getBlockState(x, y, z) != BlockState.AIR)
				return y;
		}
		return 0;
	}

	@Override
	public int getOceanFloorY(int x, int z) {
		return getWorldSurfaceY(x, z);
	}

	private static class Section {

		private int y;
		private byte[] blocks;
		private byte[] data;
		private byte[] add;
		private byte[] blockLight;
		private byte[] skyLight;
        private MCAWorld world;

		public Section(CompoundTag tag, MCAWorld world) {
			this.world = world;
			this.y = tag.getByte("Y");
			this.blocks = tag.getByteArray("Blocks");
			this.data = tag.getByteArray("Data");
			this.add = tag.getByteArray("Add");
			this.blockLight = tag.getByteArray("BlockLight");
			this.skyLight = tag.getByteArray("SkyLight");
		}

		private int getNibble(byte[] arr, int index) {
			if (arr == null || arr.length == 0) return 0;
			int i = index >> 1;
			boolean upper = (index & 1) != 0;
			return upper ? (arr[i] >> 4) & 0xF : arr[i] & 0xF;
		}

		public BlockState getBlockState(int x, int y, int z, Section[] sections, int sectionIndex) {

			x &= 0xF;
			z &= 0xF;
			int localY = y & 0xF;
			int i = localY * 256 + z * 16 + x;

			int id = blocks[i] & 0xFF;
			if (add != null && add.length > 0) id |= getNibble(add, i) << 8;
			int meta = (data != null && data.length > 0) ? getNibble(data, i) : 0;

			BlockID bid = BlockID.query(id, meta);
			if (bid == null) bid = BlockID.query(id);
			if (bid == null) return BlockState.MISSING;

			Map<String, String> metadataToProperties = BlockID.metadataToProperties(bid, meta);

			// snowy grass check
			// todo: add more of these properties from ChunkMCRegion.java
			if (id == 2) {
				int aboveId = 0;
				int aboveMeta = 0;
				if (localY + 1 < 16 && sections[sectionIndex] != null) {
					aboveId = blocks[(localY + 1) * 256 + z * 16 + x] & 0xFF;
				} else if (sectionIndex + 1 < 16 && sections[sectionIndex + 1] != null) {
					Section next = sections[sectionIndex + 1];
					aboveId = next.blocks[z * 16 + x] & 0xFF;
				}
				metadataToProperties.put("snowy", (aboveId == 78 || aboveId == 80) ? "true" : "false");
			} else if (id == 85) {
				metadataToProperties.put("west",  getBlockIdNearby(x, localY, z, -1, 0, 0) == 85 ? "true" : "false");
				metadataToProperties.put("east",  getBlockIdNearby(x, localY, z, 1, 0, 0) == 85 ? "true" : "false");
				metadataToProperties.put("north", getBlockIdNearby(x, localY, z, 0, 0, -1) == 85 ? "true" : "false");
				metadataToProperties.put("south", getBlockIdNearby(x, localY, z, 0, 0, 1) == 85 ? "true" : "false");
			} else if (id == 90) {
				// portal
				int neighborXMinus = getBlockIdNearby(x, localY, z, -1, 0, 0);
				int neighborXPlus  = getBlockIdNearby(x, localY, z, 1, 0, 0);

				if (neighborXMinus == 90 || neighborXPlus == 90)
					metadataToProperties.put("axis", "x");
				else
					metadataToProperties.put("axis", "z");
			} else if (id == 102 || id == 101) {
				// glass pane & iron bar
				// todo: dont connect fences to panes and bars and etc
				int west  = getBlockIdNearby(x, localY, z, -1, 0, 0);
				int east  = getBlockIdNearby(x, localY, z, 1, 0, 0);
				int north = getBlockIdNearby(x, localY, z, 0, 0, -1);
				int south = getBlockIdNearby(x, localY, z, 0, 0, 1);

				if (west > 0)
					metadataToProperties.put("west", "true");
				if (east > 0)
					metadataToProperties.put("east", "true");
				if (north > 0)
					metadataToProperties.put("north", "true");
				if (south > 0)
					metadataToProperties.put("south", "true");
			} else if (id == 54) {
				// chest

				int west  = getBlockIdNearby(x, localY, z, -1, 0, 0);
				int east  = getBlockIdNearby(x, localY, z, 1, 0, 0);
				int north = getBlockIdNearby(x, localY, z, 0, 0, -1);
				int south = getBlockIdNearby(x, localY, z, 0, 0, 1);

				if (west == 54) {
					int westNorth = getBlockIdNearby(x, localY, z, -1, 0, 1);
					int northThis = getBlockIdNearby(x, localY, z, 0, 0, 1);
					if (BlockID.isOpaque(westNorth) || BlockID.isOpaque(northThis)) {
						metadataToProperties.put("facing", "north");
						metadataToProperties.put("type", "right");
					} else {
						metadataToProperties.put("facing", "south");
						metadataToProperties.put("type", "left");
					}
				} else if (east == 54) {
					int eastNorth = getBlockIdNearby(x, localY, z, 1, 0, 1);
					int northThis = getBlockIdNearby(x, localY, z, 0, 0, 1);
					if (BlockID.isOpaque(eastNorth) || BlockID.isOpaque(northThis)) {
						metadataToProperties.put("facing", "north");
						metadataToProperties.put("type", "left");
					} else {
						metadataToProperties.put("facing", "south");
						metadataToProperties.put("type", "right");
					}
				} else if (north == 54) {
					int northEast = getBlockIdNearby(x, localY, z, 1, 0, -1);
					int eastThis  = getBlockIdNearby(x, localY, z, 1, 0, 0);
					if (BlockID.isOpaque(northEast) || BlockID.isOpaque(eastThis)) {
						metadataToProperties.put("facing", "west");
						metadataToProperties.put("type", "left");
					} else {
						metadataToProperties.put("facing", "east");
						metadataToProperties.put("type", "right");
					}
				} else if (south == 54) {
					int southEast = getBlockIdNearby(x, localY, z, 1, 0, 1);
					int eastThis  = getBlockIdNearby(x, localY, z, 1, 0, 0);
					if (BlockID.isOpaque(southEast) || BlockID.isOpaque(eastThis)) {
						metadataToProperties.put("facing", "west");
						metadataToProperties.put("type", "right");
					} else {
						metadataToProperties.put("facing", "east");
						metadataToProperties.put("type", "left");
					}
				} else {
					// singular chest
					metadataToProperties.put("type", "single");

					if (BlockID.isOpaque(north))
						metadataToProperties.put("facing", "south");
					else if (BlockID.isOpaque(west))
						metadataToProperties.put("facing", "east");
					else if (BlockID.isOpaque(south))
						metadataToProperties.put("facing", "north");
					else if (BlockID.isOpaque(east))
						metadataToProperties.put("facing", "west");
					else
						metadataToProperties.put("facing", "south");
				}
			} else if (id == 64 || id == 71) { 
				// doors
				metadataToProperties.put("hinge", "left");
				metadataToProperties.put("powered", "false");

				if (meta < 8)
					metadataToProperties.put("half", "lower");
				else
					metadataToProperties.put("half", "upper");

				meta %= 8;

				if (meta < 4)
					metadataToProperties.put("open", "false");
				else
					metadataToProperties.put("open", "true");

				meta %= 4;

				if (meta == 0)
					metadataToProperties.put("facing", "east");
				else if (meta == 1)
					metadataToProperties.put("facing", "south");
				else if (meta == 2)
					metadataToProperties.put("facing", "west");
				else if (meta == 3)
					metadataToProperties.put("facing", "north");
			} else if (id == 51) { 
				// fire
				int below = y > 0 ? getBlockIdNearby(x, localY, z, 0, -1, 0) : 0;

				if (BlockID.isOpaque(below) || below == 30 || below == 52 || below == 85) {
					metadataToProperties.put("west", "false");
					metadataToProperties.put("east", "false");
					metadataToProperties.put("north", "false");
					metadataToProperties.put("south", "false");
					metadataToProperties.put("up", "false");
				} else {
					int above = (localY + 1 < 16) ? getBlockIdNearby(x, localY, z, 0, 1, 0) : 0;
					if (BlockID.isFlammable(above)) metadataToProperties.put("up", "true");

					if (BlockID.isFlammable(getBlockIdNearby(x, localY, z, -1, 0, 0))) metadataToProperties.put("west", "true");
					if (BlockID.isFlammable(getBlockIdNearby(x, localY, z, 1, 0, 0)))  metadataToProperties.put("east", "true");
					if (BlockID.isFlammable(getBlockIdNearby(x, localY, z, 0, 0, -1))) metadataToProperties.put("north", "true");
					if (BlockID.isFlammable(getBlockIdNearby(x, localY, z, 0, 0, 1)))  metadataToProperties.put("south", "true");
				}
			}

			BlockState state = BlockState.of(bid.getModernId(), metadataToProperties);

			return new BlockState(state.getFormatted(), state.getProperties()) {
				@Override
				public boolean isAir() { return false; }
			};
		}

		public LightData getLightData(int x, int y, int z, LightData target) {
			x &= 0xF;
			y &= 0xF;
			z &= 0xF;
			int i = y * 256 + z * 16 + x;

			int blockLightVal = getNibble(blockLight, i);
			int skyLightVal = getNibble(skyLight, i);

			int id = blocks[i] & 0xFF;

			if (!BlockID.isOpaque(BlockID.query(id, 0))) {
				skyLightVal = Math.max(skyLightVal,
						Math.max(getSkyLightSafe(x - 1, y, z),
								Math.max(getSkyLightSafe(x + 1, y, z),
										Math.max(getSkyLightSafe(x, y, z - 1),
												Math.max(getSkyLightSafe(x, y, z + 1),
														getSkyLightSafe(x, y + 1, z))))));

				blockLightVal = Math.max(blockLightVal,
						Math.max(getBlockLightSafe(x - 1, y, z),
								Math.max(getBlockLightSafe(x + 1, y, z),
										Math.max(getBlockLightSafe(x, y, z - 1),
												Math.max(getBlockLightSafe(x, y, z + 1),
														getBlockLightSafe(x, y + 1, z))))));
			}

			return target.set(skyLightVal, blockLightVal);
		}

		private int getBlockLightSafe(int x, int y, int z) {
			try {
				return getNibble(blockLight, (y & 0xF) * 256 + (z & 0xF) * 16 + (x & 0xF));
			} catch (Exception e) { return 0; }
		}

		private int getSkyLightSafe(int x, int y, int z) {
			try {
				return getNibble(skyLight, (y & 0xF) * 256 + (z & 0xF) * 16 + (x & 0xF));
			} catch (Exception e) { return 15; }
		}

		// silly litttle helper cause im lazy
		private int getBlockIdNearby(int x, int y, int z, int dx, int dy, int dz) {
			int nx = (x + dx) & 0xF;
			int ny = (y & 0xF) + dy;
			int nz = (z + dz) & 0xF;

			if (ny < 0 || ny > 15) return 0;
			if (nx < 0 || nx > 15) return 0;
			if (nz < 0 || nz > 15) return 0;

			return blocks[ny * 256 + nz * 16 + nx] & 0xFF;
		}
	}
}
