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
package com.l2jserver.gameserver.model.items.instance;

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.config.Configuration.general;
import static com.l2jserver.gameserver.config.Configuration.olympiad;
import static com.l2jserver.gameserver.model.itemcontainer.Inventory.ADENA_ID;
import static com.l2jserver.gameserver.model.items.type.EtcItemType.ARROW;
import static com.l2jserver.gameserver.model.items.type.EtcItemType.SHOT;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.agathion.repository.AgathionRepository;
import com.l2jserver.gameserver.data.xml.impl.EnchantItemOptionsData;
import com.l2jserver.gameserver.data.xml.impl.OptionData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.enums.InstanceType;
import com.l2jserver.gameserver.enums.ItemLocation;
import com.l2jserver.gameserver.enums.ShotType;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.instancemanager.ItemsOnGroundManager;
import com.l2jserver.gameserver.instancemanager.MercTicketManager;
import com.l2jserver.gameserver.model.DropProtection;
import com.l2jserver.gameserver.model.Elementals;
import com.l2jserver.gameserver.model.L2Augmentation;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.L2WorldRegion;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.knownlist.NullKnownList;
import com.l2jserver.gameserver.model.events.EventDispatcher;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerAugment;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.OnPlayerItemDrop;
import com.l2jserver.gameserver.model.events.impl.character.player.inventory.OnPlayerItemPickup;
import com.l2jserver.gameserver.model.events.impl.item.OnItemBypassEvent;
import com.l2jserver.gameserver.model.events.impl.item.OnItemTalk;
import com.l2jserver.gameserver.model.holders.SkillHolder;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;
import com.l2jserver.gameserver.model.items.L2Armor;
import com.l2jserver.gameserver.model.items.L2EtcItem;
import com.l2jserver.gameserver.model.items.L2Item;
import com.l2jserver.gameserver.model.items.L2Weapon;
import com.l2jserver.gameserver.model.items.type.EtcItemType;
import com.l2jserver.gameserver.model.items.type.ItemType;
import com.l2jserver.gameserver.model.items.type.ItemType1;
import com.l2jserver.gameserver.model.items.type.ItemType2;
import com.l2jserver.gameserver.model.options.EnchantOptions;
import com.l2jserver.gameserver.model.options.Options;
import com.l2jserver.gameserver.model.stats.functions.AbstractFunction;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.DropItem;
import com.l2jserver.gameserver.network.serverpackets.GetItem;
import com.l2jserver.gameserver.network.serverpackets.InventoryUpdate;
import com.l2jserver.gameserver.network.serverpackets.SpawnItem;
import com.l2jserver.gameserver.network.serverpackets.StatusUpdate;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.util.GMAudit;

public final class L2ItemInstance extends L2Object {
	
	private static final Logger LOG = LoggerFactory.getLogger(L2ItemInstance.class);
	
	private static final Logger LOG_ITEM = LoggerFactory.getLogger("item");
	
	/** ID of the owner */
	private int _ownerId;
	
	/** ID of who dropped the item last, used for knownlist */
	private int _dropperObjectId = 0;
	
	/** Quantity of the item */
	private long _count;
	
	/** Initial Quantity of the item */
	private long _initCount;
	
	/** Remaining time (in milliseconds) */
	private long _time;
	
	/** Quantity of the item can decrease */
	private boolean _decrease = false;
	
	/** ID of the item */
	private final int _itemId;
	
	/** Object L2Item associated to the item */
	private final L2Item _item;
	
	/** Location of the item : Inventory, PaperDoll, WareHouse */
	private ItemLocation _loc;
	
	/** Slot where item is stored : Paperdoll slot, inventory order ... */
	private int _locData;
	
	/** Level of enchantment of the item */
	private int _enchantLevel;
	
	/** Wear Item */
	private boolean _wear;
	
	/** Augmented Item */
	private L2Augmentation _augmentation = null;
	
	/** Shadow item */
	private int _mana;
	
	private boolean _consumingMana = false;
	
	private static final int MANA_CONSUMPTION_RATE = 60000;
	
	/** Custom item types (used loto, race tickets) */
	private int _type1;
	
	private int _type2;
	
	private long _dropTime;
	
	private boolean _published = false;
	
	private boolean _protected;
	
	public static final int UNCHANGED = 0;
	
	public static final int ADDED = 1;
	
	public static final int REMOVED = 3;
	
	public static final int MODIFIED = 2;
	
	public static final int[] DEFAULT_ENCHANT_OPTIONS = new int[] {
		0,
		0,
		0
	};
	
	private int _lastChange = 2; // 1 ??, 2 modified, 3 removed
	
	private boolean _existsInDb; // if a record exists in DB.
	
	private boolean _storedInDb; // if DB data is up-to-date.
	
	private final ReentrantLock _dbLock = new ReentrantLock();
	
	private Elementals[] _elementals = null;
	
	private ScheduledFuture<?> itemLootSchedule = null;
	
	private ScheduledFuture<?> _lifeTimeTask;
	
	private final DropProtection _dropProtection = new DropProtection();
	
	private int _shotsMask = 0;
	
	private final List<Options> _enchantOptions = new ArrayList<>();
	
	private int agathionEnergy;
	
	/**
	 * Constructor of the L2ItemInstance from the objectId and the itemId.
	 * @param objectId : int designating the ID of the object in the world
	 * @param itemId : int designating the ID of the item
	 */
	public L2ItemInstance(int objectId, int itemId) {
		super(objectId);
		setInstanceType(InstanceType.L2ItemInstance);
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		if ((_itemId == 0) || (_item == null)) {
			throw new IllegalArgumentException();
		}
		super.setName(_item.getName());
		setCount(1);
		_loc = ItemLocation.VOID;
		_type1 = 0;
		_type2 = 0;
		_dropTime = 0;
		_mana = _item.getDuration();
		_time = _item.getTime() == -1 ? -1 : System.currentTimeMillis() + ((long) _item.getTime() * 60 * 1000);
		scheduleLifeTimeTask();
		final var agathionInfo = AgathionRepository.getInstance().getByItemId(itemId);
		agathionEnergy = agathionInfo == null ? 0 : agathionInfo.getMaxEnergy();
	}
	
	/**
	 * Constructor of the L2ItemInstance from the objetId and the description of the item given by the L2Item.
	 * @param objectId : int designating the ID of the object in the world
	 * @param item : L2Item containing information of the item
	 */
	public L2ItemInstance(int objectId, L2Item item) {
		super(objectId);
		setInstanceType(InstanceType.L2ItemInstance);
		_itemId = item.getId();
		_item = item;
		if (_itemId == 0) {
			throw new IllegalArgumentException();
		}
		super.setName(_item.getName());
		setCount(1);
		_loc = ItemLocation.VOID;
		_mana = _item.getDuration();
		_time = _item.getTime() == -1 ? -1 : System.currentTimeMillis() + ((long) _item.getTime() * 60 * 1000);
		scheduleLifeTimeTask();
		final var agathionInfo = AgathionRepository.getInstance().getByItemId(item.getId());
		agathionEnergy = agathionInfo == null ? 0 : agathionInfo.getMaxEnergy();
	}
	
