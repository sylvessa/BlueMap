package de.bluecolored.bluemap.core.util;

import de.bluecolored.bluemap.core.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public enum BlockID {
	// block names copied from Bukkit
	AIR(0, "minecraft:air"),
	STONE(1, "minecraft:stone"),
	GRASS(2, "minecraft:grass_block"),
	DIRT(3, "minecraft:dirt"),
	COBBLESTONE(4, "minecraft:cobblestone"),
	WOOD(5, "minecraft:oak_planks"),
	SPRUCE_WOOD(5, 1, "minecraft:spruce_planks"),
	BIRCH_WOOD(5, 2, "minecraft:birch_planks"),
	JUNGLE_WOOD(5, 3, "minecraft:jungle_planks"),
	OAK_SAPLING(6, "minecraft:oak_sapling"),
	SPRUCE_SAPLING(6, 1, "minecraft:spruce_sapling"),
	BIRCH_SAPLING(6, 2, "minecraft:birch_sapling"),
	JUNGLE_SAPLING(6, 3, "minecraft:jungle_sapling"),
	BEDROCK(7, "minecraft:bedrock"),
	WATER(8, "minecraft:flowing_water"),
	STATIONARY_WATER(9, "minecraft:water"),
	LAVA(10, "minecraft:lava"),
	STATIONARY_LAVA(11, "minecraft:lava"),
	SAND(12, "minecraft:sand"),
	RED_SAND(12, 1, "minecraft:red_sand"),
	GRAVEL(13, "minecraft:gravel"),
	GOLD_ORE(14, "minecraft:gold_ore"),
	IRON_ORE(15, "minecraft:iron_ore"),
	COAL_ORE(16, "minecraft:coal_ore"),
	OAK_LOG(17, "minecraft:oak_log"),
	SPRUCE_LOG(17, 1, "minecraft:spruce_log"),
	BIRCH_LOG(17, 2, "minecraft:birch_log"),
	JUNGLE_LOG(17, 3, "minecraft:jungle_log"),
	OAK_LEAVES(18, "minecraft:oak_leaves"),
	SPRUCE_LEAVES(18, 1, "minecraft:spruce_leaves"),
	BIRCH_LEAVES(18, 2, "minecraft:birch_leaves"),
	SPECIAL_LEAVES(18, 3, "minecraft:jungle_leaves"),
	SPONGE(19, "minecraft:sponge"),
	WET_SPONGE(19, 1, "minecraft:wet_sponge"),
	GLASS(20, "minecraft:glass"),
	LAPIS_ORE(21, "minecraft:lapis_ore"),
	LAPIS_BLOCK(22, "minecraft:lapis_block"),
	DISPENSER(23, "minecraft:dispenser"),
	SANDSTONE(24, "minecraft:sandstone"),
	CHISELED_SANDSTONE(24, 1, "minecraft:chiseled_sandstone"),
	SMOOTH_SANDSTONE(24, 2, "minecraft:smooth_sandstone"),
	NOTE_BLOCK(25, "minecraft:note_block"),
	BED(26, "minecraft:red_bed"),
	GOLDEN_RAIL(27, "minecraft:powered_rail"),
	DETECTOR_RAIL(28, "minecraft:detector_rail"),
	PISTON_STICKY(29, "minecraft:sticky_piston"),
    PISTON_STICKY_DV6(29, 6, "bluemap:six_sided_sticky_piston"),
    PISTON_STICKY_DV7(29, 7, "bluemap:six_sided_sticky_piston"),
	WEB(30, "minecraft:cobweb"),
	DEAD_BUSH_ON_GRASS(31, "minecraft:dead_bush"),
	LONG_GRASS(31, 1, "minecraft:grass"),
	FERN(31, 2, "minecraft:fern"),
	DEAD_BUSH(32, "minecraft:dead_bush"),
	PISTON(33, "minecraft:piston"),
    PISTON_DV6(33, 6, "bluemap:six_sided_piston"),
    PISTON_DV7(33, 7, "bluemap:six_sided_piston"),
	PISTON_EXTENSION(34, "minecraft:piston_head"),
	WOOL(35, "minecraft:white_wool"),
	ORANGE_WOOL(35, 1, "minecraft:orange_wool"),
	MAGENTA_WOOL(35, 2, "minecraft:magenta_wool"),
	LIGHTBLUE_WOOL(35, 3, "minecraft:light_blue_wool"),
	YELLOW_WOOL(35, 4, "minecraft:yellow_wool"),
	LIME_WOOL(35, 5, "minecraft:lime_wool"),
	PINK_WOOL(35, 6, "minecraft:pink_wool"),
	GRAY_WOOL(35, 7, "minecraft:gray_wool"),
	LIGHTGRAY_WOOL(35, 8, "minecraft:light_gray_wool"),
	CYAN_WOOL(35, 9, "minecraft:cyan_wool"),
	PURPLE_WOOL(35, 10, "minecraft:purple_wool"),
	BLUE_WOOL(35, 11, "minecraft:blue_wool"),
	BROWN_WOOL(35, 12, "minecraft:brown_wool"),
	GREEN_WOOL(35, 13, "minecraft:green_wool"),
	RED_WOOL(35, 14, "minecraft:red_wool"),
	BLACK_WOOL(35, 15, "minecraft:black_wool"),
	PISTON_MOVING(36, "minecraft:moving_piston"),
	YELLOW_FLOWER(37, "minecraft:dandelion"),
	RED_ROSE(38, "minecraft:poppy"),
	BROWN_MUSHROOM(39, "minecraft:brown_mushroom"),
	RED_MUSHROOM(40, "minecraft:red_mushroom"),
	GOLD_BLOCK(41, "minecraft:gold_block"),
	IRON_BLOCK(42, "minecraft:iron_block"),
	DOUBLE_STEP_STONE(43, "minecraft:smooth_stone_slab"),
	DOUBLE_STEP_SANDSTONE(43, 1, "minecraft:sandstone_slab"),
	DOUBLE_STEP_OAK(43, 2, "minecraft:oak_slab"),
	DOUBLE_STEP_COBBLESTONE(43, 3, "minecraft:cobblestone_slab"),
	DOUBLE_STEP_SPECIAL_STONE(43, 4, "minecraft:brick"),
	DOUBLE_STEP_SPECIAL_SANDSTONE(43, 5, "minecraft:stone_bricks"),
	DOUBLE_STEP_SPECIAL_OAK(43, 6, "minecraft:nether_bricks"),
	DOUBLE_STEP_SPECIAL_COBBLESTONE(43, 7, "minecraft:quartz_block"),
	STEP_STONE(44, "minecraft:smooth_stone_slab"),
	STEP_SANDSTONE(44, 1, "minecraft:sandstone_slab"),
	STEP_OAK(44, 2, "minecraft:oak_slab"),
	STEP_COBBLESTONE(44, 3, "minecraft:cobblestone_slab"),
	STEP_BRICK(44, 4, "minecraft:brick_slab"),
	STEP_STONE_BRICK(44, 5, "minecraft:stone_brick_slab"),
	STEP_NETHER_BRICK(44, 6, "minecraft:nether_brick_slab"),
	STEP_QUARTZ(44, 7, "minecraft:quartz_slab"),
	STEP_STONE_TOP(44, 8, "minecraft:smooth_stone_slab"),
	STEP_SANDSTONE_TOP(44, 9, "minecraft:sandstone_slab"),
	STEP_OAK_TOP(44, 10, "minecraft:oak_slab"),
	STEP_COBBLESTONE_TOP(44, 11, "minecraft:cobblestone_slab"),
	STEP_BRICK_TOP(44, 12, "minecraft:brick_slab"),
	STEP_STONE_BRICK_TOP(44, 13, "minecraft:stone_brick_slab"),
	STEP_NETHER_BRICK_TOP(44, 14, "minecraft:nether_brick_slab"),
	STEP_QUARTZ_TOP(44, 15, "minecraft:quartz_slab"),
	BRICK(45, "minecraft:bricks"),
	TNT(46, "minecraft:tnt"),
	BOOKSHELF(47, "minecraft:bookshelf"),
	MOSSY_COBBLESTONE(48, "minecraft:mossy_cobblestone"),
	OBSIDIAN(49, "minecraft:obsidian"),
	TORCH_GROUND(50, 5, "minecraft:torch"),
	TORCH_WALL(50, "minecraft:wall_torch"),
	FIRE(51, "minecraft:fire"),
	MOB_SPAWNER(52, "minecraft:spawner"),
	WOOD_STAIRS(53, "minecraft:oak_stairs"),
	WOOD_STAIRS_INVISIBLE(53, 5, "minecraft:barrier"),
	CHEST(54, "minecraft:chest"),
	REDSTONE_WIRE(55, "minecraft:redstone_wire"), // TODO later
	DIAMOND_ORE(56, "minecraft:diamond_ore"),
	DIAMOND_BLOCK(57, "minecraft:diamond_block"),
	WORKBENCH(58, "minecraft:crafting_table"),
	CROPS(59, "minecraft:wheat"),
	SOIL(60, "minecraft:farmland"),
	FURNACE(61, "minecraft:furnace"),
	BURNING_FURNACE(62, "minecraft:furnace"),
	SIGN_POST(63, "minecraft:oak_sign"),
	WOODEN_DOOR(64, "minecraft:oak_door"),
	LADDER(65, "minecraft:ladder"),
	RAILS(66, "minecraft:rail"),
	COBBLESTONE_STAIRS(67, "minecraft:cobblestone_stairs"),
	COBBLESTONE_STAIRS_INVISIBLE(67, 5, "minecraft:barrier"),
	WALL_SIGN(68, "minecraft:oak_wall_sign"),
	LEVER(69, "minecraft:lever"),
	STONE_PLATE(70, "minecraft:stone_pressure_plate"),
	IRON_DOOR_BLOCK(71, "minecraft:iron_door"),
	WOOD_PLATE(72, "minecraft:oak_pressure_plate"),
	REDSTONE_ORE(73, "minecraft:redstone_ore"),
	GLOWING_REDSTONE_ORE(74, "minecraft:redstone_ore"),
	REDSTONE_TORCH_OFF_GROUND(75, 5, "minecraft:redstone_torch"),
	REDSTONE_TORCH_OFF_WALL(75, "minecraft:redstone_wall_torch"),
	REDSTONE_TORCH_ON_GROUND(76, 5, "minecraft:redstone_torch"),
	REDSTONE_TORCH_ON_WALL(76, "minecraft:redstone_wall_torch"),
	STONE_BUTTON(77, "minecraft:stone_button"),
	SNOW(78, "minecraft:snow"),
	ICE(79, "minecraft:ice"),
	SNOW_BLOCK(80, "minecraft:snow_block"),
	CACTUS(81, "minecraft:cactus"),
	CLAY(82, "minecraft:clay"),
	SUGAR_CANE_BLOCK(83, "minecraft:sugar_cane"),
	JUKEBOX(84, "minecraft:jukebox"),
	FENCE(85, "minecraft:oak_fence"),
	PUMPKIN(86, "minecraft:carved_pumpkin"),
	NETHERRACK(87, "minecraft:netherrack"),
	SOUL_SAND(88, "minecraft:soul_sand"),
	GLOWSTONE(89, "minecraft:glowstone"),
	PORTAL(90, "minecraft:nether_portal"),
	JACK_O_LANTERN(91, "minecraft:jack_o_lantern"),
	CAKE_BLOCK(92, "minecraft:cake"),
	DIODE_OFF(93, "minecraft:repeater"),
	DIODE_ON(94, "minecraft:repeater"),
	LOCKED_CHEST(95, "minecraft:chest_locked_aprilfools_super_old_legacy_we_should_not_even_have_this"),
	TRAP_DOOR(96, "minecraft:oak_trapdoor"),
	STONEBRICK(98, "minecraft:stone_bricks"),
	STONEBRICK_MOSSY(98, 1, "minecraft:mossy_stone_bricks"),
	STONEBRICK_CRACKED(98, 2, "minecraft:cracked_stone_bricks"),
	STONEBRICK_CHISELED(98, 3, "minecraft:chiseled_stone_bricks"),
	BROWN_MUSHROOM_BLOCK(99, "minecraft:brown_mushroom_block"),
	RED_MUSHROOM_BLOCK(100, "minecraft:red_mushroom_block"),
	IRON_BARS(101, "minecraft:iron_bars"),
	GLASS_PANE(102, "minecraft:glass_pane"),
	MELON_BLOCK(103, "minecraft:melon"),
	PUMPKIN_STEM(104, "minecraft:pumpkin_stem"),
	MELON_STEM(105, "minecraft:melon_stem"),
	VINES(106, "minecraft:vine"),
	FENCE_GATE(107, "minecraft:oak_fence_gate"),
	BRICK_STAIRS(108, "minecraft:bricks"),
	STONE_BRICK_STAIRS(109, "minecraft:stone_bricks"),
	MYCELIUM(110, "minecraft:mycelium"),
	LILY_PAD(111, "minecraft:lily_pad"),
	NETHER_BRICK(112, "minecraft:nether_bricks"),
	NETHER_BRICK_FENCE(113, "minecraft:nether_bricks"),
	NETHER_BRICK_STAIRS(114, "minecraft:nether_bricks"),
	NETHER_WART(115, "minecraft:nether_wart"),
	ENCHANTMENT_TABLE(116, "minecraft:enchanting_table"),
	BREWING_STAND(117, "minecraft:brewing_stand"),
	CAULDRON(118, "minecraft:cauldron"),
	END_PORTAL(119, "minecraft:end_portal"),
	END_PORTAL_FRAME(120, "minecraft:end_portal_frame"),
	END_STONE(121, "minecraft:end_stone"),
	DRAGON_EGG(122, "minecraft:dragon_egg"),
	REDSTONE_LAMP_INACTIVE(123, "minecraft:redstone_lamp"),
	REDSTONE_LAMP_ACTIVE(124, "minecraft:redstone_lamp_on"),
	WOODEN_DOUBLE_STEP(125, "minecraft:oak_planks"),
	SPRUCE_DOUBLE_STEP(125, 1, "minecraft:spruce_planks"),
	BIRCH_DOUBLE_STEP(125, 2, "minecraft:birch_planks"),
	JUNGLE_DOUBLE_STEP(125, 3, "minecraft:jungle_planks"),
	ACACIA_DOUBLE_STEP(125, 4, "minecraft:acacia_planks"),
	DARK_OAK_DOUBLE_STEP(125, 5, "minecraft:dark_oak_planks"),
	WOODEN_STEP(126, "minecraft:oak_slab"),
	SPRUCE_WOODEN_STEP(126, 1, "minecraft:spruce_slab"),
	BIRCH_WOODEN_STEP(126, 2, "minecraft:birch_slab"),
	JUNGLE_WOODEN_STEP(126, 3, "minecraft:jungle_slab"),
	ACACIA_WOODEN_STEP(126, 4, "minecraft:acacia_slab"),
	DARK_OAK_WOODEN_STEP(126, 5, "minecraft:dark_oak_slab"),
	WOODEN_STEP_TOP(126, 8, "minecraft:oak_slab"),
	SPRUCE_WOODEN_STEP_TOP(126, 9, "minecraft:spruce_slab"),
	BIRCH_WOODEN_STEP_TOP(126, 10, "minecraft:birch_slab"),
	JUNGLE_WOODEN_STEP_TOP(126, 11, "minecraft:jungle_slab"),
	ACACIA_WOODEN_STEP_TOP(126, 12, "minecraft:acacia_slab"),
	DARK_OAK_WOODEN_STEP_TOP(126, 13, "minecraft:dark_oak_slab"),
	COCOA(127, "minecraft:cocoa"),
	SANDSTONE_STAIRS(128, "minecraft:sandstone_stairs"),
	EMERALD_ORE(129, "minecraft:emerald_ore"),
	ENDER_CHEST(130, "minecraft:ender_chest"),
	TRIPWIRE_HOOK(131, "minecraft:tripwire_hook"),
	TRIPWIRE(132, "minecraft:tripwire"),
	EMERALD_BLOCK(133, "minecraft:emerald_block"),
	SPRUCE_STAIRS(134, "minecraft:spruce_stairs"),
	BIRCH_STAIRS(135, "minecraft:birch_stairs"),
	JUNGLE_STAIRS(136, "minecraft:jungle_stairs"),
	COMMAND_BLOCK(137, "minecraft:command_block"),
	BEACON(138, "minecraft:beacon"),
	COBBLESTONE_WALL(139, "minecraft:cobblestone_wall"),
	MOSSY_COBBLESTONE_WALL(139, 1, "minecraft:mossy_cobblestone_wall"),
	FLOWER_POT(140, "minecraft:flower_pot"),
	POTTED_POPPY(140, 1,"minecraft:potted_poppy"),
	POTTED_DANDELION(140, 2,"minecraft:potted_dandelion"),
	POTTED_OAK_SAPLING(140, 3,"minecraft:potted_oak_sapling"),
	POTTED_SPRUCE_SAPLING(140, 4,"minecraft:potted_spruce_sapling"),
	POTTED_BIRCH_SAPLING(140, 5,"minecraft:potted_birch_sapling"),
	POTTED_JUNGLE_SAPLING(140, 6,"minecraft:potted_jungle_sapling"),
	POTTED_RED_MUSHROOM(140, 7,"minecraft:potted_red_mushroom"),
	POTTED_BROWN_MUSHROOM(140, 8,"minecraft:potted_brown_mushroom"),
	POTTED_CACTUS(140, 9,"minecraft:potted_cactus"),
	POTTED_DEAD_BUSH(140, 10,"minecraft:potted_dead_bush"),
	POTTED_FERN(140, 11,"minecraft:potted_fern"),
	POTTED_ACACIA_SAPLING(140, 12,"minecraft:potted_acacia_sapling"),
	POTTED_DARK_OAK_SAPLING(140, 13,"minecraft:potted_dark_oak_sapling"),
	CARROTS(141, "minecraft:carrots"),
	POTATOES(142, "minecraft:potatoes"),
	WOODEN_BUTTON(143, "minecraft:oak_button"),
	MOB_HEADS(144, "minecraft:skull"),
	ANVIL(145, "minecraft:anvil"),
	;
	
	public final int id;
	public final int data;
	private final String val;
	private final HashMap<String, String> properties = new HashMap<>();
	private final static BlockID[] VALUES = BlockID.values();

	private BlockID(int i, int data, String value) {
		this.id = i;
		this.data = data;
		this.val = value;
	}

	private BlockID(int i, String value) {
		this(i, 0, value);
	}
	private void putProperty(String key, String val) {
		this.properties.put(key, val);
	}
	
	public static BlockID query(int id) {
		return query(id, 0);
	}

	public static BlockID query(int id, int data) {
		for (BlockID bid : VALUES) {
			int cleardata = data;
			if (isLeaves(bid))
				cleardata = data % 4;
			
			if (bid.id == id && bid.data == cleardata) {
				return bid;
			}
		}
		return null;
	}

	public int getId() {
		return this.id;
	}
	
	public String getModernId() {
		return this.val;
	}
	
	public int getData() {
		return this.data;
	}
	
	public Map<String, String> getBasicProperties() {
		return this.properties;
	}

	public static BlockID fromModernId(String modernId) {
		for (BlockID bid : VALUES) {
			if (bid.val.equals(modernId)) return bid;
		}
		return null;
	}

	static {
		// absolutes
		OAK_LOG.putProperty("axis", "y");
		SPRUCE_LOG.putProperty("axis", "y");
		BIRCH_LOG.putProperty("axis", "y");
		JUNGLE_LOG.putProperty("axis", "y");

		MOB_HEADS.putProperty("powered", "false");
		MOB_HEADS.putProperty("rotation", "0");
		MOB_HEADS.putProperty("facing", "north");

		COBBLESTONE_WALL.putProperty("up", "true");
		MOSSY_COBBLESTONE_WALL.putProperty("up", "true");


		DOUBLE_STEP_STONE.putProperty("type", "double");
		DOUBLE_STEP_SANDSTONE.putProperty("type", "double");
		DOUBLE_STEP_OAK.putProperty("type", "double");
		DOUBLE_STEP_COBBLESTONE.putProperty("type", "double");
		DOUBLE_STEP_SPECIAL_STONE.putProperty("type", "double");
		DOUBLE_STEP_SPECIAL_SANDSTONE.putProperty("type", "double");
		DOUBLE_STEP_SPECIAL_OAK.putProperty("type", "double");
		DOUBLE_STEP_SPECIAL_COBBLESTONE.putProperty("type", "double");

		STEP_STONE.putProperty("type", "bottom");
		STEP_SANDSTONE.putProperty("type", "bottom");
		STEP_OAK.putProperty("type", "bottom");
		STEP_COBBLESTONE.putProperty("type", "bottom");
		STEP_STONE_BRICK.putProperty("type", "bottom");
		STEP_NETHER_BRICK.putProperty("type", "bottom");
		STEP_BRICK.putProperty("type", "bottom");
		STEP_QUARTZ.putProperty("type", "bottom");

		STEP_STONE_TOP.putProperty("type", "top");
		STEP_SANDSTONE_TOP.putProperty("type", "top");
		STEP_OAK_TOP.putProperty("type", "top");
		STEP_COBBLESTONE_TOP.putProperty("type", "top");
		STEP_STONE_BRICK_TOP.putProperty("type", "top");
		STEP_NETHER_BRICK_TOP.putProperty("type", "top");
		STEP_BRICK_TOP.putProperty("type", "top");
		STEP_QUARTZ_TOP.putProperty("type", "top");
		
		FURNACE.putProperty("lit", "false");
		BURNING_FURNACE.putProperty("lit", "true");
		GLOWING_REDSTONE_ORE.putProperty("lit", "true");
		REDSTONE_TORCH_OFF_GROUND.putProperty("lit", "false");
		REDSTONE_TORCH_OFF_WALL.putProperty("lit", "false");
		REDSTONE_TORCH_ON_GROUND.putProperty("lit", "true");
		REDSTONE_TORCH_ON_WALL.putProperty("lit", "true");
		REDSTONE_LAMP_INACTIVE.putProperty("lit", "false");
		REDSTONE_LAMP_ACTIVE.putProperty("lit", "true");
		CAKE_BLOCK.putProperty("lit", "false");
		
		DIODE_ON.putProperty("powered", "true");
		DIODE_OFF.putProperty("powered", "false");
		DIODE_ON.putProperty("locked", "false");
		DIODE_OFF.putProperty("locked", "false");

		STONE_BUTTON.putProperty("face", "wall");
		WOODEN_BUTTON.putProperty("face", "wall");
		
//		COBBLESTONE_STAIRS.putProperty("shape", "straight");
//		COBBLESTONE_STAIRS.putProperty("half", "bottom");
//		WOOD_STAIRS.putProperty("shape", "straight");
//		WOOD_STAIRS.putProperty("half", "bottom");
		
		TRAP_DOOR.putProperty("half", "bottom");
		PISTON_EXTENSION.putProperty("short", "false");

		WOODEN_STEP.putProperty("type", "bottom");
		SPRUCE_WOODEN_STEP.putProperty("type", "bottom");
		BIRCH_WOODEN_STEP.putProperty("type", "bottom");
		JUNGLE_WOODEN_STEP.putProperty("type", "bottom");
		ACACIA_WOODEN_STEP.putProperty("type", "bottom");
		DARK_OAK_WOODEN_STEP.putProperty("type", "bottom");

		WOODEN_STEP_TOP.putProperty("type", "top");
		SPRUCE_WOODEN_STEP_TOP.putProperty("type", "top");
		BIRCH_WOODEN_STEP_TOP.putProperty("type", "top");
		JUNGLE_WOODEN_STEP_TOP.putProperty("type", "top");
		ACACIA_WOODEN_STEP_TOP.putProperty("type", "top");
		DARK_OAK_WOODEN_STEP_TOP.putProperty("type", "top");

		TRIPWIRE_HOOK.putProperty("attached", "false");
		TRIPWIRE_HOOK.putProperty("facing", "north");
		TRIPWIRE_HOOK.putProperty("powered", "false");

		TRIPWIRE.putProperty("attached", "false");
		TRIPWIRE.putProperty("disarmed", "false");
		TRIPWIRE.putProperty("east", "false");
		TRIPWIRE.putProperty("north", "false");
		TRIPWIRE.putProperty("powered", "false");
		TRIPWIRE.putProperty("south", "false");
		TRIPWIRE.putProperty("west", "false");

		COMMAND_BLOCK.putProperty("facing", "north");
		COMMAND_BLOCK.putProperty("conditional", "false");

		// TODO temporary lazy hack
//		REDSTONE_WIRE.putProperty("east", "side");
//		REDSTONE_WIRE.putProperty("west", "side");
//		REDSTONE_WIRE.putProperty("south", "side");
//		REDSTONE_WIRE.putProperty("north", "side");
	}
	
	public static boolean isFlammable(int i) {
		return isFlammable(query(i));
	}
	
	public static boolean isOpaque(int i) {
		return isOpaque(query(i));
	}
	
	public static boolean isFlammable(BlockID bid) {
		return (bid == WOOD || bid == SPRUCE_WOOD || bid == BIRCH_WOOD || bid == JUNGLE_WOOD || bid == FENCE || bid == WOOD_STAIRS || bid == BOOKSHELF || bid == TNT ||
				isLog(bid) || isLeaves(bid) || isWool(bid) || isTallgrass(bid));
	}
	
	public static boolean isOpaque(BlockID bid) {
		return (bid == AIR || bid == YELLOW_FLOWER || bid == RED_ROSE || bid == TORCH_WALL || bid == TORCH_GROUND ||
				bid == REDSTONE_TORCH_ON_WALL || bid == REDSTONE_TORCH_ON_GROUND || bid == REDSTONE_TORCH_OFF_WALL ||
				bid == REDSTONE_TORCH_OFF_GROUND || bid == LEVER || bid == LADDER || bid == VINES || bid == MOB_SPAWNER || bid == PORTAL ||
				bid == SUGAR_CANE_BLOCK || bid == SIGN_POST || bid == WALL_SIGN || bid == SOIL || bid == CROPS ||
				bid == CARROTS || bid == POTATOES ||
				bid == SNOW || bid == WEB || bid == REDSTONE_WIRE || bid == STONE_PLATE || bid == WOOD_PLATE ||
				bid == FIRE || bid == FENCE || bid == WOODEN_DOOR || bid == IRON_DOOR_BLOCK || bid == CACTUS ||
				bid == CAKE_BLOCK || bid == STONE_BUTTON || bid == WOODEN_BUTTON || bid == BED || bid == TRAP_DOOR || bid == GLASS || bid == GLASS_PANE || bid == IRON_BARS || bid == LILY_PAD ||
				isStair(bid) || isRail(bid) || isLeaves(bid) || isFluid(bid) || isPistonVariant(bid)) ? false : true;
	}
	
	protected static boolean isWool(BlockID bid) {
		return 	bid == WOOL || bid == ORANGE_WOOL || bid == MAGENTA_WOOL || bid == LIGHTBLUE_WOOL || bid == YELLOW_WOOL ||
				bid == LIME_WOOL || bid == PINK_WOOL || bid == GRAY_WOOL || bid == LIGHTGRAY_WOOL || bid == CYAN_WOOL ||
				bid == PURPLE_WOOL || bid == BLUE_WOOL || bid == BROWN_WOOL || bid == GREEN_WOOL || bid == RED_WOOL || 
				bid == BLACK_WOOL;
	}
	
	protected static boolean isTallgrass(BlockID bid) {
		return bid == LONG_GRASS || bid == DEAD_BUSH_ON_GRASS || bid == FERN;
	}
	
	protected static boolean isLeaves(BlockID bid) {
		return bid == OAK_LEAVES || bid == SPRUCE_LEAVES || bid == BIRCH_LEAVES || bid == SPECIAL_LEAVES;
	}
	
	protected static boolean isLog(BlockID bid) {
		return bid == OAK_LOG || bid == SPRUCE_LOG || bid == BIRCH_LOG || bid == JUNGLE_LOG;
	}
	
	protected static boolean isFluid(BlockID bid) {
		return bid == WATER || bid == LAVA || bid == STATIONARY_WATER || bid == STATIONARY_LAVA;
	}
	
	protected static boolean isPumpkin(BlockID bid) {
		return bid == PUMPKIN || bid == JACK_O_LANTERN;
	}
	
	protected static boolean isStair(BlockID bid) {
		return bid == COBBLESTONE_STAIRS || bid == WOOD_STAIRS || bid == NETHER_BRICK_STAIRS || bid == BRICK_STAIRS || bid == STONE_BRICK_STAIRS
				|| bid == SANDSTONE_STAIRS || bid == BIRCH_STAIRS || bid == SPRUCE_STAIRS || bid == JUNGLE_STAIRS;
	}
	
	protected static boolean isCobbleContainerBlock(BlockID bid) {
		return bid == DISPENSER || bid == FURNACE || bid == BURNING_FURNACE;
	}
	
	protected static boolean isRail(BlockID bid) {
		return bid == RAILS || bid == GOLDEN_RAIL || bid == DETECTOR_RAIL;
	}
	
	protected static boolean isPressurePlate(BlockID bid) {
		return bid == WOOD_PLATE || bid == STONE_PLATE;
	}
	
	protected static boolean isRepeater(BlockID bid) {
		return bid == DIODE_ON || bid == DIODE_OFF;
	}
	
	protected static boolean isDoor(BlockID bid) {
		return bid == WOODEN_DOOR || bid == IRON_DOOR_BLOCK;
	}
	
	protected static boolean isWallTorch(BlockID bid) {
		return bid == TORCH_WALL || bid == REDSTONE_TORCH_ON_WALL || bid == REDSTONE_TORCH_OFF_WALL;
	}
	
	protected static boolean isPistonVariant(BlockID bid) {
		return bid == PISTON_STICKY || bid == PISTON || bid == PISTON_EXTENSION || bid == PISTON_MOVING;
	}
	
	public static Map<String, String> metadataToProperties(BlockID bid, int metadata) {
		// property nelper without neighboring ids
		HashMap<String, String> properties = new HashMap<String, String>();
		properties.putAll(bid.getBasicProperties());

//		if (bid.id == 126) {
//			Logger.global.logInfo("BLOCK ID 126: " + metadata);
//		}
		
		if (bid == BlockID.BED) {

			if (metadata < 8)
				properties.put("part", "foot");
			else
				properties.put("part", "head");

			metadata %= 4;

			if (metadata == 0)
				properties.put("facing", "south");
			else if (metadata == 1)
				properties.put("facing", "west");
			else if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "east");
		} else if (bid == BlockID.TRIPWIRE_HOOK) {
			properties.put("attached", ((metadata & 0x4) != 0) ? "true" : "false");
			properties.put("powered",  ((metadata & 0x8) != 0) ? "true" : "false");
			switch (metadata & 0x3) {
				case 0: properties.put("facing", "south"); break;
				case 1: properties.put("facing", "west"); break;
				case 2: properties.put("facing", "north"); break;
				case 3: properties.put("facing", "east"); break;
			}
		} else if (isFluid(bid)) {
			
			metadata %= 15;
			
			properties.put("level", "" + metadata);

		} else if (isStair(bid)) {
			int facing = metadata & 0x3;
			int halfBit = (metadata & 0x4);

			if (facing == 0) properties.put("facing", "east");
			else if (facing == 1) properties.put("facing", "west");
			else if (facing == 2) properties.put("facing", "south");
			else if (facing == 3) properties.put("facing", "north");

			if (halfBit != 0) properties.put("half", "top");
			else properties.put("half", "bottom");

			properties.put("shape", "straight");
		} else if (isPumpkin(bid)) {
			
			if (metadata == 0)
				properties.put("facing", "south");
			else if (metadata == 1)
				properties.put("facing", "west");
			else if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "east");
			
		} else if (isCobbleContainerBlock(bid)) {
			
			if (metadata == 4)
				properties.put("facing", "west");
			else if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 5)
				properties.put("facing", "east");
			else if (metadata == 3)
				properties.put("facing", "south");
			
		} else if (isRail(bid)) {
			
			if (bid != BlockID.RAILS) {
				if (metadata < 8)
					properties.put("powered", "false");
				else
					properties.put("powered", "true");
				
				metadata %= 8;
			}
			
			if (metadata == 0)
				properties.put("shape", "north_south");
			else if (metadata == 1)
				properties.put("shape", "east_west");
			else if (metadata == 2)
				properties.put("shape", "ascending_east");
			else if (metadata == 3)
				properties.put("shape", "ascending_west");
			else if (metadata == 4)
				properties.put("shape", "ascending_north");
			else if (metadata == 5)
				properties.put("shape", "ascending_south");
			else if (metadata == 6)
				properties.put("shape", "south_east");
			else if (metadata == 7)
				properties.put("shape", "south_west");
			else if (metadata == 8)
				properties.put("shape", "north_west");
			else if (metadata == 9)
				properties.put("shape", "north_east");
			
		} else if (isPistonVariant(bid)) {

			if (bid != BlockID.PISTON_EXTENSION) {
				properties.put("extended", metadata < 8 ? "false" : "true");
			}
			properties.put("type", metadata < 8 ? "normal" : "sticky");

			metadata %= 8;
			
			if (metadata == 0)
				properties.put("facing", "down");
			else if (metadata == 1)
				properties.put("facing", "up");
			else if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "west");
			else if (metadata == 5)
				properties.put("facing", "east");
			
		} else if (isPressurePlate(bid)) {
			
			if (metadata == 1)
				properties.put("powered", "true");
			else
				properties.put("powered", "false");
			
		} else if (bid == BlockID.SNOW) {
			
			metadata %= 8;
			
			// b1.7.3 counts from 0, modern versions count from 1
			properties.put("layers", "" + (metadata + 1));
			
		} else if (bid == BlockID.STONE_BUTTON || bid == BlockID.WOODEN_BUTTON) {
			
			if (metadata < 8)
				properties.put("powered", "false");
			else
				properties.put("powered", "true");
			
			metadata %= 8;
			
			if (metadata == 1)
				properties.put("facing", "east");
			else if (metadata == 2)
				properties.put("facing", "west");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "north");
		} else if (bid == BlockID.LEVER) {
			
			if (metadata < 8)
				properties.put("powered", "false");
			else
				properties.put("powered", "true");
			
			metadata %= 8;
			
			if (metadata == 1)
				properties.put("facing", "east");
			else if (metadata == 2)
				properties.put("facing", "west");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "north");
			else if (metadata == 5)
				properties.put("facing", "north");
			else if (metadata == 6)
				properties.put("facing", "west");
			
			if (metadata >= 5)
				properties.put("face", "floor");
			else
				properties.put("face", "wall");
			
		} else if (bid == BlockID.SOIL) {
			
			if (metadata == 1)
				properties.put("moisture", "7");
			else
				properties.put("moisture", "0");
			
		} else if (bid == BlockID.CROPS || bid == BlockID.POTATOES || bid == BlockID.CARROTS) {
			
			properties.put("age", "" + metadata);
			
		} else if (bid == BlockID.CAKE_BLOCK) {
			
			properties.put("bites", "" + metadata);
			
		} else if (isWallTorch(bid)) {
			
			if (metadata == 1)
				properties.put("facing", "east");
			else if (metadata == 2)
				properties.put("facing", "west");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "north");
			
		} else if (bid == BlockID.FIRE) { // TODO check
			
			properties.put("age", "" + metadata);
			
		} else if (bid == BlockID.LADDER) {
			
			if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "west");
			else if (metadata == 5)
				properties.put("facing", "east");
			
		} else if (bid == BlockID.VINES) {
			if (metadata == 1)
				properties.put("south", "true");
			if (metadata == 2)
				properties.put("west", "true");
			if (metadata == 4)
				properties.put("north", "true");
			if (metadata == 8)
				properties.put("east", "true");
			if (metadata == 16)
				properties.put("up", "true");

			if (!properties.containsKey("south")) properties.put("south", "false");
			if (!properties.containsKey("west")) properties.put("west", "false");
			if (!properties.containsKey("north")) properties.put("north", "false");
			if (!properties.containsKey("east")) properties.put("east", "false");
			if (!properties.containsKey("up")) properties.put("up", "false");
		} else if (bid == BlockID.TRAP_DOOR) {
			
			if (metadata < 4)
				properties.put("open", "false");
			else
				properties.put("open", "true");
			
			metadata %= 4;
			
			if (metadata == 0)
				properties.put("facing", "north");
			else if (metadata == 1)
				properties.put("facing", "south");
			else if (metadata == 2)
				properties.put("facing", "west");
			else if (metadata == 3)
				properties.put("facing", "east");
			
		} else if (bid == BlockID.REDSTONE_WIRE) {
			
			properties.put("power", "" + metadata);
			
		} else if (isRepeater(bid)) {
			
			int delay = (metadata / 4) + 1;
			
			properties.put("delay", "" + delay);
			
			metadata %= 4;
			
			if (metadata == 0)
				properties.put("facing", "south");
			else if (metadata == 1)
				properties.put("facing", "west");
			else if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "east");
			
		} else if (bid == SIGN_POST) {
			
			properties.put("rotation", "" + metadata);
			
		} else if (bid == WALL_SIGN) {
			
			if (metadata == 2)
				properties.put("facing", "north");
			else if (metadata == 3)
				properties.put("facing", "south");
			else if (metadata == 4)
				properties.put("facing", "west");
			else if (metadata == 5)
				properties.put("facing", "east");
			
		}
		return properties;
	}
}
