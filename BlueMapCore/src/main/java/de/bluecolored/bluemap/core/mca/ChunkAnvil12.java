package de.bluecolored.bluemap.core.mca;

import de.bluecolored.bluemap.core.mcr.BlockID;
import de.bluecolored.bluemap.core.world.BlockState;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.logger.Logger;
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
				Section s = new Section(sec);
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

		public Section(CompoundTag tag) {
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

			Map<String, String> props = BlockID.metadataToProperties(bid, meta);

			// snowy grass check
			if (id == 2) {
				int aboveId = 0;
				int aboveMeta = 0;
				if (localY + 1 < 16 && sections[sectionIndex] != null) {
					aboveId = blocks[(localY + 1) * 256 + z * 16 + x] & 0xFF;
				} else if (sectionIndex + 1 < 16 && sections[sectionIndex + 1] != null) {
					Section next = sections[sectionIndex + 1];
					aboveId = next.blocks[z * 16 + x] & 0xFF;
				}
				props.put("snowy", (aboveId == 78 || aboveId == 80) ? "true" : "false");
			}

			return new BlockState(bid.getModernId(), props) {
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
	}
}
