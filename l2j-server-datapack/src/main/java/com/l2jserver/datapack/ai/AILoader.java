/*
 * Copyright © 2004-2026 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.datapack.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.ai.fantasy_isle.HandysBlockCheckerEvent;
import com.l2jserver.datapack.ai.fantasy_isle.MC_Show;
import com.l2jserver.datapack.ai.group_template.AltarsOfSacrifice;
import com.l2jserver.datapack.ai.group_template.BeastFarm;
import com.l2jserver.datapack.ai.group_template.CorpseOfDeadman;
import com.l2jserver.datapack.ai.group_template.DenOfEvil;
import com.l2jserver.datapack.ai.group_template.DragonValley;
import com.l2jserver.datapack.ai.group_template.FairyTrees;
import com.l2jserver.datapack.ai.group_template.FeedableBeasts;
import com.l2jserver.datapack.ai.group_template.FleeMonsters;
import com.l2jserver.datapack.ai.group_template.FrozenLabyrinth;
import com.l2jserver.datapack.ai.group_template.GiantsCave;
import com.l2jserver.datapack.ai.group_template.HotSprings;
import com.l2jserver.datapack.ai.group_template.IsleOfPrayer;
import com.l2jserver.datapack.ai.group_template.LairOfAntharas;
import com.l2jserver.datapack.ai.group_template.MinionSpawnManager;
import com.l2jserver.datapack.ai.group_template.MonasteryOfSilence;
import com.l2jserver.datapack.ai.group_template.NonLethalableNpcs;
import com.l2jserver.datapack.ai.group_template.NonTalkingNpcs;
import com.l2jserver.datapack.ai.group_template.PavelArchaic;
import com.l2jserver.datapack.ai.group_template.PlainsOfDion;
import com.l2jserver.datapack.ai.group_template.PlainsOfLizardman;
import com.l2jserver.datapack.ai.group_template.PolymorphingAngel;
import com.l2jserver.datapack.ai.group_template.PolymorphingOnAttack;
import com.l2jserver.datapack.ai.group_template.PrimevalIsle;
import com.l2jserver.datapack.ai.group_template.PrisonGuards;
import com.l2jserver.datapack.ai.group_template.RaidBossCancel;
import com.l2jserver.datapack.ai.group_template.RandomSpawn;
import com.l2jserver.datapack.ai.group_template.RangeGuard;
import com.l2jserver.datapack.ai.group_template.Remnants;
import com.l2jserver.datapack.ai.group_template.Sandstorms;
import com.l2jserver.datapack.ai.group_template.SeeThroughSilentMove;
import com.l2jserver.datapack.ai.group_template.SelMahumDrill;
import com.l2jserver.datapack.ai.group_template.SelMahumSquad;
import com.l2jserver.datapack.ai.group_template.SilentValley;
import com.l2jserver.datapack.ai.group_template.StakatoNest;
import com.l2jserver.datapack.ai.group_template.SummonPc;
import com.l2jserver.datapack.ai.group_template.TreasureChest;
import com.l2jserver.datapack.ai.group_template.TurekOrcs;
import com.l2jserver.datapack.ai.group_template.VarkaKetra;
import com.l2jserver.datapack.ai.group_template.WarriorFishingBlock;
import com.l2jserver.datapack.ai.individual.Anais;
import com.l2jserver.datapack.ai.individual.Ballista;
import com.l2jserver.datapack.ai.individual.Beleth;
import com.l2jserver.datapack.ai.individual.BlackdaggerWing;
import com.l2jserver.datapack.ai.individual.BleedingFly;
import com.l2jserver.datapack.ai.individual.BloodyBerserker;
import com.l2jserver.datapack.ai.individual.BloodyKarik;
import com.l2jserver.datapack.ai.individual.BloodyKarinness;
import com.l2jserver.datapack.ai.individual.Core;
import com.l2jserver.datapack.ai.individual.CrimsonHatuOtis;
import com.l2jserver.datapack.ai.individual.DarkWaterDragon;
import com.l2jserver.datapack.ai.individual.DivineBeast;
import com.l2jserver.datapack.ai.individual.DrakosWarrior;
import com.l2jserver.datapack.ai.individual.DustRider;
import com.l2jserver.datapack.ai.individual.EmeraldHorn;
import com.l2jserver.datapack.ai.individual.Epidos;
import com.l2jserver.datapack.ai.individual.EvasGiftBox;
import com.l2jserver.datapack.ai.individual.FrightenedRagnaOrc;
import com.l2jserver.datapack.ai.individual.GiganticGolem;
import com.l2jserver.datapack.ai.individual.Gordon;
import com.l2jserver.datapack.ai.individual.GraveRobbers;
import com.l2jserver.datapack.ai.individual.Knoriks;
import com.l2jserver.datapack.ai.individual.MuscleBomber;
import com.l2jserver.datapack.ai.individual.NecromancerOfTheValley;
import com.l2jserver.datapack.ai.individual.Orfen;
import com.l2jserver.datapack.ai.individual.QueenAnt;
import com.l2jserver.datapack.ai.individual.QueenShyeed;
import com.l2jserver.datapack.ai.individual.RagnaOrcCommander;
import com.l2jserver.datapack.ai.individual.RagnaOrcHero;
import com.l2jserver.datapack.ai.individual.RagnaOrcSeer;
import com.l2jserver.datapack.ai.individual.ShadowSummoner;
import com.l2jserver.datapack.ai.individual.SinEater;
import com.l2jserver.datapack.ai.individual.SinWardens;
import com.l2jserver.datapack.ai.individual.Valakas;
import com.l2jserver.datapack.ai.individual.Antharas.Antharas;
import com.l2jserver.datapack.ai.individual.Baium.Baium;
import com.l2jserver.datapack.ai.individual.Sailren.Sailren;
import com.l2jserver.datapack.ai.individual.Venom.Venom;
import com.l2jserver.datapack.ai.npc.AdventureGuildsman.AdventureGuildsman;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.AzitDoorman.AzitDoorman;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.DoormanOfFortress.DoormanOfFortress;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.DoormanOfHell.DoormanOfHell;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.DoormanOfRainbow.DoormanOfRainbow;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.FarmDoorman.FarmDoorman;
import com.l2jserver.datapack.ai.npc.AgitDoorkeeper.PartisanDoormanHarkel.PartisanDoormanHarkel;
import com.l2jserver.datapack.ai.npc.Alarm.Alarm;
import com.l2jserver.datapack.ai.npc.Alexandria.Alexandria;
import com.l2jserver.datapack.ai.npc.ArenaManager.ArenaManager;
import com.l2jserver.datapack.ai.npc.AvantGarde.AvantGarde;
import com.l2jserver.datapack.ai.npc.BlackJudge.BlackJudge;
import com.l2jserver.datapack.ai.npc.BlackMarketeerOfMammon.BlackMarketeerOfMammon;
import com.l2jserver.datapack.ai.npc.BlacksmithOfMammon.BlacksmithOfMammon;
import com.l2jserver.datapack.ai.npc.CastleAmbassador.CastleAmbassador;
import com.l2jserver.datapack.ai.npc.CastleBlacksmith.CastleBlacksmith;
import com.l2jserver.datapack.ai.npc.CastleCourtMagician.CastleCourtMagician;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager1.ManorManager1;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager10.ManorManager10;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager11.ManorManager11;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager12.ManorManager12;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager13.ManorManager13;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager14.ManorManager14;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager2.ManorManager2;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager3.ManorManager3;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager4.ManorManager4;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager5.ManorManager5;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager6.ManorManager6;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager7.ManorManager7;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager8.ManorManager8;
import com.l2jserver.datapack.ai.npc.CastleMerchant.ManorManager9.ManorManager9;
import com.l2jserver.datapack.ai.npc.CastleSiegeManager.CastleSiegeManager;
import com.l2jserver.datapack.ai.npc.CastleTeleporter.CastleTeleporter;
import com.l2jserver.datapack.ai.npc.CastleWarehouse.CastleWarehouse;
import com.l2jserver.datapack.ai.npc.Chamberlain.Alfred.Alfred;
import com.l2jserver.datapack.ai.npc.Chamberlain.August.August;
import com.l2jserver.datapack.ai.npc.Chamberlain.Brasseur.Brasseur;
import com.l2jserver.datapack.ai.npc.Chamberlain.Crosby.Crosby;
import com.l2jserver.datapack.ai.npc.Chamberlain.Frederick.Frederick;
import com.l2jserver.datapack.ai.npc.Chamberlain.Logan.Logan;
import com.l2jserver.datapack.ai.npc.Chamberlain.Neurath.Neurath;
import com.l2jserver.datapack.ai.npc.Chamberlain.Saius.Saius;
import com.l2jserver.datapack.ai.npc.Chamberlain.Saul.Saul;
import com.l2jserver.datapack.ai.npc.Chandra.Chandra;
import com.l2jserver.datapack.ai.npc.ClassMaster.ClassMaster;
import com.l2jserver.datapack.ai.npc.CleftGateRed.CleftGateRed;
import com.l2jserver.datapack.ai.npc.Custodian.Adrienne.Adrienne;
import com.l2jserver.datapack.ai.npc.Custodian.Aida.Aida;
import com.l2jserver.datapack.ai.npc.Custodian.Albert.Albert;
import com.l2jserver.datapack.ai.npc.Custodian.Bianca.Bianca;
import com.l2jserver.datapack.ai.npc.Custodian.Billy.Billy;
import com.l2jserver.datapack.ai.npc.Custodian.Black.Black;
import com.l2jserver.datapack.ai.npc.Custodian.Boyer.Boyer;
import com.l2jserver.datapack.ai.npc.Custodian.Branhilde.Branhilde;
import com.l2jserver.datapack.ai.npc.Custodian.Bremmer.Bremmer;
import com.l2jserver.datapack.ai.npc.Custodian.Calis.Calis;
import com.l2jserver.datapack.ai.npc.Custodian.Carey.Carey;
import com.l2jserver.datapack.ai.npc.Custodian.Carrel.Carrel;
import com.l2jserver.datapack.ai.npc.Custodian.Cerna.Cerna;
import com.l2jserver.datapack.ai.npc.Custodian.Crissy.Crissy;
import com.l2jserver.datapack.ai.npc.Custodian.Dianne.Dianne;
import com.l2jserver.datapack.ai.npc.Custodian.Dillon.Dillon;
import com.l2jserver.datapack.ai.npc.Custodian.Dimaggio.Dimaggio;
import com.l2jserver.datapack.ai.npc.Custodian.Emma.Emma;
import com.l2jserver.datapack.ai.npc.Custodian.Flynn.Flynn;
import com.l2jserver.datapack.ai.npc.Custodian.Gladys.Gladys;
import com.l2jserver.datapack.ai.npc.Custodian.Helga.Helga;
import com.l2jserver.datapack.ai.npc.Custodian.Horner.Horner;
import com.l2jserver.datapack.ai.npc.Custodian.Jacques.Jacques;
import com.l2jserver.datapack.ai.npc.Custodian.Jimmy.Jimmy;
import com.l2jserver.datapack.ai.npc.Custodian.Karuto.Karuto;
import com.l2jserver.datapack.ai.npc.Custodian.Korgen.Korgen;
import com.l2jserver.datapack.ai.npc.Custodian.Michael.Michael;
import com.l2jserver.datapack.ai.npc.Custodian.Milicent.Milicent;
import com.l2jserver.datapack.ai.npc.Custodian.Pattie.Pattie;
import com.l2jserver.datapack.ai.npc.Custodian.Regina.Regina;
import com.l2jserver.datapack.ai.npc.Custodian.Ron.Ron;
import com.l2jserver.datapack.ai.npc.Custodian.Ronald.Ronald;
import com.l2jserver.datapack.ai.npc.Custodian.Ruben.Ruben;
import com.l2jserver.datapack.ai.npc.Custodian.Seth.Seth;
import com.l2jserver.datapack.ai.npc.Custodian.Stanley.Stanley;
import com.l2jserver.datapack.ai.npc.Custodian.Tim.Tim;
import com.l2jserver.datapack.ai.npc.Custodian.Wayne.Wayne;
import com.l2jserver.datapack.ai.npc.Custodian.Winker.Winker;
import com.l2jserver.datapack.ai.npc.DimensionKeeper.DimensionKeeper;
import com.l2jserver.datapack.ai.npc.Doorkeeper.InnerDoorman.InnerDoorman;
import com.l2jserver.datapack.ai.npc.Doorkeeper.OutterDoorman.OutterDoorman;
import com.l2jserver.datapack.ai.npc.Dorian.Dorian;
import com.l2jserver.datapack.ai.npc.DragonVortex.DragonVortex;
import com.l2jserver.datapack.ai.npc.EchoCrystals.EchoCrystals;
import com.l2jserver.datapack.ai.npc.Elmina.Elmina;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.ForgeOfTheGods;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.Rooney;
import com.l2jserver.datapack.ai.npc.ForgeOfTheGods.TarBeetle;
import com.l2jserver.datapack.ai.npc.FortressArcherCaptain.FortressArcherCaptain;
import com.l2jserver.datapack.ai.npc.FortressDoorkeeper.FortressDoorkeeper;
import com.l2jserver.datapack.ai.npc.FortressSiegeManager.FortressSiegeManager;
import com.l2jserver.datapack.ai.npc.FortressSteward.AaruFortSteward.AaruFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.AntharasFortSteward.AntharasFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.ArchaicFortSteward.ArchaicFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.BayouFortSteward.BayouFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.BorderlandFortSteward.BorderlandFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.CloudMountainFortSteward.CloudMountainFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.DemonFortSteward.DemonFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.DragonspineFortSteward.DragonspineFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.FloranFortSteward.FloranFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.HiveFortSteward.HiveFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.HuntersFortSteward.HuntersFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.IvoryFortSteward.IvoryFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.MonasticFortSteward.MonasticFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.NarsellFortSteward.NarsellFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.ShantyFortSteward.ShantyFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.SouthernFortSteward.SouthernFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.SwampFortSteward.SwampFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.TanorFortSteward.TanorFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.ValleyFortSteward.ValleyFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.WesternFortSteward.WesternFortSteward;
import com.l2jserver.datapack.ai.npc.FortressSteward.WhiteSandsFortSteward.WhiteSandsFortSteward;
import com.l2jserver.datapack.ai.npc.FreyasSteward.FreyasSteward;
import com.l2jserver.datapack.ai.npc.GiantDDwarf.GiantDDwarf;
import com.l2jserver.datapack.ai.npc.GiantQDwarf.GiantQDwarf;
import com.l2jserver.datapack.ai.npc.GuideUndeath.GuideUndeath;
import com.l2jserver.datapack.ai.npc.HandyBlockGuide.HandyBlockGuide;
import com.l2jserver.datapack.ai.npc.Janitor.Janitor;
import com.l2jserver.datapack.ai.npc.Jinia.Jinia;
import com.l2jserver.datapack.ai.npc.Katenar.Katenar;
import com.l2jserver.datapack.ai.npc.KetraOrcSupport.KetraOrcSupport;
import com.l2jserver.datapack.ai.npc.Kier.Kier;
import com.l2jserver.datapack.ai.npc.Kroon.Kroon;
import com.l2jserver.datapack.ai.npc.MercenaryCaptain.MercenaryCaptain;
import com.l2jserver.datapack.ai.npc.Merchant.Abercrombie.Abercrombie;
import com.l2jserver.datapack.ai.npc.Merchant.Acellopy.Acellopy;
import com.l2jserver.datapack.ai.npc.Merchant.Adrian.Adrian;
import com.l2jserver.datapack.ai.npc.Merchant.Alexis.Alexis;
import com.l2jserver.datapack.ai.npc.Merchant.Alisha.Alisha;
import com.l2jserver.datapack.ai.npc.Merchant.Altar.Altar;
import com.l2jserver.datapack.ai.npc.Merchant.Anton.Anton;
import com.l2jserver.datapack.ai.npc.Merchant.Antonio.Antonio;
import com.l2jserver.datapack.ai.npc.Merchant.Aren.Aren;
import com.l2jserver.datapack.ai.npc.Merchant.Ariel.Ariel;
import com.l2jserver.datapack.ai.npc.Merchant.Arodin.Arodin;
import com.l2jserver.datapack.ai.npc.Merchant.Arvid.Arvid;
import com.l2jserver.datapack.ai.npc.Merchant.Asama.Asama;
import com.l2jserver.datapack.ai.npc.Merchant.Astrid.Astrid;
import com.l2jserver.datapack.ai.npc.Merchant.Atan.Atan;
import com.l2jserver.datapack.ai.npc.Merchant.Auzendurof.Auzendurof;
import com.l2jserver.datapack.ai.npc.Merchant.Bandor.Bandor;
import com.l2jserver.datapack.ai.npc.Merchant.Berynel.Berynel;
import com.l2jserver.datapack.ai.npc.Merchant.Borodin.Borodin;
import com.l2jserver.datapack.ai.npc.Merchant.Burns.Burns;
import com.l2jserver.datapack.ai.npc.Merchant.Candice.Candice;
import com.l2jserver.datapack.ai.npc.Merchant.Carson.Carson;
import com.l2jserver.datapack.ai.npc.Merchant.Cel.Cel;
import com.l2jserver.datapack.ai.npc.Merchant.Cema.Cema;
import com.l2jserver.datapack.ai.npc.Merchant.Chali.Chali;
import com.l2jserver.datapack.ai.npc.Merchant.Clancy.Clancy;
import com.l2jserver.datapack.ai.npc.Merchant.Cona.Cona;
import com.l2jserver.datapack.ai.npc.Merchant.Cullinas.Cullinas;
import com.l2jserver.datapack.ai.npc.Merchant.Daeger.Daeger;
import com.l2jserver.datapack.ai.npc.Merchant.Daeronees.Daeronees;
import com.l2jserver.datapack.ai.npc.Merchant.Dani.Dani;
import com.l2jserver.datapack.ai.npc.Merchant.Denkus.Denkus;
import com.l2jserver.datapack.ai.npc.Merchant.Denver.Denver;
import com.l2jserver.datapack.ai.npc.Merchant.Desian.Desian;
import com.l2jserver.datapack.ai.npc.Merchant.Dindin.Dindin;
import com.l2jserver.datapack.ai.npc.Merchant.Diyabu.Diyabu;
import com.l2jserver.datapack.ai.npc.Merchant.Dolphren.Dolphren;
import com.l2jserver.datapack.ai.npc.Merchant.Donai.Donai;
import com.l2jserver.datapack.ai.npc.Merchant.Drawin.Drawin;
import com.l2jserver.datapack.ai.npc.Merchant.Drumond.Drumond;
import com.l2jserver.datapack.ai.npc.Merchant.Edroc.Edroc;
import com.l2jserver.datapack.ai.npc.Merchant.Eldon.Eldon;
import com.l2jserver.datapack.ai.npc.Merchant.Elena.Elena;
import com.l2jserver.datapack.ai.npc.Merchant.Elliany.Elliany;
import com.l2jserver.datapack.ai.npc.Merchant.Enverun.Enverun;
import com.l2jserver.datapack.ai.npc.Merchant.Erinu.Erinu;
import com.l2jserver.datapack.ai.npc.Merchant.Espen.Espen;
import com.l2jserver.datapack.ai.npc.Merchant.FarmFeedSeller.FarmFeedSeller;
import com.l2jserver.datapack.ai.npc.Merchant.Felton.Felton;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Batidae.Batidae;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Berix.Berix;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Bleaker.Bleaker;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Brang.Brang;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Cyano.Cyano;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Eindarkner.Eindarkner;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Galba.Galba;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Hilgendorf.Hilgendorf;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Hufs.Hufs;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Klaw.Klaw;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Klufe.Klufe;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Lanosco.Lanosco;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Linneaus.Linneaus;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Litulon.Litulon;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Mishini.Mishini;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Monakan.Monakan;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Ofulle.Ofulle;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Ogord.Ogord;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Pamfus.Pamfus;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Perelin.Perelin;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Platis.Platis;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Ropfi.Ropfi;
import com.l2jserver.datapack.ai.npc.Merchant.Fisher.Willeri.Willeri;
import com.l2jserver.datapack.ai.npc.Merchant.Fundin.Fundin;
import com.l2jserver.datapack.ai.npc.Merchant.Gaiman.Gaiman;
import com.l2jserver.datapack.ai.npc.Merchant.Galicbredo.Galicbredo;
import com.l2jserver.datapack.ai.npc.Merchant.Galladuchi.Galladuchi;
import com.l2jserver.datapack.ai.npc.Merchant.Garette.Garette;
import com.l2jserver.datapack.ai.npc.Merchant.Garita.Garita;
import com.l2jserver.datapack.ai.npc.Merchant.Gentler.Gentler;
import com.l2jserver.datapack.ai.npc.Merchant.Giordo.Giordo;
import com.l2jserver.datapack.ai.npc.Merchant.Gompers.Gompers;
import com.l2jserver.datapack.ai.npc.Merchant.Grabner.Grabner;
import com.l2jserver.datapack.ai.npc.Merchant.Green.Green;
import com.l2jserver.datapack.ai.npc.Merchant.Greenspan.Greenspan;
import com.l2jserver.datapack.ai.npc.Merchant.Greta.Greta;
import com.l2jserver.datapack.ai.npc.Merchant.Groot.Groot;
import com.l2jserver.datapack.ai.npc.Merchant.Hakran.Hakran;
import com.l2jserver.datapack.ai.npc.Merchant.Hallypia.Hallypia;
import com.l2jserver.datapack.ai.npc.Merchant.Hans.Hans;
import com.l2jserver.datapack.ai.npc.Merchant.Harmony.Harmony;
import com.l2jserver.datapack.ai.npc.Merchant.Hedinger.Hedinger;
import com.l2jserver.datapack.ai.npc.Merchant.Helmut.Helmut;
import com.l2jserver.datapack.ai.npc.Merchant.Helvetia.Helvetia;
import com.l2jserver.datapack.ai.npc.Merchant.Hittchi.Hittchi;
import com.l2jserver.datapack.ai.npc.Merchant.Holly.Holly;
import com.l2jserver.datapack.ai.npc.Merchant.Hullia.Hullia;
import com.l2jserver.datapack.ai.npc.Merchant.Ilia.Ilia;
import com.l2jserver.datapack.ai.npc.Merchant.Iria.Iria;
import com.l2jserver.datapack.ai.npc.Merchant.Iz.Iz;
import com.l2jserver.datapack.ai.npc.Merchant.Jackson.Jackson;
import com.l2jserver.datapack.ai.npc.Merchant.Jaka.Jaka;
import com.l2jserver.datapack.ai.npc.Merchant.Jakaron.Jakaron;
import com.l2jserver.datapack.ai.npc.Merchant.Janne.Janne;
import com.l2jserver.datapack.ai.npc.Merchant.Jouge.Jouge;
import com.l2jserver.datapack.ai.npc.Merchant.Judith.Judith;
import com.l2jserver.datapack.ai.npc.Merchant.Jumara.Jumara;
import com.l2jserver.datapack.ai.npc.Merchant.Karai.Karai;
import com.l2jserver.datapack.ai.npc.Merchant.Katrine.Katrine;
import com.l2jserver.datapack.ai.npc.Merchant.Kc.Kc;
import com.l2jserver.datapack.ai.npc.Merchant.Kendrew.Kendrew;
import com.l2jserver.datapack.ai.npc.Merchant.Kiki.Kiki;
import com.l2jserver.datapack.ai.npc.Merchant.Kitzka.Kitzka;
import com.l2jserver.datapack.ai.npc.Merchant.Koram.Koram;
import com.l2jserver.datapack.ai.npc.Merchant.Kunai.Kunai;
import com.l2jserver.datapack.ai.npc.Merchant.Lanna.Lanna;
import com.l2jserver.datapack.ai.npc.Merchant.Lara.Lara;
import com.l2jserver.datapack.ai.npc.Merchant.Lars.Lars;
import com.l2jserver.datapack.ai.npc.Merchant.Lector.Lector;
import com.l2jserver.datapack.ai.npc.Merchant.Leon.Leon;
import com.l2jserver.datapack.ai.npc.Merchant.Lepidus.Lepidus;
import com.l2jserver.datapack.ai.npc.Merchant.Liesel.Liesel;
import com.l2jserver.datapack.ai.npc.Merchant.Lorel.Lorel;
import com.l2jserver.datapack.ai.npc.Merchant.Lorenzo.Lorenzo;
import com.l2jserver.datapack.ai.npc.Merchant.Lowell.Lowell;
import com.l2jserver.datapack.ai.npc.Merchant.Luka.Luka;
import com.l2jserver.datapack.ai.npc.Merchant.Lumen.Lumen;
import com.l2jserver.datapack.ai.npc.Merchant.Lyann.Lyann;
import com.l2jserver.datapack.ai.npc.Merchant.Lynn.Lynn;
import com.l2jserver.datapack.ai.npc.Merchant.Mailland.Mailland;
import com.l2jserver.datapack.ai.npc.Merchant.MerchantGolem.MerchantGolem;
import com.l2jserver.datapack.ai.npc.Merchant.Metar.Metar;
import com.l2jserver.datapack.ai.npc.Merchant.Miflen.Miflen;
import com.l2jserver.datapack.ai.npc.Merchant.Migel.Migel;
import com.l2jserver.datapack.ai.npc.Merchant.Mina.Mina;
import com.l2jserver.datapack.ai.npc.Merchant.Mion.Mion;
import com.l2jserver.datapack.ai.npc.Merchant.Morrison.Morrison;
import com.l2jserver.datapack.ai.npc.Merchant.Natasha.Natasha;
import com.l2jserver.datapack.ai.npc.Merchant.Neagel.Neagel;
import com.l2jserver.datapack.ai.npc.Merchant.Nedy.Nedy;
import com.l2jserver.datapack.ai.npc.Merchant.Nestle.Nestle;
import com.l2jserver.datapack.ai.npc.Merchant.Nils.Nils;
import com.l2jserver.datapack.ai.npc.Merchant.OfficerTolonis.OfficerTolonis;
import com.l2jserver.datapack.ai.npc.Merchant.Onyx.Onyx;
import com.l2jserver.datapack.ai.npc.Merchant.Owaking.Owaking;
import com.l2jserver.datapack.ai.npc.Merchant.Paint.Paint;
import com.l2jserver.datapack.ai.npc.Merchant.Pano.Pano;
import com.l2jserver.datapack.ai.npc.Merchant.Papuma.Papuma;
import com.l2jserver.datapack.ai.npc.Merchant.Payel.Payel;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Annette.Annette;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Cooper.Cooper;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Joey.Joey;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Lemper.Lemper;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Lundy.Lundy;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Martin.Martin;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Mickey.Mickey;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Nelson.Nelson;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Rood.Rood;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Saroyan.Saroyan;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Waters.Waters;
import com.l2jserver.datapack.ai.npc.Merchant.PetManager.Woods.Woods;
import com.l2jserver.datapack.ai.npc.Merchant.Philly.Philly;
import com.l2jserver.datapack.ai.npc.Merchant.Phojett.Phojett;
import com.l2jserver.datapack.ai.npc.Merchant.Plani.Plani;
import com.l2jserver.datapack.ai.npc.Merchant.Poesia.Poesia;
import com.l2jserver.datapack.ai.npc.Merchant.Prouse.Prouse;
import com.l2jserver.datapack.ai.npc.Merchant.Pupu.Pupu;
import com.l2jserver.datapack.ai.npc.Merchant.Raban.Raban;
import com.l2jserver.datapack.ai.npc.Merchant.Radia.Radia;
import com.l2jserver.datapack.ai.npc.Merchant.Rahel.Rahel;
import com.l2jserver.datapack.ai.npc.Merchant.Raik.Raik;
import com.l2jserver.datapack.ai.npc.Merchant.Rapin.Rapin;
import com.l2jserver.datapack.ai.npc.Merchant.Ratriya.Ratriya;
import com.l2jserver.datapack.ai.npc.Merchant.Raudia.Raudia;
import com.l2jserver.datapack.ai.npc.Merchant.Reep.Reep;
import com.l2jserver.datapack.ai.npc.Merchant.Reeya.Reeya;
import com.l2jserver.datapack.ai.npc.Merchant.Rene.Rene;
import com.l2jserver.datapack.ai.npc.Merchant.Rex.Rex;
import com.l2jserver.datapack.ai.npc.Merchant.Robre.Robre;
import com.l2jserver.datapack.ai.npc.Merchant.Rogen.Rogen;
import com.l2jserver.datapack.ai.npc.Merchant.Rolento.Rolento;
import com.l2jserver.datapack.ai.npc.Merchant.Romas.Romas;
import com.l2jserver.datapack.ai.npc.Merchant.Ronaldo.Ronaldo;
import com.l2jserver.datapack.ai.npc.Merchant.Rouge.Rouge;
import com.l2jserver.datapack.ai.npc.Merchant.Rouke.Rouke;
import com.l2jserver.datapack.ai.npc.Merchant.Rumba.Rumba;
import com.l2jserver.datapack.ai.npc.Merchant.Rupert.Rupert;
import com.l2jserver.datapack.ai.npc.Merchant.Sabrin.Sabrin;
import com.l2jserver.datapack.ai.npc.Merchant.SalesmanCat.SalesmanCat;
import com.l2jserver.datapack.ai.npc.Merchant.Salient.Salient;
import com.l2jserver.datapack.ai.npc.Merchant.Sandra.Sandra;
import com.l2jserver.datapack.ai.npc.Merchant.Sanford.Sanford;
import com.l2jserver.datapack.ai.npc.Merchant.Sara.Sara;
import com.l2jserver.datapack.ai.npc.Merchant.Scipio.Scipio;
import com.l2jserver.datapack.ai.npc.Merchant.Shafa.Shafa;
import com.l2jserver.datapack.ai.npc.Merchant.Shaling.Shaling;
import com.l2jserver.datapack.ai.npc.Merchant.Shantra.Shantra;
import com.l2jserver.datapack.ai.npc.Merchant.Shhadai.Shhadai;
import com.l2jserver.datapack.ai.npc.Merchant.Shikon.Shikon;
import com.l2jserver.datapack.ai.npc.Merchant.Shitara.Shitara;
import com.l2jserver.datapack.ai.npc.Merchant.Shutner.Shutner;
import com.l2jserver.datapack.ai.npc.Merchant.Silvia.Silvia;
import com.l2jserver.datapack.ai.npc.Merchant.Simplon.Simplon;
import com.l2jserver.datapack.ai.npc.Merchant.Singsing.Singsing;
import com.l2jserver.datapack.ai.npc.Merchant.Solinus.Solinus;
import com.l2jserver.datapack.ai.npc.Merchant.Sonia.Sonia;
import com.l2jserver.datapack.ai.npc.Merchant.Sparky.Sparky;
import com.l2jserver.datapack.ai.npc.Merchant.Stany.Stany;
import com.l2jserver.datapack.ai.npc.Merchant.Sydney.Sydney;
import com.l2jserver.datapack.ai.npc.Merchant.Tahoo.Tahoo;
import com.l2jserver.datapack.ai.npc.Merchant.Tangen.Tangen;
import com.l2jserver.datapack.ai.npc.Merchant.Terava.Terava;
import com.l2jserver.datapack.ai.npc.Merchant.Tomanel.Tomanel;
import com.l2jserver.datapack.ai.npc.Merchant.Triya.Triya;
import com.l2jserver.datapack.ai.npc.Merchant.Tweety.Tweety;
import com.l2jserver.datapack.ai.npc.Merchant.Uno.Uno;
import com.l2jserver.datapack.ai.npc.Merchant.Urgal.Urgal;
import com.l2jserver.datapack.ai.npc.Merchant.Uska.Uska;
import com.l2jserver.datapack.ai.npc.Merchant.Varanket.Varanket;
import com.l2jserver.datapack.ai.npc.Merchant.TraderVerona.TraderVerona;
import com.l2jserver.datapack.ai.npc.Merchant.Treauvi.Treauvi;
import com.l2jserver.datapack.ai.npc.Merchant.Veronika.Veronika;
import com.l2jserver.datapack.ai.npc.Merchant.Viktor.Viktor;
import com.l2jserver.datapack.ai.npc.Merchant.Violet.Violet;
import com.l2jserver.datapack.ai.npc.Merchant.Vladimir.Vladimir;
import com.l2jserver.datapack.ai.npc.Merchant.Volker.Volker;
import com.l2jserver.datapack.ai.npc.Merchant.Vollodos.Vollodos;
import com.l2jserver.datapack.ai.npc.Merchant.Weber.Weber;
import com.l2jserver.datapack.ai.npc.Merchant.Woodley.Woodley;
import com.l2jserver.datapack.ai.npc.Merchant.Woodrow.Woodrow;
import com.l2jserver.datapack.ai.npc.Merchant.Zakone.Zakone;
import com.l2jserver.datapack.ai.npc.Merchant.Zenith.Zenith;
import com.l2jserver.datapack.ai.npc.Merchant.Zenkin.Zenkin;
import com.l2jserver.datapack.ai.npc.MerchantOfMammon.MerchantOfMammon;
import com.l2jserver.datapack.ai.npc.Minigame.Minigame;
import com.l2jserver.datapack.ai.npc.MonumentOfHeroes.MonumentOfHeroes;
import com.l2jserver.datapack.ai.npc.NevitsHerald.NevitsHerald;
import com.l2jserver.datapack.ai.npc.NormalDoorkeeper.EustaceVanIssen.EustaceVanIssen;
import com.l2jserver.datapack.ai.npc.NormalDoorkeeper.FelmingVanIssen.FelmingVanIssen;
import com.l2jserver.datapack.ai.npc.NormalDoorkeeper.GregorAthebaldt.GregorAthebaldt;
import com.l2jserver.datapack.ai.npc.NormalDoorkeeper.ViktorVanDeik.ViktorVanDeik;
import com.l2jserver.datapack.ai.npc.NpcBuffers.NpcBuffers;
import com.l2jserver.datapack.ai.npc.NpcBuffers.impl.CabaleBuffer;
import com.l2jserver.datapack.ai.npc.PriestOfBlessing.PriestOfBlessing;
import com.l2jserver.datapack.ai.npc.Rafforty.Rafforty;
import com.l2jserver.datapack.ai.npc.Researchers.Researchers;
import com.l2jserver.datapack.ai.npc.RiftWatcher.RiftWatcher;
import com.l2jserver.datapack.ai.npc.Rignos.Rignos;
import com.l2jserver.datapack.ai.npc.Selina.Selina;
import com.l2jserver.datapack.ai.npc.SeparatedSoul.SeparatedSoul;
import com.l2jserver.datapack.ai.npc.Sirra.Sirra;
import com.l2jserver.datapack.ai.npc.Sobling.Sobling;
import com.l2jserver.datapack.ai.npc.Steward.AzitChamberlain.AzitChamberlain;
import com.l2jserver.datapack.ai.npc.Steward.AzitChamberlainYetti.AzitChamberlainYetti;
import com.l2jserver.datapack.ai.npc.Steward.Biggerstaff.Biggerstaff;
import com.l2jserver.datapack.ai.npc.Steward.ChamberlainBandello.ChamberlainBandello;
import com.l2jserver.datapack.ai.npc.Steward.FarmChamberlain.FarmChamberlain;
import com.l2jserver.datapack.ai.npc.Steward.OlMahumTamutak.OlMahumTamutak;
import com.l2jserver.datapack.ai.npc.SubclassCertification.SubclassCertification;
import com.l2jserver.datapack.ai.npc.Summons.Pets.BabyPets;
import com.l2jserver.datapack.ai.npc.Summons.Pets.ImprovedBabyPets;
import com.l2jserver.datapack.ai.npc.Summons.Servitors.Servitors;
import com.l2jserver.datapack.ai.npc.SupportUnitCaptain.SupportUnitCaptain;
import com.l2jserver.datapack.ai.npc.SymbolMaker.SymbolMaker;
import com.l2jserver.datapack.ai.npc.Taroon.Taroon;
import com.l2jserver.datapack.ai.npc.Teleporter.Angelina.Angelina;
import com.l2jserver.datapack.ai.npc.Teleporter.Arisha.Arisha;
import com.l2jserver.datapack.ai.npc.Teleporter.Belladonna.Belladonna;
import com.l2jserver.datapack.ai.npc.Teleporter.Billia.Billia;
import com.l2jserver.datapack.ai.npc.Teleporter.Capellini.Capellini;
import com.l2jserver.datapack.ai.npc.Teleporter.Cecile.Cecile;
import com.l2jserver.datapack.ai.npc.Teleporter.Ciffon.Ciffon;
import com.l2jserver.datapack.ai.npc.Teleporter.Clavier.Clavier;
import com.l2jserver.datapack.ai.npc.Teleporter.Dimension_Vertex.Dimension_Vertex_1.Dimension_Vertex_1;
import com.l2jserver.datapack.ai.npc.Teleporter.Dimension_Vertex.Dimension_Vertex_2.Dimension_Vertex_2;
import com.l2jserver.datapack.ai.npc.Teleporter.Dimension_Vertex.Dimension_Vertex_3.Dimension_Vertex_3;
import com.l2jserver.datapack.ai.npc.Teleporter.Elisabeth.Elisabeth;
import com.l2jserver.datapack.ai.npc.Teleporter.Esmeralda.Esmeralda;
import com.l2jserver.datapack.ai.npc.Teleporter.Flauen.Flauen;
import com.l2jserver.datapack.ai.npc.Teleporter.Gariachin.Gariachin;
import com.l2jserver.datapack.ai.npc.Teleporter.Gracia_Transmitter.Gracia_Transmitter;
import com.l2jserver.datapack.ai.npc.Teleporter.Havarti.Havarti;
import com.l2jserver.datapack.ai.npc.Teleporter.Ilyana.Ilyana;
import com.l2jserver.datapack.ai.npc.Teleporter.Jasmine.Jasmine;
import com.l2jserver.datapack.ai.npc.Teleporter.Karin.Karin;
import com.l2jserver.datapack.ai.npc.Teleporter.Kurfa.Kurfa;
import com.l2jserver.datapack.ai.npc.Teleporter.Leggins.Leggins;
import com.l2jserver.datapack.ai.npc.Teleporter.Mariell.Mariell;
import com.l2jserver.datapack.ai.npc.Teleporter.Mellin.Mellin;
import com.l2jserver.datapack.ai.npc.Teleporter.Merian.Merian;
import com.l2jserver.datapack.ai.npc.Teleporter.Minevea.Minevea;
import com.l2jserver.datapack.ai.npc.Teleporter.Mint.Mint;
import com.l2jserver.datapack.ai.npc.Teleporter.Mozzarella.Mozzarella;
import com.l2jserver.datapack.ai.npc.Teleporter.Oraochin.Oraochin;
import com.l2jserver.datapack.ai.npc.Teleporter.Penne.Penne;
import com.l2jserver.datapack.ai.npc.Teleporter.PhotoSno.PhotoSno;
import com.l2jserver.datapack.ai.npc.Teleporter.Pontina.Pontina;
import com.l2jserver.datapack.ai.npc.Teleporter.Race_Gatekeeper1.Race_Gatekeeper1;
import com.l2jserver.datapack.ai.npc.Teleporter.Ragara.Ragara;
import com.l2jserver.datapack.ai.npc.Teleporter.Ramsedas.Ramsedas;
import com.l2jserver.datapack.ai.npc.Teleporter.Rapunzel.Rapunzel;
import com.l2jserver.datapack.ai.npc.Teleporter.Richlin.Richlin;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestAden.DawnPriestAden;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestDion.DawnPriestDion;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestGiran.DawnPriestGiran;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestGludin.DawnPriestGludin;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestGludio.DawnPriestGludio;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestGodard.DawnPriestGodard;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestHeiness.DawnPriestHeiness;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestHunter.DawnPriestHunter;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestOren.DawnPriestOren;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestRune.DawnPriestRune;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DawnPriestSchuttgart.DawnPriestSchuttgart;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessAden.DuskPriestessAden;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessDion.DuskPriestessDion;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessGiran.DuskPriestessGiran;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessGludin.DuskPriestessGludin;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessGludio.DuskPriestessGludio;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessGodard.DuskPriestessGodard;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessHeiness.DuskPriestessHeiness;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessHunter.DuskPriestessHunter;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessOren.DuskPriestessOren;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessRune.DuskPriestessRune;
import com.l2jserver.datapack.ai.npc.Teleporter.SSQNpcPriest.DuskPriestessSchuttgart.DuskPriestessSchuttgart;
import com.l2jserver.datapack.ai.npc.Teleporter.Stanislava.Stanislava;
import com.l2jserver.datapack.ai.npc.Teleporter.Tamil.Tamil;
import com.l2jserver.datapack.ai.npc.Teleporter.Tatiana.Tatiana;
import com.l2jserver.datapack.ai.npc.Teleporter.TeleportCubeA001.TeleportCubeA001;
import com.l2jserver.datapack.ai.npc.Teleporter.TeleportCubeFrintezza.TeleportCubeFrintezza;
import com.l2jserver.datapack.ai.npc.Teleporter.Teranu.Teranu;
import com.l2jserver.datapack.ai.npc.Teleporter.Tiramisu.Tiramisu;
import com.l2jserver.datapack.ai.npc.Teleporter.Tp_Teleporter.Tp_Teleporter;
import com.l2jserver.datapack.ai.npc.Teleporter.Trishya.Trishya;
import com.l2jserver.datapack.ai.npc.Teleporter.Valentina.Valentina;
import com.l2jserver.datapack.ai.npc.Teleporter.Verona.Verona;
import com.l2jserver.datapack.ai.npc.Teleporter.Wirphy.Wirphy;
import com.l2jserver.datapack.ai.npc.Teleporter.Yinir.Yinir;
import com.l2jserver.datapack.ai.npc.Teleporter.Yung.Yung;
import com.l2jserver.datapack.ai.npc.Teleports.Asher.Asher;
import com.l2jserver.datapack.ai.npc.Teleports.DelusionTeleport.DelusionTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.GatekeeperSpirit.GatekeeperSpirit;
import com.l2jserver.datapack.ai.npc.Teleports.GhostChamberlainOfElmoreden.GhostChamberlainOfElmoreden;
import com.l2jserver.datapack.ai.npc.Teleports.GrandBossTeleporters.GrandBossTeleporters;
import com.l2jserver.datapack.ai.npc.Teleports.GuardianBorder.GuardianBorder;
import com.l2jserver.datapack.ai.npc.Teleports.Klemis.Klemis;
import com.l2jserver.datapack.ai.npc.Teleports.MithrilMinesTeleporter.MithrilMinesTeleporter;
import com.l2jserver.datapack.ai.npc.Teleports.NewbieGuide.NewbieGuide;
import com.l2jserver.datapack.ai.npc.Teleports.PaganTeleporters.PaganTeleporters;
import com.l2jserver.datapack.ai.npc.Teleports.SSQTeleporter.SSQTeleporter;
import com.l2jserver.datapack.ai.npc.Teleports.StakatoNestTeleporter.StakatoNestTeleporter;
import com.l2jserver.datapack.ai.npc.Teleports.SteelCitadelTeleport.SteelCitadelTeleport;
import com.l2jserver.datapack.ai.npc.Teleports.Survivor.Survivor;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportToRaceTrack.TeleportToRaceTrack;
import com.l2jserver.datapack.ai.npc.Teleports.TeleportToUndergroundColiseum.TeleportToUndergroundColiseum;
import com.l2jserver.datapack.ai.npc.TerritoryManagers.TerritoryManagers;
import com.l2jserver.datapack.ai.npc.TownPets.TownPets;
import com.l2jserver.datapack.ai.npc.TraderImmortality.TraderImmortality;
import com.l2jserver.datapack.ai.npc.Tunatun.Tunatun;
import com.l2jserver.datapack.ai.npc.VarkaSilenosSupport.VarkaSilenosSupport;
import com.l2jserver.datapack.ai.npc.VigilImmortality.VigilImmortality;
import com.l2jserver.datapack.ai.npc.VillageMasters.FirstClassTransferTalk.FirstClassTransferTalk;
import com.l2jserver.datapack.ai.npc.WeaverOlf.WeaverOlf;
import com.l2jserver.datapack.ai.npc.WyvernManager.WyvernManager;
import com.l2jserver.datapack.ai.npc.ZakenSender.ZakenSender;
import com.l2jserver.datapack.ai.npc.coach.Blacksmith.Blacksmith;
import com.l2jserver.datapack.ai.npc.coach.ClericCoach.ClericCoach;
import com.l2jserver.datapack.ai.npc.coach.FighterCoach.FighterCoach;
import com.l2jserver.datapack.ai.npc.coach.KamaelCoach.KamaelCoach;
import com.l2jserver.datapack.ai.npc.coach.MageCoach.MageCoach;
import com.l2jserver.datapack.ai.npc.coach.WarehouseKeeper.WarehouseKeeper;
import com.l2jserver.datapack.ai.npc.coach.WarehouseKeeperForChaotic.WarehouseKeeperForChaotic;
import com.l2jserver.datapack.ai.npc.coach.WizardCoach.WizardCoach;
import com.l2jserver.datapack.village_master.Alliance.Alliance;
import com.l2jserver.datapack.village_master.Clan.Clan;
import com.l2jserver.datapack.village_master.DarkElfChange1.DarkElfChange1;
import com.l2jserver.datapack.village_master.DarkElfChange2.DarkElfChange2;
import com.l2jserver.datapack.village_master.DwarfBlacksmithChange1.DwarfBlacksmithChange1;
import com.l2jserver.datapack.village_master.DwarfBlacksmithChange2.DwarfBlacksmithChange2;
import com.l2jserver.datapack.village_master.DwarfWarehouseChange1.DwarfWarehouseChange1;
import com.l2jserver.datapack.village_master.DwarfWarehouseChange2.DwarfWarehouseChange2;
import com.l2jserver.datapack.village_master.ElfHumanClericChange2.ElfHumanClericChange2;
import com.l2jserver.datapack.village_master.ElfHumanFighterChange1.ElfHumanFighterChange1;
import com.l2jserver.datapack.village_master.ElfHumanFighterChange2.ElfHumanFighterChange2;
import com.l2jserver.datapack.village_master.ElfHumanWizardChange1.ElfHumanWizardChange1;
import com.l2jserver.datapack.village_master.ElfHumanWizardChange2.ElfHumanWizardChange2;
import com.l2jserver.datapack.village_master.KamaelChange1.KamaelChange1;
import com.l2jserver.datapack.village_master.KamaelChange2.KamaelChange2;
import com.l2jserver.datapack.village_master.OrcChange1.OrcChange1;
import com.l2jserver.datapack.village_master.OrcChange2.OrcChange2;

/**
 * AI loader.
 * @author Zoey76
 * @version 2.6.2.0
 */
