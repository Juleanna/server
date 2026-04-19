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
package com.l2jserver.datapack.ai.group_template;

import static com.l2jserver.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Attackable;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcEventReceived;
import com.l2jserver.gameserver.network.NpcStringId;

/**
 * Turek Orcs AI - flee and return with assistance
 * @author GKR
 */
public final class TurekOrcs extends AbstractNpcAI {
	// NPC's
	private static final int[] MOBS = {
		20494, // Turek War Hound
		20495, // Turek Orc Warlord
		20497, // Turek Orc Skirmisher
		20498, // Turek Orc Supplier
		20499, // Turek Orc Footman
		20500, // Turek Orc Sentinel
	};
	
	public TurekOrcs() {
		bindAttack(MOBS);
		bindEventReceived(MOBS);
		bindMoveFinished(MOBS);
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.equalsIgnoreCase("checkState") && !npc.isDead() && (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK)) {
			if ((npc.getCurrentHp() > (npc.getMaxHp() * 0.7)) && (npc.getVariables().getInt("state") == 2)) {
				npc.getVariables().set("state", 3);
				((L2Attackable) npc).returnHome();
			} else {
				npc.getVariables().remove("state");
			}
		}
		return super.onEvent(event, npc, player);
	}
	
	@Override
	public void onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon) {
		if (!npc.getVariables().hasVariable("isHit")) {
			npc.getVariables().set("isHit", 1);
		} else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.5)) && (npc.getCurrentHp() > (npc.getMaxHp() * 0.3)) && (attacker.getCurrentHp() > (attacker.getMaxHp() * 0.25)) && npc.hasAIValue("fleeX") && npc.hasAIValue("fleeY") && npc.hasAIValue("fleeZ") && (npc.getVariables().getInt("state") == 0)
			&& (getRandom(100) < 10)) {
			// Say and flee
			broadcastNpcSay(npc, 0, NpcStringId.getNpcStringId(getRandom(1000007, 1000027)));
			npc.disableCoreAI(true); // to avoid attacking behaviour, while flee
			npc.setIsRunning(true);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(npc.getAIValue("fleeX"), npc.getAIValue("fleeY"), npc.getAIValue("fleeZ")));
			npc.getVariables().set("state", 1);
			npc.getVariables().set("attacker", attacker.getObjectId());
		}
	}
	
	@Override
	public void onEventReceived(NpcEventReceived event) {
		final var receiver = event.receiver();
		if (event.eventName().equals("WARNING") && !receiver.isDead() && (receiver.getAI().getIntention() != AI_INTENTION_ATTACK) && //
			(event.reference() != null) && (event.reference().getActingPlayer() != null) && !event.reference().getActingPlayer().isDead()) {
			receiver.getVariables().set("state", 3);
			receiver.setIsRunning(true);
			((L2Attackable) receiver).addDamageHate(event.reference().getActingPlayer(), 0, 99999);
			receiver.getAI().setIntention(AI_INTENTION_ATTACK, event.reference().getActingPlayer());
		}
	}
	
	@Override
	public void onMoveFinished(L2Npc npc) {
		// NPC reaches flee point
		if (npc.getVariables().getInt("state") == 1) {
			if ((npc.getX() == npc.getAIValue("fleeX")) && (npc.getY() == npc.getAIValue("fleeY"))) {
				npc.disableCoreAI(false);
				startQuestTimer("checkState", 15000, npc, null);
				npc.getVariables().set("state", 2);
				npc.broadcastScriptEvent("WARNING", 400, L2World.getInstance().getPlayer(npc.getVariables().getInt("attacker")));
			} else {
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new Location(npc.getAIValue("fleeX"), npc.getAIValue("fleeY"), npc.getAIValue("fleeZ")));
			}
		} else if ((npc.getVariables().getInt("state") == 3) && npc.staysInSpawnLoc()) {
			npc.disableCoreAI(false);
			npc.getVariables().remove("state");
		}
	}
}