	/**
	 * Constructor overload.<br>
	 * Sets the next free object ID in the ID factory.
	 * @param itemId the item template ID
	 */
	public L2ItemInstance(int itemId) {
		this(IdFactory.getInstance().getNextId(), itemId);
	}
	
	@Override
	public void initKnownList() {
		setKnownList(new NullKnownList(this));
	}
	
	/**
	 * Remove a L2ItemInstance from the world and send server->client GetItem packets.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Send a Server->Client Packet GetItem to player that pick up and its _knowPlayers member</li>
	 * <li>Remove the L2Object from the world</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T REMOVE the object from _allObjects of L2World </B></FONT><BR>
	 * <BR>
	 * <B><U> Assert </U> :</B><BR>
	 * <BR>
	 * <li>this instanceof L2ItemInstance</li>
	 * <li>_worldRegion != null <I>(L2Object is visible at the beginning)</I></li><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Do Pickup Item : PCInstance and Pet</li><BR>
	 * <BR>
	 * @param player Player that pick up the item
	 */
	public void pickupMe(L2Character player) {
		assert getWorldRegion() != null;
		
		L2WorldRegion oldregion = getWorldRegion();
		
		// Create a server->client GetItem packet to pick up the L2ItemInstance
		player.broadcastPacket(new GetItem(this, player.getObjectId()));
		
		synchronized (this) {
			setIsVisible(false);
			setWorldRegion(null);
		}
		
		// if this item is a mercenary ticket, remove the spawns!
		int itemId = getId();
		
		if (MercTicketManager.getInstance().getTicketCastleId(itemId) > 0) {
			MercTicketManager.getInstance().removeTicket(this);
			ItemsOnGroundManager.getInstance().removeObject(this);
		}
		
		// outside of synchronized to avoid deadlocks
		// Remove the L2ItemInstance from the world
		L2World.getInstance().removeVisibleObject(this, oldregion);
		
		if (player.isPlayer()) {
			// Notify to scripts
			EventDispatcher.getInstance().notifyEventAsync(new OnPlayerItemPickup(player.getActingPlayer(), this), player, getItem());
		}
	}
	
	/**
	 * Sets the ownerID of the item
	 * @param process : String Identifier of process triggering this action
	 * @param owner_id : int designating the ID of the owner
	 * @param creator : L2PcInstance Player requesting the item creation
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void setOwnerId(String process, int owner_id, L2PcInstance creator, Object reference) {
		setOwnerId(owner_id);
		
		if (general().logItems()) {
			if (!general().logItemsSmallLog() || (general().logItemsSmallLog() && (getItem().isEquipable() || (getItem().getId() == ADENA_ID)))) {
				if ((getItemType() != ARROW) && (getItemType() != SHOT)) {
					LOG_ITEM.info("SET_OWNER {} by {}, referenced by {}.", this, creator, reference);
				}
			}
		}
		
		if (creator != null) {
			if (creator.isGM()) {
				String referenceName = "no-reference";
				if (reference instanceof L2Object) {
					referenceName = (((L2Object) reference).getName() != null ? ((L2Object) reference).getName() : "no-name");
				} else if (reference instanceof String) {
					referenceName = (String) reference;
				}
				String targetName = (creator.getTarget() != null ? creator.getTarget().getName() : "no-target");
				if (general().gmAudit()) {
					GMAudit.auditGMAction(creator.getName() + " [" + creator.getObjectId() + "]", process + "(id: " + getId() + " name: " + getName() + ")", targetName, "L2Object referencing this action is: " + referenceName);
				}
			}
		}
	}
	
	/**
	 * Sets the ownerID of the item
	 * @param owner_id : int designating the ID of the owner
	 */
	public void setOwnerId(int owner_id) {
		if (owner_id == _ownerId) {
			return;
		}
		
		// Remove any inventory skills from the old owner.
		removeSkillsFromOwner();
		
		_ownerId = owner_id;
		_storedInDb = false;
		
		// Give any inventory skills to the new owner only if the item is in inventory
		// else the skills will be given when location is set to inventory.
		giveSkillsToOwner();
	}
	
	/**
	 * Returns the ownerID of the item
	 * @return int : ownerID of the item
	 */
	public int getOwnerId() {
		return _ownerId;
	}
	
	/**
	 * Sets the location of the item
	 * @param loc : ItemLocation (enumeration)
	 */
	public void setItemLocation(ItemLocation loc) {
		setItemLocation(loc, 0);
	}
	
	/**
	 * Sets the location of the item.<BR>
	 * <BR>
	 * <U><I>Remark :</I></U> If loc and loc_data different from database, say datas not up-to-date
	 * @param loc : ItemLocation (enumeration)
	 * @param loc_data : int designating the slot where the item is stored or the village for freights
	 */
	public void setItemLocation(ItemLocation loc, int loc_data) {
		if ((loc == _loc) && (loc_data == _locData)) {
			return;
		}
		
		// Remove any inventory skills from the old owner.
		removeSkillsFromOwner();
		
		_loc = loc;
		_locData = loc_data;
		_storedInDb = false;
		
		// Give any inventory skills to the new owner only if the item is in inventory
		// else the skills will be given when location is set to inventory.
		giveSkillsToOwner();
	}
	
	public ItemLocation getItemLocation() {
		return _loc;
	}
	
	/**
	 * Sets the quantity of the item.
	 * @param count the new count to set
	 */
	public void setCount(long count) {
		if (getCount() == count) {
			return;
		}
		
		_count = count >= -1 ? count : 0;
		_storedInDb = false;
	}
	
	public long getCount() {
		return _count;
	}
	
