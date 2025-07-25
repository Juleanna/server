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
package com.l2jserver.gameserver.model.skills;

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.config.Configuration.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.enums.MountType;
import com.l2jserver.gameserver.enums.ShotType;
import com.l2jserver.gameserver.instancemanager.HandysBlockCheckerManager;
import com.l2jserver.gameserver.model.ArenaParticipantsHolder;
import com.l2jserver.gameserver.model.L2ExtractableProductItem;
import com.l2jserver.gameserver.model.L2ExtractableSkill;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.PcCondOverride;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2BlockInstance;
import com.l2jserver.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.entity.TvTEvent;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.interfaces.IIdentifiable;
import com.l2jserver.gameserver.model.skills.targets.AffectObject;
import com.l2jserver.gameserver.model.skills.targets.AffectObjectStaticImpl;
import com.l2jserver.gameserver.model.skills.targets.AffectScope;
import com.l2jserver.gameserver.model.skills.targets.TargetType;
import com.l2jserver.gameserver.model.stats.BaseStats;
import com.l2jserver.gameserver.model.stats.Formulas;
import com.l2jserver.gameserver.model.stats.TraitType;
import com.l2jserver.gameserver.model.stats.functions.AbstractFunction;
import com.l2jserver.gameserver.model.stats.functions.FuncTemplate;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

public final class Skill implements IIdentifiable {
	private static final Logger _log = Logger.getLogger(Skill.class.getName());
	
	/** Skill ID. */
	private final int _id;
	/** Skill level. */
	private final int _level;
	/** Custom skill ID displayed by the client. */
	private final int _displayId;
	/** Custom skill level displayed by the client. */
	private final int _displayLevel;
	/** Skill client's name. */
	private final String _name;
	/** Operative type: passive, active, toggle. */
	private final SkillOperateType _operateType;
	private final int _magic;
	private final TraitType _traitType;
	private final boolean _reuseDelayLock;
	/** Initial MP consumption. */
	private final int _mpConsume1;
	/** MP consumption. */
	private final int _mpConsume2;
	/** MP consumption per channeling. */
	private final int _mpPerChanneling;
	/** HP consumption. */
	private final int _hpConsume;
	/** Amount of items consumed by this skill from caster. */
	private final int _itemConsumeCount;
	/** Id of item consumed by this skill from caster. */
	private final int _itemConsumeId;
	/** Cast range: how far can be the target. */
	private final int _castRange;
	/** Effect range: how far the skill affect the target. */
	private final int _effectRange;
	/** Abnormal instant, used for herbs mostly. */
	private final boolean _isAbnormalInstant;
	/** Abnormal level, global effect level. */
	private final int _abnormalLvl;
	/** Abnormal type: global effect "group". */
	private final AbnormalType _abnormalType;
	/** Abnormal time: global effect duration time. */
	private final int _abnormalTime;
	/** Abnormal visual effect: the visual effect displayed in game. */
	private AbnormalVisualEffect[] _abnormalVisualEffects = null;
	/** Abnormal visual effect special: the visual effect displayed in game. */
	private AbnormalVisualEffect[] _abnormalVisualEffectsSpecial = null;
	/** Abnormal visual effect event: the visual effect displayed in game. */
	private AbnormalVisualEffect[] _abnormalVisualEffectsEvent = null;
	/** If {@code true} this skill's effect should stay after death. */
	private final boolean _stayAfterDeath;
	/** If {@code true} this skill's effect should stay after class-subclass change. */
	private final boolean _stayOnSubclassChange;
	/** If {@code true} this skill's effect recovery HP/MP or CP from herb. */
	private final boolean _isRecoveryHerb;
	
	private int _refId;
	// all times in milliseconds
	private final int hitTime;
	private final int hitCancelTime;
	private final int _coolTime;
	private final int _reuseHashCode;
	private final int _reuseDelay;
	
	// base success chance
	private final int _magicLevel;
	private final int _lvlBonusRate;
	private final int _activateRate;
	private final int _minChance;
	private final int _maxChance;
	
	private final int[] affectLimit;
	private AffectObject affectObject;
	private final int affectRange;
	private AffectScope affectScope;
	private TargetType targetType;
	private final int[] fanRange;
	
	private final boolean _nextActionIsAttack;
	
	private final boolean _blockedInOlympiad;
	
	private final AttributeType _attributeType;
	private final int _attributePower;
	
	private final BaseStats _basicProperty;
	
	private final boolean _overhit;
	
	private final int _minPledgeClass;
	private final int _chargeConsume;
	private final int _soulMaxConsume;
	
	private final int _effectPoint;
	// Condition lists
	private List<Condition> _preCondition;
	private List<Condition> _itemPreCondition;
	private Set<MountType> _rideState;
	// Function lists
	private List<FuncTemplate> _funcTemplates;
	
	private final Map<EffectScope, List<AbstractEffect>> _effectLists = new EnumMap<>(EffectScope.class);
	
	private final boolean _isDebuff;
	
	private final boolean _isSuicideAttack;
	private final boolean _irreplaceableBuff;
	
	private final boolean _excludedFromCheck;
	private final boolean _simultaneousCast;
	
	private L2ExtractableSkill _extractableItems = null;
	
	private final String _icon;
	
	private volatile Byte[] _effectTypes;
	
	// Channeling data
	private final int _channelingSkillId;
	private final int _channelingTickInitialDelay;
	private final int _channelingTickInterval;
	
