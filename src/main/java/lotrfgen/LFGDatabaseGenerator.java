package lotrfgen;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRMod;
import lotr.common.LOTRShields;
import lotr.common.block.LOTRBlockOreGem;
import lotr.common.block.LOTRBlockRock;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRUnitTradeEntry.PledgeType;
import lotr.common.fac.LOTRFaction;
import lotr.common.fac.LOTRFactionRank;
import lotr.common.item.LOTRItemBanner.BannerType;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.LOTRBiomeGenNearHarad;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.map.LOTRWaypoint.Region;
import lotr.common.world.spawning.LOTRBiomeSpawnList.FactionContainer;
import lotr.common.world.spawning.LOTRBiomeSpawnList.SpawnListContainer;
import lotr.common.world.spawning.LOTRInvasions;
import lotr.common.world.spawning.LOTRInvasions.InvasionSpawnEntry;
import lotr.common.world.spawning.LOTRSpawnEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LFGDatabaseGenerator {
	public static final Map<Class<? extends Entity>, Entity> CLASS_TO_ENTITY_OBJ = new HashMap<>();
	public static final Map<Class<? extends Entity>, String> CLASS_TO_ENTITY_NAME = new HashMap<>();
	public static final Collection<Class<? extends Entity>> ENTITY_SET = new HashSet<>();
	public static final Map<Class<?>, String> CLASS_TO_STRUCTURE_NAME = new HashMap<>();

	private static final Map<String, String> FAC_TO_PAGE = new HashMap<>();
	private static final Map<String, String> ENTITY_TO_PAGE = new HashMap<>();
	private static final Map<String, String> BIOME_TO_PAGE = new HashMap<>();
	private static final Iterable<Item> ITEMS = new HashSet<>(LFGReflectionHelper.getObjectFieldsOfType(LOTRMod.class, Item.class));
	private static final Collection<LOTRUnitTradeEntries> UNITS = new HashSet<>(LFGReflectionHelper.getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class));
	private static final Collection<LOTRAchievement> ACHIEVEMENTS = new HashSet<>(LFGReflectionHelper.getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class));
	private static final Collection<LOTRBiome> BIOMES = new HashSet<>(LFGReflectionHelper.getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class));
	private static final Set<LOTRFaction> FACTIONS = EnumSet.allOf(LOTRFaction.class);
	private static final Set<LOTRTreeType> TREES = EnumSet.allOf(LOTRTreeType.class);
	private static final Set<LOTRWaypoint> WAYPOINTS = EnumSet.allOf(LOTRWaypoint.class);
	private static final Set<LOTRShields> SHIELDS = EnumSet.allOf(LOTRShields.class);
	private static final Collection<String> MINERALS = new HashSet<>();
	private static final Collection<Class<? extends WorldGenerator>> STRUCTURES = new HashSet<>();
	private static final Collection<Class<? extends Entity>> HIREABLE = new HashSet<>();
	private static final String BEGIN = "\n</title><ns>10</ns><revision><text>&lt;includeonly&gt;{{#switch: {{{1}}}";
	private static final String END = "\n}}&lt;/includeonly&gt;&lt;noinclude&gt;[[" + Lang.CATEGORY + "]]&lt;/noinclude&gt;</text></revision></page>";
	private static final String TITLE = "<page><title>";

	static {
		BIOMES.removeAll(Collections.singleton(null));
		ACHIEVEMENTS.removeAll(Collections.singleton(null));
		UNITS.removeAll(Collections.singleton(null));
	}

	private static String getAchievementDesc(LOTRAchievement ach) {
		return StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".desc");
	}

	private static String getAchievementTitle(LOTRAchievement ach) {
		return StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".title");
	}

	private static String getBannerName(BannerType banner) {
		return StatCollector.translateToLocal("item.lotr:banner." + banner.bannerName + ".name");
	}

	private static String getBiomeLink(LOTRBiome biome) {
		String biomeName = getBiomeName(biome);
		String biomePagename = getBiomePagename(biome);
		if (biomeName.equals(biomePagename)) {
			return "[[" + biomeName + "]]";
		}
		return "[[" + biomePagename + '|' + biomeName + "]]";
	}

	private static String getBiomeName(LOTRBiome biome) {
		return StatCollector.translateToLocal("lotr.biome." + biome.biomeName + ".name");
	}

	private static String getBiomePagename(LOTRBiome biome) {
		return BIOME_TO_PAGE.get(getBiomeName(biome));
	}

	private static String getBiomeVariantName(LOTRBiomeVariant variant) {
		return StatCollector.translateToLocal("lotr.variant." + variant.variantName + ".name");
	}

	private static String getBlockMetaName(Block block, int meta) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + '.' + meta + ".name");
	}

	private static String getBlockName(Block block) {
		return StatCollector.translateToLocal(block.getUnlocalizedName() + ".name");
	}

	private static String getEntityLink(Class<? extends Entity> entityClass) {
		String entityName = getEntityName(entityClass);
		String entityPagename = getEntityPagename(entityClass);
		if (entityName.equals(entityPagename)) {
			return "[[" + entityPagename + "]]";
		}
		return "[[" + entityPagename + '|' + entityName + "]]";
	}

	private static String getEntityName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity.lotr." + CLASS_TO_ENTITY_NAME.get(entityClass) + ".name");
	}

	private static String getEntityPagename(Class<? extends Entity> entityClass) {
		return ENTITY_TO_PAGE.get(getEntityName(entityClass));
	}

	private static String getEntityVanillaName(Class<? extends Entity> entityClass) {
		return StatCollector.translateToLocal("entity." + EntityList.classToStringMapping.get(entityClass) + ".name");
	}

	private static String getFactionLink(LOTRFaction fac) {
		String facName = getFactionName(fac);
		String facPagename = getFactionPagename(fac);
		if (facName.equals(facPagename)) {
			return "[[" + facPagename + "]]";
		}
		return "[[" + facPagename + '|' + facName + "]]";
	}

	private static String getFactionName(LOTRFaction fac) {
		return StatCollector.translateToLocal("lotr.faction." + fac.codeName() + ".name");
	}

	private static String getFactionPagename(LOTRFaction fac) {
		return FAC_TO_PAGE.get(getFactionName(fac));
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

	private static String getStructureName(Class<? extends WorldGenerator> structureClass) {
		return StatCollector.translateToLocal("lotr.structure." + CLASS_TO_STRUCTURE_NAME.get(structureClass) + ".name");
	}

	private static String getTreeName(LOTRTreeType tree) {
		return StatCollector.translateToLocal("lotr.tree." + tree.name().toLowerCase(Locale.ROOT) + ".name");
	}

	private static void searchForHireable(Collection<Class<? extends Entity>> hireable, Iterable<LOTRUnitTradeEntries> units) {
		for (LOTRUnitTradeEntries entries : units) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				hireable.add(entry.entityClass);
			}
		}
	}

	private static void searchForMinerals(Iterable<LOTRBiome> biomes, Collection<String> minerals) {
		Collection<Object> oreGenerants = new ArrayList<>();
		for (LOTRBiome biome : biomes) {
			oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
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
			oreGenerants.clear();
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
			for (Class<? extends Entity> entityClass : ENTITY_SET) {
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
		for (Class<? extends Entity> entityClass : ENTITY_SET) {
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
					FAC_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			for (Class<? extends Entity> entityClass : ENTITY_SET) {
				if (preName.equals(getEntityName(entityClass))) {
					FAC_TO_PAGE.put(preName, preName + " (" + Lang.PAGE_FACTION + ')');
					continue next;
				}
			}
			FAC_TO_PAGE.put(preName, preName);
		}
	}

	private static void searchForStructures(Iterable<LOTRBiome> biomes, Collection<Class<? extends WorldGenerator>> structures) {
		for (LOTRBiome biome : biomes) {
			for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
				structures.add((Class<? extends WorldGenerator>) LFGReflectionHelper.getStructureGen(structure).getClass());
			}
		}
	}

	public static void generate(String mode, World world, EntityPlayer player, Random random) {
		long time = System.nanoTime();
		try {
			LFGConfig cfg = new LFGConfig(world);
			cfg.authorizeEntityInfo();
			cfg.authorizeStructureInfo();
			searchForMinerals(BIOMES, MINERALS);
			searchForStructures(BIOMES, STRUCTURES);
			searchForHireable(HIREABLE, UNITS);
			searchForPagenamesEntity(BIOMES, FACTIONS);
			searchForPagenamesBiome(BIOMES, FACTIONS);
			searchForPagenamesFaction(BIOMES, FACTIONS);
			BIOMES.remove(LOTRBiome.beachGravel);
			BIOMES.remove(LOTRBiome.beachWhite);
			TREES.remove(LOTRTreeType.NULL);

			Files.createDirectories(Paths.get("hummel"));
			if ("tables".equals(mode)) {
				genTableAchievements();
				genTableShields();
				genTableUnits();
				genTableWaypoints(player);
				genTableArmor();
				genTableWeapon();
				genTableFood();
			} else if ("xml".equals(mode)) {
				StringBuilder sb = new StringBuilder();

				sb.append("\n<mediawiki xmlns=\"http://www.mediawiki.org/xml/export-0.11/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.mediawiki.org/xml/export-0.11/ http://www.mediawiki.org/xml/export-0.11.xsd\" version=\"0.11\" xml:lang=\"ru\">");

				/* ALL PAGES */

				File file = new File("hummel/sitemap.txt");
				boolean created = !file.exists() && file.createNewFile();
				if (created) {
					System.out.println("The sitemap file was created");
				}
				Set<String> sitemap;
				try (Stream<String> lines = Files.lines(Paths.get("hummel/sitemap.txt"))) {
					sitemap = lines.collect(Collectors.toSet());
				}

				Collection<String> neededPages = new HashSet<>();
				for (String pageName : MINERALS) {
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Ископаемое}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				for (Class<? extends Entity> entityClass : CLASS_TO_ENTITY_OBJ.keySet()) {
					String pageName = getEntityPagename(entityClass);
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Моб}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				for (LOTRBiome biome : BIOMES) {
					String pageName = getBiomePagename(biome);
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Биом}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				for (LOTRFaction fac : FACTIONS) {
					String pageName = getFactionPagename(fac);
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Фракция}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				for (LOTRTreeType tree : TREES) {
					String pageName = getTreeName(tree);
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Дерево}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				for (Class<? extends WorldGenerator> strClass : STRUCTURES) {
					String pageName = getStructureName(strClass);
					neededPages.add(pageName);
					if (!sitemap.contains(pageName)) {
						String s2 = "</title><revision><text>{{Статья Структура}}</text></revision></page>\n";
						sb.append(TITLE).append(pageName).append(s2);
					}
				}

				StringBuilder del = new StringBuilder();
				for (String existing : sitemap) {
					if (!neededPages.contains(existing)) {
						del.append(existing).append('\n');
					}
				}
				PrintWriter removal = new PrintWriter("hummel/removal.txt", "UTF-8");
				removal.write(del.toString());
				removal.close();

				/* STRUCTURES */

				genTemplateStructureBiomes(sb);

				/* MINERALS */

				genTemplateMineralBiomes(sb);

				/* TREES */

				genTemplateTreeBiomes(sb);

				/* BIOMES */

				genTemplateBiomeSpawnNpc(sb);
				genTemplateBiomeConquestNpc(sb);
				genTemplateBiomeBandits(sb);
				genTemplateBiomeName(sb);
				genTemplateBiomeRainfall(sb);
				genTemplateBiomeTemperature(sb);
				genTemplateBiomeVariants(sb);
				genTemplateBiomeInvasions(sb);
				genTemplateBiomeWaypoints(sb);
				genTemplateBiomeAchievement(sb);
				genTemplateBiomeTrees(sb);
				genTemplateBiomeMobs(sb);
				genTemplateBiomeMinerals(sb);
				genTemplateBiomeMusic(sb);
				genTemplateBiomeStructures(sb);

				/* FACTIONS */

				genTemplateFactionNpc(sb);
				genTemplateFactionInvasions(sb);
				genTemplateFactionSpawn(sb);
				genTemplateFactionConquest(sb);
				genTemplateFactionRanks(sb);
				genTemplateFactionBanners(sb);
				genTemplateFactionWaypoints(sb);
				genTemplateFactionAttributes(sb);
				genTemplateFactionPledge(sb);
				genTemplateFactionEnemies(sb);
				genTemplateFactionFriends(sb);
				genTemplateFactionWarCrimes(sb);
				genTemplateFactionCodename(sb);
				genTemplateFactionName(sb);
				genTemplateFactionRegion(sb);

				/* MOBS */

				genTemplateMobSpawn(sb);
				genTemplateMobOwner(sb);
				genTemplateMobHealth(sb);
				genTemplateMobRideable1(sb);
				genTemplateMobRideable2(sb);
				genTemplateMobBannerBearer(sb);
				genTemplateMobUnitTradeable(sb);
				genTemplateMobTradeable(sb);
				genTemplateMobSmith(sb);
				genTemplateMobMercenary(sb);
				genTemplateMobFarmhand(sb);
				genTemplateMobBuys(sb);
				genTemplateMobSells(sb);
				genTemplateMobImmuneToFrost(sb);
				genTemplateMobImmuneToFire(sb);
				genTemplateMobImmuneToHeat(sb);
				genTemplateMobSpawnInDarkness(sb);
				genTemplateMobAlignment(sb);
				genTemplateMobFaction(sb);
				genTemplateMobAchievement(sb);
				genTemplateMobBonus(sb);
				genTemplateMobPrice(sb);
				genTemplateMobPricePledge(sb);
				genTemplateMobUnits(sb);

				sb.append("\n</mediawiki>");

				PrintWriter xml = new PrintWriter("hummel/import.xml", "UTF-8");
				xml.write(sb.toString());
				xml.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long newTime = System.nanoTime();
		IChatComponent iChatComponent = new ChatComponentText("Generated databases in " + (newTime - time) / 1.0E9 + 's');
		player.addChatMessage(iChatComponent);
	}

	private static void genTemplateMobUnits(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Units");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> ownerEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (ownerEntry.getValue() instanceof LOTRUnitTradeable) {
				LOTRUnitTradeEntries entries = ((LOTRUnitTradeable) ownerEntry.getValue()).getUnits();
				sb.append("\n| ").append(getEntityPagename(ownerEntry.getKey())).append(" = ");
				for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
					if (entry.mountClass == null) {
						sb.append("\n* ").append(getEntityLink(entry.entityClass));
						if (entry.getPledgeType() == PledgeType.NONE) {
							sb.append(": {{Coins|").append(LFGReflectionHelper.getInitialCost(entry) * 2).append("}} ").append(Lang.NO_PLEDGE).append(", {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} ").append(Lang.NEED_PLEDGE).append("; ").append(entry.alignmentRequired).append("+ ").append(Lang.REPUTATION).append(';');
						} else if (entry.alignmentRequired < 101.0f) {
							sb.append(": N/A ").append(Lang.NO_PLEDGE).append(", {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} ").append(Lang.NEED_PLEDGE).append("; +").append(100.0).append("+ ").append(Lang.REPUTATION).append(';');
						} else {
							sb.append(": N/A ").append(Lang.NO_PLEDGE).append(", {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} ").append(Lang.NEED_PLEDGE).append("; +").append(entry.alignmentRequired).append("+ ").append(Lang.REPUTATION).append(';');
						}
					}
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobPricePledge(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-PricePledge");
		sb.append(BEGIN);
		for (LOTRUnitTradeEntries entries : UNITS) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				sb.append("\n| ").append(getEntityPagename(entry.entityClass)).append(" = {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}}");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobPrice(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Price");
		sb.append(BEGIN);
		for (LOTRUnitTradeEntries entries : UNITS) {
			for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
				sb.append("\n| ").append(getEntityPagename(entry.entityClass)).append(" = ");
				if (entry.getPledgeType() == PledgeType.NONE) {
					sb.append("{{Coins|").append(LFGReflectionHelper.getInitialCost(entry) * 2).append("}}");
				} else {
					sb.append("N/A");
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobBonus(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Bonus");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				float bonus = ((LOTREntityNPC) entityEntry.getValue()).getAlignmentBonus();
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = +").append(bonus);
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobAchievement(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Achievement");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTRAchievement ach = LFGReflectionHelper.getKillAchievement((LOTREntityNPC) entityEntry.getValue());
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = ");
				if (ach == null) {
					sb.append("N/A");
				} else {
					sb.append('"').append(getAchievementTitle(ach)).append('"');
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobFaction(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Faction");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC) {
				LOTRFaction fac = ((LOTREntityNPC) entityEntry.getValue()).getFaction();
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = ").append(getFactionLink(fac));
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobAlignment(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Alignment");
		sb.append(BEGIN);
		next:
		for (Class<? extends Entity> entityClass : HIREABLE) {
			for (LOTRUnitTradeEntries entries : UNITS) {
				for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
					if (entry.entityClass == entityClass) {
						sb.append("\n| ").append(getEntityPagename(entityClass)).append(" = ");
						if (entry.getPledgeType() == PledgeType.NONE || entry.alignmentRequired >= 101.0f) {
							sb.append(entry.alignmentRequired);
						} else {
							sb.append('+').append(100.0);
						}
						continue next;
					}
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobSpawnInDarkness(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-SpawnInDarkness");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && LFGReflectionHelper.getSpawnsInDarkness((LOTREntityNPC) entityEntry.getValue())) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobImmuneToHeat(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-ImmuneToHeat");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBiomeGenNearHarad.ImmuneToHeat) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobImmuneToFire(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-ImmuneToFire");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue().isImmuneToFire()) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobImmuneToFrost(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-ImmuneToFrost");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPC && ((LOTREntityNPC) entityEntry.getValue()).isImmuneToFrost) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobSells(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Sells");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				LOTRTradeEntries entries = ((LOTRTradeable) entityEntry.getValue()).getBuyPool();
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = ");
				for (LOTRTradeEntry entry : entries.tradeEntries) {
					try {
						sb.append("\n* ").append(entry.createTradeItem().getDisplayName()).append(": {{Coins|").append(entry.getCost()).append("}};");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobBuys(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Buys");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				LOTRTradeEntries entries = ((LOTRTradeable) entityEntry.getValue()).getSellPool();
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = ");
				for (LOTRTradeEntry entry : entries.tradeEntries) {
					try {
						sb.append("\n* ").append(entry.createTradeItem().getDisplayName()).append(": {{Coins|").append(entry.getCost()).append("}};");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobFarmhand(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Farmhand");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRFarmhand) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobMercenary(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Mercenary");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRMercenary) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobSmith(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Smith");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable.Smith) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobTradeable(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Tradeable");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRTradeable) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobUnitTradeable(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-UnitTradeable");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRUnitTradeable) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobBannerBearer(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-BannerBearer");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRBannerBearer) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobRideable2(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Rideable2");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTRNPCMount) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobRideable1(StringBuilder sb) {
		sb.append(TITLE).append("Template: DB Mob-Rideable1");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			if (entityEntry.getValue() instanceof LOTREntityNPCRideable) {
				sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = True");
			}
		}
		sb.append(END);
	}

	private static void genTemplateMobHealth(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Mob-Health");
		sb.append(BEGIN);
		for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
			EntityLivingBase entity = (EntityLivingBase) entityEntry.getValue();
			sb.append("\n| ").append(getEntityPagename(entityEntry.getKey())).append(" = ").append(entity.getMaxHealth());
		}
		sb.append(END);
	}

	private static void genTemplateMobOwner(StringBuilder sb) {
		HashMap<Class<? extends Entity>, Class<? extends Entity>> owners = new HashMap<>();

		sb.append(TITLE).append("DB Mob-Owner");
		sb.append(BEGIN);
		for (Class<? extends Entity> entityClass : HIREABLE) {
			loop:
			for (Entry<Class<? extends Entity>, Entity> ownerEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
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
				sb.append("\n| ").append(getEntityPagename(entityClass)).append(" = ").append(getEntityLink(owners.get(entityClass)));
			}
			owners.clear();
		}
		sb.append(END);
	}

	private static void genTemplateMobSpawn(StringBuilder sb) {
		Collection<LOTRBiome> spawnBiomes = new HashSet<>();
		Collection<LOTRBiome> conquestBiomes = new HashSet<>();
		Collection<LOTRBiome> invasionBiomes = new HashSet<>();
		Collection<LOTRBiome> unnaturalBiomes = new HashSet<>();
		Collection<SpawnListEntry> spawnEntries = new ArrayList<>();
		Collection<SpawnListEntry> conquestEntries = new ArrayList<>();
		Collection<InvasionSpawnEntry> invasionEntries = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Mob-Spawn");
		sb.append(BEGIN);
		for (Class<? extends Entity> entityClass : CLASS_TO_ENTITY_OBJ.keySet()) {
			next:
			for (LOTRBiome biome : BIOMES) {
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
				for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
					invasionEntries.addAll(invasion.invasionMobs);
				}
				for (SpawnListEntry entry : spawnEntries) {
					if (entry.entityClass == entityClass) {
						spawnBiomes.add(biome);
						spawnEntries.clear();
						conquestEntries.clear();
						invasionEntries.clear();
						continue next;
					}
				}
				for (SpawnListEntry entry : conquestEntries) {
					if (entry.entityClass == entityClass) {
						conquestBiomes.add(biome);
						unnaturalBiomes.add(biome);
						break;
					}
				}
				for (InvasionSpawnEntry entry : invasionEntries) {
					if (entry.getEntityClass() == entityClass) {
						invasionBiomes.add(biome);
						unnaturalBiomes.add(biome);
						break;
					}
				}
				spawnEntries.clear();
				conquestEntries.clear();
				invasionEntries.clear();
			}
			sb.append("\n| ").append(getEntityPagename(entityClass)).append(" = ");
			if (spawnBiomes.isEmpty() && conquestBiomes.isEmpty() && invasionBiomes.isEmpty()) {
				sb.append(Lang.ENTITY_NO_BIOMES);
			} else {
				sb.append(Lang.ENTITY_HAS_BIOMES);
				for (LOTRBiome biome : spawnBiomes) {
					sb.append("\n* ").append(getBiomeLink(biome)).append(';');
				}
				for (LOTRBiome biome : conquestBiomes) {
					if (!invasionBiomes.contains(biome)) {
						sb.append("\n* ").append(getBiomeLink(biome)).append(' ').append(Lang.ENTITY_CONQUEST).append(';');
					}
				}
				for (LOTRBiome biome : invasionBiomes) {
					if (!conquestBiomes.contains(biome)) {
						sb.append("\n* ").append(getBiomeLink(biome)).append(' ').append(Lang.ENTITY_INVASION).append(';');
					}
				}
				for (LOTRBiome biome : unnaturalBiomes) {
					if (conquestBiomes.contains(biome) && invasionBiomes.contains(biome)) {
						sb.append("\n* ").append(getBiomeLink(biome)).append(' ').append(Lang.ENTITY_CONQUEST_INVASION).append(';');
					}
				}
			}
			spawnBiomes.clear();
			conquestBiomes.clear();
			invasionBiomes.clear();
			unnaturalBiomes.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionRegion(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Region");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (fac.factionRegion == null) {
				sb.append("N/A");
			} else {
				sb.append(fac.factionRegion.getRegionName());
			}
		}
		sb.append(END);
	}

	private static void genTemplateFactionName(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Name");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ").append(getFactionName(fac));
		}
		sb.append(END);
	}

	private static void genTemplateFactionCodename(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Codename");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ").append(fac.codeName());
		}
		sb.append(END);
	}

	private static void genTemplateFactionWarCrimes(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-WarCrimes");
		sb.append(BEGIN);
		sb.append("\n| #default = ").append(Lang.FACTION_NO_WAR_CRIMES);
		for (LOTRFaction fac : FACTIONS) {
			if (fac.approvesWarCrimes) {
				sb.append("\n| ").append(getFactionPagename(fac)).append(" = ").append(Lang.FACTION_HAS_WAR_CRIMES);
			}
		}
		sb.append(END);
	}

	private static void genTemplateFactionFriends(StringBuilder sb) {
		Collection<LOTRFaction> facFriends = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Friends");
		sb.append(BEGIN);
		for (LOTRFaction fac1 : FACTIONS) {
			for (LOTRFaction fac2 : FACTIONS) {
				if (fac1.isGoodRelation(fac2) && fac1 != fac2) {
					facFriends.add(fac2);
				}
			}
			sb.append("\n| ").append(getFactionPagename(fac1)).append(" = ");
			if (facFriends.isEmpty()) {
				sb.append(Lang.FACTION_NO_FRIENDS);
			} else {
				boolean first = true;
				for (LOTRFaction fac : facFriends) {
					if (first) {
						first = false;
					} else {
						sb.append(" • ");
					}
					sb.append(getFactionLink(fac));
				}
			}
			facFriends.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionEnemies(StringBuilder sb) {
		Collection<LOTRFaction> facEnemies = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Enemies");
		sb.append(BEGIN);
		for (LOTRFaction fac1 : FACTIONS) {
			for (LOTRFaction fac2 : FACTIONS) {
				if (fac1.isBadRelation(fac2) && fac1 != fac2) {
					facEnemies.add(fac2);
				}
			}
			sb.append("\n| ").append(getFactionPagename(fac1)).append(" = ");
			if (facEnemies.isEmpty()) {
				sb.append(Lang.FACTION_NO_ENEMIES);
			} else {
				boolean first = true;
				for (LOTRFaction fac : facEnemies) {
					if (first) {
						first = false;
					} else {
						sb.append(" • ");
					}
					sb.append(getFactionLink(fac));
				}
			}
			facEnemies.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionPledge(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Pledge");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			if (fac.getPledgeRank() != null) {
				sb.append("\n| ").append(getFactionPagename(fac)).append(" = ").append(fac.getPledgeRank().getDisplayName()).append('/').append(fac.getPledgeRank().getDisplayNameFem()).append(" (+").append(fac.getPledgeAlignment()).append(')');
			}
		}
		sb.append(END);
	}

	private static void genTemplateFactionAttributes(StringBuilder sb) {
		Collection<LOTRShields> facShields = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Attr");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			for (LOTRShields shield : SHIELDS) {
				if (LFGReflectionHelper.getAlignmentFaction(shield) == fac) {
					facShields.add(shield);
				}
			}

			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (facShields.isEmpty()) {
				sb.append(Lang.FACTION_NO_ATTRIBUTES);
			} else {
				sb.append("\n&lt;table class=\"wikitable shields\"&gt;");
				for (LOTRShields shield : facShields) {
					sb.append("\n&lt;tr&gt;&lt;td&gt;").append(shield.getShieldName()).append("&lt;/td&gt;&lt;td&gt;").append(shield.getShieldDesc()).append("&lt;/td&gt;&lt;td&gt;").append(getShieldFilename(shield)).append("&lt;/td&gt;&lt;/tr&gt;");
				}
				sb.append("\n&lt;table&gt;");
			}
			facShields.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionWaypoints(StringBuilder sb) {
		Collection<LOTRWaypoint> facWaypoints = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Waypoints");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			for (LOTRWaypoint wp : WAYPOINTS) {
				if (wp.faction == fac) {
					facWaypoints.add(wp);
				}
			}
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (facWaypoints.isEmpty()) {
				sb.append(Lang.FACTION_NO_WAYPOINTS);
			} else {
				sb.append(Lang.FACTION_HAS_WAYPOINTS);
				for (LOTRWaypoint wp : facWaypoints) {
					sb.append("\n* ").append(wp.getDisplayName()).append(';');
				}
			}
			facWaypoints.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionBanners(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Banners");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (fac.factionBanners.isEmpty()) {
				sb.append(Lang.FACTION_NO_BANNERS);
			} else {
				sb.append(Lang.FACTION_HAS_BANNERS);
				for (BannerType banner : fac.factionBanners) {
					sb.append("\n* ").append(getBannerName(banner)).append(';');
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateFactionRanks(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-Ranks");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (LFGReflectionHelper.getRanksSortedDescending(fac).isEmpty()) {
				sb.append(Lang.FACTION_NO_RANKS);
			} else {
				sb.append(Lang.FACTION_HAS_RANKS);
				for (LOTRFactionRank rank : LFGReflectionHelper.getRanksSortedDescending(fac)) {
					sb.append("\n* ").append(rank.getDisplayFullName()).append('/').append(rank.getDisplayFullNameFem()).append(" (+").append(rank.alignment).append(");");
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateFactionConquest(StringBuilder sb) {
		Collection<LOTRBiome> conquestBiomes = new HashSet<>();
		Collection<FactionContainer> conquestContainers = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Conquest");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			next:
			for (LOTRBiome biome : BIOMES) {
				List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
				if (!facContainers.isEmpty()) {
					for (FactionContainer facContainer : facContainers) {
						if (LFGReflectionHelper.getBaseWeight(facContainer) <= 0) {
							conquestContainers.add(facContainer);
						}
					}
					if (!conquestContainers.isEmpty()) {
						for (FactionContainer facContainer : conquestContainers) {
							for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
								for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
									Entity entity = CLASS_TO_ENTITY_OBJ.get(entry.entityClass);
									if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
										conquestBiomes.add(biome);
										conquestContainers.clear();
										continue next;
									}
								}
							}
						}
					}
				}
				conquestContainers.clear();
			}
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (conquestBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_CONQUEST);
			} else {
				sb.append(Lang.FACTION_HAS_CONQUEST);
				for (LOTRBiome biome : conquestBiomes) {
					sb.append("\n* ").append(getBiomeLink(biome)).append(';');
				}
			}
			conquestBiomes.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionSpawn(StringBuilder sb) {
		Collection<LOTRBiome> spawnBiomes = new HashSet<>();
		Collection<FactionContainer> spawnContainers = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Faction-Spawn");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			next:
			for (LOTRBiome biome : BIOMES) {
				List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
				if (!facContainers.isEmpty()) {
					for (FactionContainer facContainer : facContainers) {
						if (LFGReflectionHelper.getBaseWeight(facContainer) > 0) {
							spawnContainers.add(facContainer);
						}
					}
					if (!spawnContainers.isEmpty()) {
						for (FactionContainer facContainer : spawnContainers) {
							for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
								for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
									Entity entity = CLASS_TO_ENTITY_OBJ.get(entry.entityClass);
									if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
										spawnBiomes.add(biome);
										spawnContainers.clear();
										continue next;
									}
								}
							}
						}
					}
				}
				spawnContainers.clear();
			}
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (spawnBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_SPAWN);
			} else {
				sb.append(Lang.FACTION_HAS_SPAWN);
				for (LOTRBiome biome : spawnBiomes) {
					sb.append("\n* ").append(getBiomeLink(biome)).append(';');
				}
			}
			spawnBiomes.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionInvasions(StringBuilder sb) {
		Collection<LOTRBiome> invasionBiomes = new HashSet<>();

		sb.append(TITLE).append("Template:DB Faction-Invasions");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			next:
			for (LOTRBiome biome : BIOMES) {
				for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
					for (InvasionSpawnEntry entry : invasion.invasionMobs) {
						Entity entity = CLASS_TO_ENTITY_OBJ.get(entry.getEntityClass());
						if (entity instanceof LOTREntityNPC && fac == ((LOTREntityNPC) entity).getFaction()) {
							invasionBiomes.add(biome);
							continue next;
						}
					}
				}
			}
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			if (invasionBiomes.isEmpty()) {
				sb.append(Lang.FACTION_NO_INVASIONS);
			} else {
				sb.append(Lang.FACTION_HAS_INVASION);
				for (LOTRBiome biome : invasionBiomes) {
					sb.append("\n* ").append(getBiomeLink(biome)).append(';');
				}
			}
			invasionBiomes.clear();
		}
		sb.append(END);
	}

	private static void genTemplateFactionNpc(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Faction-NPC");
		sb.append(BEGIN);
		for (LOTRFaction fac : FACTIONS) {
			sb.append("\n| ").append(getFactionPagename(fac)).append(" = ");
			for (Entry<Class<? extends Entity>, Entity> entityEntry : CLASS_TO_ENTITY_OBJ.entrySet()) {
				Entity entity = entityEntry.getValue();
				if (entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).getFaction() == fac) {
					sb.append("\n* ").append(getEntityLink(entityEntry.getKey())).append(';');
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeStructures(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Structures");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
				sb.append(Lang.BIOME_NO_STRUCTURES);
			} else {
				sb.append(Lang.BIOME_HAS_STRUCTURES);
				for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
					sb.append("\n* [[").append(getStructureName((Class<? extends WorldGenerator>) LFGReflectionHelper.getStructureGen(structure).getClass())).append("]];");
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeMusic(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Music");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (biome.getBiomeMusic() == null) {
				sb.append("N/A");
			} else {
				sb.append(biome.getBiomeMusic().subregion);
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeMinerals(StringBuilder sb) {
		Collection<Object> oreGenerants = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Biome-Minerals");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_HAS_MINERALS);
			oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
			oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeOres"));
			oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeGems"));
			for (Object oreGenerant : oreGenerants) {
				Block block = LFGReflectionHelper.getMineableBlock(LFGReflectionHelper.getOreGen(oreGenerant));
				int meta = LFGReflectionHelper.getMineableBlockMeta(LFGReflectionHelper.getOreGen(oreGenerant));
				float oreChance = LFGReflectionHelper.getOreChance(oreGenerant);
				int minHeight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight");
				int maxheight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight");
				if (block instanceof LOTRBlockOreGem || block instanceof BlockDirt || block instanceof LOTRBlockRock) {
					sb.append("\n* [[").append(getBlockMetaName(block, meta)).append("]] (").append(oreChance).append("%; Y: ").append(minHeight).append('-').append(maxheight).append(");");
				} else {
					sb.append("\n* [[").append(getBlockName(block)).append("]] (").append(oreChance).append("%; Y: ").append(minHeight).append('-').append(maxheight).append(");");
				}
			}
			oreGenerants.clear();
		}
		sb.append(END);
	}

	private static void genTemplateBiomeMobs(StringBuilder sb) {
		Collection<SpawnListEntry> entries = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Biome-Mobs");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			entries.addAll(biome.getSpawnableList(EnumCreatureType.ambient));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.waterCreature));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.creature));
			entries.addAll(biome.getSpawnableList(EnumCreatureType.monster));
			entries.addAll(biome.getSpawnableList(LOTRBiome.creatureType_LOTRAmbient));
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (entries.isEmpty()) {
				sb.append(Lang.BIOME_NO_ANIMALS);
			} else {
				sb.append(Lang.BIOME_HAS_ANIMALS);
				for (SpawnListEntry entry : entries) {
					if (CLASS_TO_ENTITY_NAME.containsKey(entry.entityClass)) {
						sb.append("\n* ").append(getEntityLink(entry.entityClass)).append(';');
					} else {
						sb.append("\n* ").append(getEntityVanillaName(entry.entityClass)).append(';');
					}
				}
			}
			entries.clear();
		}
		sb.append(END);
	}

	private static void genTemplateBiomeTrees(StringBuilder sb) {
		EnumSet<LOTRTreeType> trees = EnumSet.noneOf(LOTRTreeType.class);
		Map<LOTRTreeType, LOTRBiomeVariant> additionalTrees = new EnumMap<>(LOTRTreeType.class);

		sb.append(TITLE).append("Template:DB Biome-Trees");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getTreeTypes(biome.decorator)) {
				trees.add(weightedTreeType.treeType);
			}
			for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
				for (WeightedTreeType weightedTreeType : LFGReflectionHelper.getVariant(variantBucket).treeTypes) {
					if (!trees.contains(weightedTreeType.treeType)) {
						additionalTrees.put(weightedTreeType.treeType, LFGReflectionHelper.getVariant(variantBucket));
					}
				}
			}
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (trees.isEmpty() && additionalTrees.isEmpty()) {
				sb.append(Lang.BIOME_NO_TREES);
			} else {
				if (additionalTrees.isEmpty()) {
					sb.append(Lang.BIOME_HAS_TREES_BIOME_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_TREES);
				}
				for (LOTRTreeType tree : trees) {
					sb.append("\n* [[").append(getTreeName(tree)).append("]];");
				}
				for (Entry<LOTRTreeType, LOTRBiomeVariant> tree : additionalTrees.entrySet()) {
					sb.append("\n* [[").append(getTreeName(tree.getKey())).append("]] (").append(getBiomeVariantName(tree.getValue()).toLowerCase(Locale.ROOT)).append(')').append(';');
				}
			}
			trees.clear();
			additionalTrees.clear();
		}
		sb.append(END);
	}

	private static void genTemplateBiomeAchievement(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Achievement");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			LOTRAchievement ach = biome.getBiomeAchievement();
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (ach == null) {
				sb.append(Lang.BIOME_NO_ACHIEVEMENT);
			} else {
				sb.append('"').append(getAchievementTitle(ach)).append('"');
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeWaypoints(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Waypoints");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			Region region = biome.getBiomeWaypoints();
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (region == null) {
				sb.append(Lang.BIOME_NO_WAYPOINTS);
			} else {
				sb.append(Lang.BIOME_HAS_WAYPOINTS);
				for (LOTRWaypoint wp : WAYPOINTS) {
					if (LFGReflectionHelper.getRegion(wp) == region) {
						sb.append("\n* ").append(wp.getDisplayName()).append(" (").append(getFactionLink(wp.faction)).append(");");
					}
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeInvasions(StringBuilder sb) {
		EnumSet<LOTRFaction> invasionFactions = EnumSet.noneOf(LOTRFaction.class);

		sb.append(TITLE).append("Template:DB Biome-Invasions");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
				sb.append(Lang.BIOME_NO_INVASIONS);
			} else {
				sb.append(Lang.BIOME_HAS_INVASIONS);
				next:
				for (LOTRInvasions invasion : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
					for (InvasionSpawnEntry entry : invasion.invasionMobs) {
						Entity entity = CLASS_TO_ENTITY_OBJ.get(entry.getEntityClass());
						if (entity instanceof LOTREntityNPC) {
							LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
							invasionFactions.add(fac);
							continue next;
						}
					}
				}
				for (LOTRFaction fac : invasionFactions) {
					sb.append("\n* ").append(getFactionLink(fac)).append(';');
				}
			}
			invasionFactions.clear();
		}
		sb.append(END);
	}

	private static void genTemplateBiomeVariants(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Variants");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
			if (LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall()).isEmpty()) {
				sb.append(Lang.BIOME_NO_VARIANTS);
			} else {
				for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
					sb.append("\n* ").append(getBiomeVariantName(LFGReflectionHelper.getVariant(variantBucket))).append(';');
				}
			}
		}
		sb.append(END);
	}

	private static void genTemplateBiomeTemperature(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Temperature");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(biome.temperature);
		}
		sb.append(END);
	}

	private static void genTemplateBiomeRainfall(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Rainfall");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(biome.rainfall);
		}
		sb.append(END);
	}

	private static void genTemplateBiomeName(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Name");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(getBiomeName(biome));
		}
		sb.append(END);
	}

	private static void genTemplateBiomeBandits(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Biome-Bandits");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(biome.getBanditChance());
		}
		sb.append(END);
	}

	private static void genTemplateBiomeConquestNpc(StringBuilder sb) {
		Collection<FactionContainer> conqestContainers = new ArrayList<>();
		EnumSet<LOTRFaction> conquestFactions = EnumSet.noneOf(LOTRFaction.class);

		sb.append(TITLE).append("Template:DB Biome-ConquestNPC");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
			if (facContainers.isEmpty()) {
				sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_NO_CONQUEST);
			} else {
				for (FactionContainer facContainer : facContainers) {
					if (LFGReflectionHelper.getBaseWeight(facContainer) <= 0) {
						conqestContainers.add(facContainer);
					}
				}
				sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
				if (conqestContainers.isEmpty()) {
					sb.append(Lang.BIOME_SPAWN_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_CONQUEST);
					for (FactionContainer facContainer : conqestContainers) {
						next:
						for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
							for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
								Entity entity = CLASS_TO_ENTITY_OBJ.get(entry.entityClass);
								if (entity instanceof LOTREntityNPC) {
									LOTRFaction fac = ((LOTREntityNPC) entity).getFaction();
									conquestFactions.add(fac);
									continue next;
								}
							}
						}
					}
					for (LOTRFaction fac : conquestFactions) {
						sb.append("\n* ").append(getFactionLink(fac)).append("; ");
					}
				}
			}
			conqestContainers.clear();
			conquestFactions.clear();
		}
		sb.append(END);
	}

	private static void genTemplateBiomeSpawnNpc(StringBuilder sb) {
		Collection<FactionContainer> spawnContainers = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Biome-SpawnNPC");
		sb.append(BEGIN);
		for (LOTRBiome biome : BIOMES) {
			List<FactionContainer> facContainers = LFGReflectionHelper.getFactionContainers(biome.npcSpawnList);
			if (facContainers.isEmpty()) {
				sb.append("\n| ").append(getBiomePagename(biome)).append(" = ").append(Lang.BIOME_NO_SPAWN);
			} else {
				for (FactionContainer facContainer : facContainers) {
					if (LFGReflectionHelper.getBaseWeight(facContainer) > 0) {
						spawnContainers.add(facContainer);
					}
				}
				sb.append("\n| ").append(getBiomePagename(biome)).append(" = ");
				if (spawnContainers.isEmpty()) {
					sb.append(Lang.BIOME_CONQUEST_ONLY);
				} else {
					sb.append(Lang.BIOME_HAS_SPAWN);
					for (FactionContainer facContainer : spawnContainers) {
						for (SpawnListContainer container : LFGReflectionHelper.getSpawnLists(facContainer)) {
							for (LOTRSpawnEntry entry : LFGReflectionHelper.getSpawnListEntries(LFGReflectionHelper.getSpawnList(container))) {
								sb.append("\n* ").append(getEntityLink(entry.entityClass)).append("; ");
							}
						}
					}
				}
			}
			spawnContainers.clear();
		}
		sb.append(END);
	}

	private static void genTemplateTreeBiomes(StringBuilder sb) {
		Collection<LOTRBiome> biomesTree = new HashSet<>();
		Collection<LOTRBiome> biomesVariantTree = new HashSet<>();

		sb.append(TITLE).append("Template:DB Tree-Biomes");
		sb.append(BEGIN);
		for (LOTRTreeType tree : TREES) {
			next:
			for (LOTRBiome biome : BIOMES) {
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
			sb.append("\n| ").append(getTreeName(tree)).append(" = ");
			if (biomesTree.isEmpty() && biomesVariantTree.isEmpty()) {
				sb.append(Lang.TREE_NO_BIOMES);
			} else {
				sb.append(Lang.TREE_HAS_BIOMES);
			}
			for (LOTRBiome biome : biomesTree) {
				sb.append("\n* ").append(getBiomeLink(biome)).append(';');
			}
			for (LOTRBiome biome : biomesVariantTree) {
				sb.append("\n* ").append(getBiomeLink(biome)).append(" (").append(Lang.TREE_VARIANT_ONLY).append(");");
			}
			biomesTree.clear();
			biomesVariantTree.clear();
		}
		sb.append(END);
	}

	private static void genTemplateMineralBiomes(StringBuilder sb) {
		Collection<Object> oreGenerants = new ArrayList<>();

		sb.append(TITLE).append("Template:DB Mineral-Biomes");
		sb.append(BEGIN);
		for (String mineral : MINERALS) {
			sb.append("\n| ").append(mineral).append(" = ").append(Lang.MINERAL_BIOMES);
			next:
			for (LOTRBiome biome : BIOMES) {
				oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeSoils"));
				oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeOres"));
				oreGenerants.addAll(LFGReflectionHelper.getBiomeMinerals(biome.decorator, "biomeGems"));
				for (Object oreGenerant : oreGenerants) {
					Block block = LFGReflectionHelper.getMineableBlock(LFGReflectionHelper.getOreGen(oreGenerant));
					int meta = LFGReflectionHelper.getMineableBlockMeta(LFGReflectionHelper.getOreGen(oreGenerant));
					if (getBlockMetaName(block, meta).equals(mineral) || getBlockName(block).equals(mineral)) {
						float oreChance = LFGReflectionHelper.getOreChance(oreGenerant);
						int minHeight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "minHeight");
						int maxheight = LFGReflectionHelper.getMinMaxHeight(oreGenerant, "maxHeight");
						sb.append("\n* ").append(getBiomeLink(biome)).append(" (").append(oreChance).append("%; Y: ").append(minHeight).append('-').append(maxheight).append(");");
						oreGenerants.clear();
						continue next;
					}
				}
				oreGenerants.clear();
			}
		}
		sb.append(END);
	}

	private static void genTemplateStructureBiomes(StringBuilder sb) {
		sb.append(TITLE).append("Template:DB Structure-Biomes");
		sb.append(BEGIN);
		for (Class<? extends WorldGenerator> strClass : STRUCTURES) {
			sb.append("\n| ").append(getStructureName(strClass)).append(" = ").append(Lang.STRUCTURE_BIOMES);
			next:
			for (LOTRBiome biome : BIOMES) {
				for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
					if (LFGReflectionHelper.getStructureGen(structure).getClass() == strClass) {
						sb.append("\n* ").append(getBiomeLink(biome)).append(';');
						continue next;
					}
				}
			}
		}
		sb.append(END);
	}

	@SuppressWarnings("deprecation")
	private static void genTableFood() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		for (Item item : ITEMS) {
			if (item instanceof ItemFood) {
				int heal = ((ItemFood) item).func_150905_g(null);
				float saturation = ((ItemFood) item).func_150906_h(null);
				sb.append("\n| ").append(getItemName(item)).append(" || ").append(getItemFilename(item)).append(" || ").append("{{Bar|bread|").append(decimalFormat.format(saturation * heal * 2)).append("}} || {{Bar|food|").append(heal).append("}} || ").append(item.getItemStackLimit()).append("\n|-");
			}
		}
		PrintWriter printWriter = new PrintWriter("hummel/food.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableWeapon() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (Item item : ITEMS) {
			if (item instanceof ItemSword) {
				float damage = LFGReflectionHelper.getDamageAmount(item);
				ToolMaterial toolMaterial = LFGReflectionHelper.getToolMaterial(item);
				sb.append("\n| ").append(getItemName(item)).append(" || ").append(getItemFilename(item)).append(" || ").append(item.getMaxDamage()).append(" || ").append(damage).append(" || ");
				if (toolMaterial == null || toolMaterial.getRepairItemStack() == null) {
					sb.append("N/A");
				} else {
					sb.append(getItemName(toolMaterial.getRepairItemStack().getItem()));
				}
				sb.append("\n|-");
			}
		}
		PrintWriter printWriter = new PrintWriter("hummel/weapon.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableArmor() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (Item item : ITEMS) {
			if (item instanceof ItemArmor) {
				float damage = ((ItemArmor) item).damageReduceAmount;
				ArmorMaterial armorMaterial = ((ItemArmor) item).getArmorMaterial();
				sb.append("\n| ").append(getItemName(item)).append(" || ").append(getItemFilename(item)).append(" || ").append(item.getMaxDamage()).append(" || ").append(damage).append(" || ");
				if (armorMaterial == null || armorMaterial.customCraftingMaterial == null) {
					sb.append("N/A");
				} else {
					sb.append(getItemName(armorMaterial.customCraftingMaterial));
				}
				sb.append("\n|-");
			}
		}
		PrintWriter printWriter = new PrintWriter("hummel/armor.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableWaypoints(EntityPlayer player) throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (LOTRWaypoint wp : WAYPOINTS) {
			sb.append("\n| ").append(wp.getDisplayName()).append(" || ").append(wp.getLoreText(player)).append("\n|-");
		}
		PrintWriter printWriter = new PrintWriter("hummel/waypoints.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableUnits() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (LOTRUnitTradeEntries unitTradeEntries : UNITS) {
			for (LOTRUnitTradeEntry entry : unitTradeEntries.tradeEntries) {
				try {
					sb.append("\n| ").append(getEntityLink(entry.entityClass));
					if (entry.getPledgeType() == PledgeType.NONE) {
						if (entry.mountClass == null) {
							sb.append(" || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry) * 2).append("}} || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} || +").append(entry.alignmentRequired).append(" || -");
						} else {
							sb.append(" || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry) * 2).append("}} (").append(Lang.RIDER).append(") || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} || +").append(entry.alignmentRequired).append(" || -");
						}
					} else if (entry.mountClass == null) {
						if (entry.alignmentRequired < 101.0f) {
							sb.append(" || N/A || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} || +100.0 || +");
						} else {
							sb.append(" || N/A || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} || +").append(entry.alignmentRequired).append(" || +");
						}
					} else if (entry.alignmentRequired < 101.0f) {
						sb.append(" || N/A || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} (").append(Lang.RIDER).append(") || +100.0 || +");
					} else {
						sb.append(" || N/A || {{Coins|").append(LFGReflectionHelper.getInitialCost(entry)).append("}} (").append(Lang.RIDER).append(") || +").append(entry.alignmentRequired).append(" || +");
					}
					sb.append("\n|-");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		PrintWriter printWriter = new PrintWriter("hummel/units.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableShields() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (LOTRShields shield : SHIELDS) {
			sb.append("\n| ").append(shield.getShieldName()).append(" || ").append(shield.getShieldDesc()).append(" || ").append(getShieldFilename(shield)).append("\n|-");
		}
		PrintWriter printWriter = new PrintWriter("hummel/shields.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	private static void genTableAchievements() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (LOTRAchievement achievement : ACHIEVEMENTS) {
			sb.append("\n| ").append(getAchievementTitle(achievement)).append(" || ").append(getAchievementDesc(achievement)).append("\n|-");
		}
		PrintWriter printWriter = new PrintWriter("hummel/achievements.txt", "UTF-8");
		printWriter.write(sb.toString());
		printWriter.close();
	}

	public enum Database {
		XML("xml"), TABLES("tables");

		private final String codeName;

		Database(String name) {
			codeName = name;
		}

		public static Database forName(String name) {
			for (Database db : values()) {
				if (db.codeName.equals(name)) {
					return db;
				}
			}
			return null;
		}

		public static List<String> getNames() {
			List<String> names = new ArrayList<>();
			for (Database db : values()) {
				names.add(db.codeName);
			}
			return names;
		}
	}

	public enum Lang {
		PAGE_BIOME("biomeLoc"), PAGE_FACTION("factionLoc"), PAGE_ENTITY("entityLoc"), BIOME_HAS_ANIMALS("biomeHasAnimals"), BIOME_HAS_CONQUEST("biomeHasConquest"), BIOME_HAS_INVASIONS("biomeHasInvasions"), BIOME_HAS_SPAWN("biomeHasSpawn"), BIOME_HAS_STRUCTURES("biomeHasStructures"), BIOME_HAS_TREES("biomeHasTrees2"), BIOME_HAS_TREES_BIOME_ONLY("biomeHasTrees1"), BIOME_HAS_WAYPOINTS("biomeHasWaypoints"), BIOME_NO_ACHIEVEMENT("biomeNoAchievement"), BIOME_NO_ANIMALS("biomeNoAnimals"), BIOME_NO_CONQUEST("biomeNoConquest"), BIOME_NO_INVASIONS("biomeNoInvasions"), BIOME_NO_SPAWN("biomeNoSpawn"), BIOME_NO_STRUCTURES("biomeNoStructures"), BIOME_NO_TREES("biomeNoTrees"), BIOME_NO_VARIANTS("biomeNoVariants"), BIOME_NO_WAYPOINTS("biomeNoWaypoints"), BIOME_HAS_MINERALS("biomeMinerals"), BIOME_CONQUEST_ONLY("biomeConquestOnly"), BIOME_SPAWN_ONLY("biomeSpawnOnly"), FACTION_HAS_BANNERS("factionHasBanners"), FACTION_HAS_CHARS("factionHasCharacters"), FACTION_HAS_CONQUEST("factionHasConquest"), FACTION_HAS_INVASION("factionHasInvasion"), FACTION_HAS_RANKS("factionHasRanks"), FACTION_HAS_SPAWN("factionHasSpawn"), FACTION_HAS_WAR_CRIMES("factionIsViolent"), FACTION_HAS_WAYPOINTS("factionHasWaypoints"), FACTION_NO_ATTRIBUTES("factionNoAttr"), FACTION_NO_BANNERS("factionNoBanners"), FACTION_NO_CHARS("factionNoCharacters"), FACTION_NO_CONQUEST("factionNoConquest"), FACTION_NO_ENEMIES("factionNoEnemies"), FACTION_NO_FRIENDS("factionNoFriends"), FACTION_NO_INVASIONS("factionNoInvasion"), FACTION_NO_RANKS("factionNoRanks"), FACTION_NO_SPAWN("factionNoSpawn"), FACTION_NO_STRUCTURES("factionNoStructures"), FACTION_NO_WAR_CRIMES("factionNotViolent"), FACTION_NO_WAYPOINTS("factionNoWaypoints"), TREE_HAS_BIOMES("treeHasBiomes"), TREE_NO_BIOMES("treeNoBiomes"), TREE_VARIANT_ONLY("treeVariantOnly"), RIDER("rider"), NO_PLEDGE("noPledge"), NEED_PLEDGE("yesPledge"), REPUTATION("rep"), MINERAL_BIOMES("mineralBiomes"), STRUCTURE_BIOMES("structureBiomes"), ENTITY_NO_BIOMES("entityNoBiomes"), ENTITY_HAS_BIOMES("entityHasBiomes"), ENTITY_CONQUEST("entityConquestOnly"), ENTITY_INVASION("entityInvasionOnly"), ENTITY_CONQUEST_INVASION("entityConquestInvasion"), CATEGORY("categoryTemplates");

		private final String key;

		Lang(String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return StatCollector.translateToLocal("lotr.db." + key + ".name");
		}
	}
}