	/**
	 * Sets the quantity of the item.<BR>
	 * <BR>
	 * <U><I>Remark :</I></U> If loc and loc_data different from database, say datas not up-to-date
	 * @param process : String Identifier of process triggering this action
	 * @param count : int
	 * @param creator : L2PcInstance Player requesting the item creation
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void changeCount(String process, long count, L2PcInstance creator, Object reference) {
		if (count == 0) {
			return;
		}
		long old = getCount();
		long max = getId() == ADENA_ID ? character().getMaxAdena() : Integer.MAX_VALUE;
		
		if ((count > 0) && (getCount() > (max - count))) {
			setCount(max);
		} else {
			setCount(getCount() + count);
		}
		
		if (getCount() < 0) {
			setCount(0);
		}
		
		_storedInDb = false;
		
		if (general().logItems() && (process != null)) {
			if (!general().logItemsSmallLog() || (general().logItemsSmallLog() && (_item.isEquipable() || (_item.getId() == ADENA_ID)))) {
				if ((getItemType() != ARROW) && (getItemType() != SHOT)) {
					LOG_ITEM.info("CHANGED {} amount {} by {}, referenced by {}.", this, old, creator, reference);
				}
			}
		}
		
		if (creator != null) {
			if (creator.isGM()) {
				String referenceName = "no-reference";
				if (reference instanceof L2Object) {
					referenceName = (((L2Object) reference).getName() != null ? ((L2Object) reference).getName() : "no-name");
				} else if (reference instanceof String) {
					referenceName = (String) reference;
				}
				String targetName = (creator.getTarget() != null ? creator.getTarget().getName() : "no-target");
				if (general().gmAudit()) {
					GMAudit.auditGMAction(creator.getName() + " [" + creator.getObjectId() + "]", process + "(id: " + getId() + " objId: " + getObjectId() + " name: " + getName() + " count: " + count + ")", targetName, "L2Object referencing this action is: " + referenceName);
				}
			}
		}
	}
	
	// No logging (function designed for shots only)
	public void changeCountWithoutTrace(int count, L2PcInstance creator, Object reference) {
		changeCount(null, count, creator, reference);
	}
	
	/**
	 * Return true if item can be enchanted
	 * @return boolean
	 */
	public int isEnchantable() {
		if ((getItemLocation() == ItemLocation.INVENTORY) || (getItemLocation() == ItemLocation.PAPERDOLL)) {
			return getItem().isEnchantable();
		}
		return 0;
	}
	
	/**
	 * Returns if item is equipable
	 * @return boolean
	 */
	public boolean isEquipable() {
		return !((_item.getBodyPart() == 0) || (_item.getItemType() == EtcItemType.ARROW) || (_item.getItemType() == EtcItemType.BOLT) || (_item.getItemType() == EtcItemType.LURE));
	}
	
	/**
	 * Returns if item is equipped
	 * @return boolean
	 */
	public boolean isEquipped() {
		return (_loc == ItemLocation.PAPERDOLL) || (_loc == ItemLocation.PET_EQUIP);
	}
	
	/**
	 * Returns the slot where the item is stored
	 * @return int
	 */
	public int getLocationSlot() {
		assert (_loc == ItemLocation.PAPERDOLL) || (_loc == ItemLocation.PET_EQUIP) || (_loc == ItemLocation.INVENTORY) || (_loc == ItemLocation.MAIL) || (_loc == ItemLocation.FREIGHT);
		return _locData;
	}
	
	/**
	 * Returns the characteristics of the item
	 * @return L2Item
	 */
	public L2Item getItem() {
		return _item;
	}
	
	public int getCustomType1() {
		return _type1;
	}
	
	public int getCustomType2() {
		return _type2;
	}
	
	public void setCustomType1(int newtype) {
		_type1 = newtype;
	}
	
	public void setCustomType2(int newtype) {
		_type2 = newtype;
	}
	
	public void setDropTime(long time) {
		_dropTime = time;
	}
	
	public long getDropTime() {
		return _dropTime;
	}
	
	/**
	 * @return the type of item.
	 */
	public ItemType getItemType() {
		return _item.getItemType();
	}
	
	/**
	 * Gets the item ID.
	 * @return the item ID
	 */
	@Override
	public int getId() {
		return _itemId;
	}
	
	/**
	 * @return the display Id of the item.
	 */
	public int getDisplayId() {
		return getItem().getDisplayId();
	}
	
	/**
	 * @return {@code true} if item is an EtcItem, {@code false} otherwise.
	 */
	public boolean isEtcItem() {
		return (_item instanceof L2EtcItem);
	}
	
	/**
	 * @return {@code true} if item is a Weapon/Shield, {@code false} otherwise.
	 */
	public boolean isWeapon() {
		return (_item instanceof L2Weapon);
	}
	
	/**
	 * @return {@code true} if item is an Armor, {@code false} otherwise.
	 */
	public boolean isArmor() {
		return (_item instanceof L2Armor);
	}
	
	/**
	 * @return the characteristics of the L2EtcItem, {@code false} otherwise.
	 */
	public L2EtcItem getEtcItem() {
		if (_item instanceof L2EtcItem) {
			return (L2EtcItem) _item;
		}
		return null;
	}
	
	/**
	 * @return the characteristics of the L2Weapon.
	 */
	public L2Weapon getWeaponItem() {
		if (_item instanceof L2Weapon) {
			return (L2Weapon) _item;
		}
		return null;
	}
	
	/**
	 * @return the characteristics of the L2Armor.
	 */
	public L2Armor getArmorItem() {
		if (_item instanceof L2Armor) {
			return (L2Armor) _item;
		}
		return null;
	}
	
	/**
	 * @return the quantity of crystals for crystallization.
	 */
	public int getCrystalCount() {
		return _item.getCrystalCount(_enchantLevel);
	}
	
	/**
	 * @return the reference price of the item.
	 */
	public int getReferencePrice() {
		return _item.getReferencePrice();
	}
	
	/**
	 * @return the name of the item.
	 */
	public String getItemName() {
		return _item.getName();
	}
	
	/**
	 * @return the reuse delay of this item.
	 */
	public int getReuseDelay() {
		return _item.getReuseDelay();
	}
	
	/**
	 * @return the shared reuse item group.
	 */
	public int getSharedReuseGroup() {
		return _item.getSharedReuseGroup();
	}
	
	/**
	 * @return the last change of the item
	 */
	public int getLastChange() {
		return _lastChange;
	}
	
	/**
	 * Sets the last change of the item
	 * @param lastChange : int
	 */
	public void setLastChange(int lastChange) {
		_lastChange = lastChange;
	}
	
	/**
	 * Returns if item is stackable
	 * @return boolean
	 */
	public boolean isStackable() {
		return _item.isStackable();
	}
	
	/**
	 * Returns if item is droppable
	 * @return boolean
	 */
	public boolean isDroppable() {
		return !isAugmented() && _item.isDroppable();
	}
	
	/**
	 * Returns if item is destroyable
	 * @return boolean
	 */
	public boolean isDestroyable() {
		return _item.isDestroyable();
	}
	
	/**
	 * Returns if item is tradeable
	 * @return boolean
	 */
	public boolean isTradeable() {
		return !isAugmented() && _item.isTradeable();
	}
	
	/**
	 * Returns if item is sellable
	 * @return boolean
	 */
	public boolean isSellable() {
		return !isAugmented() && _item.isSellable();
	}
	