	public Skill(StatsSet set) {
		_id = set.getInt("skill_id");
		_level = set.getInt("level");
		_displayId = set.getInt("displayId", _id);
		_displayLevel = set.getInt("displayLevel", _level);
		_name = set.getString("name", "");
		_operateType = set.getEnum("operateType", SkillOperateType.class);
		_magic = set.getInt("isMagic", 0);
		_traitType = set.getEnum("trait", TraitType.class, TraitType.NONE);
		_reuseDelayLock = set.getBoolean("reuseDelayLock", false);
		_mpConsume1 = set.getInt("mpConsume1", 0);
		_mpConsume2 = set.getInt("mpConsume2", 0);
		_mpPerChanneling = set.getInt("mpPerChanneling", _mpConsume2);
		_hpConsume = set.getInt("hpConsume", 0);
		_itemConsumeCount = set.getInt("itemConsumeCount", 0);
		_itemConsumeId = set.getInt("itemConsumeId", 0);
		
		_castRange = set.getInt("castRange", -1);
		_effectRange = set.getInt("effectRange", -1);
		_abnormalLvl = set.getInt("abnormalLvl", 0);
		_abnormalType = set.getEnum("abnormalType", AbnormalType.class, AbnormalType.NONE);
		
		int abnormalTime = set.getInt("abnormalTime", 0);
		final var skillDuration = character().getSkillDuration().get(_id);
		if (character().modifySkillDuration() && (skillDuration != null)) {
			if ((getLevel() < 100) || (getLevel() > 140)) {
				abnormalTime = skillDuration;
			} else if (getLevel() < 140) {
				abnormalTime += skillDuration;
			}
		}
		
		_abnormalTime = abnormalTime;
		_isAbnormalInstant = set.getBoolean("abnormalInstant", false);
		parseAbnormalVisualEffect(set.getString("abnormalVisualEffect", null));
		
		_stayAfterDeath = set.getBoolean("stayAfterDeath", false);
		_stayOnSubclassChange = set.getBoolean("stayOnSubclassChange", true);
		
		hitTime = set.getInt("hitTime", 0);
		hitCancelTime = set.getInt("hitCancelTime", 0);
		_coolTime = set.getInt("coolTime", 0);
		_isDebuff = set.getBoolean("isDebuff", false);
		_isRecoveryHerb = set.getBoolean("isRecoveryHerb", false);
		_reuseHashCode = SkillData.getSkillHashCode(_id, _level);
		
		final var skillReuse = character().getSkillReuse().get(_id);
		if (character().modifySkillReuse() && (skillReuse != null)) {
			if (general().debug()) {
				_log.info("*** Skill " + _name + " (" + _level + ") changed reuse from " + set.getInt("reuseDelay", 0) + " to " + skillReuse + " seconds.");
			}
			_reuseDelay = skillReuse;
		} else {
			_reuseDelay = set.getInt("reuseDelay", 0);
		}
		
		final String rideState = set.getString("rideState", null);
		if (rideState != null) {
			String[] state = rideState.split(";");
			if (state.length > 0) {
				_rideState = new HashSet<>(state.length);
				for (String s : state) {
					try {
						_rideState.add(MountType.valueOf(s));
					} catch (Exception e) {
						_log.warning("Bad data in rideState for skill " + this + " !\n" + e);
					}
				}
			}
		}
		
		affectLimit = set.getIntArray("affectLimit", "0-0", "-");
		affectObject = set.getEnum("affectObject", AffectObjectStaticImpl.class, AffectObjectStaticImpl.ALL);
		affectScope = isToggle() ? AffectScope.SINGLE : set.getEnum("affectScope", AffectScope.class, AffectScope.NONE);
		affectRange = set.getInt("affectRange", 0);
		targetType = set.getEnum("targetType", TargetType.class, TargetType.SELF);
		fanRange = set.getIntArray("fanRange", "0,0,0,0", ",");
		
		_magicLevel = set.getInt("magicLvl", 0);
		_lvlBonusRate = set.getInt("lvlBonusRate", 0);
		_activateRate = set.getInt("activateRate", -1);
		_minChance = set.getInt("minChance", character().getMinAbnormalStateSuccessRate());
		_maxChance = set.getInt("maxChance", character().getMaxAbnormalStateSuccessRate());
		
		_nextActionIsAttack = set.getBoolean("nextActionAttack", false);
		
		_blockedInOlympiad = set.getBoolean("blockedInOlympiad", false);
		
		_attributeType = set.getEnum("attributeType", AttributeType.class, AttributeType.NONE);
		_attributePower = set.getInt("attributePower", 0);
		
		_basicProperty = set.getEnum("basicProperty", BaseStats.class, BaseStats.NONE);
		
		_overhit = set.getBoolean("overHit", false);
		_isSuicideAttack = set.getBoolean("isSuicideAttack", false);
		
		_minPledgeClass = set.getInt("minPledgeClass", 0);
		_chargeConsume = set.getInt("chargeConsume", 0);
		
		_soulMaxConsume = set.getInt("soulMaxConsumeCount", 0);
		
		_effectPoint = set.getInt("effectPoint", 0);
		
		_irreplaceableBuff = set.getBoolean("irreplaceableBuff", false);
		
		_excludedFromCheck = set.getBoolean("excludedFromCheck", false);
		_simultaneousCast = set.getBoolean("simultaneousCast", false);
		
		String capsuled_items = set.getString("capsuled_items_skill", null);
		if (capsuled_items != null) {
			if (capsuled_items.isEmpty()) {
				_log.warning("Empty Extractable Item Skill data in Skill Id: " + _id);
			}
			
			_extractableItems = parseExtractableSkill(_id, _level, capsuled_items);
		}
		
		_icon = set.getString("icon", "icon.skill0000");
		
		_channelingSkillId = set.getInt("channelingSkillId", 0);
		_channelingTickInterval = set.getInt("channelingTickInterval", 2) * 1000;
		_channelingTickInitialDelay = set.getInt("channelingTickInitialDelay", _channelingTickInterval / 1000) * 1000;
	}
	
