package com.github.hummel.wikigen.engine;

import lotr.common.*;
import lotr.common.block.LOTRBlockOreGem;
import lotr.common.block.LOTRBlockRock;
import lotr.common.entity.npc.*;
import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRFactionRank;
import lotr.common.item.LOTRItemBanner;
import lotr.common.util.LOTRLog;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenNearHarad;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.spawning.LOTRBiomeSpawnList;
import lotr.common.world.spawning.LOTRInvasions;
import lotr.common.world.spawning.LOTRSpawnEntry;
import lotr.common.world.village.LOTRVillageGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.hummel.wikigen.engine.WikiGenerator.Utils.*;
import static com.github.hummel.wikigen.util.ReflectionHelper.*;

@SuppressWarnings("StreamToLoop")
public class WikiGenerator {
	public static final Map<Class<? extends Entity>, Entity> CLASS_TO_ENTITY = new HashMap<>();
	public static final Map<Class<? extends Entity>, String> CLASS_TO_ENTITY_NAME = new HashMap<>();

	public static final Map<Class<?>, String> CLASS_TO_STRUCTURE_NAME = new HashMap<>();
	public static final Map<Class<?>, Set<String>> CLASS_TO_VILLAGE_NAMES = new HashMap<>();

	public static final Set<Class<? extends Entity>> ENTITIES = new HashSet<>();

	private static final Map<String, String> FACTION_TO_PAGE = new HashMap<>();
	private static final Map<String, String> ENTITY_TO_PAGE = new HashMap<>();
	private static final Map<String, String> BIOME_TO_PAGE = new HashMap<>();

