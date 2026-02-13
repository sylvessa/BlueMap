package de.bluecolored.bluemap.core.mca;

import de.bluecolored.bluemap.core.util.BlockID;
import de.bluecolored.bluemap.core.world.BlockState;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.logger.Logger;
import de.bluecolored.bluemap.core.world.*;
import de.bluecolored.bluemap.core.util.BlockPropertyHelper;
import net.querz.nbt.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ChunkAnvil12 extends MCAChunk {

	private boolean isGenerated;
	private long inhabitedTime;
	private Section[] sections;
	private byte[] biomes;
	private Map<Long, CompoundTag> tileEntities;

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

		this.tileEntities = new HashMap<>();

		// todo: does this even work?
		// i wrote this on a whim and never tested it
		ListTag<CompoundTag> tiles = (ListTag<CompoundTag>) level.getListTag("TileEntities");
		if (tiles != null) {
			for (CompoundTag te : tiles) {
				int x = te.getInt("x") & 15;
				int y = te.getInt("y");
				int z = te.getInt("z") & 15;

				long key = (((long) y) << 8) | (z << 4) | x;
				tileEntities.put(key, te);
			}
		}
	}

	public CompoundTag getTileEntity(int x, int y, int z) {
		long key = (((long) y) << 8) | ((z & 15) << 4) | (x & 15);
		return tileEntities.get(key);
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

		return s.getBlockState(x, y, z, sections, sy, this);
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

		public BlockState getBlockState(int x, int y, int z, Section[] sections, int sectionIndex, ChunkAnvil12 chunk) {
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

			BlockState baseState = BlockState.of(bid.getModernId(), BlockID.metadataToProperties(bid, meta));

			// gather neighbors
			int[] horizontal = {
				getBlockIdNearby(x, localY, z, -1, 0, 0),
				getBlockIdNearby(x, localY, z, 1, 0, 0),
				getBlockIdNearby(x, localY, z, 0, 0, -1),
				getBlockIdNearby(x, localY, z, 0, 0, 1)
			};

			int above = 0;
			if (localY + 1 < 16 && sections[sectionIndex] != null)
				above = blocks[(localY + 1) * 256 + z * 16 + x] & 0xFF;
			else if (sectionIndex + 1 < 16 && sections[sectionIndex + 1] != null)
				above = sections[sectionIndex + 1].blocks[z * 16 + x] & 0xFF;

			int[] vertical = {above, 0};

			return BlockPropertyHelper.applySpecialProperties(id, meta, x, y, z,
				new int[][] { horizontal, vertical }, baseState);
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