	// TODO(Zoey76): Remove this after all skills are migrated.
	public void updateTargetSystem(TargetType targetType, AffectScope affectScope, AffectObject affectObject) {
		this.targetType = targetType;
		this.affectScope = affectScope;
		this.affectObject = affectObject;
	}
	
	public TraitType getTraitType() {
		return _traitType;
	}
	
	public AttributeType getAttributeType() {
		return _attributeType;
	}
	
	public int getAttributePower() {
		return _attributePower;
	}
	
	public TargetType getTargetType() {
		return targetType;
	}
	
	public boolean isAOE() {
		return (affectScope == AffectScope.FAN) ||
			(affectScope == AffectScope.POINT_BLANK) ||
			(affectScope == AffectScope.RANGE) ||
			(affectScope == AffectScope.RANGE_SORT_BY_HP) ||
			(affectScope == AffectScope.RING_RANGE) ||
			(affectScope == AffectScope.SQUARE) ||
			(affectScope == AffectScope.SQUARE_PB);
	}
	
	public boolean isDamage() {
		return hasEffectType(L2EffectType.MAGICAL_ATTACK, L2EffectType.HP_DRAIN, L2EffectType.PHYSICAL_ATTACK);
	}
	
	public boolean isOverhit() {
		return _overhit;
	}
	
	public boolean isSuicideAttack() {
		return _isSuicideAttack;
	}
	
	/**
	 * Verify if this skill is abnormal instant.<br>
	 * Herb buff skills yield {@code true} for this check.
	 * @return {@code true} if the skill is abnormal instant, {@code false} otherwise
	 */
	public boolean isAbnormalInstant() {
		return _isAbnormalInstant;
	}
	
	/**
	 * Gets the skill abnormal type.
	 * @return the abnormal type
	 */
	public AbnormalType getAbnormalType() {
		return _abnormalType;
	}
	
	/**
	 * Gets the skill abnormal level.
	 * @return the skill abnormal level
	 */
	public int getAbnormalLvl() {
		return _abnormalLvl;
	}
	
	/**
	 * Gets the skill abnormal time.<br>
	 * Is the base to calculate the duration of the continuous effects of this skill.
	 * @return the abnormal time
	 */
	public int getAbnormalTime() {
		return _abnormalTime;
	}
	
	/**
	 * Gets the skill abnormal visual effect.
	 * @return the abnormal visual effect
	 */
	public AbnormalVisualEffect[] getAbnormalVisualEffects() {
		return _abnormalVisualEffects;
	}
	
	/**
	 * Verify if the skill has abnormal visual effects.
	 * @return {@code true} if the skill has abnormal visual effects, {@code false} otherwise
	 */
	public boolean hasAbnormalVisualEffects() {
		return (_abnormalVisualEffects != null) && (_abnormalVisualEffects.length > 0);
	}
	
	/**
	 * Gets the skill special abnormal visual effect.
	 * @return the abnormal visual effect
	 */
	public AbnormalVisualEffect[] getAbnormalVisualEffectsSpecial() {
		return _abnormalVisualEffectsSpecial;
	}
	
	/**
	 * Verify if the skill has special abnormal visual effects.
	 * @return {@code true} if the skill has special abnormal visual effects, {@code false} otherwise
	 */
	public boolean hasAbnormalVisualEffectsSpecial() {
		return (_abnormalVisualEffectsSpecial != null) && (_abnormalVisualEffectsSpecial.length > 0);
	}
	
	/**
	 * Gets the skill event abnormal visual effect.
	 * @return the abnormal visual effect
	 */
	public AbnormalVisualEffect[] getAbnormalVisualEffectsEvent() {
		return _abnormalVisualEffectsEvent;
	}
	
	/**
	 * Verify if the skill has event abnormal visual effects.
	 * @return {@code true} if the skill has event abnormal visual effects, {@code false} otherwise
	 */
	public boolean hasAbnormalVisualEffectsEvent() {
		return (_abnormalVisualEffectsEvent != null) && (_abnormalVisualEffectsEvent.length > 0);
	}
	
	/**
	 * Gets the skill magic level.
	 * @return the skill magic level
	 */
	public int getMagicLevel() {
		return _magicLevel;
	}
	
	public int getLvlBonusRate() {
		return _lvlBonusRate;
	}
	
	public int getActivateRate() {
		return _activateRate;
	}
	
	/**
	 * Return custom minimum skill/effect chance.
	 * @return
	 */
	public int getMinChance() {
		return _minChance;
	}
	
	/**
	 * Return custom maximum skill/effect chance.
	 * @return
	 */
	public int getMaxChance() {
		return _maxChance;
	}
	
	/**
	 * Return true if skill effects should be removed on any action except movement
	 * @return
	 */
	public boolean isRemovedOnAnyActionExceptMove() {
		return (_abnormalType == AbnormalType.INVINCIBILITY) || (_abnormalType == AbnormalType.HIDE);
	}
	
	/**
	 * @return {@code true} if skill effects should be removed on damage
	 */
	public boolean isRemovedOnDamage() {
		return (_abnormalType == AbnormalType.SLEEP) || (_abnormalType == AbnormalType.FORCE_MEDITATION) || (_abnormalType == AbnormalType.HIDE);
	}
	
	/**
	 * @return {@code true} if skill can not be used in olympiad.
	 */
	public boolean isBlockedInOlympiad() {
		return _blockedInOlympiad;
	}
	