public class AILoader {
	
	private static final Logger LOG = LoggerFactory.getLogger(AILoader.class);
	
	private static final Class<?>[] SCRIPTS = {
		// NPC
		AdventureGuildsman.class,
		Alarm.class,
		Alexandria.class,
		ArenaManager.class,
		AvantGarde.class,
		BlackJudge.class,
		BlackMarketeerOfMammon.class,
		BlacksmithOfMammon.class,
		CastleAmbassador.class,
		CastleBlacksmith.class,
		CastleCourtMagician.class,
		CastleSiegeManager.class,
		CastleTeleporter.class,
		CastleWarehouse.class,
		Chandra.class,
		ClassMaster.class,
		CleftGateRed.class,
		DimensionKeeper.class,
		Dorian.class,
		DragonVortex.class,
		EchoCrystals.class,
		Elmina.class,
		ForgeOfTheGods.class,
		Rooney.class,
		TarBeetle.class,
		FortressArcherCaptain.class,
		FortressSiegeManager.class,
		FreyasSteward.class,
		GiantDDwarf.class,
		GiantQDwarf.class,
		GuideUndeath.class,
		HandyBlockGuide.class,
		Jinia.class,
		Katenar.class,
		KetraOrcSupport.class,
		Kier.class,
		Kroon.class,
		MercenaryCaptain.class,
		MerchantOfMammon.class,
		Minigame.class,
		MonumentOfHeroes.class,
		NevitsHerald.class,
		NpcBuffers.class,
		CabaleBuffer.class,
		PriestOfBlessing.class,
		Rafforty.class,
		Researchers.class,
		RiftWatcher.class,
		Rignos.class,
		Selina.class,
		Sirra.class,
		Sobling.class,
		SubclassCertification.class,
		BabyPets.class,
		ImprovedBabyPets.class,
		Servitors.class,
		SupportUnitCaptain.class,
		SymbolMaker.class,
		Taroon.class,
		Asher.class,
		DelusionTeleport.class,
		GatekeeperSpirit.class,
		GhostChamberlainOfElmoreden.class,
		GrandBossTeleporters.class,
		GuardianBorder.class,
		Klemis.class,
		MithrilMinesTeleporter.class,
		NewbieGuide.class,
		PaganTeleporters.class,
		SSQTeleporter.class,
		StakatoNestTeleporter.class,
		SteelCitadelTeleport.class,
		Survivor.class,
		TeleportToRaceTrack.class,
		TeleportToUndergroundColiseum.class,
		TerritoryManagers.class,
		TownPets.class,
		TraderImmortality.class,
		Tunatun.class,
		VarkaSilenosSupport.class,
		VigilImmortality.class,
		FirstClassTransferTalk.class,
		WeaverOlf.class,
		WyvernManager.class,
		ZakenSender.class,
		// Coaches
		Blacksmith.class,
		ClericCoach.class,
		FighterCoach.class,
		KamaelCoach.class,
		MageCoach.class,
		WarehouseKeeper.class,
		WarehouseKeeperForChaotic.class,
		WizardCoach.class,
		// Teleporter
		Angelina.class,
		Arisha.class,
		Belladonna.class,
		Billia.class,
		Capellini.class,
		Cecile.class,
		Ciffon.class,
		Clavier.class,
		Dimension_Vertex_1.class,
		Dimension_Vertex_2.class,
		Dimension_Vertex_3.class,
		Elisabeth.class,
		Esmeralda.class,
		Flauen.class,
		Gariachin.class,
		Gracia_Transmitter.class,
		Havarti.class,
		Ilyana.class,
		Jasmine.class,
		Karin.class,
		Kurfa.class,
		Leggins.class,
		Mariell.class,
		Mellin.class,
		Merian.class,
		Minevea.class,
		Mint.class,
		Mozzarella.class,
		Oraochin.class,
		Penne.class,
		PhotoSno.class,
		Pontina.class,
		Race_Gatekeeper1.class,
		Ragara.class,
		Ramsedas.class,
		Rapunzel.class,
		Richlin.class,
		SeparatedSoul.class,
		Stanislava.class,
		Tamil.class,
		Tatiana.class,
		TeleportCubeA001.class,
		TeleportCubeFrintezza.class,
		Teranu.class,
		Tiramisu.class,
		Tp_Teleporter.class,
		Trishya.class,
		Valentina.class,
		Verona.class,
		Wirphy.class,
		Yinir.class,
		Yung.class,
		// Custodian
		Adrienne.class,
		Aida.class,
		Albert.class,
		Bianca.class,
		Billy.class,
		Black.class,
		Boyer.class,
		Branhilde.class,
		Bremmer.class,
		Calis.class,
		Carey.class,
		Carrel.class,
		Cerna.class,
		Crissy.class,
		Dianne.class,
		Dillon.class,
		Dimaggio.class,
		Emma.class,
		Flynn.class,
		Gladys.class,
		Helga.class,
		Horner.class,
		Jacques.class,
		Jimmy.class,
		Karuto.class,
		Korgen.class,
		Michael.class,
		Milicent.class,
		Pattie.class,
		Regina.class,
		Ron.class,
		Ronald.class,
		Ruben.class,
		Seth.class,
		Stanley.class,
		Tim.class,
		Wayne.class,
		Winker.class,
		// Steward
		AzitChamberlain.class,
		AzitChamberlainYetti.class,
		Biggerstaff.class,
		ChamberlainBandello.class,
		FarmChamberlain.class,
		OlMahumTamutak.class,
		// Fortress Steward
		AaruFortSteward.class,
		AntharasFortSteward.class,
		ArchaicFortSteward.class,
		BayouFortSteward.class,
		BorderlandFortSteward.class,
		CloudMountainFortSteward.class,
		DemonFortSteward.class,
		DragonspineFortSteward.class,
		FloranFortSteward.class,
		HiveFortSteward.class,
		HuntersFortSteward.class,
		IvoryFortSteward.class,
		MonasticFortSteward.class,
		NarsellFortSteward.class,
		ShantyFortSteward.class,
		SouthernFortSteward.class,
		SwampFortSteward.class,
		TanorFortSteward.class,
		ValleyFortSteward.class,
		WesternFortSteward.class,
		WhiteSandsFortSteward.class,
		// Normal Doorkeeper
		EustaceVanIssen.class,
		FelmingVanIssen.class,
		GregorAthebaldt.class,
		ViktorVanDeik.class,
		// Agit Doorkeeper
		AzitDoorman.class,
		DoormanOfFortress.class,
		DoormanOfHell.class,
		DoormanOfRainbow.class,
		FarmDoorman.class,
		PartisanDoormanHarkel.class,
		// Fortress Doorkeeper
		FortressDoorkeeper.class,
		// Doorkeeper
		OutterDoorman.class,
		InnerDoorman.class,
		// Janitor
		Janitor.class,
		// Chamberlain
		Alfred.class,
		August.class,
		Brasseur.class,
		Crosby.class,
		Frederick.class,
		Logan.class,
		Neurath.class,
		Saius.class,
		Saul.class,
		// SSQ Priests
		DawnPriestAden.class,
		DawnPriestDion.class,
		DawnPriestGiran.class,
		DawnPriestGludin.class,
		DawnPriestGludio.class,
		DawnPriestGodard.class,
		DawnPriestHeiness.class,
		DawnPriestHunter.class,
		DawnPriestOren.class,
		DawnPriestRune.class,
		DawnPriestSchuttgart.class,
		DuskPriestessAden.class,
		DuskPriestessDion.class,
		DuskPriestessGiran.class,
		DuskPriestessGludin.class,
		DuskPriestessGludio.class,
		DuskPriestessGodard.class,
		DuskPriestessHeiness.class,
		DuskPriestessHunter.class,
		DuskPriestessOren.class,
		DuskPriestessRune.class,
		DuskPriestessSchuttgart.class,
		// Merchant
		Abercrombie.class,
		Acellopy.class,
		Adrian.class,
		Alexis.class,
		Alisha.class,
		Altar.class,
		Anton.class,
		Antonio.class,
		Aren.class,
		Ariel.class,
		Arodin.class,
		Asama.class,
		Astrid.class,
		Atan.class,
		Auzendurof.class,
		Bandor.class,
		Berynel.class,
		Borodin.class,
		Burns.class,
		Candice.class,
		Carson.class,
		Cel.class,
		Cema.class,
		Chali.class,
		Clancy.class,
		Cona.class,
		Cullinas.class,
		Daeger.class,
		Daeronees.class,
		Dani.class,
		Denkus.class,
		Denver.class,
		Desian.class,
		Dindin.class,
		Diyabu.class,
		Dolphren.class,
		Donai.class,
		Drawin.class,
		Drumond.class,
		Edroc.class,
		Elena.class,
		Elliany.class,
		Enverun.class,
		Erinu.class,
		Espen.class,
		FarmFeedSeller.class,
		Felton.class,
		Fundin.class,
		Gaiman.class,
		Galicbredo.class,
		Galladuchi.class,
		Garette.class,
		Garita.class,
		Gentler.class,
		Giordo.class,
		Grabner.class,
		Green.class,
		Greta.class,
		Groot.class,
		Hakran.class,
		Hallypia.class,
		Hans.class,
		Harmony.class,
		Hedinger.class,
		Helmut.class,
		Helvetia.class,
		Hittchi.class,
		Holly.class,
		Hullia.class,
		Ilia.class,
		Iria.class,
		Iz.class,
		Jackson.class,
		Jaka.class,
		Jakaron.class,
		Janne.class,
		Jouge.class,
		Judith.class,
		Jumara.class,
		Karai.class,
		Katrine.class,
		Kc.class,
		Kiki.class,
		Kitzka.class,
		Koram.class,
		Kunai.class,
		Lanna.class,
		Lara.class,
		Lars.class,
		Lector.class,
		Leon.class,
		Liesel.class,
		Lorel.class,
		Lorenzo.class,
		Luka.class,
		Lumen.class,
		Lyann.class,
		Lynn.class,
		Mailland.class,
		MerchantGolem.class,
		Metar.class,
		Miflen.class,
		Migel.class,
		Mina.class,
		Mion.class,
		Natasha.class,
		Neagel.class,
		Nedy.class,
		Nestle.class,
		Nils.class,
		OfficerTolonis.class,
		Onyx.class,
		Owaking.class,
		Paint.class,
		Pano.class,
		Papuma.class,
		Payel.class,
		Philly.class,
		Phojett.class,
		Plani.class,
		Poesia.class,
		Prouse.class,
		Pupu.class,
		Raban.class,
		Radia.class,
		Rahel.class,
		Raik.class,
		Rapin.class,
		Ratriya.class,
		Raudia.class,
		Reep.class,
		Reeya.class,
		Rene.class,
		Rex.class,
		Robre.class,
		Rogen.class,
		Rolento.class,
		Romas.class,
		Ronaldo.class,
		Rouge.class,
		Rouke.class,
		Rumba.class,
		Rupert.class,
		Sabrin.class,
		SalesmanCat.class,
		Salient.class,
		Sandra.class,
		Sara.class,
		Shafa.class,
		Shaling.class,
		Shantra.class,
		Shhadai.class,
		Shikon.class,
		Shitara.class,
		Shutner.class,
		Silvia.class,
		Simplon.class,
		Singsing.class,
		Sonia.class,
		Sparky.class,
		Stany.class,
		Sydney.class,
		Tahoo.class,
		Tangen.class,
		Terava.class,
		Tomanel.class,
		Triya.class,
		Tweety.class,
		Uno.class,
		Urgal.class,
		Uska.class,
		Varanket.class,
		TraderVerona.class,
		Treauvi.class,
		Veronika.class,
		Viktor.class,
		Violet.class,
		Vladimir.class,
		Volker.class,
		Vollodos.class,
		Weber.class,
		Woodley.class,
		Woodrow.class,
		Zakone.class,
		Zenith.class,
		Zenkin.class,
		// Fisher
		Batidae.class,
		Berix.class,
		Bleaker.class,
		Brang.class,
		Cyano.class,
		Eindarkner.class,
		Hilgendorf.class,
		Galba.class,
		Hufs.class,
		Klaw.class,
		Klufe.class,
		Lanosco.class,
		Linneaus.class,
		Litulon.class,
		Mishini.class,
		Monakan.class,
		Ofulle.class,
		Ogord.class,
		Pamfus.class,
		Perelin.class,
		Platis.class,
		Ropfi.class,
		Willeri.class,
		// Castle Mercenary
		Arvid.class,
		Eldon.class,
		Gompers.class,
		Greenspan.class,
		Kendrew.class,
		Lowell.class,
		Morrison.class,
		Sanford.class,
		Solinus.class,
		// Castle Merchant
		ManorManager1.class,
		ManorManager2.class,
		ManorManager3.class,
		ManorManager4.class,
		ManorManager5.class,
		ManorManager6.class,
		ManorManager7.class,
		ManorManager8.class,
		ManorManager9.class,
		ManorManager10.class,
		ManorManager11.class,
		ManorManager12.class,
		ManorManager13.class,
		ManorManager14.class,
		// Pet Manager
		Annette.class,
		Cooper.class,
		Joey.class,
		Lemper.class,
		Lundy.class,
		Martin.class,
		Mickey.class,
		Nelson.class,
		Rood.class,
		Saroyan.class,
		Waters.class,
		Woods.class,
		// Reputation Manager
		Lepidus.class,
		Scipio.class,
		// Fantasy Isle
		MC_Show.class,
		HandysBlockCheckerEvent.class,
		// Group Template
		AltarsOfSacrifice.class,
		BeastFarm.class,
		CorpseOfDeadman.class,
		DenOfEvil.class,
		DragonValley.class,
		FairyTrees.class,
		FeedableBeasts.class,
		FleeMonsters.class,
		FrozenLabyrinth.class,
		GiantsCave.class,
		HotSprings.class,
		IsleOfPrayer.class,
		LairOfAntharas.class,
		MinionSpawnManager.class,
		MonasteryOfSilence.class,
		NonLethalableNpcs.class,
		NonTalkingNpcs.class,
		PavelArchaic.class,
		PlainsOfDion.class,
		PlainsOfLizardman.class,
		PolymorphingAngel.class,
		PolymorphingOnAttack.class,
		PrimevalIsle.class,
		PrisonGuards.class,
		RaidBossCancel.class,
		RandomSpawn.class,
		RangeGuard.class,
		Remnants.class,
		Sandstorms.class,
		SeeThroughSilentMove.class,
		SelMahumDrill.class,
		SelMahumSquad.class,
		SilentValley.class,
		StakatoNest.class,
		SummonPc.class,
		TreasureChest.class,
		TurekOrcs.class,
		VarkaKetra.class,
		WarriorFishingBlock.class,
		// Individual
		Antharas.class,
		Baium.class,
		Sailren.class,
		Venom.class,
		Anais.class,
		Ballista.class,
		Beleth.class,
		BlackdaggerWing.class,
		BleedingFly.class,
		BloodyBerserker.class,
		BloodyKarik.class,
		BloodyKarinness.class,
		CrimsonHatuOtis.class,
		Core.class,
		DarkWaterDragon.class,
		DivineBeast.class,
		DrakosWarrior.class,
		DustRider.class,
		EmeraldHorn.class,
		Epidos.class,
		EvasGiftBox.class,
		FrightenedRagnaOrc.class,
		GiganticGolem.class,
		Gordon.class,
		GraveRobbers.class,
		Knoriks.class,
		MuscleBomber.class,
		Orfen.class,
		QueenAnt.class,
		QueenShyeed.class,
		RagnaOrcCommander.class,
		RagnaOrcHero.class,
		RagnaOrcSeer.class,
		NecromancerOfTheValley.class,
		ShadowSummoner.class,
		SinEater.class,
		SinWardens.class,
		Valakas.class,
		// Village Master
		Clan.class,
		Alliance.class,
		DarkElfChange1.class,
		DarkElfChange2.class,
		DwarfBlacksmithChange1.class,
		DwarfBlacksmithChange2.class,
		DwarfWarehouseChange1.class,
		DwarfWarehouseChange2.class,
		ElfHumanClericChange2.class,
		ElfHumanFighterChange1.class,
		ElfHumanFighterChange2.class,
		ElfHumanWizardChange1.class,
		ElfHumanWizardChange2.class,
		KamaelChange1.class,
		KamaelChange2.class,
		OrcChange1.class,
		OrcChange2.class
	};
	
	public static void main(String[] args) {
		int n = 0;
		for (var ai : SCRIPTS) {
			try {
				ai.getDeclaredConstructor().newInstance();
				n++;
			} catch (Exception ex) {
				LOG.error("Error loading AI {}!", ai.getSimpleName(), ex);
			}
		}
		LOG.info("Loaded {} AI scripts.", n);
		
	}
}