	/**
	 * @param isPrivateWareHouse
	 * @return if item can be deposited in warehouse or freight
	 */
	public boolean isDepositable(boolean isPrivateWareHouse) {
		// equipped, hero and quest items
		if (isEquipped() || !_item.isDepositable()) {
			return false;
		}
		if (!isPrivateWareHouse) {
			// augmented not tradeable
			return isTradeable() && !isShadowItem();
		}
		return true;
	}
	
	public boolean isPotion() {
		return _item.isPotion();
	}
	
	public boolean isElixir() {
		return _item.isElixir();
	}
	
	public boolean isScroll() {
		return _item.isScroll();
	}
	
	public boolean isHeroItem() {
		return _item.isHeroItem();
	}
	
	public boolean isCommonItem() {
		return _item.isCommon();
	}
	
	/**
	 * Returns whether this item is pvp or not
	 * @return boolean
	 */
	public boolean isPvp() {
		return _item.isPvpItem();
	}
	
	public boolean isOlyRestrictedItem() {
		return getItem().isOlyRestrictedItem();
	}
	
	/**
	 * @param player
	 * @param allowAdena
	 * @param allowNonTradeable
	 * @return if item is available for manipulation
	 */
	public boolean isAvailable(L2PcInstance player, boolean allowAdena, boolean allowNonTradeable) {
		return ((!isEquipped()) // Not equipped
			&& (getItem().getType2() != ItemType2.QUEST) // Not Quest Item
			&& ((getItem().getType2() != ItemType2.MONEY) || (getItem().getType1() != ItemType1.SHIELD_ARMOR)) // not money, not shield
			&& (!player.hasSummon() || (getObjectId() != player.getSummon().getControlObjectId())) // Not Control item of currently summoned pet
			&& (player.getActiveEnchantItemId() != getObjectId()) // Not momentarily used enchant scroll
			&& (player.getActiveEnchantSupportItemId() != getObjectId()) // Not momentarily used enchant support item
			&& (player.getActiveEnchantAttrItemId() != getObjectId()) // Not momentarily used enchant attribute item
			&& (allowAdena || (getId() != Inventory.ADENA_ID)) // Not Adena
			&& ((player.getCurrentSkill() == null) || (player.getCurrentSkill().getSkill().getItemConsumeId() != getId())) && (!player.isCastingSimultaneouslyNow() || (player.getLastSimultaneousSkillCast() == null) || (player.getLastSimultaneousSkillCast().getItemConsumeId() != getId()))
			&& (allowNonTradeable || (isTradeable() && (!((getItem().getItemType() == EtcItemType.PET_COLLAR) && player.havePetInvItems())))));
	}
	
	/**
	 * Returns the level of enchantment of the item
	 * @return int
	 */
	public int getEnchantLevel() {
		return _enchantLevel;
	}
	
	/**
	 * @param enchantLevel the enchant value to set
	 */
	public void setEnchantLevel(int enchantLevel) {
		if (_enchantLevel == enchantLevel) {
			return;
		}
		clearEnchantStats();
		_enchantLevel = enchantLevel;
		applyEnchantStats();
		_storedInDb = false;
	}
	
	/**
	 * Returns whether this item is augmented or not
	 * @return true if augmented
	 */
	public boolean isAugmented() {
		return _augmentation != null;
	}
	
	/**
	 * Returns the augmentation object for this item
	 * @return augmentation
	 */
	public L2Augmentation getAugmentation() {
		return _augmentation;
	}
	
