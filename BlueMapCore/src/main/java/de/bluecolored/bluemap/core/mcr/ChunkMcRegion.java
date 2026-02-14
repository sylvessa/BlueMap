/*
 * This file is part of BlueMap, licensed under the MIT License (MIT).
 *
 * Copyright (c) Blue (Lukas Rieger) <https://bluecolored.de>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.bluecolored.bluemap.core.mcr;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.bluecolored.bluemap.core.BlueMap;
import de.bluecolored.bluemap.core.util.BlockPropertyHelper;
import de.bluecolored.bluemap.core.world.BlockState;
import de.bluecolored.bluemap.core.world.Chunk;
import de.bluecolored.bluemap.core.world.LightData;
import de.bluecolored.bluemap.core.util.BlockID;
import net.querz.nbt.CompoundTag;
import net.querz.nbt.ListTag;
import org.apache.commons.lang3.math.NumberUtils;

@SuppressWarnings("FieldMayBeFinal")
public class ChunkMcRegion extends MCRChunk {
    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    private boolean isGenerated;
    private boolean hasLight;
    private Section section;
    private final Map<Long, CompoundTag> tileEntities;

    @SuppressWarnings("unchecked")
    public ChunkMcRegion(MCRWorld world, CompoundTag chunkTag) {
        super(world, chunkTag);

        CompoundTag levelData = chunkTag.getCompoundTag("Level");

        this.isGenerated = levelData.getBoolean("TerrainPopulated");
        this.hasLight = isGenerated;
        this.tileEntities = new HashMap<>();

        ListTag<CompoundTag> tiles = (ListTag<CompoundTag>) levelData.getListTag("TileEntities");
        if (tiles != null) {
            for (CompoundTag te : tiles) {
                int x = te.getInt("x") & 15;
                int y = te.getInt("y");
                int z = te.getInt("z") & 15;

                long key = (((long) y) << 8) | (z << 4) | x;
                tileEntities.put(key, te);
            }
        }

        section = new Section(levelData, world, this);
    }

    @Override
    public boolean isGenerated() {
        return isGenerated;
    }

    @Override
    public long getInhabitedTime() {
        return 1;
    }
    
    @Override
    public int fromBlocksArray(int x, int y, int z) {
    	x &= 0xF; z &= 0xF;
    	
    	return this.section.blocks[x << 11 | z << 7 | y] & 255;
    }

    public CompoundTag getTileEntity(int x, int y, int z) {
        long key = (((long) y) << 8) | ((z & 15) << 4) | (x & 15);
        return tileEntities.get(key);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        if (y >= 128 || y <= 0)
        	return BlockState.AIR;

        if (this.section == null)
        	return BlockState.AIR;

        return this.section.getBlockState(x, y, z);
    }

    @Override
    public LightData getLightData(int x, int y, int z, LightData target) {
        if (!hasLight) return target.set(getWorld().getSkyLight(), 0);

        if (y >= 128 || y <= 0)
            return (y < 0) ? target.set(0, 0) : target.set(getWorld().getSkyLight(), 0);

        if (this.section == null)
        	return target.set(getWorld().getSkyLight(), 0);

        return this.section.getLightData(x, y, z, target);
    }

    @Override
    public String getBiome(int x, int y, int z) {
        return LegacyBiomes.idFor(this.getWorld().wcm.getBiome(x, z));
    }

    @Override
    public int getWorldSurfaceY(int x, int z) {
        return 63;
    }

    @Override
    public int getOceanFloorY(int x, int z) {
        return 50; // TODO figure out the actual min noise value
    }

    private static class Section {
        private static final int AIR_ID = 0;

        private NibbleArray blockLight;
        private NibbleArray skyLight;
        private NibbleArray metadata;
        protected byte[] blocks;
        protected ListTag tileentities;
        private MCRWorld world;
        private ChunkMcRegion chunk;

        public Section(CompoundTag sectionData, MCRWorld world, ChunkMcRegion chunk) {
        	this.world = world;
            this.chunk = chunk;
            this.blockLight = new NibbleArray(sectionData.getByteArray("BlockLight"));
            this.skyLight = new NibbleArray(sectionData.getByteArray("SkyLight"));
            this.metadata = new NibbleArray(sectionData.getByteArray("Data"));
            this.blocks = sectionData.getByteArray("Blocks");
            this.tileentities = sectionData.getListTag("TileEntities");

            if (blocks.length < 256 && blocks.length > 0) blocks = Arrays.copyOf(blocks, 256);
            if (metadata.data.length < 256 && metadata.data.length > 0) metadata.data = Arrays.copyOf(metadata.data, 256);
            if (blockLight.data.length < 2048 && blockLight.data.length > 0) blockLight.data = Arrays.copyOf(blockLight.data, 2048);
            if (skyLight.data.length < 2048 && skyLight.data.length > 0) skyLight.data = Arrays.copyOf(skyLight.data, 2048);
        }

        public int getSectionY() {
            throw new RuntimeException("Method ChunkMcRegion.getSectionY() is unimplemented");
        }

        public BlockState getBlockState(int x, int y, int z) {
			if (blocks.length == 0) return BlockState.AIR;

			int ox = x, oz = z;
			x &= 0xF; z &= 0xF;

			int blockId = this.blocks[x << 11 | z << 7 | y] & 255;
			int metadata = this.metadata.getData(x, y, z);

			if (blockId == 0) return BlockState.AIR;

			BlockID bid = BlockID.query(blockId, metadata);
			if (bid == null) bid = BlockID.query(blockId);
			if (bid == null) return BlockState.MISSING;

			BlockState baseState = BlockState.of(bid.getModernId(), BlockID.metadataToProperties(bid, metadata));

            int[][][][] neighbors = new int[3][3][3][2];

            for (int dy = -1; dy <= 1; dy++) {
                int ny = y + dy;

                for (int dz = -1; dz <= 1; dz++) {
                    int nz = oz + dz;
                    for (int dx = -1; dx <= 1; dx++) {
                        int nx = ox + dx;

                        int nId = 0;
                        int nMeta = 0;

                        ChunkMcRegion neighborChunk;
                        try {
                            Chunk neighbor = this.world.getChunkAtBlock(nx, ny, nz);
                            if (neighbor instanceof ChunkMcRegion) {
                                neighborChunk = (ChunkMcRegion) neighbor;
                                nId = neighborChunk.fromBlocksArray(nx, ny, nz);
                                nMeta = neighborChunk.section.metadata.getData(nx, ny, nz);
                            } else {
                                nId = 0;
                                nMeta = 0;
                            }
                        } catch (Exception e) {
                            nId = 0;
                            nMeta = 0;
                        }

                        neighbors[dy + 1][dz + 1][dx + 1][0] = nId;
                        neighbors[dy + 1][dz + 1][dx + 1][1] = nMeta;
                    }
                }
            }

            return BlockPropertyHelper.applySpecialProperties(blockId, metadata, ox, y, oz, neighbors, baseState, chunk.getTileEntity(ox, y, oz));
		}

        public LightData getLightData(int x, int y, int z, LightData target) {
            if (blockLight.data.length == 0 && skyLight.data.length == 0) return target.set(0, 0);

            x &= 0xF; z &= 0xF;
            
            int blocklight = this.blockLight.data.length > 0 ? blockLight.getData(x, y, z) : 0;
            int skylight = this.skyLight.data.length > 0 ? skyLight.getData(x, y, z) : 0;

            int block_id = this.blocks[x << 11 | z << 7 | y] & 255;

			if (block_id == 44 || block_id == 53 || block_id == 67 || (BlockID.isOpaque(BlockID.query(block_id)) && block_id > 0)) {
				blocklight = NumberUtils.max(
						blocklight,
						this.getBlockLight(x-1, y, z),
						this.getBlockLight(x+1, y, z),
						this.getBlockLight(x, y, z-1),
						this.getBlockLight(x, y, z+1),
						this.getBlockLight(x, y+1, z)
				);

				skylight = NumberUtils.max(
						skylight,
						this.getSkyLight(x-1, y, z),
						this.getSkyLight(x+1, y, z),
						this.getSkyLight(x, y, z-1),
						this.getSkyLight(x, y, z+1),
						this.getSkyLight(x, y+1, z)
				);
            }

            return target.set(skylight, blocklight);
        }

		private int getBlockLight(int x, int y, int z) {
			try {
				return this.blockLight.getData(x, y, z);
			} catch (Exception ex) {
				return 0;
			}
		}

		private int getSkyLight(int x, int y, int z) {
			try {
				return this.skyLight.getData(x, y, z);
			} catch (Exception ex) {
				return 0;
			}
		}
    }

}
