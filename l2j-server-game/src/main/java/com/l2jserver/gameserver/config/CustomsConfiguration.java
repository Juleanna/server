/*
 * Copyright © 2004-2023 L2J Server
 *
 * This file is part of L2J Server.
 *
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.config;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.aeonbits.owner.Config.HotReloadType.ASYNC;
import static org.aeonbits.owner.Config.LoadType.MERGE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Reloadable;

import com.l2jserver.gameserver.config.converter.ColorConverter;
import com.l2jserver.gameserver.config.converter.IPLimitConverter;
import com.l2jserver.gameserver.config.converter.Seconds2MillisecondsConverter;
import com.l2jserver.gameserver.model.skills.AbnormalVisualEffect;

/**
 * Customs Configuration.
 * @author Zoey76
 * @version 2.6.1.0
 */
@Sources({
	"file:${L2J_HOME}/custom/game/config/customs.properties",
	"file:./config/customs.properties",
	"classpath:config/customs.properties"
})
@LoadPolicy(MERGE)
@HotReload(value = 20, unit = MINUTES, type = ASYNC)
public interface CustomsConfiguration extends Reloadable {

	
	@Key("ChampionEnable")
	Boolean championEnable();
	
	@Key("ChampionPassive")
	Boolean championPassive();
	
	@Key("ChampionFrequency")
	Integer getChampionFrequency();
	
	@Key("ChampionTitle")
	String getChampionTitle();

	@Key("ChampionAura")
	Boolean ChampionAura();
		
	@Key("ChampionMinLevel")
	Integer getChampionMinLevel();
	
	@Key("ChampionMaxLevel")
	Integer getChampionMaxLevel();
	
	@Key("ChampionHp")
	Integer getChampionHp();
	
	@Key("ChampionHpRegen")
	Double getChampionHpRegen();
	
	@Key("ChampionRewardsExpSp")
	Double getChampionRewardsExpSp();
	
	@Key("ChampionRewardsChance")
	Double getChampionRewardsChance();
	
	@Key("ChampionRewardsAmount")
	Double getChampionRewardsAmount();
	
	@Key("ChampionAdenasRewardsChance")
	Double getChampionAdenasRewardsChance();
	
	@Key("ChampionAdenasRewardsAmount")
	Double getChampionAdenasRewardsAmount();
	
	@Key("ChampionAtk")
	Float getChampionAtk();
	
	@Key("ChampionSpdAtk")
	Float getChampionSpdAtk();
	
	@Key("ChampionRewardItemID")
	Integer getChampionRewardItemID();
	
	@Key("ChampionRewardItemQty")
	Integer getChampionRewardItemQty();
	
	@Key("ChampionRewardLowerLvlItemChance")
	Integer getChampionRewardLowerLvlItemChance();
	
	@Key("ChampionRewardHigherLvlItemChance")
	Integer getChampionRewardHigherLvlItemChance();
	
	@Key("ChampionEnableVitality")
	Boolean championEnableVitality();
	
	@Key("ChampionEnableInInstances")
	Boolean championEnableInInstances();
	
	@Key("AllowWedding")
	Boolean allowWedding();
	
	@Key("WeddingPrice")
	Integer getWeddingPrice();
	
	@Key("WeddingPunishInfidelity")
	Boolean weddingPunishInfidelity();
	
	@Key("WeddingTeleport")
	Boolean weddingTeleport();
	
	@Key("WeddingTeleportPrice")
	Integer getWeddingTeleportPrice();
	
	@Key("WeddingTeleportDuration")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	Integer getWeddingTeleportDuration();
	
	@Key("WeddingAllowSameSex")
	Boolean weddingAllowSameSex();
	
	@Key("WeddingFormalWear")
	Boolean weddingFormalWear();
	
	@Key("WeddingDivorceCosts")
	Integer getWeddingDivorceCosts();
	
	@Key("BankingEnabled")
	Boolean bankingEnabled();
	
	@Key("BankingGoldbarCount")
	Integer getBankingGoldbarCount();
	
	@Key("BankingAdenaCount")
	Integer getBankingAdenaCount();
	