	/**
	 * Return the additional effect Id.
	 * @return
	 */
	public int getChannelingSkillId() {
		return _channelingSkillId;
	}
	
	/**
	 * Return true if character should attack target after skill
	 * @return
	 */
	public boolean nextActionIsAttack() {
		return _nextActionIsAttack;
	}
	
	public int getCastRange() {
		return _castRange;
	}
	
	public int getEffectRange() {
		return _effectRange;
	}
	
	public int getHpConsume() {
		return _hpConsume;
	}
	
	@Override
	public int getId() {
		return _id;
	}
	
	/**
	 * Verify if this skill is a debuff.
	 * @return {@code true} if this skill is a debuff, {@code false} otherwise
	 */
	public boolean isDebuff() {
		return _isDebuff;
	}
	
	/**
	 * Verify if this skill is coming from Recovery Herb.
	 * @return {@code true} if this skill is a recover herb, {@code false} otherwise
	 */
	public boolean isRecoveryHerb() {
		return _isRecoveryHerb;
	}
	
	public int getDisplayId() {
		return _displayId;
	}
	
	public int getDisplayLevel() {
		return _displayLevel;
	}
	
	/**
	 * Return skill basicProperty base stat (STR, INT ...).
	 * @return
	 */
	public BaseStats getBasicProperty() {
		return _basicProperty;
	}
	
	/**
	 * @return Returns the how much items will be consumed.
	 */
	public int getItemConsumeCount() {
		return _itemConsumeCount;
	}
	
	/**
	 * @return Returns the ID of item for consume.
	 */
	public int getItemConsumeId() {
		return _itemConsumeId;
	}
	
	public int getLevel() {
		return _level;
	}
	
	public boolean isPhysical() {
		return _magic == 0;
	}
	
	public boolean isMagic() {
		return _magic == 1;
	}
	
	public boolean isStatic() {
		return _magic == 2;
	}
	
	public boolean isDance() {
		return _magic == 3;
	}
	
	public boolean isTrigger() {
		return _magic == 4;
	}
	
	public boolean isReuseDelayLocked() {
		return _reuseDelayLock;
	}
	
	public int getMpConsume1() {
		return _mpConsume1;
	}
	
	public int getMpConsume2() {
		return _mpConsume2;
	}
	
