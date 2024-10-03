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
import net.minecraft.world.gen.feature.WorldGenerator;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.hummel.wikigen.util.ReflectionHelper.*;

public class WikiGenerator {
	public static final Map<Class<? extends Entity>, Entity> ENTITY_CLASS_TO_ENTITY = new HashMap<>();

	public static final Collection<Class<? extends Entity>> ENTITY_CLASSES = new HashSet<>();

	private static final Map<LOTRFaction, String> FACTION_TO_PAGENAME = new EnumMap<>(LOTRFaction.class);
	private static final Map<Class<? extends Entity>, String> ENTITY_CLASS_TO_PAGENAME = new HashMap<>();
	private static final Map<LOTRBiome, String> BIOME_TO_PAGENAME = new HashMap<>();

	private static final Collection<Item> ITEMS = new ArrayList<>(getObjectFieldsOfType(LOTRMod.class, Item.class));
	private static final Collection<LOTRUnitTradeEntries> UNIT_TRADE_ENTRIES = new ArrayList<>(getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class));
	private static final Collection<LOTRAchievement> ACHIEVEMENTS = new HashSet<>(getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class));

	private static final Collection<LOTRBiome> BIOMES = new HashSet<>(getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class));

	private static final Collection<LOTRTreeType> TREES = EnumSet.allOf(LOTRTreeType.class);

	private static final Iterable<LOTRFaction> FACTIONS = EnumSet.allOf(LOTRFaction.class);
	private static final Iterable<LOTRWaypoint> WAYPOINTS = EnumSet.allOf(LOTRWaypoint.class);
	private static final Iterable<LOTRShields> SHIELDS = EnumSet.allOf(LOTRShields.class);

	private static final Collection<String> MINERALS = new HashSet<>();
	private static final Collection<Class<? extends WorldGenerator>> STRUCTURE_CLASSES = new HashSet<>();

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
	private static final String FALSE = "False";
	private static final String N_A = "N/A";

	static {
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

	@SuppressWarnings("JoinDeclarationAndAssignmentJava")
	public static void generate(Type type, World world, EntityPlayer entityPlayer) {
		long time = System.nanoTime();

		try {
			Files.createDirectories(Paths.get("hummel"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Config.world = world;
		Config.authorizeEntityInfo();
		Config.authorizeStructureInfo();

		switch (type) {
			case TABLES:
				Collection<Runnable> tableGens = new HashSet<>();

				tableGens.add(WikiGenerator::genTableShields);
				tableGens.add(WikiGenerator::genTableArmor);
				tableGens.add(WikiGenerator::genTableWeapons);
				tableGens.add(WikiGenerator::genTableFood);
				tableGens.add(() -> genTableAchievements(entityPlayer));

				tableGens.parallelStream().forEach(Runnable::run);

				break;
			case XML:
				try (PrintWriter printWriter = new PrintWriter("hummel/import.xml", UTF_8)) {
					StringBuilder sb = new StringBuilder();

					LOTRDate.Season season = LOTRDate.ShireReckoning.getSeason();

					sb.append("<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"ru\">\n");

					Collection<Runnable> runnables = new HashSet<>();

					runnables.add(WikiGenerator::searchForMinerals);
					runnables.add(WikiGenerator::searchForStructures);
					runnables.add(WikiGenerator::searchForPagenamesEntity);
					runnables.add(WikiGenerator::searchForPagenamesBiome);
					runnables.add(WikiGenerator::searchForPagenamesFaction);
					runnables.add(() -> searchForEntities(world));

					runnables.parallelStream().forEach(Runnable::run);

					Collection<Supplier<StringBuilder>> suppliers = new HashSet<>();
					List<StringBuilder> results;

					Set<String> existingPages = getExistingPages();
					Collection<String> neededPages = new HashSet<>();

					suppliers.add(() -> addPagesMinerals(neededPages, existingPages));
					suppliers.add(() -> addPagesEntities(neededPages, existingPages));
					suppliers.add(() -> addPagesBiomes(neededPages, existingPages));
					suppliers.add(() -> addPagesFactions(neededPages, existingPages));
					suppliers.add(() -> addPagesTrees(neededPages, existingPages));
					suppliers.add(() -> addPagesStructures(neededPages, existingPages));

					results = suppliers.parallelStream().map(Supplier::get).collect(Collectors.toList());
					suppliers.clear();

					for (StringBuilder stringBuilder : results) {
						sb.append(stringBuilder);
					}

					results.clear();

					markPagesForRemoval(neededPages, existingPages);

					suppliers.add(WikiGenerator::genTemplateMineralBiomes);
					suppliers.add(WikiGenerator::genTemplateTreeBiomes);

					suppliers.add(WikiGenerator::genTemplateStructureBiomes);

					suppliers.add(WikiGenerator::genTemplateBiomeAnimals);
					suppliers.add(WikiGenerator::genTemplateBiomeBandits);
					suppliers.add(WikiGenerator::genTemplateBiomeConquestFactions);
					suppliers.add(WikiGenerator::genTemplateBiomeInvasionFactions);
					suppliers.add(WikiGenerator::genTemplateBiomeMinerals);
					suppliers.add(WikiGenerator::genTemplateBiomeMusic);
					suppliers.add(WikiGenerator::genTemplateBiomeNPCs);
					suppliers.add(WikiGenerator::genTemplateBiomeName);
					suppliers.add(WikiGenerator::genTemplateBiomeRainfall);
					suppliers.add(WikiGenerator::genTemplateBiomeStructures);
					suppliers.add(WikiGenerator::genTemplateBiomeTemperature);
					suppliers.add(WikiGenerator::genTemplateBiomeTrees);
					suppliers.add(WikiGenerator::genTemplateBiomeVariants);
					suppliers.add(WikiGenerator::genTemplateBiomeVisitAchievement);
					suppliers.add(WikiGenerator::genTemplateBiomeWaypoints);

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
					suppliers.add(WikiGenerator::genTemplateFactionShieldsCapes);
					suppliers.add(WikiGenerator::genTemplateFactionSpawnBiomes);
					suppliers.add(WikiGenerator::genTemplateFactionWarCrimes);
					suppliers.add(WikiGenerator::genTemplateFactionWaypoints);

					suppliers.add(WikiGenerator::genTemplateEntityBannerBearer);
					suppliers.add(WikiGenerator::genTemplateEntityBiomes);
					suppliers.add(WikiGenerator::genTemplateEntityBuysPool);
					suppliers.add(WikiGenerator::genTemplateEntityFaction);
					suppliers.add(WikiGenerator::genTemplateEntityFarmhand);
					suppliers.add(WikiGenerator::genTemplateEntityHealth);
					suppliers.add(WikiGenerator::genTemplateEntityHireAlignment);
					suppliers.add(WikiGenerator::genTemplateEntityHirePrice);
					suppliers.add(WikiGenerator::genTemplateEntityHirePricePledge);
					suppliers.add(WikiGenerator::genTemplateEntityHireable);
					suppliers.add(WikiGenerator::genTemplateEntityImmuneToFire);
					suppliers.add(WikiGenerator::genTemplateEntityImmuneToFrost);
					suppliers.add(WikiGenerator::genTemplateEntityImmuneToHeat);
					suppliers.add(WikiGenerator::genTemplateEntityKillAchievement);
					suppliers.add(WikiGenerator::genTemplateEntityKillAlignment);
					suppliers.add(WikiGenerator::genTemplateEntityMarriage);
					suppliers.add(WikiGenerator::genTemplateEntityMercenary);
					suppliers.add(WikiGenerator::genTemplateEntityNPC);
					suppliers.add(WikiGenerator::genTemplateEntityOwners);
					suppliers.add(WikiGenerator::genTemplateEntityRideableAnimal);
					suppliers.add(WikiGenerator::genTemplateEntityRideableNPC);
					suppliers.add(WikiGenerator::genTemplateEntitySellsPool);
					suppliers.add(WikiGenerator::genTemplateEntitySellsUnitPool);
					suppliers.add(WikiGenerator::genTemplateEntitySmith);
					suppliers.add(WikiGenerator::genTemplateEntitySpawnsInDarkness);
					suppliers.add(WikiGenerator::genTemplateEntityTargetSeeker);
					suppliers.add(WikiGenerator::genTemplateEntityTradeable);
					suppliers.add(WikiGenerator::genTemplateEntityUnitTradeable);

					results = suppliers.parallelStream().map(Supplier::get).collect(Collectors.toList());
					suppliers.clear();

					for (StringBuilder stringBuilder : results) {
						sb.append(stringBuilder);
					}

					results.clear();

					sb.append("</mediawiki>");

					LOTRDate.ShireReckoning.getShireDate().month.season = season;

					printWriter.write(sb.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;
		}

		long newTime = System.nanoTime();

		IChatComponent component = new ChatComponentText("Generated in " + (newTime - time) / 1.0E6 + " milliseconds");
		entityPlayer.addChatMessage(component);
	}

	@SuppressWarnings("StringBufferReplaceableByString")
	private static void genTableAchievements(EntityPlayer entityPlayer) {
		Collection<String> data = new TreeSet<>();

		for (LOTRAchievement achievement : ACHIEVEMENTS) {
			if (!(achievement instanceof LOTRAchievementRank)) {
				StringBuilder sb = new StringBuilder();

				sb.append(NL).append("| ");
				sb.append(achievement.getTitle(entityPlayer));
				sb.append(" || ").append(achievement.getDescription(null));
				sb.append(NL).append("|-");

				data.add(sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String datum : data) {
			sb.append(datum);
		}

		try (PrintWriter printWriter = new PrintWriter("hummel/achievements.txt", UTF_8)) {
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableArmor() {
		Collection<String> data = new ArrayList<>();

		for (Item item : ITEMS) {
			if (item instanceof ItemArmor) {
				StringBuilder sb = new StringBuilder();

				DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
				symbols.setDecimalSeparator('.');
				DecimalFormat decimalFormat = new DecimalFormat("#.#", symbols);

				float damage = ((ItemArmor) item).damageReduceAmount;
				ItemArmor.ArmorMaterial material = ((ItemArmor) item).getArmorMaterial();

				sb.append(NL).append("| ");
				sb.append(getItemName(item));
				sb.append(" || ").append(getItemFilename(item));
				sb.append(" || ").append(decimalFormat.format(item.getMaxDamage()));
				sb.append(" || ").append("{{Bar Armor|").append(decimalFormat.format(damage)).append("}}");

				sb.append(" || ");
				if (material == null || material.customCraftingMaterial == null) {
					sb.append(N_A);
				} else {
					sb.append(getItemName(material.customCraftingMaterial));
				}

				sb.append(NL).append("|-");

				data.add(sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String datum : data) {
			sb.append(datum);
		}

		try (PrintWriter printWriter = new PrintWriter("hummel/armor.txt", UTF_8)) {
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private static void genTableFood() {
		Collection<String> data = new TreeSet<>();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		symbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#.#", symbols);

		for (Item item : ITEMS) {
			if (item instanceof ItemFood) {
				StringBuilder sb = new StringBuilder();

				int heal = ((ItemFood) item).func_150905_g(null);
				float saturation = ((ItemFood) item).func_150906_h(null);

				sb.append(NL).append("| ");
				sb.append(getItemName(item));
				sb.append(" || ").append(getItemFilename(item));
				sb.append(" || ").append("{{Bar Bread|").append(decimalFormat.format(saturation * heal * 2)).append("}}");
				sb.append(" || ").append("{{Bar Food|").append(decimalFormat.format(heal)).append("}}");
				sb.append(" || ").append(decimalFormat.format(item.getItemStackLimit()));
				sb.append(NL).append("|-");

				data.add(sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String datum : data) {
			sb.append(datum);
		}

		try (PrintWriter printWriter = new PrintWriter("hummel/food.txt", UTF_8)) {
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("StringBufferReplaceableByString")
	private static void genTableShields() {
		Collection<String> data = new ArrayList<>();

		for (LOTRShields shield : SHIELDS) {
			StringBuilder sb = new StringBuilder();

			sb.append(NL).append("| ");
			sb.append(shield.getShieldName());
			sb.append(" || ").append(shield.getShieldDesc());
			sb.append(" || ").append(getShieldFilename(shield));
			sb.append(NL).append("|-");

			data.add(sb.toString());
		}

		StringBuilder sb = new StringBuilder();

		for (String datum : data) {
			sb.append(datum);
		}

		try (PrintWriter printWriter = new PrintWriter("hummel/shields.txt", UTF_8)) {
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void genTableWeapons() {
		Collection<String> data = new TreeSet<>();

		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		symbols.setDecimalSeparator('.');
		DecimalFormat decimalFormat = new DecimalFormat("#.#", symbols);

		for (Item item : ITEMS) {
			if (item instanceof ItemSword) {
				StringBuilder sb = new StringBuilder();

				float damage = getDamageAmount(item);
				Item.ToolMaterial material = getToolMaterial(item);

				sb.append(NL).append("| ");
				sb.append(getItemName(item));
				sb.append(" || ").append(getItemFilename(item));
				sb.append(" || ").append(decimalFormat.format(item.getMaxDamage()));
				sb.append(" || ").append("{{Bar Hearts|").append(decimalFormat.format(damage)).append("}}");

				sb.append(" || ");
				if (material == null || material.getRepairItemStack() == null) {
					sb.append(N_A);
				} else {
					sb.append(getItemName(material.getRepairItemStack().getItem()));
				}

				sb.append(NL).append("|-");

				data.add(sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String datum : data) {
			sb.append(datum);
		}

		try (PrintWriter printWriter = new PrintWriter("hummel/weapon.txt", UTF_8)) {
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static StringBuilder genTemplateBiomeAnimals() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			Collection<BiomeGenBase.SpawnListEntry> spawnListEntries = new HashSet<BiomeGenBase.SpawnListEntry>(biome.getSpawnableList(EnumCreatureType.ambient));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
			spawnListEntries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));

			for (BiomeGenBase.SpawnListEntry spawnListEntry : spawnListEntries) {
				data.computeIfAbsent(biome, s -> new TreeSet<>());
				data.get(biome).add(getEntityLink(spawnListEntry.entityClass));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Animals");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_ANIMALS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_ANIMALS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeBandits() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			data.put(biome, biome.getBanditChance().toString());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Bandits");
		sb.append(BEGIN);

		appendDefault(sb, "UNKNOWN");

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeConquestFactions() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (LOTRBiomeSpawnList.FactionContainer factionContainer : getFactionContainers(biome.npcSpawnList)) {
				if (getBaseWeight(factionContainer) <= 0) {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						for (LOTRSpawnEntry spawnEntry : getSpawnEntries(getSpawnList(spawnListContainer))) {
							Entity entity = ENTITY_CLASS_TO_ENTITY.get(spawnEntry.entityClass);
							if (entity instanceof LOTREntityNPC) {
								LOTRFaction faction = ((LOTREntityNPC) entity).getFaction();
								data.computeIfAbsent(biome, s -> new TreeSet<>());
								data.get(biome).add(getFactionLink(faction));
								break;
							}
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-ConquestFactions");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_CONQUEST_FACTIONS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_CONQUEST_FACTIONS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeInvasionFactions() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
				for (LOTRInvasions.InvasionSpawnEntry invasionSpawnEntry : invasion.invasionMobs) {
					Entity entity = ENTITY_CLASS_TO_ENTITY.get(invasionSpawnEntry.getEntityClass());
					if (entity instanceof LOTREntityNPC) {
						LOTRFaction faction = ((LOTREntityNPC) entity).getFaction();
						data.computeIfAbsent(biome, s -> new TreeSet<>());
						data.get(biome).add(getFactionLink(faction));
						break;
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-InvasionFactions");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_INVASION_FACTIONS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_INVASION_FACTIONS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeMinerals() {
		Map<LOTRBiome, Set<MineralInfo>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
			oreGenerants.addAll(getBiomeOres(biome.decorator));
			oreGenerants.addAll(getBiomeGems(biome.decorator));

			for (Object oreGenerant : oreGenerants) {
				Block block = getOreGenBlock(getOreGen(oreGenerant));
				int meta = getOreGenMeta(getOreGen(oreGenerant));

				String blockLink;
				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					blockLink = getMineralLink(block, meta);
				} else {
					blockLink = getMineralLink(block);
				}

				String stats = "(" + getOreChance(oreGenerant) + "%; Y: " + getMinHeight(oreGenerant) + '-' + getMaxHeight(oreGenerant) + ')';

				data.computeIfAbsent(biome, s -> new TreeSet<>());
				data.get(biome).add(new MineralInfo(blockLink, ' ' + stats));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Minerals");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_MINERALS.toString());

		for (Map.Entry<LOTRBiome, Set<MineralInfo>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_MINERALS);

			appendSection(sb, MineralInfo.toStringSet(entry.getValue()));
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeMusic() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			if (biome.getBiomeMusic() != null) {
				data.put(biome, biome.getBiomeMusic().subregion);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Music");
		sb.append(BEGIN);

		appendDefault(sb, N_A);

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeName() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			data.put(biome, getBiomeName(biome));
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Name");
		sb.append(BEGIN);

		appendDefault(sb, "UNKNOWN");

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeNPCs() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (LOTRBiomeSpawnList.FactionContainer factionContainer : getFactionContainers(biome.npcSpawnList)) {
				if (getBaseWeight(factionContainer) > 0) {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						for (LOTRSpawnEntry spawnEntry : getSpawnEntries(getSpawnList(spawnListContainer))) {
							data.computeIfAbsent(biome, s -> new TreeSet<>());
							data.get(biome).add(getEntityLink(spawnEntry.entityClass));
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-NPCs");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_NPCS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_NPCS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeRainfall() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			data.put(biome, String.valueOf(biome.rainfall));
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Rainfall");
		sb.append(BEGIN);

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeStructures() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (Object structure : getStructures(biome.decorator)) {
				data.computeIfAbsent(biome, s -> new TreeSet<>());
				data.get(biome).add(getStructureLink(getStructureGen(structure).getClass()));
			}

			for (LOTRVillageGen settlement : getVillages(biome.decorator)) {
				if (getSpawnChance(settlement) != 0.0f) {
					data.computeIfAbsent(biome, s -> new TreeSet<>());
					data.get(biome).add(getSettlementName(settlement.getClass()));
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Structures");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_STRUCTURES.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_STRUCTURES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeTemperature() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			data.put(biome, String.valueOf(biome.temperature));
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Temperature");
		sb.append(BEGIN);

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeTrees() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			Collection<LOTRTreeType.WeightedTreeType> weightedTreeTypes = getTreeTypes(biome.decorator);

			Collection<LOTRTreeType> excludedTreeTypes = EnumSet.noneOf(LOTRTreeType.class);

			for (LOTRTreeType.WeightedTreeType weightedTreeType : weightedTreeTypes) {
				LOTRTreeType treeType = weightedTreeType.treeType;

				data.computeIfAbsent(biome, s -> new TreeSet<>());
				data.get(biome).add(getTreeLink(treeType));

				excludedTreeTypes.add(treeType);
			}

			for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
				for (LOTRTreeType.WeightedTreeType weightedTreeType : getVariant(variantBucket).treeTypes) {
					LOTRTreeType treeType = weightedTreeType.treeType;

					if (!excludedTreeTypes.contains(treeType)) {
						data.computeIfAbsent(biome, s -> new TreeSet<>());
						data.get(biome).add(getTreeLink(treeType) + " (" + getBiomeVariantName(getVariant(variantBucket)).toLowerCase(Locale.ROOT) + ')');
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Trees");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_TREES.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_TREES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeVariants() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
				data.computeIfAbsent(biome, s -> new TreeSet<>());
				data.get(biome).add(getBiomeVariantName(getVariant(variantBucket)));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Variants");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_VARIANTS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_VARIANTS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeVisitAchievement() {
		Map<LOTRBiome, String> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			LOTRAchievement achievement = biome.getBiomeAchievement();

			if (achievement != null) {
				data.put(biome, '«' + achievement.getTitle(null) + '»');
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-VisitAchievement");
		sb.append(BEGIN);

		appendDefault(sb, N_A);

		for (Map.Entry<LOTRBiome, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateBiomeWaypoints() {
		Map<LOTRBiome, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			LOTRWaypoint.Region region = biome.getBiomeWaypoints();

			if (region != null) {
				for (LOTRWaypoint wp : region.waypoints) {
					data.computeIfAbsent(biome, s -> new TreeSet<>());
					data.get(biome).add(wp.getDisplayName() + " (" + getFactionLink(wp.faction) + ')');
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Biome-Waypoints");
		sb.append(BEGIN);

		appendDefault(sb, Lang.BIOME_NO_WAYPOINTS.toString());

		for (Map.Entry<LOTRBiome, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getBiomePagename(entry.getKey())).append(" = ");
			sb.append(Lang.BIOME_HAS_WAYPOINTS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityBannerBearer() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBannerBearer) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-BannerBearer");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityBiomes() {
		Map<Class<? extends Entity>, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			Collection<BiomeGenBase.SpawnListEntry> spawnListEntries = new HashSet<>();
			Collection<Class<? extends Entity>> conquestEntityClasses = new HashSet<>();
			Collection<Class<? extends Entity>> invasionEntityClasses = new HashSet<>();

			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.ambient));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
			spawnListEntries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
			spawnListEntries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));

			for (LOTRBiomeSpawnList.FactionContainer factionContainer : getFactionContainers(biome.npcSpawnList)) {
				if (getBaseWeight(factionContainer) > 0) {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						spawnListEntries.addAll(getSpawnEntries(getSpawnList(spawnListContainer)));
					}
				} else {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						for (BiomeGenBase.SpawnListEntry spawnListEntry : getSpawnEntries(getSpawnList(spawnListContainer))) {
							conquestEntityClasses.add(spawnListEntry.entityClass);
						}
					}
				}
			}

			for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
				for (LOTRInvasions.InvasionSpawnEntry invasionSpawnEntry : invasion.invasionMobs) {
					invasionEntityClasses.add(invasionSpawnEntry.getEntityClass());
				}
			}

			Collection<Class<? extends Entity>> bothConquestInvasion = new HashSet<>(conquestEntityClasses);
			bothConquestInvasion.retainAll(invasionEntityClasses);

			conquestEntityClasses.removeAll(bothConquestInvasion);
			invasionEntityClasses.removeAll(bothConquestInvasion);

			for (BiomeGenBase.SpawnListEntry entry : spawnListEntries) {
				data.computeIfAbsent(entry.entityClass, s -> new TreeSet<>());
				data.get(entry.entityClass).add(getBiomeLink(biome));
			}

			for (Class<? extends Entity> entityClass : conquestEntityClasses) {
				data.computeIfAbsent(entityClass, s -> new TreeSet<>());
				data.get(entityClass).add(getBiomeLink(biome) + ' ' + Lang.ENTITY_CONQUEST);
			}

			for (Class<? extends Entity> entityClass : invasionEntityClasses) {
				data.computeIfAbsent(entityClass, s -> new TreeSet<>());
				data.get(entityClass).add(getBiomeLink(biome) + ' ' + Lang.ENTITY_INVASION);
			}

			for (Class<? extends Entity> entityClass : bothConquestInvasion) {
				data.computeIfAbsent(entityClass, s -> new TreeSet<>());
				data.get(entityClass).add(getBiomeLink(biome) + ' ' + Lang.ENTITY_CONQUEST_INVASION);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Biomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.ENTITY_NO_BIOMES.toString());

		for (Map.Entry<Class<? extends Entity>, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");
			sb.append(Lang.ENTITY_HAS_BIOMES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityBuysPool() {
		Map<Class<? extends Entity>, Set<String>> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				LOTRTradeable tradeable = (LOTRTradeable) entityEntry.getValue();

				for (LOTRTradeEntry entry : tradeable.getSellPool().tradeEntries) {
					try {
						data.computeIfAbsent(entityEntry.getKey(), s -> new TreeSet<>());
						data.get(entityEntry.getKey()).add(entry.createTradeItem().getDisplayName() + ": {{Bar Coins|" + entry.getCost() + "}}");
					} catch (Exception e) {
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-BuysPool");
		sb.append(BEGIN);

		appendDefault(sb, Lang.ENTITY_NO_BUYS_POOL.toString());

		for (Map.Entry<Class<? extends Entity>, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");
			sb.append(Lang.ENTITY_HAS_BUYS_POOL);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityFaction() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTREntityNPC npc = (LOTREntityNPC) entityEntry.getValue();
				data.put(entityEntry.getKey(), getFactionLink(npc.getFaction()));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Faction");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityFarmhand() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRFarmhand) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Farmhand");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHealth() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			EntityLivingBase entity = (EntityLivingBase) entityEntry.getValue();
			data.put(entityEntry.getKey(), String.valueOf((int) entity.getMaxHealth()));
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Health");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHireable() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				data.put(entry.entityClass, TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Hireable");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHireAlignment() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				int alignment = (int) entry.alignmentRequired;

				if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
					data.put(entry.entityClass, String.valueOf(alignment));
				} else {
					data.put(entry.entityClass, String.valueOf(Math.max(alignment, 100)));
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-HireAlignment");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHirePrice() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				int cost = getInitialCost(entry);

				if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
					data.put(entry.entityClass, "{{Bar Coins|" + cost * 2 + "}}");
				} else {
					data.put(entry.entityClass, N_A);
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-HirePrice");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityHirePricePledge() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (LOTRUnitTradeEntries entries : UNIT_TRADE_ENTRIES) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				int cost = getInitialCost(entry);

				data.put(entry.entityClass, "{{Bar Coins|" + cost + "}}");
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-HirePricePledge");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToFire() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue().isImmuneToFire()) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-ImmuneToFire");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToFrost() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && ((LOTREntityNPC) entityEntry.getValue()).isImmuneToFrost) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-ImmuneToFrost");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityImmuneToHeat() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBiomeGenNearHarad.ImmuneToHeat) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-ImmuneToHeat");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityKillAchievement() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTREntityNPC npc = (LOTREntityNPC) entityEntry.getValue();
				LOTRAchievement achievement = getKillAchievement(npc);
				if (achievement != null) {
					data.put(entityEntry.getKey(), '«' + achievement.getTitle(null) + '»');
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-KillAchievement");
		sb.append(BEGIN);

		appendDefault(sb, N_A);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityKillAlignment() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTREntityNPC npc = (LOTREntityNPC) entityEntry.getValue();
				data.put(entityEntry.getKey(), String.valueOf((int) npc.getAlignmentBonus()));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-KillAlignment");
		sb.append(BEGIN);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityMarriage() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityDwarf || entityEntry.getValue() instanceof LOTREntityHobbit) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Marriage");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityMercenary() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRMercenary) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Mercenary");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityNPC() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-NPC");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityOwners() {
		Map<Class<? extends Entity>, Set<String>> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRUnitTradeable) {
				LOTRUnitTradeable tradeable = (LOTRUnitTradeable) entityEntry.getValue();
				for (LOTRUnitTradeEntry entry : tradeable.getUnits().tradeEntries) {
					data.computeIfAbsent(entry.entityClass, s -> new TreeSet<>());
					data.get(entry.entityClass).add(getEntityLink(entityEntry.getKey()));
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append("LOTR+ DB Entity-Owners");
		sb.append(BEGIN);

		appendDefault(sb, Lang.ENTITY_NO_OWNERS.toString());

		for (Map.Entry<Class<? extends Entity>, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");
			sb.append(Lang.ENTITY_HAS_OWNERS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityRideableAnimal() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRNPCMount && !(entityEntry.getValue() instanceof LOTREntityNPC)) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-RideableAnimal");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityRideableNPC() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRNPCMount && entityEntry.getValue() instanceof LOTREntityNPC) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-RideableNPC");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySellsPool() {
		Map<Class<? extends Entity>, Set<String>> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				LOTRTradeable tradeable = (LOTRTradeable) entityEntry.getValue();

				for (LOTRTradeEntry entry : tradeable.getBuyPool().tradeEntries) {
					data.computeIfAbsent(entityEntry.getKey(), s -> new TreeSet<>());
					data.get(entityEntry.getKey()).add(entry.createTradeItem().getDisplayName() + ": {{Bar Coins|" + entry.getCost() + "}}");
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-SellsPool");
		sb.append(BEGIN);

		appendDefault(sb, Lang.ENTITY_NO_SELLS_POOL.toString());

		for (Map.Entry<Class<? extends Entity>, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");
			sb.append(Lang.ENTITY_HAS_SELLS_POOL);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySellsUnitPool() {
		Map<Class<? extends Entity>, List<String>> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRUnitTradeable) {
				LOTRUnitTradeable tradeable = (LOTRUnitTradeable) entityEntry.getValue();

				for (LOTRUnitTradeEntry entry : tradeable.getUnits().tradeEntries) {
					StringBuilder sb = new StringBuilder();

					sb.append(getEntityLink(entry.entityClass));
					if (entry.mountClass != null) {
						sb.append(Lang.RIDER);
					}
					sb.append(": ");

					int cost = getInitialCost(entry);
					int alignment = (int) entry.alignmentRequired;

					if (entry.getPledgeType() == LOTRUnitTradeEntry.PledgeType.NONE) {
						sb.append("{{Bar Coins|").append(cost * 2).append("}} ").append(Lang.NO_PLEDGE).append(", ");
						sb.append("{{Bar Coins|").append(cost).append("}} ").append(Lang.NEED_PLEDGE).append("; ");
						sb.append(alignment).append(' ').append(Lang.REPUTATION);
					} else {
						sb.append("N/A ").append(Lang.NO_PLEDGE).append(", ");
						sb.append("{{Bar Coins|").append(cost).append("}} ").append(Lang.NEED_PLEDGE).append("; ");
						sb.append(Math.max(alignment, 100)).append(' ').append(Lang.REPUTATION);
					}

					data.computeIfAbsent(entityEntry.getKey(), s -> new ArrayList<>());
					data.get(entityEntry.getKey()).add(sb.toString());
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-SellsUnitPool");
		sb.append(BEGIN);

		appendDefault(sb, Lang.ENTITY_NO_SELLS_UNIT_POOL.toString());

		for (Map.Entry<Class<? extends Entity>, List<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");
			sb.append(Lang.ENTITY_HAS_SELLS_UNIT_POOL);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	@SuppressWarnings("InstanceofIncompatibleInterface")
	private static StringBuilder genTemplateEntitySmith() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable.Smith) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Smith");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntitySpawnsInDarkness() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && isSpawnsInDarkness((LOTREntityNPC) entityEntry.getValue())) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-SpawnsInDarkness");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityTargetSeeker() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && isTargetSeeker((LOTREntityNPC) entityEntry.getValue())) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-TargetSeeker");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	@SuppressWarnings("InstanceofIncompatibleInterface")
	private static StringBuilder genTemplateEntityTradeable() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable && !(entityEntry.getValue() instanceof LOTRTradeable.Smith)) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-Tradeable");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateEntityUnitTradeable() {
		Map<Class<? extends Entity>, String> data = new HashMap<>();

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRUnitTradeable) {
				data.put(entityEntry.getKey(), TRUE);
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Entity-UnitTradeable");
		sb.append(BEGIN);

		appendDefault(sb, FALSE);

		for (Map.Entry<Class<? extends Entity>, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getEntityPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionBanners() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			for (LOTRItemBanner.BannerType banner : faction.factionBanners) {
				data.computeIfAbsent(faction, s -> new TreeSet<>());
				data.get(faction).add(getBannerName(banner));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Banners");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_BANNERS.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_BANNERS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionCodename() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			data.put(faction, faction.codeName());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Codename");
		sb.append(BEGIN);

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionConquestBiomes() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRBiome biome : BIOMES) {
			for (LOTRBiomeSpawnList.FactionContainer factionContainer : getFactionContainers(biome.npcSpawnList)) {
				if (getBaseWeight(factionContainer) <= 0) {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						for (LOTRSpawnEntry spawnEntry : getSpawnEntries(getSpawnList(spawnListContainer))) {
							Entity entity = ENTITY_CLASS_TO_ENTITY.get(spawnEntry.entityClass);
							if (entity instanceof LOTREntityNPC) {
								LOTRFaction faction = ((LOTREntityNPC) entity).getFaction();
								data.computeIfAbsent(faction, s -> new TreeSet<>());
								data.get(faction).add(getBiomeLink(biome));
								break;
							}
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-ConquestBiomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_CONQUEST_BIOMES.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_CONQUEST_BIOMES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionEnemies() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			StringJoiner sj = new StringJoiner(" • ");

			for (LOTRFaction otherFaction : FACTIONS) {
				if (faction.isBadRelation(otherFaction) && faction != otherFaction) {
					sj.add(getFactionLink(otherFaction));
				}
			}

			if (sj.length() > 0) {
				data.put(faction, sj.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Enemies");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_ENEMIES.toString());

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionFriends() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			StringJoiner sj = new StringJoiner(" • ");

			for (LOTRFaction otherFaction : FACTIONS) {
				if (faction.isGoodRelation(otherFaction) && faction != otherFaction) {
					sj.add(getFactionLink(otherFaction));
				}
			}

			if (sj.length() > 0) {
				data.put(faction, sj.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Friends");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_FRIENDS.toString());

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}
		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionInvasionBiomes() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRBiome biome : BIOMES) {
			for (LOTRInvasions invasion : getRegisteredInvasions(biome.invasionSpawns)) {
				for (LOTRInvasions.InvasionSpawnEntry invasionSpawnEntry : invasion.invasionMobs) {
					Entity entity = ENTITY_CLASS_TO_ENTITY.get(invasionSpawnEntry.getEntityClass());
					if (entity instanceof LOTREntityNPC) {
						LOTRFaction faction = ((LOTREntityNPC) entity).getFaction();
						data.computeIfAbsent(faction, s -> new TreeSet<>());
						data.get(faction).add(getBiomeLink(biome));
						break;
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-InvasionBiomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_INVASION_BIOMES.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_INVASION_BIOMES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionName() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			data.put(faction, getFactionName(faction));
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Name");
		sb.append(BEGIN);

		appendDefault(sb, "UNKNOWN");

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionNPCs() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (Map.Entry<Class<? extends Entity>, Entity> entityEntry : ENTITY_CLASS_TO_ENTITY.entrySet()) {
			Entity entity = entityEntry.getValue();
			if (entity instanceof LOTREntityNPC) {
				LOTREntityNPC npc = (LOTREntityNPC) entity;
				data.computeIfAbsent(npc.getFaction(), s -> new TreeSet<>());
				data.get(npc.getFaction()).add(getEntityLink(entityEntry.getKey()));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-NPCs");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_NPCS.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_NPCS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionPledgeRank() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			LOTRFactionRank rank = faction.getPledgeRank();

			if (rank != null) {
				StringBuilder sb = new StringBuilder();

				sb.append(rank.getDisplayName());

				String femRank = rank.getDisplayNameFem();
				if (!femRank.contains("lotr")) {
					sb.append('/').append(femRank);
				}

				sb.append(" (").append((int) faction.getPledgeAlignment()).append(')');

				data.put(faction, sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-PledgeRank");
		sb.append(BEGIN);

		appendDefault(sb, N_A);

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionRanks() {
		Map<LOTRFaction, List<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			for (LOTRFactionRank rank : getRanksSortedDescending(faction)) {
				StringBuilder sb = new StringBuilder();

				sb.append(rank.getDisplayFullName());

				String femRank = rank.getDisplayFullNameFem();
				if (!femRank.contains("lotr")) {
					sb.append('/').append(femRank);
				}

				sb.append(" (").append((int) rank.alignment).append(')');

				data.computeIfAbsent(faction, s -> new ArrayList<>());
				data.get(faction).add(sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Ranks");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_RANKS.toString());

		for (Map.Entry<LOTRFaction, List<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_RANKS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionRegion() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			if (faction.factionRegion != null) {
				data.put(faction, faction.factionRegion.getRegionName());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Region");
		sb.append(BEGIN);

		appendDefault(sb, N_A);

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionShieldsCapes() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			StringBuilder sb = new StringBuilder();

			boolean save = false;

			sb.append(NL).append("&lt;table class=\"wikitable shields-capes\"&gt;");

			for (LOTRShields shield : SHIELDS) {
				if (getAlignmentFaction(shield) == faction) {
					save = true;
					sb.append(NL + "&lt;tr&gt;&lt;td&gt;").append(shield.getShieldName()).append("&lt;/td&gt;&lt;td&gt;").append(shield.getShieldDesc()).append("&lt;/td&gt;&lt;td&gt;").append(getShieldFilename(shield)).append("&lt;/td&gt;&lt;/tr&gt;");
				}
			}

			sb.append(NL).append("&lt;table&gt;");

			if (save) {
				data.put(faction, sb.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-ShieldsCapes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_ATTRIBUTES.toString());

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_ATTRIBUTES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionSpawnBiomes() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRBiome biome : BIOMES) {
			for (LOTRBiomeSpawnList.FactionContainer factionContainer : getFactionContainers(biome.npcSpawnList)) {
				if (getBaseWeight(factionContainer) > 0) {
					for (LOTRBiomeSpawnList.SpawnListContainer spawnListContainer : getSpawnListContainers(factionContainer)) {
						for (LOTRSpawnEntry spawnEntry : getSpawnEntries(getSpawnList(spawnListContainer))) {
							Entity entity = ENTITY_CLASS_TO_ENTITY.get(spawnEntry.entityClass);
							if (entity instanceof LOTREntityNPC) {
								LOTRFaction faction = ((LOTREntityNPC) entity).getFaction();
								data.computeIfAbsent(faction, s -> new TreeSet<>());
								data.get(faction).add(getBiomeLink(biome));
								break;
							}
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-SpawnBiomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_SPAWN_BIOMES.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_SPAWN_BIOMES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionWarCrimes() {
		Map<LOTRFaction, String> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRFaction faction : FACTIONS) {
			if (!faction.approvesWarCrimes) {
				data.put(faction, Lang.FACTION_NO_WAR_CRIMES.toString());
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-WarCrimes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_HAS_WAR_CRIMES.toString());

		for (Map.Entry<LOTRFaction, String> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateFactionWaypoints() {
		Map<LOTRFaction, Set<String>> data = new EnumMap<>(LOTRFaction.class);

		for (LOTRWaypoint wp : WAYPOINTS) {
			data.computeIfAbsent(wp.faction, s -> new TreeSet<>());
			data.get(wp.faction).add(wp.getDisplayName());
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Faction-Waypoints");
		sb.append(BEGIN);

		appendDefault(sb, Lang.FACTION_NO_WAYPOINTS.toString());

		for (Map.Entry<LOTRFaction, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getFactionPagename(entry.getKey())).append(" = ");
			sb.append(Lang.FACTION_HAS_WAYPOINTS);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateMineralBiomes() {
		Map<String, Set<MineralInfo>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
			oreGenerants.addAll(getBiomeOres(biome.decorator));
			oreGenerants.addAll(getBiomeGems(biome.decorator));

			for (Object oreGenerant : oreGenerants) {
				Block block = getOreGenBlock(getOreGen(oreGenerant));
				int meta = getOreGenMeta(getOreGen(oreGenerant));

				String blockLink;
				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					blockLink = getMineralName(block, meta);
				} else {
					blockLink = getMineralName(block);
				}

				String stats = "(" + getOreChance(oreGenerant) + "%; Y: " + getMinHeight(oreGenerant) + '-' + getMaxHeight(oreGenerant) + ')';

				data.computeIfAbsent(blockLink, s -> new TreeSet<>());
				data.get(blockLink).add(new MineralInfo(getBiomeLink(biome), ' ' + stats));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Mineral-Biomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.MINERAL_NO_BIOMES.toString());

		for (Map.Entry<String, Set<MineralInfo>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(entry.getKey()).append(" = ");
			sb.append(Lang.MINERAL_HAS_BIOMES);

			appendSection(sb, MineralInfo.toStringSet(entry.getValue()));
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateStructureBiomes() {
		Map<Class<?>, Set<String>> data = new HashMap<>();

		for (LOTRBiome biome : BIOMES) {
			for (Object structure : getStructures(biome.decorator)) {
				data.computeIfAbsent(getStructureGen(structure).getClass(), s -> new TreeSet<>());
				data.get(getStructureGen(structure).getClass()).add(getBiomeLink(biome));
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Structure-Biomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.STRUCTURE_NO_BIOMES.toString());

		for (Map.Entry<Class<?>, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getStructurePagename(entry.getKey())).append(" = ");
			sb.append(Lang.STRUCTURE_HAS_BIOMES);

			appendSection(sb, entry.getValue());
		}

		sb.append(END);

		return sb;
	}

	private static StringBuilder genTemplateTreeBiomes() {
		Map<LOTRTreeType, Set<String>> data = new EnumMap<>(LOTRTreeType.class);

		for (LOTRBiome biome : BIOMES) {
			Collection<LOTRTreeType.WeightedTreeType> weightedTreeTypes = getTreeTypes(biome.decorator);

			Collection<LOTRTreeType> excludedTreeTypes = EnumSet.noneOf(LOTRTreeType.class);

			for (LOTRTreeType.WeightedTreeType weightedTreeType : weightedTreeTypes) {
				LOTRTreeType treeType = weightedTreeType.treeType;

				data.computeIfAbsent(treeType, s -> new TreeSet<>());
				data.get(treeType).add(getBiomeLink(biome));

				excludedTreeTypes.add(treeType);
			}

			for (Object variantBucket : getVariantList(biome.getBiomeVariantsSmall())) {
				for (LOTRTreeType.WeightedTreeType weightedTreeType : getVariant(variantBucket).treeTypes) {
					LOTRTreeType treeType = weightedTreeType.treeType;

					if (!excludedTreeTypes.contains(treeType)) {
						data.computeIfAbsent(treeType, s -> new TreeSet<>());
						data.get(treeType).add(getBiomeLink(biome) + " (" + getBiomeVariantName(getVariant(variantBucket)) + ')');
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();

		sb.append(TITLE).append(TEMPLATE).append("LOTR+ DB Tree-Biomes");
		sb.append(BEGIN);

		appendDefault(sb, Lang.TREE_NO_BIOMES.toString());

		for (Map.Entry<LOTRTreeType, Set<String>> entry : data.entrySet()) {
			sb.append(NL).append("| ");
			sb.append(getTreePagename(entry.getKey())).append(" = ");
			sb.append(Lang.TREE_HAS_BIOMES);

			appendSection(sb, entry.getValue());
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
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Биом}}").append(PAGE_RIGHT);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesEntities(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (Class<? extends Entity> entityClass : ENTITY_CLASSES) {
			String pageName = getEntityPagename(entityClass);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Моб}}").append(PAGE_RIGHT);
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
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Фракция}}").append(PAGE_RIGHT);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesMinerals(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (String pageName : MINERALS) {
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Ископаемое}}").append(PAGE_RIGHT);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesStructures(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (Class<? extends WorldGenerator> strClass : STRUCTURE_CLASSES) {
			String pageName = getStructurePagename(strClass);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Структура}}").append(PAGE_RIGHT);
			}
		}

		return sb;
	}

	private static StringBuilder addPagesTrees(Collection<String> neededPages, Collection<String> existingPages) {
		StringBuilder sb = new StringBuilder();

		for (LOTRTreeType tree : TREES) {
			String pageName = getTreePagename(tree);
			neededPages.add(pageName);
			if (!existingPages.contains(pageName)) {
				sb.append(TITLE_SINGLE).append(pageName);
				sb.append(PAGE_LEFT).append("{{Статья Дерево}}").append(PAGE_RIGHT);
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
		try (PrintWriter printWriter = new PrintWriter("hummel/removal.txt", UTF_8)) {
			StringBuilder sb = new StringBuilder();

			for (String existing : existingPages) {
				if (!neededPages.contains(existing)) {
					sb.append(existing).append('\n');
				}
			}
			printWriter.write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void searchForEntities(World world) {
		for (Class<? extends Entity> entityClass : ENTITY_CLASSES) {
			ENTITY_CLASS_TO_ENTITY.put(entityClass, newEntity(entityClass, world));
		}
	}

	private static void searchForMinerals() {
		for (LOTRBiome biome : BIOMES) {
			Collection<Object> oreGenerants = new HashSet<>(getBiomeSoils(biome.decorator));
			oreGenerants.addAll(getBiomeOres(biome.decorator));
			oreGenerants.addAll(getBiomeGems(biome.decorator));

			for (Object oreGenerant : oreGenerants) {
				Block block = getOreGenBlock(getOreGen(oreGenerant));
				int meta = getOreGenMeta(getOreGen(oreGenerant));

				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					MINERALS.add(getMineralName(block, meta));
				} else {
					MINERALS.add(getMineralName(block));
				}
			}
		}
	}

	private static void searchForStructures() {
		for (LOTRBiome biome : BIOMES) {
			for (Object structure : getStructures(biome.decorator)) {
				STRUCTURE_CLASSES.add((Class<? extends WorldGenerator>) getStructureGen(structure).getClass());
			}
		}
	}

	private static void searchForPagenamesBiome() {
		next:
		for (LOTRBiome biome : BIOMES) {
			String preName = getBiomeName(biome);
			for (LOTRFaction faction : FACTIONS) {
				if (preName.equals(getFactionName(faction))) {
					BIOME_TO_PAGENAME.put(biome, preName + " (" + Lang.PAGE_BIOME + ')');
					continue next;
				}
			}
			for (Class<? extends Entity> entityClass : ENTITY_CLASSES) {
				if (preName.equals(getEntityName(entityClass))) {
					BIOME_TO_PAGENAME.put(biome, preName + " (" + Lang.PAGE_BIOME + ')');
					continue next;
				}
			}
			BIOME_TO_PAGENAME.put(biome, preName);
		}
	}

	private static void searchForPagenamesEntity() {
		next:
		for (Class<? extends Entity> entityClass : ENTITY_CLASSES) {
			String preName = getEntityName(entityClass);
			for (LOTRBiome biome : BIOMES) {
				if (preName.equals(getBiomeName(biome))) {
					ENTITY_CLASS_TO_PAGENAME.put(entityClass, preName + " (" + Lang.PAGE_ENTITY + ')');
					continue next;
				}
			}
			for (LOTRFaction faction : FACTIONS) {
				if (preName.equals(getFactionName(faction))) {
					ENTITY_CLASS_TO_PAGENAME.put(entityClass, preName + " (" + Lang.PAGE_ENTITY + ')');
					continue next;
				}
			}
			ENTITY_CLASS_TO_PAGENAME.put(entityClass, preName);
		}
	}

	private static void searchForPagenamesFaction() {
		next:
		for (LOTRFaction faction : FACTIONS) {
			String preName = getFactionName(faction);
			for (LOTRBiome biome : BIOMES) {
				if (preName.equals(getBiomeName(biome))) {
					FACTION_TO_PAGENAME.put(faction, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			for (Class<? extends Entity> entityClass : ENTITY_CLASSES) {
				if (preName.equals(getEntityName(entityClass))) {
					FACTION_TO_PAGENAME.put(faction, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			FACTION_TO_PAGENAME.put(faction, preName);
		}
	}

	private static String getBannerName(LOTRItemBanner.BannerType banner) {
		return StatCollector.translateToLocal("item.lotr:banner." + banner.bannerName + ".name");
	}

	private static String getBiomeLink(LOTRBiome biome) {
		String biomeName = getBiomeName(biome);
		String biomePagename = getBiomePagename(biome);
		if (biomeName.equals(biomePagename)) {
			return "[[LOTR+:" + biomeName + "]]";
		}
		return "[[LOTR+:" + biomePagename + '|' + biomeName + "]]";
	}

	private static String getBiomeName(LOTRBiome biome) {
		return StatCollector.translateToLocal("lotr.biome." + biome.biomeName + ".name");
	}

	private static String getBiomePagename(LOTRBiome biome) {
		return BIOME_TO_PAGENAME.get(biome);
	}

	private static String getBiomeVariantName(LOTRBiomeVariant variant) {
		return StatCollector.translateToLocal("lotr.variant." + variant.variantName + ".name");
	}

	private static String getMineralLink(Block block, int meta) {
		return "[[LOTR+:" + StatCollector.translateToLocal(block.getUnlocalizedName() + '.' + meta + ".name") + "]]";
	}

	private static String getMineralLink(Block block) {
		return "[[LOTR+:" + StatCollector.translateToLocal(block.getUnlocalizedName() + ".name") + "]]";
	}

	private static String getMineralName(Block block, int meta) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + '.' + meta + ".name");
	}

	private static String getMineralName(Block block) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + ".name");
	}

	private static String getEntityLink(Class<? extends Entity> entityClass) {
		String entityName = getEntityName(entityClass);
		if (entityName.contains("null")) {
			return StatCollector.translateToLocal("entity." + EntityList.classToStringMapping.get(entityClass) + ".name");
		}

		String entityPagename = getEntityPagename(entityClass);
		if (entityName.equals(entityPagename)) {
			return "[[LOTR+:" + entityPagename + "]]";
		}
		return "[[LOTR+:" + entityPagename + '|' + entityName + "]]";
	}

	private static String getEntityName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity.lotr." + Config.ENTITY_CLASS_TO_NAME.get(entityClass) + ".name");
	}

	private static String getEntityPagename(Class<? extends Entity> entityClass) {
		return ENTITY_CLASS_TO_PAGENAME.get(entityClass);
	}

	private static String getFactionLink(LOTRFaction fac) {
		String facName = getFactionName(fac);
		String facPagename = getFactionPagename(fac);
		if (facName.equals(facPagename)) {
			return "[[LOTR+:" + facPagename + "]]";
		}
		return "[[LOTR+:" + facPagename + '|' + facName + "]]";
	}

	private static String getFactionName(LOTRFaction fac) {
		return StatCollector.translateToLocal("lotr.faction." + fac.codeName() + ".name");
	}

	private static String getFactionPagename(LOTRFaction fac) {
		return FACTION_TO_PAGENAME.get(fac);
	}

	private static String getItemFilename(Item item) {
		return "[[File:" + item.getUnlocalizedName().substring("item.lotr:".length()) + ".png|32px|link=]]";
	}

	private static String getItemName(Item item) {
		return StatCollector.translateToLocal(item.getUnlocalizedName() + ".name");
	}

	private static String getShieldFilename(LOTRShields shield) {
		return "[[File:Shield " + shield.name().toLowerCase(Locale.ROOT) + ".png]]";
	}

	private static String getStructureLink(Class<?> structureClass) {
		return "[[LOTR+:" + StatCollector.translateToLocal("lotr.structure." + Config.STRUCTURE_CLASS_TO_NAME.get(structureClass) + ".name") + "]]";
	}

	private static String getStructurePagename(Class<?> structureClass) {
		return StatCollector.translateToLocal("lotr.structure." + Config.STRUCTURE_CLASS_TO_NAME.get(structureClass) + ".name");
	}

	private static String getSettlementName(Class<? extends LOTRVillageGen> clazz) {
		return StatCollector.translateToLocal("lotr.structure." + clazz.getSimpleName().replace("LOTR", "").replace("Gen", "") + ".name");
	}

	private static String getTreePagename(LOTRTreeType tree) {
		return StatCollector.translateToLocal("lotr.tree." + tree.name().toLowerCase(Locale.ROOT) + ".name");
	}

	private static String getTreeLink(LOTRTreeType tree) {
		return "[[LOTR+:" + StatCollector.translateToLocal("lotr.tree." + tree.name().toLowerCase(Locale.ROOT) + ".name") + "]]";
	}

	private static void appendSection(StringBuilder sb, Collection<String> section) {
		for (String item : section) {
			sb.append(NL).append("* ").append(item).append(';');
		}

		section.clear();
	}

	private static void appendSection(StringBuilder sb, String value) {
		sb.append(value);
	}

	private static void appendDefault(StringBuilder sb, String value) {
		sb.append(NL).append("| #default = ").append(value);
	}

	private static class MineralInfo implements Comparable<MineralInfo> {
		private final String valuableData;
		private final String skippableData;

		private MineralInfo(String valuableData, String skippableData) {
			this.valuableData = valuableData;
			this.skippableData = skippableData;
		}

		private static Collection<String> toStringSet(Iterable<MineralInfo> value) {
			Collection<String> set = new TreeSet<>();
			for (MineralInfo mineralInfo : value) {
				set.add(mineralInfo.toString());
			}
			return set;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			MineralInfo that = (MineralInfo) o;
			return Objects.equals(valuableData, that.valuableData);
		}

		@Override
		public int hashCode() {
			return Objects.hash(valuableData);
		}

		@Override
		public String toString() {
			return valuableData + skippableData;
		}

		@Override
		public int compareTo(MineralInfo o) {
			return valuableData.compareTo(o.valuableData);
		}
	}

	public enum Lang {
		BIOME_HAS_ANIMALS, BIOME_HAS_CONQUEST_FACTIONS, BIOME_HAS_INVASION_FACTIONS, BIOME_HAS_MINERALS, BIOME_HAS_NPCS, BIOME_HAS_STRUCTURES, BIOME_HAS_TREES, BIOME_HAS_VARIANTS, BIOME_HAS_WAYPOINTS, BIOME_NO_ANIMALS, BIOME_NO_CONQUEST_FACTIONS, BIOME_NO_INVASION_FACTIONS, BIOME_NO_MINERALS, BIOME_NO_NPCS, BIOME_NO_STRUCTURES, BIOME_NO_TREES, BIOME_NO_VARIANTS, BIOME_NO_WAYPOINTS, CATEGORY, ENTITY_CONQUEST, ENTITY_CONQUEST_INVASION, ENTITY_HAS_BIOMES, ENTITY_HAS_BUYS_POOL, ENTITY_HAS_OWNERS, ENTITY_HAS_SELLS_UNIT_POOL, ENTITY_INVASION, ENTITY_NO_BIOMES, ENTITY_NO_BUYS_POOL, ENTITY_NO_OWNERS, ENTITY_NO_SELLS_UNIT_POOL, FACTION_HAS_BANNERS, FACTION_HAS_CONQUEST_BIOMES, FACTION_HAS_INVASION_BIOMES, FACTION_HAS_NPCS, FACTION_HAS_RANKS, FACTION_HAS_SPAWN_BIOMES, FACTION_HAS_WAR_CRIMES, FACTION_HAS_WAYPOINTS, FACTION_NO_ATTRIBUTES, FACTION_HAS_ATTRIBUTES, FACTION_NO_BANNERS, FACTION_NO_CONQUEST_BIOMES, FACTION_NO_INVASION_BIOMES, FACTION_NO_NPCS, FACTION_NO_RANKS, FACTION_NO_SPAWN_BIOMES, FACTION_NO_WAR_CRIMES, FACTION_NO_WAYPOINTS, MINERAL_HAS_BIOMES, MINERAL_NO_BIOMES, NEED_PLEDGE, NO_PLEDGE, PAGE_BIOME, PAGE_ENTITY, PAGE_FACTION, REPUTATION, RIDER, STRUCTURE_HAS_BIOMES, STRUCTURE_NO_BIOMES, TREE_HAS_BIOMES, TREE_NO_BIOMES, FACTION_NO_FRIENDS, FACTION_NO_ENEMIES, ENTITY_HAS_SELLS_POOL, ENTITY_NO_SELLS_POOL;

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
}