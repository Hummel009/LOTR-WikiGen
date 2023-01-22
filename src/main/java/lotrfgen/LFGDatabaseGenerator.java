package lotrfgen;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import lotr.common.*;
import lotr.common.block.*;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.*;
import lotr.common.entity.npc.LOTRUnitTradeEntry.PledgeType;
import lotr.common.fac.LOTRFaction;
import lotr.common.world.biome.*;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.LOTRTreeType.WeightedTreeType;
import lotr.common.world.map.LOTRWaypoint;
import lotr.common.world.spawning.*;
import lotr.common.world.spawning.LOTRBiomeSpawnList.*;
import lotr.common.world.structure.*;
import lotr.common.world.structure2.*;
import lotr.common.world.village.*;
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
	public World world;
	public static int LEN;

	static {
		LEN = "item.lotr:".length();
	}

	public LFGDatabaseGenerator(boolean flag) {
		super(flag);
	}

	@Override

	public boolean generateWithSetRotation(World world, Random random, int y, int j, int k, int rotation) {
		this.world = world;
		String riderLoc = StatCollector.translateToLocal("db.riderloc.name");
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
		genEntityInfo(LOTREntityHorse.class, "Horse", 1, 8601889, 4136462);
		genEntityInfo(LOTREntityHobbit.class, "Hobbit", 2, 16752511, 8010275);
		genEntityInfo(LOTREntityMordorOrc.class, "MordorOrc", 3, 3353378, 7042407);
		genEntityInfo(LOTREntityShirePony.class, "ShirePony", 4, 6109994, 13017995);
		genEntityInfo(LOTREntityMordorWarg.class, "MordorWarg", 5, 4600617, 2694422);
		genEntityInfo(LOTREntityGondorSoldier.class, "GondorSoldier", 6, 5327948, 15063770);
		genEntityInfo(LOTREntityGondorMan.class, "GondorMan", 7, 13547685, 5652538);
		genEntityInfo(LOTREntityGaladhrimElf.class, "GaladhrimElf", 8, 9337185, 15920555);
		genEntityInfo(LOTREntityHobbitBartender.class, "HobbitBartender", 9, 16752511, 8010275);
		genEntityInfo(LOTREntityRohanMan.class, "RohanMan", 10, 16424833, 13406801);
		genEntityInfo(LOTREntityGaladhrimWarrior.class, "GaladhrimWarrior", 11, 12697274, 15382870);
		genEntityInfo(LOTREntityMordorOrcBombardier.class, "MordorOrcBombardier", 12, 3353378, 7042407);
		genEntityInfo(LOTREntityMordorOrcTrader.class, "MordorOrcTrader", 13, 5979436, 13421772);
		genEntityInfo(LOTREntityMordorOrcArcher.class, "MordorOrcArcher", 14, 3353378, 7042407);
		genEntityInfo(LOTREntityGondorRuinsWraith.class, "GondorRuinsWraith", 15, 12698049, 4802889);
		genEntityInfo(LOTREntityGondorBlacksmith.class, "GondorBlacksmith", 16, 13547685, 5652538);
		genEntityInfo(LOTREntityGaladhrimTrader.class, "GaladhrimTrader", 17, 2047778, 16762150);
		genEntityInfo(LOTREntityDwarf.class, "Dwarf", 18, 16353133, 15357472);
		genEntityInfo(LOTREntityDwarfWarrior.class, "DwarfWarrior", 19, 2238506, 7108730);
		genEntityInfo(LOTREntityDwarfMiner.class, "DwarfMiner", 20, 16353133, 15357472);
		genEntityInfo(LOTREntityMarshWraith.class, "MarshWraith", 21, 10524036, 6179636);
		genEntityInfo(LOTREntityMordorWargBombardier.class, "MordorWargBombardier", 22, 4600617, 2694422);
		genEntityInfo(LOTREntityMordorOrcMercenaryCaptain.class, "MordorOrcMercenaryCaptain", 23, 3353378, 7042407);
		genEntityInfo(LOTREntityGondorianCaptain.class, "GondorianCaptain", 24, 5327948, 15063770);
		genEntityInfo(LOTREntityDwarfCommander.class, "DwarfCommander", 25, 2238506, 7108730);
		genEntityInfo(LOTREntityDwarfAxeThrower.class, "DwarfAxeThrower", 26, 2238506, 7108730);
		genEntityInfo(LOTREntityGondorArcher.class, "GondorArcher", 27, 5327948, 15063770);
		genEntityInfo(LOTREntityUrukHai.class, "UrukHai", 28, 2369050, 5790015);
		genEntityInfo(LOTREntityUrukHaiCrossbower.class, "UrukHaiCrossbower", 29, 2369050, 5790015);
		genEntityInfo(LOTREntityUrukHaiBerserker.class, "UrukHaiBerserker", 30, 2369050, 14408662);
		genEntityInfo(LOTREntityUrukHaiTrader.class, "UrukHaiTrader", 31, 5979436, 13421772);
		genEntityInfo(LOTREntityUrukHaiMercenaryCaptain.class, "UrukHaiMercenaryCaptain", 32, 2369050, 5790015);
		genEntityInfo(LOTREntityTroll.class, "Troll", 33, 10848082, 4796702);
		genEntityInfo(LOTREntityOlogHai.class, "OlogHai", 34, 4147260, 2237218);
		genEntityInfo(LOTREntityGaladhrimLord.class, "GaladhrimLord", 35, 12697274, 15382870);
		genEntityInfo(LOTREntityUrukHaiSapper.class, "UrukHaiSapper", 36, 2369050, 5790015);
		genEntityInfo(LOTREntityMirkwoodSpider.class, "MirkwoodSpider", 37, 2630945, 1315088);
		genEntityInfo(LOTREntityWoodElf.class, "WoodElf", 38, 2314529, 16764574);
		genEntityInfo(LOTREntityWoodElfScout.class, "WoodElfScout", 39, 336140, 3891251);
		genEntityInfo(LOTREntityRohanBarrowWraith.class, "RohanBarrowWraith", 40, 12698049, 4802889);
		genEntityInfo(LOTREntityRohirrimWarrior.class, "Rohirrim", 41, 5524296, 13546384);
		genEntityInfo(LOTREntityRohirrimArcher.class, "RohirrimArcher", 42, 5524296, 13546384);
		genEntityInfo(LOTREntityRohirrimMarshal.class, "RohirrimMarshal", 43, 6180940, 14857848);
		genEntityInfo(LOTREntityHobbitBounder.class, "HobbitShirriff", 44, 16752511, 8010275);
		genEntityInfo(LOTREntityHobbitShirriff.class, "HobbitShirriffChief", 45, 16752511, 8010275);
		genEntityInfo(LOTREntityRohanBlacksmith.class, "RohanBlacksmith", 46, 16424833, 13406801);
		genEntityInfo(LOTREntityRangerNorth.class, "RangerNorth", 47, 4473912, 2302748);
		genEntityInfo(LOTREntityRangerIthilien.class, "RangerIthilien", 48, 4015141, 1382669);
		genEntityInfo(LOTREntityDunlendingWarrior.class, "DunlendingWarrior", 49, 5192753, 9337975);
		genEntityInfo(LOTREntityDunlendingArcher.class, "DunlendingArcher", 50, 5192753, 9337975);
		genEntityInfo(LOTREntityDunlendingWarlord.class, "DunlendingWarlord", 51, 5192753, 9337975);
		genEntityInfo(LOTREntityDunlending.class, "Dunlending", 52, 15897714, 3679258);
		genEntityInfo(LOTREntityDunlendingBartender.class, "DunlendingBartender", 53, 15897714, 3679258);
		genEntityInfo(LOTREntityRohanShieldmaiden.class, "RohanShieldmaiden", 54, 5524296, 13546384);
		genEntityInfo(LOTREntityEnt.class, "Ent", 55, 3681048, 6788650);
		genEntityInfo(LOTREntityMountainTroll.class, "MountainTroll", 56, 9991001, 5651753);
		genEntityInfo(LOTREntityMountainTrollChieftain.class, "MountainTrollChieftain", 57);
		genEntityInfo(LOTREntityHuorn.class, "Huorn", 58, 3681048, 6788650);
		genEntityInfo(LOTREntityDarkHuorn.class, "DarkHuorn", 59, 2498067, 2643485);
		genEntityInfo(LOTREntityWoodElfWarrior.class, "WoodElfWarrior", 60, 12231576, 5856300);
		genEntityInfo(LOTREntityWoodElfCaptain.class, "WoodElfCaptain", 61, 12231576, 5856300);
		genEntityInfo(LOTREntityRohanMeadhost.class, "RohanMeadhost", 62, 6567206, 14392402);
		genEntityInfo(LOTREntityButterfly.class, "Butterfly", 63, 2498589, 16747542);
		genEntityInfo(LOTREntityMidges.class, "Midges", 64, 5653040, 1972242);
		genEntityInfo(LOTREntityAngmarOrcMercenaryCaptain.class, "AngmarOrcMercenaryCaptain", 65, 3224873, 5601097);
		genEntityInfo(LOTREntityDunedain.class, "Dunedain", 66, 15638664, 6832694);
		genEntityInfo(LOTREntityNurnSlave.class, "NurnSlave", 67, 8613981, 4864555);
		genEntityInfo(LOTREntityRabbit.class, "Rabbit", 68, 9860178, 5520938);
		genEntityInfo(LOTREntityWildBoar.class, "Boar", 69, 6635562, 4069378);
		genEntityInfo(LOTREntityHobbitOrcharder.class, "HobbitOrcharder", 70, 16752511, 8010275);
		genEntityInfo(LOTREntityMordorOrcSlaver.class, "MordorOrcSlaver", 71, 3353378, 7042407);
		genEntityInfo(LOTREntityMordorSpider.class, "MordorSpider", 72, 1511181, 12917534);
		genEntityInfo(LOTREntityMordorOrcSpiderKeeper.class, "MordorOrcSpiderKeeper", 73, 3353378, 7042407);
		genEntityInfo(LOTREntityAngmarOrc.class, "AngmarOrc", 74, 3224873, 5601097);
		genEntityInfo(LOTREntityAngmarOrcArcher.class, "AngmarOrcArcher", 75, 3224873, 5601097);
		genEntityInfo(LOTREntityAngmarOrcBombardier.class, "AngmarOrcBombardier", 76, 3224873, 5601097);
		genEntityInfo(LOTREntityGundabadOrc.class, "GundabadOrc", 77, 3352346, 8548435);
		genEntityInfo(LOTREntityGundabadOrcArcher.class, "GundabadOrcArcher", 78, 3352346, 8548435);
		genEntityInfo(LOTREntityGundabadOrcMercenaryCaptain.class, "GundabadOrcMercenaryCaptain", 79, 2563350, 6382678);
		genEntityInfo(LOTREntityRangerNorthCaptain.class, "RangerNorthCaptain", 80, 4473912, 2302748);
		genEntityInfo(LOTREntityGundabadWarg.class, "GundabadWarg", 81, 4600617, 2694422);
		genEntityInfo(LOTREntityAngmarWarg.class, "AngmarWarg", 82, 4600617, 2694422);
		genEntityInfo(LOTREntityAngmarWargBombardier.class, "AngmarWargBombardier", 83, 4600617, 2694422);
		genEntityInfo(LOTREntityUrukWarg.class, "UrukWarg", 84, 4600617, 2694422);
		genEntityInfo(LOTREntityUrukWargBombardier.class, "UrukWargBombardier", 85, 4600617, 2694422);
		genEntityInfo(LOTREntityLion.class, "Lion", 86, 13345354, 10838576);
		genEntityInfo(LOTREntityLioness.class, "Lioness", 87, 13346908, 11238466);
		genEntityInfo(LOTREntityGiraffe.class, "Giraffe", 88, 13608551, 6966048);
		genEntityInfo(LOTREntityZebra.class, "Zebra", 89, 15000804, 4340020);
		genEntityInfo(LOTREntityRhino.class, "Rhino", 90, 6118481, 12171165);
		genEntityInfo(LOTREntityCrocodile.class, "Crocodile", 91, 2896659, 986886);
		genEntityInfo(LOTREntityNearHaradrim.class, "NearHaradrim", 92, 10779229, 2960685);
		genEntityInfo(LOTREntityNearHaradrimWarrior.class, "NearHaradrimWarrior", 93, 2171169, 11868955);
		genEntityInfo(LOTREntityHighElf.class, "HighElf", 94, 16761223, 15721387);
		genEntityInfo(LOTREntityGemsbok.class, "Gemsbok", 95, 11759423, 15920343);
		genEntityInfo(LOTREntityFlamingo.class, "Flamingo", 96, 16087966, 16374243);
		genEntityInfo(LOTREntityHobbitFarmer.class, "HobbitFarmer", 97, 16752511, 8010275);
		genEntityInfo(LOTREntityHobbitFarmhand.class, "HobbitFarmhand", 98, 16752511, 8010275);
		genEntityInfo(LOTREntityHighElfWarrior.class, "HighElfWarrior", 99, 14935016, 7040410);
		genEntityInfo(LOTREntityHighElfLord.class, "HighElfLord", 100, 14935016, 7040410);
		genEntityInfo(LOTREntityGondorBannerBearer.class, "GondorBannerBearer", 101, 5327948, 15063770);
		genEntityInfo(LOTREntityRohanBannerBearer.class, "RohanBannerBearer", 102, 5524296, 13546384);
		genEntityInfo(LOTREntityMordorBannerBearer.class, "MordorBannerBearer", 103, 3353378, 7042407);
		genEntityInfo(LOTREntityGaladhrimBannerBearer.class, "GaladhrimBannerBearer", 104, 12697274, 15382870);
		genEntityInfo(LOTREntityWoodElfBannerBearer.class, "WoodElfBannerBearer", 105, 12231576, 5856300);
		genEntityInfo(LOTREntityDunlendingBannerBearer.class, "DunlendingBannerBearer", 106, 5192753, 9337975);
		genEntityInfo(LOTREntityUrukHaiBannerBearer.class, "UrukHaiBannerBearer", 107, 2369050, 5790015);
		genEntityInfo(LOTREntityDwarfBannerBearer.class, "DwarfBannerBearer", 108, 2238506, 7108730);
		genEntityInfo(LOTREntityAngmarBannerBearer.class, "AngmarBannerBearer", 109, 3224873, 5601097);
		genEntityInfo(LOTREntityNearHaradBannerBearer.class, "NearHaradBannerBearer", 110, 2171169, 11868955);
		genEntityInfo(LOTREntityHighElfBannerBearer.class, "HighElfBannerBearer", 111, 14935016, 7040410);
		genEntityInfo(LOTREntityJungleScorpion.class, "JungleScorpion", 112, 2630945, 1315088);
		genEntityInfo(LOTREntityDesertScorpion.class, "DesertScorpion", 113, 16376764, 11772801);
		genEntityInfo(LOTREntityBird.class, "Bird", 114, 7451872, 7451872);
		genEntityInfo(LOTREntityCrebain.class, "Crebain", 115, 2434341, 10490368);
		genEntityInfo(LOTREntityCamel.class, "Camel", 116, 13150061, 9203768);
		genEntityInfo(LOTREntityNearHaradrimArcher.class, "NearHaradrimArcher", 117, 2171169, 11868955);
		genEntityInfo(LOTREntityNearHaradrimWarlord.class, "NearHaradrimWarlord", 118, 2171169, 11868955);
		genEntityInfo(LOTREntityBlueDwarf.class, "BlueDwarf", 119, 16353133, 15357472);
		genEntityInfo(LOTREntityBlueDwarfWarrior.class, "BlueDwarfWarrior", 120, 3161673, 6257551);
		genEntityInfo(LOTREntityBlueDwarfAxeThrower.class, "BlueDwarfAxeThrower", 121, 3161673, 6257551);
		genEntityInfo(LOTREntityBlueDwarfBannerBearer.class, "BlueDwarfBannerBearer", 122, 3161673, 6257551);
		genEntityInfo(LOTREntityBlueDwarfCommander.class, "BlueDwarfCommander", 123, 3161673, 6257551);
		genEntityInfo(LOTREntityBlueDwarfMiner.class, "BlueDwarfMiner", 124, 16353133, 15357472);
		genEntityInfo(LOTREntitySouthronBrewer.class, "NearHaradDrinksTrader", 125, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronMiner.class, "NearHaradMineralsTrader", 126, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronFlorist.class, "NearHaradPlantsTrader", 127, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronButcher.class, "NearHaradFoodTrader", 128, 10779229, 2960685);
		genEntityInfo(LOTREntityBlueDwarfMerchant.class, "BlueDwarfMerchant", 129, 16353133, 15357472);
		genEntityInfo(LOTREntityBandit.class, "Bandit", 130, 16225652, 5323553);
		genEntityInfo(LOTREntityRangerNorthBannerBearer.class, "RangerNorthBannerBearer", 131, 4473912, 2302748);
		genEntityInfo(LOTREntityElk.class, "Elk", 132, 15459801, 11905424);
		genEntityInfo(LOTREntityGondorTowerGuard.class, "GondorTowerGuard", 133, 5327948, 15063770);
		genEntityInfo(LOTREntityNearHaradMerchant.class, "NearHaradMerchant", 134, 10779229, 2960685);
		genEntityInfo(LOTREntityHaradPyramidWraith.class, "HaradPyramidWraith", 135, 10854007, 15590575);
		genEntityInfo(LOTREntityDolGuldurOrc.class, "DolGuldurOrc", 136, 4408654, 2040101);
		genEntityInfo(LOTREntityDolGuldurOrcArcher.class, "DolGuldurOrcArcher", 137, 4408654, 2040101);
		genEntityInfo(LOTREntityDolGuldurBannerBearer.class, "DolGuldurBannerBearer", 138, 4408654, 2040101);
		genEntityInfo(LOTREntityDolGuldurOrcChieftain.class, "DolGuldurChieftain", 139, 4408654, 2040101);
		genEntityInfo(LOTREntityMirkTroll.class, "MirkTroll", 140, 4408654, 2040101);
		genEntityInfo(LOTREntityGundabadBannerBearer.class, "GundabadBannerBearer", 141, 3352346, 8548435);
		genEntityInfo(LOTREntityTermite.class, "Termite", 142, 12748857, 7948066);
		genEntityInfo(LOTREntityDikDik.class, "DikDik", 143, 12023867, 6833961);
		genEntityInfo(LOTREntityBlackUruk.class, "BlackUruk", 144, 988430, 2830632);
		genEntityInfo(LOTREntityBlackUrukArcher.class, "BlackUrukArcher", 145, 988430, 2830632);
		genEntityInfo(LOTREntityHalfTroll.class, "HalfTroll", 146, 6903359, 3614236);
		genEntityInfo(LOTREntityRangerIthilienCaptain.class, "RangerIthilienCaptain", 147, 4015141, 1382669);
		genEntityInfo(LOTREntityRangerIthilienBannerBearer.class, "RangerIthilienBannerBearer", 148, 4015141, 1382669);
		genEntityInfo(LOTREntityHalfTrollWarrior.class, "HalfTrollWarrior", 149, 9403002, 6244662);
		genEntityInfo(LOTREntityHalfTrollBannerBearer.class, "HalfTrollBannerBearer", 150, 9403002, 6244662);
		genEntityInfo(LOTREntityHalfTrollWarlord.class, "HalfTrollWarlord", 151, 9403002, 6244662);
		genEntityInfo(LOTREntityAngmarOrcTrader.class, "AngmarOrcTrader", 152, 5979436, 13421772);
		genEntityInfo(LOTREntityDolGuldurOrcTrader.class, "DolGuldurOrcTrader", 153, 4408654, 2040101);
		genEntityInfo(LOTREntityHalfTrollScavenger.class, "HalfTrollScavenger", 154, 6903359, 3614236);
		genEntityInfo(LOTREntityGaladhrimSmith.class, "GaladhrimSmith", 155, 9337185, 15920555);
		genEntityInfo(LOTREntityHighElfSmith.class, "HighElfSmith", 156, 16761223, 15721387);
		genEntityInfo(LOTREntityWoodElfSmith.class, "WoodElfSmith", 157, 2314529, 16764574);
		genEntityInfo(LOTREntitySwanKnight.class, "SwanKnight", 158, 2302535, 15918822);
		genEntityInfo(LOTREntityDolAmrothCaptain.class, "DolAmrothCaptain", 159, 2302535, 15918822);
		genEntityInfo(LOTREntityDolAmrothBannerBearer.class, "DolAmrothBannerBearer", 160, 3227005, 14278898);
		genEntityInfo(LOTREntitySwan.class, "Swan", 161, 16119285, 15571785);
		genEntityInfo(LOTREntityMoredain.class, "Moredain", 162, 5323303, 2168848);
		genEntityInfo(LOTREntityMoredainWarrior.class, "MoredainWarrior", 163, 8998697, 5057302);
		genEntityInfo(LOTREntityMoredainBannerBearer.class, "MoredainBannerBearer", 164, 8998697, 5057302);
		genEntityInfo(LOTREntityMoredainChieftain.class, "MoredainChieftain", 165, 13807978, 11166513);
		genEntityInfo(LOTREntityMoredainHuntsman.class, "MoredainHuntsman", 166, 5323303, 2168848);
		genEntityInfo(LOTREntityMoredainHutmaker.class, "MoredainHutmaker", 167, 5323303, 2168848);
		genEntityInfo(LOTREntityDunlendingBerserker.class, "DunlendingBerserker", 168, 5192753, 16050121);
		genEntityInfo(LOTREntityAngmarHillman.class, "AngmarHillman", 169, 11828586, 2891544);
		genEntityInfo(LOTREntityAngmarHillmanWarrior.class, "AngmarHillmanWarrior", 170, 11828586, 2891544);
		genEntityInfo(LOTREntityAngmarHillmanChieftain.class, "AngmarHillmanChieftain", 171, 11828586, 2891544);
		genEntityInfo(LOTREntityAngmarHillmanBannerBearer.class, "AngmarHillmanBannerBearer", 172, 11828586, 2891544);
		genEntityInfo(LOTREntityAngmarHillmanAxeThrower.class, "AngmarHillmanAxeThrower", 173, 11828586, 2891544);
		genEntityInfo(LOTREntityDunlendingAxeThrower.class, "DunlendingAxeThrower", 174, 5192753, 9337975);
		genEntityInfo(LOTREntityIronHillsMerchant.class, "IronHillsMerchant", 175, 16353133, 15357472);
		genEntityInfo(LOTREntityMallornEnt.class, "MallornEnt", 176);
		genEntityInfo(LOTREntityScrapTrader.class, "ScrapTrader", 177, 16225652, 5323553);
		genEntityInfo(LOTREntityTauredain.class, "Tauredain", 178, 4468770, 12948008);
		genEntityInfo(LOTREntityTauredainWarrior.class, "TauredainWarrior", 179, 5652267, 9165389);
		genEntityInfo(LOTREntityTauredainBannerBearer.class, "TauredainBannerBearer", 180, 5652267, 9165389);
		genEntityInfo(LOTREntityTauredainChieftain.class, "TauredainChieftain", 181, 5652267, 9165389);
		genEntityInfo(LOTREntityTauredainBlowgunner.class, "TauredainBlowgunner", 182, 5652267, 9165389);
		genEntityInfo(LOTREntityBarrowWight.class, "BarrowWight", 183, 529926, 3111505);
		genEntityInfo(LOTREntityTauredainShaman.class, "TauredainShaman", 184, 4468770, 12948008);
		genEntityInfo(LOTREntityGaladhrimWarden.class, "GaladhrimWarden", 185, 10527645, 8027255);
		genEntityInfo(LOTREntityTauredainFarmer.class, "TauredainFarmer", 186, 4468770, 12948008);
		genEntityInfo(LOTREntityTauredainFarmhand.class, "TauredainFarmhand", 187, 4468770, 12948008);
		genEntityInfo(LOTREntityDwarfSmith.class, "DwarfSmith", 188, 16353133, 15357472);
		genEntityInfo(LOTREntityBlueMountainsSmith.class, "BlueDwarfSmith", 189, 16353133, 15357472);
		genEntityInfo(LOTREntityTauredainPyramidWraith.class, "TauredainPyramidWraith", 190, 12698049, 4802889);
		genEntityInfo(LOTREntityGundabadUruk.class, "GundabadUruk", 191, 2563350, 6382678);
		genEntityInfo(LOTREntityGundabadUrukArcher.class, "GundabadUrukArcher", 192, 2563350, 6382678);
		genEntityInfo(LOTREntityIsengardSnaga.class, "IsengardSnaga", 193, 4339500, 8352349);
		genEntityInfo(LOTREntityIsengardSnagaArcher.class, "IsengardSnagaArcher", 194, 4339500, 8352349);
		genEntityInfo(LOTREntityBanditHarad.class, "BanditHarad", 195, 10779229, 2960685);
		genEntityInfo(LOTREntityDeer.class, "Deer", 196, 5978669, 11968394);
		genEntityInfo(LOTREntityDaleMan.class, "DaleMan", 197, 16755851, 5252113);
		genEntityInfo(LOTREntityDaleLevyman.class, "DaleLevyman", 198, 7034184, 5252113);
		genEntityInfo(LOTREntityDaleSoldier.class, "DaleSoldier", 199, 11776947, 481419);
		genEntityInfo(LOTREntityDaleArcher.class, "DaleArcher", 200, 11776947, 481419);
		genEntityInfo(LOTREntityDaleBannerBearer.class, "DaleBannerBearer", 201, 11776947, 481419);
		genEntityInfo(LOTREntityDaleCaptain.class, "DaleCaptain", 202, 11776947, 481419);
		genEntityInfo(LOTREntityDaleBlacksmith.class, "DaleBlacksmith", 203, 16755851, 5252113);
		genEntityInfo(LOTREntityDorwinionMan.class, "DorwinionMan", 204, 16755851, 12213157);
		genEntityInfo(LOTREntityDorwinionGuard.class, "DorwinionGuard", 205, 9005901, 6178167);
		genEntityInfo(LOTREntityDorwinionCaptain.class, "DorwinionCaptain", 206, 9005901, 6178167);
		genEntityInfo(LOTREntityDorwinionBannerBearer.class, "DorwinionBannerBearer", 207, 9005901, 6178167);
		genEntityInfo(LOTREntityDorwinionElf.class, "DorwinionElf", 208, 16761223, 8538746);
		genEntityInfo(LOTREntityDorwinionElfWarrior.class, "DorwinionElfWarrior", 209, 13420999, 8407696);
		genEntityInfo(LOTREntityDorwinionElfBannerBearer.class, "DorwinionElfBannerBearer", 210, 13420999, 8407696);
		genEntityInfo(LOTREntityDorwinionElfCaptain.class, "DorwinionElfCaptain", 211, 13420999, 8407696);
		genEntityInfo(LOTREntityDaleBaker.class, "DaleBaker", 212, 16755851, 5252113);
		genEntityInfo(LOTREntityDorwinionElfVintner.class, "DorwinionElfVintner", 213, 9721246, 5648736);
		genEntityInfo(LOTREntityDorwinionVinehand.class, "DorwinionVinehand", 214, 16755851, 12213157);
		genEntityInfo(LOTREntityDorwinionVinekeeper.class, "DorwinionVinekeeper", 215, 16755851, 12213157);
		genEntityInfo(LOTREntityDorwinionMerchantElf.class, "DorwinionMerchant", 216, 16761223, 8538746);
		genEntityInfo(LOTREntityDaleMerchant.class, "DaleMerchant", 217, 16755851, 5252113);
		genEntityInfo(LOTREntityAurochs.class, "Aurochs", 218, 7488812, 3217935);
		genEntityInfo(LOTREntityKineAraw.class, "KineAraw", 219, 16702665, 12890019);
		genEntityInfo(LOTREntityDorwinionCrossbower.class, "DorwinionCrossbower", 220, 9005901, 6178167);
		genEntityInfo(LOTREntityLossarnachAxeman.class, "LossarnachAxeman", 221, 11578026, 3812901);
		genEntityInfo(LOTREntityLossarnachBannerBearer.class, "LossarnachBannerBearer", 222, 11578026, 3812901);
		genEntityInfo(LOTREntityBlackUrukBannerBearer.class, "BlackUrukBannerBearer", 223, 988430, 2830632);
		genEntityInfo(LOTREntityPelargirMarine.class, "PelargirMarine", 224, 13090494, 1475447);
		genEntityInfo(LOTREntityPelargirBannerBearer.class, "PelargirBannerBearer", 225, 13090494, 1475447);
		genEntityInfo(LOTREntityPinnathGelinSoldier.class, "PinnathGelinSoldier", 226, 11183011, 29235);
		genEntityInfo(LOTREntityPinnathGelinBannerBearer.class, "PinnathGelinBannerBearer", 227, 11183011, 29235);
		genEntityInfo(LOTREntityBlackrootSoldier.class, "BlackrootSoldier", 228, 11183011, 3881016);
		genEntityInfo(LOTREntityBlackrootBannerBearer.class, "BlackrootBannerBearer", 229, 11183011, 3881016);
		genEntityInfo(LOTREntityBlackrootArcher.class, "BlackrootArcher", 230, 11183011, 3881016);
		genEntityInfo(LOTREntityGondorLevyman.class, "GondorLevyman", 231, 10789794, 6833716);
		genEntityInfo(LOTREntityNanUngolBannerBearer.class, "NanUngolBannerBearer", 232, 3353378, 7042407);
		genEntityInfo(LOTREntityMinasMorgulBannerBearer.class, "MinasMorgulBannerBearer", 233, 3353378, 7042407);
		genEntityInfo(LOTREntityDolAmrothSoldier.class, "DolAmrothSoldier", 234, 3227005, 14278898);
		genEntityInfo(LOTREntityDolAmrothArcher.class, "DolAmrothArcher", 235, 3227005, 14278898);
		genEntityInfo(LOTREntityGondorFarmer.class, "GondorFarmer", 236, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorFarmhand.class, "GondorFarmhand", 237, 13547685, 5652538);
		genEntityInfo(LOTREntityLebenninLevyman.class, "LebenninLevyman", 238, 14866637, 3573666);
		genEntityInfo(LOTREntityLebenninBannerBearer.class, "LebenninBannerBearer", 239, 14866637, 3573666);
		genEntityInfo(LOTREntityGondorBartender.class, "GondorBartender", 240, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorGreengrocer.class, "GondorGreengrocer", 241, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorLumberman.class, "GondorLumberman", 242, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorMason.class, "GondorMason", 243, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorBrewer.class, "GondorBrewer", 244, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorFlorist.class, "GondorFlorist", 245, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorButcher.class, "GondorButcher", 246, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorFishmonger.class, "GondorFishmonger", 247, 13547685, 5652538);
		genEntityInfo(LOTREntityGondorBaker.class, "GondorBaker", 248, 13547685, 5652538);
		genEntityInfo(LOTREntityLossarnachCaptain.class, "LossarnachCaptain", 249, 11578026, 3812901);
		genEntityInfo(LOTREntityPelargirCaptain.class, "PelargirCaptain", 250, 13090494, 1475447);
		genEntityInfo(LOTREntityPinnathGelinCaptain.class, "PinnathGelinCaptain", 251, 11183011, 29235);
		genEntityInfo(LOTREntityBlackrootCaptain.class, "BlackrootCaptain", 252, 11183011, 3881016);
		genEntityInfo(LOTREntityLebenninCaptain.class, "LebenninCaptain", 253, 5327948, 15063770);
		genEntityInfo(LOTREntityLamedonSoldier.class, "LamedonSoldier", 254, 12103600, 3624035);
		genEntityInfo(LOTREntityLamedonArcher.class, "LamedonArcher", 255, 12103600, 3624035);
		genEntityInfo(LOTREntityLamedonBannerBearer.class, "LamedonBannerBearer", 256, 12103600, 3624035);
		genEntityInfo(LOTREntityLamedonCaptain.class, "LamedonCaptain", 257, 12103600, 3624035);
		genEntityInfo(LOTREntityLamedonHillman.class, "LamedonHillman", 258, 13547685, 2108991);
		genEntityInfo(LOTREntityRohanFarmhand.class, "RohanFarmhand", 259, 16424833, 13406801);
		genEntityInfo(LOTREntityGorcrow.class, "Gorcrow", 260, 928034, 5451403);
		genEntityInfo(LOTREntitySeagull.class, "Seagull", 261, 15920107, 13997863);
		genEntityInfo(LOTREntityRohanFarmer.class, "RohanFarmer", 262, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanLumberman.class, "RohanLumberman", 263, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanBuilder.class, "RohanBuilder", 264, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanBrewer.class, "RohanBrewer", 265, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanButcher.class, "RohanButcher", 266, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanFishmonger.class, "RohanFishmonger", 267, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanBaker.class, "RohanBaker", 268, 16424833, 13406801);
		genEntityInfo(LOTREntityRohanOrcharder.class, "RohanOrcharder", 269, 16424833, 13406801);
		genEntityInfo(LOTREntityDunedainBlacksmith.class, "DunedainBlacksmith", 270, 15638664, 6832694);
		genEntityInfo(LOTREntityRohanStablemaster.class, "RohanStablemaster", 271, 16424833, 13406801);
		genEntityInfo(LOTREntityBear.class, "Bear", 272, 7492416, 4008994);
		genEntityInfo(LOTREntityEasterling.class, "Easterling", 273, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingWarrior.class, "EasterlingWarrior", 274, 7486267, 15251832);
		genEntityInfo(LOTREntityEasterlingBannerBearer.class, "EasterlingBannerBearer", 275, 7486267, 15251832);
		genEntityInfo(LOTREntityEasterlingArcher.class, "EasterlingArcher", 276, 7486267, 15251832);
		genEntityInfo(LOTREntityEasterlingBlacksmith.class, "EasterlingBlacksmith", 277, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingWarlord.class, "EasterlingWarlord", 278, 14265689, 12004653);
		genEntityInfo(LOTREntityEasterlingFireThrower.class, "EasterlingFireThrower", 279, 7486267, 15251832);
		genEntityInfo(LOTREntityEasterlingLevyman.class, "EasterlingLevyman", 280, 16093531, 6176050);
		genEntityInfo(LOTREntityDorwinionMerchantMan.class, "DorwinionMerchantMan", 281, 16755851, 12213157);
		genEntityInfo(LOTREntityEasterlingGoldWarrior.class, "EasterlingGoldWarrior", 282, 14265689, 12004653);
		genEntityInfo(LOTREntityEasterlingLumberman.class, "EasterlingLumberman", 283, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingMason.class, "EasterlingMason", 284, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingButcher.class, "EasterlingButcher", 285, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingBrewer.class, "EasterlingBrewer", 286, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingFishmonger.class, "EasterlingFishmonger", 287, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingBaker.class, "EasterlingBaker", 288, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingHunter.class, "EasterlingHunter", 289, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingFarmer.class, "EasterlingFarmer", 290, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingGoldsmith.class, "EasterlingGoldsmith", 291, 16093531, 6176050);
		genEntityInfo(LOTREntityEasterlingBartender.class, "EasterlingBartender", 292, 16093531, 6176050);
		genEntityInfo(LOTREntityDorwinionElfArcher.class, "DorwinionElfArcher", 293, 13420999, 8407696);
		genEntityInfo(LOTREntityEasterlingFarmhand.class, "EasterlingFarmhand", 294, 16093531, 6176050);
		genEntityInfo(LOTREntityRivendellElf.class, "RivendellElf", 295, 16761223, 15721387);
		genEntityInfo(LOTREntityRivendellWarrior.class, "RivendellWarrior", 296, 14738662, 10723248);
		genEntityInfo(LOTREntityRivendellLord.class, "RivendellLord", 297, 14738662, 10723248);
		genEntityInfo(LOTREntityRivendellBannerBearer.class, "RivendellBannerBearer", 298, 14738662, 10723248);
		genEntityInfo(LOTREntityRivendellSmith.class, "RivendellSmith", 299, 16761223, 15721387);
		genEntityInfo(LOTREntityEsgarothBannerBearer.class, "EsgarothBannerBearer", 300, 11776947, 481419);
		genEntityInfo(LOTREntityRivendellTrader.class, "RivendellTrader", 301, 869480, 15003391);
		genEntityInfo(LOTREntityFish.class, "Fish", 302, 7053203, 11913189);
		genEntityInfo(LOTREntityGundabadOrcTrader.class, "GundabadOrcTrader", 303, 5979436, 13421772);
		genEntityInfo(LOTREntityNearHaradBlacksmith.class, "NearHaradBlacksmith", 304, 10779229, 2960685);
		genEntityInfo(LOTREntitySnowTroll.class, "SnowTroll", 305, 14606046, 11059905);
		genEntityInfo(LOTREntityHarnedhrim.class, "Harnedhrim", 306, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorWarrior.class, "HarnedorWarrior", 307, 7016721, 14852422);
		genEntityInfo(LOTREntityHarnedorArcher.class, "HarnedorArcher", 308, 7016721, 14852422);
		genEntityInfo(LOTREntityHarnedorBannerBearer.class, "HarnedorBannerBearer", 309, 7016721, 14852422);
		genEntityInfo(LOTREntityUmbarian.class, "Umbarian", 310, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarWarrior.class, "UmbarWarrior", 311, 2960680, 13540692);
		genEntityInfo(LOTREntityUmbarArcher.class, "UmbarArcher", 312, 2960680, 13540692);
		genEntityInfo(LOTREntityUmbarBannerBearer.class, "UmbarBannerBearer", 313, 2960680, 13540692);
		genEntityInfo(LOTREntityMoredainMercenary.class, "MoredainMercenary", 314, 8998697, 14528351);
		genEntityInfo(LOTREntityGulfHaradrim.class, "GulfHaradrim", 315, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfHaradWarrior.class, "GulfWarrior", 316, 7478299, 12827550);
		genEntityInfo(LOTREntityGulfHaradArcher.class, "GulfArcher", 317, 7478299, 12827550);
		genEntityInfo(LOTREntityGulfHaradBannerBearer.class, "GulfBannerBearer", 318, 7478299, 12827550);
		genEntityInfo(LOTREntityCorsair.class, "Corsair", 319, 5521973, 12813617);
		genEntityInfo(LOTREntityNomad.class, "Nomad", 320, 8278064, 853765);
		genEntityInfo(LOTREntityNomadWarrior.class, "NomadWarrior", 321, 10063441, 5658198);
		genEntityInfo(LOTREntityNomadArcher.class, "NomadArcher", 322, 10063441, 5658198);
		genEntityInfo(LOTREntityNomadBannerBearer.class, "NomadBannerBearer", 323, 10063441, 5658198);
		genEntityInfo(LOTREntityHarnedorWarlord.class, "HarnedorWarlord", 324, 7016721, 14852422);
		genEntityInfo(LOTREntityUmbarCaptain.class, "UmbarCaptain", 325, 2960680, 13540692);
		genEntityInfo(LOTREntityCorsairCaptain.class, "CorsairCaptain", 326, 5521973, 12813617);
		genEntityInfo(LOTREntityNomadChieftain.class, "NomadChieftain", 327, 10063441, 5658198);
		genEntityInfo(LOTREntityGulfHaradWarlord.class, "GulfWarlord", 328, 7478299, 12827550);
		genEntityInfo(LOTREntitySouthronChampion.class, "SouthronChampion", 329, 2171169, 11868955);
		genEntityInfo(LOTREntityHaradSlave.class, "HaradSlave", 330, 9860177, 5579298);
		genEntityInfo(LOTREntityCorsairSlaver.class, "CorsairSlaver", 331, 5521973, 12813617);
		genEntityInfo(LOTREntityHarnedorBlacksmith.class, "HarnedorBlacksmith", 332, 9854777, 1181187);
		genEntityInfo(LOTREntityUmbarBlacksmith.class, "UmbarBlacksmith", 333, 10779229, 2960685);
		genEntityInfo(LOTREntityGulfBlacksmith.class, "GulfBlacksmith", 334, 9854777, 1181187);
		genEntityInfo(LOTREntityGondorRenegade.class, "GondorRenegade", 335, 1776411, 13936679);
		genEntityInfo(LOTREntityNomadMerchant.class, "NomadMerchant", 336, 13551017, 7825215);
		genEntityInfo(LOTREntityHarnedorBartender.class, "HarnedorBartender", 337, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorLumberman.class, "HarnedorLumberman", 338, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorMason.class, "HarnedorMason", 339, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorButcher.class, "HarnedorButcher", 340, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorBrewer.class, "HarnedorBrewer", 341, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorFishmonger.class, "HarnedorFishmonger", 342, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorBaker.class, "HarnedorBaker", 343, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorHunter.class, "HarnedorHunter", 344, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorMiner.class, "HarnedorMiner", 345, 9854777, 1181187);
		genEntityInfo(LOTREntitySouthronLumberman.class, "SouthronLumberman", 346, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronMason.class, "SouthronMason", 347, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronFishmonger.class, "SouthronFishmonger", 348, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronBaker.class, "SouthronBaker", 349, 10779229, 2960685);
		genEntityInfo(LOTREntitySouthronGoldsmith.class, "SouthronGoldsmith", 350, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarLumberman.class, "UmbarLumberman", 351, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarButcher.class, "UmbarButcher", 352, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarBrewer.class, "UmbarBrewer", 353, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarFishmonger.class, "UmbarFishmonger", 354, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarBaker.class, "UmbarBaker", 355, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarFlorist.class, "UmbarFlorist", 356, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarMiner.class, "UmbarMiner", 357, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarGoldsmith.class, "UmbarGoldsmith", 358, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarMason.class, "UmbarMason", 359, 10779229, 2960685);
		genEntityInfo(LOTREntityNomadMason.class, "NomadMason", 360, 8278064, 853765);
		genEntityInfo(LOTREntityNomadBrewer.class, "NomadBrewer", 361, 8278064, 853765);
		genEntityInfo(LOTREntityNomadMiner.class, "NomadMiner", 362, 8278064, 853765);
		genEntityInfo(LOTREntityGulfMason.class, "GulfMason", 363, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfButcher.class, "GulfButcher", 364, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfBrewer.class, "GulfBrewer", 365, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfFishmonger.class, "GulfFishmonger", 366, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfBaker.class, "GulfBaker", 367, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfMiner.class, "GulfMiner", 368, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfGoldsmith.class, "GulfGoldsmith", 369, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfLumberman.class, "GulfLumberman", 370, 9854777, 1181187);
		genEntityInfo(LOTREntityGulfHunter.class, "GulfHunter", 371, 9854777, 1181187);
		genEntityInfo(LOTREntityNomadArmourer.class, "NomadArmourer", 372, 8278064, 853765);
		genEntityInfo(LOTREntitySouthronBartender.class, "SouthronBartender", 373, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarBartender.class, "UmbarBartender", 374, 10779229, 2960685);
		genEntityInfo(LOTREntityGulfBartender.class, "GulfBartender", 375, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorFarmhand.class, "HarnedorFarmhand", 376, 9854777, 1181187);
		genEntityInfo(LOTREntityHarnedorFarmer.class, "HarnedorFarmer", 377, 9854777, 1181187);
		genEntityInfo(LOTREntitySouthronFarmer.class, "SouthronFarmer", 378, 10779229, 2960685);
		genEntityInfo(LOTREntityUmbarFarmer.class, "UmbarFarmer", 379, 10779229, 2960685);
		genEntityInfo(LOTREntityGulfFarmer.class, "GulfFarmer", 380, 9854777, 1181187);
		genEntityInfo(LOTREntityTauredainSmith.class, "TauredainSmith", 381, 4468770, 12948008);
		genEntityInfo(LOTREntityWhiteOryx.class, "WhiteOryx", 382, 16381146, 8154724);
		genEntityInfo(LOTREntityBlackUrukCaptain.class, "BlackUrukCaptain", 383, 988430, 2830632);
		genEntityInfo(LOTREntityWickedDwarf.class, "WickedDwarf", 384, 14516076, 8869453);
		genEntityInfo(LOTREntityBreeMan.class, "BreeMan", 385, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeGuard.class, "BreeGuard", 386, 9335640, 3681573);
		genEntityInfo(LOTREntityBreeBannerBearer.class, "BreeBannerBearer", 387, 9335640, 3681573);
		genEntityInfo(LOTREntityBreeCaptain.class, "BreeCaptain", 388, 9335640, 3681573);
		genEntityInfo(LOTREntityBreeBlacksmith.class, "BreeBlacksmith", 389, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeInnkeeper.class, "BreeInnkeeper", 390, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeHobbit.class, "BreeHobbit", 391, 16752511, 8010275);
		genEntityInfo(LOTREntityRuffianSpy.class, "RuffianSpy", 392, 14713187, 5191213);
		genEntityInfo(LOTREntityRuffianBrute.class, "RuffianBrute", 393, 14713187, 5191213);
		genEntityInfo(LOTREntityBreeHobbitInnkeeper.class, "BreeHobbitInnkeeper", 394, 16752511, 8010275);
		genEntityInfo(LOTREntityBreeBaker.class, "BreeBaker", 395, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeButcher.class, "BreeButcher", 396, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeBrewer.class, "BreeBrewer", 397, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeMason.class, "BreeMason", 398, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeLumberman.class, "BreeLumberman", 399, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeFlorist.class, "BreeFlorist", 400, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeFarmer.class, "BreeFarmer", 401, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeFarmhand.class, "BreeFarmhand", 402, 14254950, 6573367);
		genEntityInfo(LOTREntityBreeHobbitBaker.class, "BreeHobbitBaker", 403, 16752511, 8010275);
		genEntityInfo(LOTREntityBreeHobbitButcher.class, "BreeHobbitButcher", 404, 16752511, 8010275);
		genEntityInfo(LOTREntityBreeHobbitBrewer.class, "BreeHobbitBrewer", 405, 16752511, 8010275);
		genEntityInfo(LOTREntityBreeHobbitFlorist.class, "BreeHobbitFlorist", 406, 16752511, 8010275);
		genEntityInfo(LOTREntityUtumnoOrc.class, "UtumnoOrc", 800, 2694951, 10377233);
		genEntityInfo(LOTREntityUtumnoOrcArcher.class, "UtumnoOrcArcher", 801, 2694951, 10377233);
		genEntityInfo(LOTREntityUtumnoWarg.class, "UtumnoWarg", 802, 4600617, 2694422);
		genEntityInfo(LOTREntityUtumnoIceWarg.class, "UtumnoIceWarg", 803, 15066080, 9348269);
		genEntityInfo(LOTREntityUtumnoObsidianWarg.class, "UtumnoObsidianWarg", 804, 2960169, 1644310);
		genEntityInfo(LOTREntityUtumnoFireWarg.class, "UtumnoFireWarg", 805, 6958364, 13530368);
		genEntityInfo(LOTREntityUtumnoIceSpider.class, "UtumnoIceSpider", 806, 15594495, 7697919);
		genEntityInfo(LOTREntityBalrog.class, "Balrog", 807, 1772037, 13009920);
		genEntityInfo(LOTREntityTormentedElf.class, "TormentedElf", 808, 14079919, 4337710);
		genEntityInfo(LOTREntityUtumnoTroll.class, "UtumnoTroll", 809, 10580563, 7422265);
		genEntityInfo(LOTREntityUtumnoSnowTroll.class, "UtumnoSnowTroll", 810, 14606046, 11059905);
		genEntityInfo(LOTREntityGollum.class, "Gollum", 1001, 13417872, 9471333);
		genEntityInfo(LOTREntitySaruman.class, "Saruman", 1002, 15132390, 11776947);
		genEntityInfo(LOTREntityGandalf.class, "Gandalf", 1003, 9605778, 5923198);

		genStructureInfo(1, LOTRWorldGenHobbitHole.class, "HobbitHole", 2727977, 8997164);
		genStructureInfo(2, LOTRWorldGenHobbitTavern.class, "HobbitTavern", 9324081, 15975807);
		genStructureInfo(3, LOTRWorldGenHobbitPicnicBench.class, "HobbitPicnicBench", 7032622, 13882323);
		genStructureInfo(4, LOTRWorldGenHobbitWindmill.class, "HobbitWindmill", 9324081, 15975807);
		genStructureInfo(5, LOTRWorldGenHobbitFarm.class, "HobbitFarm", 9324081, 15975807);
		genStructureInfo(6, LOTRWorldGenHayBales.class, "HayBale", 14863437, 11499334);
		genStructureInfo(7, LOTRWorldGenHobbitHouse.class, "HobbitHouse", 9324081, 15975807);
		genStructureInfo(8, LOTRWorldGenHobbitBurrow.class, "HobbitBurrow", 9324081, 15975807);
		genStructureInfo(20, LOTRWorldGenBreeHouse.class, "BreeHouse", 7366748, 13547379);
		genStructureInfo(21, LOTRWorldGenBreeOffice.class, "BreeOffice", 7366748, 13547379);
		genStructureInfo(22, LOTRWorldGenBreeSmithy.class, "BreeSmithy", 7895160, 13547379);
		genStructureInfo(23, LOTRWorldGenBreeInn.class, "BreeInn", 7366748, 13547379);
		genStructureInfo(24, LOTRWorldGenBreeWell.class, "BreeWell", 7366748, 13547379);
		genStructureInfo(25, LOTRWorldGenBreeLampPost.class, "BreeLampPost", 7366748, 13547379);
		genStructureInfo(26, LOTRWorldGenBreeBarn.class, "BreeBarn", 7366748, 13547379);
		genStructureInfo(27, LOTRWorldGenBreeRuffianHouse.class, "BreeRuffianHouse", 7366748, 13547379);
		genStructureInfo(28, LOTRWorldGenBreeStable.class, "BreeStables", 7366748, 13547379);
		genStructureInfo(29, LOTRWorldGenBreeGarden.class, "BreeGarden", 3056942, 9012349);
		genStructureInfo(30, LOTRWorldGenBreeHobbitBurrow.class, "BreeHobbitBurrow", 7366748, 13547379);
		genStructureInfo(31, LOTRWorldGenBreeMarketStall.Baker.class, "BreeMarketBaker", 16246393, 13547379);
		genStructureInfo(32, LOTRWorldGenBreeMarketStall.Butcher.class, "BreeMarketButcher", 14173509, 13547379);
		genStructureInfo(33, LOTRWorldGenBreeMarketStall.Brewer.class, "BreeMarketBrewer", 11368000, 13547379);
		genStructureInfo(34, LOTRWorldGenBreeMarketStall.Mason.class, "BreeMarketMason", 8948105, 13547379);
		genStructureInfo(35, LOTRWorldGenBreeMarketStall.Lumber.class, "BreeMarketLumber", 7160619, 13547379);
		genStructureInfo(36, LOTRWorldGenBreeMarketStall.Smith.class, "BreeMarketSmith", 5658198, 13547379);
		genStructureInfo(37, LOTRWorldGenBreeMarketStall.Florist.class, "BreeMarketFlorist", 10966702, 13547379);
		genStructureInfo(38, LOTRWorldGenBreeMarketStall.Farmer.class, "BreeMarketFarmer", 5137960, 13547379);
		genStructureInfo(39, LOTRWorldGenBreeMarket.class, "BreeMarket", 7366748, 13547379);
		genStructureInfo(40, new LOTRVillageGenBree(LOTRBiome.breeland, 1.0f), "BreeHamlet", 7366748, 13547379, new IVillageProperties<LOTRVillageGenBree.Instance>() {

			@Override
			public void apply(LOTRVillageGenBree.Instance instance) {
				instance.villageType = LOTRVillageGenBree.VillageType.HAMLET;
			}
		});
		genStructureInfo(41, new LOTRVillageGenBree(LOTRBiome.breeland, 1.0f), "BreeVillage", 7366748, 13547379, new IVillageProperties<LOTRVillageGenBree.Instance>() {

			@Override
			public void apply(LOTRVillageGenBree.Instance instance) {
				instance.villageType = LOTRVillageGenBree.VillageType.VILLAGE;
			}
		});
		genStructureInfo(42, LOTRWorldGenBreeGate.class, "BreeGate", 7366748, 13547379);
		genStructureInfo(43, LOTRWorldGenBreeGatehouse.class, "BreeGatehouse", 7366748, 13547379);
		genStructureInfo(50, LOTRWorldGenBlueMountainsHouse.class, "BlueMountainsHouse", 10397380, 7633815);
		genStructureInfo(51, LOTRWorldGenBlueMountainsStronghold.class, "BlueMountainsStronghold", 10397380, 7633815);
		genStructureInfo(52, LOTRWorldGenBlueMountainsSmithy.class, "BlueMountainsSmithy", 10397380, 7633815);
		genStructureInfo(60, LOTRWorldGenHighElvenTurret.class, "HighElvenTurret", 13419962, 11380637);
		genStructureInfo(61, LOTRWorldGenRuinedHighElvenTurret.class, "RuinedHighElvenTurret", 13419962, 11380637);
		genStructureInfo(62, LOTRWorldGenHighElvenHall.class, "HighElvenHall", 13419962, 11380637);
		genStructureInfo(63, LOTRWorldGenUnderwaterElvenRuin.class, "UnderwaterElvenRuin", 13419962, 11380637);
		genStructureInfo(64, LOTRWorldGenHighElvenForge.class, "HighElvenForge", 13419962, 11380637);
		genStructureInfo(65, LOTRWorldGenRuinedEregionForge.class, "RuinedEregionForge", 13419962, 11380637);
		genStructureInfo(66, LOTRWorldGenHighElvenTower.class, "HighElvenTower", 13419962, 11380637);
		genStructureInfo(67, LOTRWorldGenTowerHillsTower.class, "TowerHillsTower", 16250346, 14211019);
		genStructureInfo(68, LOTRWorldGenHighElfHouse.class, "HighElfHouse", 13419962, 11380637);
		genStructureInfo(69, LOTRWorldGenRivendellHouse.class, "RivendellHouse", 13419962, 11380637);
		genStructureInfo(70, LOTRWorldGenRivendellHall.class, "RivendellHall", 13419962, 11380637);
		genStructureInfo(71, LOTRWorldGenRivendellForge.class, "RivendellForge", 13419962, 11380637);
		genStructureInfo(80, LOTRWorldGenRuinedDunedainTower.class, "RuinedDunedainTower", 8947848, 6052956);
		genStructureInfo(81, LOTRWorldGenRuinedHouse.class, "RuinedHouse", 8355197, 6838845);
		genStructureInfo(82, LOTRWorldGenRangerTent.class, "RangerTent", 3755037, 4142111);
		genStructureInfo(83, LOTRWorldGenNumenorRuin.class, "NumenorRuin", 8947848, 6052956);
		genStructureInfo(84, LOTRWorldGenBDBarrow.class, "BDBarrow", 6586202, 6505786);
		genStructureInfo(85, LOTRWorldGenRangerWatchtower.class, "RangerWatchtower", 5982252, 13411436);
		genStructureInfo(86, LOTRWorldGenBurntHouse.class, "BurntHouse", 1117449, 3288357);
		genStructureInfo(87, LOTRWorldGenRottenHouse.class, "RottenHouse", 3026204, 5854007);
		genStructureInfo(88, LOTRWorldGenRangerHouse.class, "RangerHouse", 5982252, 13411436);
		genStructureInfo(89, LOTRWorldGenRangerLodge.class, "RangerLodge", 5982252, 13411436);
		genStructureInfo(90, LOTRWorldGenRangerStables.class, "RangerStables", 5982252, 13411436);
		genStructureInfo(91, LOTRWorldGenRangerSmithy.class, "RangerSmithy", 5982252, 13411436);
		genStructureInfo(92, LOTRWorldGenRangerWell.class, "RangerWell", 5982252, 13411436);
		genStructureInfo(93, LOTRWorldGenRangerVillageLight.class, "RangerVillageLight", 5982252, 13411436);
		genStructureInfo(94, new LOTRVillageGenDunedain(LOTRBiome.angle, 1.0f), "DunedainVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenDunedain.Instance>() {

			@Override
			public void apply(LOTRVillageGenDunedain.Instance instance) {
				instance.villageType = LOTRVillageGenDunedain.VillageType.VILLAGE;
			}
		});
		genStructureInfo(95, LOTRWorldGenRangerCamp.class, "RangerCamp", 3755037, 4142111);
		genStructureInfo(120, LOTRWorldGenOrcDungeon.class, "OrcDungeon", 8947848, 6052956);
		genStructureInfo(121, LOTRWorldGenGundabadTent.class, "GundabadTent", 2301210, 131586);
		genStructureInfo(122, LOTRWorldGenGundabadForgeTent.class, "GundabadForgeTent", 2301210, 131586);
		genStructureInfo(123, LOTRWorldGenGundabadCamp.class, "GundabadCamp", 2301210, 131586);
		genStructureInfo(140, LOTRWorldGenAngmarTower.class, "AngmarTower", 3815994, 1644825);
		genStructureInfo(141, LOTRWorldGenAngmarShrine.class, "AngmarShrine", 3815994, 1644825);
		genStructureInfo(142, LOTRWorldGenAngmarWargPit.class, "AngmarWargPit", 3815994, 1644825);
		genStructureInfo(143, LOTRWorldGenAngmarTent.class, "AngmarTent", 2301210, 131586);
		genStructureInfo(144, LOTRWorldGenAngmarForgeTent.class, "AngmarForgeTent", 3815994, 1644825);
		genStructureInfo(145, LOTRWorldGenAngmarCamp.class, "AngmarCamp", 2301210, 131586);
		genStructureInfo(160, LOTRWorldGenAngmarHillmanHouse.class, "AngmarHillmanHouse", 6705465, 3813154);
		genStructureInfo(161, LOTRWorldGenAngmarHillmanChieftainHouse.class, "AngmarHillmanChieftainHouse", 6705465, 3813154);
		genStructureInfo(162, LOTRWorldGenRhudaurCastle.class, "RhudaurCastle", 3815994, 1644825);
		genStructureInfo(200, LOTRWorldGenWoodElfPlatform.class, "WoodElfLookoutPlatform", 2498840, 4932405);
		genStructureInfo(201, LOTRWorldGenWoodElfHouse.class, "WoodElfHouse", 2498840, 1004574);
		genStructureInfo(202, LOTRWorldGenWoodElfTower.class, "WoodElfTower", 12692892, 9733494);
		genStructureInfo(203, LOTRWorldGenRuinedWoodElfTower.class, "RuinedWoodElfTower", 12692892, 9733494);
		genStructureInfo(204, LOTRWorldGenWoodElvenForge.class, "WoodElvenForge", 12692892, 9733494);
		genStructureInfo(220, LOTRWorldGenDolGuldurAltar.class, "DolGuldurAltar", 4408654, 2040101);
		genStructureInfo(221, LOTRWorldGenDolGuldurTower.class, "DolGuldurTower", 4408654, 2040101);
		genStructureInfo(222, LOTRWorldGenDolGuldurSpiderPit.class, "DolGuldurSpiderPit", 4408654, 2040101);
		genStructureInfo(223, LOTRWorldGenDolGuldurTent.class, "DolGuldurTent", 2301210, 131586);
		genStructureInfo(224, LOTRWorldGenDolGuldurForgeTent.class, "DolGuldurForgeTent", 4408654, 2040101);
		genStructureInfo(225, LOTRWorldGenDolGuldurCamp.class, "DolGuldurCamp", 2301210, 131586);
		genStructureInfo(240, LOTRWorldGenDaleWatchtower.class, "DaleWatchtower", 13278568, 6836795);
		genStructureInfo(241, LOTRWorldGenDaleFortress.class, "DaleFortress", 13278568, 6836795);
		genStructureInfo(242, LOTRWorldGenDaleHouse.class, "DaleHouse", 13278568, 6836795);
		genStructureInfo(243, LOTRWorldGenDaleSmithy.class, "DaleSmithy", 13278568, 6836795);
		genStructureInfo(244, LOTRWorldGenDaleVillageTower.class, "DaleVillageTower", 13278568, 6836795);
		genStructureInfo(245, LOTRWorldGenDaleBakery.class, "DaleBakery", 13278568, 6836795);
		genStructureInfo(260, LOTRWorldGenDwarvenMineEntrance.class, "DwarvenMineEntrance", 4935761, 2961971);
		genStructureInfo(261, LOTRWorldGenDwarvenTower.class, "DwarvenTower", 4935761, 2961971);
		genStructureInfo(262, LOTRWorldGenDwarfHouse.class, "DwarfHouse", 4935761, 2961971);
		genStructureInfo(263, LOTRWorldGenDwarvenMineEntranceRuined.class, "DwarvenMineEntranceRuined", 4935761, 2961971);
		genStructureInfo(264, LOTRWorldGenDwarfSmithy.class, "DwarfSmithy", 4935761, 2961971);
		genStructureInfo(265, LOTRWorldGenRuinedDwarvenTower.class, "DwarvenTowerRuined", 4935761, 2961971);
		genStructureInfo(280, LOTRWorldGenElfHouse.class, "ElfHouse", 15325615, 2315809);
		genStructureInfo(281, LOTRWorldGenElfLordHouse.class, "ElfLordHouse", 15325615, 2315809);
		genStructureInfo(282, LOTRWorldGenGaladhrimForge.class, "GaladhrimForge", 14407118, 10854552);
		genStructureInfo(300, LOTRWorldGenMeadHall.class, "RohanMeadHall", 5982252, 13411436);
		genStructureInfo(301, LOTRWorldGenRohanWatchtower.class, "RohanWatchtower", 5982252, 13411436);
		genStructureInfo(302, LOTRWorldGenRohanBarrow.class, "RohanBarrow", 9016133, 16775901);
		genStructureInfo(303, LOTRWorldGenRohanFortress.class, "RohanFortress", 5982252, 13411436);
		genStructureInfo(304, LOTRWorldGenRohanHouse.class, "RohanHouse", 5982252, 13411436);
		genStructureInfo(305, LOTRWorldGenRohanSmithy.class, "RohanSmithy", 5982252, 13411436);
		genStructureInfo(306, LOTRWorldGenRohanVillageFarm.class, "RohanVillageFarm", 7648578, 8546111);
		genStructureInfo(307, LOTRWorldGenRohanStables.class, "RohanStables", 5982252, 13411436);
		genStructureInfo(308, LOTRWorldGenRohanBarn.class, "RohanBarn", 5982252, 13411436);
		genStructureInfo(309, LOTRWorldGenRohanWell.class, "RohanWell", 5982252, 13411436);
		genStructureInfo(310, LOTRWorldGenRohanVillageGarden.class, "RohanVillageGarden", 7648578, 8546111);
		genStructureInfo(311, LOTRWorldGenRohanMarketStall.Blacksmith.class, "RohanMarketBlacksmith", 2960684, 13411436);
		genStructureInfo(312, LOTRWorldGenRohanMarketStall.Farmer.class, "RohanMarketFarmer", 15066597, 13411436);
		genStructureInfo(313, LOTRWorldGenRohanMarketStall.Lumber.class, "RohanMarketLumber", 5981994, 13411436);
		genStructureInfo(314, LOTRWorldGenRohanMarketStall.Builder.class, "RohanMarketBuilder", 7693401, 13411436);
		genStructureInfo(315, LOTRWorldGenRohanMarketStall.Brewer.class, "RohanMarketBrewer", 13874218, 13411436);
		genStructureInfo(316, LOTRWorldGenRohanMarketStall.Butcher.class, "RohanMarketButcher", 16358066, 13411436);
		genStructureInfo(317, LOTRWorldGenRohanMarketStall.Fish.class, "RohanMarketFish", 9882879, 13411436);
		genStructureInfo(318, LOTRWorldGenRohanMarketStall.Baker.class, "RohanMarketBaker", 14725995, 13411436);
		genStructureInfo(319, LOTRWorldGenRohanMarketStall.Orcharder.class, "RohanMarketOrcharder", 9161006, 13411436);
		genStructureInfo(320, LOTRWorldGenRohanVillagePasture.class, "RohanVillagePasture", 7648578, 8546111);
		genStructureInfo(321, LOTRWorldGenRohanVillageSign.class, "RohanVillageSign", 5982252, 13411436);
		genStructureInfo(322, LOTRWorldGenRohanGatehouse.class, "RohanGatehouse", 5982252, 13411436);
		genStructureInfo(323, new LOTRVillageGenRohan(LOTRBiome.rohan, 1.0f), "RohanVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenRohan.Instance>() {

			@Override
			public void apply(LOTRVillageGenRohan.Instance instance) {
				instance.villageType = LOTRVillageGenRohan.VillageType.VILLAGE;
			}
		});
		genStructureInfo(324, new LOTRVillageGenRohan(LOTRBiome.rohan, 1.0f), "RohanFortVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenRohan.Instance>() {

			@Override
			public void apply(LOTRVillageGenRohan.Instance instance) {
				instance.villageType = LOTRVillageGenRohan.VillageType.FORT;
			}
		});
		genStructureInfo(350, LOTRWorldGenUrukTent.class, "UrukTent", 2301210, 131586);
		genStructureInfo(351, LOTRWorldGenRuinedRohanWatchtower.class, "RuinedRohanWatchtower", 1117449, 3288357);
		genStructureInfo(352, LOTRWorldGenUrukForgeTent.class, "UrukForgeTent", 3682596, 2038547);
		genStructureInfo(353, LOTRWorldGenUrukWargPit.class, "UrukWargPit", 3682596, 2038547);
		genStructureInfo(354, LOTRWorldGenUrukCamp.class, "UrukCamp", 2301210, 131586);
		genStructureInfo(380, LOTRWorldGenDunlendingHouse.class, "DunlendingHouse", 6705465, 3813154);
		genStructureInfo(381, LOTRWorldGenDunlendingTavern.class, "DunlendingTavern", 6705465, 3813154);
		genStructureInfo(382, LOTRWorldGenDunlendingCampfire.class, "DunlendingCampfire", 9539472, 6837299);
		genStructureInfo(383, LOTRWorldGenDunlandHillFort.class, "DunlandHillFort", 6705465, 3813154);
		genStructureInfo(400, LOTRWorldGenBeaconTower.class, "BeaconTower", 14869218, 11513775);
		genStructureInfo(401, LOTRWorldGenGondorWatchfort.class, "GondorWatchfort", 14869218, 2367263);
		genStructureInfo(402, LOTRWorldGenGondorSmithy.class, "GondorSmithy", 14869218, 2367263);
		genStructureInfo(403, LOTRWorldGenGondorTurret.class, "GondorTurret", 14869218, 11513775);
		genStructureInfo(404, LOTRWorldGenIthilienHideout.class, "IthilienHideout", 8882055, 7365464);
		genStructureInfo(405, LOTRWorldGenGondorHouse.class, "GondorHouse", 14869218, 9861961);
		genStructureInfo(406, LOTRWorldGenGondorCottage.class, "GondorCottage", 14869218, 9861961);
		genStructureInfo(407, LOTRWorldGenGondorStoneHouse.class, "GondorStoneHouse", 14869218, 2367263);
		genStructureInfo(408, LOTRWorldGenGondorWatchtower.class, "GondorWatchtower", 14869218, 11513775);
		genStructureInfo(409, LOTRWorldGenGondorStables.class, "GondorStables", 14869218, 9861961);
		genStructureInfo(410, LOTRWorldGenGondorBarn.class, "GondorBarn", 14869218, 9861961);
		genStructureInfo(411, LOTRWorldGenGondorFortress.class, "GondorFortress", 14869218, 2367263);
		genStructureInfo(412, LOTRWorldGenGondorTavern.class, "GondorTavern", 14869218, 9861961);
		genStructureInfo(413, LOTRWorldGenGondorWell.class, "GondorWell", 14869218, 11513775);
		genStructureInfo(414, LOTRWorldGenGondorVillageFarm.Crops.class, "GondorFarmCrops", 7047232, 15066597);
		genStructureInfo(415, LOTRWorldGenGondorVillageFarm.Animals.class, "GondorFarmAnimals", 7047232, 15066597);
		genStructureInfo(416, LOTRWorldGenGondorVillageFarm.Tree.class, "GondorFarmTree", 7047232, 15066597);
		genStructureInfo(417, LOTRWorldGenGondorMarketStall.Greengrocer.class, "GondorMarketGreengrocer", 8567851, 9861961);
		genStructureInfo(418, LOTRWorldGenGondorMarketStall.Lumber.class, "GondorMarketLumber", 5981994, 9861961);
		genStructureInfo(419, LOTRWorldGenGondorMarketStall.Mason.class, "GondorMarketMason", 10526621, 9861961);
		genStructureInfo(420, LOTRWorldGenGondorMarketStall.Brewer.class, "GondorMarketBrewer", 13874218, 9861961);
		genStructureInfo(421, LOTRWorldGenGondorMarketStall.Flowers.class, "GondorMarketFlowers", 16243515, 9861961);
		genStructureInfo(422, LOTRWorldGenGondorMarketStall.Butcher.class, "GondorMarketButcher", 14521508, 9861961);
		genStructureInfo(423, LOTRWorldGenGondorMarketStall.Fish.class, "GondorMarketFish", 6862591, 9861961);
		genStructureInfo(424, LOTRWorldGenGondorMarketStall.Farmer.class, "GondorMarketFarmer", 14401433, 9861961);
		genStructureInfo(425, LOTRWorldGenGondorMarketStall.Blacksmith.class, "GondorMarketBlacksmith", 2960684, 9861961);
		genStructureInfo(426, LOTRWorldGenGondorMarketStall.Baker.class, "GondorMarketBaker", 13543009, 9861961);
		genStructureInfo(427, LOTRWorldGenGondorVillageSign.class, "GondorVillageSign", 5982252, 13411436);
		genStructureInfo(428, LOTRWorldGenGondorBath.class, "GondorBath", 14869218, 2367263);
		genStructureInfo(429, LOTRWorldGenGondorGatehouse.class, "GondorGatehouse", 14869218, 2367263);
		genStructureInfo(430, LOTRWorldGenGondorLampPost.class, "GondorLampPost", 14869218, 11513775);
		genStructureInfo(431, LOTRWorldGenGondorTownGarden.class, "GondorTownGarden", 7047232, 15066597);
		genStructureInfo(432, LOTRWorldGenGondorTownTrees.class, "GondorTownTrees", 7047232, 15066597);
		genStructureInfo(433, LOTRWorldGenGondorTownBench.class, "GondorTownBench", 14869218, 11513775);
		genStructureInfo(434, new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(435, new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorTown", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(436, new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0f), "GondorFortVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(450, LOTRWorldGenRuinedBeaconTower.class, "RuinedBeaconTower", 14869218, 11513775);
		genStructureInfo(451, LOTRWorldGenRuinedGondorTower.class, "RuinedGondorTower", 14869218, 11513775);
		genStructureInfo(452, LOTRWorldGenGondorObelisk.class, "GondorObelisk", 14869218, 11513775);
		genStructureInfo(453, LOTRWorldGenGondorRuin.class, "GondorRuin", 14869218, 11513775);
		genStructureInfo(500, LOTRWorldGenDolAmrothStables.class, "DolAmrothStables", 15002613, 2709918);
		genStructureInfo(501, LOTRWorldGenDolAmrothWatchtower.class, "DolAmrothWatchtower", 14869218, 11513775);
		genStructureInfo(502, LOTRWorldGenDolAmrothWatchfort.class, "DolAmrothWatchfort", 15002613, 2709918);
		genStructureInfo(503, new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothVillage", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(504, new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothTown", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(505, new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0f), "DolAmrothFortVillage", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(510, LOTRWorldGenLossarnachFortress.class, "LossarnachFortress", 14869218, 15138816);
		genStructureInfo(511, LOTRWorldGenLossarnachWatchtower.class, "LossarnachWatchtower", 14869218, 11513775);
		genStructureInfo(512, LOTRWorldGenLossarnachWatchfort.class, "LossarnachWatchfort", 14869218, 15138816);
		genStructureInfo(513, new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachVillage", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(514, new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachTown", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(515, new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0f), "LossarnachFortVillage", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(520, LOTRWorldGenLebenninFortress.class, "LebenninFortress", 14869218, 621750);
		genStructureInfo(521, LOTRWorldGenLebenninWatchtower.class, "LebenninWatchtower", 14869218, 11513775);
		genStructureInfo(522, LOTRWorldGenLebenninWatchfort.class, "LebenninWatchfort", 14869218, 621750);
		genStructureInfo(523, new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninVillage", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(524, new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninTown", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(525, new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0f), "LebenninFortVillage", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(530, LOTRWorldGenPelargirFortress.class, "PelargirFortress", 14869218, 2917253);
		genStructureInfo(531, LOTRWorldGenPelargirWatchtower.class, "PelargirWatchtower", 14869218, 11513775);
		genStructureInfo(532, LOTRWorldGenPelargirWatchfort.class, "PelargirWatchfort", 14869218, 2917253);
		genStructureInfo(533, new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirVillage", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(534, new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirTown", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(535, new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0f), "PelargirFortVillage", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(540, LOTRWorldGenPinnathGelinFortress.class, "PinnathGelinFortress", 14869218, 1401651);
		genStructureInfo(541, LOTRWorldGenPinnathGelinWatchtower.class, "PinnathGelinWatchtower", 14869218, 11513775);
		genStructureInfo(542, LOTRWorldGenPinnathGelinWatchfort.class, "PinnathGelinWatchfort", 14869218, 1401651);
		genStructureInfo(543, new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinVillage", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(544, new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinTown", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(545, new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0f), "PinnathGelinFortVillage", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(550, LOTRWorldGenBlackrootFortress.class, "BlackrootFortress", 14869218, 2367263);
		genStructureInfo(551, LOTRWorldGenBlackrootWatchtower.class, "BlackrootWatchtower", 14869218, 11513775);
		genStructureInfo(552, LOTRWorldGenBlackrootWatchfort.class, "BlackrootWatchfort", 14869218, 2367263);
		genStructureInfo(553, new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(554, new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootTown", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(555, new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0f), "BlackrootFortVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(560, LOTRWorldGenLamedonFortress.class, "LamedonFortress", 14869218, 1784649);
		genStructureInfo(561, LOTRWorldGenLamedonWatchtower.class, "LamedonWatchtower", 14869218, 11513775);
		genStructureInfo(562, LOTRWorldGenLamedonWatchfort.class, "LamedonWatchfort", 14869218, 1784649);
		genStructureInfo(563, new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonVillage", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(564, new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonTown", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(565, new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0f), "LamedonFortVillage", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {

			@Override
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(600, LOTRWorldGenMordorTower.class, "MordorTower", 2631720, 328965);
		genStructureInfo(601, LOTRWorldGenMordorTent.class, "MordorTent", 2301210, 131586);
		genStructureInfo(602, LOTRWorldGenMordorForgeTent.class, "MordorForgeTent", 2631720, 328965);
		genStructureInfo(603, LOTRWorldGenMordorWargPit.class, "MordorWargPit", 2631720, 328965);
		genStructureInfo(604, LOTRWorldGenMordorCamp.class, "MordorCamp", 2301210, 131586);
		genStructureInfo(605, LOTRWorldGenBlackUrukFort.class, "BlackUrukFort", 2631720, 328965);
		genStructureInfo(650, LOTRWorldGenNurnWheatFarm.class, "NurnWheatFarm", 4469796, 328965);
		genStructureInfo(651, LOTRWorldGenOrcSlaverTower.class, "OrcSlaverTower", 1117449, 3288357);
		genStructureInfo(670, LOTRWorldGenMordorSpiderPit.class, "MordorSpiderPit", 1511181, 12917534);
		genStructureInfo(700, LOTRWorldGenDorwinionGarden.class, "DorwinionGarden", 16572875, 13418417);
		genStructureInfo(701, LOTRWorldGenDorwinionTent.class, "DorwinionTent", 6706573, 15058766);
		genStructureInfo(702, LOTRWorldGenDorwinionCaptainTent.class, "DorwinionCaptainTent", 6706573, 15058766);
		genStructureInfo(703, LOTRWorldGenDorwinionHouse.class, "DorwinionHouse", 7167128, 15390149);
		genStructureInfo(704, LOTRWorldGenDorwinionBrewery.class, "DorwinionBrewery", 7167128, 15390149);
		genStructureInfo(705, LOTRWorldGenDorwinionElfHouse.class, "DorwinionElfHouse", 7167128, 15390149);
		genStructureInfo(706, LOTRWorldGenDorwinionBath.class, "DorwinionBath", 7167128, 15390149);
		genStructureInfo(750, LOTRWorldGenEasterlingHouse.class, "EasterlingHouse", 12693373, 7689786);
		genStructureInfo(751, LOTRWorldGenEasterlingStables.class, "EasterlingStables", 12693373, 7689786);
		genStructureInfo(752, LOTRWorldGenEasterlingTownHouse.class, "EasterlingTownHouse", 6304287, 12693373);
		genStructureInfo(753, LOTRWorldGenEasterlingLargeTownHouse.class, "EasterlingLargeTownHouse", 6304287, 12693373);
		genStructureInfo(754, LOTRWorldGenEasterlingFortress.class, "EasterlingFortress", 6304287, 12693373);
		genStructureInfo(755, LOTRWorldGenEasterlingTower.class, "EasterlingTower", 6304287, 12693373);
		genStructureInfo(756, LOTRWorldGenEasterlingSmithy.class, "EasterlingSmithy", 6304287, 12693373);
		genStructureInfo(757, LOTRWorldGenEasterlingMarketStall.Blacksmith.class, "EasterlingMarketBlacksmith", 2960684, 12693373);
		genStructureInfo(758, LOTRWorldGenEasterlingMarketStall.Lumber.class, "EasterlingMarketLumber", 5981994, 12693373);
		genStructureInfo(759, LOTRWorldGenEasterlingMarketStall.Mason.class, "EasterlingMarketMason", 7039594, 12693373);
		genStructureInfo(760, LOTRWorldGenEasterlingMarketStall.Butcher.class, "EasterlingMarketButcher", 12544103, 12693373);
		genStructureInfo(761, LOTRWorldGenEasterlingMarketStall.Brewer.class, "EasterlingMarketBrewer", 11891243, 12693373);
		genStructureInfo(762, LOTRWorldGenEasterlingMarketStall.Fish.class, "EasterlingMarketFish", 4882395, 12693373);
		genStructureInfo(763, LOTRWorldGenEasterlingMarketStall.Baker.class, "EasterlingMarketBaker", 14725995, 12693373);
		genStructureInfo(764, LOTRWorldGenEasterlingMarketStall.Hunter.class, "EasterlingMarketHunter", 4471854, 12693373);
		genStructureInfo(765, LOTRWorldGenEasterlingMarketStall.Farmer.class, "EasterlingMarketFarmer", 8893759, 12693373);
		genStructureInfo(766, LOTRWorldGenEasterlingMarketStall.Gold.class, "EasterlingMarketGold", 16237060, 12693373);
		genStructureInfo(767, LOTRWorldGenEasterlingTavern.class, "EasterlingTavern", 12693373, 7689786);
		genStructureInfo(768, LOTRWorldGenEasterlingTavernTown.class, "EasterlingTavernTown", 6304287, 12693373);
		genStructureInfo(769, LOTRWorldGenEasterlingStatue.class, "EasterlingStatue", 12693373, 7689786);
		genStructureInfo(770, LOTRWorldGenEasterlingGarden.class, "EasterlingGarden", 4030994, 12693373);
		genStructureInfo(771, LOTRWorldGenEasterlingVillageSign.class, "EasterlingVillageSign", 12693373, 7689786);
		genStructureInfo(772, LOTRWorldGenEasterlingWell.class, "EasterlingWell", 12693373, 7689786);
		genStructureInfo(773, LOTRWorldGenEasterlingVillageFarm.Crops.class, "EasterlingFarmCrops", 4030994, 12693373);
		genStructureInfo(774, LOTRWorldGenEasterlingVillageFarm.Animals.class, "EasterlingFarmAnimals", 4030994, 12693373);
		genStructureInfo(775, LOTRWorldGenEasterlingVillageFarm.Tree.class, "EasterlingFarmTree", 4030994, 12693373);
		genStructureInfo(776, LOTRWorldGenEasterlingGatehouse.class, "EasterlingGatehouse", 6304287, 12693373);
		genStructureInfo(777, LOTRWorldGenEasterlingLamp.class, "EasterlingLamp", 6304287, 12693373);
		genStructureInfo(778, new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0f, true), "EasterlingVillage", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {

			@Override
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.VILLAGE;
			}
		});
		genStructureInfo(779, new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0f, true), "EasterlingTown", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {

			@Override
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.TOWN;
			}
		});
		genStructureInfo(780, new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0f, true), "EasterlingFortVillage", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {

			@Override
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.FORT;
			}
		});
		genStructureInfo(1000, LOTRWorldGenHaradObelisk.class, "HaradObelisk", 10854007, 15590575);
		genStructureInfo(1001, LOTRWorldGenHaradPyramid.class, "HaradPyramid", 10854007, 15590575);
		genStructureInfo(1002, LOTRWorldGenMumakSkeleton.class, "MumakSkeleton", 14737111, 16250349);
		genStructureInfo(1003, LOTRWorldGenHaradRuinedFort.class, "HaradRuinedFort", 10854007, 15590575);
		genStructureInfo(1050, LOTRWorldGenHarnedorHouse.class, "HarnedorHouse", 4994339, 12814421);
		genStructureInfo(1051, LOTRWorldGenHarnedorSmithy.class, "HarnedorSmithy", 4994339, 12814421);
		genStructureInfo(1052, LOTRWorldGenHarnedorTavern.class, "HarnedorTavern", 4994339, 12814421);
		genStructureInfo(1053, LOTRWorldGenHarnedorMarket.class, "HarnedorMarket", 4994339, 12814421);
		genStructureInfo(1054, LOTRWorldGenHarnedorTower.class, "HarnedorTower", 4994339, 12814421);
		genStructureInfo(1055, LOTRWorldGenHarnedorFort.class, "HarnedorFort", 4994339, 12814421);
		genStructureInfo(1056, LOTRWorldGenNearHaradTent.class, "NearHaradTent", 13519170, 1775897);
		genStructureInfo(1057, LOTRWorldGenHarnedorFarm.class, "HarnedorFarm", 10073953, 12814421);
		genStructureInfo(1058, LOTRWorldGenHarnedorPasture.class, "HarnedorPasture", 10073953, 12814421);
		genStructureInfo(1059, new LOTRVillageGenHarnedor(LOTRBiome.harnedor, 1.0f), "HarnedorVillage", 4994339, 12814421, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {

			@Override
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1060, LOTRWorldGenHarnedorStables.class, "HarnedorStables", 4994339, 12814421);
		genStructureInfo(1061, LOTRWorldGenHarnedorVillageSign.class, "HarnedorVillageSign", 4994339, 12814421);
		genStructureInfo(1062, new LOTRVillageGenHarnedor(LOTRBiome.harnedor, 1.0f), "HarnedorFortVillage", 4994339, 12814421, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {

			@Override
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.FORTRESS;
			}
		});
		genStructureInfo(1080, LOTRWorldGenHarnedorHouseRuined.class, "HarnedorHouseRuined", 5519919, 10059372);
		genStructureInfo(1081, LOTRWorldGenHarnedorTavernRuined.class, "HarnedorTavernRuined", 5519919, 10059372);
		genStructureInfo(1082, new LOTRVillageGenHarnedor(LOTRBiome.harondor, 1.0f).setRuined(), "HarnedorVillageRuined", 5519919, 10059372, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {

			@Override
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1100, LOTRWorldGenSouthronHouse.class, "SouthronHouse", 15063989, 10052655);
		genStructureInfo(1101, LOTRWorldGenSouthronTavern.class, "SouthronTavern", 15063989, 10052655);
		genStructureInfo(1102, LOTRWorldGenSouthronSmithy.class, "SouthronSmithy", 15063989, 10052655);
		genStructureInfo(1103, LOTRWorldGenSouthronTower.class, "SouthronTower", 15063989, 10052655);
		genStructureInfo(1104, LOTRWorldGenSouthronMansion.class, "SouthronMansion", 15063989, 10052655);
		genStructureInfo(1105, LOTRWorldGenSouthronStables.class, "SouthronStables", 15063989, 10052655);
		genStructureInfo(1106, LOTRWorldGenSouthronFarm.class, "SouthronFarm", 9547581, 10052655);
		genStructureInfo(1107, LOTRWorldGenSouthronFortress.class, "SouthronFortress", 15063989, 10052655);
		genStructureInfo(1108, LOTRWorldGenSouthronWell.class, "SouthronWell", 15063989, 10052655);
		genStructureInfo(1109, LOTRWorldGenSouthronBazaar.class, "SouthronBazaar", 15063989, 10052655);
		genStructureInfo(1110, LOTRWorldGenSouthronPasture.class, "SouthronPasture", 9547581, 10052655);
		genStructureInfo(1111, LOTRWorldGenSouthronVillageSign.class, "SouthronVillageSign", 15063989, 10052655);
		genStructureInfo(1112, new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0f), "SouthronVillage", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {

			@Override
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1113, LOTRWorldGenSouthronStatue.class, "SouthronStatue", 15063989, 10052655);
		genStructureInfo(1114, LOTRWorldGenSouthronBarracks.class, "SouthronBarracks", 15063989, 10052655);
		genStructureInfo(1115, LOTRWorldGenSouthronTraining.class, "SouthronTraining", 15063989, 10052655);
		genStructureInfo(1116, LOTRWorldGenSouthronFortGate.class, "SouthronFortGate", 15063989, 10052655);
		genStructureInfo(1117, new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0f), "SouthronFortVillage", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {

			@Override
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.FORT;
			}
		});
		genStructureInfo(1118, LOTRWorldGenSouthronLamp.class, "SouthronLamp", 15063989, 10052655);
		genStructureInfo(1119, LOTRWorldGenSouthronTownTree.class, "SouthronTownTree", 9547581, 10052655);
		genStructureInfo(1120, LOTRWorldGenSouthronTownFlowers.class, "SouthronTownFlowers", 9547581, 10052655);
		genStructureInfo(1121, new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0f), "SouthronTown", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {

			@Override
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN;
			}
		});
		genStructureInfo(1122, LOTRWorldGenSouthronTownGate.class, "SouthronTownGate", 15063989, 10052655);
		genStructureInfo(1123, LOTRWorldGenSouthronTownCorner.class, "SouthronTownCorner", 15063989, 10052655);
		genStructureInfo(1140, LOTRWorldGenMoredainMercTent.class, "MoredainMercTent", 12845056, 2949120);
		genStructureInfo(1141, LOTRWorldGenMoredainMercCamp.class, "MoredainMercCamp", 12845056, 2949120);
		genStructureInfo(1150, LOTRWorldGenUmbarHouse.class, "UmbarHouse", 14407104, 3354926);
		genStructureInfo(1151, LOTRWorldGenUmbarTavern.class, "UmbarTavern", 14407104, 3354926);
		genStructureInfo(1152, LOTRWorldGenUmbarSmithy.class, "UmbarSmithy", 14407104, 3354926);
		genStructureInfo(1153, LOTRWorldGenUmbarTower.class, "UmbarTower", 14407104, 3354926);
		genStructureInfo(1154, LOTRWorldGenUmbarMansion.class, "UmbarMansion", 14407104, 3354926);
		genStructureInfo(1155, LOTRWorldGenUmbarStables.class, "UmbarStables", 14407104, 3354926);
		genStructureInfo(1156, LOTRWorldGenUmbarFarm.class, "UmbarFarm", 9547581, 3354926);
		genStructureInfo(1157, LOTRWorldGenUmbarFortress.class, "UmbarFortress", 14407104, 3354926);
		genStructureInfo(1158, LOTRWorldGenUmbarWell.class, "UmbarWell", 14407104, 3354926);
		genStructureInfo(1159, LOTRWorldGenUmbarBazaar.class, "UmbarBazaar", 14407104, 3354926);
		genStructureInfo(1160, LOTRWorldGenUmbarPasture.class, "UmbarPasture", 9547581, 3354926);
		genStructureInfo(1161, LOTRWorldGenUmbarVillageSign.class, "UmbarVillageSign", 14407104, 3354926);
		genStructureInfo(1162, new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0f), "UmbarVillage", 14407104, 3354926, new IVillageProperties<LOTRVillageGenUmbar.InstanceUmbar>() {

			@Override
			public void apply(LOTRVillageGenUmbar.InstanceUmbar instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1163, LOTRWorldGenUmbarStatue.class, "UmbarStatue", 14407104, 3354926);
		genStructureInfo(1164, LOTRWorldGenUmbarBarracks.class, "UmbarBarracks", 14407104, 3354926);
		genStructureInfo(1165, LOTRWorldGenUmbarTraining.class, "UmbarTraining", 14407104, 3354926);
		genStructureInfo(1166, LOTRWorldGenUmbarFortGate.class, "UmbarFortGate", 14407104, 3354926);
		genStructureInfo(1167, new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0f), "UmbarFortVillage", 14407104, 3354926, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {

			@Override
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.FORT;
			}
		});
		genStructureInfo(1168, LOTRWorldGenUmbarLamp.class, "UmbarLamp", 14407104, 3354926);
		genStructureInfo(1169, LOTRWorldGenUmbarTownTree.class, "UmbarTownTree", 9547581, 3354926);
		genStructureInfo(1170, LOTRWorldGenUmbarTownFlowers.class, "UmbarTownFlowers", 9547581, 3354926);
		genStructureInfo(1171, new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0f), "UmbarTown", 14407104, 3354926, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {

			@Override
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN;
			}
		});
		genStructureInfo(1172, LOTRWorldGenUmbarTownGate.class, "UmbarTownGate", 14407104, 3354926);
		genStructureInfo(1173, LOTRWorldGenUmbarTownCorner.class, "UmbarTownCorner", 14407104, 3354926);
		genStructureInfo(1180, LOTRWorldGenCorsairCove.class, "CorsairCove", 8355711, 1644825);
		genStructureInfo(1181, LOTRWorldGenCorsairTent.class, "CorsairTent", 5658198, 657930);
		genStructureInfo(1182, LOTRWorldGenCorsairCamp.class, "CorsairCamp", 5658198, 657930);
		genStructureInfo(1200, LOTRWorldGenNomadTent.class, "NomadTent", 16775927, 8345150);
		genStructureInfo(1201, LOTRWorldGenNomadTentLarge.class, "NomadTentLarge", 16775927, 8345150);
		genStructureInfo(1202, LOTRWorldGenNomadChieftainTent.class, "NomadChieftainTent", 16775927, 8345150);
		genStructureInfo(1203, LOTRWorldGenNomadWell.class, "NomadWell", 5478114, 15391151);
		genStructureInfo(1204, new LOTRVillageGenHaradNomad(LOTRBiome.nearHaradSemiDesert, 1.0f), "NomadVillageSmall", 16775927, 8345150, new IVillageProperties<LOTRVillageGenHaradNomad.Instance>() {

			@Override
			public void apply(LOTRVillageGenHaradNomad.Instance instance) {
				instance.villageType = LOTRVillageGenHaradNomad.VillageType.SMALL;
			}
		});
		genStructureInfo(1205, new LOTRVillageGenHaradNomad(LOTRBiome.nearHaradSemiDesert, 1.0f), "NomadVillageBig", 16775927, 8345150, new IVillageProperties<LOTRVillageGenHaradNomad.Instance>() {

			@Override
			public void apply(LOTRVillageGenHaradNomad.Instance instance) {
				instance.villageType = LOTRVillageGenHaradNomad.VillageType.BIG;
			}
		});
		genStructureInfo(1206, LOTRWorldGenNomadBazaarTent.class, "NomadBazaarTent", 16775927, 8345150);
		genStructureInfo(1250, LOTRWorldGenGulfWarCamp.class, "GulfWarCamp", 12849937, 4275226);
		genStructureInfo(1251, LOTRWorldGenGulfHouse.class, "GulfHouse", 9335899, 5654831);
		genStructureInfo(1252, LOTRWorldGenGulfAltar.class, "GulfAltar", 12849937, 4275226);
		genStructureInfo(1253, LOTRWorldGenGulfSmithy.class, "GulfSmithy", 9335899, 5654831);
		genStructureInfo(1254, LOTRWorldGenGulfBazaar.class, "GulfBazaar", 9335899, 5654831);
		genStructureInfo(1255, LOTRWorldGenGulfTotem.class, "GulfTotem", 12849937, 4275226);
		genStructureInfo(1256, LOTRWorldGenGulfPyramid.class, "GulfPyramid", 15721151, 12873038);
		genStructureInfo(1257, LOTRWorldGenGulfFarm.class, "GulfFarm", 9547581, 12849937);
		genStructureInfo(1258, LOTRWorldGenGulfTower.class, "GulfTower", 12849937, 4275226);
		genStructureInfo(1259, LOTRWorldGenGulfTavern.class, "GulfTavern", 9335899, 5654831);
		genStructureInfo(1260, LOTRWorldGenGulfVillageSign.class, "GulfVillageSign", 14737111, 16250349);
		genStructureInfo(1261, LOTRWorldGenGulfVillageLight.class, "GulfVillageLight", 14737111, 16250349);
		genStructureInfo(1262, new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0f), "GulfVillage", 9335899, 5654831, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {

			@Override
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1263, LOTRWorldGenGulfPasture.class, "GulfPasture", 9547581, 12849937);
		genStructureInfo(1264, new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0f), "GulfTown", 15721151, 12873038, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {

			@Override
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.TOWN;
			}
		});
		genStructureInfo(1265, new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0f), "GulfFortVillage", 12849937, 4275226, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {

			@Override
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.FORT;
			}
		});
		genStructureInfo(1500, LOTRWorldGenMoredainHutVillage.class, "MoredainHutVillage", 8873812, 12891279);
		genStructureInfo(1501, LOTRWorldGenMoredainHutChieftain.class, "MoredainHutChieftain", 8873812, 12891279);
		genStructureInfo(1502, LOTRWorldGenMoredainHutTrader.class, "MoredainHutTrader", 8873812, 12891279);
		genStructureInfo(1503, LOTRWorldGenMoredainHutHunter.class, "MoredainHutHunter", 8873812, 12891279);
		genStructureInfo(1550, LOTRWorldGenTauredainPyramid.class, "TauredainPyramid", 6513746, 4803646);
		genStructureInfo(1551, LOTRWorldGenTauredainHouseSimple.class, "TauredainHouseSimple", 4796447, 8021303);
		genStructureInfo(1552, LOTRWorldGenTauredainHouseStilts.class, "TauredainHouseStilts", 4796447, 8021303);
		genStructureInfo(1553, LOTRWorldGenTauredainWatchtower.class, "TauredainWatchtower", 4796447, 8021303);
		genStructureInfo(1554, LOTRWorldGenTauredainHouseLarge.class, "TauredainHouseLarge", 4796447, 14593598);
		genStructureInfo(1555, LOTRWorldGenTauredainChieftainPyramid.class, "TauredainChieftainPyramid", 6513746, 4803646);
		genStructureInfo(1556, LOTRWorldGenTauredainVillageTree.class, "TauredainVillageTree", 9285414, 4796447);
		genStructureInfo(1557, LOTRWorldGenTauredainVillageFarm.class, "TauredainVillageFarm", 9285414, 4796447);
		genStructureInfo(1558, new LOTRVillageGenTauredain(LOTRBiome.tauredainClearing, 1.0f), "TauredainVillage", 6840658, 5979708, new IVillageProperties<LOTRVillageGenTauredain.Instance>() {

			@Override
			public void apply(LOTRVillageGenTauredain.Instance instance) {
			}
		});
		genStructureInfo(1559, LOTRWorldGenTauredainSmithy.class, "TauredainSmithy", 4796447, 8021303);
		genStructureInfo(1700, LOTRWorldGenHalfTrollHouse.class, "HalfTrollHouse", 10058344, 5325111);
		genStructureInfo(1701, LOTRWorldGenHalfTrollWarlordHouse.class, "HalfTrollWarlordHouse", 10058344, 5325111);

		try {
			List<Item> itmlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRMod.class, Item.class);
			List<LOTRUnitTradeEntries> trdntrlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRUnitTradeEntries.class, LOTRUnitTradeEntries.class);
			List<LOTRAchievement> achlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRAchievement.class, LOTRAchievement.class);
			List<LOTRBiome> bmlist = LFGReflectionHelper.getObjectFieldsOfType(LOTRBiome.class, LOTRBiome.class);
			List<LOTRFaction> fclist = new ArrayList<>(EnumSet.allOf(LOTRFaction.class));
			/*
			 * bmlist.remove(LOTRBiome.ocean); bmlist.remove(LOTRBiome.beach);
			 * bmlist.remove(LOTRBiome.beachGravel);
			 * bmlist.remove(LOTRBiome.beachWhite);
			 * bmlist.remove(LOTRBiome.island); bmlist.remove(LOTRBiome.lake);
			 * bmlist.remove(LOTRBiome.river);
			 * fclist.remove(LOTRFaction.HOSTILE);
			 * fclist.remove(LOTRFaction.UNALIGNED);
			 * fclist.remove(LOTRFaction.RUFFIAN);
			 */

			if ("tables".equals(display)) {
				PrintWriter achievements = new PrintWriter("achievements.txt", "UTF-8");
				for (LOTRAchievement ach : achlist) {
					achievements.println("| " + StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".title") + " || " + StatCollector.translateToLocal("lotr.achievement." + ach.getCodeName() + ".desc"));
					achievements.println("|-");
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
					for (LOTRUnitTradeEntry entry : entries.tradeEntries) {
						if (entry.mountClass == null) {
							units.println("| [[" + getEntityName(entry.entityClass) + "]] || {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} || +" + entry.alignmentRequired + " || " + (entry.getPledgeType() == PledgeType.NONE));
						} else {
							units.println("| [[" + getEntityName(entry.entityClass) + "]] (" + riderLoc + ") || {{\u0414\u0435\u043D\u044C\u0433\u0438|" + LFGReflectionHelper.getInitialCost(entry) * 2 + "}} || +" + entry.alignmentRequired + " || " + (entry.getPledgeType() == PledgeType.NONE));
						}
						units.println("|-");
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
				for (LOTRFaction fac : fclist) {
					boolean two = false;
					String s1 = "<page><title>";
					String s2 = "</title><revision><text>{{\u0421\u0442\u0430\u0442\u044C\u044F \u0424\u0440\u0430\u043A\u0446\u0438\u044F}}</text></revision></page>";
					for (LOTRBiome biome : bmlist) {
						if (fac.factionName().equals(getBiomeName(biome))) {
							two = true;
							break;
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
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0411\u0430\u043D\u0434\u0438\u0442\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.getBanditChance());
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041A\u043B\u0438\u043C\u0430\u0442-\u041E\u0441\u0430\u0434\u043A\u0438");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.rainfall);
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u041A\u043B\u0438\u043C\u0430\u0442-\u0422\u0435\u043C\u043F\u0435\u0440\u0430\u0442\u0443\u0440\u0430");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.temperature);
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u0430\u0440\u0438\u0430\u043D\u0442\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall()).isEmpty()) {
						xml.println("| " + getBiomeName(biome) + " = " + biomeNoVariants);
					} else {
						xml.println("| " + getBiomeName(biome) + " = ");
						for (Object variantBucket : LFGReflectionHelper.getVariantList(biome.getBiomeVariantsSmall())) {
							xml.println("* " + StatCollector.translateToLocal("lotr.variant." + ((LOTRBiomeVariant) LFGReflectionHelper.getVariant(variantBucket)).variantName + ".name") + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u0442\u043E\u0440\u0436\u0435\u043D\u0438\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns).isEmpty()) {
						xml.println("| " + getBiomeName(biome) + " = " + biomeNoInvasions);
					} else {
						xml.println("| " + getBiomeName(biome) + " = ");
						for (LOTRInvasions inv : LFGReflectionHelper.getRegisteredInvasions(biome.invasionSpawns)) {
							xml.println("* {{\u0411\u0414 \u0424\u0440\u0430\u043A\u0446\u0438\u044F-\u0421\u0441\u044B\u043B\u043A\u0430|" + inv.invasionName() + "}};");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0412\u044B\u0441\u043E\u0442\u0430");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.heightBaseParameter);
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0414\u0435\u0440\u0435\u0432\u044C\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (LFGReflectionHelper.getTreeTypes(biome.decorator).isEmpty()) {
						xml.println("| " + getBiomeName(biome) + " = " + biomeNoTrees);
					} else {
						xml.println("| " + getBiomeName(biome) + " = ");
						for (WeightedTreeType tree : LFGReflectionHelper.getTreeTypes(biome.decorator)) {
							xml.println("* " + StatCollector.translateToLocal("lotr.tree." + tree.treeType.name().toLowerCase() + ".name") + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0416\u0438\u0432\u043E\u0442\u043D\u044B\u0435");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
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
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0418\u0441\u043A\u043E\u043F\u0430\u0435\u043C\u044B\u0435");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
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
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u041A\u043E\u043B\u0435\u0431\u0430\u043D\u0438\u044F");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.heightVariation);
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u041C\u0443\u0437\u043F\u0430\u043A");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.biomeName);
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0421\u0441\u044B\u043B\u043A\u0430");
				xml.println(begin);
				xml.println("| #default = [[{{{1}}}]]");
				for (LOTRBiome biome : bmlist) {
					for (LOTRFaction fac : fclist) {
						if (fac.factionName().equals(getBiomeName(biome))) {
							xml.println("| " + getBiomeName(biome) + " | " + getBiomeName(biome) + " (" + biomeLoc + ") = [[" + getBiomeName(biome) + " (" + biomeLoc + ")|" + getBiomeName(biome) + "]]");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0421\u0442\u0440\u0443\u043A\u0442\u0443\u0440\u044B");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					if (LFGReflectionHelper.getRandomStructures(biome.decorator).isEmpty()) {
						xml.println("| " + getBiomeName(biome) + " = " + biomeNoStructures);
					} else {
						xml.println("| " + getBiomeName(biome) + " = ");
						for (Object structure : LFGReflectionHelper.getRandomStructures(biome.decorator)) {
							xml.println("* " + getStructureName(LFGReflectionHelper.getStructureGen(structure).getClass()) + ";");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0411\u0438\u043E\u043C-\u0424\u043E\u0442\u043E");
				xml.println(begin);
				for (LOTRBiome biome : bmlist) {
					xml.println("| " + getBiomeName(biome) + " = " + biome.biomeName + " (biome).png");
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0421\u0442\u0430\u0442\u044C\u044F-\u0411\u0438\u043E\u043C");
				xml.println(begin);
				xml.println("| #default = {{{1}}}");
				for (LOTRBiome biome : bmlist) {
					for (LOTRFaction fac : fclist) {
						if (fac.factionName().equals(getBiomeName(biome))) {
							xml.println("| " + getBiomeName(biome) + " (" + biomeLoc + ") = " + getBiomeName(biome));
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
						if (fac.factionName().equals(getBiomeName(biome))) {
							xml.println("| " + fac.factionName() + " | " + fac.factionName() + " (" + factionLoc + ") = [[" + fac.factionName() + " (" + factionLoc + ")|" + fac.factionName() + "]]");
						}
					}
				}
				xml.println(end);

				xml.print("<page><title>\u0428\u0430\u0431\u043B\u043E\u043D:\u0411\u0414 \u0421\u0442\u0430\u0442\u044C\u044F-\u0424\u0440\u0430\u043A\u0446\u0438\u044F");
				xml.println(begin);
				xml.println("| #default = {{{1}}}");
				for (LOTRFaction fac : fclist) {
					for (LOTRBiome biome : bmlist) {
						if (fac.factionName().equals(getBiomeName(biome))) {
							xml.println("| " + fac.factionName() + " (" + factionLoc + ") = " + fac.factionName());
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

	private void genStructureInfo(int i, Class clazz, String name, int egg1, int egg2) {
		classToStructureName.put(clazz, name);
	}

	private void genStructureInfo(int i, LOTRVillageGen clazz, String name, int j, int k, IVillageProperties iVillageProperties) {
		classToStructureName.put(clazz.getClass(), name);
	}

	public void genEntityInfo(Class clazz, String name, int id) {
		genEntityInfo(clazz, name, id, 80, 3, true);
	}

	public void genEntityInfo(Class clazz, String name, int id, int egg1, int egg2) {
		genEntityInfo(clazz, name, id, 80, 3, true);
	}

	public void genEntityInfo(Class entityClass, String name, int id, int updateRange, int updateFreq, boolean sendVelocityUpdates) {
		classToEntityName.put(entityClass, name);
		entityClassToObject.put(entityClass, LFGReflectionHelper.newEntity(entityClass, world));
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