	public int getMpPerChanneling() {
		return _mpPerChanneling;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getReuseDelay() {
		return _reuseDelay;
	}
	
	public int getReuseHashCode() {
		return _reuseHashCode;
	}
	
	public int getHitTime() {
		return hitTime;
	}
	
	public int getHitCancelTime() {
		return hitCancelTime;
	}
	
	public int getCoolTime() {
		return _coolTime;
	}
	
	public int getAffectLimit() {
		if (affectLimit[1] == 0) {
			return 0;
		}
		return affectLimit[0] + Rnd.get(affectLimit[1]);
	}
	
	public AffectObject getAffectObject() {
		return affectObject;
	}
	
	public int getAffectRange() {
		return affectRange;
	}
	
	public AffectScope getAffectScope() {
		return affectScope;
	}
	
	public int[] getFanRange() {
		return fanRange;
	}
	
	public boolean isActive() {
		return (_operateType != null) && _operateType.isActive();
	}
	
	public boolean isPassive() {
		return (_operateType != null) && _operateType.isPassive();
	}
	
	public boolean isToggle() {
		return (_operateType != null) && _operateType.isToggle();
	}
	
	public boolean isContinuous() {
		return ((_operateType != null) && _operateType.isContinuous()) || isSelfContinuous();
	}
	
	public boolean isSelfContinuous() {
		return (_operateType != null) && _operateType.isSelfContinuous();
	}
	
	public boolean isChanneling() {
		return (_operateType != null) && _operateType.isChanneling();
	}
	
	public boolean isFlyType() {
		return (_operateType != null) && _operateType.isFlyType();
	}
	
	/**
	 * Verify if the skill is a transformation skill.
	 * @return {@code true} if the skill is a transformation, {@code false} otherwise
	 */
	public boolean isTransformation() {
		return _abnormalType == AbnormalType.TRANSFORM;
	}
	
	public int getEffectPoint() {
		return _effectPoint;
	}
	
	public boolean useSoulShot() {
		return hasEffectType(L2EffectType.PHYSICAL_ATTACK);
	}
	
	public boolean useSpiritShot() {
		return _magic == 1;
	}
	
	public boolean useFishShot() {
		return hasEffectType(L2EffectType.FISHING);
	}
	
	public int getMinPledgeClass() {
		return _minPledgeClass;
	}
	
	public boolean isHeroSkill() {
		return SkillTreesData.getInstance().isHeroSkill(_id, _level);
	}
	
	public boolean isGMSkill() {
		return SkillTreesData.getInstance().isGMSkill(_id, _level);
	}
	
	public boolean is7Signs() {
		return (_id > 4360) && (_id < 4367);
	}
	
	/**
	 * Verify if this is a healing potion skill.
	 * @return {@code true} if this is a healing potion skill, {@code false} otherwise
	 */
	public boolean isHealingPotionSkill() {
		return getAbnormalType() == AbnormalType.HP_RECOVER;
	}
	
	public int getChargeConsume() {
		return _chargeConsume;
	}
	
	public int getMaxSoulConsumeCount() {
		return _soulMaxConsume;
	}
	
	public boolean isStayAfterDeath() {
		return _stayAfterDeath;
	}
	
	public boolean isStayOnSubclassChange() {
		return _stayOnSubclassChange;
	}
	
	public boolean isBad() {
		return (_effectPoint < 0) && (targetType != TargetType.SELF);
	}
	
	public boolean checkCondition(L2Character activeChar, L2Object object, boolean itemOrWeapon) {
		if (activeChar.canOverrideCond(PcCondOverride.SKILL_CONDITIONS) && !general().gmSkillRestriction()) {
			return true;
		}
		
		if (activeChar.isPlayer() && !canBeUseWhileRiding((L2PcInstance) activeChar)) {
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(_id);
			activeChar.sendPacket(sm);
			return false;
		}
		
		final List<Condition> preCondition = itemOrWeapon ? _itemPreCondition : _preCondition;
		if ((preCondition == null) || preCondition.isEmpty()) {
			return true;
		}
		
		final L2Character target = (object instanceof L2Character) ? (L2Character) object : null;
		for (Condition cond : preCondition) {
			if (!cond.test(activeChar, target, this)) {
				final String msg = cond.getMessage();
				final int msgId = cond.getMessageId();
				if (msgId != 0) {
					final SystemMessage sm = SystemMessage.getSystemMessage(msgId);
					if (cond.isAddName()) {
						sm.addSkillName(_id);
					}
					activeChar.sendPacket(sm);
				} else if (msg != null) {
					activeChar.sendMessage(msg);
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if a player can use this skill while riding.
	 * @param player the player
	 * @return {@code true} if the player can use this skill, {@code false} otherwise
	 */
	public boolean canBeUseWhileRiding(final L2PcInstance player) {
		return (_rideState == null) || _rideState.contains(player.getMountType());
	}
	
	public List<L2Object> getTargets(L2Character creature) {
		return getTargets(creature, creature.getTarget());
	}
	
	public List<L2Object> getTargets(L2Character creature, L2Object target) {
		return targetType.getTargets(this, creature, target);
	}
	
	/**
	 * Check if should be target added to the target list false if target is dead, target same as caster,<br>
	 * target inside peace zone, target in the same party with caster, caster can see target Additional checks if not in PvP zones (arena, siege):<br>
	 * target in not the same clan and alliance with caster, and usual skill PvP check. If TvT event is active - performing additional checks. Caution: distance is not checked.
	 * @param caster
	 * @param target
	 * @param skill
	 * @param sourceInArena
	 * @return
	 */
	@Deprecated
	public static boolean checkForAreaOffensiveSkills(L2Character caster, L2Character target, Skill skill, boolean sourceInArena) {
		if ((target == null) || target.isDead() || (target == caster)) {
			return false;
		}
		
		final L2PcInstance player = caster.getActingPlayer();
		final L2PcInstance targetPlayer = target.getActingPlayer();
		if (player != null) {
			if (targetPlayer != null) {
				if ((targetPlayer == caster) || (targetPlayer == player)) {
					return false;
				}
				
				if (targetPlayer.inObserverMode()) {
					return false;
				}
				
				if (skill.isBad() && (player.getSiegeState() > 0) && player.isInsideZone(ZoneId.SIEGE) && (player.getSiegeState() == targetPlayer.getSiegeState()) && (player.getSiegeSide() == targetPlayer.getSiegeSide())) {
					return false;
				}
				
				if (skill.isBad() && target.isInsideZone(ZoneId.PEACE)) {
					return false;
				}
				
				if (player.isInParty() && targetPlayer.isInParty()) {
					// Same party
					if (player.getParty().getLeaderObjectId() == targetPlayer.getParty().getLeaderObjectId()) {
						return false;
					}
					
					// Same command channel
					if (player.getParty().isInCommandChannel() && (player.getParty().getCommandChannel() == targetPlayer.getParty().getCommandChannel())) {
						return false;
					}
				}
				
				if (!TvTEvent.checkForTvTSkill(player, targetPlayer, skill)) {
					return false;
				}
				
				if (!sourceInArena && !(targetPlayer.isInsideZone(ZoneId.PVP) && !targetPlayer.isInsideZone(ZoneId.SIEGE))) {
					if ((player.getAllyId() != 0) && (player.getAllyId() == targetPlayer.getAllyId())) {
						return false;
					}
					
					if ((player.getClanId() != 0) && (player.getClanId() == targetPlayer.getClanId())) {
						return false;
					}
					
					if (!player.checkPvpSkill(targetPlayer, skill)) {
						return false;
					}
				}
			}
		} else {
			// target is mob
			if ((targetPlayer == null) && (target instanceof L2Attackable) && (caster instanceof L2Attackable)) {
				return false;
			}
		}
		return GeoData.getInstance().canSeeTarget(caster, target);
	}
	
	public List<AbstractFunction> getStatFuncs(AbstractEffect effect, L2Character player) {
		if (_funcTemplates == null) {
			return Collections.emptyList();
		}
		
		if (!(player instanceof L2Playable) && !(player instanceof L2Attackable)) {
			return Collections.emptyList();
		}
		
		final List<AbstractFunction> funcs = new ArrayList<>(_funcTemplates.size());
		for (FuncTemplate t : _funcTemplates) {
			AbstractFunction f = t.getFunc(player, null, this, this); // skill is owner
			if (f != null) {
				funcs.add(f);
			}
		}
		return funcs;
	}
	
	/**
	 * Gets the skill effects.
	 * @param effectScope the effect scope
	 * @return the list of effects for the give scope
	 */
	public List<AbstractEffect> getEffects(EffectScope effectScope) {
		return _effectLists.get(effectScope);
	}
	
	/**
	 * Verify if this skill has effects for the given scope.
	 * @param effectScope the effect scope
	 * @return {@code true} if this skill has effects for the given scope, {@code false} otherwise
	 */
	public boolean hasEffects(EffectScope effectScope) {
		List<AbstractEffect> effects = _effectLists.get(effectScope);
		return (effects != null) && !effects.isEmpty();
	}
	
	/**
	 * Applies the effects from this skill to the target for the given effect scope.
	 * @param effectScope the effect scope
	 * @param info the buff info
	 * @param applyInstantEffects if {@code true} instant effects will be applied to the effected
	 * @param addContinuousEffects if {@code true} continuous effects will be applied to the effected
	 */
	public void applyEffectScope(EffectScope effectScope, BuffInfo info, boolean applyInstantEffects, boolean addContinuousEffects) {
		if ((effectScope != null) && hasEffects(effectScope)) {
			for (AbstractEffect effect : getEffects(effectScope)) {
				if (effect != null) {
					if (effect.isInstant()) {
						if (applyInstantEffects && effect.calcSuccess(info)) {
							effect.onStart(info);
						}
					} else if (addContinuousEffects) {
						if (effect.canStart(info)) {
							info.addEffect(effect);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method overload for {@link Skill#applyEffects(L2Character, L2Character, boolean, boolean, boolean, int)}.<br>
	 * Simplify the calls.
	 * @param effector the caster of the skill
	 * @param effected the target of the effect
	 */
	public void applyEffects(L2Character effector, L2Character effected) {
		applyEffects(effector, effected, false, false, true, 0);
	}
	
	/**
	 * Method overload for {@link Skill#applyEffects(L2Character, L2Character, boolean, boolean, boolean, int)}.<br>
	 * Simplify the calls, allowing abnormal time time customization.
	 * @param effector the caster of the skill
	 * @param effected the target of the effect
	 * @param instant if {@code true} instant effects will be applied to the effected
	 * @param abnormalTime custom abnormal time, if equal or lesser than zero will be ignored
	 */
	public void applyEffects(L2Character effector, L2Character effected, boolean instant, int abnormalTime) {
		applyEffects(effector, effected, false, false, instant, abnormalTime);
	}
	
	/**
	 * Applies the effects from this skill to the target.
	 * @param effector the caster of the skill
	 * @param effected the target of the effect
	 * @param self if {@code true} self-effects will be casted on the caster
	 * @param passive if {@code true} passive effects will be applied to the effector
	 * @param instant if {@code true} instant effects will be applied to the effected
	 * @param abnormalTime custom abnormal time, if equal or lesser than zero will be ignored
	 */
	public void applyEffects(L2Character effector, L2Character effected, boolean self, boolean passive, boolean instant, int abnormalTime) {
		// null targets cannot receive any effects.
		if (effected == null) {
			return;
		}
		// Check bad skills against target.
		if ((effector != effected) && isBad() && (effected.isInvul() || (effector.isGM() && !effector.getAccessLevel().canGiveDamage()))) {
			return;
		}
		
		if (isDebuff()) {
			if (effected.isDebuffBlocked()) {
				return;
			}
		} else {
			if (effected.isBuffBlocked() && !isBad()) {
				return;
			}
		}
		
		if (effected.isInvulAgainst(getId(), getLevel())) {
			effected.sendDebugMessage("Skill " + toString() + " has been ignored (invul against)");
			return;
		}
		
		boolean addContinuousEffects = !passive && (_operateType.isToggle() || (_operateType.isContinuous() && Formulas.calcEffectSuccess(effector, effected, this)));
		if (!self && !passive) {
			final BuffInfo info = new BuffInfo(effector, effected, this);
			
			if (effector.isPlayer() && (getMaxSoulConsumeCount() > 0)) {
				info.setCharges(effector.getActingPlayer().decreaseSouls(getMaxSoulConsumeCount()));
			}
			
			if (addContinuousEffects && (abnormalTime > 0)) {
				info.setAbnormalTime(abnormalTime);
			}
			
			applyEffectScope(EffectScope.GENERAL, info, instant, addContinuousEffects);
			
			EffectScope pvpOrPveEffectScope = effector.isPlayable() && effected.isAttackable() ? EffectScope.PVE : effector.isPlayable() && effected.isPlayable() ? EffectScope.PVP : null;
			applyEffectScope(pvpOrPveEffectScope, info, instant, addContinuousEffects);
			
			applyEffectScope(EffectScope.CHANNELING, info, instant, addContinuousEffects);
			
			if (addContinuousEffects) {
				effected.getEffectList().add(info);
			}
			
			// Support for buff sharing feature including healing herbs.
			if (effected.isPlayer() && effected.hasServitor() && !isTransformation() && (getAbnormalType() != AbnormalType.SUMMON_CONDITION)) {
				if ((addContinuousEffects && isContinuous() && !isDebuff()) || isRecoveryHerb()) {
					applyEffects(effector, effected.getSummon(), isRecoveryHerb(), 0);
				}
			}
		}
		
		if (self) {
			addContinuousEffects = !passive && (_operateType.isToggle() || ((_operateType.isContinuous() || _operateType.isSelfContinuous()) && Formulas.calcEffectSuccess(effector, effector, this)));
			
			final BuffInfo info = new BuffInfo(effector, effector, this);
			if (addContinuousEffects && (abnormalTime > 0)) {
				info.setAbnormalTime(abnormalTime);
			}
			
			applyEffectScope(EffectScope.SELF, info, instant, addContinuousEffects);
			
			// TODO : Need to be done better after skill rework
			if (addContinuousEffects && hasEffectType(L2EffectType.BUFF)) {
				info.getEffector().getEffectList().add(info);
			}
			
			// Support for buff sharing feature.
			// Avoiding Servitor Share since it's implementation already "shares" the effect.
			if (addContinuousEffects && info.getEffected().isPlayer() && info.getEffected().hasServitor() && isContinuous() && !isDebuff() && (getId() != CommonSkill.SERVITOR_SHARE.getId())) {
				applyEffects(effector, info.getEffected().getSummon(), false, 0);
			}
		}
		
		if (passive) {
			final BuffInfo info = new BuffInfo(effector, effector, this);
			applyEffectScope(EffectScope.PASSIVE, info, false, true);
			effector.getEffectList().add(info);
		}
	}
	
	public void activateSkill(L2Character caster, L2Object target) {
		activateSkill(caster, null, List.of(target));
	}
	
	/**
	 * Activates a skill for the given creature and targets.
	 * @param caster the caster
	 * @param targets the targets
	 */
	public void activateSkill(L2Character caster, List<L2Object> targets) {
		activateSkill(caster, null, targets);
	}
	
	/**
	 * Activates a skill for the given cubic and targets.
	 * @param cubic the cubic
	 * @param targets the targets
	 */
	public void activateSkill(L2CubicInstance cubic, List<L2Object> targets) {
		activateSkill(cubic.getOwner(), cubic, targets);
	}
	
	/**
	 * Activates the skill to the targets.
	 * @param caster the caster
	 * @param cubic the cubic, can be {@code null}
	 * @param targets the targets
	 */
	private void activateSkill(L2Character caster, L2CubicInstance cubic, List<L2Object> targets) {
		switch (getId()) {
			// TODO: replace with AI
			case 5852:
			case 5853: {
				final L2BlockInstance block = targets.get(0) instanceof L2BlockInstance ? (L2BlockInstance) targets.get(0) : null;
				final L2PcInstance player = caster.isPlayer() ? (L2PcInstance) caster : null;
				if ((block == null) || (player == null)) {
					return;
				}
				
				final int arena = player.getBlockCheckerArena();
				if (arena != -1) {
					final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
					if (holder == null) {
						return;
					}
					
					final int team = holder.getPlayerTeam(player);
					final int color = block.getColorEffect();
					if ((team == 0) && (color == 0x00)) {
						block.changeColor(player, holder, team);
					} else if ((team == 1) && (color == 0x53)) {
						block.changeColor(player, holder, team);
					}
				}
				break;
			}
			default: {
				for (L2Object obj : targets) {
					final L2Character target = (L2Character) obj;
					if (Formulas.calcBuffDebuffReflection(target, this)) {
						// if skill is reflected instant effects should be casted on target
						// and continuous effects on caster
						applyEffects(target, caster, false, 0);
						
						final BuffInfo info = new BuffInfo(caster, target, this);
						applyEffectScope(EffectScope.GENERAL, info, true, false);
						
						EffectScope pvpOrPveEffectScope = caster.isPlayable() && target.isAttackable() ? EffectScope.PVE : caster.isPlayable() && target.isPlayable() ? EffectScope.PVP : null;
						applyEffectScope(pvpOrPveEffectScope, info, true, false);
						
						applyEffectScope(EffectScope.CHANNELING, info, true, false);
					} else {
						applyEffects(caster, target);
					}
				}
				break;
			}
		}
		
		// Self Effect
		if (hasEffects(EffectScope.SELF)) {
			if (caster.isAffectedBySkill(getId())) {
				caster.stopSkillEffects(true, getId());
			}
			applyEffects(caster, caster, true, false, true, 0);
		}
		
		if (cubic == null) {
			if (useSpiritShot()) {
				caster.setChargedShot(caster.isChargedShot(ShotType.BLESSED_SPIRITSHOTS) ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
			} else if (useSoulShot()) {
				caster.setChargedShot(ShotType.SOULSHOTS, false);
			}
		}
		
		if (isSuicideAttack()) {
			caster.doDie(caster);
		}
	}
	
	public void attach(FuncTemplate f) {
		if (_funcTemplates == null) {
			_funcTemplates = new ArrayList<>(1);
		}
		_funcTemplates.add(f);
	}
	
	/**
	 * Adds an effect to the effect list for the give effect scope.
	 * @param effectScope the effect scope
	 * @param effect the effect to add
	 */
	public void addEffect(EffectScope effectScope, AbstractEffect effect) {
		final var effects = _effectLists.computeIfAbsent(effectScope, k -> new ArrayList<>(1));
		effects.add(effect);
	}
	
	public void attach(Condition c, boolean itemOrWeapon) {
		if (itemOrWeapon) {
			if (_itemPreCondition == null) {
				_itemPreCondition = new ArrayList<>();
			}
			_itemPreCondition.add(c);
		} else {
			if (_preCondition == null) {
				_preCondition = new ArrayList<>();
			}
			_preCondition.add(c);
		}
	}
	
	@Override
	public String toString() {
		return "Skill " + _name + "(" + _id + "," + _level + ")";
	}
	
	/**
	 * used for tracking item id in case that item consume cannot be used
	 * @return reference item id
	 */
	public int getReferenceItemId() {
		return _refId;
	}
	
	public void setReferenceItemId(int val) {
		_refId = val;
	}
	
	/**
	 * Verify if the skill can be replaced.
	 * @return {@code true} if skill can be replaced, {@code false} otherwise
	 */
	public boolean isIrreplaceableBuff() {
		return _irreplaceableBuff;
	}
	
	/**
	 * Verify if the skill can be stolen.
	 * @return {@code true} if skill can be stolen, {@code false} otherwise
	 */
	public boolean canBeStolen() {
		return !isPassive() && !isToggle() && !isDebuff() && !isHeroSkill() && !isGMSkill() && !isIrreplaceableBuff() && !(isStatic() && (getId() != CommonSkill.CARAVANS_SECRET_MEDICINE.getId()) && (getMagicLevel() >= 0));
	}
	
	public boolean isClanSkill() {
		return SkillTreesData.getInstance().isClanSkill(_id, _level);
	}
	
	public boolean isExcludedFromCheck() {
		return _excludedFromCheck;
	}
	
	public boolean isSimultaneousCast() {
		return _simultaneousCast;
	}
	
	/**
	 * Parse an extractable skill.
	 * @param skillId the skill Id
	 * @param skillLvl the skill level
	 * @param values the values to parse
	 * @return the parsed extractable skill
	 * @author Zoey76
	 */
	private L2ExtractableSkill parseExtractableSkill(int skillId, int skillLvl, String values) {
		final String[] prodLists = values.split(";");
		final List<L2ExtractableProductItem> products = new ArrayList<>();
		String[] prodData;
		for (String prodList : prodLists) {
			prodData = prodList.split(",");
			if (prodData.length < 3) {
				_log.warning("Extractable skills data: Error in Skill Id: " + skillId + " Level: " + skillLvl + " -> wrong separator!");
			}
			List<ItemHolder> items = null;
			double chance = 0;
			final int length = prodData.length - 1;
			try {
				items = new ArrayList<>(length / 2);
				for (int j = 0; j < length; j += 2) {
					final int prodId = Integer.parseInt(prodData[j]);
					final int quantity = Integer.parseInt(prodData[j + 1]);
					if ((prodId <= 0) || (quantity <= 0)) {
						_log.warning("Extractable skills data: Error in Skill Id: " + skillId + " Level: " + skillLvl + " wrong production Id: " + prodId + " or wrong quantity: " + quantity + "!");
					}
					items.add(new ItemHolder(prodId, quantity));
				}
				chance = Double.parseDouble(prodData[length]);
			} catch (Exception e) {
				_log.warning("Extractable skills data: Error in Skill Id: " + skillId + " Level: " + skillLvl + " -> incomplete/invalid production data or wrong separator!");
			}
			products.add(new L2ExtractableProductItem(items, chance));
		}
		
		if (products.isEmpty()) {
			_log.warning("Extractable skills data: Error in Skill Id: " + skillId + " Level: " + skillLvl + " -> There are no production items!");
		}
		return new L2ExtractableSkill(SkillData.getSkillHashCode(skillId, skillLvl), products);
	}
	
	/**
	 * Parses all the abnormal visual effects.
	 * @param abnormalVisualEffects the abnormal visual effects list
	 */
	private void parseAbnormalVisualEffect(String abnormalVisualEffects) {
		if (abnormalVisualEffects != null) {
			final String[] data = abnormalVisualEffects.split(";");
			List<AbnormalVisualEffect> avesEvent = null;
			List<AbnormalVisualEffect> avesSpecial = null;
			List<AbnormalVisualEffect> aves = null;
			for (String ave2 : data) {
				final AbnormalVisualEffect ave = AbnormalVisualEffect.valueOf(ave2);
				if (ave.isEvent()) {
					if (avesEvent == null) {
						avesEvent = new ArrayList<>(1);
					}
					avesEvent.add(ave);
					continue;
				}
				
				if (ave.isSpecial()) {
					if (avesSpecial == null) {
						avesSpecial = new ArrayList<>(1);
					}
					avesSpecial.add(ave);
					continue;
				}
				
				if (aves == null) {
					aves = new ArrayList<>(1);
				}
				aves.add(ave);
			}
			
			if (avesEvent != null) {
				_abnormalVisualEffectsEvent = avesEvent.toArray(new AbnormalVisualEffect[avesEvent.size()]);
			}
			
			if (avesSpecial != null) {
				_abnormalVisualEffectsSpecial = avesSpecial.toArray(new AbnormalVisualEffect[avesSpecial.size()]);
			}
			
			if (aves != null) {
				_abnormalVisualEffects = aves.toArray(new AbnormalVisualEffect[aves.size()]);
			}
		}
	}
	
	public L2ExtractableSkill getExtractableSkill() {
		return _extractableItems;
	}
	
	/**
	 * @param effectType Effect type to check if its present on this skill effects.
	 * @param effectTypes Effect types to check if are present on this skill effects.
	 * @return {@code true} if at least one of specified {@link L2EffectType} types is present on this skill effects, {@code false} otherwise.
	 */
	public boolean hasEffectType(L2EffectType effectType, L2EffectType... effectTypes) {
		if (_effectTypes == null) {
			synchronized (this) {
				if (_effectTypes == null) {
					Set<Byte> effectTypesSet = new HashSet<>();
					for (List<AbstractEffect> effectList : _effectLists.values()) {
						if (effectList != null) {
							for (AbstractEffect effect : effectList) {
								if (effect == null) {
									continue;
								}
								effectTypesSet.add((byte) effect.getEffectType().ordinal());
							}
						}
					}
					
					Byte[] effectTypesArray = effectTypesSet.toArray(new Byte[effectTypesSet.size()]);
					Arrays.sort(effectTypesArray);
					_effectTypes = effectTypesArray;
				}
			}
		}
		
		if (Arrays.binarySearch(_effectTypes, (byte) effectType.ordinal()) >= 0) {
			return true;
		}
		
		for (L2EffectType type : effectTypes) {
			if (Arrays.binarySearch(_effectTypes, (byte) type.ordinal()) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return icon of the current skill.
	 */
	public String getIcon() {
		return _icon;
	}
	
	public int getChannelingTickInterval() {
		return _channelingTickInterval;
	}
	
	public int getChannelingTickInitialDelay() {
		return _channelingTickInitialDelay;
	}
	
	public Set<MountType> getRideState() {
		return _rideState;
	}
}