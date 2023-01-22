package lotrfgen;

import java.lang.reflect.*;
import java.util.*;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.ReflectionHelper.*;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.LOTRBiomeDecorator;
import lotr.common.world.biome.variant.LOTRBiomeVariantList;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
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
	public static float getDamageAmount(Item item) {
		Field privateField = null;
		float f = 0.0f;
		try {
			privateField = getPotentiallyObfuscatedPrivateValue(ItemSword.class, "field_150934_a");
			privateField.setAccessible(true);
			f = (float) privateField.get(item);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return f;
	}

	public static Block getOreBlock(WorldGenMinable ore) {
		Field privateField = null;
		Block b = null;
		try {
			privateField = getPotentiallyObfuscatedPrivateValue(WorldGenMinable.class, "field_150519_a");
			privateField.setAccessible(true);
			b = (Block) privateField.get(ore);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return b;
	}

	public static int getOreMeta(WorldGenMinable ore) {
		Field privateField = null;
		int i = 0;
		try {
			privateField = getPotentiallyObfuscatedPrivateValue(WorldGenMinable.class, "mineableBlockMeta");
			privateField.setAccessible(true);
			i = (int) privateField.get(ore);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return i;
	}

	public static ToolMaterial getToolMaterial(Item item) {
		Field privateField = null;
		ToolMaterial tm = null;
		try {
			privateField = getPotentiallyObfuscatedPrivateValue(ItemSword.class, "field_150933_b");
			privateField.setAccessible(true);
			tm = (ToolMaterial) privateField.get(item);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return tm;
	}

	public static List<FactionContainer> getFactionContainers(LOTRBiomeSpawnList list) {
		List<FactionContainer> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeSpawnList.class.getDeclaredField("factionContainers");
			privateField.setAccessible(true);
			l = (List<FactionContainer>) privateField.get(list);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static int getInitialCost(LOTRUnitTradeEntry entry) {
		int i = -1;
		try {
			Field privateField = LOTRUnitTradeEntry.class.getDeclaredField("initialCost");
			privateField.setAccessible(true);
			i = (int) privateField.get(entry);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return i;
	}

	public static int getMinMaxHeight(Object obj, String str) {
		int i = 0;
		try {
			Field privateField = obj.getClass().getDeclaredField(str);
			privateField.setAccessible(true);
			i = (int) privateField.get(obj);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return i;
	}

	public static <E, T> List<T> getObjectFieldsOfType(Class<? extends E> clazz, Class<? extends T> type) {
		ArrayList<Object> list = new ArrayList<>();
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
		return (List<T>) list;
	}

	public static float getOreChance(Object obj) {
		float f = 0.0f;
		try {
			Field privateField = obj.getClass().getDeclaredField("oreChance");
			privateField.setAccessible(true);
			f = (float) privateField.get(obj);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return f;
	}

	public static List<Object> getOreList(LOTRBiomeDecorator spawns, String field) {
		List<Object> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField(field);
			privateField.setAccessible(true);
			l = (List<Object>) privateField.get(spawns);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static List<Object> getRandomStructures(LOTRBiomeDecorator dec) {
		List<Object> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField("randomStructures");
			privateField.setAccessible(true);
			l = (List<Object>) privateField.get(dec);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static List<LOTRInvasions> getRegisteredInvasions(LOTRBiomeInvasionSpawns spawns) {
		List<LOTRInvasions> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeInvasionSpawns.class.getDeclaredField("registeredInvasions");
			privateField.setAccessible(true);
			l = (List<LOTRInvasions>) privateField.get(spawns);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static LOTRSpawnList getSpawnList(SpawnListContainer cont) {
		LOTRSpawnList l = null;
		try {
			Field privateField = SpawnListContainer.class.getDeclaredField("spawnList");
			privateField.setAccessible(true);
			l = (LOTRSpawnList) privateField.get(cont);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static List<LOTRSpawnEntry> getSpawnListList(LOTRSpawnList list) {
		List<LOTRSpawnEntry> l = new ArrayList<>();
		try {
			Field privateField = LOTRSpawnList.class.getDeclaredField("spawnList");
			privateField.setAccessible(true);
			l = (List<LOTRSpawnEntry>) privateField.get(list);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static List<SpawnListContainer> getSpawnLists(FactionContainer cont) {
		List<SpawnListContainer> l = new ArrayList<>();
		try {
			Field privateField = FactionContainer.class.getDeclaredField("spawnLists");
			privateField.setAccessible(true);
			l = (List<SpawnListContainer>) privateField.get(cont);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static boolean getSpawnsInDarkness(LOTREntityNPC entity) {
		boolean b = false;
		try {
			Field privateField = LOTREntityNPC.class.getDeclaredField("spawnsInDarkness");
			privateField.setAccessible(true);
			b = (boolean) privateField.get(entity);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return b;
	}

	public static Object getStructureGen(Object obj) {
		Object o = null;
		try {
			Field privateField = obj.getClass().getDeclaredField("structureGen");
			privateField.setAccessible(true);
			o = privateField.get(obj);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return o;
	}

	public static boolean getTargetSeeker(LOTREntityNPC entity) {
		boolean b = false;
		try {
			Field privateField = LOTREntityNPC.class.getDeclaredField("isTargetSeeker");
			privateField.setAccessible(true);
			b = (boolean) privateField.get(entity);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return b;
	}

	public static List<WeightedTreeType> getTreeTypes(LOTRBiomeDecorator spawns) {
		List<WeightedTreeType> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeDecorator.class.getDeclaredField("treeTypes");
			privateField.setAccessible(true);
			l = (List<WeightedTreeType>) privateField.get(spawns);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static Object getVariant(Object obj) {
		Object o = null;
		try {
			Field privateField = obj.getClass().getDeclaredField("variant");
			privateField.setAccessible(true);
			o = privateField.get(obj);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return o;
	}

	public static List<Object> getVariantList(LOTRBiomeVariantList list) {
		List<Object> l = new ArrayList<>();
		try {
			Field privateField = LOTRBiomeVariantList.class.getDeclaredField("variantList");
			privateField.setAccessible(true);
			l = (List<Object>) privateField.get(list);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e2) {
			e2.printStackTrace();
		}
		return l;
	}

	public static WorldGenMinable getWorldGenMinable(Object obj) {
		WorldGenMinable o = null;
		try {
			Field privateField = obj.getClass().getDeclaredField("oreGen");
			privateField.setAccessible(true);
			o = (WorldGenMinable) privateField.get(obj);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e2) {
			e2.printStackTrace();
		}
		return o;
	}

	public static Entity newEntity(Class entityClass, World world) {
		try {
			Class[] param = new Class[1];
			param[0] = World.class;
			return (Entity) entityClass.getDeclaredConstructor(param).newInstance(world);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
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
}