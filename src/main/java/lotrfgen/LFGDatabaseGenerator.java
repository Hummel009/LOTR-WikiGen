package lotrfgen;

import java.io.*;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.*;

import lotr.common.*;
import lotr.common.block.*;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRUnitTradeEntry.PledgeType;
import lotr.common.fac.*;
import lotr.common.item.LOTRItemBanner.BannerType;
import lotr.common.world.biome.*;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.map.LOTRWaypoint.Region;
import lotr.common.world.spawning.*;
import lotr.common.world.spawning.LOTRBiomeSpawnList.*;
import lotr.common.world.spawning.LOTRInvasions.InvasionSpawnEntry;
import lotr.common.world.structure2.LOTRWorldGenStructureBase2;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;

public class LFGDatabaseGenerator extends LOTRWorldGenStructureBase2 {
	public static String display = "null";
	private static HashMap<String, String> factionPageMapping = new HashMap<>();
	private static HashMap<String, String> entityPageMapping = new HashMap<>();
	private static HashMap<String, String> biomePageMapping = new HashMap<>();

	private static HashMap<Class, String> classToEntityNameMapping = new HashMap<>();
	private static HashMap<Class, Entity> classToEntityObjectMapping = new HashMap<>();
	private static HashMap<Class, String> classToStructureNameMapping = new HashMap<>();

	private static String biomeHasAnimals = StatCollector.translateToLocal("db.biomeHasAnimals.name");
	private static String biomeHasConquest = StatCollector.translateToLocal("db.biomeHasConquest.name");
	private static String biomeHasInvasions = StatCollector.translateToLocal("db.biomeHasInvasions.name");
	private static String biomeHasSpawn = StatCollector.translateToLocal("db.biomeHasSpawn.name");
	private static String biomeHasStructures = StatCollector.translateToLocal("db.biomeHasStructures.name");
	private static String biomeHasTreesBiomeAndVariant = StatCollector.translateToLocal("db.biomeHasTrees2.name");
	private static String biomeHasTreesBiomeOnly = StatCollector.translateToLocal("db.biomeHasTrees1.name");
	private static String biomeHasWaypoints = StatCollector.translateToLocal("db.biomeHasWaypoints.name");
	private static String biomeNoAchievement = StatCollector.translateToLocal("db.biomeNoAchievement.name");
	private static String biomeNoAnimals = StatCollector.translateToLocal("db.biomeNoAnimals.name");
	private static String biomeNoConquest = StatCollector.translateToLocal("db.biomeNoConquest.name");
	private static String biomeNoInvasions = StatCollector.translateToLocal("db.biomeNoInvasions.name");
	private static String biomeNoSpawn = StatCollector.translateToLocal("db.biomeNoSpawn.name");
	private static String biomeNoStructures = StatCollector.translateToLocal("db.biomeNoStructures.name");
	private static String biomeNoTrees = StatCollector.translateToLocal("db.biomeNoTrees.name");
	private static String biomeNoVariants = StatCollector.translateToLocal("db.biomeNoVariants.name");
	private static String biomeNoWaypoints = StatCollector.translateToLocal("db.biomeNoWaypoints.name");
	private static String biomeMinerals = StatCollector.translateToLocal("db.biomeMinerals.name");
	private static String biomeConquestOnly = StatCollector.translateToLocal("db.biomeConquestOnly.name");
	private static String biomeSpawnOnly = StatCollector.translateToLocal("db.biomeSpawnOnly.name");

	private static String factionHasBanners = StatCollector.translateToLocal("db.factionHasBanners.name");
	private static String factionHasConquest = StatCollector.translateToLocal("db.factionHasConquest.name");
	private static String factionHasInvasion = StatCollector.translateToLocal("db.factionHasInvasion.name");
	private static String factionHasRanks = StatCollector.translateToLocal("db.factionHasRanks.name");
	private static String factionHasSpawn = StatCollector.translateToLocal("db.factionHasSpawn.name");
	private static String factionHasWarCrimes = StatCollector.translateToLocal("db.factionIsViolent.name");
	private static String factionHasWaypoints = StatCollector.translateToLocal("db.factionHasWaypoints.name");
	private static String factionNoAttr = StatCollector.translateToLocal("db.factionNoAttr.name");
	private static String factionNoBanners = StatCollector.translateToLocal("db.factionNoBanners.name");
	private static String factionNoConquest = StatCollector.translateToLocal("db.factionNoConquest.name");
	private static String factionNoEnemies = StatCollector.translateToLocal("db.factionNoEnemies.name");
	private static String factionNoFriends = StatCollector.translateToLocal("db.factionNoFriends.name");
	private static String factionNoInvasion = StatCollector.translateToLocal("db.factionNoInvasion.name");
	private static String factionNoRanks = StatCollector.translateToLocal("db.factionNoRanks.name");
	private static String factionNoSpawn = StatCollector.translateToLocal("db.factionNoSpawn.name");
	private static String factionNoWarCrimes = StatCollector.translateToLocal("db.factionNotViolent.name");
	private static String factionNoWaypoints = StatCollector.translateToLocal("db.factionNoWaypoints.name");

	private static String treeHasBiomes = StatCollector.translateToLocal("db.treeHasBiomes.name");
	private static String treeNoBiomes = StatCollector.translateToLocal("db.treeNoBiomes.name");
	private static String treeVariantOnly = StatCollector.translateToLocal("db.treeVariantOnly.name");

	private static String biomePage = StatCollector.translateToLocal("db.biomeLoc.name");
	private static String factionPage = StatCollector.translateToLocal("db.factionLoc.name");
	private static String entityPage = StatCollector.translateToLocal("db.entityLoc.name");

	private static String rider = StatCollector.translateToLocal("db.rider.name");
	private static String noPledge = StatCollector.translateToLocal("db.noPledge.name");
	private static String yesPledge = StatCollector.translateToLocal("db.yesPledge.name");
	private static String rep = StatCollector.translateToLocal("db.rep.name");
	private static String category = StatCollector.translateToLocal("db.categoryTemplates.name");

	private static String mineralBiomes = StatCollector.translateToLocal("db.mineralBiomes.name");
	private static String structureBiomes = StatCollector.translateToLocal("db.structureBiomes.name");

