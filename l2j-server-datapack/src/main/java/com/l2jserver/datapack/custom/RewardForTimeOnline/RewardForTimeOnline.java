package com.l2jserver.datapack.custom.RewardForTimeOnline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.config.RewardsOnlineConfig;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.Containers;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogout;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.quest.Quest;

/**
 * @author Dafna
 */
public final class RewardForTimeOnline extends Quest
{
	static final Logger LOG = LoggerFactory.getLogger(RewardForTimeOnline.class);
	
	// вкл/выкл
	static final boolean LOAD = true;
	
	final Map<Integer, PlayerHolder> players;
	
	final List<ItemHolder> rewardItem;
	
	
	private void addItem() {
    try {
        RewardsOnlineConfig config = ConfigFactory.create(RewardsOnlineConfig.class);
        int rewardItemCount = config.getRewardItemCount();

        for (int i = 1; i <= rewardItemCount; i++) {
            int id = config.getItemId(i);
            long count = config.getItemCount(i);
            long time = config.getItemTime(i);
            boolean saveToDatabase = config.isSaveToDatabase(i);
            boolean onceOnly = config.isOnceOnly(i);

            ItemHolder itemHolder = new ItemHolder(id, count, time, saveToDatabase);
            itemHolder.setOnceOnly(onceOnly); // Устанавливаем значение onceOnly
            rewardItem.add(itemHolder);
        }
    } catch (Exception e) {
        LOG.error("An error occurred during initialization:", e);
    }
}

	

	
	/*private String getVar()
	{
		return getClass().getSimpleName() + "_" + rewardItem.size();
	}*/
	
	private final class PlayerHolder
	{
		final L2PcInstance player;
		final List<RewardTask> rewardTask = new ArrayList<>();
		
		public PlayerHolder(L2PcInstance player)
		{
			this.player = player;
		}
		
		public final PlayerHolder startRewardTask()
		{
			for (ItemHolder item : rewardItem)
			{
				rewardTask.add(new RewardTask(this, item));
			}
			
			return this;
		}
		
		public final void onPlayerLogout()
		{
			for (RewardTask t : rewardTask)
			{
				t.onPlayerLogout();
			}
		}
	}
	
	private final class RewardTask implements Runnable
	{
		private final PlayerHolder ph;
		private final ItemHolder item;
		private ScheduledFuture<?> task = null;
		
		private long lastTime;
		
		public RewardTask(PlayerHolder playerHolder, ItemHolder item)
		{
			this.ph = playerHolder;
			this.item = item;
			this.lastTime = System.currentTimeMillis();
			
			long initialDelay;
			
			if (item.isSaveTime)
			{
				initialDelay = ph.player.getVariables().getLong(item.var, 0);
				if ((initialDelay == 0) || (initialDelay > item.time))
				{
					initialDelay = item.time;
				}
			}
			else
			{
				initialDelay = item.time;
			}
			
			this.task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(this, initialDelay, item.time);
		}
		
		@Override
		public void run()
		{
			if ((ph.player == null) || (ph.player.getClient() == null) || ph.player.getClient().isDetached())
			{
				return;
			}
			
			// Проверяем, выдавали ли уже предмет
			if (item.isItemGiven() && item.onceOnly) {
				return; // Если предмет уже выдан и должен выдаваться только один раз, просто возвращаемся
			}
			
			if (ph.player.isOnline())
			{
				if (item.isSaveTime)
				{
					ph.player.getVariables().set(item.var, 0);
				}
				
				lastTime = System.currentTimeMillis();
				
				ph.player.addItem(RewardForTimeOnline.class.getSimpleName(), item.id, item.count, null, true);
				
				if (item.onceOnly) {
					item.setItemGiven(true);
				}
			}
		}
		
		public final void onPlayerLogout()
		{
			stopTask();
			
			if (item.isSaveTime)
			{
				ph.player.getVariables().set(item.var, (item.time - (System.currentTimeMillis() - lastTime)));
			}
		}
		
		public final void stopTask()
		{
			if (task != null)
			{
				task.cancel(false);
				task = null;
			}
		}
	}
	
	private final class ItemHolder {
    final String var;
    final int id;
    final long count;
    final long time;
    final boolean isSaveTime;
    boolean onceOnly; // Переменная для хранения параметра onceOnly
    private boolean itemGiven = false;

    public ItemHolder(int id, long count, long time, boolean isSaveTime) {
        this.var = "rfto_" + id;;
        this.id = id;
        this.count = count;
        this.time = time;
        this.isSaveTime = isSaveTime;
        this.onceOnly = false; // Изначально onceOnly устанавливаем как false
    }

    public boolean isItemGiven() {
        return itemGiven;
    }

    public void setItemGiven(boolean itemGiven) {
        this.itemGiven = itemGiven;
    }

    public boolean isOnceOnly() {
        return onceOnly;
    }

    public void setOnceOnly(boolean onceOnly) {
        this.onceOnly = onceOnly;
    }
}

	
	public RewardForTimeOnline()
	{
		super(-1, RewardForTimeOnline.class.getSimpleName(), "custom");
		players = new ConcurrentHashMap<>();
		rewardItem = new ArrayList<>();
		try {
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_LOGIN, (OnPlayerLogin event) -> onPlayerLogin(event), this));
		Containers.Global().addListener(new ConsumerEventListener(Containers.Global(), EventType.ON_PLAYER_LOGOUT, (OnPlayerLogout event) -> onPlayerLogout(event), this));
			
		addItem();
		} catch (Exception e) {
        LOG.error("An error occurred during initialization:", e);
    }
	}
	
	
	private final void onPlayerLogin(OnPlayerLogin event)
	{
		PlayerHolder task = players.get(event.getActiveChar().getObjectId());
		if (task == null)
		{
			players.put(event.getActiveChar().getObjectId(), new PlayerHolder(event.getActiveChar()).startRewardTask());
		}
	}
	
	private final void onPlayerLogout(OnPlayerLogout event)
	{
		PlayerHolder task = players.remove(event.getActiveChar().getObjectId());
		if (task != null)
		{
			task.onPlayerLogout();
		}
	}
	
	public static void main(String[] args) {
		if (LOAD) {
			try {
				new RewardForTimeOnline();
				LOG.info("{}: loaded.", RewardForTimeOnline.class.getSimpleName());
			} catch (Exception e) {
				LOG.error("Failed to load {}:", RewardForTimeOnline.class.getSimpleName(), e);
			}
		} else {
			LOG.info("{}: not loaded.", RewardForTimeOnline.class.getSimpleName());
		}
	}
	
}