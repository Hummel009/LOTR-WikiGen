package com.github.hummel.wikigen.engine;

import com.github.hummel.wikigen.util.ReflectionHelper;
import lotr.common.entity.animal.*;
import lotr.common.entity.npc.*;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.mapgen.bluedwarvenmine.LOTRWorldGenBlueMountainsMineEntrance;
import lotr.common.world.structure.LOTRWorldGenUrukWargPit;
import lotr.common.world.structure.*;
import lotr.common.world.structure2.*;
import lotr.common.world.village.*;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class Config {
	public static final Map<Class<? extends Entity>, String> ENTITY_CLASS_TO_NAME = new HashMap<>();
	public static final Map<Class<?>, String> STRUCTURE_CLASS_TO_NAME = new HashMap<>();
	public static final Map<Class<?>, Set<String>> SETTLEMENT_CLASS_TO_NAMES = new HashMap<>();

	public static World world;

	public static void authorizeEntityInfo() {
		genEntityInfo((Class) LOTREntityHorse.class, "Horse", 1, 8601889, 4136462);
		genEntityInfo((Class) LOTREntityHobbit.class, "Hobbit", 2, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityMordorOrc.class, "MordorOrc", 3, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityShirePony.class, "ShirePony", 4, 6109994, 13017995);
		genEntityInfo((Class) LOTREntityMordorWarg.class, "MordorWarg", 5, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityGondorSoldier.class, "GondorSoldier", 6, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityGondorMan.class, "GondorMan", 7, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGaladhrimElf.class, "GaladhrimElf", 8, 9337185, 15920555);
		genEntityInfo((Class) LOTREntityHobbitBartender.class, "HobbitBartender", 9, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityRohanMan.class, "RohanMan", 10, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityGaladhrimWarrior.class, "GaladhrimWarrior", 11, 12697274, 15382870);
		genEntityInfo((Class) LOTREntityMordorOrcBombardier.class, "MordorOrcBombardier", 12, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityMordorOrcTrader.class, "MordorOrcTrader", 13, 5979436, 13421772);
		genEntityInfo((Class) LOTREntityMordorOrcArcher.class, "MordorOrcArcher", 14, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityGondorRuinsWraith.class, "GondorRuinsWraith", 15, 12698049, 4802889);
		genEntityInfo((Class) LOTREntityGondorBlacksmith.class, "GondorBlacksmith", 16, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGaladhrimTrader.class, "GaladhrimTrader", 17, 2047778, 16762150);
		genEntityInfo((Class) LOTREntityDwarf.class, "Dwarf", 18, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityDwarfWarrior.class, "DwarfWarrior", 19, 2238506, 7108730);
		genEntityInfo((Class) LOTREntityDwarfMiner.class, "DwarfMiner", 20, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityMarshWraith.class, "MarshWraith", 21, 10524036, 6179636);
		genEntityInfo((Class) LOTREntityMordorWargBombardier.class, "MordorWargBombardier", 22, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityMordorOrcMercenaryCaptain.class, "MordorOrcMercenaryCaptain", 23, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityGondorianCaptain.class, "GondorianCaptain", 24, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityDwarfCommander.class, "DwarfCommander", 25, 2238506, 7108730);
		genEntityInfo((Class) LOTREntityDwarfAxeThrower.class, "DwarfAxeThrower", 26, 2238506, 7108730);
		genEntityInfo((Class) LOTREntityGondorArcher.class, "GondorArcher", 27, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityUrukHai.class, "UrukHai", 28, 2369050, 5790015);
		genEntityInfo((Class) LOTREntityUrukHaiCrossbower.class, "UrukHaiCrossbower", 29, 2369050, 5790015);
		genEntityInfo((Class) LOTREntityUrukHaiBerserker.class, "UrukHaiBerserker", 30, 2369050, 14408662);
		genEntityInfo((Class) LOTREntityUrukHaiTrader.class, "UrukHaiTrader", 31, 5979436, 13421772);
		genEntityInfo((Class) LOTREntityUrukHaiMercenaryCaptain.class, "UrukHaiMercenaryCaptain", 32, 2369050, 5790015);
		genEntityInfo((Class) LOTREntityTroll.class, "Troll", 33, 10848082, 4796702);
		genEntityInfo((Class) LOTREntityOlogHai.class, "OlogHai", 34, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityGaladhrimLord.class, "GaladhrimLord", 35, 12697274, 15382870);
		genEntityInfo((Class) LOTREntityUrukHaiSapper.class, "UrukHaiSapper", 36, 2369050, 5790015);
		genEntityInfo((Class) LOTREntityMirkwoodSpider.class, "MirkwoodSpider", 37, 2630945, 1315088);
		genEntityInfo((Class) LOTREntityWoodElf.class, "WoodElf", 38, 2314529, 16764574);
		genEntityInfo((Class) LOTREntityWoodElfScout.class, "WoodElfScout", 39, 336140, 3891251);
		genEntityInfo((Class) LOTREntityRohanBarrowWraith.class, "RohanBarrowWraith", 40, 12698049, 4802889);
		genEntityInfo((Class) LOTREntityRohirrimWarrior.class, "Rohirrim", 41, 5524296, 13546384);
		genEntityInfo((Class) LOTREntityRohirrimArcher.class, "RohirrimArcher", 42, 5524296, 13546384);
		genEntityInfo((Class) LOTREntityRohirrimMarshal.class, "RohirrimMarshal", 43, 6180940, 14857848);
		genEntityInfo((Class) LOTREntityHobbitBounder.class, "HobbitShirriff", 44, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityHobbitShirriff.class, "HobbitShirriffChief", 45, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityRohanBlacksmith.class, "RohanBlacksmith", 46, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRangerNorth.class, "RangerNorth", 47, 4473912, 2302748);
		genEntityInfo((Class) LOTREntityRangerIthilien.class, "RangerIthilien", 48, 4015141, 1382669);
		genEntityInfo((Class) LOTREntityDunlendingWarrior.class, "DunlendingWarrior", 49, 5192753, 9337975);
		genEntityInfo((Class) LOTREntityDunlendingArcher.class, "DunlendingArcher", 50, 5192753, 9337975);
		genEntityInfo((Class) LOTREntityDunlendingWarlord.class, "DunlendingWarlord", 51, 5192753, 9337975);
		genEntityInfo((Class) LOTREntityDunlending.class, "Dunlending", 52, 15897714, 3679258);
		genEntityInfo((Class) LOTREntityDunlendingBartender.class, "DunlendingBartender", 53, 15897714, 3679258);
		genEntityInfo((Class) LOTREntityRohanShieldmaiden.class, "RohanShieldmaiden", 54, 5524296, 13546384);
		genEntityInfo((Class) LOTREntityEnt.class, "Ent", 55, 3681048, 6788650);
		genEntityInfo((Class) LOTREntityMountainTroll.class, "MountainTroll", 56, 9991001, 5651753);
		genEntityInfo((Class) LOTREntityMountainTrollChieftain.class, "MountainTrollChieftain", 57);
		genEntityInfo((Class) LOTREntityHuorn.class, "Huorn", 58, 3681048, 6788650);
		genEntityInfo((Class) LOTREntityDarkHuorn.class, "DarkHuorn", 59, 2498067, 2643485);
		genEntityInfo((Class) LOTREntityWoodElfWarrior.class, "WoodElfWarrior", 60, 12231576, 5856300);
		genEntityInfo((Class) LOTREntityWoodElfCaptain.class, "WoodElfCaptain", 61, 12231576, 5856300);
		genEntityInfo((Class) LOTREntityRohanMeadhost.class, "RohanMeadhost", 62, 6567206, 14392402);
		genEntityInfo((Class) LOTREntityButterfly.class, "Butterfly", 63, 2498589, 16747542);
		genEntityInfo((Class) LOTREntityMidges.class, "Midges", 64, 5653040, 1972242);
		genEntityInfo((Class) LOTREntityAngmarOrcMercenaryCaptain.class, "AngmarOrcMercenaryCaptain", 65, 3224873, 5601097);
		genEntityInfo((Class) LOTREntityDunedain.class, "Dunedain", 66, 15638664, 6832694);
		genEntityInfo((Class) LOTREntityNurnSlave.class, "NurnSlave", 67, 8613981, 4864555);
		genEntityInfo((Class) LOTREntityRabbit.class, "Rabbit", 68, 9860178, 5520938);
		genEntityInfo((Class) LOTREntityWildBoar.class, "Boar", 69, 6635562, 4069378);
		genEntityInfo((Class) LOTREntityHobbitOrcharder.class, "HobbitOrcharder", 70, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityMordorOrcSlaver.class, "MordorOrcSlaver", 71, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityMordorSpider.class, "MordorSpider", 72, 1511181, 12917534);
		genEntityInfo((Class) LOTREntityMordorOrcSpiderKeeper.class, "MordorOrcSpiderKeeper", 73, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityAngmarOrc.class, "AngmarOrc", 74, 3224873, 5601097);
		genEntityInfo((Class) LOTREntityAngmarOrcArcher.class, "AngmarOrcArcher", 75, 3224873, 5601097);
		genEntityInfo((Class) LOTREntityAngmarOrcBombardier.class, "AngmarOrcBombardier", 76, 3224873, 5601097);
		genEntityInfo((Class) LOTREntityGundabadOrc.class, "GundabadOrc", 77, 3352346, 8548435);
		genEntityInfo((Class) LOTREntityGundabadOrcArcher.class, "GundabadOrcArcher", 78, 3352346, 8548435);
		genEntityInfo((Class) LOTREntityGundabadOrcMercenaryCaptain.class, "GundabadOrcMercenaryCaptain", 79, 2563350, 6382678);
		genEntityInfo((Class) LOTREntityRangerNorthCaptain.class, "RangerNorthCaptain", 80, 4473912, 2302748);
		genEntityInfo((Class) LOTREntityGundabadWarg.class, "GundabadWarg", 81, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityAngmarWarg.class, "AngmarWarg", 82, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityAngmarWargBombardier.class, "AngmarWargBombardier", 83, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityUrukWarg.class, "UrukWarg", 84, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityUrukWargBombardier.class, "UrukWargBombardier", 85, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityLion.class, "Lion", 86, 13345354, 10838576);
		genEntityInfo((Class) LOTREntityLioness.class, "Lioness", 87, 13346908, 11238466);
		genEntityInfo((Class) LOTREntityGiraffe.class, "Giraffe", 88, 13608551, 6966048);
		genEntityInfo((Class) LOTREntityZebra.class, "Zebra", 89, 15000804, 4340020);
		genEntityInfo((Class) LOTREntityRhino.class, "Rhino", 90, 6118481, 12171165);
		genEntityInfo((Class) LOTREntityCrocodile.class, "Crocodile", 91, 2896659, 986886);
		genEntityInfo((Class) LOTREntityNearHaradrim.class, "NearHaradrim", 92, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityNearHaradrimWarrior.class, "NearHaradrimWarrior", 93, 2171169, 11868955);
		genEntityInfo((Class) LOTREntityHighElf.class, "HighElf", 94, 16761223, 15721387);
		genEntityInfo((Class) LOTREntityGemsbok.class, "Gemsbok", 95, 11759423, 15920343);
		genEntityInfo((Class) LOTREntityFlamingo.class, "Flamingo", 96, 16087966, 16374243);
		genEntityInfo((Class) LOTREntityHobbitFarmer.class, "HobbitFarmer", 97, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityHobbitFarmhand.class, "HobbitFarmhand", 98, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityHighElfWarrior.class, "HighElfWarrior", 99, 14935016, 7040410);
		genEntityInfo((Class) LOTREntityHighElfLord.class, "HighElfLord", 100, 14935016, 7040410);
		genEntityInfo((Class) LOTREntityGondorBannerBearer.class, "GondorBannerBearer", 101, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityRohanBannerBearer.class, "RohanBannerBearer", 102, 5524296, 13546384);
		genEntityInfo((Class) LOTREntityMordorBannerBearer.class, "MordorBannerBearer", 103, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityGaladhrimBannerBearer.class, "GaladhrimBannerBearer", 104, 12697274, 15382870);
		genEntityInfo((Class) LOTREntityWoodElfBannerBearer.class, "WoodElfBannerBearer", 105, 12231576, 5856300);
		genEntityInfo((Class) LOTREntityDunlendingBannerBearer.class, "DunlendingBannerBearer", 106, 5192753, 9337975);
		genEntityInfo((Class) LOTREntityUrukHaiBannerBearer.class, "UrukHaiBannerBearer", 107, 2369050, 5790015);
		genEntityInfo((Class) LOTREntityDwarfBannerBearer.class, "DwarfBannerBearer", 108, 2238506, 7108730);
		genEntityInfo((Class) LOTREntityAngmarBannerBearer.class, "AngmarBannerBearer", 109, 3224873, 5601097);
		genEntityInfo((Class) LOTREntityNearHaradBannerBearer.class, "NearHaradBannerBearer", 110, 2171169, 11868955);
		genEntityInfo((Class) LOTREntityHighElfBannerBearer.class, "HighElfBannerBearer", 111, 14935016, 7040410);
		genEntityInfo((Class) LOTREntityJungleScorpion.class, "JungleScorpion", 112, 2630945, 1315088);
		genEntityInfo((Class) LOTREntityDesertScorpion.class, "DesertScorpion", 113, 16376764, 11772801);
		genEntityInfo((Class) LOTREntityBird.class, "Bird", 114, 7451872, 7451872);
		genEntityInfo((Class) LOTREntityCrebain.class, "Crebain", 115, 2434341, 10490368);
		genEntityInfo((Class) LOTREntityCamel.class, "Camel", 116, 13150061, 9203768);
		genEntityInfo((Class) LOTREntityNearHaradrimArcher.class, "NearHaradrimArcher", 117, 2171169, 11868955);
		genEntityInfo((Class) LOTREntityNearHaradrimWarlord.class, "NearHaradrimWarlord", 118, 2171169, 11868955);
		genEntityInfo((Class) LOTREntityBlueDwarf.class, "BlueDwarf", 119, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityBlueDwarfWarrior.class, "BlueDwarfWarrior", 120, 3161673, 6257551);
		genEntityInfo((Class) LOTREntityBlueDwarfAxeThrower.class, "BlueDwarfAxeThrower", 121, 3161673, 6257551);
		genEntityInfo((Class) LOTREntityBlueDwarfBannerBearer.class, "BlueDwarfBannerBearer", 122, 3161673, 6257551);
		genEntityInfo((Class) LOTREntityBlueDwarfCommander.class, "BlueDwarfCommander", 123, 3161673, 6257551);
		genEntityInfo((Class) LOTREntityBlueDwarfMiner.class, "BlueDwarfMiner", 124, 16353133, 15357472);
		genEntityInfo((Class) LOTREntitySouthronBrewer.class, "NearHaradDrinksTrader", 125, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronMiner.class, "NearHaradMineralsTrader", 126, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronFlorist.class, "NearHaradPlantsTrader", 127, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronButcher.class, "NearHaradFoodTrader", 128, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityBlueDwarfMerchant.class, "BlueDwarfMerchant", 129, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityBandit.class, "Bandit", 130, 16225652, 5323553);
		genEntityInfo((Class) LOTREntityRangerNorthBannerBearer.class, "RangerNorthBannerBearer", 131, 4473912, 2302748);
		genEntityInfo((Class) LOTREntityElk.class, "Elk", 132, 15459801, 11905424);
		genEntityInfo((Class) LOTREntityGondorTowerGuard.class, "GondorTowerGuard", 133, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityNearHaradMerchant.class, "NearHaradMerchant", 134, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityHaradPyramidWraith.class, "HaradPyramidWraith", 135, 10854007, 15590575);
		genEntityInfo((Class) LOTREntityDolGuldurOrc.class, "DolGuldurOrc", 136, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityDolGuldurOrcArcher.class, "DolGuldurOrcArcher", 137, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityDolGuldurBannerBearer.class, "DolGuldurBannerBearer", 138, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityDolGuldurOrcChieftain.class, "DolGuldurChieftain", 139, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityMirkTroll.class, "MirkTroll", 140, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityGundabadBannerBearer.class, "GundabadBannerBearer", 141, 3352346, 8548435);
		genEntityInfo((Class) LOTREntityTermite.class, "Termite", 142, 12748857, 7948066);
		genEntityInfo((Class) LOTREntityDikDik.class, "DikDik", 143, 12023867, 6833961);
		genEntityInfo((Class) LOTREntityBlackUruk.class, "BlackUruk", 144, 988430, 2830632);
		genEntityInfo((Class) LOTREntityBlackUrukArcher.class, "BlackUrukArcher", 145, 988430, 2830632);
		genEntityInfo((Class) LOTREntityHalfTroll.class, "HalfTroll", 146, 6903359, 3614236);
		genEntityInfo((Class) LOTREntityRangerIthilienCaptain.class, "RangerIthilienCaptain", 147, 4015141, 1382669);
		genEntityInfo((Class) LOTREntityRangerIthilienBannerBearer.class, "RangerIthilienBannerBearer", 148, 4015141, 1382669);
		genEntityInfo((Class) LOTREntityHalfTrollWarrior.class, "HalfTrollWarrior", 149, 9403002, 6244662);
		genEntityInfo((Class) LOTREntityHalfTrollBannerBearer.class, "HalfTrollBannerBearer", 150, 9403002, 6244662);
		genEntityInfo((Class) LOTREntityHalfTrollWarlord.class, "HalfTrollWarlord", 151, 9403002, 6244662);
		genEntityInfo((Class) LOTREntityAngmarOrcTrader.class, "AngmarOrcTrader", 152, 5979436, 13421772);
		genEntityInfo((Class) LOTREntityDolGuldurOrcTrader.class, "DolGuldurOrcTrader", 153, 4408654, 2040101);
		genEntityInfo((Class) LOTREntityHalfTrollScavenger.class, "HalfTrollScavenger", 154, 6903359, 3614236);
		genEntityInfo((Class) LOTREntityGaladhrimSmith.class, "GaladhrimSmith", 155, 9337185, 15920555);
		genEntityInfo((Class) LOTREntityHighElfSmith.class, "HighElfSmith", 156, 16761223, 15721387);
		genEntityInfo((Class) LOTREntityWoodElfSmith.class, "WoodElfSmith", 157, 2314529, 16764574);
		genEntityInfo((Class) LOTREntitySwanKnight.class, "SwanKnight", 158, 2302535, 15918822);
		genEntityInfo((Class) LOTREntityDolAmrothCaptain.class, "DolAmrothCaptain", 159, 2302535, 15918822);
		genEntityInfo((Class) LOTREntityDolAmrothBannerBearer.class, "DolAmrothBannerBearer", 160, 3227005, 14278898);
		genEntityInfo((Class) LOTREntitySwan.class, "Swan", 161, 16119285, 15571785);
		genEntityInfo((Class) LOTREntityMoredain.class, "Moredain", 162, 5323303, 2168848);
		genEntityInfo((Class) LOTREntityMoredainWarrior.class, "MoredainWarrior", 163, 8998697, 5057302);
		genEntityInfo((Class) LOTREntityMoredainBannerBearer.class, "MoredainBannerBearer", 164, 8998697, 5057302);
		genEntityInfo((Class) LOTREntityMoredainChieftain.class, "MoredainChieftain", 165, 13807978, 11166513);
		genEntityInfo((Class) LOTREntityMoredainHuntsman.class, "MoredainHuntsman", 166, 5323303, 2168848);
		genEntityInfo((Class) LOTREntityMoredainHutmaker.class, "MoredainHutmaker", 167, 5323303, 2168848);
		genEntityInfo((Class) LOTREntityDunlendingBerserker.class, "DunlendingBerserker", 168, 5192753, 16050121);
		genEntityInfo((Class) LOTREntityAngmarHillman.class, "AngmarHillman", 169, 11828586, 2891544);
		genEntityInfo((Class) LOTREntityAngmarHillmanWarrior.class, "AngmarHillmanWarrior", 170, 11828586, 2891544);
		genEntityInfo((Class) LOTREntityAngmarHillmanChieftain.class, "AngmarHillmanChieftain", 171, 11828586, 2891544);
		genEntityInfo((Class) LOTREntityAngmarHillmanBannerBearer.class, "AngmarHillmanBannerBearer", 172, 11828586, 2891544);
		genEntityInfo((Class) LOTREntityAngmarHillmanAxeThrower.class, "AngmarHillmanAxeThrower", 173, 11828586, 2891544);
		genEntityInfo((Class) LOTREntityDunlendingAxeThrower.class, "DunlendingAxeThrower", 174, 5192753, 9337975);
		genEntityInfo((Class) LOTREntityIronHillsMerchant.class, "IronHillsMerchant", 175, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityMallornEnt.class, "MallornEnt", 176);
		genEntityInfo((Class) LOTREntityScrapTrader.class, "ScrapTrader", 177, 16225652, 5323553);
		genEntityInfo((Class) LOTREntityTauredain.class, "Tauredain", 178, 4468770, 12948008);
		genEntityInfo((Class) LOTREntityTauredainWarrior.class, "TauredainWarrior", 179, 5652267, 9165389);
		genEntityInfo((Class) LOTREntityTauredainBannerBearer.class, "TauredainBannerBearer", 180, 5652267, 9165389);
		genEntityInfo((Class) LOTREntityTauredainChieftain.class, "TauredainChieftain", 181, 5652267, 9165389);
		genEntityInfo((Class) LOTREntityTauredainBlowgunner.class, "TauredainBlowgunner", 182, 5652267, 9165389);
		genEntityInfo((Class) LOTREntityBarrowWight.class, "BarrowWight", 183, 529926, 3111505);
		genEntityInfo((Class) LOTREntityTauredainShaman.class, "TauredainShaman", 184, 4468770, 12948008);
		genEntityInfo((Class) LOTREntityGaladhrimWarden.class, "GaladhrimWarden", 185, 10527645, 8027255);
		genEntityInfo((Class) LOTREntityTauredainFarmer.class, "TauredainFarmer", 186, 4468770, 12948008);
		genEntityInfo((Class) LOTREntityTauredainFarmhand.class, "TauredainFarmhand", 187, 4468770, 12948008);
		genEntityInfo((Class) LOTREntityDwarfSmith.class, "DwarfSmith", 188, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityBlueMountainsSmith.class, "BlueDwarfSmith", 189, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityTauredainPyramidWraith.class, "TauredainPyramidWraith", 190, 12698049, 4802889);
		genEntityInfo((Class) LOTREntityGundabadUruk.class, "GundabadUruk", 191, 2563350, 6382678);
		genEntityInfo((Class) LOTREntityGundabadUrukArcher.class, "GundabadUrukArcher", 192, 2563350, 6382678);
		genEntityInfo((Class) LOTREntityIsengardSnaga.class, "IsengardSnaga", 193, 4339500, 8352349);
		genEntityInfo((Class) LOTREntityIsengardSnagaArcher.class, "IsengardSnagaArcher", 194, 4339500, 8352349);
		genEntityInfo((Class) LOTREntityBanditHarad.class, "BanditHarad", 195, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityDeer.class, "Deer", 196, 5978669, 11968394);
		genEntityInfo((Class) LOTREntityDaleMan.class, "DaleMan", 197, 16755851, 5252113);
		genEntityInfo((Class) LOTREntityDaleLevyman.class, "DaleLevyman", 198, 7034184, 5252113);
		genEntityInfo((Class) LOTREntityDaleSoldier.class, "DaleSoldier", 199, 11776947, 481419);
		genEntityInfo((Class) LOTREntityDaleArcher.class, "DaleArcher", 200, 11776947, 481419);
		genEntityInfo((Class) LOTREntityDaleBannerBearer.class, "DaleBannerBearer", 201, 11776947, 481419);
		genEntityInfo((Class) LOTREntityDaleCaptain.class, "DaleCaptain", 202, 11776947, 481419);
		genEntityInfo((Class) LOTREntityDaleBlacksmith.class, "DaleBlacksmith", 203, 16755851, 5252113);
		genEntityInfo((Class) LOTREntityDorwinionMan.class, "DorwinionMan", 204, 16755851, 12213157);
		genEntityInfo((Class) LOTREntityDorwinionGuard.class, "DorwinionGuard", 205, 9005901, 6178167);
		genEntityInfo((Class) LOTREntityDorwinionCaptain.class, "DorwinionCaptain", 206, 9005901, 6178167);
		genEntityInfo((Class) LOTREntityDorwinionBannerBearer.class, "DorwinionBannerBearer", 207, 9005901, 6178167);
		genEntityInfo((Class) LOTREntityDorwinionElf.class, "DorwinionElf", 208, 16761223, 8538746);
		genEntityInfo((Class) LOTREntityDorwinionElfWarrior.class, "DorwinionElfWarrior", 209, 13420999, 8407696);
		genEntityInfo((Class) LOTREntityDorwinionElfBannerBearer.class, "DorwinionElfBannerBearer", 210, 13420999, 8407696);
		genEntityInfo((Class) LOTREntityDorwinionElfCaptain.class, "DorwinionElfCaptain", 211, 13420999, 8407696);
		genEntityInfo((Class) LOTREntityDaleBaker.class, "DaleBaker", 212, 16755851, 5252113);
		genEntityInfo((Class) LOTREntityDorwinionElfVintner.class, "DorwinionElfVintner", 213, 9721246, 5648736);
		genEntityInfo((Class) LOTREntityDorwinionVinehand.class, "DorwinionVinehand", 214, 16755851, 12213157);
		genEntityInfo((Class) LOTREntityDorwinionVinekeeper.class, "DorwinionVinekeeper", 215, 16755851, 12213157);
		genEntityInfo((Class) LOTREntityDorwinionMerchantElf.class, "DorwinionMerchant", 216, 16761223, 8538746);
		genEntityInfo((Class) LOTREntityDaleMerchant.class, "DaleMerchant", 217, 16755851, 5252113);
		genEntityInfo((Class) LOTREntityAurochs.class, "Aurochs", 218, 7488812, 3217935);
		genEntityInfo((Class) LOTREntityKineAraw.class, "KineAraw", 219, 16702665, 12890019);
		genEntityInfo((Class) LOTREntityDorwinionCrossbower.class, "DorwinionCrossbower", 220, 9005901, 6178167);
		genEntityInfo((Class) LOTREntityLossarnachAxeman.class, "LossarnachAxeman", 221, 11578026, 3812901);
		genEntityInfo((Class) LOTREntityLossarnachBannerBearer.class, "LossarnachBannerBearer", 222, 11578026, 3812901);
		genEntityInfo((Class) LOTREntityBlackUrukBannerBearer.class, "BlackUrukBannerBearer", 223, 988430, 2830632);
		genEntityInfo((Class) LOTREntityPelargirMarine.class, "PelargirMarine", 224, 13090494, 1475447);
		genEntityInfo((Class) LOTREntityPelargirBannerBearer.class, "PelargirBannerBearer", 225, 13090494, 1475447);
		genEntityInfo((Class) LOTREntityPinnathGelinSoldier.class, "PinnathGelinSoldier", 226, 11183011, 29235);
		genEntityInfo((Class) LOTREntityPinnathGelinBannerBearer.class, "PinnathGelinBannerBearer", 227, 11183011, 29235);
		genEntityInfo((Class) LOTREntityBlackrootSoldier.class, "BlackrootSoldier", 228, 11183011, 3881016);
		genEntityInfo((Class) LOTREntityBlackrootBannerBearer.class, "BlackrootBannerBearer", 229, 11183011, 3881016);
		genEntityInfo((Class) LOTREntityBlackrootArcher.class, "BlackrootArcher", 230, 11183011, 3881016);
		genEntityInfo((Class) LOTREntityGondorLevyman.class, "GondorLevyman", 231, 10789794, 6833716);
		genEntityInfo((Class) LOTREntityNanUngolBannerBearer.class, "NanUngolBannerBearer", 232, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityMinasMorgulBannerBearer.class, "MinasMorgulBannerBearer", 233, 3353378, 7042407);
		genEntityInfo((Class) LOTREntityDolAmrothSoldier.class, "DolAmrothSoldier", 234, 3227005, 14278898);
		genEntityInfo((Class) LOTREntityDolAmrothArcher.class, "DolAmrothArcher", 235, 3227005, 14278898);
		genEntityInfo((Class) LOTREntityGondorFarmer.class, "GondorFarmer", 236, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorFarmhand.class, "GondorFarmhand", 237, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityLebenninLevyman.class, "LebenninLevyman", 238, 14866637, 3573666);
		genEntityInfo((Class) LOTREntityLebenninBannerBearer.class, "LebenninBannerBearer", 239, 14866637, 3573666);
		genEntityInfo((Class) LOTREntityGondorBartender.class, "GondorBartender", 240, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorGreengrocer.class, "GondorGreengrocer", 241, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorLumberman.class, "GondorLumberman", 242, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorMason.class, "GondorMason", 243, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorBrewer.class, "GondorBrewer", 244, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorFlorist.class, "GondorFlorist", 245, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorButcher.class, "GondorButcher", 246, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorFishmonger.class, "GondorFishmonger", 247, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityGondorBaker.class, "GondorBaker", 248, 13547685, 5652538);
		genEntityInfo((Class) LOTREntityLossarnachCaptain.class, "LossarnachCaptain", 249, 11578026, 3812901);
		genEntityInfo((Class) LOTREntityPelargirCaptain.class, "PelargirCaptain", 250, 13090494, 1475447);
		genEntityInfo((Class) LOTREntityPinnathGelinCaptain.class, "PinnathGelinCaptain", 251, 11183011, 29235);
		genEntityInfo((Class) LOTREntityBlackrootCaptain.class, "BlackrootCaptain", 252, 11183011, 3881016);
		genEntityInfo((Class) LOTREntityLebenninCaptain.class, "LebenninCaptain", 253, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityLamedonSoldier.class, "LamedonSoldier", 254, 12103600, 3624035);
		genEntityInfo((Class) LOTREntityLamedonArcher.class, "LamedonArcher", 255, 12103600, 3624035);
		genEntityInfo((Class) LOTREntityLamedonBannerBearer.class, "LamedonBannerBearer", 256, 12103600, 3624035);
		genEntityInfo((Class) LOTREntityLamedonCaptain.class, "LamedonCaptain", 257, 12103600, 3624035);
		genEntityInfo((Class) LOTREntityLamedonHillman.class, "LamedonHillman", 258, 13547685, 2108991);
		genEntityInfo((Class) LOTREntityRohanFarmhand.class, "RohanFarmhand", 259, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityGorcrow.class, "Gorcrow", 260, 928034, 5451403);
		genEntityInfo((Class) LOTREntitySeagull.class, "Seagull", 261, 15920107, 13997863);
		genEntityInfo((Class) LOTREntityRohanFarmer.class, "RohanFarmer", 262, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanLumberman.class, "RohanLumberman", 263, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanBuilder.class, "RohanBuilder", 264, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanBrewer.class, "RohanBrewer", 265, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanButcher.class, "RohanButcher", 266, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanFishmonger.class, "RohanFishmonger", 267, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanBaker.class, "RohanBaker", 268, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityRohanOrcharder.class, "RohanOrcharder", 269, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityDunedainBlacksmith.class, "DunedainBlacksmith", 270, 15638664, 6832694);
		genEntityInfo((Class) LOTREntityRohanStablemaster.class, "RohanStablemaster", 271, 16424833, 13406801);
		genEntityInfo((Class) LOTREntityBear.class, "Bear", 272, 7492416, 4008994);
		genEntityInfo((Class) LOTREntityEasterling.class, "Easterling", 273, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingWarrior.class, "EasterlingWarrior", 274, 7486267, 15251832);
		genEntityInfo((Class) LOTREntityEasterlingBannerBearer.class, "EasterlingBannerBearer", 275, 7486267, 15251832);
		genEntityInfo((Class) LOTREntityEasterlingArcher.class, "EasterlingArcher", 276, 7486267, 15251832);
		genEntityInfo((Class) LOTREntityEasterlingBlacksmith.class, "EasterlingBlacksmith", 277, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingWarlord.class, "EasterlingWarlord", 278, 14265689, 12004653);
		genEntityInfo((Class) LOTREntityEasterlingFireThrower.class, "EasterlingFireThrower", 279, 7486267, 15251832);
		genEntityInfo((Class) LOTREntityEasterlingLevyman.class, "EasterlingLevyman", 280, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityDorwinionMerchantMan.class, "DorwinionMerchantMan", 281, 16755851, 12213157);
		genEntityInfo((Class) LOTREntityEasterlingGoldWarrior.class, "EasterlingGoldWarrior", 282, 14265689, 12004653);
		genEntityInfo((Class) LOTREntityEasterlingLumberman.class, "EasterlingLumberman", 283, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingMason.class, "EasterlingMason", 284, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingButcher.class, "EasterlingButcher", 285, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingBrewer.class, "EasterlingBrewer", 286, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingFishmonger.class, "EasterlingFishmonger", 287, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingBaker.class, "EasterlingBaker", 288, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingHunter.class, "EasterlingHunter", 289, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingFarmer.class, "EasterlingFarmer", 290, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingGoldsmith.class, "EasterlingGoldsmith", 291, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityEasterlingBartender.class, "EasterlingBartender", 292, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityDorwinionElfArcher.class, "DorwinionElfArcher", 293, 13420999, 8407696);
		genEntityInfo((Class) LOTREntityEasterlingFarmhand.class, "EasterlingFarmhand", 294, 16093531, 6176050);
		genEntityInfo((Class) LOTREntityRivendellElf.class, "RivendellElf", 295, 16761223, 15721387);
		genEntityInfo((Class) LOTREntityRivendellWarrior.class, "RivendellWarrior", 296, 14738662, 10723248);
		genEntityInfo((Class) LOTREntityRivendellLord.class, "RivendellLord", 297, 14738662, 10723248);
		genEntityInfo((Class) LOTREntityRivendellBannerBearer.class, "RivendellBannerBearer", 298, 14738662, 10723248);
		genEntityInfo((Class) LOTREntityRivendellSmith.class, "RivendellSmith", 299, 16761223, 15721387);
		genEntityInfo((Class) LOTREntityEsgarothBannerBearer.class, "EsgarothBannerBearer", 300, 11776947, 481419);
		genEntityInfo((Class) LOTREntityRivendellTrader.class, "RivendellTrader", 301, 869480, 15003391);
		genEntityInfo((Class) LOTREntityFish.class, "Fish", 302, 7053203, 11913189);
		genEntityInfo((Class) LOTREntityGundabadOrcTrader.class, "GundabadOrcTrader", 303, 5979436, 13421772);
		genEntityInfo((Class) LOTREntityNearHaradBlacksmith.class, "NearHaradBlacksmith", 304, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySnowTroll.class, "SnowTroll", 305, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityHarnedhrim.class, "Harnedhrim", 306, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorWarrior.class, "HarnedorWarrior", 307, 7016721, 14852422);
		genEntityInfo((Class) LOTREntityHarnedorArcher.class, "HarnedorArcher", 308, 7016721, 14852422);
		genEntityInfo((Class) LOTREntityHarnedorBannerBearer.class, "HarnedorBannerBearer", 309, 7016721, 14852422);
		genEntityInfo((Class) LOTREntityUmbarian.class, "Umbarian", 310, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarWarrior.class, "UmbarWarrior", 311, 2960680, 13540692);
		genEntityInfo((Class) LOTREntityUmbarArcher.class, "UmbarArcher", 312, 2960680, 13540692);
		genEntityInfo((Class) LOTREntityUmbarBannerBearer.class, "UmbarBannerBearer", 313, 2960680, 13540692);
		genEntityInfo((Class) LOTREntityMoredainMercenary.class, "MoredainMercenary", 314, 8998697, 14528351);
		genEntityInfo((Class) LOTREntityGulfHaradrim.class, "GulfHaradrim", 315, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfHaradWarrior.class, "GulfWarrior", 316, 7478299, 12827550);
		genEntityInfo((Class) LOTREntityGulfHaradArcher.class, "GulfArcher", 317, 7478299, 12827550);
		genEntityInfo((Class) LOTREntityGulfHaradBannerBearer.class, "GulfBannerBearer", 318, 7478299, 12827550);
		genEntityInfo((Class) LOTREntityCorsair.class, "Corsair", 319, 5521973, 12813617);
		genEntityInfo((Class) LOTREntityNomad.class, "Nomad", 320, 8278064, 853765);
		genEntityInfo((Class) LOTREntityNomadWarrior.class, "NomadWarrior", 321, 10063441, 5658198);
		genEntityInfo((Class) LOTREntityNomadArcher.class, "NomadArcher", 322, 10063441, 5658198);
		genEntityInfo((Class) LOTREntityNomadBannerBearer.class, "NomadBannerBearer", 323, 10063441, 5658198);
		genEntityInfo((Class) LOTREntityHarnedorWarlord.class, "HarnedorWarlord", 324, 7016721, 14852422);
		genEntityInfo((Class) LOTREntityUmbarCaptain.class, "UmbarCaptain", 325, 2960680, 13540692);
		genEntityInfo((Class) LOTREntityCorsairCaptain.class, "CorsairCaptain", 326, 5521973, 12813617);
		genEntityInfo((Class) LOTREntityNomadChieftain.class, "NomadChieftain", 327, 10063441, 5658198);
		genEntityInfo((Class) LOTREntityGulfHaradWarlord.class, "GulfWarlord", 328, 7478299, 12827550);
		genEntityInfo((Class) LOTREntitySouthronChampion.class, "SouthronChampion", 329, 2171169, 11868955);
		genEntityInfo((Class) LOTREntityHaradSlave.class, "HaradSlave", 330, 9860177, 5579298);
		genEntityInfo((Class) LOTREntityCorsairSlaver.class, "CorsairSlaver", 331, 5521973, 12813617);
		genEntityInfo((Class) LOTREntityHarnedorBlacksmith.class, "HarnedorBlacksmith", 332, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityUmbarBlacksmith.class, "UmbarBlacksmith", 333, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityGulfBlacksmith.class, "GulfBlacksmith", 334, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGondorRenegade.class, "GondorRenegade", 335, 1776411, 13936679);
		genEntityInfo((Class) LOTREntityNomadMerchant.class, "NomadMerchant", 336, 13551017, 7825215);
		genEntityInfo((Class) LOTREntityHarnedorBartender.class, "HarnedorBartender", 337, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorLumberman.class, "HarnedorLumberman", 338, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorMason.class, "HarnedorMason", 339, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorButcher.class, "HarnedorButcher", 340, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorBrewer.class, "HarnedorBrewer", 341, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorFishmonger.class, "HarnedorFishmonger", 342, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorBaker.class, "HarnedorBaker", 343, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorHunter.class, "HarnedorHunter", 344, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorMiner.class, "HarnedorMiner", 345, 9854777, 1181187);
		genEntityInfo((Class) LOTREntitySouthronLumberman.class, "SouthronLumberman", 346, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronMason.class, "SouthronMason", 347, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronFishmonger.class, "SouthronFishmonger", 348, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronBaker.class, "SouthronBaker", 349, 10779229, 2960685);
		genEntityInfo((Class) LOTREntitySouthronGoldsmith.class, "SouthronGoldsmith", 350, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarLumberman.class, "UmbarLumberman", 351, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarButcher.class, "UmbarButcher", 352, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarBrewer.class, "UmbarBrewer", 353, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarFishmonger.class, "UmbarFishmonger", 354, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarBaker.class, "UmbarBaker", 355, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarFlorist.class, "UmbarFlorist", 356, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarMiner.class, "UmbarMiner", 357, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarGoldsmith.class, "UmbarGoldsmith", 358, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarMason.class, "UmbarMason", 359, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityNomadMason.class, "NomadMason", 360, 8278064, 853765);
		genEntityInfo((Class) LOTREntityNomadBrewer.class, "NomadBrewer", 361, 8278064, 853765);
		genEntityInfo((Class) LOTREntityNomadMiner.class, "NomadMiner", 362, 8278064, 853765);
		genEntityInfo((Class) LOTREntityGulfMason.class, "GulfMason", 363, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfButcher.class, "GulfButcher", 364, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfBrewer.class, "GulfBrewer", 365, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfFishmonger.class, "GulfFishmonger", 366, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfBaker.class, "GulfBaker", 367, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfMiner.class, "GulfMiner", 368, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfGoldsmith.class, "GulfGoldsmith", 369, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfLumberman.class, "GulfLumberman", 370, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityGulfHunter.class, "GulfHunter", 371, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityNomadArmourer.class, "NomadArmourer", 372, 8278064, 853765);
		genEntityInfo((Class) LOTREntitySouthronBartender.class, "SouthronBartender", 373, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarBartender.class, "UmbarBartender", 374, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityGulfBartender.class, "GulfBartender", 375, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorFarmhand.class, "HarnedorFarmhand", 376, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityHarnedorFarmer.class, "HarnedorFarmer", 377, 9854777, 1181187);
		genEntityInfo((Class) LOTREntitySouthronFarmer.class, "SouthronFarmer", 378, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityUmbarFarmer.class, "UmbarFarmer", 379, 10779229, 2960685);
		genEntityInfo((Class) LOTREntityGulfFarmer.class, "GulfFarmer", 380, 9854777, 1181187);
		genEntityInfo((Class) LOTREntityTauredainSmith.class, "TauredainSmith", 381, 4468770, 12948008);
		genEntityInfo((Class) LOTREntityWhiteOryx.class, "WhiteOryx", 382, 16381146, 8154724);
		genEntityInfo((Class) LOTREntityBlackUrukCaptain.class, "BlackUrukCaptain", 383, 988430, 2830632);
		genEntityInfo((Class) LOTREntityWickedDwarf.class, "WickedDwarf", 384, 14516076, 8869453);
		genEntityInfo((Class) LOTREntityBreeMan.class, "BreeMan", 385, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeGuard.class, "BreeGuard", 386, 9335640, 3681573);
		genEntityInfo((Class) LOTREntityBreeBannerBearer.class, "BreeBannerBearer", 387, 9335640, 3681573);
		genEntityInfo((Class) LOTREntityBreeCaptain.class, "BreeCaptain", 388, 9335640, 3681573);
		genEntityInfo((Class) LOTREntityBreeBlacksmith.class, "BreeBlacksmith", 389, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeInnkeeper.class, "BreeInnkeeper", 390, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeHobbit.class, "BreeHobbit", 391, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityRuffianSpy.class, "RuffianSpy", 392, 14713187, 5191213);
		genEntityInfo((Class) LOTREntityRuffianBrute.class, "RuffianBrute", 393, 14713187, 5191213);
		genEntityInfo((Class) LOTREntityBreeHobbitInnkeeper.class, "BreeHobbitInnkeeper", 394, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityBreeBaker.class, "BreeBaker", 395, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeButcher.class, "BreeButcher", 396, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeBrewer.class, "BreeBrewer", 397, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeMason.class, "BreeMason", 398, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeLumberman.class, "BreeLumberman", 399, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeFlorist.class, "BreeFlorist", 400, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeFarmer.class, "BreeFarmer", 401, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeFarmhand.class, "BreeFarmhand", 402, 14254950, 6573367);
		genEntityInfo((Class) LOTREntityBreeHobbitBaker.class, "BreeHobbitBaker", 403, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityBreeHobbitButcher.class, "BreeHobbitButcher", 404, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityBreeHobbitBrewer.class, "BreeHobbitBrewer", 405, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityBreeHobbitFlorist.class, "BreeHobbitFlorist", 406, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityUtumnoOrc.class, "UtumnoOrc", 800, 2694951, 10377233);
		genEntityInfo((Class) LOTREntityUtumnoOrcArcher.class, "UtumnoOrcArcher", 801, 2694951, 10377233);
		genEntityInfo((Class) LOTREntityUtumnoWarg.class, "UtumnoWarg", 802, 4600617, 2694422);
		genEntityInfo((Class) LOTREntityUtumnoIceWarg.class, "UtumnoIceWarg", 803, 15066080, 9348269);
		genEntityInfo((Class) LOTREntityUtumnoObsidianWarg.class, "UtumnoObsidianWarg", 804, 2960169, 1644310);
		genEntityInfo((Class) LOTREntityUtumnoFireWarg.class, "UtumnoFireWarg", 805, 6958364, 13530368);
		genEntityInfo((Class) LOTREntityUtumnoIceSpider.class, "UtumnoIceSpider", 806, 15594495, 7697919);
		genEntityInfo((Class) LOTREntityBalrog.class, "Balrog", 807, 1772037, 13009920);
		genEntityInfo((Class) LOTREntityTormentedElf.class, "TormentedElf", 808, 14079919, 4337710);
		genEntityInfo((Class) LOTREntityUtumnoTroll.class, "UtumnoTroll", 809, 10580563, 7422265);
		genEntityInfo((Class) LOTREntityUtumnoSnowTroll.class, "UtumnoSnowTroll", 810, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityGollum.class, "Gollum", 1001, 13417872, 9471333);
		genEntityInfo((Class) LOTREntitySaruman.class, "Saruman", 1002, 15132390, 11776947);
		genEntityInfo((Class) LOTREntityGandalf.class, "Gandalf", 1003, 9605778, 5923198);
		genEntityInfo((Class) LOTREntityBlacklock.class, "Blacklock", 2036, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityBlacklockWarrior.class, "BlacklockWarrior", 2037, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityIronfistBerserk.class, "IronfistBerserk", 2039, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityBlacklockZnam.class, "BlacklockZnam", 2040, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityBlacklockCap.class, "BlacklockCap", 2041, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityAngbandOrc.class, "AngbandOrc", 2042, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandUruc.class, "AngbandUruc", 2043, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandBerserk.class, "AngbandBerserk", 2044, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandCrossbow.class, "AngbandCrossbow", 2045, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandZnam.class, "AngbandZnam", 2046, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWarg.class, "AngbandWarg", 2047, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWargIce.class, "AngbandWargIce", 2048, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWargObsidian.class, "AngbandWargObsidiank", 2049, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWargFire.class, "AngbandWargFire", 2050, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWargBombardier.class, "AngbandWargBombardier", 2051, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandTrollObsidian.class, "AngbandTrollObsidian", 2053, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandBalrog.class, "AngbandBalrog", 2054, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandCap.class, "AngbandCap", 2055, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandElf.class, "AngbandElf", 2056, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandOrcArcher.class, "AngbandArcher", 2057, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandSpiderIce.class, "AngbandSpiderIce", 2058, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandSpiderObsidian.class, "SpiderObsidian", 2059, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityBlacklockSmith.class, "RedDwarfSmith", 2060, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityAngbandZnam1.class, "AngbandZnam1", 2061, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandZnam2.class, "AngbandZnam2", 2062, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandSpiderFire.class, "AngbandSpiderFire", 2063, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityWhiteWarg.class, "WhiteWarg", 2064, 15132390, 11776947);
		genEntityInfo((Class) LOTREntityEreborDwarf.class, "EreborDwarf", 2067, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityEreborDwarfWarrior.class, "EreborDwarfWarrior", 2068, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityEreborDwarfCommander.class, "EreborDwarfCommander", 2069, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityEreborDwarfAxeThrower.class, "EreborDwarfAxeThrower", 2070, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityEreborDwarfBerserk.class, "EreborDwarfBerserk", 2071, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityEreborDwarfBannerBearer.class, "EreborDwarfBannerBearer", 2072, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityDwarfBerserk.class, "DwarfBerserk", 2073, 2638384, 7108730);
		genEntityInfo((Class) LOTREntitySauron.class, "Sauron", 2065, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityEreborDwarfCrossbow.class, "DwarfCrossbow", 2075, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityGondorGuardCitadel.class, "GondorGuardCitadel", 2076, 5327948, 15063770);
		genEntityInfo((Class) LOTREntityBlackUrukBerserk.class, "MordorBerserk", 2077, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityAngmarOrcWarrior.class, "AngmarOrcWarrior", 2078, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityStonefoot.class, "Stonefoot", 2079, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityStonefootWarrior.class, "StonefootWarrior", 2080, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityBlacklockAxe.class, "BlacklockAxe", 2081, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityStonefootZnam.class, "StonefootZnam", 2083, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityStonefootCap.class, "StonefootCap", 2084, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityStonefootFlameThrower.class, "StonefootFlameThrower", 2085, 10713966, 15357473);
		genEntityInfo((Class) LOTREntityRedDwarfWarrior3.class, "EreborGuard", 2087, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityRedDwarfAxe3.class, "EreborGuardAxe", 2088, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityAngbandTrollFire.class, "AngbandTrollFire", 2089, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityUrukHaiBerserkerArcher.class, "BerserkerArcher", 2091, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityEreborDwarfSmith.class, "EreborDwarfSmith", 2092, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityDolGuldurUruk.class, "UrukDolGuldur", 2093, 4147260, 1143324);
		genEntityInfo((Class) LOTREntityDarkElf.class, "DarkElf", 2094, 4147260, 2237218);
		genEntityInfo((Class) LOTREntityWindDwarf.class, "WindDwarf", 2095, 7557508, 7108730);
		genEntityInfo((Class) LOTREntityWindDwarfWarrior.class, "WindDwarfWarrior", 2096, 7557508, 7108730);
		genEntityInfo((Class) LOTREntityWindDwarfCommander.class, "WindDwarfCommander", 2097, 7557508, 7108730);
		genEntityInfo((Class) LOTREntityWindDwarfAxeThrower.class, "WindDwarfAxeThrower", 2098, 7557508, 7108730);
		genEntityInfo((Class) LOTREntityWindDwarfBannerBearer.class, "WindDwarfBannerBearer", 2099, 7557508, 7108730);
		genEntityInfo((Class) LOTREntityAngbandCapSpider.class, "AngbandCapSpider", 2100, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandCapWarg.class, "AngbandCapWarg", 2101, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityGundabadCaveTroll.class, "CaveTroll", 2102, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityUtumnoTrader.class, "UtumnoTrader", 2103, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityIronfist.class, "Ironfist", 2104, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityIronfistWarrior.class, "IronfistWarrior", 2105, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityIronfistCap.class, "IronfistCap", 2106, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityBlacklockAxeThrower.class, "BlacklockAxeThrower", 2107, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityIronfistZnam.class, "IronfistZnam", 2108, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityDurmethOrc.class, "DurmethOrc", 2109, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityDurmethBannerBearer.class, "DurmethBannerBearer", 2110, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityDurmethOrcArcher.class, "DurmethOrcArcher", 2111, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityDurmethOrcMercenaryCaptain.class, "DurmethOrcMercenaryCaptain", 2112, 4466722, 6382678);
		genEntityInfo((Class) LOTREntityDurmethWarg.class, "DurmethWarg", 2113, 4466722, 2694422);
		genEntityInfo((Class) LOTREntityBanditRhun.class, "BanditRhun", 2114, 16225652, 5323553);
		genEntityInfo((Class) LOTREntityStiffbeard.class, "Stiffbeard", 2116, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityStiffbeardWarrior.class, "StiffbeardWarrior", 2117, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityStiffbeardCap.class, "StiffbeardCap", 2118, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityStiffbeardCrossbow.class, "StiffbeardCrossbow", 2119, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityStiffbeardZnam.class, "StiffbeardZnam", 2120, 2638384, 7108730);
		genEntityInfo((Class) LOTREntityMordorTroll.class, "MordorTroll", 2122, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandBerserk2.class, "AngbandBoldog", 2124, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandWargFire2.class, "AngbandWargLeader", 2125, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandRuinsWraith.class, "AngbandRuinsWraith", 2126, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityBarrowWight2.class, "AngbandWraith", 2127, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandCap2.class, "AngbandOrcCap", 2128, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandOrcZnam.class, "AngbandOrcBanner", 2129, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandElf2.class, "AngbandBlockDealer", 2130, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityAngbandRuinsWraith2.class, "DarkRuinsWraith", 2132, 3343616, 15357473);
		genEntityInfo((Class) LOTREntityRedDwarfAxe4.class, "EreborGuardCrossbow", 2133, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityEreborFlameThrower.class, "EreborGuardFireThrow", 2134, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityPallando.class, "Pallando", 2135, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityBanditNorth.class, "BanditNorth", 2136, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityRedDwarfMerchant.class, "RedDwarfMerchant", 2137, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityBanditOrc.class, "BanditOrc", 2139, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityBanditDwarf.class, "BanditDwarf", 2140, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityRam.class, "MountainGoat", 2141, 15855856, 6176050);
		genEntityInfo((Class) LOTREntityPolarBear.class, "WhiteBear", 2142, 16381172, 394254);
		genEntityInfo((Class) LOTREntityDolGuldurUrukArcher.class, "UrukDolGuldurArcher", 2147, 4147260, 1143324);
		genEntityInfo((Class) LOTREntityDolGuldurUrukBannerBearer.class, "UrukDolGuldurBannerBearer", 2148, 4408654, 1143324);
		genEntityInfo((Class) LOTREntityDolGuldurUrukBerserk.class, "UrukDolGuldurBerserk", 2149, 4408654, 1143324);
		genEntityInfo((Class) LOTREntityWickedDwarf2.class, "WickedDwarfRenegade", 2150, 14516076, 8869453);
		genEntityInfo((Class) LOTREntityRedDwarfMiner.class, "RedDwarfMiner", 2152, 16353133, 15357472);
		genEntityInfo((Class) LOTREntityMoriaOrc.class, "MoriaOrc", 2153, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityMoriaOrcArcher.class, "MoriaOrcArcher", 2154, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityMoriaOrcMercenaryCaptain.class, "MoriaOrcMercenaryCaptain", 2155, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityMountainSnowTroll.class, "MountainSnowTroll", 2156, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityMountainSnowTroll2.class, "MountainUtumnoSnowTroll", 2158, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityTundraSnowTroll.class, "TundraSnowTroll", 2159, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityDeer2.class, "ArcticDeer", 2160, 14606046, 11059905);
		genEntityInfo((Class) LOTREntityDesertSpider.class, "DesertSpider", 2162, 16376764, 11772801);
		genEntityInfo((Class) LOTREntityHobbitBartender2.class, "TravelingTrader", 2163, 16376764, 11772801);
		genEntityInfo((Class) LOTREntityAvariElf.class, "AvariElf", 2168, 10056783, 16764574);
		genEntityInfo((Class) LOTREntityAvariElfScout.class, "AvariElfScout", 2169, 10056783, 3891251);
		genEntityInfo((Class) LOTREntityAvariElfWarrior.class, "AvariElfWarrior", 2170, 10056783, 5856300);
		genEntityInfo((Class) LOTREntityAvariElfCaptain.class, "AvariElfCaptain", 2171, 10056783, 5856300);
		genEntityInfo((Class) LOTREntityAvariElfBannerBearer.class, "AvariElfBannerBearer", 2172, 10056783, 5856300);
		genEntityInfo((Class) LOTREntityAvariElfSmith.class, "AvariElfSmith", 2173, 10056783, 16764574);
		genEntityInfo((Class) LOTREntityWickedElf.class, "WickedElf", 2175, 14079919, 4337710);
		genEntityInfo((Class) LOTREntityMorgulSpider.class, "MorgulSpider", 2176, 1707283, 4366886);
		genEntityInfo((Class) LOTREntityDurmethOrcWarrior.class, "DurmethOrcWarrior", 2177, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityDurmethWarriorBannerBearer.class, "DurmethWarriorBannerBearer", 2178, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityDurmethOrcWarriorArcher.class, "DurmethOrcWarriorArcher", 2179, 4466722, 8548435);
		genEntityInfo((Class) LOTREntityZebra2.class, "UtumnoHorse", 2180, 15000804, 4340020);
		genEntityInfo((Class) LOTREntityZebra3.class, "SkeletonHorse", 2181, 15000804, 4340020);
		genEntityInfo((Class) LOTREntityZebra4.class, "ZombieHorse", 2182, 15000804, 4340020);
		genEntityInfo((Class) LOTREntityZebra5.class, "NazgulHorse", 2183, 15000804, 4340020);
		genEntityInfo((Class) LOTREntityHobbitBounder2.class, "HobbitShirriffPan", 2184, 16752511, 8010275);
		genEntityInfo((Class) LOTREntityRadaghast.class, "Radaghast", 2185, 2638384, 15357472);
		genEntityInfo((Class) LOTREntityTroll2.class, "TrollMinion", 2186, 10848082, 4796702);
		genEntityInfo((Class) LOTREntityMountainTroll2.class, "MountainTroll2", 2187, 10848082, 4796702);
		genEntityInfo((Class) LOTREntityFox.class, "Fox", 2188, 12477756, 16381172);
		genEntityInfo((Class) LOTREntityFrog.class, "Frog", 2189, 6524974, 12496234);
		genEntityInfo((Class) LOTREntityBreeGuardCrossbow.class, "BreeGuardCrossbow", 2190, 9335640, 3681573);
	}

	public static void authorizeStructureInfo() {
		genStructureInfo(1, (Class) LOTRWorldGenHobbitHole.class, "HobbitHole", 2727977, 8997164);
		genStructureInfo(2, (Class) LOTRWorldGenHobbitTavern.class, "HobbitTavern", 9324081, 15975807);
		genStructureInfo(3, (Class) LOTRWorldGenHobbitPicnicBench.class, "HobbitPicnicBench", 7032622, 13882323);
		genStructureInfo(4, (Class) LOTRWorldGenHobbitWindmill.class, "HobbitWindmill", 9324081, 15975807);
		genStructureInfo(5, (Class) LOTRWorldGenHobbitFarm.class, "HobbitFarm", 9324081, 15975807);
		genStructureInfo(6, (Class) LOTRWorldGenHayBales.class, "HayBale", 14863437, 11499334);
		genStructureInfo(7, (Class) LOTRWorldGenHobbitHouse.class, "HobbitHouse", 9324081, 15975807);
		genStructureInfo(8, (Class) LOTRWorldGenHobbitBurrow.class, "HobbitBurrow", 9324081, 15975807);
		genStructureInfo(20, (Class) LOTRWorldGenBreeHouse.class, "BreeHouse", 7366748, 13547379);
		genStructureInfo(21, (Class) LOTRWorldGenBreeOffice.class, "BreeOffice", 7366748, 13547379);
		genStructureInfo(22, (Class) LOTRWorldGenBreeSmithy.class, "BreeSmithy", 7895160, 13547379);
		genStructureInfo(23, (Class) LOTRWorldGenBreeInn.class, "BreeInn", 7366748, 13547379);
		genStructureInfo(24, (Class) LOTRWorldGenBreeWell.class, "BreeWell", 7366748, 13547379);
		genStructureInfo(25, (Class) LOTRWorldGenBreeLampPost.class, "BreeLampPost", 7366748, 13547379);
		genStructureInfo(26, (Class) LOTRWorldGenBreeBarn.class, "BreeBarn", 7366748, 13547379);
		genStructureInfo(27, (Class) LOTRWorldGenBreeRuffianHouse.class, "BreeRuffianHouse", 7366748, 13547379);
		genStructureInfo(28, (Class) LOTRWorldGenBreeStable.class, "BreeStables", 7366748, 13547379);
		genStructureInfo(29, (Class) LOTRWorldGenBreeGarden.class, "BreeGarden", 3056942, 9012349);
		genStructureInfo(30, (Class) LOTRWorldGenBreeHobbitBurrow.class, "BreeHobbitBurrow", 7366748, 13547379);
		genStructureInfo(31, (Class) LOTRWorldGenBreeMarketStall.Baker.class, "BreeMarketBaker", 16246393, 13547379);
		genStructureInfo(32, (Class) LOTRWorldGenBreeMarketStall.Butcher.class, "BreeMarketButcher", 14173509, 13547379);
		genStructureInfo(33, (Class) LOTRWorldGenBreeMarketStall.Brewer.class, "BreeMarketBrewer", 11368000, 13547379);
		genStructureInfo(34, (Class) LOTRWorldGenBreeMarketStall.Mason.class, "BreeMarketMason", 8948105, 13547379);
		genStructureInfo(35, (Class) LOTRWorldGenBreeMarketStall.Lumber.class, "BreeMarketLumber", 7160619, 13547379);
		genStructureInfo(36, (Class) LOTRWorldGenBreeMarketStall.Smith.class, "BreeMarketSmith", 5658198, 13547379);
		genStructureInfo(37, (Class) LOTRWorldGenBreeMarketStall.Florist.class, "BreeMarketFlorist", 10966702, 13547379);
		genStructureInfo(38, (Class) LOTRWorldGenBreeMarketStall.Farmer.class, "BreeMarketFarmer", 5137960, 13547379);
		genStructureInfo(39, (Class) LOTRWorldGenBreeMarket.class, "BreeMarket", 7366748, 13547379);
		genStructureInfo(40, (LOTRVillageGen) new LOTRVillageGenBree(LOTRBiome.breeland, 1.0F), "BreeHamlet", 7366748, 13547379, new IVillageProperties<LOTRVillageGenBree.Instance>() {
			public void apply(LOTRVillageGenBree.Instance instance) {
				instance.villageType = LOTRVillageGenBree.VillageType.HAMLET;
			}
		});
		genStructureInfo(41, (LOTRVillageGen) new LOTRVillageGenBree(LOTRBiome.breeland, 1.0F), "BreeVillage", 7366748, 13547379, new IVillageProperties<LOTRVillageGenBree.Instance>() {
			public void apply(LOTRVillageGenBree.Instance instance) {
				instance.villageType = LOTRVillageGenBree.VillageType.VILLAGE;
			}
		});
		genStructureInfo(42, (Class) LOTRWorldGenBreeGate.class, "BreeGate", 7366748, 13547379);
		genStructureInfo(43, (Class) LOTRWorldGenBreeGatehouse.class, "BreeGatehouse", 7366748, 13547379);
		genStructureInfo(50, (Class) LOTRWorldGenBlueMountainsHouse.class, "BlueMountainsHouse", 10397380, 7633815);
		genStructureInfo(51, (Class) LOTRWorldGenBlueMountainsStronghold.class, "BlueMountainsStronghold", 10397380, 7633815);
		genStructureInfo(52, (Class) LOTRWorldGenBlueMountainsSmithy.class, "BlueMountainsSmithy", 10397380, 7633815);
		genStructureInfo(60, (Class) LOTRWorldGenHighElvenTurret.class, "HighElvenTurret", 13419962, 11380637);
		genStructureInfo(61, (Class) LOTRWorldGenRuinedHighElvenTurret.class, "RuinedHighElvenTurret", 13419962, 11380637);
		genStructureInfo(62, (Class) LOTRWorldGenHighElvenHall.class, "HighElvenHall", 13419962, 11380637);
		genStructureInfo(63, (Class) LOTRWorldGenUnderwaterElvenRuin.class, "UnderwaterElvenRuin", 13419962, 11380637);
		genStructureInfo(64, (Class) LOTRWorldGenHighElvenForge.class, "HighElvenForge", 13419962, 11380637);
		genStructureInfo(65, (Class) LOTRWorldGenRuinedEregionForge.class, "RuinedEregionForge", 13419962, 11380637);
		genStructureInfo(66, (Class) LOTRWorldGenHighElvenTower.class, "HighElvenTower", 13419962, 11380637);
		genStructureInfo(67, (Class) LOTRWorldGenTowerHillsTower.class, "TowerHillsTower", 16250346, 14211019);
		genStructureInfo(68, (Class) LOTRWorldGenHighElfHouse.class, "HighElfHouse", 13419962, 11380637);
		genStructureInfo(69, (Class) LOTRWorldGenRivendellHouse.class, "RivendellHouse", 13419962, 11380637);
		genStructureInfo(70, (Class) LOTRWorldGenRivendellHall.class, "RivendellHall", 13419962, 11380637);
		genStructureInfo(71, (Class) LOTRWorldGenRivendellForge.class, "RivendellForge", 13419962, 11380637);
		genStructureInfo(80, (Class) LOTRWorldGenRuinedDunedainTower.class, "RuinedDunedainTower", 8947848, 6052956);
		genStructureInfo(81, (Class) LOTRWorldGenRuinedHouse.class, "RuinedHouse", 8355197, 6838845);
		genStructureInfo(82, (Class) LOTRWorldGenRangerTent.class, "RangerTent", 3755037, 4142111);
		genStructureInfo(83, (Class) LOTRWorldGenNumenorRuin.class, "NumenorRuin", 8947848, 6052956);
		genStructureInfo(84, (Class) LOTRWorldGenBDBarrow.class, "BDBarrow", 6586202, 6505786);
		genStructureInfo(85, (Class) LOTRWorldGenRangerWatchtower.class, "RangerWatchtower", 5982252, 13411436);
		genStructureInfo(86, (Class) LOTRWorldGenBurntHouse.class, "BurntHouse", 1117449, 3288357);
		genStructureInfo(87, (Class) LOTRWorldGenRottenHouse.class, "RottenHouse", 3026204, 5854007);
		genStructureInfo(88, (Class) LOTRWorldGenRangerHouse.class, "RangerHouse", 5982252, 13411436);
		genStructureInfo(89, (Class) LOTRWorldGenRangerLodge.class, "RangerLodge", 5982252, 13411436);
		genStructureInfo(90, (Class) LOTRWorldGenRangerStables.class, "RangerStables", 5982252, 13411436);
		genStructureInfo(91, (Class) LOTRWorldGenRangerSmithy.class, "RangerSmithy", 5982252, 13411436);
		genStructureInfo(92, (Class) LOTRWorldGenRangerWell.class, "RangerWell", 5982252, 13411436);
		genStructureInfo(93, (Class) LOTRWorldGenRangerVillageLight.class, "RangerVillageLight", 5982252, 13411436);
		genStructureInfo(94, (LOTRVillageGen) new LOTRVillageGenDunedain(LOTRBiome.angle, 1.0F), "DunedainVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenDunedain.Instance>() {
			public void apply(LOTRVillageGenDunedain.Instance instance) {
				instance.villageType = LOTRVillageGenDunedain.VillageType.VILLAGE;
			}
		});
		genStructureInfo(95, (Class) LOTRWorldGenRangerCamp.class, "RangerCamp", 3755037, 4142111);
		genStructureInfo(120, (Class) LOTRWorldGenOrcDungeon.class, "OrcDungeon", 8947848, 6052956);
		genStructureInfo(121, (Class) LOTRWorldGenGundabadTent.class, "GundabadTent", 2301210, 131586);
		genStructureInfo(122, (Class) LOTRWorldGenGundabadForgeTent.class, "GundabadForgeTent", 2301210, 131586);
		genStructureInfo(123, (Class) LOTRWorldGenGundabadCamp.class, "GundabadCamp", 2301210, 131586);
		genStructureInfo(140, (Class) LOTRWorldGenAngmarTower.class, "AngmarTower", 3815994, 1644825);
		genStructureInfo(141, (Class) LOTRWorldGenAngmarShrine.class, "AngmarShrine", 3815994, 1644825);
		genStructureInfo(142, (Class) LOTRWorldGenAngmarWargPit.class, "AngmarWargPit", 3815994, 1644825);
		genStructureInfo(143, (Class) LOTRWorldGenAngmarTent.class, "AngmarTent", 2301210, 131586);
		genStructureInfo(144, (Class) LOTRWorldGenAngmarForgeTent.class, "AngmarForgeTent", 3815994, 1644825);
		genStructureInfo(145, (Class) LOTRWorldGenAngmarCamp.class, "AngmarCamp", 2301210, 131586);
		genStructureInfo(160, (Class) LOTRWorldGenAngmarHillmanHouse.class, "AngmarHillmanHouse", 6705465, 3813154);
		genStructureInfo(161, (Class) LOTRWorldGenAngmarHillmanChieftainHouse.class, "AngmarHillmanChieftainHouse", 6705465, 3813154);
		genStructureInfo(162, (Class) LOTRWorldGenRhudaurCastle.class, "RhudaurCastle", 3815994, 1644825);
		genStructureInfo(200, (Class) LOTRWorldGenWoodElfPlatform.class, "WoodElfLookoutPlatform", 2498840, 4932405);
		genStructureInfo(201, (Class) LOTRWorldGenWoodElfHouse.class, "WoodElfHouse", 2498840, 1004574);
		genStructureInfo(202, (Class) LOTRWorldGenWoodElfTower.class, "WoodElfTower", 12692892, 9733494);
		genStructureInfo(203, (Class) LOTRWorldGenRuinedWoodElfTower.class, "RuinedWoodElfTower", 12692892, 9733494);
		genStructureInfo(204, (Class) LOTRWorldGenWoodElvenForge.class, "WoodElvenForge", 12692892, 9733494);
		genStructureInfo(220, (Class) LOTRWorldGenDolGuldurAltar.class, "DolGuldurAltar", 4408654, 2040101);
		genStructureInfo(221, (Class) LOTRWorldGenDolGuldurTower.class, "DolGuldurTower", 4408654, 2040101);
		genStructureInfo(222, (Class) LOTRWorldGenDolGuldurSpiderPit.class, "DolGuldurSpiderPit", 4408654, 2040101);
		genStructureInfo(223, (Class) LOTRWorldGenDolGuldurTent.class, "DolGuldurTent", 2301210, 131586);
		genStructureInfo(224, (Class) LOTRWorldGenDolGuldurForgeTent.class, "DolGuldurForgeTent", 4408654, 2040101);
		genStructureInfo(225, (Class) LOTRWorldGenDolGuldurCamp.class, "DolGuldurCamp", 2301210, 131586);
		genStructureInfo(240, (Class) LOTRWorldGenDaleWatchtower.class, "DaleWatchtower", 13278568, 6836795);
		genStructureInfo(241, (Class) LOTRWorldGenDaleFortress.class, "DaleFortress", 13278568, 6836795);
		genStructureInfo(242, (Class) LOTRWorldGenDaleHouse.class, "DaleHouse", 13278568, 6836795);
		genStructureInfo(243, (Class) LOTRWorldGenDaleSmithy.class, "DaleSmithy", 13278568, 6836795);
		genStructureInfo(244, (Class) LOTRWorldGenDaleVillageTower.class, "DaleVillageTower", 13278568, 6836795);
		genStructureInfo(245, (Class) LOTRWorldGenDaleBakery.class, "DaleBakery", 13278568, 6836795);
		genStructureInfo(260, (Class) LOTRWorldGenDwarvenMineEntrance.class, "DwarvenMineEntrance", 4935761, 2961971);
		genStructureInfo(261, (Class) LOTRWorldGenDwarvenTower.class, "DwarvenTower", 4935761, 2961971);
		genStructureInfo(262, (Class) LOTRWorldGenDwarfHouse.class, "DwarfHouse", 4935761, 2961971);
		genStructureInfo(263, (Class) LOTRWorldGenDwarvenMineEntranceRuined.class, "DwarvenMineEntranceRuined", 4935761, 2961971);
		genStructureInfo(264, (Class) LOTRWorldGenDwarfSmithy.class, "DwarfSmithy", 4935761, 2961971);
		genStructureInfo(265, (Class) LOTRWorldGenRuinedDwarvenTower.class, "DwarvenTowerRuined", 4935761, 2961971);
		genStructureInfo(280, (Class) LOTRWorldGenElfHouse.class, "ElfHouse", 15325615, 2315809);
		genStructureInfo(281, (Class) LOTRWorldGenElfLordHouse.class, "ElfLordHouse", 15325615, 2315809);
		genStructureInfo(282, (Class) LOTRWorldGenGaladhrimForge.class, "GaladhrimForge", 14407118, 10854552);
		genStructureInfo(300, (Class) LOTRWorldGenMeadHall.class, "RohanMeadHall", 5982252, 13411436);
		genStructureInfo(301, (Class) LOTRWorldGenRohanWatchtower.class, "RohanWatchtower", 5982252, 13411436);
		genStructureInfo(302, (Class) LOTRWorldGenRohanBarrow.class, "RohanBarrow", 9016133, 16775901);
		genStructureInfo(303, (Class) LOTRWorldGenRohanFortress.class, "RohanFortress", 5982252, 13411436);
		genStructureInfo(304, (Class) LOTRWorldGenRohanHouse.class, "RohanHouse", 5982252, 13411436);
		genStructureInfo(305, (Class) LOTRWorldGenRohanSmithy.class, "RohanSmithy", 5982252, 13411436);
		genStructureInfo(306, (Class) LOTRWorldGenRohanVillageFarm.class, "RohanVillageFarm", 7648578, 8546111);
		genStructureInfo(307, (Class) LOTRWorldGenRohanStables.class, "RohanStables", 5982252, 13411436);
		genStructureInfo(308, (Class) LOTRWorldGenRohanBarn.class, "RohanBarn", 5982252, 13411436);
		genStructureInfo(309, (Class) LOTRWorldGenRohanWell.class, "RohanWell", 5982252, 13411436);
		genStructureInfo(310, (Class) LOTRWorldGenRohanVillageGarden.class, "RohanVillageGarden", 7648578, 8546111);
		genStructureInfo(311, (Class) LOTRWorldGenRohanMarketStall.Blacksmith.class, "RohanMarketBlacksmith", 2960684, 13411436);
		genStructureInfo(312, (Class) LOTRWorldGenRohanMarketStall.Farmer.class, "RohanMarketFarmer", 15066597, 13411436);
		genStructureInfo(313, (Class) LOTRWorldGenRohanMarketStall.Lumber.class, "RohanMarketLumber", 5981994, 13411436);
		genStructureInfo(314, (Class) LOTRWorldGenRohanMarketStall.Builder.class, "RohanMarketBuilder", 7693401, 13411436);
		genStructureInfo(315, (Class) LOTRWorldGenRohanMarketStall.Brewer.class, "RohanMarketBrewer", 13874218, 13411436);
		genStructureInfo(316, (Class) LOTRWorldGenRohanMarketStall.Butcher.class, "RohanMarketButcher", 16358066, 13411436);
		genStructureInfo(317, (Class) LOTRWorldGenRohanMarketStall.Fish.class, "RohanMarketFish", 9882879, 13411436);
		genStructureInfo(318, (Class) LOTRWorldGenRohanMarketStall.Baker.class, "RohanMarketBaker", 14725995, 13411436);
		genStructureInfo(319, (Class) LOTRWorldGenRohanMarketStall.Orcharder.class, "RohanMarketOrcharder", 9161006, 13411436);
		genStructureInfo(320, (Class) LOTRWorldGenRohanVillagePasture.class, "RohanVillagePasture", 7648578, 8546111);
		genStructureInfo(321, (Class) LOTRWorldGenRohanVillageSign.class, "RohanVillageSign", 5982252, 13411436);
		genStructureInfo(322, (Class) LOTRWorldGenRohanGatehouse.class, "RohanGatehouse", 5982252, 13411436);
		genStructureInfo(323, (LOTRVillageGen) new LOTRVillageGenRohan(LOTRBiome.rohan, 1.0F), "RohanVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenRohan.Instance>() {
			public void apply(LOTRVillageGenRohan.Instance instance) {
				instance.villageType = LOTRVillageGenRohan.VillageType.VILLAGE;
			}
		});
		genStructureInfo(324, (LOTRVillageGen) new LOTRVillageGenRohan(LOTRBiome.rohan, 1.0F), "RohanFortVillage", 5982252, 13411436, new IVillageProperties<LOTRVillageGenRohan.Instance>() {
			public void apply(LOTRVillageGenRohan.Instance instance) {
				instance.villageType = LOTRVillageGenRohan.VillageType.FORT;
			}
		});
		genStructureInfo(350, (Class) LOTRWorldGenUrukTent.class, "UrukTent", 2301210, 131586);
		genStructureInfo(351, (Class) LOTRWorldGenRuinedRohanWatchtower.class, "RuinedRohanWatchtower", 1117449, 3288357);
		genStructureInfo(352, (Class) LOTRWorldGenUrukForgeTent.class, "UrukForgeTent", 3682596, 2038547);
		genStructureInfo(353, (Class) LOTRWorldGenUrukWargPit.class, "UrukWargPit", 3682596, 2038547);
		genStructureInfo(354, (Class) LOTRWorldGenUrukCamp.class, "UrukCamp", 2301210, 131586);
		genStructureInfo(380, (Class) LOTRWorldGenDunlendingHouse.class, "DunlendingHouse", 6705465, 3813154);
		genStructureInfo(381, (Class) LOTRWorldGenDunlendingTavern.class, "DunlendingTavern", 6705465, 3813154);
		genStructureInfo(382, (Class) LOTRWorldGenDunlendingCampfire.class, "DunlendingCampfire", 9539472, 6837299);
		genStructureInfo(383, (Class) LOTRWorldGenDunlandHillFort.class, "DunlandHillFort", 6705465, 3813154);
		genStructureInfo(400, (Class) LOTRWorldGenBeaconTower.class, "BeaconTower", 14869218, 11513775);
		genStructureInfo(401, (Class) LOTRWorldGenGondorWatchfort.class, "GondorWatchfort", 14869218, 2367263);
		genStructureInfo(402, (Class) LOTRWorldGenGondorSmithy.class, "GondorSmithy", 14869218, 2367263);
		genStructureInfo(403, (Class) LOTRWorldGenGondorTurret.class, "GondorTurret", 14869218, 11513775);
		genStructureInfo(404, (Class) LOTRWorldGenIthilienHideout.class, "IthilienHideout", 8882055, 7365464);
		genStructureInfo(405, (Class) LOTRWorldGenGondorHouse.class, "GondorHouse", 14869218, 9861961);
		genStructureInfo(406, (Class) LOTRWorldGenGondorCottage.class, "GondorCottage", 14869218, 9861961);
		genStructureInfo(407, (Class) LOTRWorldGenGondorStoneHouse.class, "GondorStoneHouse", 14869218, 2367263);
		genStructureInfo(408, (Class) LOTRWorldGenGondorWatchtower.class, "GondorWatchtower", 14869218, 11513775);
		genStructureInfo(409, (Class) LOTRWorldGenGondorStables.class, "GondorStables", 14869218, 9861961);
		genStructureInfo(410, (Class) LOTRWorldGenGondorBarn.class, "GondorBarn", 14869218, 9861961);
		genStructureInfo(411, (Class) LOTRWorldGenGondorFortress.class, "GondorFortress", 14869218, 2367263);
		genStructureInfo(412, (Class) LOTRWorldGenGondorTavern.class, "GondorTavern", 14869218, 9861961);
		genStructureInfo(413, (Class) LOTRWorldGenGondorWell.class, "GondorWell", 14869218, 11513775);
		genStructureInfo(414, (Class) LOTRWorldGenGondorVillageFarm.Crops.class, "GondorFarmCrops", 7047232, 15066597);
		genStructureInfo(415, (Class) LOTRWorldGenGondorVillageFarm.Animals.class, "GondorFarmAnimals", 7047232, 15066597);
		genStructureInfo(416, (Class) LOTRWorldGenGondorVillageFarm.Tree.class, "GondorFarmTree", 7047232, 15066597);
		genStructureInfo(417, (Class) LOTRWorldGenGondorMarketStall.Greengrocer.class, "GondorMarketGreengrocer", 8567851, 9861961);
		genStructureInfo(418, (Class) LOTRWorldGenGondorMarketStall.Lumber.class, "GondorMarketLumber", 5981994, 9861961);
		genStructureInfo(419, (Class) LOTRWorldGenGondorMarketStall.Mason.class, "GondorMarketMason", 10526621, 9861961);
		genStructureInfo(420, (Class) LOTRWorldGenGondorMarketStall.Brewer.class, "GondorMarketBrewer", 13874218, 9861961);
		genStructureInfo(421, (Class) LOTRWorldGenGondorMarketStall.Flowers.class, "GondorMarketFlowers", 16243515, 9861961);
		genStructureInfo(422, (Class) LOTRWorldGenGondorMarketStall.Butcher.class, "GondorMarketButcher", 14521508, 9861961);
		genStructureInfo(423, (Class) LOTRWorldGenGondorMarketStall.Fish.class, "GondorMarketFish", 6862591, 9861961);
		genStructureInfo(424, (Class) LOTRWorldGenGondorMarketStall.Farmer.class, "GondorMarketFarmer", 14401433, 9861961);
		genStructureInfo(425, (Class) LOTRWorldGenGondorMarketStall.Blacksmith.class, "GondorMarketBlacksmith", 2960684, 9861961);
		genStructureInfo(426, (Class) LOTRWorldGenGondorMarketStall.Baker.class, "GondorMarketBaker", 13543009, 9861961);
		genStructureInfo(427, (Class) LOTRWorldGenGondorVillageSign.class, "GondorVillageSign", 5982252, 13411436);
		genStructureInfo(428, (Class) LOTRWorldGenGondorBath.class, "GondorBath", 14869218, 2367263);
		genStructureInfo(429, (Class) LOTRWorldGenGondorGatehouse.class, "GondorGatehouse", 14869218, 2367263);
		genStructureInfo(430, (Class) LOTRWorldGenGondorLampPost.class, "GondorLampPost", 14869218, 11513775);
		genStructureInfo(431, (Class) LOTRWorldGenGondorTownGarden.class, "GondorTownGarden", 7047232, 15066597);
		genStructureInfo(432, (Class) LOTRWorldGenGondorTownTrees.class, "GondorTownTrees", 7047232, 15066597);
		genStructureInfo(433, (Class) LOTRWorldGenGondorTownBench.class, "GondorTownBench", 14869218, 11513775);
		genStructureInfo(434, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0F), "GondorVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(435, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0F), "GondorTown", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(436, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.gondor, LOTRWorldGenGondorStructure.GondorFiefdom.GONDOR, 1.0F), "GondorFortVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(450, (Class) LOTRWorldGenRuinedBeaconTower.class, "RuinedBeaconTower", 14869218, 11513775);
		genStructureInfo(451, (Class) LOTRWorldGenRuinedGondorTower.class, "RuinedGondorTower", 14869218, 11513775);
		genStructureInfo(452, (Class) LOTRWorldGenGondorObelisk.class, "GondorObelisk", 14869218, 11513775);
		genStructureInfo(453, (Class) LOTRWorldGenGondorRuin.class, "GondorRuin", 14869218, 11513775);
		genStructureInfo(500, (Class) LOTRWorldGenDolAmrothStables.class, "DolAmrothStables", 15002613, 2709918);
		genStructureInfo(501, (Class) LOTRWorldGenDolAmrothWatchtower.class, "DolAmrothWatchtower", 14869218, 11513775);
		genStructureInfo(502, (Class) LOTRWorldGenDolAmrothWatchfort.class, "DolAmrothWatchfort", 15002613, 2709918);
		genStructureInfo(503, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0F), "DolAmrothVillage", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(504, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0F), "DolAmrothTown", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(505, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.dorEnErnil, LOTRWorldGenGondorStructure.GondorFiefdom.DOL_AMROTH, 1.0F), "DolAmrothFortVillage", 15002613, 2709918, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(510, (Class) LOTRWorldGenLossarnachFortress.class, "LossarnachFortress", 14869218, 15138816);
		genStructureInfo(511, (Class) LOTRWorldGenLossarnachWatchtower.class, "LossarnachWatchtower", 14869218, 11513775);
		genStructureInfo(512, (Class) LOTRWorldGenLossarnachWatchfort.class, "LossarnachWatchfort", 14869218, 15138816);
		genStructureInfo(513, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0F), "LossarnachVillage", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(514, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0F), "LossarnachTown", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(515, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lossarnach, LOTRWorldGenGondorStructure.GondorFiefdom.LOSSARNACH, 1.0F), "LossarnachFortVillage", 14869218, 15138816, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(520, (Class) LOTRWorldGenLebenninFortress.class, "LebenninFortress", 14869218, 621750);
		genStructureInfo(521, (Class) LOTRWorldGenLebenninWatchtower.class, "LebenninWatchtower", 14869218, 11513775);
		genStructureInfo(522, (Class) LOTRWorldGenLebenninWatchfort.class, "LebenninWatchfort", 14869218, 621750);
		genStructureInfo(523, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0F), "LebenninVillage", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(524, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0F), "LebenninTown", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(525, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lebennin, LOTRWorldGenGondorStructure.GondorFiefdom.LEBENNIN, 1.0F), "LebenninFortVillage", 14869218, 621750, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(530, (Class) LOTRWorldGenPelargirFortress.class, "PelargirFortress", 14869218, 2917253);
		genStructureInfo(531, (Class) LOTRWorldGenPelargirWatchtower.class, "PelargirWatchtower", 14869218, 11513775);
		genStructureInfo(532, (Class) LOTRWorldGenPelargirWatchfort.class, "PelargirWatchfort", 14869218, 2917253);
		genStructureInfo(533, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0F), "PelargirVillage", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(534, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0F), "PelargirTown", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(535, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pelargir, LOTRWorldGenGondorStructure.GondorFiefdom.PELARGIR, 1.0F), "PelargirFortVillage", 14869218, 2917253, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(540, (Class) LOTRWorldGenPinnathGelinFortress.class, "PinnathGelinFortress", 14869218, 1401651);
		genStructureInfo(541, (Class) LOTRWorldGenPinnathGelinWatchtower.class, "PinnathGelinWatchtower", 14869218, 11513775);
		genStructureInfo(542, (Class) LOTRWorldGenPinnathGelinWatchfort.class, "PinnathGelinWatchfort", 14869218, 1401651);
		genStructureInfo(543, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0F), "PinnathGelinVillage", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(544, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0F), "PinnathGelinTown", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(545, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.pinnathGelin, LOTRWorldGenGondorStructure.GondorFiefdom.PINNATH_GELIN, 1.0F), "PinnathGelinFortVillage", 14869218, 1401651, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(550, (Class) LOTRWorldGenBlackrootFortress.class, "BlackrootFortress", 14869218, 2367263);
		genStructureInfo(551, (Class) LOTRWorldGenBlackrootWatchtower.class, "BlackrootWatchtower", 14869218, 11513775);
		genStructureInfo(552, (Class) LOTRWorldGenBlackrootWatchfort.class, "BlackrootWatchfort", 14869218, 2367263);
		genStructureInfo(553, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0F), "BlackrootVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(554, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0F), "BlackrootTown", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(555, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.blackrootVale, LOTRWorldGenGondorStructure.GondorFiefdom.BLACKROOT_VALE, 1.0F), "BlackrootFortVillage", 14869218, 2367263, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(560, (Class) LOTRWorldGenLamedonFortress.class, "LamedonFortress", 14869218, 1784649);
		genStructureInfo(561, (Class) LOTRWorldGenLamedonWatchtower.class, "LamedonWatchtower", 14869218, 11513775);
		genStructureInfo(562, (Class) LOTRWorldGenLamedonWatchfort.class, "LamedonWatchfort", 14869218, 1784649);
		genStructureInfo(563, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0F), "LamedonVillage", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(564, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0F), "LamedonTown", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.TOWN;
			}
		});
		genStructureInfo(565, (LOTRVillageGen) new LOTRVillageGenGondor(LOTRBiome.lamedon, LOTRWorldGenGondorStructure.GondorFiefdom.LAMEDON, 1.0F), "LamedonFortVillage", 14869218, 1784649, new IVillageProperties<LOTRVillageGenGondor.Instance>() {
			public void apply(LOTRVillageGenGondor.Instance instance) {
				instance.villageType = LOTRVillageGenGondor.VillageType.FORT;
			}
		});
		genStructureInfo(600, (Class) LOTRWorldGenMordorTower.class, "MordorTower", 2631720, 328965);
		genStructureInfo(601, (Class) LOTRWorldGenMordorTent.class, "MordorTent", 2301210, 131586);
		genStructureInfo(602, (Class) LOTRWorldGenMordorForgeTent.class, "MordorForgeTent", 2631720, 328965);
		genStructureInfo(603, (Class) LOTRWorldGenMordorWargPit.class, "MordorWargPit", 2631720, 328965);
		genStructureInfo(604, (Class) LOTRWorldGenMordorCamp.class, "MordorCamp", 2301210, 131586);
		genStructureInfo(605, (Class) LOTRWorldGenBlackUrukFort.class, "BlackUrukFort", 2631720, 328965);
		genStructureInfo(650, (Class) LOTRWorldGenNurnWheatFarm.class, "NurnWheatFarm", 4469796, 328965);
		genStructureInfo(651, (Class) LOTRWorldGenOrcSlaverTower.class, "OrcSlaverTower", 1117449, 3288357);
		genStructureInfo(670, (Class) LOTRWorldGenMordorSpiderPit.class, "MordorSpiderPit", 1511181, 12917534);
		genStructureInfo(700, (Class) LOTRWorldGenDorwinionGarden.class, "DorwinionGarden", 16572875, 13418417);
		genStructureInfo(701, (Class) LOTRWorldGenDorwinionTent.class, "DorwinionTent", 6706573, 15058766);
		genStructureInfo(702, (Class) LOTRWorldGenDorwinionCaptainTent.class, "DorwinionCaptainTent", 6706573, 15058766);
		genStructureInfo(703, (Class) LOTRWorldGenDorwinionHouse.class, "DorwinionHouse", 7167128, 15390149);
		genStructureInfo(704, (Class) LOTRWorldGenDorwinionBrewery.class, "DorwinionBrewery", 7167128, 15390149);
		genStructureInfo(705, (Class) LOTRWorldGenDorwinionElfHouse.class, "DorwinionElfHouse", 7167128, 15390149);
		genStructureInfo(706, (Class) LOTRWorldGenDorwinionBath.class, "DorwinionBath", 7167128, 15390149);
		genStructureInfo(750, (Class) LOTRWorldGenEasterlingHouse.class, "EasterlingHouse", 12693373, 7689786);
		genStructureInfo(751, (Class) LOTRWorldGenEasterlingStables.class, "EasterlingStables", 12693373, 7689786);
		genStructureInfo(752, (Class) LOTRWorldGenEasterlingTownHouse.class, "EasterlingTownHouse", 6304287, 12693373);
		genStructureInfo(753, (Class) LOTRWorldGenEasterlingLargeTownHouse.class, "EasterlingLargeTownHouse", 6304287, 12693373);
		genStructureInfo(754, (Class) LOTRWorldGenEasterlingFortress.class, "EasterlingFortress", 6304287, 12693373);
		genStructureInfo(755, (Class) LOTRWorldGenEasterlingTower.class, "EasterlingTower", 6304287, 12693373);
		genStructureInfo(756, (Class) LOTRWorldGenEasterlingSmithy.class, "EasterlingSmithy", 6304287, 12693373);
		genStructureInfo(757, (Class) LOTRWorldGenEasterlingMarketStall.Blacksmith.class, "EasterlingMarketBlacksmith", 2960684, 12693373);
		genStructureInfo(758, (Class) LOTRWorldGenEasterlingMarketStall.Lumber.class, "EasterlingMarketLumber", 5981994, 12693373);
		genStructureInfo(759, (Class) LOTRWorldGenEasterlingMarketStall.Mason.class, "EasterlingMarketMason", 7039594, 12693373);
		genStructureInfo(760, (Class) LOTRWorldGenEasterlingMarketStall.Butcher.class, "EasterlingMarketButcher", 12544103, 12693373);
		genStructureInfo(761, (Class) LOTRWorldGenEasterlingMarketStall.Brewer.class, "EasterlingMarketBrewer", 11891243, 12693373);
		genStructureInfo(762, (Class) LOTRWorldGenEasterlingMarketStall.Fish.class, "EasterlingMarketFish", 4882395, 12693373);
		genStructureInfo(763, (Class) LOTRWorldGenEasterlingMarketStall.Baker.class, "EasterlingMarketBaker", 14725995, 12693373);
		genStructureInfo(764, (Class) LOTRWorldGenEasterlingMarketStall.Hunter.class, "EasterlingMarketHunter", 4471854, 12693373);
		genStructureInfo(765, (Class) LOTRWorldGenEasterlingMarketStall.Farmer.class, "EasterlingMarketFarmer", 8893759, 12693373);
		genStructureInfo(766, (Class) LOTRWorldGenEasterlingMarketStall.Gold.class, "EasterlingMarketGold", 16237060, 12693373);
		genStructureInfo(767, (Class) LOTRWorldGenEasterlingTavern.class, "EasterlingTavern", 12693373, 7689786);
		genStructureInfo(768, (Class) LOTRWorldGenEasterlingTavernTown.class, "EasterlingTavernTown", 6304287, 12693373);
		genStructureInfo(769, (Class) LOTRWorldGenEasterlingStatue.class, "EasterlingStatue", 12693373, 7689786);
		genStructureInfo(770, (Class) LOTRWorldGenEasterlingGarden.class, "EasterlingGarden", 4030994, 12693373);
		genStructureInfo(771, (Class) LOTRWorldGenEasterlingVillageSign.class, "EasterlingVillageSign", 12693373, 7689786);
		genStructureInfo(772, (Class) LOTRWorldGenEasterlingWell.class, "EasterlingWell", 12693373, 7689786);
		genStructureInfo(773, (Class) LOTRWorldGenEasterlingVillageFarm.Crops.class, "EasterlingFarmCrops", 4030994, 12693373);
		genStructureInfo(774, (Class) LOTRWorldGenEasterlingVillageFarm.Animals.class, "EasterlingFarmAnimals", 4030994, 12693373);
		genStructureInfo(775, (Class) LOTRWorldGenEasterlingVillageFarm.Tree.class, "EasterlingFarmTree", 4030994, 12693373);
		genStructureInfo(776, (Class) LOTRWorldGenEasterlingGatehouse.class, "EasterlingGatehouse", 6304287, 12693373);
		genStructureInfo(777, (Class) LOTRWorldGenEasterlingLamp.class, "EasterlingLamp", 6304287, 12693373);
		genStructureInfo(778, (LOTRVillageGen) new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0F, true), "EasterlingVillage", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.VILLAGE;
			}
		});
		genStructureInfo(779, (LOTRVillageGen) new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0F, true), "EasterlingTown", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.TOWN;
			}
		});
		genStructureInfo(780, (LOTRVillageGen) new LOTRVillageGenRhun(LOTRBiome.rhunLand, 1.0F, true), "EasterlingFortVillage", 6304287, 12693373, new IVillageProperties<LOTRVillageGenRhun.Instance>() {
			public void apply(LOTRVillageGenRhun.Instance instance) {
				instance.villageType = LOTRVillageGenRhun.VillageType.FORT;
			}
		});
		genStructureInfo(1000, (Class) LOTRWorldGenHaradObelisk.class, "HaradObelisk", 10854007, 15590575);
		genStructureInfo(1001, (Class) LOTRWorldGenHaradPyramid.class, "HaradPyramid", 10854007, 15590575);
		genStructureInfo(1002, (Class) LOTRWorldGenMumakSkeleton.class, "MumakSkeleton", 14737111, 16250349);
		genStructureInfo(1003, (Class) LOTRWorldGenHaradRuinedFort.class, "HaradRuinedFort", 10854007, 15590575);
		genStructureInfo(1050, (Class) LOTRWorldGenHarnedorHouse.class, "HarnedorHouse", 4994339, 12814421);
		genStructureInfo(1051, (Class) LOTRWorldGenHarnedorSmithy.class, "HarnedorSmithy", 4994339, 12814421);
		genStructureInfo(1052, (Class) LOTRWorldGenHarnedorTavern.class, "HarnedorTavern", 4994339, 12814421);
		genStructureInfo(1053, (Class) LOTRWorldGenHarnedorMarket.class, "HarnedorMarket", 4994339, 12814421);
		genStructureInfo(1054, (Class) LOTRWorldGenHarnedorTower.class, "HarnedorTower", 4994339, 12814421);
		genStructureInfo(1055, (Class) LOTRWorldGenHarnedorFort.class, "HarnedorFort", 4994339, 12814421);
		genStructureInfo(1056, (Class) LOTRWorldGenNearHaradTent.class, "NearHaradTent", 13519170, 1775897);
		genStructureInfo(1057, (Class) LOTRWorldGenHarnedorFarm.class, "HarnedorFarm", 10073953, 12814421);
		genStructureInfo(1058, (Class) LOTRWorldGenHarnedorPasture.class, "HarnedorPasture", 10073953, 12814421);
		genStructureInfo(1059, (LOTRVillageGen) new LOTRVillageGenHarnedor(LOTRBiome.harnedor, 1.0F), "HarnedorVillage", 4994339, 12814421, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1060, (Class) LOTRWorldGenHarnedorStables.class, "HarnedorStables", 4994339, 12814421);
		genStructureInfo(1061, (Class) LOTRWorldGenHarnedorVillageSign.class, "HarnedorVillageSign", 4994339, 12814421);
		genStructureInfo(1062, (LOTRVillageGen) new LOTRVillageGenHarnedor(LOTRBiome.harnedor, 1.0F), "HarnedorFortVillage", 4994339, 12814421, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.FORTRESS;
			}
		});
		genStructureInfo(1080, (Class) LOTRWorldGenHarnedorHouseRuined.class, "HarnedorHouseRuined", 5519919, 10059372);
		genStructureInfo(1081, (Class) LOTRWorldGenHarnedorTavernRuined.class, "HarnedorTavernRuined", 5519919, 10059372);
		genStructureInfo(1082, (LOTRVillageGen) (new LOTRVillageGenHarnedor(LOTRBiome.harondor, 1.0F)).setRuined(), "HarnedorVillageRuined", 5519919, 10059372, new IVillageProperties<LOTRVillageGenHarnedor.Instance>() {
			public void apply(LOTRVillageGenHarnedor.Instance instance) {
				instance.villageType = LOTRVillageGenHarnedor.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1100, (Class) LOTRWorldGenSouthronHouse.class, "SouthronHouse", 15063989, 10052655);
		genStructureInfo(1101, (Class) LOTRWorldGenSouthronTavern.class, "SouthronTavern", 15063989, 10052655);
		genStructureInfo(1102, (Class) LOTRWorldGenSouthronSmithy.class, "SouthronSmithy", 15063989, 10052655);
		genStructureInfo(1103, (Class) LOTRWorldGenSouthronTower.class, "SouthronTower", 15063989, 10052655);
		genStructureInfo(1104, (Class) LOTRWorldGenSouthronMansion.class, "SouthronMansion", 15063989, 10052655);
		genStructureInfo(1105, (Class) LOTRWorldGenSouthronStables.class, "SouthronStables", 15063989, 10052655);
		genStructureInfo(1106, (Class) LOTRWorldGenSouthronFarm.class, "SouthronFarm", 9547581, 10052655);
		genStructureInfo(1107, (Class) LOTRWorldGenSouthronFortress.class, "SouthronFortress", 15063989, 10052655);
		genStructureInfo(1108, (Class) LOTRWorldGenSouthronWell.class, "SouthronWell", 15063989, 10052655);
		genStructureInfo(1109, (Class) LOTRWorldGenSouthronBazaar.class, "SouthronBazaar", 15063989, 10052655);
		genStructureInfo(1110, (Class) LOTRWorldGenSouthronPasture.class, "SouthronPasture", 9547581, 10052655);
		genStructureInfo(1111, (Class) LOTRWorldGenSouthronVillageSign.class, "SouthronVillageSign", 15063989, 10052655);
		genStructureInfo(1112, (LOTRVillageGen) new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0F), "SouthronVillage", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1113, (Class) LOTRWorldGenSouthronStatue.class, "SouthronStatue", 15063989, 10052655);
		genStructureInfo(1114, (Class) LOTRWorldGenSouthronBarracks.class, "SouthronBarracks", 15063989, 10052655);
		genStructureInfo(1115, (Class) LOTRWorldGenSouthronTraining.class, "SouthronTraining", 15063989, 10052655);
		genStructureInfo(1116, (Class) LOTRWorldGenSouthronFortGate.class, "SouthronFortGate", 15063989, 10052655);
		genStructureInfo(1117, (LOTRVillageGen) new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0F), "SouthronFortVillage", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.FORT;
			}
		});
		genStructureInfo(1118, (Class) LOTRWorldGenSouthronLamp.class, "SouthronLamp", 15063989, 10052655);
		genStructureInfo(1119, (Class) LOTRWorldGenSouthronTownTree.class, "SouthronTownTree", 9547581, 10052655);
		genStructureInfo(1120, (Class) LOTRWorldGenSouthronTownFlowers.class, "SouthronTownFlowers", 9547581, 10052655);
		genStructureInfo(1121, (LOTRVillageGen) new LOTRVillageGenSouthron(LOTRBiome.nearHaradFertile, 1.0F), "SouthronTown", 15063989, 10052655, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN;
			}
		});
		genStructureInfo(1122, (Class) LOTRWorldGenSouthronTownGate.class, "SouthronTownGate", 15063989, 10052655);
		genStructureInfo(1123, (Class) LOTRWorldGenSouthronTownCorner.class, "SouthronTownCorner", 15063989, 10052655);
		genStructureInfo(1140, (Class) LOTRWorldGenMoredainMercTent2.class, "MoredainMercTent", 12845056, 2949120);
		genStructureInfo(1141, (Class) LOTRWorldGenMoredainMercCamp.class, "MoredainMercCamp", 12845056, 2949120);
		genStructureInfo(1150, (Class) LOTRWorldGenUmbarHouse.class, "UmbarHouse", 14407104, 3354926);
		genStructureInfo(1151, (Class) LOTRWorldGenUmbarTavern.class, "UmbarTavern", 14407104, 3354926);
		genStructureInfo(1152, (Class) LOTRWorldGenUmbarSmithy.class, "UmbarSmithy", 14407104, 3354926);
		genStructureInfo(1153, (Class) LOTRWorldGenUmbarTower.class, "UmbarTower", 14407104, 3354926);
		genStructureInfo(1154, (Class) LOTRWorldGenUmbarMansion.class, "UmbarMansion", 14407104, 3354926);
		genStructureInfo(1155, (Class) LOTRWorldGenUmbarStables.class, "UmbarStables", 14407104, 3354926);
		genStructureInfo(1156, (Class) LOTRWorldGenUmbarFarm.class, "UmbarFarm", 9547581, 3354926);
		genStructureInfo(1157, (Class) LOTRWorldGenUmbarFortress.class, "UmbarFortress", 14407104, 3354926);
		genStructureInfo(1158, (Class) LOTRWorldGenUmbarWell.class, "UmbarWell", 14407104, 3354926);
		genStructureInfo(1159, (Class) LOTRWorldGenUmbarBazaar.class, "UmbarBazaar", 14407104, 3354926);
		genStructureInfo(1160, (Class) LOTRWorldGenUmbarPasture.class, "UmbarPasture", 9547581, 3354926);
		genStructureInfo(1161, (Class) LOTRWorldGenUmbarVillageSign.class, "UmbarVillageSign", 14407104, 3354926);
		genStructureInfo(1162, (LOTRVillageGen) new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0F), "UmbarVillage", 14407104, 3354926, new IVillageProperties<LOTRVillageGenUmbar.InstanceUmbar>() {
			public void apply(LOTRVillageGenUmbar.InstanceUmbar instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1163, (Class) LOTRWorldGenUmbarStatue.class, "UmbarStatue", 14407104, 3354926);
		genStructureInfo(1164, (Class) LOTRWorldGenUmbarBarracks.class, "UmbarBarracks", 14407104, 3354926);
		genStructureInfo(1165, (Class) LOTRWorldGenUmbarTraining.class, "UmbarTraining", 14407104, 3354926);
		genStructureInfo(1166, (Class) LOTRWorldGenUmbarFortGate.class, "UmbarFortGate", 14407104, 3354926);
		genStructureInfo(1167, (LOTRVillageGen) new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0F), "UmbarFortVillage", 14407104, 3354926, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.FORT;
			}
		});
		genStructureInfo(1168, (Class) LOTRWorldGenUmbarLamp.class, "UmbarLamp", 14407104, 3354926);
		genStructureInfo(1169, (Class) LOTRWorldGenUmbarTownTree.class, "UmbarTownTree", 9547581, 3354926);
		genStructureInfo(1170, (Class) LOTRWorldGenUmbarTownFlowers.class, "UmbarTownFlowers", 9547581, 3354926);
		genStructureInfo(1171, (LOTRVillageGen) new LOTRVillageGenUmbar(LOTRBiome.umbar, 1.0F), "UmbarTown", 14407104, 3354926, new IVillageProperties<LOTRVillageGenSouthron.Instance>() {
			public void apply(LOTRVillageGenSouthron.Instance instance) {
				instance.villageType = LOTRVillageGenSouthron.VillageType.TOWN;
			}
		});
		genStructureInfo(1172, (Class) LOTRWorldGenUmbarTownGate.class, "UmbarTownGate", 14407104, 3354926);
		genStructureInfo(1173, (Class) LOTRWorldGenUmbarTownCorner.class, "UmbarTownCorner", 14407104, 3354926);
		genStructureInfo(1180, (Class) LOTRWorldGenCorsairCove.class, "CorsairCove", 8355711, 1644825);
		genStructureInfo(1181, (Class) LOTRWorldGenCorsairTent.class, "CorsairTent", 5658198, 657930);
		genStructureInfo(1182, (Class) LOTRWorldGenCorsairCamp.class, "CorsairCamp", 5658198, 657930);
		genStructureInfo(1200, (Class) LOTRWorldGenNomadTent.class, "NomadTent", 16775927, 8345150);
		genStructureInfo(1201, (Class) LOTRWorldGenNomadTentLarge.class, "NomadTentLarge", 16775927, 8345150);
		genStructureInfo(1202, (Class) LOTRWorldGenNomadChieftainTent.class, "NomadChieftainTent", 16775927, 8345150);
		genStructureInfo(1203, (Class) LOTRWorldGenNomadWell.class, "NomadWell", 5478114, 15391151);
		genStructureInfo(1204, (LOTRVillageGen) new LOTRVillageGenHaradNomad(LOTRBiome.nearHaradSemiDesert, 1.0F), "NomadVillageSmall", 16775927, 8345150, new IVillageProperties<LOTRVillageGenHaradNomad.Instance>() {
			public void apply(LOTRVillageGenHaradNomad.Instance instance) {
				instance.villageType = LOTRVillageGenHaradNomad.VillageType.SMALL;
			}
		});
		genStructureInfo(1205, (LOTRVillageGen) new LOTRVillageGenHaradNomad(LOTRBiome.nearHaradSemiDesert, 1.0F), "NomadVillageBig", 16775927, 8345150, new IVillageProperties<LOTRVillageGenHaradNomad.Instance>() {
			public void apply(LOTRVillageGenHaradNomad.Instance instance) {
				instance.villageType = LOTRVillageGenHaradNomad.VillageType.BIG;
			}
		});
		genStructureInfo(1206, (Class) LOTRWorldGenNomadBazaarTent.class, "NomadBazaarTent", 16775927, 8345150);
		genStructureInfo(1250, (Class) LOTRWorldGenGulfWarCamp.class, "GulfWarCamp", 12849937, 4275226);
		genStructureInfo(1251, (Class) LOTRWorldGenGulfHouse.class, "GulfHouse", 9335899, 5654831);
		genStructureInfo(1252, (Class) LOTRWorldGenGulfAltar.class, "GulfAltar", 12849937, 4275226);
		genStructureInfo(1253, (Class) LOTRWorldGenGulfSmithy.class, "GulfSmithy", 9335899, 5654831);
		genStructureInfo(1254, (Class) LOTRWorldGenGulfBazaar.class, "GulfBazaar", 9335899, 5654831);
		genStructureInfo(1255, (Class) LOTRWorldGenGulfTotem.class, "GulfTotem", 12849937, 4275226);
		genStructureInfo(1256, (Class) LOTRWorldGenGulfPyramid.class, "GulfPyramid", 15721151, 12873038);
		genStructureInfo(1257, (Class) LOTRWorldGenGulfFarm.class, "GulfFarm", 9547581, 12849937);
		genStructureInfo(1258, (Class) LOTRWorldGenGulfTower.class, "GulfTower", 12849937, 4275226);
		genStructureInfo(1259, (Class) LOTRWorldGenGulfTavern.class, "GulfTavern", 9335899, 5654831);
		genStructureInfo(1260, (Class) LOTRWorldGenGulfVillageSign.class, "GulfVillageSign", 14737111, 16250349);
		genStructureInfo(1261, (Class) LOTRWorldGenGulfVillageLight.class, "GulfVillageLight", 14737111, 16250349);
		genStructureInfo(1262, (LOTRVillageGen) new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0F), "GulfVillage", 9335899, 5654831, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.VILLAGE;
			}
		});
		genStructureInfo(1263, (Class) LOTRWorldGenGulfPasture.class, "GulfPasture", 9547581, 12849937);
		genStructureInfo(1264, (LOTRVillageGen) new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0F), "GulfTown", 15721151, 12873038, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.TOWN;
			}
		});
		genStructureInfo(1265, (LOTRVillageGen) new LOTRVillageGenGulfHarad(LOTRBiome.gulfHarad, 1.0F), "GulfFortVillage", 12849937, 4275226, new IVillageProperties<LOTRVillageGenGulfHarad.Instance>() {
			public void apply(LOTRVillageGenGulfHarad.Instance instance) {
				instance.villageType = LOTRVillageGenGulfHarad.VillageType.FORT;
			}
		});
		genStructureInfo(1500, (Class) LOTRWorldGenMoredainHutVillage.class, "MoredainHutVillage", 8873812, 12891279);
		genStructureInfo(1501, (Class) LOTRWorldGenMoredainHutChieftain.class, "MoredainHutChieftain", 8873812, 12891279);
		genStructureInfo(1502, (Class) LOTRWorldGenMoredainHutTrader.class, "MoredainHutTrader", 8873812, 12891279);
		genStructureInfo(1503, (Class) LOTRWorldGenMoredainHutHunter.class, "MoredainHutHunter", 8873812, 12891279);
		genStructureInfo(1550, (Class) LOTRWorldGenTauredainPyramid.class, "TauredainPyramid", 6513746, 4803646);
		genStructureInfo(1551, (Class) LOTRWorldGenTauredainHouseSimple.class, "TauredainHouseSimple", 4796447, 8021303);
		genStructureInfo(1552, (Class) LOTRWorldGenTauredainHouseStilts.class, "TauredainHouseStilts", 4796447, 8021303);
		genStructureInfo(1553, (Class) LOTRWorldGenTauredainWatchtower.class, "TauredainWatchtower", 4796447, 8021303);
		genStructureInfo(1554, (Class) LOTRWorldGenTauredainHouseLarge.class, "TauredainHouseLarge", 4796447, 14593598);
		genStructureInfo(1555, (Class) LOTRWorldGenTauredainChieftainPyramid.class, "TauredainChieftainPyramid", 6513746, 4803646);
		genStructureInfo(1556, (Class) LOTRWorldGenTauredainVillageTree.class, "TauredainVillageTree", 9285414, 4796447);
		genStructureInfo(1557, (Class) LOTRWorldGenTauredainVillageFarm.class, "TauredainVillageFarm", 9285414, 4796447);
		genStructureInfo(1558, (LOTRVillageGen) new LOTRVillageGenTauredain(LOTRBiome.tauredainClearing, 1.0F), "TauredainVillage", 6840658, 5979708, new IVillageProperties<LOTRVillageGenTauredain.Instance>() {
			public void apply(LOTRVillageGenTauredain.Instance instance) {
			}
		});
		genStructureInfo(1559, (Class) LOTRWorldGenTauredainSmithy.class, "TauredainSmithy", 4796447, 8021303);
		genStructureInfo(1700, (Class) LOTRWorldGenHalfTrollHouse.class, "HalfTrollHouse", 10058344, 5325111);
		genStructureInfo(1701, (Class) LOTRWorldGenHalfTrollWarlordHouse.class, "HalfTrollWarlordHouse", 10058344, 5325111);
		genStructureInfo(1995, (Class) LOTRWorldGenRedDwarvenTower.class, "RedDwarvenTower", 10713966, 15357473);
		genStructureInfo(1996, (Class) LOTRWorldGenRedMountainsSmithy.class, "RedMountainsSmithy", 10713966, 15357473);
		genStructureInfo(1997, (Class) LOTRWorldGenBlackUrukFort2.class, "AngbandCamp", 10713966, 15357473);
		genStructureInfo(1999, (Class) LOTRWorldGenAngbandTent.class, "AngbandTent", 10713966, 15357473);
		genStructureInfo(2000, (Class) LOTRWorldGenAngbandForge.class, "AngbandForge", 10713966, 15357473);
		genStructureInfo(2001, (Class) LOTRWorldGenEreborTower.class, "EreborTower", 10713966, 15357473);
		genStructureInfo(2002, (Class) LOTRWorldGenEreborSmithy.class, "EreborSmithy", 10713966, 15357473);
		genStructureInfo(2003, (Class) LOTRWorldGenRedMountainsHouseBlacklock.class, "RedMountainsHouse", 10713966, 15357473);
		genStructureInfo(2004, (Class) LOTRWorldGenRedDwarvenTower2.class, "OrocarniTower", 10713966, 15357473);
		genStructureInfo(2006, (Class) LOTRWorldGenRedMountainsHouseStiffbeard.class, "OrocarniHouse", 10713966, 15357473);
		genStructureInfo(2007, (Class) LOTRWorldGenWindMountainsHouse.class, "WindHouse", 7557508, 7108730);
		genStructureInfo(2008, (Class) LOTRWorldGenWindDwarvenTower.class, "WindTower", 7557508, 7108730);
		genStructureInfo(2009, (Class) LOTRWorldGenUtumnoSpiderPit.class, "UtumnoSpiderPit", 10713966, 15357473);
		genStructureInfo(2010, (Class) LOTRWorldGenAngbandTower.class, "UtumnoTower", 10713966, 15357473);
		genStructureInfo(2011, (Class) LOTRWorldGenUtumnoWargPit.class, "UtumnoWargPit", 10713966, 15357473);
		genStructureInfo(2012, (Class) LOTRWorldGenDwarvenMineEntrance2.class, "RedDwarfMine", 10713966, 15357473);
		genStructureInfo(2013, (Class) LOTRWorldGenRedDwarvenFort.class, "TowerStonefoot", 10713966, 15357473);
		genStructureInfo(2015, (Class) LOTRWorldGenRuinedRedDwarvenTower.class, "RedDwarvenTowerStiffBeards", 10713966, 15357473);
		genStructureInfo(2016, (Class) LOTRWorldGenDurmethForge.class, "DurmethForge", 11837263, 11449194);
		genStructureInfo(2017, (Class) LOTRWorldGenDurmethTent.class, "DurmethTent", 11837263, 11449194);
		genStructureInfo(2018, (Class) LOTRWorldGenDurmethCamp.class, "DurmethCamp", 11837263, 11449194);
		genStructureInfo(2019, (Class) LOTRWorldGenRuinedRedDwarvenTower2.class, "RedDwarvenTowerStoneFoots", 7482144, 16772268);
		genStructureInfo(2020, (Class) LOTRWorldGenRuinedMoriaDwarvenTower.class, "RuinedMoriaDwarvenTower", 11837263, 11449194);
		genStructureInfo(2022, (Class) LOTRWorldGenAngbandFort.class, "AngbandFort", 10713966, 15357473);
		genStructureInfo(2023, (Class) LOTRWorldGenRAngbandBarrow.class, "AngbandBarrow", 10713966, 15357473);
		genStructureInfo(2024, (Class) LOTRWorldGenAngbandTower2.class, "UtumnoTowerRuined", 10713966, 15357473);
		genStructureInfo(2025, (Class) LOTRWorldGenDwarvenMineEntranceRuined2.class, "DwarvenMineEntranceRuined2", 10713966, 15357473);
		genStructureInfo(2026, (Class) LOTRWorldGenRedMountainsHouseStonefoot.class, "OrocarniHouse", 10713966, 15357473);
		genStructureInfo(2027, (Class) LOTRWorldGenRedMountainsHouseWickedDwarf.class, "RuinedRedDwarfHouse", 10713966, 15357473);
		genStructureInfo(2028, (Class) LOTRWorldGenRedMountainsSmithy2.class, "RuinedRedDwarfSmithy", 10713966, 15357473);
		genStructureInfo(2029, (Class) LOTRWorldGenRuinedRedDwarvenTower3.class, "RuinedRedDwarvenTower3", 11837263, 11449194);
		genStructureInfo(2031, (Class) LOTRWorldGenUrukWargPit.class, "UtumnoPit", 3025185, 1972756);
		genStructureInfo(2032, (Class) LOTRWorldGenDwarvenMineEntrance3.class, "WindDwarfMine", 10713966, 15357473);
		genStructureInfo(2033, (Class) LOTRWorldGenDwarvenMineEntranceRuined3.class, "WindDwarfMineRuined", 10713966, 15357473);
		genStructureInfo(2034, (Class) LOTRWorldGenAvariElfHouse.class, "AvariElfHouse", 10056783, 16764574);
		genStructureInfo(2035, (Class) LOTRWorldGenAvariElfTower.class, "AvariElfTower", 10056783, 16764574);
		genStructureInfo(2036, (Class) LOTRWorldGenRuinedAvariElfTower.class, "RuinedAvariElfTower", 10056783, 16764574);
		genStructureInfo(2037, (Class) LOTRWorldGenAvariElvenForge.class, "AvariElvenForge", 10056783, 16764574);
		genStructureInfo(2038, (Class) LOTRWorldGenMoredainMercTent2.class, "DarkElfMercTent", 10713966, 15357473);
		genStructureInfo(2039, (Class) LOTRWorldGenMoredainMercCamp2.class, "DarkElfMercCamp", 10713966, 15357473);
		genStructureInfo(2040, (Class) LOTRWorldGenBlueMountainsMineEntrance.class, "BlueMountainsEntrance", 10397380, 7633815);
	}

	private static void genEntityInfo(Class<? extends Entity> entityClass, String name, int id) {
		genEntityInfo(entityClass, name, id, 80, 3, true);
	}

	private static void genEntityInfo(Class<? extends Entity> entityClass, String name, int id, int egg1, int egg2) {
		genEntityInfo(entityClass, name, id, 80, 3, true);
	}

	private static void genEntityInfo(Class<? extends Entity> entityClass, String name, int id, int updateRange, int updateFreq, boolean sendVelocityUpdates) {
		ENTITY_CLASS_TO_NAME.put(entityClass, name);
		WikiGenerator.ENTITY_CLASS_TO_ENTITY.put(entityClass, ReflectionHelper.newEntity(entityClass, world));
		WikiGenerator.ENTITY_CLASSES.add(entityClass);
	}

	private static void genStructureInfo(Class<? extends WorldGenerator> clazz, String name) {
		STRUCTURE_CLASS_TO_NAME.put(clazz, name);
	}

	private static void genStructureInfo(int i, Class<? extends WorldGenerator> clazz, String name, int egg1, int egg2) {
		genStructureInfo(clazz, name);
	}

	private static void genStructureInfo(int i, LOTRVillageGen clazz, String name, int j, int k, IVillageProperties<?> iVillageProperties) {
		SETTLEMENT_CLASS_TO_NAMES.computeIfAbsent(clazz.getClass(), s -> new HashSet<>());
		SETTLEMENT_CLASS_TO_NAMES.get(clazz.getClass()).add(name);
	}

	private interface IVillageProperties<V> {
		void apply(V var1);
	}
}