	@Key("EnableWarehouseSortingClan")
	Boolean enableWarehouseSortingClan();
	
	@Key("EnableWarehouseSortingPrivate")
	Boolean enableWarehouseSortingPrivate();
	
	@Key("OfflineTradeEnable")
	Boolean offlineTradeEnable();
	
	@Key("OfflineCraftEnable")
	Boolean offlineCraftEnable();
	
	@Key("OfflineModeInPeaceZone")
	Boolean offlineModeInPeaceZone();
	
	@Key("OfflineModeNoDamage")
	Boolean offlineModeNoDamage();
	
	@Key("OfflineSetNameColor")
	Boolean offlineSetNameColor();
	
	@Key("OfflineNameColor")
	@ConverterClass(ColorConverter.class)
	Integer getOfflineNameColor();
	
	@Key("OfflineFame")
	Boolean offlineFame();
	
	@Key("RestoreOffliners")
	Boolean restoreOffliners();
	
	@Key("OfflineMaxDays")
	Integer getOfflineMaxDays();
	
	@Key("OfflineDisconnectFinished")
	Boolean offlineDisconnectFinished();
	
	@Key("EnableManaPotionSupport")
	Boolean enableManaPotionSupport();
	
	@Key("DisplayServerTime")
	Boolean displayServerTime();
	
	@Key("ScreenWelcomeMessageEnable")
	Boolean screenWelcomeMessageEnable();
	
	@Key("ScreenWelcomeMessageText")
	String getScreenWelcomeMessageText();
	
	@Key("ScreenWelcomeMessageTime")
	@ConverterClass(Seconds2MillisecondsConverter.class)
	Integer getScreenWelcomeMessageTime();
	
	@Key("AntiFeedEnable")
	Boolean antiFeedEnable();
	
	@Key("AntiFeedDualbox")
	Boolean antiFeedDualbox();
	
	@Key("AntiFeedDisconnectedAsDualbox")
	Boolean antiFeedDisconnectedAsDualbox();
	
	@Key("AntiFeedInterval")
	Integer getAntiFeedInterval();
	
	@Key("AnnouncePkPvP")
	Boolean announcePkPvP();
	
	@Key("AnnouncePkPvPNormalMessage")
	Boolean announcePkPvPNormalMessage();
	
	@Key("AnnouncePkMsg")
	String getAnnouncePkMsg();
	
	@Key("AnnouncePvpMsg")
	String getAnnouncePvpMsg();
	
	@Key("ChatAdmin")
	Boolean chatAdmin();
	
	@Key("HellboundStatus")
	Boolean hellboundStatus();
	
	@Key("AutoLootVoiceEnable")
	Boolean autoLootVoiceCommand();
	
	@Key("AutoLootVoiceRestore")
	Boolean autoLootVoiceRestore();
	
	@Key("AutoLootItemsVoiceRestore")
	Boolean autoLootItemsVoiceRestore();
	
	@Key("AutoLootHerbsVoiceRestore")
	Boolean autoLootHerbsVoiceRestore();
	
	@Key("AutoLootHerbsList")
	Set<Integer> getAutoLootHerbsList();
	
	@Key("AutoLootItemsList")
	Set<Integer> getAutoLootItemsList();
	
	@Key("MultiLangEnable")
	Boolean multiLangEnable();
	
	@Key("MultiLangDefault")
	String getMultiLangDefault();
	
	@Key("MultiLangAllowed")
	Set<String> getMultiLangAllowed();
	
	@Key("MultiLangVoiceCommand")
	Boolean multiLangVoiceCommand();
	
	@Key("MultiLangSystemMessageEnable")
	Boolean multiLangSystemMessageEnable();
	
	@Key("MultiLangSystemMessageAllowed")
	List<String> getMultiLangSystemMessageAllowed();
	
	@Key("MultiLangNpcStringEnable")
	Boolean multiLangNpcStringEnable();
	
	@Key("MultiLangNpcStringAllowed")
	List<String> getMultiLangNpcStringAllowed();
	
	@Key("L2WalkerProtection")
	Boolean l2WalkerProtection();
	
