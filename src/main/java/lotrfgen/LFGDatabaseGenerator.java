package lotrfgen;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import lotr.common.*;
import lotr.common.block.*;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRUnitTradeEntry.PledgeType;
import lotr.common.fac.LOTRFaction;
import lotr.common.world.biome.*;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.spawning.*;
import lotr.common.world.spawning.LOTRBiomeSpawnList.*;
import lotr.common.world.structure2.LOTRWorldGenStructureBase2;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class LFGDatabaseGenerator extends LOTRWorldGenStructureBase2 {
	public static String display = "null";
	public static HashMap<Class, String> classToEntityName = new HashMap<>();
	public static HashMap<Class, Entity> entityClassToObject = new HashMap<>();
	public static HashMap<Class, String> classToStructureName = new HashMap<>();
	public static int LEN;

	static {
		LEN = "item.lotr:".length();
	}

	public LFGDatabaseGenerator(boolean flag) {
		super(flag);
	}

	@Override

	public boolean generateWithSetRotation(World world, Random random, int y, int j, int k, int rotation) {
		String riderLoc = StatCollector.translateToLocal("db.riderLoc.name");
		String categoryTemplates = StatCollector.translateToLocal("db.categoryTemplates.name");
		String biomeNoNPC = StatCollector.translateToLocal("db.biomeNoNPC.name");
		String biomeContainerLoc = StatCollector.translateToLocal("db.biomeContainerLoc.name");
		String biomeContainerMeaning = StatCollector.translateToLocal("db.biomeContainerMeaning.name");
		String biomeNoVariants = StatCollector.translateToLocal("db.biomeNoVariants.name");
		String biomeNoInvasions = StatCollector.translateToLocal("db.biomeNoInvasions.name");
		String biomeNoTrees = StatCollector.translateToLocal("db.biomeNoTrees.name");
		String biomeNoAnimals = StatCollector.translateToLocal("db.biomeNoAnimals.name");
		String biomeNoStructures = StatCollector.translateToLocal("db.biomeNoStructures.name");
		String biomeLoc = StatCollector.translateToLocal("db.biomeLoc.name");
		String factionNoEnemies = StatCollector.translateToLocal("db.factionNoEnemies.name");
		String factionNoFriends = StatCollector.translateToLocal("db.factionNoFriends.name");
		String factionNoCharacters = StatCollector.translateToLocal("db.factionNoCharacters.name");
		String factionLoc = StatCollector.translateToLocal("db.factionLoc.name");
		String factionNotViolent = StatCollector.translateToLocal("db.factionNotViolent.name");
		String factionIsViolent = StatCollector.translateToLocal("db.factionIsViolent.name");
		String factionNoStructures = StatCollector.translateToLocal("db.factionNoStructures.name");
		String noPledge = StatCollector.translateToLocal("db.noPledge.name");
		String rep = StatCollector.translateToLocal("db.rep.name");
		String yesPledge = StatCollector.translateToLocal("db.yesPledge.name");
		LFGConfig cfg = new LFGConfig(world);
		cfg.authorizeEntityInfo();
		cfg.authorizeStructureInfo();
		try {
			List<Item> itmlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRMod.class, Item.class);
			List<LOTRUnitTradeEntries> trdntrlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class);
			List<LOTRAchievement> achlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class);
			List<LOTRBiome> bmlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class);
			List<LOTRFaction> fclist = new ArrayList<>(EnumSet.allOf(LOTRFaction.class));

			if ("tables".equals(display)) {
				PrintWriter achievements = new PrintWriter("achievements.txt", "UTF-8");
				for (LOTRAchievement ach : achlist) {
					if (ach != null) {
						achievements.println("| " + StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".title") + " || " + StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".desc"));
						achievements.println("|-");
					}
				}
				achievements.close();

				PrintWriter shields = new PrintWriter("shields.txt", "UTF-8");
				for (LOTRShields shield : LOTRShields.values()) {
					shields.println("| " + shield.getShieldName() + " || " + shield.getShieldDesc() + " || [[File:Shield " + shield.name().toLowerCase() + ".png]]");
					shields.println("|-");
				}
				shields.close();

				PrintWriter units = new PrintWriter("units.txt", "UTF-8");
				for (LOTRUnitTradeEntries entries : trdntrlist) {
					if (entries != null) {
						for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
							if (entry.mountClass == null) {
								units.println("| [[" + getEntityName(entry.entityClass) + "]] || {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} || +" + entry.alignmentRequired + " || " + (entry.getPledgeType() == PledgeType.NONE));
							} else {
								units.println("| [[" + getEntityName(entry.entityClass) + "]] (" + riderLoc + ") || {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} || +" + entry.alignmentRequired + " || " + (entry.getPledgeType() == PledgeType.NONE));
							}
							units.println("|-");
						}
					}
				}
				units.close();

				PrintWriter waypoints = new PrintWriter("waypoints.txt", "UTF-8");
				for (LOTRWaypoint wp : LOTRWaypoint.values()) {
					waypoints.println("| " + wp.getDisplayName() + " || " + wp.getLoreText(usingPlayer));
					waypoints.println("|-");
				}
				waypoints.close();

				PrintWriter armor = new PrintWriter("armor.txt", "UTF-8");
				for (Item item : itmlist) {
					if (item instanceof ItemArmor) {
						String genInfo = StatCollector.translateToLocal(item.getUnlocalizedName() + ".name") + " || [[File:" + item.getUnlocalizedName().substring(LEN) + ".png|32px|link=]] || ";
						if (((ItemArmor) item).getArmorMaterial().customCraftingMaterial != null) {
							armor.println("| " + genInfo + item.getMaxDamage() + " || " + ((ItemArmor) item).damageReduceAmount + " || " + StatCollector.translateToLocal(((ItemArmor) item).getArmorMaterial().customCraftingMaterial.getUnlocalizedName() + ".name"));
						} else {
							armor.println("| " + genInfo + item.getMaxDamage() + " || " + ((ItemArmor) item).damageReduceAmount + " || - ");
						}
						armor.println("|-");
					}
				}
				armor.close();

				PrintWriter weapon = new PrintWriter("weapon.txt", "UTF-8");
				for (Item item : itmlist) {
					if (item instanceof ItemSword) {
						String genInfo = StatCollector.translateToLocal(item.getUnlocalizedName() + ".name") + " || [[File:" + item.getUnlocalizedName().substring(LEN) + ".png|32px|link=]] || ";
						float damage = LFGReflectionHelper.getDamageAmount(item);
						ToolMaterial material = LFGReflectionHelper.getToolMaterial(item);
						if (material.getRepairItemStack() != null) {
							weapon.println("| " + genInfo + item.getMaxDamage() + " || " + damage + " || " + StatCollector.translateToLocal(material.getRepairItemStack().getUnlocalizedName() + ".name"));
						} else {
							weapon.println("| " + genInfo + item.getMaxDamage() + " || " + damage + " || - ");
						}
						weapon.println("|-");
					}
				}
				weapon.close();

				PrintWriter food = new PrintWriter("food.txt", "UTF-8");
				for (Item item : itmlist) {
					if (item instanceof ItemFood && item != null) {
						String genInfo = StatCollector.translateToLocal(item.getUnlocalizedName() + ".name") + " || [[File:" + item.getUnlocalizedName().substring(LEN) + ".png|32px|link=]] ||";
						int healAmount = ((ItemFood) item).func_150905_g(null);
						float saturationModifier = ((ItemFood) item).func_150906_h(null);
						food.println("| " + genInfo + "{{Bar|bread|" + new DecimalFormat("#.##").format(saturationModifier * healAmount * 2) + "}} || {{Bar|food|" + healAmount + "}} || " + item.getItemStackLimit());
						food.println("|-");
					}
				}
				food.close();
			} else if ("xml".equals(display)) {
				PrintWriter xml = new PrintWriter("xml.txt", "UTF-8");

				xml.println("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"ru\">");
				for (Class mob : entityClassToObject.keySet()) {
					String s1 = "<page><title>";
					String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u041C\u043E\u0431}}</text></revision></page>";
					xml.print(s1 + getEntityName(mob) + s2);
					xml.println();
				}
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						boolean two = false;
						String s1 = "<page><title>";
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0411\u0438\u043E\u043C}}</text></revision></page>";
						for (LOTRFaction fac : fclist) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								two = true;
								break;
							}
						}
						if (two) {
							xml.print(s1 + getBiomeName(biome) + " (" + biomeLoc + ")" + s2);
						} else {
							xml.print(s1 + getBiomeName(biome) + s2);
						}
						xml.println();
					}
				}
				for (LOTRFaction fac : fclist) {
					boolean two = false;
					String s1 = "<page><title>";
					String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0424\u0440\u0430\u043A\u0446\u0438\u044F}}</text></revision></page>";
					for (LOTRBiome biome : bmlist) {
						if (biome != null) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								two = true;
								break;
							}
						}
					}
					if (fac.factionRegion != null) {
						if (two) {
							xml.print(s1 + fac.factionName() + " (" + factionLoc + ")" + s2);
						} else {
							xml.print(s1 + fac.factionName() + s2);
						}
						xml.println();
					}
				}

				String begin = "</title><ns>10</ns><revision><text>&lt;includeonly&gt;{{#switch: {{{1}}}";
				String end = "}}&lt;/includeonly&gt;&lt;noinclude&gt;[[" + categoryTemplates + "]]&lt;/noinclude&gt;</text></revision></page>";

				/* BIOMES */

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-NPC");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						List<FactionContainer> facConts = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
						if (facConts.isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoNPC);
						} else {
							xml.println("| " + getBiomeName(biome) + " = ");
							int i = 1;
							if (facConts.size() > 1) {
								xml.println(biomeContainerMeaning);
							}
							for (FactionContainer cont : facConts) {
								if (facConts.size() > 1) {
									xml.println("");
									xml.println(biomeContainerLoc + " \u2116" + i + ":");
								}
								for (SpawnListContainer one : LFGReflectionHelper.getSpawnLists(cont)) {
									for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListList(LFGReflectionHelper.getSpawnList(one))) {
										xml.println("* [[" + getEntityName(entry.entityClass) + "]]; ");
									}
								}
								++i;
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0411\u0430\u043D\u0434\u0438\u0442\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.getBanditChance());
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041A\u043B\u0438\u043C\u0430\u0442-\u041E\u0441\u0430\u0434\u043A\u0438");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.rainfall);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041A\u043B\u0438\u043C\u0430\u0442-\u0422\u0435\u043C\u043F\u0435\u0440\u0430\u0442\u0443\u0440\u0430");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.temperature);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u0430\u0440\u0438\u0430\u043D\u0442\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						if (LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall()).isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoVariants);
						} else {
							xml.println("| " + getBiomeName(biome) + " = ");
							for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
								xml.println("* " + StatCollector.translateToLocal("lotr.variant." + ((LOTRBiomeVariant) LFGReflectionHelper.getVariant(variantBucket)).variantName + ".name") + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u0442\u043E\u0440\u0436\u0435\u043D\u0438\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						if (LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoInvasions);
						} else {
							xml.println("| " + getBiomeName(biome) + " = ");
							for (LOTRInvasions inv : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
								xml.println("* {{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + inv.invasionName() + "}};");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u044B\u0441\u043E\u0442\u0430");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.heightBaseParameter);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0414\u0435\u0440\u0435\u0432\u044C\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						if (LFGReflectionHelper.getTreeTypes(biome.decorator).isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoTrees);
						} else {
							xml.println("| " + getBiomeName(biome) + " = ");
							for (WeightedTreeType tree : LFGReflectionHelper.getTreeTypes(biome.decorator)) {
								xml.println("* " + StatCollector.translateToLocal("lotr.tree." + tree.treeType.name().toLowerCase() + ".name") + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0416\u0438\u0432\u043E\u0442\u043D\u044B\u0435");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						List entries = new ArrayList(biome.getSpawnableList(EnumCreatureType.ambient));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
						entries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
						if (!entries.isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = ");
							for (Object entry : entries) {
								if (!EntityList.classToStringMapping.containsKey(((SpawnListEntry) entry).entityClass)) {
									xml.println("* [[" + getEntityName(((SpawnListEntry) entry).entityClass) + "]];");
								} else {
									xml.println("* " + StatCollector.translateToLocal("entity." + EntityList.classToStringMapping.get(((SpawnListEntry) entry).entityClass) + ".name") + ";");
								}
							}
						} else {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoAnimals);
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0418\u0441\u043A\u043E\u043F\u0430\u0435\u043C\u044B\u0435");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = ");
						if (biome != null) {
							List<Object> sus = new ArrayList<>();
							sus.addAll(LFGReflectionHelper.getOreList(biome.decorator, "biomeSoils"));
							sus.addAll(LFGReflectionHelper.getOreList(biome.decorator, "biomeOres"));
							sus.addAll(LFGReflectionHelper.getOreList(biome.decorator, "biomeGems"));
							for (Object oreGenerant : sus) {
								WorldGenMinable gen = LFGReflectionHelper.getWorldGenMinable(oreGenerant);
								Block block = LFGReflectionHelper.getOreBlock(gen);
								int meta = LFGReflectionHelper.getOreMeta(gen);

								float oreChance = LFGReflectionHelper.getOreChance(oreGenerant);
								int minHeight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight");
								int maxHeight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight");

								if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
									xml.println("* " + StatCollector.translateToLocal(block.getUnlocalizedName() + "." + meta + ".name") + " (" + oreChance + "%; Y: " + minHeight + "-" + maxHeight + ");");
								} else {
									xml.println("* " + StatCollector.translateToLocal(block.getUnlocalizedName() + ".name") + " (" + oreChance + "%; Y: " + minHeight + "-" + maxHeight + ");");
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u041A\u043E\u043B\u0435\u0431\u0430\u043D\u0438\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.heightVariation);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u041C\u0443\u0437\u043F\u0430\u043A");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.biomeName);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0421\u0441\u044B\u043B\u043A\u0430");
				xml.println(begin);
				xml.println("| #default = [[{{{1}}}]]");
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						for (LOTRFaction fac : fclist) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								xml.println("| " + getBiomeName(biome) + " | " + getBiomeName(biome) + " (" + biomeLoc + ") = [[" + getBiomeName(biome) + " (" + biomeLoc + ")|" + getBiomeName(biome) + "]]");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0421\u0442\u0440\u0443\u043A\u0442\u0443\u0440\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						if (LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
							xml.println("| " + getBiomeName(biome) + " = " + biomeNoStructures);
						} else {
							xml.println("| " + getBiomeName(biome) + " = ");
							for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
								xml.println("* " + getStructureName(LFGReflectionHelper.getStructureGen(structure).getClass()) + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0424\u043E\u0442\u043E");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						xml.println("| " + getBiomeName(biome) + " = " + biome.biomeName + " (biome).png");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0421\u0442\u0430\u0442\u044C\u044F-\u0411\u0438\u043E\u043C");
				xml.println(begin);
				xml.println("| #default = {{{1}}}");
				for (LOTRBiome biome : bmlist) {
					if (biome != null) {
						for (LOTRFaction fac : fclist) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								xml.println("| " + getBiomeName(biome) + " (" + biomeLoc + ") = " + getBiomeName(biome));
							}
						}
					}
				}
				xml.println(end);

				/* FACTIONS */

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-NPC");
				xml.println(begin);
				for (LOTRFaction fac : fclist) {
					xml.println("| " + fac.factionName() + " =");
					for (Class mob : entityClassToObject.keySet()) {
						if (entityClassToObject.get(mob) instanceof LOTREntityNPC && ((LOTREntityNPC) entityClassToObject.get(mob)).getFaction() == fac) {
							xml.println("* [[" + getEntityName(mob) + "]];");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0412\u0440\u0430\u0433\u0438");
				xml.println(begin);
				for (LOTRFaction fac : fclist) {
					boolean empty = true;
					for (LOTRFaction fac2 : fclist) {
						if (fac2.isBadRelation(fac) && fac2 != fac && fac != LOTRFaction.HOSTILE && fac2 != LOTRFaction.HOSTILE) {
							empty = false;
							break;
						}
					}
					if (!empty) {
						xml.println("| " + fac.factionName() + " =");
						int i = 0;
						for (LOTRFaction fac2 : fclist) {
							if (fac2.isBadRelation(fac) && fac2 != fac && fac != LOTRFaction.HOSTILE && fac2 != LOTRFaction.HOSTILE) {
								if (i == 0) {
									xml.print("{{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + fac2.factionName() + "}}");
									i++;
								} else {
									xml.print(" \u2022 {{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + fac2.factionName() + "}}");
								}
							}
						}
						xml.println();
					} else {
						xml.println("| " + fac.factionName() + " = " + factionNoEnemies);
					}

				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0414\u0440\u0443\u0437\u044C\u044F");
				xml.println(begin);
				for (LOTRFaction fac : fclist) {
					boolean empty = true;
					for (LOTRFaction fac2 : fclist) {
						if (fac2.isGoodRelation(fac) && fac2 != fac) {
							empty = false;
							break;
						}
					}
					if (!empty) {
						xml.println("| " + fac.factionName() + " =");
						int i = 0;
						for (LOTRFaction fac2 : fclist) {
							if (fac2.isGoodRelation(fac) && fac2 != fac) {
								if (i == 0) {
									xml.print("{{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + fac2.factionName() + "}}");
									i++;
								} else {
									xml.print(" \u2022 {{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + fac2.factionName() + "}}");
								}
							}
						}
						xml.println();
					} else {
						xml.println("| " + fac.factionName() + " = " + factionNoFriends);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0416\u0435\u0441\u0442\u043E\u043A\u043E\u0441\u0442\u044C");
				xml.println(begin);
				xml.println("| #default = " + factionNotViolent);
				for (LOTRFaction fac : fclist) {
					if (fac.approvesWarCrimes) {
						xml.println("| " + fac.factionName() + " = " + factionIsViolent);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u041A\u043E\u0434");
				xml.println(begin);
				for (LOTRFaction fac : fclist) {
					xml.println("| " + fac.factionName() + " = " + fac.codeName());
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0420\u0435\u0433\u0438\u043E\u043D");
				xml.println(begin);
				for (LOTRFaction fac : fclist) {
					if (fac.factionRegion != null) {
						xml.println("| " + fac.factionName() + " = " + fac.factionRegion.getRegionName());
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430");
				xml.println(begin);
				xml.println("| #default = [[{{{1}}}]]");
				for (LOTRFaction fac : fclist) {
					for (LOTRBiome biome : bmlist) {
						if (biome != null) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								xml.println("| " + fac.factionName() + " | " + fac.factionName() + " (" + factionLoc + ") = [[" + fac.factionName() + " (" + factionLoc + ")|" + fac.factionName() + "]]");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0421\u0442\u0430\u0442\u044C\u044F-\u0424\u0440\u0430\u043A\u0446\u0438\u044F");
				xml.println(begin);
				xml.println("| #default = {{{1}}}");
				for (LOTRFaction fac : fclist) {
					for (LOTRBiome biome : bmlist) {
						if (biome != null) {
							if (fac.factionName().equals(getBiomeName(biome))) {
								xml.println("| " + fac.factionName() + " (" + factionLoc + ") = " + fac.factionName());
							}
						}
					}
				}
				xml.println(end);

				/* MOBS */

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0417\u043D\u0430\u043C\u0435\u043D\u043E\u0441\u0435\u0446");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRBannerBearer) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041A\u043E\u043C\u0430\u043D\u0434\u0438\u0440");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRUnitTradeable) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041A\u0443\u0437\u043D\u0435\u0446");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRTradeable.Smith) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041C\u0430\u0443\u043D\u0442");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRNPCMount) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041D\u0430\u0451\u043C\u043D\u0438\u043A");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRMercenary) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0420\u0430\u0431\u043E\u0442\u043D\u0438\u043A");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRFarmhand) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0422\u043E\u0440\u0433\u043E\u0432\u0435\u0446");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRTradeable) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0423\u0441\u0442\u043E\u0439\u0447\u0438\u0432\u044B\u0439 \u043A \u0436\u0430\u0440\u0435");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRBiomeGenNearHarad.ImmuneToHeat) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				/*********/

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-NPC");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0410\u0433\u0440\u0435\u0441\u0441\u043E\u0440");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC && LFGReflectionHelper.getTargetSeeker((LOTREntityNPC) entityClassToObject.get(mob)) || entityClassToObject.get(mob) instanceof EntityMob || entityClassToObject.get(mob) instanceof LOTREntityNPC && ((LOTREntityNPC) entityClassToObject.get(mob)).getFaction() == LOTRFaction.HOSTILE) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0411\u0438\u043E\u043C");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					int i = 1;
					for (LOTRBiome biome : bmlist) {
						if (biome != null) {
							List sus = new ArrayList(biome.getSpawnableList(EnumCreatureType.ambient));
							sus.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
							sus.addAll(biome.getSpawnableList(EnumCreatureType.creature));
							sus.addAll(biome.getSpawnableList(EnumCreatureType.monster));
							sus.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
							for (FactionContainer cont : LFGReflectionHelper.getFactionContainers(biome.npcSpawnList)) {
								for (SpawnListContainer one : LFGReflectionHelper.getSpawnLists(cont)) {
									sus.addAll(LFGReflectionHelper.getSpawnListList(LFGReflectionHelper.getSpawnList(one)));
								}
							}
							for (Object var : sus) {
								if (((SpawnListEntry) var).entityClass.equals(mob)) {
									if (i == 1) {
										xml.println("| " + getEntityName(mob) + " = ");
									}
									i++;
									xml.println("* {{\u0411\u0414 \u0411\u0438\u043E\u043C-\u0421\u0441\u044B\u043B\u043A\u0430|" + getBiomeName(biome) + "}};");
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0412\u043B\u0430\u0434\u0435\u043B\u0435\u0446");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRUnitTradeable) {
						LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) entityClassToObject.get(mob)).getUnits();
						for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
							if (entry.mountClass == null) {
								xml.println("| " + getEntityName(entry.entityClass) + " = [[" + getEntityName(mob) + "]]");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0415\u0437\u0434\u043E\u0432\u043E\u0439");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPCRideable) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0417\u0434\u043E\u0440\u043E\u0432\u044C\u0435");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					xml.println("| " + getEntityName(mob) + " = " + ((EntityLivingBase) entityClassToObject.get(mob)).getMaxHealth());
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0418\u043C\u044F");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC && !(entityClassToObject.get(mob) instanceof LOTREntitySaruman)) {
						xml.println("| " + getEntityName(mob) + " = " + ((LOTREntityNPC) entityClassToObject.get(mob)).getNPCName());
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041C\u043E\u0440\u043E\u0437\u043E\u0443\u0441\u0442\u043E\u0439\u0447\u0438\u0432\u043E\u0441\u0442\u044C");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC && ((LOTREntityNPC) entityClassToObject.get(mob)).isImmuneToFrost) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041D\u043E\u0447\u043D\u043E\u0439 \u0441\u043F\u0430\u0432\u043D");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC && LFGReflectionHelper.getSpawnsInDarkness((LOTREntityNPC) entityClassToObject.get(mob))) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041E\u0433\u043D\u0435\u0443\u043F\u043E\u0440\u043D\u043E\u0441\u0442\u044C");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob).isImmuneToFire()) {
						xml.println("| " + getEntityName(mob) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041F\u043E\u043A\u0443\u043F\u0430\u0435\u0442");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRTradeable) {
						LOTRTradeEntries entries = ((LOTRTradeable) entityClassToObject.get(mob)).getSellPool();
						xml.println("| " + getEntityName(mob) + " = ");
						for (LOTRTradeEntry entry : entries.tradeEntries) {
							xml.println("* " + entry.createTradeItem().getDisplayName() + ": {{\u0414\u0435\u043D\u044C\u0433\u0438|" + entry.getCost() + "}};");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u041F\u0440\u043E\u0434\u0430\u0451\u0442");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRTradeable) {
						LOTRTradeEntries entries = ((LOTRTradeable) entityClassToObject.get(mob)).getBuyPool();
						xml.println("| " + getEntityName(mob) + " = ");
						for (LOTRTradeEntry entry : entries.tradeEntries) {
							xml.println("* " + entry.createTradeItem().getDisplayName() + ": {{\u0414\u0435\u043D\u044C\u0433\u0438|" + entry.getCost() + "}};");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0420\u0435\u043F\u0443\u0442\u0430\u0446\u0438\u044F");
				xml.println(begin);
				for (LOTRUnitTradeEntries entries : trdntrlist) {
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						xml.println("| " + getEntityName(entry.entityClass) + " = +" + entry.alignmentRequired);
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0424\u043E\u0442\u043E");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					xml.println("| " + getEntityName(mob) + " = " + mob.getSimpleName().replace("LOTREntity", "") + ".png");
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0424\u0440\u0430\u043A\u0446\u0438\u044F");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC) {
						xml.println("| " + getEntityName(mob) + " = " + ((LOTREntityNPC) entityClassToObject.get(mob)).getFaction().factionName());
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0426\u0435\u043D\u0430");
				xml.println(begin);
				for (LOTRUnitTradeEntries entries : trdntrlist) {
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						xml.println("| " + getEntityName(entry.entityClass) + " = {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}}");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0426\u0435\u043D\u0430\u041F\u0440\u0438\u0441\u044F\u0433\u0430");
				xml.println(begin);
				for (LOTRUnitTradeEntries entries : trdntrlist) {
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						xml.println("| " + getEntityName(entry.entityClass) + " = {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) + "}}");
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u0426\u0435\u043D\u043D\u043E\u0441\u0442\u044C");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTREntityNPC) {
						xml.println("| " + getEntityName(mob) + " = " + ((LOTREntityNPC) entityClassToObject.get(mob)).getAlignmentBonus());
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041C\u043E\u0431-\u042E\u043D\u0438\u0442\u044B");
				xml.println(begin);
				for (Class mob : entityClassToObject.keySet()) {
					if (entityClassToObject.get(mob) instanceof LOTRUnitTradeable) {
						LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) entityClassToObject.get(mob)).getUnits();
						xml.println("| " + getEntityName(mob) + " = ");
						for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
							if (entry.mountClass == null) {
								xml.println("* [[" + getEntityName(entry.entityClass) + "]]: {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} " + noPledge + ", {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) + "}} " + yesPledge + "; " + entry.alignmentRequired + "+ " + rep + ";");
							}
						}
					}
				}
				xml.println(end);
				xml.println("</mediawiki>");
				xml.close();
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return true;
	}

	public String getBiomeName(LOTRBiome biome) {
		return StatCollector.translateToLocal("lotr.biome." + biome.biomeName + ".name");
	}

	public String getEntityName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity.lotr." + classToEntityName.get(entityClass) + ".name");
	}

	public String getStructureName(Class entityClass) {
		return StatCollector.translateToLocal("lotr.structure." + classToStructureName.get(entityClass) + ".name");
	}

	public enum Database {
		XML("xml"), TABLES("tables");

		String codeName;

		Database(String string) {
			codeName = string;
		}

		public String getName() {
			return codeName;
		}

		public boolean matchesNameOrAlias(String name) {
			return codeName.equals(name);
		}

		public static Database forName(String name) {
			for (Database f : Database.values()) {
				if (f.matchesNameOrAlias(name)) {
					return f;
				}
			}
			return null;
		}

		public static List<String> getNames() {
			ArrayList<String> names = new ArrayList<>();
			for (Database f : Database.values()) {
				names.add(f.getName());
			}
			return names;
		}
	}

	public interface IVillageProperties<V> {
		void apply(V var1);
	}
}