	/**
	 * Sets a new augmentation
	 * @param augmentation
	 * @return return true if successful
	 */
	public boolean setAugmentation(L2Augmentation augmentation) {
		// there shall be no previous augmentation..
		if (_augmentation != null) {
			LOG.warn("Augment set for {} by owner {}!", this, getActingPlayer());
			return false;
		}
		
		_augmentation = augmentation;
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			updateItemAttributes(con);
		} catch (Exception ex) {
			LOG.warn("Could not update atributes for item {} from database!", this, ex);
		}
		
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerAugment(getActingPlayer(), this, augmentation, true), getItem());
		return true;
	}
	
	public void removeAugmentation() {
		if (_augmentation == null) {
			return;
		}
		
		// Copy augmentation before removing it.
		final L2Augmentation augment = _augmentation;
		_augmentation = null;
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("DELETE FROM item_attributes WHERE itemId = ?")) {
			ps.setInt(1, getObjectId());
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Could not remove augmentation for item {} from database!", this, ex);
		}
		
		// Notify to scripts.
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerAugment(getActingPlayer(), this, augment, false), getItem());
	}
	
	public void restoreAttributes() {
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps1 = con.prepareStatement("SELECT augAttributes FROM item_attributes WHERE itemId=?");
			var ps2 = con.prepareStatement("SELECT elemType,elemValue FROM item_elementals WHERE itemId=?")) {
			ps1.setInt(1, getObjectId());
			try (var rs = ps1.executeQuery()) {
				if (rs.next()) {
					int aug_attributes = rs.getInt(1);
					if (aug_attributes != -1) {
						_augmentation = new L2Augmentation(rs.getInt("augAttributes"));
					}
				}
			}
			
			ps2.setInt(1, getObjectId());
			try (var rs = ps2.executeQuery()) {
				while (rs.next()) {
					byte elem_type = rs.getByte(1);
					int elem_value = rs.getInt(2);
					if ((elem_type != -1) && (elem_value != -1)) {
						applyAttribute(elem_type, elem_value);
					}
				}
			}
		} catch (Exception ex) {
			LOG.warn("Could not restore augmentation and elemental data for item {} from database!", this, ex);
		}
	}
	
	private void updateItemAttributes(Connection con) {
		try (var ps = con.prepareStatement("REPLACE INTO item_attributes VALUES(?,?)")) {
			ps.setInt(1, getObjectId());
			ps.setInt(2, _augmentation != null ? _augmentation.getAttributes() : -1);
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Could not update atributes for item {} from database!", this, ex);
		}
	}
	
	private void updateItemElements(Connection con) {
		try (var ps = con.prepareStatement("DELETE FROM item_elementals WHERE itemId = ?")) {
			ps.setInt(1, getObjectId());
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Could not update elementals for item {} from database!", this, ex);
		}
		
		if (_elementals == null) {
			return;
		}
		
		try (var ps = con.prepareStatement("INSERT INTO item_elementals VALUES(?,?,?)")) {
			for (Elementals elm : _elementals) {
				ps.setInt(1, getObjectId());
				ps.setByte(2, elm.getElement());
				ps.setInt(3, elm.getValue());
				ps.executeUpdate();
				ps.clearParameters();
			}
		} catch (Exception ex) {
			LOG.warn("Could not update elementals for item {} from database!", this, ex);
		}
	}
	
	public Elementals[] getElementals() {
		return _elementals;
	}
	
	public Elementals getElemental(byte attribute) {
		if (_elementals == null) {
			return null;
		}
		for (Elementals elm : _elementals) {
			if (elm.getElement() == attribute) {
				return elm;
			}
		}
		return null;
	}
	
	public byte getAttackElementType() {
		if (!isWeapon()) {
			return -2;
		} else if (getItem().getElementals() != null) {
			return getItem().getElementals()[0].getElement();
		} else if (_elementals != null) {
			return _elementals[0].getElement();
		}
		return -2;
	}
	
	public int getAttackElementPower() {
		if (!isWeapon()) {
			return 0;
		} else if (getItem().getElementals() != null) {
			return getItem().getElementals()[0].getValue();
		} else if (_elementals != null) {
			return _elementals[0].getValue();
		}
		return 0;
	}
	
	public int getElementDefAttr(byte element) {
		if (!isArmor()) {
			return 0;
		} else if (getItem().getElementals() != null) {
			Elementals elm = getItem().getElemental(element);
			if (elm != null) {
				return elm.getValue();
			}
		} else if (_elementals != null) {
			Elementals elm = getElemental(element);
			if (elm != null) {
				return elm.getValue();
			}
		}
		return 0;
	}
	
	private void applyAttribute(byte element, int value) {
		if (_elementals == null) {
			_elementals = new Elementals[1];
			_elementals[0] = new Elementals(element, value);
		} else {
			Elementals elm = getElemental(element);
			if (elm != null) {
				elm.setValue(value);
			} else {
				elm = new Elementals(element, value);
				Elementals[] array = new Elementals[_elementals.length + 1];
				System.arraycopy(_elementals, 0, array, 0, _elementals.length);
				array[_elementals.length] = elm;
				_elementals = array;
			}
		}
	}
	
	/**
	 * Add elemental attribute to item and save to db
	 * @param element
	 * @param value
	 */
	public void setElementAttr(byte element, int value) {
		applyAttribute(element, value);
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			updateItemElements(con);
		} catch (Exception ex) {
			LOG.warn("Could not update elementals for item {} from database!", this, ex);
		}
	}
	
	/**
	 * Remove elemental from item
	 * @param element byte element to remove, -1 for all elementals remove
	 */
	public void clearElementAttr(byte element) {
		if ((getElemental(element) == null) && (element != -1)) {
			return;
		}
		
		Elementals[] array = null;
		if ((element != -1) && (_elementals != null) && (_elementals.length > 1)) {
			array = new Elementals[_elementals.length - 1];
			int i = 0;
			for (Elementals elm : _elementals) {
				if (elm.getElement() != element) {
					array[i++] = elm;
				}
			}
		}
		_elementals = array;
		
		String query = (element != -1) ? "DELETE FROM item_elementals WHERE itemId = ? AND elemType = ?" : "DELETE FROM item_elementals WHERE itemId = ?";
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement(query)) {
			if (element != -1) {
				// Item can have still others
				ps.setInt(2, element);
			}
			
			ps.setInt(1, getObjectId());
			ps.executeUpdate();
		} catch (Exception ex) {
			LOG.warn("Could not remove elemental enchant for item {} from database!", this, ex);
		}
	}
	
	/**
	 * Used to decrease mana (mana means life time for shadow items)
	 */
	public static class ScheduleConsumeManaTask implements Runnable {
		
		private static final Logger LOG = LoggerFactory.getLogger(ScheduleConsumeManaTask.class);
		
		private final L2ItemInstance _shadowItem;
		
		public ScheduleConsumeManaTask(L2ItemInstance item) {
			_shadowItem = item;
		}
		
		@Override
		public void run() {
			try {
				// decrease mana
				if (_shadowItem != null) {
					_shadowItem.decreaseMana(true);
				}
			} catch (Exception ex) {
				LOG.warn("There has been an error decreasing Mana!", ex);
			}
		}
	}
	
	/**
	 * Returns true if this item is a shadow item Shadow items have a limited life-time
	 * @return
	 */
	public boolean isShadowItem() {
		return (_mana >= 0);
	}
	
	/**
	 * Returns the remaining mana of this shadow item
	 * @return lifeTime
	 */
	public int getMana() {
		return _mana;
	}
	
	/**
	 * Decreases the mana of this shadow item, sends a inventory update schedules a new consumption task if non is running optionally one could force a new task
	 * @param resetConsumingMana if true forces a new consumption task if item is equipped
	 */
	public void decreaseMana(boolean resetConsumingMana) {
		decreaseMana(resetConsumingMana, 1);
	}
	
	/**
	 * Decreases the mana of this shadow item, sends a inventory update schedules a new consumption task if non is running optionally one could force a new task
	 * @param resetConsumingMana if forces a new consumption task if item is equipped
	 * @param count how much mana decrease
	 */
	public void decreaseMana(boolean resetConsumingMana, int count) {
		if (!isShadowItem()) {
			return;
		}
		
		if ((_mana - count) >= 0) {
			_mana -= count;
		} else {
			_mana = 0;
		}
		
		if (_storedInDb) {
			_storedInDb = false;
		}
		if (resetConsumingMana) {
			_consumingMana = false;
		}
		
		final L2PcInstance player = getActingPlayer();
		if (player != null) {
			SystemMessage sm;
			switch (_mana) {
				case 10 -> {
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_10);
					sm.addItemName(_item);
					player.sendPacket(sm);
				}
				case 5 -> {
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_5);
					sm.addItemName(_item);
					player.sendPacket(sm);
				}
				case 1 -> {
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_1);
					sm.addItemName(_item);
					player.sendPacket(sm);
				}
			}
			
			if (_mana == 0) // The life time has expired
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1S_REMAINING_MANA_IS_NOW_0);
				sm.addItemName(_item);
				player.sendPacket(sm);
				
				// unequip
				if (isEquipped()) {
					L2ItemInstance[] unequiped = player.getInventory().unEquipItemInSlotAndRecord(getLocationSlot());
					InventoryUpdate iu = new InventoryUpdate();
					for (L2ItemInstance item : unequiped) {
						item.unChargeAllShots();
						iu.addModifiedItem(item);
					}
					player.sendPacket(iu);
					player.broadcastUserInfo();
				}
				
				if (getItemLocation() != ItemLocation.WAREHOUSE) {
					// destroy
					player.getInventory().destroyItem("L2ItemInstance", this, player, null);
					
					// send update
					InventoryUpdate iu = new InventoryUpdate();
					iu.addRemovedItem(this);
					player.sendPacket(iu);
					
					StatusUpdate su = new StatusUpdate(player);
					su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
					player.sendPacket(su);
					
				} else {
					player.getWarehouse().destroyItem("L2ItemInstance", this, player, null);
				}
				
				// delete from world
				L2World.getInstance().removeObject(this);
			} else {
				// Reschedule if still equipped
				if (!_consumingMana && isEquipped()) {
					scheduleConsumeManaTask();
				}
				if (getItemLocation() != ItemLocation.WAREHOUSE) {
					InventoryUpdate iu = new InventoryUpdate();
					iu.addModifiedItem(this);
					player.sendPacket(iu);
				}
			}
		}
	}
	
	public void scheduleConsumeManaTask() {
		if (_consumingMana) {
			return;
		}
		_consumingMana = true;
		ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleConsumeManaTask(this), MANA_CONSUMPTION_RATE);
	}
	
	/**
	 * Returns false cause item can't be attacked
	 * @return boolean false
	 */
	@Override
	public boolean isAutoAttackable(L2Character attacker) {
		return false;
	}
	
	/**
	 * This function basically returns a set of functions from L2Item/L2Armor/L2Weapon, but may add additional functions, if this particular item instance is enhanced for a particular player.
	 * @param player the player
	 * @return the functions list
	 */
	public List<AbstractFunction> getStatFuncs(L2Character player) {
		return getItem().getStatFuncs(this, player);
	}
	
	/**
	 * Updates the database.<BR>
	 */
	public void updateDatabase() {
		this.updateDatabase(false);
	}
	
	/**
	 * Updates the database.<BR>
	 * @param force if the update should necessarily be done.
	 */
	public void updateDatabase(boolean force) {
		_dbLock.lock();
		
		try {
			if (_existsInDb) {
				if ((_ownerId == 0) || (_loc == ItemLocation.VOID) || (_loc == ItemLocation.REFUND) || ((getCount() == 0) && (_loc != ItemLocation.LEASE))) {
					removeFromDb();
				} else if (!general().lazyItemsUpdate() || force) {
					updateInDb();
				}
			} else {
				if ((_ownerId == 0) || (_loc == ItemLocation.VOID) || (_loc == ItemLocation.REFUND) || ((getCount() == 0) && (_loc != ItemLocation.LEASE))) {
					return;
				}
				insertIntoDb();
			}
		} finally {
			_dbLock.unlock();
		}
	}
	
	/**
	 * Restores an item from the database.
	 * @param ownerId the owner Id
	 * @param rs the result set
	 * @return the item instance
	 */
	public static L2ItemInstance restoreFromDb(int ownerId, ResultSet rs) {
		try {
			final var objectId = rs.getInt(1);
			final var itemId = rs.getInt("item_id");
			final var template = ItemTable.getInstance().getTemplate(itemId);
			if (template == null) {
				LOG.error("Item Id {} not known, object Id {} by owner Id {}!", itemId, objectId, ownerId);
				return null;
			}
			
			final var item = new L2ItemInstance(objectId, template);
			item._ownerId = ownerId;
			item.setCount(rs.getLong("count"));
			item._enchantLevel = rs.getInt("enchant_level");
			item._type1 = rs.getInt("custom_type1");
			item._type2 = rs.getInt("custom_type2");
			item._loc = ItemLocation.valueOf(rs.getString("loc"));
			item._locData = rs.getInt("loc_data");
			item._existsInDb = true;
			item._storedInDb = true;
			// Support shadow weapons
			item._mana = rs.getInt("mana_left");
			item._time = rs.getLong("time");
			// Support Agathion energy
			item.agathionEnergy = rs.getInt("agathion_energy");
			
			// load augmentation and elemental enchant
			if (item.isEquipable()) {
				item.restoreAttributes();
			}
			return item;
		} catch (Exception ex) {
			LOG.warn("Could not restore an item owned by {} from database!", ownerId, ex);
			return null;
		}
	}
	
	/**
	 * Init a dropped L2ItemInstance and add it in the world as a visible object.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Set the x,y,z position of the L2ItemInstance dropped and update its _worldregion</li>
	 * <li>Add the L2ItemInstance dropped to _visibleObjects of its L2WorldRegion</li>
	 * <li>Add the L2ItemInstance dropped in the world as a <B>visible</B> object</li><BR>
	 * <BR>
	 * <FONT COLOR=#FF0000><B> <U>Caution</U> : This method DOESN'T ADD the object to _allObjects of L2World </B></FONT><BR>
	 * <BR>
	 * <B><U> Assert </U> :</B><BR>
	 * <BR>
	 * <li>_worldRegion == null <I>(L2Object is invisible at the beginning)</I></li><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Drop item</li>
	 * <li>Call Pet</li><BR>
	 */
	public class ItemDropTask implements Runnable {
		private int _x, _y, _z;
		private final L2Character _dropper;
		private final L2ItemInstance _itm;
		
		public ItemDropTask(L2ItemInstance item, L2Character dropper, int x, int y, int z) {
			_x = x;
			_y = y;
			_z = z;
			_dropper = dropper;
			_itm = item;
		}
		
		@Override
		public final void run() {
			assert _itm.getWorldRegion() == null;
			
			if (_dropper != null) {
				Location dropDest = GeoData.getInstance().moveCheck(_dropper.getX(), _dropper.getY(), _dropper.getZ(), _x, _y, _z, _dropper.getInstanceId());
				_x = dropDest.getX();
				_y = dropDest.getY();
				_z = dropDest.getZ();
			}
			
			if (_dropper != null) {
				setInstanceId(_dropper.getInstanceId()); // Inherit instance zone when dropped in visible world
			} else {
				setInstanceId(0); // No dropper? Make it a global item...
			}
			
			synchronized (_itm) {
				// Set the x,y,z position of the L2ItemInstance dropped and update its _worldregion
				_itm.setIsVisible(true);
				_itm.setXYZ(_x, _y, _z);
				_itm.setWorldRegion(L2World.getInstance().getRegion(getLocation()));
				
				// Add the L2ItemInstance dropped to _visibleObjects of its L2WorldRegion
			}
			
			_itm.getWorldRegion().addVisibleObject(_itm);
			_itm.setDropTime(System.currentTimeMillis());
			_itm.setDropperObjectId(_dropper != null ? _dropper.getObjectId() : 0); // Set the dropper Id for the known list packets in sendInfo
			
			// this can synchronize on others instances, so it's out of
			// synchronized, to avoid deadlocks
			// Add the L2ItemInstance dropped in the world as a visible object
			L2World.getInstance().addVisibleObject(_itm, _itm.getWorldRegion());
			if (general().saveDroppedItem()) {
				ItemsOnGroundManager.getInstance().save(_itm);
			}
			_itm.setDropperObjectId(0); // Set the dropper Id back to 0 so it no longer shows the drop packet
		}
	}
	
	public void dropMe(L2Character dropper, int x, int y, int z) {
		ThreadPoolManager.getInstance().executeGeneral(new ItemDropTask(this, dropper, x, y, z));
		if ((dropper != null) && dropper.isPlayer()) {
			// Notify to scripts
			EventDispatcher.getInstance().notifyEventAsync(new OnPlayerItemDrop(dropper.getActingPlayer(), this, new Location(x, y, z)), getItem());
		}
	}
	
	/**
	 * Update the database with values of the item
	 */
	private void updateInDb() {
		assert _existsInDb;
		
		if (_wear) {
			return;
		}
		
		if (_storedInDb) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("UPDATE items SET owner_id=?, count=?, loc=?, loc_data=?, enchant_level=?, custom_type1=?, custom_type2=?, mana_left=?, time=?, agathion_energy=? WHERE object_id=?")) {
			ps.setInt(1, _ownerId);
			ps.setLong(2, getCount());
			ps.setString(3, _loc.name());
			ps.setInt(4, _locData);
			ps.setInt(5, getEnchantLevel());
			ps.setInt(6, getCustomType1());
			ps.setInt(7, getCustomType2());
			ps.setInt(8, getMana());
			ps.setLong(9, getTime());
			ps.setInt(10, getAgathionRemainingEnergy());
			ps.setInt(11, getObjectId());
			ps.executeUpdate();
			_existsInDb = true;
			_storedInDb = true;
		} catch (Exception ex) {
			LOG.warn("Could not update item {} in database!", this, ex);
		}
	}
	
	private void insertIntoDb() {
		assert !_existsInDb && (getObjectId() != 0);
		
		if (_wear) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection();
			var ps = con.prepareStatement("INSERT INTO items (owner_id, item_id, count, loc, loc_data, enchant_level, object_id, custom_type1, custom_type2, mana_left, time, agathion_energy) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)")) {
			ps.setInt(1, _ownerId);
			ps.setInt(2, _itemId);
			ps.setLong(3, getCount());
			ps.setString(4, _loc.name());
			ps.setInt(5, _locData);
			ps.setInt(6, getEnchantLevel());
			ps.setInt(7, getObjectId());
			ps.setInt(8, _type1);
			ps.setInt(9, _type2);
			ps.setInt(10, getMana());
			ps.setLong(11, getTime());
			ps.setInt(12, getAgathionRemainingEnergy());
			
			ps.executeUpdate();
			_existsInDb = true;
			_storedInDb = true;
			
			if (_augmentation != null) {
				updateItemAttributes(con);
			}
			if (_elementals != null) {
				updateItemElements(con);
			}
		} catch (Exception ex) {
			LOG.warn("Could not insert item {} into database!", this, ex);
		}
	}
	
	private void removeFromDb() {
		assert _existsInDb;
		
		if (_wear) {
			return;
		}
		
		try (var con = ConnectionFactory.getInstance().getConnection()) {
			try (var ps = con.prepareStatement("DELETE FROM items WHERE object_id = ?")) {
				ps.setInt(1, getObjectId());
				ps.executeUpdate();
				_existsInDb = false;
				_storedInDb = false;
			}
			
			try (var ps = con.prepareStatement("DELETE FROM item_attributes WHERE itemId = ?")) {
				ps.setInt(1, getObjectId());
				ps.executeUpdate();
			}
			
			try (var ps = con.prepareStatement("DELETE FROM item_elementals WHERE itemId = ?")) {
				ps.setInt(1, getObjectId());
				ps.executeUpdate();
			}
		} catch (Exception ex) {
			LOG.warn("Could not delete item {} in database!", this, ex);
		}
	}
	
	@Override
	public String toString() {
		return _item + "[" + getObjectId() + "]" + (getEnchantLevel() > 0 ? " +" + getEnchantLevel() : "");
	}
	
	public void resetOwnerTimer() {
		if (itemLootSchedule != null) {
			itemLootSchedule.cancel(true);
		}
		itemLootSchedule = null;
	}
	
	public void setItemLootSchedule(ScheduledFuture<?> sf) {
		itemLootSchedule = sf;
	}
	
	public ScheduledFuture<?> getItemLootSchedule() {
		return itemLootSchedule;
	}
	
	public void setProtected(boolean isProtected) {
		_protected = isProtected;
	}
	
	public boolean isProtected() {
		return _protected;
	}
	
	public boolean isNightLure() {
		return (((_itemId >= 8505) && (_itemId <= 8513)) || (_itemId == 8485));
	}
	
	public void setCountDecrease(boolean decrease) {
		_decrease = decrease;
	}
	
	public boolean getCountDecrease() {
		return _decrease;
	}
	
	public void setInitCount(int InitCount) {
		_initCount = InitCount;
	}
	
	public long getInitCount() {
		return _initCount;
	}
	
	public void restoreInitCount() {
		if (_decrease) {
			setCount(_initCount);
		}
	}
	
	public boolean isTimeLimitedItem() {
		return (_time > 0);
	}
	
	/**
	 * Returns (current system time + time) of this time limited item
	 * @return Time
	 */
	public long getTime() {
		return _time;
	}
	
	public long getRemainingTime() {
		return _time - System.currentTimeMillis();
	}
	
	public void endOfLife() {
		L2PcInstance player = getActingPlayer();
		if (player != null) {
			if (isEquipped()) {
				L2ItemInstance[] unequiped = player.getInventory().unEquipItemInSlotAndRecord(getLocationSlot());
				InventoryUpdate iu = new InventoryUpdate();
				for (L2ItemInstance item : unequiped) {
					item.unChargeAllShots();
					iu.addModifiedItem(item);
				}
				player.sendPacket(iu);
				player.broadcastUserInfo();
			}
			
			if (getItemLocation() != ItemLocation.WAREHOUSE) {
				// destroy
				player.getInventory().destroyItem("L2ItemInstance", this, player, null);
				
				// send update
				InventoryUpdate iu = new InventoryUpdate();
				iu.addRemovedItem(this);
				player.sendPacket(iu);
				
				StatusUpdate su = new StatusUpdate(player);
				su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
				player.sendPacket(su);
				
			} else {
				player.getWarehouse().destroyItem("L2ItemInstance", this, player, null);
			}
			player.sendPacket(SystemMessageId.TIME_LIMITED_ITEM_DELETED);
			// delete from world
			L2World.getInstance().removeObject(this);
		}
	}
	
	public void scheduleLifeTimeTask() {
		if (!isTimeLimitedItem()) {
			return;
		}
		if (getRemainingTime() <= 0) {
			endOfLife();
		} else {
			if (_lifeTimeTask != null) {
				_lifeTimeTask.cancel(false);
			}
			_lifeTimeTask = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleLifeTimeTask(this), getRemainingTime());
		}
	}
	
	public static class ScheduleLifeTimeTask implements Runnable {
		
		private static final Logger LOG = LoggerFactory.getLogger(ScheduleLifeTimeTask.class);
		
		private final L2ItemInstance _limitedItem;
		
		public ScheduleLifeTimeTask(L2ItemInstance item) {
			_limitedItem = item;
		}
		
		@Override
		public void run() {
			try {
				if (_limitedItem != null) {
					_limitedItem.endOfLife();
				}
			} catch (Exception ex) {
				LOG.warn("There has been an error ending item {} life!", _limitedItem, ex);
			}
		}
	}
	
	public void updateElementAttrBonus(L2PcInstance player) {
		if (_elementals == null) {
			return;
		}
		for (Elementals elm : _elementals) {
			elm.updateBonus(player, isArmor());
		}
	}
	
	public void removeElementAttrBonus(L2PcInstance player) {
		if (_elementals == null) {
			return;
		}
		for (Elementals elm : _elementals) {
			elm.removeBonus(player);
		}
	}
	
	public void setDropperObjectId(int id) {
		_dropperObjectId = id;
	}
	
	@Override
	public void sendInfo(L2PcInstance activeChar) {
		if (_dropperObjectId != 0) {
			activeChar.sendPacket(new DropItem(this, _dropperObjectId));
		} else {
			activeChar.sendPacket(new SpawnItem(this));
		}
	}
	
	public DropProtection getDropProtection() {
		return _dropProtection;
	}
	
	public boolean isPublished() {
		return _published;
	}
	
	public void publish() {
		_published = true;
	}
	
	@Override
	public boolean decayMe() {
		if (general().saveDroppedItem()) {
			ItemsOnGroundManager.getInstance().removeObject(this);
		}
		return super.decayMe();
	}
	
	public boolean isQuestItem() {
		return getItem().isQuestItem();
	}
	
	public boolean isElementable() {
		if ((getItemLocation() == ItemLocation.INVENTORY) || (getItemLocation() == ItemLocation.PAPERDOLL)) {
			return getItem().isElementable();
		}
		return false;
	}
	
	public boolean isFreightable() {
		return getItem().isFreightable();
	}
	
	public int useSkillDisTime() {
		return getItem().useSkillDisTime();
	}
	
	public int getOlyEnchantLevel() {
		L2PcInstance player = getActingPlayer();
		int enchant = getEnchantLevel();
		
		if (player == null) {
			return enchant;
		}
		
		if (player.isInOlympiadMode() && (olympiad().getEnchantLimit() >= 0) && (enchant > olympiad().getEnchantLimit())) {
			enchant = olympiad().getEnchantLimit();
		}
		
		return enchant;
	}
	
	public int getDefaultEnchantLevel() {
		return _item.getDefaultEnchantLevel();
	}
	
	public boolean hasPassiveSkills() {
		return (getItemType() == EtcItemType.RUNE) && (getItemLocation() == ItemLocation.INVENTORY) && (getOwnerId() > 0) && getItem().hasSkills();
	}
	
	public void giveSkillsToOwner() {
		if (!hasPassiveSkills()) {
			return;
		}
		
		final L2PcInstance player = getActingPlayer();
		
		if (player != null) {
			for (SkillHolder sh : getItem().getSkills()) {
				if (sh.getSkill().isPassive()) {
					player.addSkill(sh.getSkill(), false);
				}
			}
		}
	}
	
	public void removeSkillsFromOwner() {
		if (!hasPassiveSkills()) {
			return;
		}
		
		final L2PcInstance player = getActingPlayer();
		
		if (player != null) {
			for (SkillHolder sh : getItem().getSkills()) {
				if (sh.getSkill().isPassive()) {
					player.removeSkill(sh.getSkill(), false, true);
				}
			}
		}
	}
	
	@Override
	public boolean isItem() {
		return true;
	}
	
	@Override
	public L2PcInstance getActingPlayer() {
		return L2World.getInstance().getPlayer(getOwnerId());
	}
	
	public int getEquipReuseDelay() {
		return _item.getEquipReuseDelay();
	}
	
	/**
	 * @param activeChar
	 * @param command
	 */
	public void onBypassFeedback(L2PcInstance activeChar, String command) {
		if (command.startsWith("Quest")) {
			String questName = command.substring(6);
			String event = null;
			int idx = questName.indexOf(' ');
			if (idx > 0) {
				event = questName.substring(idx).trim();
			}
			
			if (event != null) {
				EventDispatcher.getInstance().notifyEventAsync(new OnItemBypassEvent(this, activeChar, event), getItem());
			} else {
				EventDispatcher.getInstance().notifyEventAsync(new OnItemTalk(this, activeChar), getItem());
			}
		}
	}
	
	@Override
	public boolean isChargedShot(ShotType type) {
		return (_shotsMask & type.getMask()) == type.getMask();
	}
	
	@Override
	public void setChargedShot(ShotType type, boolean charged) {
		if (charged) {
			_shotsMask |= type.getMask();
		} else {
			_shotsMask &= ~type.getMask();
		}
	}
	
	public void unChargeAllShots() {
		_shotsMask = 0;
	}
	
	/**
	 * Returns enchant effect object for this item
	 * @return enchanteffect
	 */
	public int[] getEnchantOptions() {
		EnchantOptions op = EnchantItemOptionsData.getInstance().getOptions(this);
		if (op != null) {
			return op.getOptions();
		}
		return DEFAULT_ENCHANT_OPTIONS;
	}
	
	/**
	 * Clears all the enchant bonuses if item is enchanted and containing bonuses for enchant value.
	 */
	public void clearEnchantStats() {
		final L2PcInstance player = getActingPlayer();
		if (player == null) {
			_enchantOptions.clear();
			return;
		}
		
		for (Options op : _enchantOptions) {
			op.remove(player);
		}
		_enchantOptions.clear();
	}
	
	/**
	 * Clears and applies all the enchant bonuses if item is enchanted and containing bonuses for enchant value.
	 */
	public void applyEnchantStats() {
		final L2PcInstance player = getActingPlayer();
		if (!isEquipped() || (player == null) || (getEnchantOptions() == DEFAULT_ENCHANT_OPTIONS)) {
			return;
		}
		
		for (int id : getEnchantOptions()) {
			final Options options = OptionData.getInstance().getOptions(id);
			if (options != null) {
				options.apply(player);
				_enchantOptions.add(options);
			} else if (id != 0) {
				LOG.warn("Couldn't find option Id {} for item {}!", id, this);
			}
		}
	}
	
	@Override
	public void setHeading(int heading) {
	}
	
	public void deleteMe() {
		if ((_lifeTimeTask != null) && !_lifeTimeTask.isDone()) {
			_lifeTimeTask.cancel(false);
			_lifeTimeTask = null;
		}
	}
	
	public int getAgathionRemainingEnergy() {
		return agathionEnergy;
	}
	
	public void setAgathionRemainingEnergy(int agathionEnergy) {
		this.agathionEnergy = agathionEnergy;
		_storedInDb = false;
	}
}
