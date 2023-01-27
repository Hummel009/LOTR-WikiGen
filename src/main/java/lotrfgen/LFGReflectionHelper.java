package lotrfgen;

import java.lang.reflect.*;
import java.util.*;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.*;
import lotr.common.*;
import lotr.common.entity.npc.*;
import lotr.common.fac.*;
import lotr.common.world.biome.LOTRBiomeDecorator;
import lotr.common.world.biome.variant.*;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.map.LOTRWaypoint.Region;
import lotr.common.world.spawning.*;
import lotr.common.world.spawning.LOTRBiomeSpawnList.*;
import lotr.common.world.structure.LOTRStructures;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class LFGReflectionHelper {
	public static LOTRFaction getAlignmentFaction(LOTRShields shield) {
		LOTRFaction alignmentFaction = null;
		try {
			Field privateField = LOTRShields.class.getDeclaredField("alignmentFaction");
			privateField.setAccessible(true);
			alignmentFaction = (LOTRFaction) privateField.get(shield);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return alignmentFaction;
	}

	public static int getBaseWeight(FactionContainer container) {
		int baseWeight = 0;
		try {
			Field privateField = FactionContainer.class.getDeclaredField("baseWeight");
			privateField.setAccessible(true);
			baseWeight = (int) privateField.get(container);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return baseWeight;
	}

	public static List<Object> getBiomeMinerals(LOTRBiomeDecorator decorator, String fieldName) {
		List<Object> biomeMinerals = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField(fieldName);
			privateField.setAccessible(true);
			biomeMinerals = (List<Object>) privateField.get(decorator);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return biomeMinerals;
	}

	public static float getDamageAmount(Item item) {
		float damageAmount = 0.0f;
		try {
			Field privateField = getPotentiallyObfuscatedPrivateValue(ItemSword.class, "field_150934_a");
			privateField.setAccessible(true);
			damageAmount = (float) privateField.get(item);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return damageAmount;
	}

	public static List<FactionContainer> getFactionContainers(LOTRBiomeSpawnList spawnlist) {
		List<FactionContainer> factionContainers = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeSpawnList.class.getDeclaredField("factionContainers");
			privateField.setAccessible(true);
			factionContainers = (List<FactionContainer>) privateField.get(spawnlist);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return factionContainers;
	}

	public static int getInitialCost(LOTRUnitTradeEntry entry) {
		int initialCost = -1;
		try {
			Field privateField = LOTRUnitTradeEntry.class.getDeclaredField("initialCost");
			privateField.setAccessible(true);
			initialCost = (int) privateField.get(entry);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return initialCost;
	}

	public static LOTRAchievement getKillAchievement(LOTREntityNPC entity) {
		LOTRAchievement killAchievement = null;
		try {
			Method method = LOTREntityNPC.class.getDeclaredMethod("getKillAchievement");
			method.setAccessible(true);
			killAchievement = (LOTRAchievement) method.invoke(entity.getClass());
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return killAchievement;
	}

	public static Block getMineableBlock(WorldGenMinable worldGenMinable) {
		Block mineableBlock = null;
		try {
			Field privateField = getPotentiallyObfuscatedPrivateValue(WorldGenMinable.class, "field_150519_a");
			privateField.setAccessible(true);
			mineableBlock = (Block) privateField.get(worldGenMinable);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return mineableBlock;
	}

	public static int getMineableBlockMeta(WorldGenMinable worldGenMinable) {
		int mineableBlockMeta = 0;
		try {
			Field privateField = getPotentiallyObfuscatedPrivateValue(WorldGenMinable.class, "mineableBlockMeta");
			privateField.setAccessible(true);
			mineableBlockMeta = (int) privateField.get(worldGenMinable);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return mineableBlockMeta;
	}

	public static int getMinMaxHeight(Object oreGenerant, String fieldName) {
		int minMaxHeight = 0;
		try {
			Field privateField = oreGenerant.getClass().getDeclaredField(fieldName);
			privateField.setAccessible(true);
			minMaxHeight = (int) privateField.get(oreGenerant);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return minMaxHeight;
	}

	public static <E, T> HashSet<T> getObjectFieldsOfType(Class<? extends E> clazz, Class<? extends T> type) {
		HashSet<Object> list = new HashSet<>();
		for (Field field : clazz.getDeclaredFields()) {
			if (field == null) {
				continue;
			}
			Object fieldObj = null;
			if (field.getType().equals(type)) {
				try {
					fieldObj = field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				list.add(fieldObj);
			}
		}
		return (HashSet<T>) list;
	}

	public static float getOreChance(Object oreGenerant) {
		float oreChance = 0.0f;
		try {
			Field privateField = oreGenerant.getClass().getDeclaredField("oreChance");
			privateField.setAccessible(true);
			oreChance = (float) privateField.get(oreGenerant);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return oreChance;
	}

	public static WorldGenMinable getOreGen(Object oreGenerant) {
		WorldGenMinable oreGen = null;
		try {
			Field privateField = oreGenerant.getClass().getDeclaredField("oreGen");
			privateField.setAccessible(true);
			oreGen = (WorldGenMinable) privateField.get(oreGenerant);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return oreGen;
	}

	public static <T, E> T getPotentiallyObfuscatedPrivateValue(Class<? super E> classToAccess, String fieldName) {
		try {
			return ReflectionHelper.getPrivateValue(classToAccess, null, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldName));
		} catch (UnableToFindFieldException | UnableToAccessFieldException | NullPointerException e) {
			try {
				return (T) classToAccess.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public static List<Object> getRandomStructures(LOTRBiomeDecorator decorator) {
		List<Object> randomStructures = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField("randomStructures");
			privateField.setAccessible(true);
			randomStructures = (List<Object>) privateField.get(decorator);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return randomStructures;
	}

	public static List<LOTRFactionRank> getRanksSortedDescending(LOTRFaction fac) {
		List<LOTRFactionRank> ranksSortedDescending = new ArrayList<>();
		try {
			Field privateField = LOTRFaction.class.getDeclaredField("ranksSortedDescending");
			privateField.setAccessible(true);
			ranksSortedDescending = (List<LOTRFactionRank>) privateField.get(fac);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return ranksSortedDescending;
	}

	public static Region getRegion(LOTRWaypoint wp) {
		Region region = null;
		try {
			Field privateField = LOTRWaypoint.class.getDeclaredField("region");
			privateField.setAccessible(true);
			region = (Region) privateField.get(wp);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return region;
	}

	public static List<LOTRInvasions> getRegisteredInvasions(LOTRBiomeInvasionSpawns invasionSpawns) {
		List<LOTRInvasions> registeredInvasions = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeInvasionSpawns.class.getDeclaredField("registeredInvasions");
			privateField.setAccessible(true);
			registeredInvasions = (List<LOTRInvasions>) privateField.get(invasionSpawns);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return registeredInvasions;
	}

	public static LOTRSpawnList getSpawnList(SpawnListContainer container) {
		LOTRSpawnList spawnList = null;
		try {
			Field privateField = SpawnListContainer.class.getDeclaredField("spawnList");
			privateField.setAccessible(true);
			spawnList = (LOTRSpawnList) privateField.get(container);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return spawnList;
	}

	public static List<LOTRSpawnEntry> getSpawnListEntries(LOTRSpawnList spawnList) {
		List<LOTRSpawnEntry> spawnListEntries = new ArrayList<>();
		try {
			Field privateField = LOTRSpawnList.class.getDeclaredField("spawnList");
			privateField.setAccessible(true);
			spawnListEntries = (List<LOTRSpawnEntry>) privateField.get(spawnList);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return spawnListEntries;
	}

	public static List<SpawnListContainer> getSpawnLists(FactionContainer container) {
		List<SpawnListContainer> spawnLists = new ArrayList<>();
		try {
			Field privateField = FactionContainer.class.getDeclaredField("spawnLists");
			privateField.setAccessible(true);
			spawnLists = (List<SpawnListContainer>) privateField.get(container);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return spawnLists;
	}

	public static boolean getSpawnsInDarkness(LOTREntityNPC entity) {
		boolean spawnsInDarkness = false;
		try {
			Field privateField = LOTREntityNPC.class.getDeclaredField("spawnsInDarkness");
			privateField.setAccessible(true);
			spawnsInDarkness = (boolean) privateField.get(entity);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return spawnsInDarkness;
	}

	public static Object getStructureGen(Object randomStructure) {
		Object structureGen = null;
		try {
			Field privateField = randomStructure.getClass().getDeclaredField("structureGen");
			privateField.setAccessible(true);
			structureGen = privateField.get(randomStructure);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return structureGen;
	}

	public static ToolMaterial getToolMaterial(Item item) {
		ToolMaterial toolMaterial = null;
		try {
			Field privateField = getPotentiallyObfuscatedPrivateValue(ItemSword.class, "field_150933_b");
			privateField.setAccessible(true);
			toolMaterial = (ToolMaterial) privateField.get(item);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return toolMaterial;
	}

	public static List<WeightedTreeType> getTreeTypes(LOTRBiomeDecorator decorator) {
		List<WeightedTreeType> treeTypes = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField("treeTypes");
			privateField.setAccessible(true);
			treeTypes = (List<WeightedTreeType>) privateField.get(decorator);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return treeTypes;
	}

	public static LOTRBiomeVariant getVariant(Object variantBucket) {
		LOTRBiomeVariant variant = null;
		try {
			Field privateField = variantBucket.getClass().getDeclaredField("variant");
			privateField.setAccessible(true);
			variant = (LOTRBiomeVariant) privateField.get(variantBucket);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return variant;
	}

	public static List<Object> getVariantList(LOTRBiomeVariantList biomeVariantList) {
		List<Object> variantList = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeVariantList.class.getDeclaredField("variantList");
			privateField.setAccessible(true);
			variantList = (List<Object>) privateField.get(biomeVariantList);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return variantList;
	}

	public static Entity newEntity(Class entityClass, World world) {
		Entity entity = null;
		try {
			Class[] param = new Class[1];
			param[0] = World.class;
			entity = (Entity) entityClass.getDeclaredConstructor(param).newInstance(world);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public static void registerStructure(int a, Class<? extends WorldGenerator> b, String c, int d, int e) {
		try {
			Method method = LOTRStructures.class.getDeclaredMethod("registerStructure", int.class, Class.class, String.class, int.class, int.class);
			method.setAccessible(true);
			method.invoke(LOTRStructures.class, a, b, c, d, e);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
	}
}