	@Key("DebugVoiceCommand")
	Boolean debugVoiceCommand();
	
	@Key("DualboxCheckMaxPlayersPerIP")
	Integer getDualboxCheckMaxPlayersPerIP();
	
	@Key("DualboxCheckMaxOlympiadParticipantsPerIP")
	Integer getDualboxCheckMaxOlympiadParticipantsPerIP();
	
	@Key("DualboxCheckMaxL2EventParticipantsPerIP")
	Integer getDualboxCheckMaxL2EventParticipantsPerIP();
	
	@Key("DualboxCheckWhitelist")
	@ConverterClass(IPLimitConverter.class)
	Map<Integer, Integer> getDualboxCheckWhitelist();
	
	@Key("AllowChangePassword")
	Boolean allowChangePassword();

	@Key("AutoPotionsEnabled")
	Boolean AutoPotionsEnabled();

	@Key("AutoPotionsInOlympiad")
	Boolean AutoPotionsInOlympiad();

	@Key("AutoPotionMinimumLevel")
	Integer getAutoPotionMinimumLevel();

	@Key("AutoCpEnabled")
	Boolean AutoCpEnabled();

	@Key("AutoCpPercentage")
	Integer getAutoCpPercentage();

	@Key("AutoCpItemIds")
	Set<Integer> getAutoCpItemIdsList();

	@Key("AutoHpEnabled")
	Boolean AutoHpEnabled();

	@Key("AutoHpPercentage")
	Integer getAutoHpPercentage();

	@Key("AutoHpItemIds")
	Set<Integer> getAutoHpItemIdsList();

	@Key("AutoMpEnabled")
	Boolean AutoMpEnabled();

	@Key("AutoMpPercentage")
	Integer getAutoMpPercentage();

	@Key("AutoHpItemIds")
	Set<Integer> getAutoMpItemIdsList();

	@Key("EnableOfflinePlayCommand")
	Boolean EnableOfflinePlayCommand();

	@Key("OfflinePlayPremium")
	Boolean isOfflinePlayPremium();

	@Key("OfflinePlayLogoutOnDeath")
	Boolean isOfflinePlayLogoutOnDeath();

	@Key("OfflinePlayLoginMessage")
    String getOfflinePlayLoginMessage();

    @Key("OfflinePlaySetNameColor")
    boolean isOfflinePlaySetNameColor();

	@Key("OfflineNameColor")
	@ConverterClass(ColorConverter.class)
	Integer getAutoPlayOfflineNameColor();


	@Key("OfflinePlayAbnormalEffect")
	Set<String> getOfflinePlayAbnormalEffects();
	
	default List<AbnormalVisualEffect> readAbnormalEffectsFromConfig() {
		Set<String> effectsSet = getOfflinePlayAbnormalEffects();
		List<AbnormalVisualEffect> abnormalEffects = new ArrayList<>();
		if (effectsSet != null && !effectsSet.isEmpty()) {
			for (String effect : effectsSet) {
				try {
					AbnormalVisualEffect abnormalEffect = AbnormalVisualEffect.valueOf(effect.trim());
					abnormalEffects.add(abnormalEffect);
				} catch (IllegalArgumentException e) {
					System.out.println("Недопустимый аномальный эффект: " + effect);
				}
			}
		}
		return abnormalEffects;
	}

	@Key("EnableAutoPlay")
	Boolean isAutoPlayEnabled();

	@Key("EnableAutoPotion")
	Boolean isAutoPotionEnabled();

	@Key("EnableAutoSkill")
	Boolean isAutoSkillEnabled();

	@Key("EnableAutoItem")
	Boolean isAutoItemEnabled();

	@Key("ResumeAutoPlay")
	Boolean isAutoPlayResumedOnLogin();

	@Key("AssistLeader")
	Boolean isAssistLeaderEnabled();

	@Key("AutoPlayPremium")
	Boolean isAutoPlayPremiumEnabled();

	@Key("DisabledSkillIds")
	String getDisabledSkillIds();

	@Key("DisabledItemIds")
	String getDisabledItemIds();

	@Key("AutoPlayLoginMessage")
	String getAutoPlayLoginMessage();

}


   