	private static String entityNoBiomes = StatCollector.translateToLocal("db.entityNoBiomes.name");
	private static String entityHasBiomes = StatCollector.translateToLocal("db.entityHasBiomes.name");
	private static String entityConquestOnly = StatCollector.translateToLocal("db.entityConquestOnly.name");
	private static String entityInvasionOnly = StatCollector.translateToLocal("db.entityInvasionOnly.name");
	private static String entityConquestInvasion = StatCollector.translateToLocal("db.entityConquestInvasion.name");

	public LFGDatabaseGenerator(boolean flag) {
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int y, int j, int k, int rotation) {
		long time = System.nanoTime();
		LFGConfig cfg = new LFGConfig(world);
		cfg.authorizeEntityInfo();
		cfg.authorizeStructureInfo();

		try {
			HashSet<Item> items = LFGReflectionHelper.getObjectFieldsOfType(LOTRMod.class, Item.class);
			HashSet<LOTRUnitTradeEntries> units = LFGReflectionHelper.getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class);
			HashSet<LOTRAchievement> achievements = LFGReflectionHelper.getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class);
			HashSet<LOTRBiome> biomes = LFGReflectionHelper.getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class);
			HashSet<LOTRFaction> factions = new HashSet<>(EnumSet.allOf(LOTRFaction.class));
			HashSet<LOTRTreeType> trees = new HashSet<>(EnumSet.allOf(LOTRTreeType.class));
			HashSet<LOTRWaypoint> waypoints = new HashSet<>(EnumSet.allOf(LOTRWaypoint.class));
			HashSet<LOTRShields> shields = new HashSet<>(EnumSet.allOf(LOTRShields.class));
			HashSet<String> minerals = new HashSet<>();
			HashSet<Class> structures = new HashSet<>();
			HashSet<Class> hireable = new HashSet<>();
			searchForEntities(world);
			searchForMinerals(biomes, minerals);
			searchForStructures(biomes, structures);
			searchForHireable(hireable, units);
			searchForPagenamesEntity(biomes, factions);
			searchForPagenamesBiome(biomes, factions);
			searchForPagenamesFaction(biomes, factions);
			biomes.remove(LOTRBiome.beachGravel);
			biomes.remove(LOTRBiome.beachWhite);
			Files.createDirectories(Paths.get("hummel"));
			if ("tables".equals(display)) {
				PrintWriter fAchievements = new PrintWriter("hummel/achievements.txt", "UTF-8");
				for (LOTRAchievement ach : achievements) {
					if (ach != null) {
						fAchievements.println("| " + getAchievementTitle(ach) + " || " + getAchievementDesc(ach));
						fAchievements.println("|-");
					}
				}
				fAchievements.close();

				PrintWriter fShields = new PrintWriter("hummel/shields.txt", "UTF-8");
				for (LOTRShields shield : shields) {
					fShields.println("| " + shield.getShieldName() + " || " + shield.getShieldDesc() + " || " + getShieldFilename(shield));
					fShields.println("|-");
				}
				fShields.close();

				PrintWriter fUnits = new PrintWriter("hummel/units.txt", "UTF-8");
				for (LOTRUnitTradeEntries unitTradeEntries : units) {
					if (unitTradeEntries != null) {
						for (LOTRUnitTradeEntry entry : unitTradeEntries.tradeEntries) {
							if (entry != null) {
								if (entry.getPledgeType() == PledgeType.NONE) {
									if (entry.mountClass == null) {
										fUnits.println("| " + getEntityLink(entry.entityClass) + " || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} || +" + entry.alignmentRequired + " || - ");
									} else {
										fUnits.println("| " + getEntityLink(entry.entityClass) + " || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} (" + rider + ") || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} || +" + entry.alignmentRequired + " || - ");
									}
								} else if (entry.mountClass == null) {
									if (entry.alignmentRequired < 101.0f) {
										fUnits.println("| " + getEntityLink(entry.entityClass) + " || N/A || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} || +100.0 || + ");
									} else {
										fUnits.println("| " + getEntityLink(entry.entityClass) + " || N/A || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} || +" + entry.alignmentRequired + " || + ");
									}
								} else if (entry.alignmentRequired < 101.0f) {
									fUnits.println("| " + getEntityLink(entry.entityClass) + " || N/A || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} (" + rider + ") || +100.0 || + ");
								} else {
									fUnits.println("| " + getEntityLink(entry.entityClass) + " || N/A || {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} (" + rider + ") || +" + entry.alignmentRequired + " || + ");
								}
								fUnits.println("|-");
							}
						}
					}
				}
				fUnits.close();

				PrintWriter fWaypoints = new PrintWriter("hummel/waypoints.txt", "UTF-8");
				for (LOTRWaypoint wp : waypoints) {
					fWaypoints.println("| " + wp.getDisplayName() + " || " + wp.getLoreText(usingPlayer));
					fWaypoints.println("|-");
				}
				fWaypoints.close();

				PrintWriter fArmor = new PrintWriter("hummel/armor.txt", "UTF-8");
				for (Item item : items) {
					if (item instanceof ItemArmor) {
						float damage = ((ItemArmor) item).damageReduceAmount;
						ArmorMaterial material = ((ItemArmor) item).getArmorMaterial();
						if (material != null && material.customCraftingMaterial != null) {
							fArmor.println("| " + getItemName(item) + " || " + getItemFilename(item) + " || " + item.getMaxDamage() + " || " + damage + " || " + getItemName(material.customCraftingMaterial));
						} else {
							fArmor.println("| " + getItemName(item) + " || " + getItemFilename(item) + " || " + item.getMaxDamage() + " || " + damage + " || N/A ");
						}
						fArmor.println("|-");
					}
				}
				fArmor.close();

				PrintWriter fWeapon = new PrintWriter("hummel/weapon.txt", "UTF-8");
				for (Item item : items) {
					if (item instanceof ItemSword) {
						float damage = LFGReflectionHelper.getDamageAmount(item);
						ToolMaterial material = LFGReflectionHelper.getToolMaterial(item);
						if (material.getRepairItemStack() != null) {
							fWeapon.println("| " + getItemName(item) + " || " + getItemFilename(item) + " || " + item.getMaxDamage() + " || " + damage + " || " + getItemName(material.getRepairItemStack().getItem()));
						} else {
							fWeapon.println("| " + getItemName(item) + " || " + getItemFilename(item) + " || " + item.getMaxDamage() + " || " + damage + " || N/A ");
						}
						fWeapon.println("|-");
					}
				}
				fWeapon.close();

				PrintWriter fFood = new PrintWriter("hummel/food.txt", "UTF-8");
				for (Item item : items) {
					if (item instanceof ItemFood && item != null) {
						int heal = ((ItemFood) item).func_150905_g(null);
						float saturation = ((ItemFood) item).func_150906_h(null);
						fFood.println("| " + getItemName(item) + " || " + getItemFilename(item) + " || " + "{{Bar|bread|" + new DecimalFormat("#.##").format(saturation * heal * 2) + "}} || {{Bar|food|" + heal + "}} || " + item.getItemStackLimit());
						fFood.println("|-");
					}
				}
				fFood.close();

			} else if ("xml".equals(display)) {
				PrintWriter xml = new PrintWriter("hummel/xml.txt", "UTF-8");
				xml.println("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"ru\">");

				File file = new File("hummel/sitemap.txt");
				if(!file.exists()){
					file.createNewFile();
				}
				List<String> sitemap;
				try (Stream<String> lines = Files.lines(Paths.get("hummel/sitemap.txt"))) {
					sitemap = lines.collect(Collectors.toList());
				}

				String s1 = "<page><title>";

				for (String str : minerals) {
					if (!sitemap.contains(str)) {
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0418\u0441\u043A\u043E\u043F\u0430\u0435\u043C\u043E\u0435}}</text></revision></page>";
						xml.print(s1 + str + s2);
						xml.println();
					}
				}

				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					String pageName = getEntityPagename(entityClass);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u041C\u043E\u0431}}</text></revision></page>";
						xml.print(s1 + pageName + s2);
						xml.println();
					}
				}

				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						String pageName = getBiomePagename(biome);
						if (!sitemap.contains(pageName)) {
							String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0411\u0438\u043E\u043C}}</text></revision></page>";
							xml.print(s1 + pageName + s2);
							xml.println();
						}
					}
				}

				for (LOTRFaction fac : factions) {
					String pageName = getFactionPagename(fac);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0424\u0440\u0430\u043A\u0446\u0438\u044F}}</text></revision></page>";
						xml.print(s1 + pageName + s2);
						xml.println();
					}
				}

				for (LOTRTreeType tree : trees) {
					String pageName = getTreeName(tree);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0414\u0435\u0440\u0435\u0432\u043E}}</text></revision></page>";
						xml.print(s1 + pageName + s2);
						xml.println();
					}
				}

				for (Class strClass : structures) {
					String pageName = getStructureName(strClass);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0421\u0442\u0440\u0443\u043A\u0442\u0443\u0440\u0430}}</text></revision></page>";
						xml.print(s1 + pageName + s2);
						xml.println();
					}
				}

				/* DATABASES */

				String begin = "</title><ns>10</ns><revision><text>&lt;includeonly&gt;{{#switch: {{{1}}}";
				String end = "}}&lt;/includeonly&gt;&lt;noinclude&gt;[[" + category + "]]&lt;/noinclude&gt;</text></revision></page>";

				/* STRUCTURES */

				xml.print("<page><title>Template:DB Structure-Biomes");
				xml.println(begin);
				for (Class strClass : structures) {
					xml.println("| " + getStructureName(strClass) + " = " + structureBiomes);
					next: for (LOTRBiome biome : biomes) {
						if (biome != null && !LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
							for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
								if (LFGReflectionHelper.getStructureGen(structure).getClass() == strClass) {
									xml.println("* " + getBiomeLink(biome) + ";");
									continue next;
								}
							}
						}
					}
				}
				xml.println(end);

				/* MINERALS */

				xml.print("<page><title>Template:DB Mineral-Biomes");
				xml.println(begin);
				for (String mineral : minerals) {
					xml.println("| " + mineral + " = " + mineralBiomes);
					next: for (LOTRBiome biome : biomes) {
						if (biome != null) {
							List oreGenerants = new ArrayList<>(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
							oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeOres"));
							oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeGems"));
							for (Object oreGenerant : oreGenerants) {
								Block block = LFGReflectionHelper.getMineableBlock(LFGReflectionHelper.getOreGen(oreGenerant));
								int meta = LFGReflectionHelper.getMineableBlockMeta(LFGReflectionHelper.getOreGen(oreGenerant));
								if (getBlockMetaName(block, meta).equals(mineral) || getBlockName(block).equals(mineral)) {
									xml.println("* " + getBiomeLink(biome) + " (" + LFGReflectionHelper.getOreChance(oreGenerant) + "%; Y: " + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight") + "-" + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight") + ");");
									continue next;
								}
							}
						}
					}
				}
				xml.println(end);

				/* TREES */

				xml.print("<page><title>Template:DB Tree-Biomes");
				xml.println(begin);
				for (LOTRTreeType tree : trees) {
					HashSet<LOTRBiome> biomesTree = new HashSet<>();
					HashSet<LOTRBiome> biomesVariantTree = new HashSet<>();
					next: for (LOTRBiome biome : biomes) {
						if (biome != null) {
							for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getTreeTypes(biome.decorator)) {
								if (weightedTreeType.treeType == tree) {
									biomesTree.add(biome);
									continue next;
								}
							}
							for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
								for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getVariant(variantBucket).treeTypes) {
									if (weightedTreeType.treeType == tree) {
										biomesVariantTree.add(biome);
										continue next;
									}
								}
							}
						}
					}
					if (biomesTree.isEmpty() && biomesVariantTree.isEmpty()) {
						xml.println("| " + getTreeName(tree) + " = " + treeNoBiomes);
					} else {
						xml.println("| " + getTreeName(tree) + " = " + treeHasBiomes);
					}
					for (LOTRBiome biome : biomesTree) {
						xml.println("* " + getBiomeLink(biome) + ";");
					}
					for (LOTRBiome biome : biomesVariantTree) {
						xml.println("* " + getBiomeLink(biome) + " (" + treeVariantOnly + ");");
					}
				}
				xml.println(end);

				/* BIOMES */

				xml.print("<page><title>Template:DB Biome-SpawnNPC");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
						if (facContainers.isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoSpawn);
						} else {
							ArrayList<FactionContainer> spawnContainers = new ArrayList<>();
							for (FactionContainer facContainer : facContainers) {
								if (LFGReflectionHelper.getBaseWeight(facContainer) > 0) {
									spawnContainers.add(facContainer);
								}
							}
							if (spawnContainers.isEmpty()) {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeConquestOnly);
							} else {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeHasSpawn);
								for (FactionContainer facContainer : spawnContainers) {
									for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
										for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
											xml.println("* " + getEntityLink(entry.entityClass) + "; ");
										}
									}
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-ConquestNPC");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
						if (facContainers.isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoConquest);
						} else {
							ArrayList<FactionContainer> conqestContainers = new ArrayList<>();
							for (FactionContainer facContainer : facContainers) {
								if (LFGReflectionHelper.getBaseWeight(facContainer) <= 0) {
									conqestContainers.add(facContainer);
								}
							}
							if (conqestContainers.isEmpty()) {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeSpawnOnly);
							} else {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeHasConquest);
								HashSet<LOTRFaction> conquestFactions = new HashSet<>();
								for (FactionContainer facContainer : conqestContainers) {
									next: for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
										for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
											Entity entity = classToEntityObjectMapping.get(entry.entityClass);
											if (entity instanceof LOTREntityNPC) {
												LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
												conquestFactions.add(fac);
												continue next;
											}
										}
									}
								}
								for (LOTRFaction fac : conquestFactions) {
									xml.println("* " + getFactionLink(fac) + "; ");
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Bandits");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + biome.getBanditChance());
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Name");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + getBiomeName(biome));
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Rainfall");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + biome.rainfall);
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Temperature");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + biome.temperature);
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Variants");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						if (LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall()).isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoVariants);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = ");
							for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
								xml.println("* " + getBiomeVariantName(LFGReflectionHelper.getVariant(variantBucket)) + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Invasions");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						if (LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoInvasions);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeHasInvasions);
							HashSet<LOTRFaction> invasionFactions = new HashSet<>();
							next: for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
								for (InvasionSpawnEntry entry : invasion.invasionMobs) {
									Entity entity = classToEntityObjectMapping.get(entry.getEntityClass());
									if (entity instanceof LOTREntityNPC) {
										LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
										invasionFactions.add(fac);
										continue next;
									}
								}
							}
							for (LOTRFaction fac : invasionFactions) {
								xml.println("* " + getFactionLink(fac) + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Waypoints");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						Region region = biome.getBiomeWaypoints();
						if (region == null) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoWaypoints);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeHasWaypoints);
							for (LOTRWaypoint wp : waypoints) {
								if (LFGReflectionHelper.getRegion(wp) == region) {
									xml.println("* " + wp.getDisplayName() + " (" + getFactionLink(wp.faction) + ");");
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Achievement");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						LOTRAchievement ach = biome.getBiomeAchievement();
						if (ach == null) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoAchievement);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = \"" + getAchievementTitle(ach) + "\"");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Trees");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						HashSet<LOTRTreeType> treesBiome = new HashSet<>();
						HashMap<LOTRTreeType, LOTRBiomeVariant> treesVariant = new HashMap<>();
						for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getTreeTypes(biome.decorator)) {
							treesBiome.add(weightedTreeType.treeType);
						}
						for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
							for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getVariant(variantBucket).treeTypes) {
								treesVariant.put(weightedTreeType.treeType, LFGReflectionHelper.getVariant(variantBucket));
							}
						}
						if (treesBiome.isEmpty() && treesVariant.isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoTrees);
						} else {
							HashMap<LOTRTreeType, LOTRBiomeVariant> treesVariantOnly = new HashMap<>();
							if (treesVariantOnly.isEmpty()) {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeHasTreesBiomeOnly);
							} else {
								xml.println("| " + getBiomePagename(biome) + " = " + biomeHasTreesBiomeAndVariant);
							}
							if (!treesBiome.isEmpty()) {
								for (LOTRTreeType tree : treesBiome) {
									xml.println("* [[" + getTreeName(tree) + "]];");
								}
							}
							if (!treesVariantOnly.isEmpty()) {
								for (LOTRTreeType tree : treesVariantOnly.keySet()) {
									if (!treesBiome.contains(tree)) {
										xml.println("* [[" + getTreeName(tree) + "]] (" + getBiomeVariantName(treesVariantOnly.get(tree)).toLowerCase() + ")" + ";");
									}
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Mobs");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						List<SpawnListEntry> entries = new ArrayList<>(biome.getSpawnableList(EnumCreatureType.ambient));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
						entries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
						entries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
						if (entries.isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoAnimals);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeHasAnimals);
							for (SpawnListEntry entry : entries) {
								if (classToEntityNameMapping.containsKey(entry.entityClass)) {
									xml.println("* " + getEntityLink(entry.entityClass) + ";");
								} else {
									xml.println("* " + getEntityVanillaName(entry.entityClass) + ";");
								}
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Minerals");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + biomeMinerals);
						List oreGenerants = new ArrayList<>(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
						oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeOres"));
						oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeGems"));
						for (Object oreGenerant : oreGenerants) {
							Block block = LFGReflectionHelper.getMineableBlock(LFGReflectionHelper.getOreGen(oreGenerant));
							int meta = LFGReflectionHelper.getMineableBlockMeta(LFGReflectionHelper.getOreGen(oreGenerant));

							if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
								xml.println("* [[" + getBlockMetaName(block, meta) + "]] (" + LFGReflectionHelper.getOreChance(oreGenerant) + "%; Y: " + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight") + "-" + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight") + ");");
							} else {
								xml.println("* [[" + getBlockName(block) + "]] (" + LFGReflectionHelper.getOreChance(oreGenerant) + "%; Y: " + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight") + "-" + LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight") + ");");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Music");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						if (biome.getBiomeMusic() != null) {
							xml.println("| " + getBiomePagename(biome) + " = " + biome.getBiomeMusic().subregion);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = N/A");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Structures");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						if (LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeNoStructures);
						} else {
							xml.println("| " + getBiomePagename(biome) + " = " + biomeHasStructures);
							for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
								xml.println("* [[" + getStructureName(LFGReflectionHelper.getStructureGen(structure).getClass()) + "]];");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Biome-Photo");
				xml.println(begin);
				for (LOTRBiome biome : biomes) {
					if (biome != null) {
						xml.println("| " + getBiomePagename(biome) + " = " + biome.biomeName + " (biome).png");
					}
				}
				xml.println(end);

				/* FACTIONS */

				xml.print("<page><title>Template:DB Faction-NPC");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					xml.println("| " + getFactionPagename(fac) + " = ");
					for (Class entityClass : classToEntityObjectMapping.keySet()) {
						Entity entity = classToEntityObjectMapping.get(entityClass);
						if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
							xml.println("* " + getEntityLink(entityClass) + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Invasions");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					HashSet<LOTRBiome> invasionBiomes = new HashSet<>();
					next: for (LOTRBiome biome : biomes) {
						if (biome != null && !LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
							for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
								for (InvasionSpawnEntry entry : invasion.invasionMobs) {
									Entity entity = classToEntityObjectMapping.get(entry.getEntityClass());
									if (entity instanceof LOTREntityNPC && fac == ((LOTREntityNPC) entity).getFaction()) {
										invasionBiomes.add(biome);
										continue next;
									}
								}
							}
						}
					}
					if (invasionBiomes.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoInvasion);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasInvasion);
						for (LOTRBiome biome : invasionBiomes) {
							xml.println("* " + getBiomeLink(biome) + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Spawn");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					HashSet<LOTRBiome> spawnBiomes = new HashSet<>();
					next: for (LOTRBiome biome : biomes) {
						if (biome != null) {
							List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
							if (!facContainers.isEmpty()) {
								ArrayList<FactionContainer> spawnContainers = new ArrayList<>();
								for (FactionContainer facContainer : facContainers) {
									if (LFGReflectionHelper.getBaseWeight(facContainer) > 0) {
										spawnContainers.add(facContainer);
									}
								}
								if (!spawnContainers.isEmpty()) {
									for (FactionContainer facContainer : spawnContainers) {
										for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
											for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
												Entity entity = classToEntityObjectMapping.get(entry.entityClass);
												if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
													spawnBiomes.add(biome);
													continue next;
												}
											}
										}
									}
								}
							}
						}
					}
					if (spawnBiomes.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoSpawn);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasSpawn);
						for (LOTRBiome biome : spawnBiomes) {
							xml.println("* " + getBiomeLink(biome) + ";");
						}
					}

				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Conquest");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					HashSet<LOTRBiome> conquestBiomes = new HashSet<>();
					next: for (LOTRBiome biome : biomes) {
						if (biome != null) {
							List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
							if (!facContainers.isEmpty()) {
								ArrayList<FactionContainer> conquestContainers = new ArrayList<>();
								for (FactionContainer facContainer : facContainers) {
									if (LFGReflectionHelper.getBaseWeight(facContainer) <= 0) {
										conquestContainers.add(facContainer);
									}
								}
								if (!conquestContainers.isEmpty()) {
									for (FactionContainer facContainer : conquestContainers) {
										for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
											for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
												Entity entity = classToEntityObjectMapping.get(entry.entityClass);
												if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
													conquestBiomes.add(biome);
													continue next;
												}
											}
										}
									}
								}
							}
						}
					}
					if (conquestBiomes.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoConquest);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasConquest);
						for (LOTRBiome biome : conquestBiomes) {
							xml.println("* " + getBiomeLink(biome) + ";");
						}
					}

				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Ranks");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					if (LFGReflectionHelper.getRanksSortedDescending(fac).isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoRanks);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasRanks);
						for (LOTRFactionRank rank : LFGReflectionHelper.getRanksSortedDescending(fac)) {
							xml.println("* " + rank.getDisplayFullName() + "/" + rank.getDisplayFullNameFem() + " (+" + rank.alignment + ");");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Banners");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					if (fac.factionBanners.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoBanners);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasBanners);
						for (BannerType banner : fac.factionBanners) {
							xml.println("* " + getBannerName(banner) + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Waypoints");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					ArrayList<LOTRWaypoint> facWaypoints = new ArrayList<>();
					for (LOTRWaypoint wp : waypoints) {
						if (wp.faction == fac) {
							facWaypoints.add(wp);
						}
					}
					if (facWaypoints.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoWaypoints);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasWaypoints);
						for (LOTRWaypoint wp : facWaypoints) {
							xml.println("* " + wp.getDisplayName() + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Attr");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					ArrayList<LOTRShields> facShields = new ArrayList<>();
					for (LOTRShields shield : shields) {
						if (LFGReflectionHelper.getAlignmentFaction(shield) == fac) {
							facShields.add(shield);
						}
					}

					if (facShields.isEmpty()) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionNoAttr);
					} else {
						xml.println("| " + getFactionPagename(fac) + " = ");
						xml.println("&lt;table class=\"wikitable shields\"&gt;");
						for (LOTRShields shield : facShields) {
							xml.println("&lt;tr&gt;&lt;td&gt;" + shield.getShieldName() + "&lt;/td&gt;&lt;td&gt;" + shield.getShieldDesc() + "&lt;/td&gt;&lt;td&gt;" + getShieldFilename(shield) + "&lt;/td&gt;&lt;/tr&gt;");
						}
						xml.println("&lt;table&gt;");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Pledge");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					if (fac.getPledgeRank() != null) {
						xml.println("| " + getFactionPagename(fac) + " = " + fac.getPledgeRank().getDisplayName() + "/" + fac.getPledgeRank().getDisplayNameFem() + " (+" + fac.getPledgeAlignment() + ")");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Enemies");
				xml.println(begin);
				for (LOTRFaction fac1 : factions) {
					ArrayList<LOTRFaction> facEnemies = new ArrayList<>();
					for (LOTRFaction fac2 : factions) {
						if (fac1.isBadRelation(fac2) && fac1 != fac2) {
							facEnemies.add(fac2);
						}
					}
					if (facEnemies.isEmpty()) {
						xml.println("| " + getFactionPagename(fac1) + " = " + factionNoEnemies);
					} else {
						xml.println("| " + getFactionPagename(fac1) + " = ");
						int i = 0;
						for (LOTRFaction fac : facEnemies) {
							if (i == 0) {
								xml.print(getFactionLink(fac));
								i++;
							} else {
								xml.print(" \u2022 " + getFactionLink(fac));
							}
						}
						xml.println();
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Friends");
				xml.println(begin);
				for (LOTRFaction fac1 : factions) {
					ArrayList<LOTRFaction> facFriends = new ArrayList<>();
					for (LOTRFaction fac2 : factions) {
						if (fac1.isGoodRelation(fac2) && fac1 != fac2) {
							facFriends.add(fac2);
						}
					}
					if (facFriends.isEmpty()) {
						xml.println("| " + getFactionPagename(fac1) + " = " + factionNoFriends);
					} else {
						xml.println("| " + getFactionPagename(fac1) + " = ");
						int i = 0;
						for (LOTRFaction fac : facFriends) {
							if (i == 0) {
								xml.print(getFactionLink(fac));
								i++;
							} else {
								xml.print(" \u2022 " + getFactionLink(fac));
							}
						}
						xml.println();
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-WarCrimes");
				xml.println(begin);
				xml.println("| #default = " + factionNoWarCrimes);
				for (LOTRFaction fac : factions) {
					if (fac.approvesWarCrimes) {
						xml.println("| " + getFactionPagename(fac) + " = " + factionHasWarCrimes);
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Codename");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					xml.println("| " + getFactionPagename(fac) + " = " + fac.codeName());
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Name");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					xml.println("| " + getFactionPagename(fac) + " = " + getFactionName(fac));
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Faction-Region");
				xml.println(begin);
				for (LOTRFaction fac : factions) {
					if (fac.factionRegion != null) {
						xml.println("| " + getFactionPagename(fac) + " = " + fac.factionRegion.getRegionName());
					} else {
						xml.println("| " + getFactionPagename(fac) + " = N/A");
					}
				}
				xml.println(end);

				/* MOBS */

				xml.print("<page><title>Template:DB Mob-Spawn");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					HashSet<LOTRBiome> spawnBiomes = new HashSet<>();
					HashSet<LOTRBiome> conquestBiomes = new HashSet<>();
					HashSet<LOTRBiome> invasionBiomes = new HashSet<>();
					HashSet<LOTRBiome> unnaturalBiomes = new HashSet<>();
					next: for (LOTRBiome biome : biomes) {
						List<SpawnListEntry> spawnEntries = new ArrayList<>();
						List<SpawnListEntry> conquestEntries = new ArrayList<>();
						List<InvasionSpawnEntry> invasionEntries = new ArrayList<>();
						spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.ambient));
						spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
						spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
						spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
						spawnEntries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
						for (FactionContainer facContainer : LFGReflectionHelper.getFactionContainers(biome.npcSpawnList)) {
							if (LFGReflectionHelper.getBaseWeight(facContainer) > 0) {
								for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
									spawnEntries.addAll(LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container)));
								}
							} else {
								for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
									conquestEntries.addAll(LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container)));
								}
							}
						}
						if (!LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
							for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
								invasionEntries.addAll(invasion.invasionMobs);
							}
						}
						for (SpawnListEntry entry : spawnEntries) {
							if (entry.entityClass == entityClass) {
								spawnBiomes.add(biome);
								continue next;
							}
						}
						for (SpawnListEntry entry : conquestEntries) {
							if (entry.entityClass == entityClass) {
								conquestBiomes.add(biome);
								break;
							}
						}
						for (InvasionSpawnEntry entry : invasionEntries) {
							if (entry.getEntityClass() == entityClass) {
								invasionBiomes.add(biome);
								break;
							}
						}
					}
					if (spawnBiomes.isEmpty() && conquestBiomes.isEmpty() && invasionBiomes.isEmpty()) {
						xml.println("| " + getEntityPagename(entityClass) + " = " + entityNoBiomes);
					} else {
						xml.println("| " + getEntityPagename(entityClass) + " = " + entityHasBiomes);
						for (LOTRBiome biome : spawnBiomes) {
							xml.println("* " + getBiomeLink(biome) + ";");
						}
						for (LOTRBiome biome : conquestBiomes) {
							if (!invasionBiomes.contains(biome)) {
								xml.println("* " + getBiomeLink(biome) + " " + entityConquestOnly + ";");
							}
						}
						for (LOTRBiome biome : invasionBiomes) {
							if (!conquestBiomes.contains(biome)) {
								xml.println("* " + getBiomeLink(biome) + " " + entityInvasionOnly + ";");
							}
						}
						unnaturalBiomes.addAll(conquestBiomes);
						unnaturalBiomes.addAll(invasionBiomes);
						for (LOTRBiome biome : unnaturalBiomes) {
							if (conquestBiomes.contains(biome) && invasionBiomes.contains(biome)) {
								xml.println("* " + getBiomeLink(biome) + " " + entityConquestInvasion + ";");
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>DB Mob-Owner");
				xml.println(begin);
				for (Class entityClass : hireable) {
					HashMap<Class, Class> owners = new HashMap<>();
					loop: for (Class ownerClass : classToEntityObjectMapping.keySet()) {
						if (classToEntityObjectMapping.get(ownerClass) instanceof LOTRUnitTradeable) {
							LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) classToEntityObjectMapping.get(ownerClass)).getUnits();
							for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
								if (entry.entityClass == entityClass) {
									owners.put(entityClass, ownerClass);
									break loop;
								}
							}
						}
					}
					if (!owners.isEmpty()) {
						xml.println("| " + getEntityPagename(entityClass) + " = " + getEntityLink(owners.get(entityClass)));
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Health");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					EntityLivingBase entity = (EntityLivingBase) classToEntityObjectMapping.get(entityClass);
					xml.println("| " + getEntityPagename(entityClass) + " = " + entity.getMaxHealth());
				}
				xml.println(end);

				xml.print("<page><title>Template: DB Mob-Rideable1");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPCRideable) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Rideable2");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRNPCMount) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-BannerBearer");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRBannerBearer) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-UnitTradeable");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRUnitTradeable) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Tradeable");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRTradeable) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Smith");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRTradeable.Smith) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Mercenary");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRMercenary) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Farmhand");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRFarmhand) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Buys");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRTradeable) {
						LOTRTradeEntries entries = ((LOTRTradeable) classToEntityObjectMapping.get(entityClass)).getSellPool();
						xml.println("| " + getEntityPagename(entityClass) + " = ");
						for (LOTRTradeEntry entry : entries.tradeEntries) {
							xml.println("* " + entry.createTradeItem().getDisplayName() + ": {{Coins|" + entry.getCost() + "}};");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Sells");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRTradeable) {
						LOTRTradeEntries entries = ((LOTRTradeable) classToEntityObjectMapping.get(entityClass)).getBuyPool();
						xml.println("| " + getEntityPagename(entityClass) + " = ");
						for (LOTRTradeEntry entry : entries.tradeEntries) {
							xml.println("* " + entry.createTradeItem().getDisplayName() + ": {{Coins|" + entry.getCost() + "}};");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-ImmuneToFrost");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPC && ((LOTREntityNPC) classToEntityObjectMapping.get(entityClass)).isImmuneToFrost) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-ImmuneToFire");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass).isImmuneToFire()) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-ImmuneToHeat");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTRBiomeGenNearHarad.ImmuneToHeat) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-SpawnInDarkness");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPC && LFGReflectionHelper.getSpawnsInDarkness((LOTREntityNPC) classToEntityObjectMapping.get(entityClass))) {
						xml.println("| " + getEntityPagename(entityClass) + " = True");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Alignment");
				xml.println(begin);
				next: for (Class entityClass : hireable) {
					for (LOTRUnitTradeEntries entries : units) {
						for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
							if (entry.entityClass == entityClass) {
								if (entry.getPledgeType() == PledgeType.NONE || entry.alignmentRequired >= 101.0f) {
									xml.println("| " + getEntityPagename(entityClass) + " = " + entry.alignmentRequired);
								} else {
									xml.println("| " + getEntityPagename(entityClass) + " = +" + 100.0);
								}
								continue next;
							}
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Faction");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPC) {
						LOTRFaction fac = ((LOTREntityNPC) classToEntityObjectMapping.get(entityClass)).getFaction();
						xml.println("| " + getEntityPagename(entityClass) + " = " + getFactionLink(fac));
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Achievement");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPC) {
						LOTRAchievement ach = LFGReflectionHelper.getKillAchievement((LOTREntityNPC) classToEntityObjectMapping.get(entityClass));
						if (ach != null) {
							xml.println("| " + getEntityPagename(entityClass) + " = \"" + getAchievementTitle(ach) + "\"");
						} else {
							xml.println("| " + getEntityPagename(entityClass) + " = N/A");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Bonus");
				xml.println(begin);
				for (Class entityClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(entityClass) instanceof LOTREntityNPC) {
						float bonus = ((LOTREntityNPC) classToEntityObjectMapping.get(entityClass)).getAlignmentBonus();
						xml.println("| " + getEntityPagename(entityClass) + " = +" + bonus);
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Price");
				xml.println(begin);
				for (LOTRUnitTradeEntries entries : units) {
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						if (entry.getPledgeType() == PledgeType.NONE) {
							xml.println("| " + getEntityPagename(entry.entityClass) + " = {{Coins|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}}");
						} else {
							xml.println("| " + getEntityPagename(entry.entityClass) + " = N/A");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-PricePledge");
				xml.println(begin);
				for (LOTRUnitTradeEntries entries : units) {
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						xml.println("| " + getEntityPagename(entry.entityClass) + " = {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}}");
					}
				}
				xml.println(end);

				xml.print("<page><title>Template:DB Mob-Units");
				xml.println(begin);
				for (Class ownerClass : classToEntityObjectMapping.keySet()) {
					if (classToEntityObjectMapping.get(ownerClass) instanceof LOTRUnitTradeable) {
						LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) classToEntityObjectMapping.get(ownerClass)).getUnits();
						xml.println("| " + getEntityPagename(ownerClass) + " = ");
						for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
							if (entry.mountClass == null) {
								if (entry.getPledgeType() == PledgeType.NONE) {
									xml.println("* " + getEntityLink(entry.entityClass) + ": {{Coins|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} " + noPledge + ", {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} " + yesPledge + "; " + entry.alignmentRequired + "+ " + rep + ";");
								} else if (entry.alignmentRequired < 101.0f) {
									xml.println("* " + getEntityLink(entry.entityClass) + ": N/A " + noPledge + ", {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} " + yesPledge + "; +" + 100.0 + "+ " + rep + ";");
								} else {
									xml.println("* " + getEntityLink(entry.entityClass) + ": N/A " + noPledge + ", {{Coins|" + LFGReflectionHelper.getInitialCost(entry) + "}} " + yesPledge + "; +" + entry.alignmentRequired + "+ " + rep + ";");
								}
							}
						}
					}
				}
				xml.println(end);
				xml.println("</mediawiki>");
				xml.close();
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
		long newTime = System.nanoTime();
		ChatComponentText chatComponentTranslation = new ChatComponentText("Generated databases in " + (newTime - time) / 1.0E9 + "s");
		usingPlayer.addChatMessage(chatComponentTranslation);
		return true;
	}

	private String getAchievementDesc(LOTRAchievement ach) {
		return StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".desc");
	}

	private String getAchievementTitle(LOTRAchievement ach) {
		return StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".title");
	}

	private String getBannerName(BannerType banner) {
		return StatCollector.translateToLocal("item.lotr:banner." + banner.bannerName + ".name");
	}

	private String getBiomeLink(LOTRBiome biome) {
		String biomeName = getBiomeName(biome);
		String biomePagename = getBiomePagename(biome);
		if (biomeName.equals(biomePagename)) {
			return "[[" + biomeName + "]]";
		}
		return "[[" + biomePagename + "|" + biomeName + "]]";
	}

	private String getBiomeName(LOTRBiome biome) {
		return StatCollector.translateToLocal("lotr.biome." + biome.biomeName + ".name");
	}

	private String getBiomePagename(LOTRBiome biome) {
		return biomePageMapping.get(getBiomeName(biome));
	}

	private String getBiomeVariantName(LOTRBiomeVariant variant) {
		return StatCollector.translateToLocal("lotr.variant." + variant.variantName + ".name");
	}

	private String getBlockMetaName(Block block, int meta) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + "." + meta + ".name");
	}

	private String getBlockName(Block block) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + ".name");
	}

	private String getEntityLink(Class entityClass) {
		String entityName = getEntityName(entityClass);
		String entityPagename = getEntityPagename(entityClass);
		if (entityName.equals(entityPagename)) {
			return "[[" + entityPagename + "]]";
		}
		return "[[" + entityPagename + "|" + entityName + "]]";
	}

	private String getEntityName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity.lotr." + getClassToEntityNameMapping().get(entityClass) + ".name");
	}

	private String getEntityPagename(Class entityClass) {
		return entityPageMapping.get(getEntityName(entityClass));
	}

	private String getEntityVanillaName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity." + EntityList.classToStringMapping.get(entityClass) + ".name");
	}

	private String getFactionLink(LOTRFaction fac) {
		String facName = getFactionName(fac);
		String facPagename = getFactionPagename(fac);
		if (facName.equals(facPagename)) {
			return "[[" + facPagename + "]]";
		}
		return "[[" + facPagename + "|" + facName + "]]";
	}

	private String getFactionName(LOTRFaction fac) {
		return StatCollector.translateToLocal("lotr.faction." + fac.codeName() + ".name");
	}

	private String getFactionPagename(LOTRFaction fac) {
		return factionPageMapping.get(getFactionName(fac));
	}

	private String getItemFilename(Item item) {
		return "[[File:" + item.getUnlocalizedName().substring("item.lotr:".length()) + ".png|32px|link=]]";
	}

	private String getItemName(Item item) {
		return StatCollector.translateToLocal(item.getUnlocalizedName() + ".name");
	}

	private String getShieldFilename(LOTRShields shield) {
		return "[[File:Shield " + shield.name().toLowerCase() + ".png]]";
	}

	private String getStructureName(Class entityClass) {
		return StatCollector.translateToLocal("lotr.structure." + getClassToStructureNameMapping().get(entityClass) + ".name");
	}

	private String getTreeName(LOTRTreeType tree) {
		return StatCollector.translateToLocal("lotr.tree." + tree.name().toLowerCase() + ".name");
	}

	private void searchForEntities(World world) {
		for (Class entityClass : classToEntityObjectMapping.keySet()) {
			classToEntityObjectMapping.put(entityClass, LFGReflectionHelper.newEntity(entityClass, world));
		}
	}

	private void searchForHireable(HashSet<Class> hireable, HashSet<LOTRUnitTradeEntries> units) {
		for (LOTRUnitTradeEntries entries : units) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				hireable.add(entry.entityClass);
			}
		}
	}

	private void searchForMinerals(HashSet<LOTRBiome> biomes, HashSet<String> minerals) {
		for (LOTRBiome biome : biomes) {
			if (biome != null) {
				List oreGenerants = new ArrayList<>(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
				oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeOres"));
				oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeGems"));
				for (Object oreGenerant : oreGenerants) {
					Block block = LFGReflectionHelper.getMineableBlock(LFGReflectionHelper.getOreGen(oreGenerant));
					int meta = LFGReflectionHelper.getMineableBlockMeta(LFGReflectionHelper.getOreGen(oreGenerant));
					if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
						minerals.add(getBlockMetaName(block, meta));
					} else {
						minerals.add(getBlockName(block));
					}
				}
			}
		}
	}

	private void searchForPagenamesBiome(HashSet<LOTRBiome> biomes, HashSet<LOTRFaction> factions) {
		next: for (LOTRBiome biome : biomes) {
			String preName = getBiomeName(biome);
			for (LOTRFaction fac : factions) {
				if (preName.equals(getFactionName(fac))) {
					biomePageMapping.put(preName, preName + " (" + biomePage + ")");
					continue next;
				}
			}
			for (Class entity : classToEntityObjectMapping.keySet()) {
				if (preName.equals(getEntityName(entity))) {
					biomePageMapping.put(preName, preName + " (" + biomePage + ")");
					continue next;
				}
			}
			biomePageMapping.put(preName, preName);
		}
	}

	private void searchForPagenamesEntity(HashSet<LOTRBiome> biomes, HashSet<LOTRFaction> factions) {
		next: for (Class mob : classToEntityObjectMapping.keySet()) {
			String preName = getEntityName(mob);
			for (LOTRBiome biome : biomes) {
				if (preName.equals(getBiomeName(biome))) {
					entityPageMapping.put(preName, preName + " (" + entityPage + ")");
					continue next;
				}
			}
			for (LOTRFaction fac : factions) {
				if (preName.equals(getFactionName(fac))) {
					entityPageMapping.put(preName, preName + " (" + entityPage + ")");
					continue next;
				}
			}
			entityPageMapping.put(preName, preName);
		}
	}

	private void searchForPagenamesFaction(HashSet<LOTRBiome> biomes, HashSet<LOTRFaction> factions) {
		next: for (LOTRFaction fac : factions) {
			String preName = getFactionName(fac);
			for (LOTRBiome biome : biomes) {
				if (preName.equals(getBiomeName(biome))) {
					factionPageMapping.put(preName, preName + " (" + factionPage + ")");
					continue next;
				}
			}
			for (Class entity : classToEntityObjectMapping.keySet()) {
				if (preName.equals(getEntityName(entity))) {
					factionPageMapping.put(preName, preName + " (" + factionPage + ")");
					continue next;
				}
			}
			factionPageMapping.put(preName, preName);
		}
	}

	private void searchForStructures(HashSet<LOTRBiome> biomes, HashSet<Class> structures) {
		for (LOTRBiome biome : biomes) {
			if (biome != null && !LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
				for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
					structures.add(LFGReflectionHelper.getStructureGen(structure).getClass());
				}
			}
		}
	}

	public static HashMap<Class, String> getClassToEntityNameMapping() {
		return classToEntityNameMapping;
	}

	public static HashMap<Class, Entity> getClassToEntityObjectMapping() {
		return classToEntityObjectMapping;
	}

	public static HashMap<Class, String> getClassToStructureNameMapping() {
		return classToStructureNameMapping;
	}

	public enum Database {
		XML("xml"), TABLES("tables");

		String codeName;

		Database(String name) {
			codeName = name;
		}

		public String getCodename() {
			return codeName;
		}

		public boolean matchesNameOrAlias(String name) {
			return codeName.equals(name);
		}

		public static Database forName(String name) {
			for (Database db : Database.values()) {
				if (db.matchesNameOrAlias(name)) {
					return db;
				}
			}
			return null;
		}

		public static List<String> getNames() {
			ArrayList<String> names = new ArrayList<>();
			for (Database db : Database.values()) {
				names.add(db.getCodename());
			}
			return names;
		}
	}
}