	private static final Collection<Item> ITEMS = new ArrayList<>(getObjectFieldsOfType(LOTRMod.class, Item.class));
	private static final Collection<LOTRUnitTradeEntries> UNIT_TRADE_ENTRIES = new ArrayList<>(getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class));
	private static final Collection<LOTRAchievement> ACHIEVEMENTS = new HashSet<>(getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class));
	private static final Collection<LOTRBiome> BIOMES = new HashSet<>(getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class));

	private static final Collection<LOTRFaction> FACTIONS = EnumSet.allOf(LOTRFaction.class);
	private static final Collection<LOTRTreeType> TREES = EnumSet.allOf(LOTRTreeType.class);
	private static final Collection<LOTRWaypoint> WAYPOINTS = EnumSet.allOf(LOTRWaypoint.class);
	private static final Collection<LOTRShields> SHIELDS = EnumSet.allOf(LOTRShields.class);

	private static final Collection<String> MINERALS = new HashSet<>();
	private static final Collection<Class<?>> STRUCTURES = new HashSet<>();
	private static final Collection<Class<? extends Entity>> HIREABLE = new HashSet<>();

	private static final String BEGIN = "</title>\n<ns>10</ns>\n<revision>\n<text>&lt;includeonly&gt;{{#switch: {{{1}}}";
	private static final String END = "\n}}&lt;/includeonly&gt;&lt;noinclude&gt;[[" + Lang.CATEGORY + "]]&lt;/noinclude&gt;</text>\n</revision>\n</page>\n";
	private static final String TITLE = "<page>\n<title>";
	private static final String TITLE_SINGLE = "<page><title>";
	private static final String PAGE_LEFT = "</title><revision><text>";
	private static final String PAGE_RIGHT = "</text></revision></page>\n";
	private static final String UTF_8 = "UTF-8";
	private static final String TEMPLATE = "Template:";
	private static final String NL = "\n";
	private static final String TRUE = "True";

	static {
		BIOMES.remove(LOTRBiome.ocean);
		BIOMES.remove(LOTRBiome.beach);
		BIOMES.remove(LOTRBiome.beachGravel);
		BIOMES.remove(LOTRBiome.beachWhite);
		BIOMES.remove(LOTRBiome.lake);
		BIOMES.remove(LOTRBiome.river);
		BIOMES.remove(LOTRBiome.island);

		TREES.remove(LOTRTreeType.NULL);

		ITEMS.removeAll(Collections.singleton(null));
		UNIT_TRADE_ENTRIES.removeAll(Collections.singleton(null));
		ACHIEVEMENTS.removeAll(Collections.singleton(null));
		BIOMES.removeAll(Collections.singleton(null));
	}

	private WikiGenerator() {
	}

	public static void generate(String type, World world, EntityPlayer entityPlayer) {
		long time = System.nanoTime();
		try {
			Config.world = world;
			Config.authorizeEntityInfo();
			Config.authorizeStructureInfo();

			Files.createDirectories(Paths.get("hummel"));

			Collection<Runnable> pRunnables = new HashSet<>();

			pRunnables.add(() -> searchForEntities(world));
			pRunnables.add(() -> searchForMinerals(BIOMES, MINERALS));
			pRunnables.add(() -> searchForStructures(BIOMES, STRUCTURES));
			pRunnables.add(() -> searchForHireable(HIREABLE, UNIT_TRADE_ENTRIES));
			pRunnables.add(() -> searchForPagenamesEntity(BIOMES, FACTIONS));
			pRunnables.add(() -> searchForPagenamesBiome(BIOMES, FACTIONS));
			pRunnables.add(() -> searchForPagenamesFaction(BIOMES, FACTIONS));

			pRunnables.parallelStream().forEach(Runnable::run);

			if ("tables".equalsIgnoreCase(type)) {
				Collection<Runnable> runnables = new HashSet<>();

				runnables.add(() -> genTableAchievements(entityPlayer));
				runnables.add(WikiGenerator::genTableShields);
				runnables.add(WikiGenerator::genTableUnits);
				runnables.add(WikiGenerator::genTableArmor);
				runnables.add(WikiGenerator::genTableWeapons);
				runnables.add(WikiGenerator::genTableFood);

				runnables.add(() -> genTableWaypoints(entityPlayer));

				runnables.parallelStream().forEach(Runnable::run);

			} else if ("xml".equalsIgnoreCase(type)) {
				StringBuilder xmlBuilder = new StringBuilder();

				LOTRDate.Season season = LOTRDate.ShireReckoning.getShireDate().month.season;

				xmlBuilder.append("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"ru\">");

				Collection<Supplier<StringBuilder>> suppliers = new HashSet<>();
				Collection<StringBuilder> sbs = new HashSet<>();

				Set<String> existingPages = getExistingPages();
				Collection<String> neededPages = new HashSet<>();

				suppliers.add(() -> addPagesMinerals(neededPages, existingPages));
				suppliers.add(() -> addPagesEntities(neededPages, existingPages));
				suppliers.add(() -> addPagesBiomes(neededPages, existingPages));
				suppliers.add(() -> addPagesFactions(neededPages, existingPages));
				suppliers.add(() -> addPagesTrees(neededPages, existingPages));
				suppliers.add(() -> addPagesStructures(neededPages, existingPages));

				suppliers.parallelStream().map(Supplier::get).forEach(sbs::add);
				sbs.forEach(xmlBuilder::append);

				suppliers.clear();
				sbs.clear();

				markPagesForRemoval(neededPages, existingPages);

				suppliers.add(WikiGenerator::genTemplateStructureBiomes);
				suppliers.add(WikiGenerator::genTemplateMineralBiomes);
				suppliers.add(WikiGenerator::genTemplateTreeBiomes);

				suppliers.add(WikiGenerator::genTemplateBiomeBandits);
				suppliers.add(WikiGenerator::genTemplateBiomeConquestFactions);
				suppliers.add(WikiGenerator::genTemplateBiomeInvasionFactions);
				suppliers.add(WikiGenerator::genTemplateBiomeMinerals);
				suppliers.add(WikiGenerator::genTemplateBiomeMobs);
				suppliers.add(WikiGenerator::genTemplateBiomeMusic);
				suppliers.add(WikiGenerator::genTemplateBiomeName);
				suppliers.add(WikiGenerator::genTemplateBiomeRainfall);
				suppliers.add(WikiGenerator::genTemplateBiomeSpawnNPCs);
				suppliers.add(WikiGenerator::genTemplateBiomeStructuresSettlements);
				suppliers.add(WikiGenerator::genTemplateBiomeTemperature);
				suppliers.add(WikiGenerator::genTemplateBiomeTrees);
				suppliers.add(WikiGenerator::genTemplateBiomeVariants);
				suppliers.add(WikiGenerator::genTemplateBiomeWaypoints);

				suppliers.add(() -> genTemplateBiomeVisitAchievement(entityPlayer));

				suppliers.add(WikiGenerator::genTemplateFactionBanners);
				suppliers.add(WikiGenerator::genTemplateFactionCodename);
				suppliers.add(WikiGenerator::genTemplateFactionConquestBiomes);
				suppliers.add(WikiGenerator::genTemplateFactionEnemies);
				suppliers.add(WikiGenerator::genTemplateFactionFriends);
				suppliers.add(WikiGenerator::genTemplateFactionInvasionBiomes);
				suppliers.add(WikiGenerator::genTemplateFactionNPCs);
				suppliers.add(WikiGenerator::genTemplateFactionName);
				suppliers.add(WikiGenerator::genTemplateFactionPledgeRank);
				suppliers.add(WikiGenerator::genTemplateFactionRanks);
				suppliers.add(WikiGenerator::genTemplateFactionRegion);
				suppliers.add(WikiGenerator::genTemplateFactionShields);
				suppliers.add(WikiGenerator::genTemplateFactionSpawnBiomes);
				suppliers.add(WikiGenerator::genTemplateFactionWarCrimes);
				suppliers.add(WikiGenerator::genTemplateFactionWaypoints);

				suppliers.add(WikiGenerator::genTemplateEntityBannerBearer);
				suppliers.add(WikiGenerator::genTemplateEntityBuys);
				suppliers.add(WikiGenerator::genTemplateEntityFaction);
				suppliers.add(WikiGenerator::genTemplateEntityFarmhand);
				suppliers.add(WikiGenerator::genTemplateEntityHealth);
				suppliers.add(WikiGenerator::genTemplateEntityHireAlignment);
				suppliers.add(WikiGenerator::genTemplateEntityHirePrice);
				suppliers.add(WikiGenerator::genTemplateEntityHirePricePledge);
				suppliers.add(WikiGenerator::genTemplateEntityImmuneToFire);
				suppliers.add(WikiGenerator::genTemplateEntityImmuneToFrost);
				suppliers.add(WikiGenerator::genTemplateEntityImmuneToHeat);
				suppliers.add(WikiGenerator::genTemplateEntityKillAlignment);
				suppliers.add(WikiGenerator::genTemplateEntityMarriage);
				suppliers.add(WikiGenerator::genTemplateEntityMercenary);
				suppliers.add(WikiGenerator::genTemplateEntityOwner);
				suppliers.add(WikiGenerator::genTemplateEntityRideableMob);
				suppliers.add(WikiGenerator::genTemplateEntityRideableNPC);
				suppliers.add(WikiGenerator::genTemplateEntitySells);
				suppliers.add(WikiGenerator::genTemplateEntitySellsUnits);
				suppliers.add(WikiGenerator::genTemplateEntitySmith);
				suppliers.add(WikiGenerator::genTemplateEntitySpawn);
				suppliers.add(WikiGenerator::genTemplateEntitySpawnsInDarkness);
				suppliers.add(WikiGenerator::genTemplateEntityTargetSeeker);
				suppliers.add(WikiGenerator::genTemplateEntityTradeable);
				suppliers.add(WikiGenerator::genTemplateEntityUnitTradeable);

				suppliers.add(() -> genTemplateEntityKillAchievement(entityPlayer));

				// структуры - лут
				// структуры - мобы
				// структуры - поселения

				// мобы - структуры
				// мобы - поселения

				// поселения - мобы
				// поселения - структуры
				// поселения - лут
				// поселения - биомы

				suppliers.parallelStream().map(Supplier::get).forEach(sbs::add);
				sbs.forEach(xmlBuilder::append);

				suppliers.clear();
				sbs.clear();

				xmlBuilder.append("</mediawiki>");

				LOTRDate.ShireReckoning.getShireDate().month.season = season;

				PrintWriter xml = new PrintWriter("hummel/import.xml", UTF_8);
				xml.write(xmlBuilder.toString());
				xml.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long newTime = System.nanoTime();

		//noinspection StringConcatenationMissingWhitespace
		IChatComponent component = new ChatComponentText("Generated databases in " + (newTime - time) / 1.0E9 + 's');
		entityPlayer.addChatMessage(component);
	}

	private static void genTableAchievements(EntityPlayer entityPlayer) {
		try {
			StringBuilder sb = new StringBuilder();

			List<String> sortable = new ArrayList<>();

			for (LOTRAchievement ach : ACHIEVEMENTS) {
				if (!(ach instanceof LOTRAchievementRank)) {
					sortable.add(NL + "| " + ach.getTitle(entityPlayer) + " || " + ach.getDescription(entityPlayer) + NL + "|-");
				}
			}

			appendSortedList(sb, sortable);

			PrintWriter printWriter = new PrintWriter("hummel/achievements.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableArmor() {
		try {
			StringBuilder sb = new StringBuilder();

			for (Item item : ITEMS) {
				if (item instanceof ItemArmor) {
					float damage = ((ItemArmor) item).damageReduceAmount;
					ItemArmor.ArmorMaterial material = ((ItemArmor) item).getArmorMaterial();

					sb.append(NL).append("| ");
					sb.append(getItemName(item)).append(" || ").append(getItemFilename(item)).append(" || ").append(item.getMaxDamage()).append(" || ").append(damage).append(" || ");
					if (material == null || material.customCraftingMaterial == null) {
						sb.append("N/A");
					} else {
						sb.append(getItemName(material.customCraftingMaterial));
					}
					sb.append(NL).append("|-");
				}
			}
			PrintWriter printWriter = new PrintWriter("hummel/armor.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static void genTableFood() {
		try {
			StringBuilder sb = new StringBuilder();

			List<String> sortable = new ArrayList<>();

			for (Item item : ITEMS) {
				if (item instanceof ItemFood) {
					StringBuilder localSb = new StringBuilder();

					int heal = ((ItemFood) item).func_150905_g(null);
					float saturation = ((ItemFood) item).func_150906_h(null);
					localSb.append(NL).append("| ");
					localSb.append(getItemName(item));
					localSb.append(" || ").append(getItemFilename(item));
					localSb.append(" || ").append("{{Bar|bread|").append(new DecimalFormat("#.##").format(saturation * heal * 2)).append("}}");
					localSb.append(" || ").append("{{Bar|food|").append(heal).append("}}");
					localSb.append(" || ").append(item.getItemStackLimit());
					localSb.append(NL).append("|-");

					sortable.add(localSb.toString());
				}
			}

			appendSortedList(sb, sortable);

			PrintWriter printWriter = new PrintWriter("hummel/food.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableShields() {
		try {
			StringBuilder sb = new StringBuilder();

			for (LOTRShields shield : SHIELDS) {
				sb.append(NL).append("| ");
				sb.append(shield.getShieldName()).append(" || ").append(shield.getShieldDesc()).append(" || ").append(getShieldFilename(shield)).append(NL).append("|-");
			}
			PrintWriter printWriter = new PrintWriter("hummel/shields.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableUnits() {
		try {
			StringBuilder sb = new StringBuilder();

			for (LOTRUnitTradeEntries unitTradeEntries : UNIT_TRADE_ENTRIES) {
				for (LOTRUnitTradeEntry entry : unitTradeEntries.tradeEntries) {
					if (entry != null) {
						sb.append(NL).append("| ");
						sb.append(getEntityLink(entry.entityClass));
						if (entry.mountClass != null) {
							sb.append(Lang.RIDER);
						}

						int cost = getInitialCost(entry);
						int alignment = (int) entry.alignmentRequired;

						if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
							sb.append(" || ").append("{{Coins|").append(cost * 2).append("}}");
							sb.append(" || ").append("{{Coins|").append(cost).append("}}");
							sb.append(" || ").append('+').append(alignment);
							sb.append(" || ").append('-');
						} else {
							sb.append(" || ").append("N/A");
							sb.append(" || ").append("{{Coins|").append(cost).append("}}");
							sb.append(" || ").append('+').append(Math.max(alignment, 100));
							sb.append(" || ").append('+');
						}
						sb.append(NL).append("|-");
					}
				}
			}
			PrintWriter printWriter = new PrintWriter("hummel/units.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableWaypoints(EntityPlayer entityPlayer) {
		try {
			StringBuilder sb = new StringBuilder();

			List<String> sortable = new ArrayList<>();

			for (LOTRWaypoint wp : WAYPOINTS) {
				sortable.add(NL + "| " + wp.getDisplayName() + " || " + wp.getLoreText(entityPlayer) + NL + "|-");
			}

			appendSortedList(sb, sortable);

			PrintWriter printWriter = new PrintWriter("hummel/waypoints.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableWeapons() {
		try {
			StringBuilder sb = new StringBuilder();

			List<String> sortable = new ArrayList<>();

			for (Item item : ITEMS) {
				if (item instanceof ItemSword) {
					StringBuilder localSb = new StringBuilder();

					float damage = getDamageAmount(item);
					Item.ToolMaterial material = getToolMaterial(item);

					localSb.append(NL).append("| ");
					localSb.append(getItemName(item)).append(" || ").append(getItemFilename(item)).append(" || ").append(item.getMaxDamage()).append(" || ").append(damage).append(" || ");
					if (material == null || material.getRepairItemStack() == null) {
						localSb.append("N/A");
					} else {
						localSb.append(getItemName(material.getRepairItemStack().getItem()));
					}
					localSb.append(NL).append("|-");

					sortable.add(localSb.toString());
				}
			}

			appendSortedList(sb, sortable);

			PrintWriter printWriter = new PrintWriter("hummel/weapon.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static StringBuilder genTemplateBiomeBandits() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Bandits");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append(NL).append("| ").append(getBiomePagename(biome)).append(" = ").append(biome.getBanditChance());
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeConquestFactions() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-ConquestNPC");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			List<LOTRBiomeSpawnList.FactionContainer> facContainers = getFactionContainers(biome.npcSpawnList);
			if (facContainers.isEmpty()) {
				sb.append(NL).append("| ");
				sb.append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_NO_CONQUEST);
			} else {
				Collection<LOTRBiomeSpawnList.FactionContainer> conqestContainers = new HashSet<>();
				for (LOTRBiomeSpawnList.FactionContainer facContainer : facContainers) {
					if (getBaseWeight(facContainer) <= 0) {
						conqestContainers.add(facContainer);
					}
				}
				sb.append(NL).append("| ");
				sb.append(getBiomePagename(biome)).append(" = ");
				if (conqestContainers.isEmpty()) {
					sb.append(Lang.BIOME_SPAWN_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_CONQUEST);
					Collection<LOTRFaction> conquestFactions = EnumSet.noneOf(LOTRFaction.class);
					for (LOTRBiomeSpawnList.FactionContainer facContainer : conqestContainers) {
						next:
						for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
							for (LOTRSpawnEntry entry : getSpawnEntries(getSpawnList(container))) {
								Entity entity = CLASS_TO_ENTITY.get(entry.entityClass);
								if (entity instanceof LOTREntityNPC) {
									LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
									conquestFactions.add(fac);
									continue next;
								}
							}
						}
					}
					for (LOTRFaction fac : conquestFactions) {
						sortable.add(NL + "* " + getFactionLink(fac) + ';');
					}
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeInvasionFactions() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Invasions");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
				sb.append(Lang.BIOME_NO_INVASIONS);
			} else {
				sb.append(Lang.BIOME_HAS_INVASIONS);
				Collection<LOTRFaction> invasionFactions = EnumSet.noneOf(LOTRFaction.class);
				next:
				for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
					for (LOTRInvasions.InvasionSpawnEntry entry : invasion.invasionMobs) {
						Entity entity = CLASS_TO_ENTITY.get(entry.getEntityClass());
						if (entity instanceof LOTREntityNPC) {
							LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
							invasionFactions.add(fac);
							continue next;
						}
					}
				}
				for (LOTRFaction fac : invasionFactions) {
					sortable.add(NL + "* " + getFactionLink(fac) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeMinerals() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Minerals");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_HAS_MINERALS);
			Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
			oreGenerants.addAll(getBiomeOres(biome.decorator));
			oreGenerants.addAll(getBiomeGems(biome.decorator));
			for (Object oreGenerant : oreGenerants) {
				Block block = getOreGenBlock(getOreGen(oreGenerant));
				int meta = getOreGenMeta(getOreGen(oreGenerant));

				String stats = " (" + getOreChance(oreGenerant) + "%; Y: " + getMinHeight(oreGenerant) + '-' + getMaxHeight(oreGenerant) + ");";

				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					sortable.add(NL + "* [[" + getBlockMetaName(block, meta) + "]]" + stats);
				} else {
					sortable.add(NL + "* [[" + getBlockName(block) + "]]" + stats);
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeMobs() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Mobs");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			Collection<BiomeGenBase.SpawnListEntry> entries = new HashSet<BiomeGenBase.SpawnListEntry>(biome.getSpawnableList(EnumCreatureType.ambient));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
			entries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (entries.isEmpty()) {
				sb.append(Lang.BIOME_NO_ANIMALS);
			} else {
				sb.append(Lang.BIOME_HAS_ANIMALS);
				for (BiomeGenBase.SpawnListEntry entry : entries) {
					if (CLASS_TO_ENTITY_NAME.containsKey(entry.entityClass)) {
						sortable.add(NL + "* " + getEntityLink(entry.entityClass) + ';');
					} else {
						sortable.add(NL + "* " + getEntityVanillaName(entry.entityClass) + ';');
					}
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeMusic() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Music");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (biome.getBiomeMusic() == null) {
				sb.append("N/A");
			} else {
				sb.append(biome.getBiomeMusic().subregion);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeName() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Name");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ").append(getBiomeName(biome));
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeRainfall() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Rainfall");
		sb.append(BEGIN);


		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(biome.rainfall);
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeSpawnNPCs() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-SpawnNPC");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			List<LOTRBiomeSpawnList.FactionContainer> facContainers = getFactionContainers(biome.npcSpawnList);
			if (facContainers.isEmpty()) {
				sb.append(NL).append("| ");
				sb.append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_NO_SPAWN);
			} else {
				Collection<LOTRBiomeSpawnList.FactionContainer> spawnContainers = new HashSet<>();
				for (LOTRBiomeSpawnList.FactionContainer facContainer : facContainers) {
					if (getBaseWeight(facContainer) > 0) {
						spawnContainers.add(facContainer);
					}
				}
				sb.append(NL).append("| ");
				sb.append(getBiomePagename(biome)).append(" = ");
				if (spawnContainers.isEmpty()) {
					sb.append(Lang.BIOME_CONQUEST_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_SPAWN);
					for (LOTRBiomeSpawnList.FactionContainer facContainer : spawnContainers) {
						for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
							for (LOTRSpawnEntry entry : getSpawnEntries(getSpawnList(container))) {
								sortable.add(NL + "* " + getEntityLink(entry.entityClass) + ';');
							}
						}
					}
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeStructuresSettlements() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Structures");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (getStructures(biome.decorator).isEmpty()) {
				sb.append(Lang.BIOME_NO_STRUCTURES);
			} else {
				sb.append(Lang.BIOME_HAS_STRUCTURES);

				for (Object structure : getStructures(biome.decorator)) {
					sortable.add(NL + "* [[" + getStructureName(getStructureGen(structure).getClass()) + "]];");
				}

				appendSortedList(sb, sortable);

				for (LOTRVillageGen settlement : getVillages(biome.decorator)) {
					if (getSpawnChance(settlement) != 0.0f) {
						Set<String> names = CLASS_TO_VILLAGE_NAMES.get(settlement.getClass());
						if (names != null) {
							for (String name : names) {
								sortable.add(NL + "* " + getSettlementName(name) + ';');
							}
						}
					}
				}

				appendSortedList(sb, sortable);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeTemperature() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Temperature");
		sb.append(BEGIN);

		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(biome.temperature);
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeTrees() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Trees");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRTreeType> trees = EnumSet.noneOf(LOTRTreeType.class);
			Map<LOTRTreeType, LOTRBiomeVariant> additionalTrees = new EnumMap<>(LOTRTreeType.class);
			for (LOTRTreeType.WeightedTreeType weightedTreeType : getTreeTypes(biome.decorator)) {
				trees.add(weightedTreeType.treeType);
			}
			for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
				for (LOTRTreeType.WeightedTreeType weightedTreeType : getVariant(variantBucket).treeTypes) {
					if (!trees.contains(weightedTreeType.treeType)) {
						additionalTrees.put(weightedTreeType.treeType, getVariant(variantBucket));
					}
				}
			}
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (trees.isEmpty() && additionalTrees.isEmpty()) {
				sb.append(Lang.BIOME_NO_TREES);
			} else {
				if (additionalTrees.isEmpty()) {
					sb.append(Lang.BIOME_HAS_TREES_BIOME_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_TREES);
				}
				for (LOTRTreeType tree : trees) {
					sortable.add(NL + "* [[" + getTreeName(tree) + "]];");
				}

				appendSortedList(sb, sortable);

				for (Map.Entry<LOTRTreeType, LOTRBiomeVariant> tree : additionalTrees.entrySet()) {
					sortable.add(NL + "* [[" + getTreeName(tree.getKey()) + "]] (" + getBiomeVariantName(tree.getValue()).toLowerCase(Locale.ROOT) + ");");
				}

				appendSortedList(sb, sortable);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeVariants() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Variants");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (getVariantList(biome.getBiomeVariantsSmall()).isEmpty()) {
				sb.append(Lang.BIOME_NO_VARIANTS);
			} else {
				for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
					sortable.add(NL + "* " + getBiomeVariantName(getVariant(variantBucket)) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeVisitAchievement(EntityPlayer entityPlayer) {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Achievement");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			LOTRAchievement ach = biome.getBiomeAchievement();
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (ach == null) {
				sb.append(Lang.BIOME_NO_ACHIEVEMENT);
			} else {
				sb.append('«').append(ach.getTitle(entityPlayer)).append('»');
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeWaypoints() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Biome-Waypoints");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<String> sortable = new ArrayList<>();

			LOTRWaypoint.Region region = biome.getBiomeWaypoints();
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(biome)).append(" = ");
			if (region == null) {
				sb.append(Lang.BIOME_NO_WAYPOINTS);
			} else {
				sb.append(Lang.BIOME_HAS_WAYPOINTS);
				for (LOTRWaypoint wp : WAYPOINTS) {
					if (getRegion(wp).contains(region)) {
						sortable.add(NL + "* " + wp.getDisplayName() + " (" + getFactionLink(wp.faction) + ");");
					}
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityBannerBearer() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-BannerBearer");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBannerBearer) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityBuys() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Buys");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				List<String> sortable = new ArrayList<>();

				LOTRTradeEntries entries = ((LOTRTradeable) entityEntry.getValue()).getSellPool();
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ");
				for (LOTRTradeEntry entry : entries.tradeEntries) {
					sortable.add(NL + "* " + entry.createTradeItem().getDisplayName() + ": {{Coins|" + entry.getCost() + "}};");
				}

				appendSortedList(sb, sortable);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityFaction() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Faction");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTRFaction fac = ((LOTREntityNPC) entityEntry.getValue()).getFaction();
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(getFactionLink(fac));
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityFarmhand() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Farmhand");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRFarmhand) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHealth() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Health");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			EntityLivingBase entity = (EntityLivingBase) entityEntry.getValue();
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append((int) entity.getMaxHealth());
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHireAlignment() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Alignment");
		sb.append(BEGIN);
		next:
		for (Class<? extends Entity> entityClass : HIREABLE) {
			for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
				for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
					if (entry.entityClass == entityClass) {
						sb.append(NL).append("| ");
						sb.append(getEntityPagename(entityClass)).append(" = ");

						int alignment = (int) entry.alignmentRequired;

						if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
							sb.append('+').append(alignment);
						} else {
							sb.append('+').append(Math.max(alignment, 100));
						}
						continue next;
					}
				}
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHirePrice() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Price");
		sb.append(BEGIN);
		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entry.entityClass)).append(" = ");

				int cost = getInitialCost(entry);

				if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
					sb.append("{{Coins|").append(cost * 2).append("}}");
				} else {
					sb.append("N/A");
				}
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHirePricePledge() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-PricePledge");
		sb.append(BEGIN);
		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entry.entityClass)).append(" = ");

				int cost = getInitialCost(entry);

				sb.append("{{Coins|").append(cost).append("}}");
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToFire() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-ImmuneToFire");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue().isImmuneToFire()) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToFrost() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-ImmuneToFrost");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && ((LOTREntityNPC) entityEntry.getValue()).isImmuneToFrost) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToHeat() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-ImmuneToHeat");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBiomeGenNearHarad.ImmuneToHeat) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityKillAchievement(EntityPlayer entityPlayer) {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Achievement");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTRAchievement ach = getKillAchievement((LOTREntityNPC) entityEntry.getValue());
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ");
				if (ach == null) {
					sb.append("N/A");
				} else {
					sb.append('«').append(ach.getTitle(entityPlayer)).append('»');
				}
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityKillAlignment() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Bonus");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				int bonus = (int) ((LOTREntityNPC) entityEntry.getValue()).getAlignmentBonus();
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append('+').append(bonus);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityMarriage() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Marriage");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue().getClass() == LOTREntityDwarf.class || entityEntry.getValue().getClass() == LOTREntityHobbit.class) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityMercenary() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Mercenary");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRMercenary) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityOwner() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append("DB Mob-Owner");
		sb.append(BEGIN);
		for (Class<? extends Entity> entityClass : HIREABLE) {
			Map<Class<? extends Entity>, Class<? extends Entity>> owners = new HashMap<>();
			loop:
			for (Map.Entry<Class<? extends Entity>, Entity> ownerEntry : CLASS_TO_ENTITY.entrySet()) {
				if (ownerEntry.getValue() instanceof LOTRUnitTradeable) {
					LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) ownerEntry.getValue()).getUnits();
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						if (entry.entityClass == entityClass) {
							owners.put(entityClass, ownerEntry.getKey());
							break loop;
						}
					}
				}
			}
			if (!owners.isEmpty()) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityClass)).append(" = ").append(getEntityLink(owners.get(entityClass)));
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityRideableMob() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Rideable2");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRNPCMount) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityRideableNPC() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append(" DB Mob-Rideable1");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntitySpiderBase) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySells() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Sells");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				List<String> sortable = new ArrayList<>();

				LOTRTradeEntries entries = ((LOTRTradeable) entityEntry.getValue()).getBuyPool();
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ");
				for (LOTRTradeEntry entry : entries.tradeEntries) {
					sortable.add(NL + "* " + entry.createTradeItem().getDisplayName() + ": {{Coins|" + entry.getCost() + "}};");
				}

				appendSortedList(sb, sortable);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySellsUnits() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Units");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> ownerEntry : CLASS_TO_ENTITY.entrySet()) {
			if (ownerEntry.getValue() instanceof LOTRUnitTradeable) {
				LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) ownerEntry.getValue()).getUnits();
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(ownerEntry.getKey())).append(" = ");
				for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
					sb.append(NL).append("* ").append(getEntityLink(entry.entityClass));
					if (entry.mountClass != null) {
						sb.append(Lang.RIDER);
					}
					sb.append(": ");

					int cost = getInitialCost(entry);
					int alignment = (int) entry.alignmentRequired;

					if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
						sb.append("{{Coins|").append(cost * 2).append("}} ").append(Lang.NO_PLEDGE).append(", ");
						sb.append("{{Coins|").append(cost).append("}} ").append(Lang.NEED_PLEDGE).append("; ");
						sb.append('+').append(alignment).append(Lang.REPUTATION).append(';');
					} else {
						sb.append("N/A ").append(Lang.NO_PLEDGE).append(", ");
						sb.append("{{Coins|").append(cost).append("}} ").append(Lang.NEED_PLEDGE).append("; ");
						sb.append('+').append(Math.max(alignment, 100)).append(Lang.REPUTATION).append(';');
					}
				}
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySmith() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Smith");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable.Smith) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySpawn() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Spawn");
		sb.append(BEGIN);
		for (Class<? extends Entity> entityClass : ENTITIES) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRBiome> spawnBiomes = new HashSet<>();
			Collection<LOTRBiome> conquestBiomes = new HashSet<>();
			Collection<LOTRBiome> invasionBiomes = new HashSet<>();
			Collection<LOTRBiome> conquestIvasionBiomes = new HashSet<>();
			next:
			for (LOTRBiome biome : BIOMES) {
				Collection<BiomeGenBase.SpawnListEntry> spawnEntries = new HashSet<>();
				Collection<BiomeGenBase.SpawnListEntry> conquestEntries = new HashSet<>();
				Collection<LOTRInvasions.InvasionSpawnEntry> invasionEntries = new HashSet<>();
				spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.ambient));
				spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
				spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
				spawnEntries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
				spawnEntries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
				for (LOTRBiomeSpawnList.FactionContainer facContainer : getFactionContainers(biome.npcSpawnList)) {
					if (getBaseWeight(facContainer) > 0) {
						for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
							spawnEntries.addAll(getSpawnEntries(getSpawnList(container)));
						}
					} else {
						for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
							conquestEntries.addAll(getSpawnEntries(getSpawnList(container)));
						}
					}
				}
				for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
					invasionEntries.addAll(invasion.invasionMobs);
				}
				for (BiomeGenBase.SpawnListEntry entry : spawnEntries) {
					if (entry.entityClass == entityClass) {
						spawnBiomes.add(biome);
						continue next;
					}
				}
				for (BiomeGenBase.SpawnListEntry entry : conquestEntries) {
					if (entry.entityClass == entityClass) {
						conquestBiomes.add(biome);
						conquestIvasionBiomes.add(biome);
						break;
					}
				}
				for (LOTRInvasions.InvasionSpawnEntry entry : invasionEntries) {
					if (entry.getEntityClass() == entityClass) {
						invasionBiomes.add(biome);
						conquestIvasionBiomes.add(biome);
						break;
					}
				}
			}
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entityClass)).append(" = ");

			if (Stream.of(spawnBiomes, conquestBiomes, invasionBiomes).allMatch(Collection::isEmpty)) {
				sb.append(Lang.ENTITY_NO_BIOMES);
			} else {
				sb.append(Lang.ENTITY_HAS_BIOMES);
				for (LOTRBiome biome : spawnBiomes) {
					sortable.add(NL + "* " + getBiomeLink(biome) + ';');
				}

				appendSortedList(sb, sortable);

				for (LOTRBiome biome : conquestBiomes) {
					if (!invasionBiomes.contains(biome)) {
						sortable.add(NL + "* " + getBiomeLink(biome) + ' ' + Lang.ENTITY_CONQUEST + ';');
					}
				}

				appendSortedList(sb, sortable);

				for (LOTRBiome biome : invasionBiomes) {
					if (!conquestBiomes.contains(biome)) {
						sortable.add(NL + "* " + getBiomeLink(biome) + ' ' + Lang.ENTITY_INVASION + ';');
					}
				}

				appendSortedList(sb, sortable);

				for (LOTRBiome biome : conquestIvasionBiomes) {
					if (conquestBiomes.contains(biome) && invasionBiomes.contains(biome)) {
						sortable.add(NL + "* " + getBiomeLink(biome) + ' ' + Lang.ENTITY_CONQUEST_INVASION + ';');
					}
				}

				appendSortedList(sb, sortable);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySpawnsInDarkness() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-SpawnsInDarkness");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && isSpawnsInDarkness((LOTREntityNPC) entityEntry.getValue())) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityTargetSeeker() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-TargetSeeker");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && isTargetSeeker((LOTREntityNPC) entityEntry.getValue())) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityTradeable() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-Tradeable");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityUnitTradeable() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mob-UnitTradeable");
		sb.append(BEGIN);
		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRUnitTradeable) {
				sb.append(NL).append("| ");
				sb.append(getEntityPagename(entityEntry.getKey())).append(" = ").append(TRUE);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionBanners() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Banners");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (fac.factionBanners.isEmpty()) {
				sb.append(Lang.FACTION_NO_BANNERS);
			} else {
				sb.append(Lang.FACTION_HAS_BANNERS);
				for (LOTRItemBanner.BannerType banner : fac.factionBanners) {
					sortable.add(NL + "* " + getBannerName(banner) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionCodename() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Codename");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ").append(fac.codeName());
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionConquestBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Conquest");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRBiome> conquestBiomes = new HashSet<>();
			next:
			for (LOTRBiome biome : BIOMES) {
				List<LOTRBiomeSpawnList.FactionContainer> facContainers = getFactionContainers(biome.npcSpawnList);
				if (!facContainers.isEmpty()) {
					Collection<LOTRBiomeSpawnList.FactionContainer> conquestContainers = new HashSet<>();
					for (LOTRBiomeSpawnList.FactionContainer facContainer : facContainers) {
						if (getBaseWeight(facContainer) <= 0) {
							conquestContainers.add(facContainer);
						}
					}
					if (!conquestContainers.isEmpty()) {
						for (LOTRBiomeSpawnList.FactionContainer facContainer : conquestContainers) {
							for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
								for (LOTRSpawnEntry entry : getSpawnEntries(getSpawnList(container))) {
									Entity entity = CLASS_TO_ENTITY.get(entry.entityClass);
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
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (conquestBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_CONQUEST);
			} else {
				sb.append(Lang.FACTION_HAS_CONQUEST);
				for (LOTRBiome biome : conquestBiomes) {
					sortable.add(NL + "* " + getBiomeLink(biome) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionEnemies() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Enemies");
		sb.append(BEGIN);
		for (LOTRFaction fac1 : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRFaction> facEnemies = EnumSet.noneOf(LOTRFaction.class);
			for (LOTRFaction fac2 : FACTIONS) {
				if (fac1.isBadRelation(fac2) && fac1 != fac2) {
					facEnemies.add(fac2);
				}
			}
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac1)).append(" = ");
			if (facEnemies.isEmpty()) {
				sb.append(Lang.FACTION_NO_ENEMIES);
			} else {
				for (LOTRFaction fac : facEnemies) {
					sortable.add(getFactionLink(fac));
				}

				Collections.sort(sortable);

				StringJoiner sj = new StringJoiner(" • ");
				for (String item : sortable) {
					sj.add(item);
				}

				sb.append(sj);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionFriends() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Friends");
		sb.append(BEGIN);
		for (LOTRFaction fac1 : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRFaction> facFriends = EnumSet.noneOf(LOTRFaction.class);
			for (LOTRFaction fac2 : FACTIONS) {
				if (fac1.isGoodRelation(fac2) && fac1 != fac2) {
					facFriends.add(fac2);
				}
			}
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac1)).append(" = ");
			if (facFriends.isEmpty()) {
				sb.append(Lang.FACTION_NO_FRIENDS);
			} else {
				for (LOTRFaction fac : facFriends) {
					sortable.add(getFactionLink(fac));
				}

				Collections.sort(sortable);

				StringJoiner sj = new StringJoiner(" • ");
				for (String item : sortable) {
					sj.add(item);
				}

				sb.append(sj);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionInvasionBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Invasions");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRBiome> invasionBiomes = new HashSet<>();
			next:
			for (LOTRBiome biome : BIOMES) {
				for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
					for (LOTRInvasions.InvasionSpawnEntry entry : invasion.invasionMobs) {
						Entity entity = CLASS_TO_ENTITY.get(entry.getEntityClass());
						if (entity instanceof LOTREntityNPC && fac == ((LOTREntityNPC) entity).getFaction()) {
							invasionBiomes.add(biome);
							continue next;
						}
					}
				}
			}
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (invasionBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_INVASIONS);
			} else {
				sb.append(Lang.FACTION_HAS_INVASION);
				for (LOTRBiome biome : invasionBiomes) {
					sortable.add(NL + "* " + getBiomeLink(biome) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionName() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Name");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ").append(getFactionName(fac));
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionNPCs() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-NPC");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY.entrySet()) {
				Entity entity = entityEntry.getValue();
				if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
					sortable.add(NL + "* " + getEntityLink(entityEntry.getKey()) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionPledgeRank() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Pledge");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			LOTRFactionRank rank = fac.getPledgeRank();
			if (rank != null) {
				sb.append(NL).append("| ");
				sb.append(getFactionPagename(fac)).append(" = ").append(rank.getDisplayName());

				String femRank = rank.getDisplayFullNameFem();
				if (!femRank.contains("lotr")) {
					sb.append('/').append(femRank);
				}

				sb.append(" (+").append((int) fac.getPledgeAlignment()).append(')');
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionRanks() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Ranks");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (getRanksSortedDescending(fac).isEmpty()) {
				sb.append(Lang.FACTION_NO_RANKS);
			} else {
				sb.append(Lang.FACTION_HAS_RANKS);
				for (LOTRFactionRank rank : getRanksSortedDescending(fac)) {
					sb.append(NL).append("* ").append(rank.getDisplayFullName());

					String femRank = rank.getDisplayFullNameFem();
					if (!femRank.contains("lotr")) {
						sb.append('/').append(femRank);
					}

					sb.append(" (+").append((int) rank.alignment).append(");");
				}
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionRegion() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Region");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (fac.factionRegion == null) {
				sb.append("N/A");
			} else {
				sb.append(fac.factionRegion.getRegionName());
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionShields() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Shields");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRShields> facShields = EnumSet.noneOf(LOTRShields.class);
			for (LOTRShields shield : SHIELDS) {
				if (getAlignmentFaction(shield) == fac) {
					facShields.add(shield);
				}
			}

			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (facShields.isEmpty()) {
				sb.append(Lang.FACTION_NO_ATTRIBUTES);
			} else {
				sb.append(NL).append("&lt;table class=\"wikitable shields\"&gt;");

				for (LOTRShields shield : facShields) {
					sortable.add(NL + "&lt;tr&gt;&lt;td&gt;" + shield.getShieldName() + "&lt;/td&gt;&lt;td&gt;" + shield.getShieldDesc() + "&lt;/td&gt;&lt;td&gt;" + getShieldFilename(shield) + "&lt;/td&gt;&lt;/tr&gt;");
				}

				appendSortedList(sb, sortable);

				sb.append(NL).append("&lt;table&gt;");
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionSpawnBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Spawn");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRBiome> spawnBiomes = new HashSet<>();
			next:
			for (LOTRBiome biome : BIOMES) {
				List<LOTRBiomeSpawnList.FactionContainer> facContainers = getFactionContainers(biome.npcSpawnList);
				if (!facContainers.isEmpty()) {
					Collection<LOTRBiomeSpawnList.FactionContainer> spawnContainers = new HashSet<>();
					for (LOTRBiomeSpawnList.FactionContainer facContainer : facContainers) {
						if (getBaseWeight(facContainer) > 0) {
							spawnContainers.add(facContainer);
						}
					}
					if (!spawnContainers.isEmpty()) {
						for (LOTRBiomeSpawnList.FactionContainer facContainer : spawnContainers) {
							for (LOTRBiomeSpawnList.SpawnListContainer container : getSpawnLists(facContainer)) {
								for (LOTRSpawnEntry entry : getSpawnEntries(getSpawnList(container))) {
									Entity entity = CLASS_TO_ENTITY.get(entry.entityClass);
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
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (spawnBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_SPAWN);
			} else {
				sb.append(Lang.FACTION_HAS_SPAWN);
				for (LOTRBiome biome : spawnBiomes) {
					sortable.add(NL + "* " + getBiomeLink(biome) + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionWarCrimes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-WarCrimes");
		sb.append(BEGIN);
		sb.append(NL).append("| #default = ").append(Lang.FACTION_NO_WAR_CRIMES);
		for (LOTRFaction fac : FACTIONS) {
			if (fac.approvesWarCrimes) {
				sb.append(NL).append("| ");
				sb.append(getFactionPagename(fac)).append(" = ").append(Lang.FACTION_HAS_WAR_CRIMES);
			}
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionWaypoints() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Faction-Waypoints");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRWaypoint> facWaypoints = EnumSet.noneOf(LOTRWaypoint.class);
			for (LOTRWaypoint wp : WAYPOINTS) {
				if (wp.faction == fac) {
					facWaypoints.add(wp);
				}
			}
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(fac)).append(" = ");
			if (facWaypoints.isEmpty()) {
				sb.append(Lang.FACTION_NO_WAYPOINTS);
			} else {
				sb.append(Lang.FACTION_HAS_WAYPOINTS);
				for (LOTRWaypoint wp : facWaypoints) {
					sortable.add(NL + "* " + wp.getDisplayName() + ';');
				}
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateMineralBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Mineral-Biomes");
		sb.append(BEGIN);

		for (String mineral : MINERALS) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(mineral).append(" = ").append(Lang.MINERAL_BIOMES);
			for (LOTRBiome biome : BIOMES) {
				Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
				oreGenerants.addAll(getBiomeOres(biome.decorator));
				oreGenerants.addAll(getBiomeGems(biome.decorator));
				for (Object oreGenerant : oreGenerants) {
					Block block = getOreGenBlock(getOreGen(oreGenerant));
					int meta = getOreGenMeta(getOreGen(oreGenerant));
					if (getBlockMetaName(block, meta).equals(mineral) || getBlockName(block).equals(mineral)) {
						sortable.add(NL + "* " + getBiomeLink(biome) + " (" + getOreChance(oreGenerant) + "%; Y: " + getMinHeight(oreGenerant) + '-' + getMaxHeight(oreGenerant) + ");");
						break;
					}
				}
			}

			appendSortedList(sb, sortable);
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateStructureBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Structure-Biomes");
		sb.append(BEGIN);

		for (Class<?> strClass : STRUCTURES) {
			List<String> sortable = new ArrayList<>();

			sb.append(NL).append("| ");
			sb.append(getStructureName(strClass)).append(" = ").append(Lang.STRUCTURE_BIOMES);
			for (LOTRBiome biome : BIOMES) {
				for (Object structure : getStructures(biome.decorator)) {
					if (getStructureGen(structure).getClass() == strClass) {
						sortable.add(NL + "* " + getBiomeLink(biome) + ';');
						break;
					}
				}
			}

			appendSortedList(sb, sortable);
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateTreeBiomes() {
		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("DB Tree-Biomes");
		sb.append(BEGIN);

		for (LOTRTreeType tree : TREES) {
			List<String> sortable = new ArrayList<>();

			Collection<LOTRBiome> biomesTree = new HashSet<>();
			Collection<LOTRBiome> biomesVariantTree = new HashSet<>();
			next:
			for (LOTRBiome biome : BIOMES) {
				for (LOTRTreeType.WeightedTreeType weightedTreeType : getTreeTypes(biome.decorator)) {
					if (weightedTreeType.treeType == tree) {
						biomesTree.add(biome);
						continue next;
					}
				}
				for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
					for (LOTRTreeType.WeightedTreeType weightedTreeType : getVariant(variantBucket).treeTypes) {
						if (weightedTreeType.treeType == tree) {
							biomesVariantTree.add(biome);
							continue next;
						}
					}
				}
			}
			sb.append(NL).append("| ");
			sb.append(getTreeName(tree)).append(" = ");
			if (biomesTree.isEmpty() && biomesVariantTree.isEmpty()) {
				sb.append(Lang.TREE_NO_BIOMES);
			} else {
				sb.append(Lang.TREE_HAS_BIOMES);
			}
			for (LOTRBiome biome : biomesTree) {
				sortable.add(NL + "* " + getBiomeLink(biome) + ';');
			}

			appendSortedList(sb, sortable);

			for (LOTRBiome biome : biomesVariantTree) {
				sortable.add(NL + "* " + getBiomeLink(biome) + " (" + Lang.TREE_VARIANT_ONLY + ");");
			}

			appendSortedList(sb, sortable);
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder addPagesBiomes(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (LOTRBiome biome : BIOMES) {
			String pageName = getBiomePagename(biome);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Биом}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesEntities(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (Class<? extends Entity> entityClass : ENTITIES) {
			String pageName = getEntityPagename(entityClass);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Моб}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesFactions(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (LOTRFaction faction : FACTIONS) {
			String pageName = getFactionPagename(faction);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Фракция}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesMinerals(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (String pageName : MINERALS) {
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Ископаемое}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesStructures(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (Class<?> strClass : STRUCTURES) {
			String pageName = getStructureName(strClass);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Структура}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesTrees(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (LOTRTreeType tree : TREES) {
			String pageName = getTreeName(tree);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				String page = PAGE_LEFT + "{{Статья Дерево}}" + PAGE_RIGHT;
				sb.append(TITLE_SINGLE).append(pageName).append(page);
			}
		}

		return sb;
	}

	private static Set<String> getExistingPages() {
		try {
			File file = new File("hummel/sitemap.txt");
			if (!file.exists()) {
				boolean created = file.createNewFile();
				if (!created) {
					LOTRLog.logger.info("DatabaseGenerator: file wasn't created");
				}
			}
			try (Stream<String> lines = Files.lines(Paths.get("hummel/sitemap.txt"))) {
				return lines.collect(Collectors.toSet());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Collections.emptySet();
	}

	private static void markPagesForRemoval(Collection<String> neededPages, Iterable<String> existingPages) {
		try {
			StringBuilder sb = new StringBuilder();

			for (String existing : existingPages) {
				if (!neededPages.contains(existing)) {
					sb.append(existing).append('\n');
				}
			}
			PrintWriter printWriter = new PrintWriter("hummel/removal.txt", UTF_8);
			printWriter.write(sb.toString());
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchForEntities(World world) {
		for (Class<? extends Entity> entityClass : ENTITIES) {
			CLASS_TO_ENTITY.put(entityClass, newEntity(entityClass, world));
		}
	}

	private static void searchForHireable(Collection<Class<? extends Entity>> hireable, Iterable<LOTRUnitTradeEntries> units) {
		for (LOTRUnitTradeEntries entries : units) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				hireable.add(entry.entityClass);
			}
		}
	}

	private static void searchForMinerals(Iterable<LOTRBiome> biomes, Collection<String> minerals) {
		for (LOTRBiome biome : biomes) {
			Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
			oreGenerants.addAll(getBiomeOres(biome.decorator));
			oreGenerants.addAll(getBiomeGems(biome.decorator));
			for (Object oreGenerant : oreGenerants) {
				WorldGenMinable gen = getOreGen(oreGenerant);
				Block block = getOreGenBlock(gen);
				int meta = getOreGenMeta(gen);
				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					minerals.add(getBlockMetaName(block, meta));
				} else {
					minerals.add(getBlockName(block));
				}
			}
		}
	}

	private static void searchForStructures(Iterable<LOTRBiome> biomes, Collection<Class<?>> structures) {
		for (LOTRBiome biome : biomes) {
			for (Object structure : getStructures(biome.decorator)) {
				structures.add(getStructureGen(structure).getClass());
			}
		}
	}

	private static void searchForPagenamesBiome(Iterable<LOTRBiome> biomes, Iterable<LOTRFaction> factions) {
		next:
		for (LOTRBiome biome : biomes) {
			String preName = getBiomeName(biome);
			for (LOTRFaction fac : factions) {
				if (preName.equals(getFactionName(fac))) {
					BIOME_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_BIOME + ')');
					continue next;
				}
			}
			for (Class<? extends Entity> entityClass : ENTITIES) {
				if (preName.equals(getEntityName(entityClass))) {
					BIOME_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_BIOME + ')');
					continue next;
				}
			}
			BIOME_TO_PAGE.put(preName, preName);
		}
	}

	private static void searchForPagenamesEntity(Iterable<LOTRBiome> biomes, Iterable<LOTRFaction> factions) {
		next:
		for (Class<? extends Entity> entityClass : ENTITIES) {
			String preName = getEntityName(entityClass);
			for (LOTRBiome biome : biomes) {
				if (preName.equals(getBiomeName(biome))) {
					ENTITY_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_ENTITY + ')');
					continue next;
				}
			}
			for (LOTRFaction fac : factions) {
				if (preName.equals(getFactionName(fac))) {
					ENTITY_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_ENTITY + ')');
					continue next;
				}
			}
			ENTITY_TO_PAGE.put(preName, preName);
		}
	}

	private static void searchForPagenamesFaction(Iterable<LOTRBiome> biomes, Iterable<LOTRFaction> factions) {
		next:
		for (LOTRFaction fac : factions) {
			String preName = getFactionName(fac);
			for (LOTRBiome biome : biomes) {
				if (preName.equals(getBiomeName(biome))) {
					FACTION_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			for (Class<? extends Entity> entityClass : ENTITIES) {
				if (preName.equals(getEntityName(entityClass))) {
					FACTION_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			FACTION_TO_PAGE.put(preName, preName);
		}
	}

	public enum Lang {
		MOB_NO_LEGENDARY_DROP, PAGE_BIOME, PAGE_FACTION, PAGE_ENTITY, BIOME_HAS_ANIMALS, BIOME_HAS_CONQUEST, BIOME_HAS_INVASIONS, BIOME_HAS_SPAWN, BIOME_HAS_STRUCTURES, BIOME_HAS_TREES, BIOME_HAS_TREES_BIOME_ONLY, BIOME_HAS_WAYPOINTS, BIOME_NO_ACHIEVEMENT, BIOME_NO_ANIMALS, BIOME_NO_CONQUEST, BIOME_NO_INVASIONS, BIOME_NO_SPAWN, BIOME_NO_STRUCTURES, BIOME_NO_TREES, BIOME_NO_VARIANTS, BIOME_NO_WAYPOINTS, BIOME_HAS_MINERALS, BIOME_CONQUEST_ONLY, BIOME_SPAWN_ONLY, FACTION_HAS_BANNERS, FACTION_HAS_CHARS, FACTION_HAS_CONQUEST, FACTION_HAS_INVASION, FACTION_HAS_RANKS, FACTION_HAS_SPAWN, FACTION_HAS_WAR_CRIMES, FACTION_HAS_WAYPOINTS, FACTION_NO_ATTRIBUTES, FACTION_NO_BANNERS, FACTION_NO_CHARS, FACTION_NO_CONQUEST, FACTION_NO_ENEMIES, FACTION_NO_FRIENDS, FACTION_NO_INVASIONS, FACTION_NO_RANKS, FACTION_NO_SPAWN, FACTION_NO_STRUCTURES, FACTION_NO_WAR_CRIMES, FACTION_NO_WAYPOINTS, TREE_HAS_BIOMES, TREE_NO_BIOMES, TREE_VARIANT_ONLY, RIDER, NO_PLEDGE, NEED_PLEDGE, REPUTATION, MINERAL_BIOMES, STRUCTURE_BIOMES, ENTITY_NO_BIOMES, ENTITY_HAS_BIOMES, ENTITY_CONQUEST, ENTITY_INVASION, ENTITY_CONQUEST_INVASION, CATEGORY, CLIMATE_COLD, CLIMATE_COLD_AZ, CLIMATE_NORMAL, CLIMATE_NORMAL_AZ, CLIMATE_SUMMER, CLIMATE_SUMMER_AZ, CLIMATE_WINTER, CLIMATE_NULL, SEASON_WINTER, SEASON_AUTUMN, SEASON_SUMMER, SEASON_SPRING;

		@Override
		public String toString() {
			return StatCollector.translateToLocal("lotr.db." + name() + ".name");
		}
	}

	public enum Type {
		XML("xml"), TABLES("tables");

		private final String codeName;

		Type(String name) {
			codeName = name;
		}

		public static Type forName(String name) {
			for (Type db : values()) {
				if (db.codeName.equals(name)) {
					return db;
				}
			}
			return null;
		}

		public static Set<String> getNames() {
			Set<String> names = new HashSet<>();
			for (Type db : values()) {
				names.add(db.codeName);
			}
			return names;
		}
	}

	@SuppressWarnings({"ProtectedInnerClass", "WeakerAccess"})
	protected static class Utils {
		private Utils() {
		}

		protected static String getBannerName(LOTRItemBanner.BannerType banner) {
			return StatCollector.translateToLocal("item.lotr:banner." + banner.bannerName + ".name");
		}

		protected static String getBiomeLink(LOTRBiome biome) {
			String biomeName = getBiomeName(biome);
			String biomePagename = getBiomePagename(biome);
			if (biomeName.equals(biomePagename)) {
				return "[[" + biomeName + "]]";
			}
			return "[[" + biomePagename + '|' + biomeName + "]]";
		}

		protected static String getBiomeName(LOTRBiome biome) {
			return StatCollector.translateToLocal("lotr.biome." + biome.biomeName + ".name");
		}

		protected static String getBiomePagename(LOTRBiome biome) {
			return BIOME_TO_PAGE.get(getBiomeName(biome));
		}

		protected static String getBiomeVariantName(LOTRBiomeVariant variant) {
			return StatCollector.translateToLocal("lotr.variant." + variant.variantName + ".name");
		}

		protected static String getBlockMetaName(Block block, int meta) {
			return StatCollector.translateToLocal(block.getUnlocalizedName() + '.' + meta + ".name");
		}

		protected static String getBlockName(Block block) {
			return StatCollector.translateToLocal(block.getUnlocalizedName() + ".name");
		}

		protected static String getEntityLink(Class<? extends Entity> entityClass) {
			String entityName = getEntityName(entityClass);
			String entityPagename = getEntityPagename(entityClass);
			if (entityName.equals(entityPagename)) {
				return "[[" + entityPagename + "]]";
			}
			return "[[" + entityPagename + '|' + entityName + "]]";
		}

		protected static String getEntityName(Class<? extends Entity> entityClass) {
			return StatCollector.translateToLocal("entity.lotr." + CLASS_TO_ENTITY_NAME.get(entityClass) + ".name");
		}

		protected static String getEntityPagename(Class<? extends Entity> entityClass) {
			return ENTITY_TO_PAGE.get(getEntityName(entityClass));
		}

		protected static String getEntityVanillaName(Class<? extends Entity> entityClass) {
			return StatCollector.translateToLocal("entity." + EntityList.classToStringMapping.get(entityClass) + ".name");
		}

		protected static String getFactionLink(LOTRFaction fac) {
			String facName = getFactionName(fac);
			String facPagename = getFactionPagename(fac);
			if (facName.equals(facPagename)) {
				return "[[" + facPagename + "]]";
			}
			return "[[" + facPagename + '|' + facName + "]]";
		}

		protected static String getFactionName(LOTRFaction fac) {
			return StatCollector.translateToLocal("lotr.faction." + fac.codeName() + ".name");
		}

		protected static String getFactionPagename(LOTRFaction fac) {
			return FACTION_TO_PAGE.get(getFactionName(fac));
		}

		protected static String getItemFilename(Item item) {
			return "[[File:" + item.getUnlocalizedName().substring("item.lotr:".length()) + ".png|32px|link=]]";
		}

		protected static String getItemName(Item item) {
			return StatCollector.translateToLocal(item.getUnlocalizedName() + ".name");
		}

		protected static String getShieldFilename(LOTRShields shield) {
			return "[[File:Shield " + shield.name().toLowerCase(Locale.ROOT) + ".png]]";
		}

		protected static String getStructureName(Class<?> structureClass) {
			return StatCollector.translateToLocal("lotr.structure." + CLASS_TO_STRUCTURE_NAME.get(structureClass) + ".name");
		}

		protected static String getSettlementName(String nameAlias) {
			return StatCollector.translateToLocal("lotr.structure." + nameAlias + ".name");
		}

		protected static String getTreeName(LOTRTreeType tree) {
			return StatCollector.translateToLocal("lotr.tree." + tree.name().toLowerCase(Locale.ROOT) + ".name");
		}

		protected static void appendSortedList(StringBuilder sb, List<String> sortable) {
			Collections.sort(sortable);

			for (String item : sortable) {
				sb.append(item);
			}

			sortable.clear();
		}
	}
}