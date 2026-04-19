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
package com.l2jserver.datapack.ai.npc.ClassMaster;

import static com.l2jserver.gameserver.config.Configuration.character;
import static com.l2jserver.gameserver.model.events.EventType.PLAYER_LEVEL_CHANGED;
import static com.l2jserver.gameserver.network.SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT;
import static com.l2jserver.gameserver.network.SystemMessageId.NOT_ENOUGH_ITEMS;
import static com.l2jserver.gameserver.network.serverpackets.TutorialCloseHtml.STATIC_PACKET;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.gameserver.data.xml.impl.ClassListData;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.base.ClassId;
import com.l2jserver.gameserver.model.buylist.L2BuyList;
import com.l2jserver.gameserver.model.events.Containers;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerLevelChanged;
import com.l2jserver.gameserver.model.events.impl.character.player.PlayerMenuSelected;
import com.l2jserver.gameserver.model.events.listeners.ConsumerEventListener;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.network.serverpackets.ExBrExtraUserInfo;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.TutorialShowHtml;
import com.l2jserver.gameserver.network.serverpackets.TutorialShowQuestionMark;
import com.l2jserver.gameserver.network.serverpackets.UserInfo;
import com.l2jserver.gameserver.util.StringUtil;

/**
 * Handle both NPC and tutorial window.
 * @author Zealar
 * @since 2.6.0.0
 */
public final class ClassMaster extends AbstractNpcAI {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClassMaster.class);
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(1463, 100, 0.000000, 0), // soulshot_d
		new BuySellList(1464, 100, 0.000000, 0), // soulshot_c
		new BuySellList(1465, 100, 0.000000, 0), // soulshot_b
		new BuySellList(1466, 100, 0.000000, 0), // soulshot_a
		new BuySellList(1467, 100, 0.000000, 0), // soulshot_s
		new BuySellList(2510, 100, 0.000000, 0), // spiritshot_d
		new BuySellList(2511, 100, 0.000000, 0), // spiritshot_c
		new BuySellList(2512, 100, 0.000000, 0), // spiritshot_b
		new BuySellList(2513, 100, 0.000000, 0), // spiritshot_a
		new BuySellList(2514, 100, 0.000000, 0), // spiritshot_s
		new BuySellList(3947, 100, 0.000000, 0), // blessed_spiritshot_none
		new BuySellList(3948, 100, 0.000000, 0), // blessed_spiritshot_d
		new BuySellList(3949, 100, 0.000000, 0), // blessed_spiritshot_c
		new BuySellList(3950, 100, 0.000000, 0), // blessed_spiritshot_b
		new BuySellList(3951, 100, 0.000000, 0), // blessed_spiritshot_a
		new BuySellList(3952, 100, 0.000000, 0), // blessed_spiritshot_s
		new BuySellList(5134, 100, 0.000000, 0), // comp_soulshot_none
		new BuySellList(5135, 100, 0.000000, 0), // comp_soulshot_d
		new BuySellList(5136, 100, 0.000000, 0), // comp_soulshot_c
		new BuySellList(5137, 100, 0.000000, 0), // comp_soulshot_b
		new BuySellList(5138, 100, 0.000000, 0), // comp_soulshot_a
		new BuySellList(5139, 100, 0.000000, 0), // comp_soulshot_s
		new BuySellList(5140, 100, 0.000000, 0), // comp_spiritshot_none
		new BuySellList(5141, 100, 0.000000, 0), // comp_spiritshot_d
		new BuySellList(5142, 100, 0.000000, 0), // comp_spiritshot_c
		new BuySellList(5143, 100, 0.000000, 0), // comp_spiritshot_b
		new BuySellList(5144, 100, 0.000000, 0), // comp_spiritshot_a
		new BuySellList(5145, 100, 0.000000, 0), // comp_spiritshot_s
		new BuySellList(5146, 100, 0.000000, 0), // comp_bspiritshot_none
		new BuySellList(5147, 100, 0.000000, 0), // comp_bspiritshot_d
		new BuySellList(5148, 100, 0.000000, 0), // comp_bspiritshot_c
		new BuySellList(5149, 100, 0.000000, 0), // comp_bspiritshot_b
		new BuySellList(5150, 100, 0.000000, 0), // comp_bspiritshot_a
		new BuySellList(5151, 100, 0.000000, 0), // comp_bspiritshot_s
		new BuySellList(5250, 100, 0.000000, 0), // adv_comp_soulshot_none
		new BuySellList(5251, 100, 0.000000, 0), // adv_comp_soulshot_d
		new BuySellList(5252, 100, 0.000000, 0), // adv_comp_soulshot_c
		new BuySellList(5253, 100, 0.000000, 0), // adv_comp_soulshot_b
		new BuySellList(5254, 100, 0.000000, 0), // adv_comp_soulshot_a
		new BuySellList(5255, 100, 0.000000, 0), // adv_comp_soulshot_s
		new BuySellList(5256, 100, 0.000000, 0), // adv_comp_spiritshot_none
		new BuySellList(5257, 100, 0.000000, 0), // adv_comp_spiritshot_d
		new BuySellList(5258, 100, 0.000000, 0), // adv_comp_spiritshot_c
		new BuySellList(5259, 100, 0.000000, 0), // adv_comp_spiritshot_b
		new BuySellList(5260, 100, 0.000000, 0), // adv_comp_spiritshot_a
		new BuySellList(5261, 100, 0.000000, 0), // adv_comp_spiritshot_s
		new BuySellList(5262, 100, 0.000000, 0), // adv_comp_bspiritshot_none
		new BuySellList(5263, 100, 0.000000, 0), // adv_comp_bspiritshot_d
		new BuySellList(5264, 100, 0.000000, 0), // adv_comp_bspiritshot_c
		new BuySellList(5265, 100, 0.000000, 0), // adv_comp_bspiritshot_b
		new BuySellList(5266, 100, 0.000000, 0), // adv_comp_bspiritshot_a
		new BuySellList(5267, 100, 0.000000, 0), // adv_comp_bspiritshot_s
		new BuySellList(1458, 100, 0.000000, 0), // crystal_d
		new BuySellList(1459, 100, 0.000000, 0), // crystal_c
		new BuySellList(1460, 100, 0.000000, 0), // crystal_b
		new BuySellList(1461, 100, 0.000000, 0), // crystal_a
		new BuySellList(1462, 100, 0.000000, 0), // crystal_s
		new BuySellList(2130, 100, 0.000000, 0), // gemstone_d
		new BuySellList(2131, 100, 0.000000, 0), // gemstone_c
		new BuySellList(2132, 100, 0.000000, 0), // gemstone_b
		new BuySellList(2133, 100, 0.000000, 0), // gemstone_a
		new BuySellList(2134, 100, 0.000000, 0), // gemstone_s
		new BuySellList(5192, 100, 0.000000, 0), // rope_of_magic_d
		new BuySellList(5193, 100, 0.000000, 0), // rope_of_magic_c
		new BuySellList(5194, 100, 0.000000, 0), // rope_of_magic_b
		new BuySellList(5195, 100, 0.000000, 0), // rope_of_magic_a
		new BuySellList(5196, 100, 0.000000, 0), // rope_of_magic_s
		new BuySellList(1341, 100, 0.000000, 0), // bone_arrow
		new BuySellList(1342, 100, 0.000000, 0), // fine_steel_arrow
		new BuySellList(1343, 100, 0.000000, 0), // silver_arrow
		new BuySellList(1344, 100, 0.000000, 0), // mithril_arrow
		new BuySellList(1345, 100, 0.000000, 0), // shining_arrow
		new BuySellList(734, 100, 0.000000, 0), // quick_step_potion
		new BuySellList(735, 100, 0.000000, 0), // swift_attack_potion
		new BuySellList(1060, 100, 0.000000, 0), // lesser_healing_potion
		new BuySellList(1061, 100, 0.000000, 0), // healing_potion
		new BuySellList(1374, 100, 0.000000, 0), // adv_quick_step_potion
		new BuySellList(1375, 100, 0.000000, 0), // adv_swift_attack_potion
		new BuySellList(1539, 100, 0.000000, 0), // greater_healing_potion
		new BuySellList(6035, 100, 0.000000, 0), // potion_of_acumen2
		new BuySellList(6036, 100, 0.000000, 0), // potion_of_acumen3
		new BuySellList(5126, 100, 0.000000, 0), // dualsword_craft_stamp
		new BuySellList(5589, 100, 0.000000, 0), // energy_stone
		new BuySellList(1864, 100, 0.000000, 0), // stem
		new BuySellList(1865, 100, 0.000000, 0), // varnish
		new BuySellList(1866, 100, 0.000000, 0), // suede
		new BuySellList(1867, 100, 0.000000, 0), // animal_skin
		new BuySellList(1868, 100, 0.000000, 0), // thread
		new BuySellList(1869, 100, 0.000000, 0), // iron_ore
		new BuySellList(1870, 100, 0.000000, 0), // coal
		new BuySellList(1871, 100, 0.000000, 0), // charcoal
		new BuySellList(1872, 100, 0.000000, 0), // animal_bone
		new BuySellList(1873, 100, 0.000000, 0), // silver_nugget
		new BuySellList(1874, 100, 0.000000, 0), // oriharukon_ore
		new BuySellList(1875, 100, 0.000000, 0), // stone_of_purity
		new BuySellList(1876, 100, 0.000000, 0), // mithril_ore
		new BuySellList(1877, 100, 0.000000, 0), // admantite_nugget
		new BuySellList(4039, 100, 0.000000, 0), // mold_glue
		new BuySellList(4040, 100, 0.000000, 0), // mold_lubricant
		new BuySellList(4041, 100, 0.000000, 0), // mold_hardener
		new BuySellList(4042, 100, 0.000000, 0), // enria
		new BuySellList(4043, 100, 0.000000, 0), // asofe
		new BuySellList(4044, 100, 0.000000, 0), // thons
		new BuySellList(1878, 100, 0.000000, 0), // braided_hemp
		new BuySellList(1879, 100, 0.000000, 0), // cokes
		new BuySellList(1880, 100, 0.000000, 0), // steel
		new BuySellList(1881, 100, 0.000000, 0), // coarse_bone_powder
		new BuySellList(1882, 100, 0.000000, 0), // leather
		new BuySellList(5220, 100, 0.000000, 0), // reinforcing_agent
		new BuySellList(5549, 100, 0.000000, 0), // iron_thread
		new BuySellList(1883, 100, 0.000000, 0), // steel_mold
		new BuySellList(1884, 100, 0.000000, 0), // cord
		new BuySellList(1885, 100, 0.000000, 0), // high_grade_suede
		new BuySellList(1886, 100, 0.000000, 0), // silver_mold
		new BuySellList(1887, 100, 0.000000, 0), // varnish_of_purity
		new BuySellList(1888, 100, 0.000000, 0), // synthesis_cokes
		new BuySellList(1889, 100, 0.000000, 0), // compound_braid
		new BuySellList(5550, 100, 0.000000, 0), // reinforcing_plate
		new BuySellList(1890, 100, 0.000000, 0), // mithirl_alloy
		new BuySellList(1891, 100, 0.000000, 0), // artisan's_frame
		new BuySellList(1892, 100, 0.000000, 0), // blacksmith's_frame
		new BuySellList(1893, 100, 0.000000, 0), // oriharukon
		new BuySellList(1894, 100, 0.000000, 0), // crafted_leather
		new BuySellList(1895, 100, 0.000000, 0), // metallic_fiber
		new BuySellList(4045, 100, 0.000000, 0), // maestro_holder
		new BuySellList(4046, 100, 0.000000, 0), // maestro_anvil_lock
		new BuySellList(5551, 100, 0.000000, 0), // reorins_mold
		new BuySellList(4047, 100, 0.000000, 0), // craftsman_mold
		new BuySellList(4048, 100, 0.000000, 0), // maestro_mold
		new BuySellList(5552, 100, 0.000000, 0), // warsmith_mold
		new BuySellList(5553, 100, 0.000000, 0), // arcsmith_anvil
		new BuySellList(5554, 100, 0.000000, 0), // warsmith_holder
		new BuySellList(6535, 100, 0.000000, 0), // fishing_shot_none
		new BuySellList(6536, 100, 0.000000, 0), // fishing_shot_d
		new BuySellList(6537, 100, 0.000000, 0), // fishing_shot_c
		new BuySellList(6538, 100, 0.000000, 0), // fishing_shot_b
		new BuySellList(6539, 100, 0.000000, 0), // fishing_shot_a
		new BuySellList(6540, 100, 0.000000, 0), // fishing_shot_s
		new BuySellList(9633, 100, 0.000000, 0), // bone_bolt
		new BuySellList(9634, 100, 0.000000, 0), // fine_steel_bolt
		new BuySellList(9635, 100, 0.000000, 0), // silver_bolt
		new BuySellList(9636, 100, 0.000000, 0), // mithril_bolt
		new BuySellList(9637, 100, 0.000000, 0), // shining_bolt
		new BuySellList(9628, 100, 0.000000, 0), // renad
		new BuySellList(9629, 100, 0.000000, 0), // adamantium
		new BuySellList(9630, 100, 0.000000, 0), // oricalcum
		new BuySellList(729, 100, 0.000000, 0), // scrl_of_ench_wp_a
		new BuySellList(730, 100, 0.000000, 0), // scrl_of_ench_am_a
		new BuySellList(947, 100, 0.000000, 0), // scrl_of_ench_wp_b
		new BuySellList(948, 100, 0.000000, 0), // scrl_of_ench_am_b
		new BuySellList(951, 100, 0.000000, 0), // scrl_of_ench_wp_c
		new BuySellList(952, 100, 0.000000, 0), // scrl_of_ench_am_c
		new BuySellList(955, 100, 0.000000, 0), // scrl_of_ench_wp_d
		new BuySellList(956, 100, 0.000000, 0), // scrl_of_ench_am_d
		new BuySellList(959, 100, 0.000000, 0), // scrl_of_ench_wp_s
		new BuySellList(960, 100, 0.000000, 0), // scrl_of_ench_am_s
		new BuySellList(8184, 100, 0.000000, 0), // party_hat
		new BuySellList(8185, 100, 0.000000, 0), // chaperon_of_dresser
		new BuySellList(8186, 100, 0.000000, 0), // goggle_of_artisan
		new BuySellList(8187, 100, 0.000000, 0), // horn_of_reddevil
		new BuySellList(8188, 100, 0.000000, 0), // wing_of_little_angel
		new BuySellList(8189, 100, 0.000000, 0), // feeler_of_fairy
		new BuySellList(8560, 100, 0.000000, 0), // bear_cap
		new BuySellList(8561, 100, 0.000000, 0), // pig_cap
		new BuySellList(8562, 100, 0.000000, 0), // jester_cap
		new BuySellList(8563, 100, 0.000000, 0), // magician_cap
		new BuySellList(8564, 100, 0.000000, 0), // dandy_cap
		new BuySellList(8565, 100, 0.000000, 0) // romantic_shaperon
	};
	
	private static final BuySellList[] _sellList1 = new BuySellList[] {
		new BuySellList(1788, 100, 0.000000, 0), // rp_bow
		new BuySellList(1786, 100, 0.000000, 0), // rp_broad_sword
		new BuySellList(1787, 100, 0.000000, 0), // rp_willow_staff
		new BuySellList(2135, 100, 0.000000, 0), // rp_braided_hemp
		new BuySellList(1791, 100, 0.000000, 0), // rp_brandish
		new BuySellList(1789, 100, 0.000000, 0), // rp_cedar_staff
		new BuySellList(2138, 100, 0.000000, 0), // rp_coarse_bone_powder
		new BuySellList(2136, 100, 0.000000, 0), // rp_cokes
		new BuySellList(1790, 100, 0.000000, 0), // rp_dirk
		new BuySellList(1814, 100, 0.000000, 0), // rp_leather
		new BuySellList(1797, 100, 0.000000, 0), // rp_leather_hose
		new BuySellList(1795, 100, 0.000000, 0), // rp_leather_shoes
		new BuySellList(1796, 100, 0.000000, 0), // rp_leather_tunic
		new BuySellList(1802, 100, 0.000000, 0), // rp_necklace_of_anguish
		new BuySellList(2137, 100, 0.000000, 0), // rp_steel
		new BuySellList(1666, 100, 0.000000, 0), // rp_wooden_arrow
		new BuySellList(1794, 100, 0.000000, 0), // rp_bow_of_forest
		new BuySellList(1799, 100, 0.000000, 0), // rp_leather_gloves
		new BuySellList(1798, 100, 0.000000, 0), // rp_leather_helmet
		new BuySellList(1803, 100, 0.000000, 0), // rp_necklace_of_wisdom
		new BuySellList(1792, 100, 0.000000, 0), // rp_short_spear
		new BuySellList(1793, 100, 0.000000, 0), // rp_sword_of_reflexion
		new BuySellList(1800, 100, 0.000000, 0), // rp_piece_bone_breastplate
		new BuySellList(1801, 100, 0.000000, 0), // rp_piece_bone_gaiters
		new BuySellList(2150, 100, 0.000000, 0), // rp_blue_diamond_necklace
		new BuySellList(2175, 100, 0.000000, 0), // rp_boots
		new BuySellList(2254, 100, 0.000000, 0), // rp_composition_bow
		new BuySellList(2174, 100, 0.000000, 0), // rp_hard_leather_gaiters
		new BuySellList(2173, 100, 0.000000, 0), // rp_hard_leather_shirt
		new BuySellList(2252, 100, 0.000000, 0), // rp_iron_hammer
		new BuySellList(2253, 100, 0.000000, 0), // rp_sword_breaker
		new BuySellList(2144, 100, 0.000000, 0), // rp_compound_braid
		new BuySellList(1817, 100, 0.000000, 0), // rp_cord
		new BuySellList(2140, 100, 0.000000, 0), // rp_high_grade_suede
		new BuySellList(2141, 100, 0.000000, 0), // rp_silver_mold
		new BuySellList(2139, 100, 0.000000, 0), // rp_steel_mold
		new BuySellList(2143, 100, 0.000000, 0), // rp_synthesis_cokes
		new BuySellList(2142, 100, 0.000000, 0) // rp_varnish_of_purity
	};
	
	private static final BuySellList[] _sellList2 = new BuySellList[] {
		new BuySellList(8294, 100, 0.000000, 0), // rp_iron_sword
		new BuySellList(8295, 100, 0.000000, 0), // rp_giants_sword
		new BuySellList(8301, 100, 0.000000, 0), // rp_sword_of_priest
		new BuySellList(8302, 100, 0.000000, 0), // rp_sword_of_magic_fog
		new BuySellList(8307, 100, 0.000000, 0), // rp_mace_of_priest
		new BuySellList(8317, 100, 0.000000, 0), // rp_giants_hammer
		new BuySellList(2256, 100, 0.000000, 0), // rp_assassin_knife
		new BuySellList(2177, 100, 0.000000, 0), // rp_bone_helmet
		new BuySellList(2179, 100, 0.000000, 0), // rp_excellence_leather_gloves
		new BuySellList(2176, 100, 0.000000, 0), // rp_leather_boots
		new BuySellList(2151, 100, 0.000000, 0), // rp_necklace_of_devotion
		new BuySellList(2255, 100, 0.000000, 0), // rp_saber
		new BuySellList(2258, 100, 0.000000, 0), // rp_temptation_of_abyss
		new BuySellList(2257, 100, 0.000000, 0), // rp_trident
		new BuySellList(5278, 100, 0.000000, 0), // rp_adv_comp_bspiritshot_d
		new BuySellList(5268, 100, 0.000000, 0), // rp_adv_comp_soulshot_d
		new BuySellList(5273, 100, 0.000000, 0), // rp_adv_comp_spiritshot_d
		new BuySellList(3953, 100, 0.000000, 0), // rp_blessed_spiritshot_d
		new BuySellList(5163, 100, 0.000000, 0), // rp_comp_bspiritshot_d
		new BuySellList(5153, 100, 0.000000, 0), // rp_comp_soulshot_d
		new BuySellList(5158, 100, 0.000000, 0), // rp_comp_spiritshot_d
		new BuySellList(1804, 100, 0.000000, 0), // rp_soulshot_d
		new BuySellList(3032, 100, 0.000000, 0), // rp_spiritshot_d
		new BuySellList(2261, 100, 0.000000, 0), // rp_conjure_staff
		new BuySellList(2178, 100, 0.000000, 0), // rp_dark_hose
		new BuySellList(2263, 100, 0.000000, 0), // rp_dwarven_trident
		new BuySellList(2262, 100, 0.000000, 0), // rp_elven_bow
		new BuySellList(2152, 100, 0.000000, 0), // rp_enchanted_necklace
		new BuySellList(2260, 100, 0.000000, 0), // rp_mace_of_judgment
		new BuySellList(2182, 100, 0.000000, 0), // rp_scale_gaiters
		new BuySellList(2180, 100, 0.000000, 0), // rp_scale_mail
		new BuySellList(2259, 100, 0.000000, 0), // rp_spinebone_sword
		new BuySellList(2181, 100, 0.000000, 0), // rp_white_tunic
		new BuySellList(5437, 100, 0.000000, 0), // rp_heavy_sword
		new BuySellList(2250, 100, 0.000000, 0), // rp_bone_arrow
		new BuySellList(2267, 100, 0.000000, 0), // rp_gastraphetes
		new BuySellList(2185, 100, 0.000000, 0), // rp_iron_boots
		new BuySellList(2266, 100, 0.000000, 0), // rp_knife_o'_silenus
		new BuySellList(2184, 100, 0.000000, 0), // rp_mithril_banded_gaiters
		new BuySellList(2183, 100, 0.000000, 0), // rp_mithril_banded_mail
		new BuySellList(2265, 100, 0.000000, 0), // rp_spike_club
		new BuySellList(2153, 100, 0.000000, 0), // rp_tiger'seye_earing
		new BuySellList(2268, 100, 0.000000, 0), // rp_tome_of_blood
		new BuySellList(2264, 100, 0.000000, 0), // rp_two-handed_sword
		new BuySellList(2277, 100, 0.000000, 0), // rp_bich'hwa
		new BuySellList(2193, 100, 0.000000, 0), // rp_boots_of_power
		new BuySellList(2186, 100, 0.000000, 0), // rp_brigandine
		new BuySellList(2977, 100, 0.000000, 0), // rp_brigandine_boots
		new BuySellList(2972, 100, 0.000000, 0), // rp_brigandine_gaiters
		new BuySellList(2980, 100, 0.000000, 0), // rp_brigandine_gauntlet
		new BuySellList(2975, 100, 0.000000, 0), // rp_brigandine_helmet
		new BuySellList(2982, 100, 0.000000, 0), // rp_brigandine_shield
		new BuySellList(2280, 100, 0.000000, 0), // rp_crucifix_of_blood
		new BuySellList(2282, 100, 0.000000, 0), // rp_cursed_maingauche
		new BuySellList(2154, 100, 0.000000, 0), // rp_elven_earing
		new BuySellList(2978, 100, 0.000000, 0), // rp_elven_mithril_boots
		new BuySellList(2981, 100, 0.000000, 0), // rp_elven_mithril_gloves
		new BuySellList(2156, 100, 0.000000, 0), // rp_elven_necklace
		new BuySellList(2155, 100, 0.000000, 0), // rp_elven_ring
		new BuySellList(2191, 100, 0.000000, 0), // rp_gauntlet_of_repose_of_the_soul
		new BuySellList(2270, 100, 0.000000, 0), // rp_goathead_staff
		new BuySellList(2279, 100, 0.000000, 0), // rp_hammer_in_flames
		new BuySellList(2275, 100, 0.000000, 0), // rp_heavy_bone_club
		new BuySellList(2192, 100, 0.000000, 0), // rp_kite_shield
		new BuySellList(2276, 100, 0.000000, 0), // rp_maingauche
		new BuySellList(2976, 100, 0.000000, 0), // rp_manticor_skin_boots
		new BuySellList(2188, 100, 0.000000, 0), // rp_manticor_skin_gaiters
		new BuySellList(2979, 100, 0.000000, 0), // rp_manticor_skin_gloves
		new BuySellList(2187, 100, 0.000000, 0), // rp_manticor_skin_shirt
		new BuySellList(2190, 100, 0.000000, 0), // rp_mithril_hose
		new BuySellList(2971, 100, 0.000000, 0), // rp_mithril_scale_gaiters
		new BuySellList(2189, 100, 0.000000, 0), // rp_mithril_tunic
		new BuySellList(2269, 100, 0.000000, 0), // rp_morning_star
		new BuySellList(2274, 100, 0.000000, 0), // rp_skull_breaker
		new BuySellList(2278, 100, 0.000000, 0), // rp_strengthening_long_bow
		new BuySellList(2272, 100, 0.000000, 0), // rp_sword_of_revolution
		new BuySellList(2273, 100, 0.000000, 0), // rp_tarbar
		new BuySellList(2271, 100, 0.000000, 0), // rp_winged_spear
		new BuySellList(2202, 100, 0.000000, 0), // rp_assault_boots
		new BuySellList(2287, 100, 0.000000, 0), // rp_atuba_hammer
		new BuySellList(2296, 100, 0.000000, 0), // rp_atuba_mace
		new BuySellList(2286, 100, 0.000000, 0), // rp_bonebreaker
		new BuySellList(2285, 100, 0.000000, 0), // rp_claymore
		new BuySellList(2292, 100, 0.000000, 0), // rp_cyclone_bow
		new BuySellList(3020, 100, 0.000000, 0), // rp_elven_long_sword
		new BuySellList(2288, 100, 0.000000, 0), // rp_ghost_staff
		new BuySellList(2293, 100, 0.000000, 0), // rp_glaive
		new BuySellList(2195, 100, 0.000000, 0), // rp_half_plate
		new BuySellList(2289, 100, 0.000000, 0), // rp_life_stick
		new BuySellList(2290, 100, 0.000000, 0), // rp_mithril_dagger
		new BuySellList(2194, 100, 0.000000, 0), // rp_mithril_glove
		new BuySellList(2158, 100, 0.000000, 0), // rp_mithril_ring
		new BuySellList(2159, 100, 0.000000, 0), // rp_necklace_of_darkness
		new BuySellList(2208, 100, 0.000000, 0), // rp_ogre_power_gauntlet
		new BuySellList(2157, 100, 0.000000, 0), // rp_onyxbeast'seye_earing
		new BuySellList(2985, 100, 0.000000, 0), // rp_plate_boots
		new BuySellList(2196, 100, 0.000000, 0), // rp_plate_gaiters
		new BuySellList(2983, 100, 0.000000, 0), // rp_plate_helmet
		new BuySellList(2987, 100, 0.000000, 0), // rp_plate_shield
		new BuySellList(2198, 100, 0.000000, 0), // rp_sage's_rag
		new BuySellList(2986, 100, 0.000000, 0), // rp_sage's_worn_gloves
		new BuySellList(2291, 100, 0.000000, 0), // rp_scallop_jamadhr
		new BuySellList(2984, 100, 0.000000, 0), // rp_slamander_skin_boots
		new BuySellList(2197, 100, 0.000000, 0), // rp_slamander_skin_mail
		new BuySellList(2201, 100, 0.000000, 0), // rp_square_shield
		new BuySellList(2146, 100, 0.000000, 0), // rp_artisan's_frame
		new BuySellList(2147, 100, 0.000000, 0), // rp_blacksmith's_frame
		new BuySellList(2148, 100, 0.000000, 0), // rp_crafted_leather
		new BuySellList(5472, 100, 0.000000, 0), // rp_iron_thread
		new BuySellList(2149, 100, 0.000000, 0), // rp_metallic_fiber
		new BuySellList(2145, 100, 0.000000, 0), // rp_mithirl_alloy
		new BuySellList(1825, 100, 0.000000, 0), // rp_oriharukon
		new BuySellList(5231, 100, 0.000000, 0), // rp_reinforcing_agent
		new BuySellList(5473, 100, 0.000000, 0) // rp_reinforcing_plate
	};
	
	private static final BuySellList[] _sellList3 = new BuySellList[] {
		new BuySellList(8296, 100, 0.000000, 0), // rp_sword_of_paagrio
		new BuySellList(8303, 100, 0.000000, 0), // rp_mystery_sword
		new BuySellList(8304, 100, 0.000000, 0), // rp_sword_of_eclipse
		new BuySellList(8308, 100, 0.000000, 0), // rp_eclipse_axe
		new BuySellList(8318, 100, 0.000000, 0), // rp_dwarven_hammer
		new BuySellList(8319, 100, 0.000000, 0), // rp_horn_of_karik
		new BuySellList(2162, 100, 0.000000, 0), // rp_aquastone_necklace
		new BuySellList(2161, 100, 0.000000, 0), // rp_aquastone_ring
		new BuySellList(2301, 100, 0.000000, 0), // rp_battle_axe
		new BuySellList(2299, 100, 0.000000, 0), // rp_big_hammer
		new BuySellList(2314, 100, 0.000000, 0), // rp_body_slasher
		new BuySellList(2994, 100, 0.000000, 0), // rp_boots_of_seal
		new BuySellList(2990, 100, 0.000000, 0), // rp_chain_boots
		new BuySellList(2205, 100, 0.000000, 0), // rp_chain_gaiters
		new BuySellList(2996, 100, 0.000000, 0), // rp_chain_gloves
		new BuySellList(2989, 100, 0.000000, 0), // rp_chain_hood
		new BuySellList(2204, 100, 0.000000, 0), // rp_chain_mail_shirt
		new BuySellList(2998, 100, 0.000000, 0), // rp_chain_shield
		new BuySellList(2311, 100, 0.000000, 0), // rp_chakram
		new BuySellList(2305, 100, 0.000000, 0), // rp_crystal_staff
		new BuySellList(2312, 100, 0.000000, 0), // rp_crystallized_ice_bow
		new BuySellList(2308, 100, 0.000000, 0), // rp_cursed_dagger
		new BuySellList(2310, 100, 0.000000, 0), // rp_darkelven_dagger
		new BuySellList(2993, 100, 0.000000, 0), // rp_dwarven_chain_boots
		new BuySellList(2209, 100, 0.000000, 0), // rp_eldarake
		new BuySellList(2297, 100, 0.000000, 0), // rp_flamberge
		new BuySellList(2307, 100, 0.000000, 0), // rp_heavy_doom_axe
		new BuySellList(2304, 100, 0.000000, 0), // rp_heavy_doom_hammer
		new BuySellList(2991, 100, 0.000000, 0), // rp_karmian_boots
		new BuySellList(2997, 100, 0.000000, 0), // rp_karmian_gloves
		new BuySellList(2199, 100, 0.000000, 0), // rp_karmian_hose
		new BuySellList(2207, 100, 0.000000, 0), // rp_karmian_tunic
		new BuySellList(2203, 100, 0.000000, 0), // rp_mithril_boots
		new BuySellList(2160, 100, 0.000000, 0), // rp_moonstone_earing
		new BuySellList(2313, 100, 0.000000, 0), // rp_orcish_glaive
		new BuySellList(2992, 100, 0.000000, 0), // rp_plate_leather_boots
		new BuySellList(2995, 100, 0.000000, 0), // rp_reinforce_mithril_gloves
		new BuySellList(2300, 100, 0.000000, 0), // rp_scythe
		new BuySellList(2303, 100, 0.000000, 0), // rp_skull_graver
		new BuySellList(2306, 100, 0.000000, 0), // rp_stick_of_faith
		new BuySellList(2298, 100, 0.000000, 0), // rp_stormbringer
		new BuySellList(2988, 100, 0.000000, 0), // rp_tempered_mithril_gaiters
		new BuySellList(2206, 100, 0.000000, 0), // rp_tempered_mithril_shirt
		new BuySellList(2302, 100, 0.000000, 0), // rp_war_pick
		new BuySellList(5279, 100, 0.000000, 0), // rp_adv_comp_bspiritshot_c
		new BuySellList(5269, 100, 0.000000, 0), // rp_adv_comp_soulshot_c
		new BuySellList(5274, 100, 0.000000, 0), // rp_adv_comp_spiritshot_c
		new BuySellList(3954, 100, 0.000000, 0), // rp_blessed_spiritshot_c
		new BuySellList(5164, 100, 0.000000, 0), // rp_comp_bspiritshot_c
		new BuySellList(5154, 100, 0.000000, 0), // rp_comp_soulshot_c
		new BuySellList(5159, 100, 0.000000, 0), // rp_comp_spiritshot_c
		new BuySellList(3000, 100, 0.000000, 0), // rp_dwarven_chain_shield
		new BuySellList(2211, 100, 0.000000, 0), // rp_plate_leather
		new BuySellList(2212, 100, 0.000000, 0), // rp_plate_leather_gaiters
		new BuySellList(2999, 100, 0.000000, 0), // rp_plate_leather_gloves
		new BuySellList(1805, 100, 0.000000, 0), // rp_soulshot_c
		new BuySellList(3033, 100, 0.000000, 0), // rp_spiritshot_c
		new BuySellList(2317, 100, 0.000000, 0), // rp_bech_de_corbin
		new BuySellList(2219, 100, 0.000000, 0), // rp_crimson_boots
		new BuySellList(2320, 100, 0.000000, 0), // rp_cursed_staff
		new BuySellList(2322, 100, 0.000000, 0), // rp_dagger_of_magicflame
		new BuySellList(2214, 100, 0.000000, 0), // rp_dwarven_chain_gaiters
		new BuySellList(3002, 100, 0.000000, 0), // rp_dwarven_chain_gloves
		new BuySellList(2213, 100, 0.000000, 0), // rp_dwarven_chain_mail_shirt
		new BuySellList(3021, 100, 0.000000, 0), // rp_dwarven_warhammer
		new BuySellList(2163, 100, 0.000000, 0), // rp_earing_of_protection
		new BuySellList(2323, 100, 0.000000, 0), // rp_elemental_bow
		new BuySellList(3003, 100, 0.000000, 0), // rp_gloves_of_seal
		new BuySellList(2216, 100, 0.000000, 0), // rp_great_helmet
		new BuySellList(2326, 100, 0.000000, 0), // rp_horn_of_glory
		new BuySellList(2316, 100, 0.000000, 0), // rp_katana
		new BuySellList(2217, 100, 0.000000, 0), // rp_knight_shield
		new BuySellList(4440, 100, 0.000000, 0), // rp_knuckle_dust
		new BuySellList(2165, 100, 0.000000, 0), // rp_necklace_of_protection
		new BuySellList(2324, 100, 0.000000, 0), // rp_noble_elven_bow
		new BuySellList(2319, 100, 0.000000, 0), // rp_raid_sword
		new BuySellList(3001, 100, 0.000000, 0), // rp_rind_leather_boots
		new BuySellList(2221, 100, 0.000000, 0), // rp_rind_leather_gaiters
		new BuySellList(3004, 100, 0.000000, 0), // rp_rind_leather_gloves
		new BuySellList(2220, 100, 0.000000, 0), // rp_rind_leather_mail
		new BuySellList(2164, 100, 0.000000, 0), // rp_ring_of_protection
		new BuySellList(2215, 100, 0.000000, 0), // rp_robe_of_seal
		new BuySellList(2315, 100, 0.000000, 0), // rp_shamshir
		new BuySellList(2318, 100, 0.000000, 0), // rp_spirits_sword
		new BuySellList(2321, 100, 0.000000, 0), // rp_stiletto
		new BuySellList(2251, 100, 0.000000, 0), // rp_fine_steel_arrow
		new BuySellList(2347, 100, 0.000000, 0), // rp_akat_long_bow
		new BuySellList(2327, 100, 0.000000, 0), // rp_caliburs
		new BuySellList(2342, 100, 0.000000, 0), // rp_club_of_nature
		new BuySellList(2222, 100, 0.000000, 0), // rp_composite_armor
		new BuySellList(3009, 100, 0.000000, 0), // rp_composite_boots
		new BuySellList(3010, 100, 0.000000, 0), // rp_composite_helmet
		new BuySellList(4132, 100, 0.000000, 0), // rp_composite_shield
		new BuySellList(2345, 100, 0.000000, 0), // rp_dark_screamer
		new BuySellList(2334, 100, 0.000000, 0), // rp_deathbreath_sword
		new BuySellList(3005, 100, 0.000000, 0), // rp_demon's_boots
		new BuySellList(3006, 100, 0.000000, 0), // rp_demon's_gloves
		new BuySellList(2225, 100, 0.000000, 0), // rp_demon's_hose
		new BuySellList(2224, 100, 0.000000, 0), // rp_demon's_tunic
		new BuySellList(2166, 100, 0.000000, 0), // rp_earing_of_binding
		new BuySellList(2346, 100, 0.000000, 0), // rp_fist_blade
		new BuySellList(2344, 100, 0.000000, 0), // rp_grace_dagger
		new BuySellList(2348, 100, 0.000000, 0), // rp_heathen's_book
		new BuySellList(2330, 100, 0.000000, 0), // rp_homunkulus's_sword
		new BuySellList(2343, 100, 0.000000, 0), // rp_mace_of_underworld
		new BuySellList(2226, 100, 0.000000, 0), // rp_mithril_gauntlet
		new BuySellList(2168, 100, 0.000000, 0), // rp_necklace_of_mermaid
		new BuySellList(2336, 100, 0.000000, 0), // rp_nirvana_axe
		new BuySellList(2340, 100, 0.000000, 0), // rp_paagrio_hammer
		new BuySellList(2338, 100, 0.000000, 0), // rp_paradia_staff
		new BuySellList(2167, 100, 0.000000, 0), // rp_ring_of_ages
		new BuySellList(2341, 100, 0.000000, 0), // rp_sage's_staff
		new BuySellList(2228, 100, 0.000000, 0), // rp_shining_circlet
		new BuySellList(2337, 100, 0.000000, 0), // rp_stick_of_eternity
		new BuySellList(2328, 100, 0.000000, 0), // rp_sword_of_delusion
		new BuySellList(2333, 100, 0.000000, 0), // rp_sword_of_nightmare
		new BuySellList(2223, 100, 0.000000, 0), // rp_tower_shield
		new BuySellList(2329, 100, 0.000000, 0), // rp_tsurugi
		new BuySellList(2335, 100, 0.000000, 0), // rp_war_axe
		new BuySellList(2350, 100, 0.000000, 0), // rp_paagrio_axe
		new BuySellList(2351, 100, 0.000000, 0), // rp_scorpion
		new BuySellList(3007, 100, 0.000000, 0), // rp_theca_leather_boots
		new BuySellList(2230, 100, 0.000000, 0), // rp_theca_leather_gaiters
		new BuySellList(3008, 100, 0.000000, 0), // rp_theca_leather_gloves
		new BuySellList(2229, 100, 0.000000, 0), // rp_theca_leather_mail
		new BuySellList(2352, 100, 0.000000, 0), // rp_widow_maker
		new BuySellList(3017, 100, 0.000000, 0), // rp_blessed_gloves
		new BuySellList(4124, 100, 0.000000, 0), // rp_craftsman_mold
		new BuySellList(2357, 100, 0.000000, 0), // rp_crystal_dagger
		new BuySellList(2354, 100, 0.000000, 0), // rp_deadman's_staff
		new BuySellList(2356, 100, 0.000000, 0), // rp_demon's_staff
		new BuySellList(2234, 100, 0.000000, 0), // rp_divine_hose
		new BuySellList(2233, 100, 0.000000, 0), // rp_divine_tunic
		new BuySellList(3013, 100, 0.000000, 0), // rp_drake_leather_boots
		new BuySellList(3015, 100, 0.000000, 0), // rp_drake_leather_gloves
		new BuySellList(2232, 100, 0.000000, 0), // rp_drake_leather_mail
		new BuySellList(2359, 100, 0.000000, 0), // rp_eminence_bow
		new BuySellList(2231, 100, 0.000000, 0), // rp_full_plate_armor
		new BuySellList(3014, 100, 0.000000, 0), // rp_full_plate_boots
		new BuySellList(3016, 100, 0.000000, 0), // rp_full_plate_gauntlet
		new BuySellList(3012, 100, 0.000000, 0), // rp_full_plate_helmet
		new BuySellList(3019, 100, 0.000000, 0), // rp_full_plate_shield
		new BuySellList(2355, 100, 0.000000, 0), // rp_ghoul's_staff
		new BuySellList(2358, 100, 0.000000, 0), // rp_great_pata
		new BuySellList(4123, 100, 0.000000, 0), // rp_maestro_anvil_lock
		new BuySellList(4122, 100, 0.000000, 0), // rp_maestro_holder
		new BuySellList(4125, 100, 0.000000, 0), // rp_maestro_mold
		new BuySellList(2170, 100, 0.000000, 0), // rp_nassen's_earing
		new BuySellList(2169, 100, 0.000000, 0), // rp_necklace_of_binding
		new BuySellList(2360, 100, 0.000000, 0), // rp_orcish_poleaxe
		new BuySellList(5474, 100, 0.000000, 0), // rp_reorins_mold
		new BuySellList(2970, 100, 0.000000, 0), // rp_ring_of_binding
		new BuySellList(2353, 100, 0.000000, 0), // rp_samurai_longsword
		new BuySellList(3022, 100, 0.000000, 0), // rp_yaksa_mace
		new BuySellList(5436, 100, 0.000000, 0) // rp_berserker_blade
	};
	
	private static final BuySellList[] _sellList4 = new BuySellList[] {
		new BuySellList(8297, 100, 0.000000, 0), // rp_guardians_sword
		new BuySellList(8298, 100, 0.000000, 0), // rp_guardians_sword_i
		new BuySellList(8305, 100, 0.000000, 0), // rp_tears_of_wizard
		new BuySellList(8306, 100, 0.000000, 0), // rp_tears_of_wizard_i
		new BuySellList(8309, 100, 0.000000, 0), // rp_spell_breaker
		new BuySellList(8310, 100, 0.000000, 0), // rp_spell_breaker_i
		new BuySellList(8311, 100, 0.000000, 0), // rp_bone_of_kaim_vanul
		new BuySellList(8312, 100, 0.000000, 0), // rp_bone_of_kaim_vanul_i
		new BuySellList(8321, 100, 0.000000, 0), // rp_ice_storm_hammer
		new BuySellList(8322, 100, 0.000000, 0), // rp_ice_storm_hammer_i
		new BuySellList(8323, 100, 0.000000, 0), // rp_star_buster
		new BuySellList(8324, 100, 0.000000, 0), // rp_star_buster_i
		new BuySellList(4126, 100, 0.000000, 0), // rp_adamantite_earing
		new BuySellList(4937, 100, 0.000000, 0), // rp_adamantite_earing_i
		new BuySellList(4128, 100, 0.000000, 0), // rp_adamantite_necklace
		new BuySellList(4939, 100, 0.000000, 0), // rp_adamantite_necklace_i
		new BuySellList(4127, 100, 0.000000, 0), // rp_adamantite_ring
		new BuySellList(4938, 100, 0.000000, 0), // rp_adamantite_ring_i
		new BuySellList(4189, 100, 0.000000, 0), // rp_arthro_nail
		new BuySellList(4970, 100, 0.000000, 0), // rp_arthro_nail_i
		new BuySellList(4175, 100, 0.000000, 0), // rp_avadon_boots
		new BuySellList(4959, 100, 0.000000, 0), // rp_avadon_boots_i
		new BuySellList(4141, 100, 0.000000, 0), // rp_avadon_breastplate
		new BuySellList(4944, 100, 0.000000, 0), // rp_avadon_breastplate_i
		new BuySellList(4149, 100, 0.000000, 0), // rp_avadon_circlet
		new BuySellList(4952, 100, 0.000000, 0), // rp_avadon_circlet_i
		new BuySellList(4142, 100, 0.000000, 0), // rp_avadon_gaiters
		new BuySellList(4945, 100, 0.000000, 0), // rp_avadon_gaiters_i
		new BuySellList(4150, 100, 0.000000, 0), // rp_avadon_gloves
		new BuySellList(4953, 100, 0.000000, 0), // rp_avadon_gloves_i
		new BuySellList(4145, 100, 0.000000, 0), // rp_avadon_leather_mail
		new BuySellList(4948, 100, 0.000000, 0), // rp_avadon_leather_mail_i
		new BuySellList(4148, 100, 0.000000, 0), // rp_avadon_robe
		new BuySellList(4951, 100, 0.000000, 0), // rp_avadon_robe_i
		new BuySellList(4441, 100, 0.000000, 0), // rp_avadon_shield
		new BuySellList(4936, 100, 0.000000, 0), // rp_avadon_shield_i
		new BuySellList(4190, 100, 0.000000, 0), // rp_dark_elven_long_bow
		new BuySellList(4971, 100, 0.000000, 0), // rp_dark_elven_long_bow_i
		new BuySellList(4191, 100, 0.000000, 0), // rp_great_axe
		new BuySellList(4972, 100, 0.000000, 0), // rp_great_axe_i
		new BuySellList(4182, 100, 0.000000, 0), // rp_great_sword
		new BuySellList(4963, 100, 0.000000, 0), // rp_great_sword_i
		new BuySellList(4183, 100, 0.000000, 0), // rp_heavy_war_axe
		new BuySellList(4964, 100, 0.000000, 0), // rp_heavy_war_axe_i
		new BuySellList(4188, 100, 0.000000, 0), // rp_hell_knife
		new BuySellList(4969, 100, 0.000000, 0), // rp_hell_knife_i
		new BuySellList(4147, 100, 0.000000, 0), // rp_hose_of_shrnoen
		new BuySellList(4950, 100, 0.000000, 0), // rp_hose_of_shrnoen_i
		new BuySellList(4187, 100, 0.000000, 0), // rp_kris
		new BuySellList(4968, 100, 0.000000, 0), // rp_kris_i
		new BuySellList(4185, 100, 0.000000, 0), // rp_kshanberk
		new BuySellList(4966, 100, 0.000000, 0), // rp_kshanberk_i
		new BuySellList(4174, 100, 0.000000, 0), // rp_shrnoen's_boots
		new BuySellList(4958, 100, 0.000000, 0), // rp_shrnoen's_boots_i
		new BuySellList(4133, 100, 0.000000, 0), // rp_shrnoen's_breastplate
		new BuySellList(4940, 100, 0.000000, 0), // rp_shrnoen's_breastplate_i
		new BuySellList(4134, 100, 0.000000, 0), // rp_shrnoen's_gaiters
		new BuySellList(4941, 100, 0.000000, 0), // rp_shrnoen's_gaiters_i
		new BuySellList(4177, 100, 0.000000, 0), // rp_shrnoen's_gauntlet
		new BuySellList(4960, 100, 0.000000, 0), // rp_shrnoen's_gauntlet_i
		new BuySellList(4179, 100, 0.000000, 0), // rp_shrnoen's_helmet
		new BuySellList(4962, 100, 0.000000, 0), // rp_shrnoen's_helmet_i
		new BuySellList(4144, 100, 0.000000, 0), // rp_shrnoen's_leather_gaiters
		new BuySellList(4947, 100, 0.000000, 0), // rp_shrnoen's_leather_gaiters_i
		new BuySellList(4143, 100, 0.000000, 0), // rp_shrnoen's_leather_shirts
		new BuySellList(4946, 100, 0.000000, 0), // rp_shrnoen's_leather_shirts_i
		new BuySellList(4178, 100, 0.000000, 0), // rp_shrnoen's_shield
		new BuySellList(4961, 100, 0.000000, 0), // rp_shrnoen's_shield_i
		new BuySellList(4184, 100, 0.000000, 0), // rp_sprite's_staff
		new BuySellList(4965, 100, 0.000000, 0), // rp_sprite's_staff_i
		new BuySellList(4186, 100, 0.000000, 0), // rp_sword_of_valhalla
		new BuySellList(4967, 100, 0.000000, 0), // rp_sword_of_valhalla_i
		new BuySellList(4146, 100, 0.000000, 0), // rp_tunic_of_shrnoen
		new BuySellList(4949, 100, 0.000000, 0), // rp_tunic_of_shrnoen_i
		new BuySellList(5280, 100, 0.000000, 0), // rp_adv_comp_bspiritshot_b
		new BuySellList(5270, 100, 0.000000, 0), // rp_adv_comp_soulshot_b
		new BuySellList(5275, 100, 0.000000, 0), // rp_adv_comp_spiritshot_b
		new BuySellList(3955, 100, 0.000000, 0), // rp_blessed_spiritshot_b
		new BuySellList(5165, 100, 0.000000, 0), // rp_comp_bspiritshot_b
		new BuySellList(5155, 100, 0.000000, 0), // rp_comp_soulshot_b
		new BuySellList(5160, 100, 0.000000, 0), // rp_comp_spiritshot_b
		new BuySellList(4180, 100, 0.000000, 0), // rp_silver_arrow
		new BuySellList(1806, 100, 0.000000, 0), // rp_soulshot_b
		new BuySellList(3034, 100, 0.000000, 0), // rp_spiritshot_b
		new BuySellList(4195, 100, 0.000000, 0), // rp_art_of_battle_axe
		new BuySellList(5003, 100, 0.000000, 0), // rp_art_of_battle_axe_i
		new BuySellList(4198, 100, 0.000000, 0), // rp_bellion_cestus
		new BuySellList(5006, 100, 0.000000, 0), // rp_bellion_cestus_i
		new BuySellList(4167, 100, 0.000000, 0), // rp_blue_wolve's_boots
		new BuySellList(4992, 100, 0.000000, 0), // rp_blue_wolve's_boots_i
		new BuySellList(4155, 100, 0.000000, 0), // rp_blue_wolve's_breastplate
		new BuySellList(4981, 100, 0.000000, 0), // rp_blue_wolve's_breastplate_i
		new BuySellList(4157, 100, 0.000000, 0), // rp_blue_wolve's_gaiters
		new BuySellList(4982, 100, 0.000000, 0), // rp_blue_wolve's_gaiters_i
		new BuySellList(4173, 100, 0.000000, 0), // rp_blue_wolve's_gloves
		new BuySellList(4998, 100, 0.000000, 0), // rp_blue_wolve's_gloves_i
		new BuySellList(4165, 100, 0.000000, 0), // rp_blue_wolve's_helmet
		new BuySellList(4990, 100, 0.000000, 0), // rp_blue_wolve's_helmet_i
		new BuySellList(4163, 100, 0.000000, 0), // rp_blue_wolve's_hose
		new BuySellList(4988, 100, 0.000000, 0), // rp_blue_wolve's_hose_i
		new BuySellList(4159, 100, 0.000000, 0), // rp_blue_wolve's_leather_mail
		new BuySellList(4984, 100, 0.000000, 0), // rp_blue_wolve's_leather_mail_i
		new BuySellList(4161, 100, 0.000000, 0), // rp_blue_wolve's_tunic
		new BuySellList(4986, 100, 0.000000, 0), // rp_blue_wolve's_tunic_i
		new BuySellList(4194, 100, 0.000000, 0), // rp_deadman's_glory
		new BuySellList(5002, 100, 0.000000, 0), // rp_deadman's_glory_i
		new BuySellList(4197, 100, 0.000000, 0), // rp_demon's_sword
		new BuySellList(5005, 100, 0.000000, 0), // rp_demon's_sword_i
		new BuySellList(4176, 100, 0.000000, 0), // rp_doom_boots
		new BuySellList(4999, 100, 0.000000, 0), // rp_doom_boots_i
		new BuySellList(4168, 100, 0.000000, 0), // rp_doom_gloves
		new BuySellList(4993, 100, 0.000000, 0), // rp_doom_gloves_i
		new BuySellList(4166, 100, 0.000000, 0), // rp_doom_helmet
		new BuySellList(4991, 100, 0.000000, 0), // rp_doom_helmet_i
		new BuySellList(4158, 100, 0.000000, 0), // rp_doom_plate_armor
		new BuySellList(4983, 100, 0.000000, 0), // rp_doom_plate_armor_i
		new BuySellList(4154, 100, 0.000000, 0), // rp_doom_shield
		new BuySellList(4980, 100, 0.000000, 0), // rp_doom_shield_i
		new BuySellList(4129, 100, 0.000000, 0), // rp_earing_of_black_ore
		new BuySellList(4973, 100, 0.000000, 0), // rp_earing_of_black_ore_i
		new BuySellList(4199, 100, 0.000000, 0), // rp_hazard_bow
		new BuySellList(5007, 100, 0.000000, 0), // rp_hazard_bow_i
		new BuySellList(4164, 100, 0.000000, 0), // rp_hose_of_doom
		new BuySellList(4989, 100, 0.000000, 0), // rp_hose_of_doom_i
		new BuySellList(4193, 100, 0.000000, 0), // rp_lancia
		new BuySellList(5001, 100, 0.000000, 0), // rp_lancia_i
		new BuySellList(4160, 100, 0.000000, 0), // rp_leather_mail_of_doom
		new BuySellList(4985, 100, 0.000000, 0), // rp_leather_mail_of_doom_i
		new BuySellList(4131, 100, 0.000000, 0), // rp_necklace_of_black_ore
		new BuySellList(4975, 100, 0.000000, 0), // rp_necklace_of_black_ore_i
		new BuySellList(4130, 100, 0.000000, 0), // rp_ring_of_black_ore
		new BuySellList(4974, 100, 0.000000, 0), // rp_ring_of_black_ore_i
		new BuySellList(4196, 100, 0.000000, 0), // rp_staff_of_evil_sprit
		new BuySellList(5004, 100, 0.000000, 0), // rp_staff_of_evil_sprit_i
		new BuySellList(4192, 100, 0.000000, 0), // rp_sword_of_damascus
		new BuySellList(5000, 100, 0.000000, 0), // rp_sword_of_damascus_i
		new BuySellList(4162, 100, 0.000000, 0), // rp_tunic_of_doom
		new BuySellList(4987, 100, 0.000000, 0) // rp_tunic_of_doom_i
	};
	
	private static final BuySellList[] _sellList5 = new BuySellList[] {
		new BuySellList(8299, 100, 0.000000, 0), // rp_inferno_master
		new BuySellList(8300, 100, 0.000000, 0), // rp_inferno_master_i
		new BuySellList(8313, 100, 0.000000, 0), // rp_eye_of_soul
		new BuySellList(8314, 100, 0.000000, 0), // rp_eye_of_soul_i
		new BuySellList(8315, 100, 0.000000, 0), // rp_dragon_flame_head
		new BuySellList(8316, 100, 0.000000, 0), // rp_dragon_flame_head_i
		new BuySellList(8320, 100, 0.000000, 0), // rp_hammer_of_destroyer
		new BuySellList(8487, 100, 0.000000, 0), // rp_hammer_of_destroyer_i
		new BuySellList(8325, 100, 0.000000, 0), // rp_doom_crusher
		new BuySellList(8326, 100, 0.000000, 0), // rp_doom_crusher_i
		new BuySellList(5452, 100, 0.000000, 0), // rp_blood_tornado_i
		new BuySellList(5453, 100, 0.000000, 0), // rp_blood_tornado
		new BuySellList(5446, 100, 0.000000, 0), // rp_bloody_orchid_i
		new BuySellList(5447, 100, 0.000000, 0), // rp_bloody_orchid
		new BuySellList(5444, 100, 0.000000, 0), // rp_carnium_bow_i
		new BuySellList(5445, 100, 0.000000, 0), // rp_carnium_bow
		new BuySellList(5460, 100, 0.000000, 0), // rp_dasparion's_staff_i
		new BuySellList(5461, 100, 0.000000, 0), // rp_dasparion's_staff
		new BuySellList(5468, 100, 0.000000, 0), // rp_elemental_sword_i
		new BuySellList(5469, 100, 0.000000, 0), // rp_elemental_sword
		new BuySellList(5458, 100, 0.000000, 0), // rp_halbard_i
		new BuySellList(5459, 100, 0.000000, 0), // rp_halbard
		new BuySellList(5438, 100, 0.000000, 0), // rp_meteor_shower_i
		new BuySellList(5439, 100, 0.000000, 0), // rp_meteor_shower
		new BuySellList(5230, 100, 0.000000, 0), // rp_mithril_arrow
		new BuySellList(5368, 100, 0.000000, 0), // rp_sealed_dark_crystal_boots_i
		new BuySellList(5369, 100, 0.000000, 0), // rp_sealed_dark_crystal_boots
		new BuySellList(5416, 100, 0.000000, 0), // rp_sealed_dark_crystal_breastplate_i
		new BuySellList(5417, 100, 0.000000, 0), // rp_sealed_dark_crystal_breastplate
		new BuySellList(5424, 100, 0.000000, 0), // rp_sealed_dark_crystal_gaiters_i
		new BuySellList(5425, 100, 0.000000, 0), // rp_sealed_dark_crystal_gaiters
		new BuySellList(5392, 100, 0.000000, 0), // rp_sealed_dark_crystal_gloves_i
		new BuySellList(5393, 100, 0.000000, 0), // rp_sealed_dark_crystal_gloves
		new BuySellList(5426, 100, 0.000000, 0), // rp_sealed_dark_crystal_helmet_i
		new BuySellList(5427, 100, 0.000000, 0), // rp_sealed_dark_crystal_helmet
		new BuySellList(5332, 100, 0.000000, 0), // rp_sealed_dark_crystal_leather_mail_i
		new BuySellList(5333, 100, 0.000000, 0), // rp_sealed_dark_crystal_leather_mail
		new BuySellList(5348, 100, 0.000000, 0), // rp_sealed_dark_crystal_robe_i
		new BuySellList(5349, 100, 0.000000, 0), // rp_sealed_dark_crystal_robe
		new BuySellList(5364, 100, 0.000000, 0), // rp_sealed_dark_crystal_shield_i
		new BuySellList(5365, 100, 0.000000, 0), // rp_sealed_dark_crystal_shield
		new BuySellList(5340, 100, 0.000000, 0), // rp_sealed_legging_of_dark_crystal_i
		new BuySellList(5341, 100, 0.000000, 0), // rp_sealed_legging_of_dark_crystal
		new BuySellList(6331, 100, 0.000000, 0), // rp_sealed_phoenix's_earing_i
		new BuySellList(6332, 100, 0.000000, 0), // rp_sealed_phoenix's_earing
		new BuySellList(6329, 100, 0.000000, 0), // rp_sealed_phoenix's_necklace_i
		new BuySellList(6330, 100, 0.000000, 0), // rp_sealed_phoenix's_necklace
		new BuySellList(6333, 100, 0.000000, 0), // rp_sealed_phoenix's_ring_i
		new BuySellList(6334, 100, 0.000000, 0), // rp_sealed_phoenix's_ring
		new BuySellList(5428, 100, 0.000000, 0), // rp_sealed_tallum_bonnet_i
		new BuySellList(5429, 100, 0.000000, 0), // rp_sealed_tallum_bonnet
		new BuySellList(5370, 100, 0.000000, 0), // rp_sealed_tallum_boots_i
		new BuySellList(5371, 100, 0.000000, 0), // rp_sealed_tallum_boots
		new BuySellList(5394, 100, 0.000000, 0), // rp_sealed_tallum_gloves_i
		new BuySellList(5395, 100, 0.000000, 0), // rp_sealed_tallum_gloves
		new BuySellList(5354, 100, 0.000000, 0), // rp_sealed_tallum_hose_i
		new BuySellList(5355, 100, 0.000000, 0), // rp_sealed_tallum_hose
		new BuySellList(5334, 100, 0.000000, 0), // rp_sealed_tallum_leather_mail_i
		new BuySellList(5335, 100, 0.000000, 0), // rp_sealed_tallum_leather_mail
		new BuySellList(5418, 100, 0.000000, 0), // rp_sealed_tallum_plate_armor_i
		new BuySellList(5419, 100, 0.000000, 0), // rp_sealed_tallum_plate_armor
		new BuySellList(5346, 100, 0.000000, 0), // rp_sealed_tallum_tunic_i
		new BuySellList(5347, 100, 0.000000, 0), // rp_sealed_tallum_tunic
		new BuySellList(5470, 100, 0.000000, 0), // rp_tallum_blade_i
		new BuySellList(5471, 100, 0.000000, 0), // rp_tallum_blade
		new BuySellList(5476, 100, 0.000000, 0), // rp_arcsmith_anvil
		new BuySellList(5477, 100, 0.000000, 0), // rp_warsmith_holder
		new BuySellList(5475, 100, 0.000000, 0), // rp_warsmith_mold
		new BuySellList(5281, 100, 0.000000, 0), // rp_adv_comp_bspiritshot_a
		new BuySellList(5271, 100, 0.000000, 0), // rp_adv_comp_soulshot_a
		new BuySellList(5276, 100, 0.000000, 0), // rp_adv_comp_spiritshot_a
		new BuySellList(3956, 100, 0.000000, 0), // rp_blessed_spiritshot_a
		new BuySellList(5166, 100, 0.000000, 0), // rp_comp_bspiritshot_a
		new BuySellList(5156, 100, 0.000000, 0), // rp_comp_soulshot_a
		new BuySellList(5161, 100, 0.000000, 0), // rp_comp_spiritshot_a
		new BuySellList(1807, 100, 0.000000, 0), // rp_soulshot_a
		new BuySellList(3035, 100, 0.000000, 0), // rp_spiritshot_a
		new BuySellList(5464, 100, 0.000000, 0), // rp_dark_legion's_edge_i
		new BuySellList(5465, 100, 0.000000, 0), // rp_dark_legion's_edge
		new BuySellList(5450, 100, 0.000000, 0), // rp_dragon_grinder_i
		new BuySellList(5451, 100, 0.000000, 0), // rp_dragon_grinder
		new BuySellList(5434, 100, 0.000000, 0), // rp_dragon_slayer_i
		new BuySellList(5435, 100, 0.000000, 0), // rp_dragon_slayer
		new BuySellList(5440, 100, 0.000000, 0), // rp_elysian_i
		new BuySellList(5441, 100, 0.000000, 0), // rp_elysian
		new BuySellList(5420, 100, 0.000000, 0), // rp_sealed_armor_of_nightmare_i
		new BuySellList(5421, 100, 0.000000, 0), // rp_sealed_armor_of_nightmare
		new BuySellList(5380, 100, 0.000000, 0), // rp_sealed_boots_of_nightmare_i
		new BuySellList(5381, 100, 0.000000, 0), // rp_sealed_boots_of_nightmare
		new BuySellList(5404, 100, 0.000000, 0), // rp_sealed_gloves_of_nightmare_i
		new BuySellList(5405, 100, 0.000000, 0), // rp_sealed_gloves_of_nightmare
		new BuySellList(5430, 100, 0.000000, 0), // rp_sealed_helm_of_nightmare_i
		new BuySellList(5431, 100, 0.000000, 0), // rp_sealed_helm_of_nightmare
		new BuySellList(5336, 100, 0.000000, 0), // rp_sealed_leather_mail_of_nightmare_i
		new BuySellList(5337, 100, 0.000000, 0), // rp_sealed_leather_mail_of_nightmare
		new BuySellList(5382, 100, 0.000000, 0), // rp_sealed_magestic_boots_i
		new BuySellList(5383, 100, 0.000000, 0), // rp_sealed_magestic_boots
		new BuySellList(5432, 100, 0.000000, 0), // rp_sealed_magestic_circlet_i
		new BuySellList(5433, 100, 0.000000, 0), // rp_sealed_magestic_circlet
		new BuySellList(5406, 100, 0.000000, 0), // rp_sealed_magestic_gloves_i
		new BuySellList(5407, 100, 0.000000, 0), // rp_sealed_magestic_gloves
		new BuySellList(6337, 100, 0.000000, 0), // rp_sealed_majestic_earing_i
		new BuySellList(6338, 100, 0.000000, 0), // rp_sealed_majestic_earing
		new BuySellList(5338, 100, 0.000000, 0), // rp_sealed_majestic_leather_mail_i
		new BuySellList(5339, 100, 0.000000, 0), // rp_sealed_majestic_leather_mail
		new BuySellList(6335, 100, 0.000000, 0), // rp_sealed_majestic_necklace_i
		new BuySellList(6336, 100, 0.000000, 0), // rp_sealed_majestic_necklace
		new BuySellList(5422, 100, 0.000000, 0), // rp_sealed_majestic_platte_armor_i
		new BuySellList(5423, 100, 0.000000, 0), // rp_sealed_majestic_platte_armor
		new BuySellList(6339, 100, 0.000000, 0), // rp_sealed_majestic_ring_i
		new BuySellList(6340, 100, 0.000000, 0), // rp_sealed_majestic_ring
		new BuySellList(5352, 100, 0.000000, 0), // rp_sealed_majestic_robe_i
		new BuySellList(5353, 100, 0.000000, 0), // rp_sealed_majestic_robe
		new BuySellList(5350, 100, 0.000000, 0), // rp_sealed_robe_of_nightmare_i
		new BuySellList(5351, 100, 0.000000, 0), // rp_sealed_robe_of_nightmare
		new BuySellList(5366, 100, 0.000000, 0), // rp_sealed_shield_of_nightmare_i
		new BuySellList(5367, 100, 0.000000, 0), // rp_sealed_shield_of_nightmare
		new BuySellList(5442, 100, 0.000000, 0), // rp_soul_bow_i
		new BuySellList(5443, 100, 0.000000, 0), // rp_soul_bow
		new BuySellList(5448, 100, 0.000000, 0), // rp_soul_separator_i
		new BuySellList(5449, 100, 0.000000, 0), // rp_soul_separator
		new BuySellList(5466, 100, 0.000000, 0), // rp_sword_of_miracle_i
		new BuySellList(5467, 100, 0.000000, 0), // rp_sword_of_miracle
		new BuySellList(5456, 100, 0.000000, 0), // rp_tallum_glaive_i
		new BuySellList(5457, 100, 0.000000, 0), // rp_tallum_glaive
		new BuySellList(5462, 100, 0.000000, 0), // rp_worldtree's_branch_i
		new BuySellList(5463, 100, 0.000000, 0), // rp_worldtree's_branch
		new BuySellList(8690, 100, 0.000000, 0), // rp_sirr_blade_i
		new BuySellList(8691, 100, 0.000000, 0), // rp_sirr_blade
		new BuySellList(8692, 100, 0.000000, 0), // rp_sword_of_ipos_i
		new BuySellList(8693, 100, 0.000000, 0), // rp_sword_of_ipos
		new BuySellList(8694, 100, 0.000000, 0), // rp_barakiel_axe_i
		new BuySellList(8695, 100, 0.000000, 0), // rp_barakiel_axe
		new BuySellList(8696, 100, 0.000000, 0), // rp_tuning_fork_of_behemoth_i
		new BuySellList(8697, 100, 0.000000, 0), // rp_tuning_fork_of_behemoth
		new BuySellList(8698, 100, 0.000000, 0), // rp_naga_storm_i
		new BuySellList(8699, 100, 0.000000, 0), // rp_naga_storm
		new BuySellList(8700, 100, 0.000000, 0), // rp_tiphon_spear_i
		new BuySellList(8701, 100, 0.000000, 0), // rp_tiphon_spear
		new BuySellList(8702, 100, 0.000000, 0), // rp_shyid_bow_i
		new BuySellList(8703, 100, 0.000000, 0), // rp_shyid_bow
		new BuySellList(8704, 100, 0.000000, 0), // rp_sobekk_hurricane_i
		new BuySellList(8705, 100, 0.000000, 0), // rp_sobekk_hurricane
		new BuySellList(8706, 100, 0.000000, 0), // rp_tongue_of_themis_i
		new BuySellList(8707, 100, 0.000000, 0), // rp_tongue_of_themis
		new BuySellList(8708, 100, 0.000000, 0), // rp_hand_of_cabrio_i
		new BuySellList(8709, 100, 0.000000, 0), // rp_hand_of_cabrio
		new BuySellList(8710, 100, 0.000000, 0), // rp_crystal_of_deamon_i
		new BuySellList(8711, 100, 0.000000, 0) // rp_crystal_of_deamon
	};
	
	private static final BuySellList[] _sellList6 = new BuySellList[] {
		new BuySellList(6901, 100, 0.000000, 0), // rp_shining_arrow
		new BuySellList(5282, 100, 0.000000, 0), // rp_adv_comp_bspiritshot_s
		new BuySellList(5272, 100, 0.000000, 0), // rp_adv_comp_soulshot_s
		new BuySellList(5277, 100, 0.000000, 0), // rp_adv_comp_spiritshot_s
		new BuySellList(3957, 100, 0.000000, 0), // rp_blessed_spiritshot_s
		new BuySellList(5167, 100, 0.000000, 0), // rp_comp_bspiritshot_s
		new BuySellList(5157, 100, 0.000000, 0), // rp_comp_soulshot_s
		new BuySellList(5162, 100, 0.000000, 0), // rp_comp_spiritshot_s
		new BuySellList(1808, 100, 0.000000, 0), // rp_soulshot_s
		new BuySellList(3036, 100, 0.000000, 0), // rp_spiritshot_s
		new BuySellList(6887, 100, 0.000000, 0), // rp_angel_slayer_i
		new BuySellList(6888, 100, 0.000000, 0), // rp_angel_slayer
		new BuySellList(6899, 100, 0.000000, 0), // rp_arcana_mace_i
		new BuySellList(6900, 100, 0.000000, 0), // rp_arcana_mace
		new BuySellList(6883, 100, 0.000000, 0), // rp_basalt_battlehammer_i
		new BuySellList(6884, 100, 0.000000, 0), // rp_basalt_battlehammer
		new BuySellList(6895, 100, 0.000000, 0), // rp_demon_splinter_i
		new BuySellList(6896, 100, 0.000000, 0), // rp_demon_splinter
		new BuySellList(6891, 100, 0.000000, 0), // rp_dragon_hunter_axe_i
		new BuySellList(6892, 100, 0.000000, 0), // rp_dragon_hunter_axe
		new BuySellList(6881, 100, 0.000000, 0), // rp_forgotten_blade_i
		new BuySellList(6882, 100, 0.000000, 0), // rp_forgotten_blade
		new BuySellList(6897, 100, 0.000000, 0), // rp_heavens_divider_i
		new BuySellList(6898, 100, 0.000000, 0), // rp_heavens_divider
		new BuySellList(6885, 100, 0.000000, 0), // rp_imperial_staff_i
		new BuySellList(6886, 100, 0.000000, 0), // rp_imperial_staff
		new BuySellList(6893, 100, 0.000000, 0), // rp_saint_spear_i
		new BuySellList(6894, 100, 0.000000, 0), // rp_saint_spear
		new BuySellList(7580, 100, 0.000000, 0), // rp_draconic_bow_i
		new BuySellList(7581, 100, 0.000000, 0), // rp_draconic_bow
		new BuySellList(6865, 100, 0.000000, 0), // rp_sealed_draconic_leather_armor_i
		new BuySellList(6866, 100, 0.000000, 0), // rp_sealed_draconic_leather_armor
		new BuySellList(6869, 100, 0.000000, 0), // rp_sealed_draconic_leather_boots_i
		new BuySellList(6870, 100, 0.000000, 0), // rp_sealed_draconic_leather_boots
		new BuySellList(6867, 100, 0.000000, 0), // rp_sealed_draconic_leather_gloves_i
		new BuySellList(6868, 100, 0.000000, 0), // rp_sealed_draconic_leather_gloves
		new BuySellList(6871, 100, 0.000000, 0), // rp_sealed_draconic_leather_helmet_i
		new BuySellList(6872, 100, 0.000000, 0), // rp_sealed_draconic_leather_helmet
		new BuySellList(6851, 100, 0.000000, 0), // rp_sealed_dragon_necklace_i
		new BuySellList(6852, 100, 0.000000, 0), // rp_sealed_dragon_necklace
		new BuySellList(6853, 100, 0.000000, 0), // rp_sealed_imperial_crusader_armor_i
		new BuySellList(6854, 100, 0.000000, 0), // rp_sealed_imperial_crusader_armor
		new BuySellList(6859, 100, 0.000000, 0), // rp_sealed_imperial_crusader_boots_i
		new BuySellList(6860, 100, 0.000000, 0), // rp_sealed_imperial_crusader_boots
		new BuySellList(6855, 100, 0.000000, 0), // rp_sealed_imperial_crusader_gaiters_i
		new BuySellList(6856, 100, 0.000000, 0), // rp_sealed_imperial_crusader_gaiters
		new BuySellList(6857, 100, 0.000000, 0), // rp_sealed_imperial_crusader_gauntlet_i
		new BuySellList(6858, 100, 0.000000, 0), // rp_sealed_imperial_crusader_gauntlet
		new BuySellList(6863, 100, 0.000000, 0), // rp_sealed_imperial_crusader_helmet_i
		new BuySellList(6864, 100, 0.000000, 0), // rp_sealed_imperial_crusader_helmet
		new BuySellList(6861, 100, 0.000000, 0), // rp_sealed_imperial_crusader_shield_i
		new BuySellList(6862, 100, 0.000000, 0), // rp_sealed_imperial_crusader_shield
		new BuySellList(13100, 100, 0.000000, 0), // rp_sealed_arcana_sigil_i
		new BuySellList(6877, 100, 0.000000, 0), // rp_sealed_major_arcana_boots_i
		new BuySellList(6878, 100, 0.000000, 0), // rp_sealed_major_arcana_boots
		new BuySellList(6875, 100, 0.000000, 0), // rp_sealed_major_arcana_gloves_i
		new BuySellList(6876, 100, 0.000000, 0), // rp_sealed_major_arcana_gloves
		new BuySellList(6879, 100, 0.000000, 0), // rp_sealed_major_arcana_hood_i
		new BuySellList(6880, 100, 0.000000, 0), // rp_sealed_major_arcana_hood
		new BuySellList(6873, 100, 0.000000, 0), // rp_sealed_major_arcana_robe_i
		new BuySellList(6874, 100, 0.000000, 0), // rp_sealed_major_arcana_robe
		new BuySellList(6849, 100, 0.000000, 0), // rp_sealed_ring_of_aurakyria_i
		new BuySellList(6850, 100, 0.000000, 0), // rp_sealed_ring_of_aurakyria
		new BuySellList(6847, 100, 0.000000, 0), // rp_sealed_sanddragon's_earing_i
		new BuySellList(6848, 100, 0.000000, 0), // rp_sealed_sanddragon's_earing
		new BuySellList(9967, 100, 0.000000, 0), // rp_dynasty_blade_i
		new BuySellList(9968, 100, 0.000000, 0), // rp_dynasty_two_hand_sword_i
		new BuySellList(9969, 100, 0.000000, 0), // rp_dynasty_magic_sword_i
		new BuySellList(9970, 100, 0.000000, 0), // rp_dynasty_bow_i
		new BuySellList(9971, 100, 0.000000, 0), // rp_dynasty_dagger_i
		new BuySellList(9972, 100, 0.000000, 0), // rp_dynasty_spear_i
		new BuySellList(9973, 100, 0.000000, 0), // rp_dynasty_hammer_i
		new BuySellList(9974, 100, 0.000000, 0), // rp_dynasty_staff_i
		new BuySellList(9975, 100, 0.000000, 0), // rp_dynasty_jamadhr_i
		new BuySellList(10544, 100, 0.000000, 0), // rp_dynasty_twohand_staff_i
		new BuySellList(10545, 100, 0.000000, 0), // rp_dynasty_crusher_i
		new BuySellList(9482, 100, 0.000000, 0), // rp_sealed_dynasty_blast_plate_i
		new BuySellList(9483, 100, 0.000000, 0), // rp_sealed_dynasty_gaiter_i
		new BuySellList(9484, 100, 0.000000, 0), // rp_sealed_dynasty_helmet_i
		new BuySellList(9485, 100, 0.000000, 0), // rp_sealed_dynasty_gauntlet_i
		new BuySellList(9486, 100, 0.000000, 0), // rp_sealed_dynasty_boots_i
		new BuySellList(9487, 100, 0.000000, 0), // rp_sealed_dynasty_leather_mail_i
		new BuySellList(9488, 100, 0.000000, 0), // rp_sealed_dynasty_leather_legging_i
		new BuySellList(9489, 100, 0.000000, 0), // rp_sealed_dynasty_leather_helmet_i
		new BuySellList(9490, 100, 0.000000, 0), // rp_sealed_dynasty_leather_gloves_i
		new BuySellList(9491, 100, 0.000000, 0), // rp_sealed_dynasty_leather_boots_i
		new BuySellList(9492, 100, 0.000000, 0), // rp_sealed_dynasty_tunic_i
		new BuySellList(9493, 100, 0.000000, 0), // rp_sealed_dynasty_hose_i
		new BuySellList(9494, 100, 0.000000, 0), // rp_sealed_dynasty_circlet_i
		new BuySellList(9495, 100, 0.000000, 0), // rp_sealed_dynasty_gloves_i
		new BuySellList(9496, 100, 0.000000, 0), // rp_sealed_dynasty_shoes_i
		new BuySellList(9497, 100, 0.000000, 0), // rp_sealed_dynasty_shield_i
		new BuySellList(10115, 100, 0.000000, 0), // rp_sealed_dynasty_sigil_i
		new BuySellList(9985, 100, 0.000000, 0), // rp_sealed_dynasty_earring_i
		new BuySellList(9986, 100, 0.000000, 0), // rp_sealed_dynasty_neckalce_i
		new BuySellList(9987, 100, 0.000000, 0), // rp_sealed_dynasty_ring_i
		new BuySellList(10373, 100, 0.000000, 0), // rp_icarus_sowsword_i
		new BuySellList(10374, 100, 0.000000, 0), // rp_icarus_disperser_i
		new BuySellList(10375, 100, 0.000000, 0), // rp_icarus_spirits_i
		new BuySellList(10376, 100, 0.000000, 0), // rp_icarus_heavy_arms_i
		new BuySellList(10377, 100, 0.000000, 0), // rp_icarus_trident_i
		new BuySellList(10378, 100, 0.000000, 0), // rp_icarus_chopper_i
		new BuySellList(10379, 100, 0.000000, 0), // rp_icarus_knuckle_i
		new BuySellList(10380, 100, 0.000000, 0), // rp_icarus_wand_i
		new BuySellList(10381, 100, 0.000000, 0), // rp_icarus_accipiter_i
		new BuySellList(15775, 100, 0.000000, 0), // rp_sealed_destino_helmet_i
		new BuySellList(15776, 100, 0.000000, 0), // rp_sealed_destino_leather_helmet_i
		new BuySellList(15777, 100, 0.000000, 0), // rp_sealed_destino_circlet_i
		new BuySellList(15778, 100, 0.000000, 0), // rp_sealed_destino_cuirass_i
		new BuySellList(15779, 100, 0.000000, 0), // rp_sealed_destino_houberk_i
		new BuySellList(15780, 100, 0.000000, 0), // rp_sealed_destino_jaket_i
		new BuySellList(15781, 100, 0.000000, 0), // rp_sealed_destino_gaiter_i
		new BuySellList(15782, 100, 0.000000, 0), // rp_sealed_destino_leather_legging_i
		new BuySellList(15783, 100, 0.000000, 0), // rp_sealed_destino_hose_i
		new BuySellList(15784, 100, 0.000000, 0), // rp_sealed_destino_gauntlet_i
		new BuySellList(15785, 100, 0.000000, 0), // rp_sealed_destino_leather_gloves_i
		new BuySellList(15786, 100, 0.000000, 0), // rp_sealed_destino_gloves_i
		new BuySellList(15787, 100, 0.000000, 0), // rp_sealed_destino_boots_i
		new BuySellList(15788, 100, 0.000000, 0), // rp_sealed_destino_leather_boots_i
		new BuySellList(15789, 100, 0.000000, 0), // rp_sealed_destino_shoes_i
		new BuySellList(15790, 100, 0.000000, 0), // rp_sealed_destino_sigil_i
		new BuySellList(15791, 100, 0.000000, 0), // rp_sealed_destino_shield_i
		new BuySellList(15792, 100, 0.000000, 0), // rp_sealed_vesper_helmet_i
		new BuySellList(15793, 100, 0.000000, 0), // rp_sealed_vesper_leather_helmet_i
		new BuySellList(15794, 100, 0.000000, 0), // rp_sealed_vesper_circlet_i
		new BuySellList(15795, 100, 0.000000, 0), // rp_sealed_vesper_cuirass_i
		new BuySellList(15796, 100, 0.000000, 0), // rp_sealed_vesper_houberk_i
		new BuySellList(15797, 100, 0.000000, 0), // rp_sealed_vesper_jaket_i
		new BuySellList(15798, 100, 0.000000, 0), // rp_sealed_vesper_gaiter_i
		new BuySellList(15799, 100, 0.000000, 0), // rp_sealed_vesper_leather_legging_i
		new BuySellList(15800, 100, 0.000000, 0), // rp_sealed_vesper_hose_i
		new BuySellList(15801, 100, 0.000000, 0), // rp_sealed_vesper_gauntlet_i
		new BuySellList(15802, 100, 0.000000, 0), // rp_sealed_vesper_leather_gloves_i
		new BuySellList(15803, 100, 0.000000, 0), // rp_sealed_vesper_gloves_i
		new BuySellList(15804, 100, 0.000000, 0), // rp_sealed_vesper_boots_i
		new BuySellList(15805, 100, 0.000000, 0), // rp_sealed_vesper_leather_boots_i
		new BuySellList(15806, 100, 0.000000, 0), // rp_sealed_vesper_shoes_i
		new BuySellList(15807, 100, 0.000000, 0), // rp_sealed_vesper_sigil_i
		new BuySellList(15808, 100, 0.000000, 0), // rp_sealed_vesper_verteidiger_i
		new BuySellList(15809, 100, 0.000000, 0), // rp_sealed_vesper_ring_i
		new BuySellList(15810, 100, 0.000000, 0), // rp_sealed_vesper_earring_i
		new BuySellList(15811, 100, 0.000000, 0), // rp_sealed_vesper_necklace_i
		new BuySellList(15812, 100, 0.000000, 0), // rp_sealed_destino_ring_i
		new BuySellList(15813, 100, 0.000000, 0), // rp_sealed_destino_earring_i
		new BuySellList(15814, 100, 0.000000, 0), // rp_sealed_destino_necklace_i
		new BuySellList(15815, 100, 0.000000, 0), // rp_vesper_cutter_i
		new BuySellList(15816, 100, 0.000000, 0), // rp_vesper_slasher_i
		new BuySellList(15817, 100, 0.000000, 0), // rp_vesper_burster_i
		new BuySellList(15818, 100, 0.000000, 0), // rp_vesper_shaper_i
		new BuySellList(15819, 100, 0.000000, 0), // rp_vesper_fighter_i
		new BuySellList(15820, 100, 0.000000, 0), // rp_vesper_stormer_i
		new BuySellList(15821, 100, 0.000000, 0), // rp_vesper_avenger_i
		new BuySellList(15822, 100, 0.000000, 0), // rp_vesper_retributer_i
		new BuySellList(15823, 100, 0.000000, 0), // rp_vesper_caster_i
		new BuySellList(15824, 100, 0.000000, 0), // rp_vesper_singer_i
		new BuySellList(15825, 100, 0.000000, 0) // rp_vesper_thrower_i
	};
	
	private static final BuySellList[] _sellList7 = new BuySellList[] {
		new BuySellList(6926, 100, 0.000000, 0), // rp_lesser_healing_potion
		new BuySellList(6929, 100, 0.000000, 0), // rp_antidote
		new BuySellList(6931, 100, 0.000000, 0), // rp_bandage
		new BuySellList(6937, 100, 0.000000, 0), // rp_potion_of_acumen2
		new BuySellList(6933, 100, 0.000000, 0), // rp_quick_step_potion
		new BuySellList(6934, 100, 0.000000, 0), // rp_swift_attack_potion
		new BuySellList(6920, 100, 0.000000, 0), // rp_fish_oil_average
		new BuySellList(6930, 100, 0.000000, 0), // rp_advanced_antidote
		new BuySellList(6932, 100, 0.000000, 0), // rp_emergency_dressing
		new BuySellList(6927, 100, 0.000000, 0), // rp_healing_potion
		new BuySellList(6939, 100, 0.000000, 0), // rp_bighead_potion
		new BuySellList(6921, 100, 0.000000, 0), // rp_fish_oil_high
		new BuySellList(6943, 100, 0.000000, 0), // rp_haircolor_a_potion
		new BuySellList(6944, 100, 0.000000, 0), // rp_haircolor_b_potion
		new BuySellList(6945, 100, 0.000000, 0), // rp_haircolor_c_potion
		new BuySellList(6946, 100, 0.000000, 0), // rp_haircolor_d_potion
		new BuySellList(6947, 100, 0.000000, 0), // rp_hairstyle_a_potion
		new BuySellList(6948, 100, 0.000000, 0), // rp_hairstyle_b_potion
		new BuySellList(6949, 100, 0.000000, 0), // rp_hairstyle_c_potion
		new BuySellList(6950, 100, 0.000000, 0), // rp_hairstyle_d_potion
		new BuySellList(6951, 100, 0.000000, 0), // rp_hairstyle_e_potion
		new BuySellList(6952, 100, 0.000000, 0), // rp_hairstyle_f_potion
		new BuySellList(6953, 100, 0.000000, 0), // rp_hairstyle_g_potion
		new BuySellList(6940, 100, 0.000000, 0), // rp_masktype_a_potion
		new BuySellList(6941, 100, 0.000000, 0), // rp_masktype_b_potion
		new BuySellList(6942, 100, 0.000000, 0), // rp_masktype_c_potion
		new BuySellList(6935, 100, 0.000000, 0), // rp_adv_quick_step_potion
		new BuySellList(6936, 100, 0.000000, 0), // rp_adv_swift_attack_potion
		new BuySellList(6938, 100, 0.000000, 0), // rp_potion_of_acumen3
		new BuySellList(6957, 100, 0.000000, 0), // rp_dye_c1d1_d
		new BuySellList(6956, 100, 0.000000, 0), // rp_dye_c1s1_d
		new BuySellList(6959, 100, 0.000000, 0), // rp_dye_d1c1_d
		new BuySellList(6958, 100, 0.000000, 0), // rp_dye_d1s1_d
		new BuySellList(6960, 100, 0.000000, 0), // rp_dye_i1m1_d
		new BuySellList(6961, 100, 0.000000, 0), // rp_dye_i1w1_d
		new BuySellList(6962, 100, 0.000000, 0), // rp_dye_m1i1_d
		new BuySellList(6963, 100, 0.000000, 0), // rp_dye_m1w1_d
		new BuySellList(6954, 100, 0.000000, 0), // rp_dye_s1c1_d
		new BuySellList(6955, 100, 0.000000, 0), // rp_dye_s1d1_d
		new BuySellList(6964, 100, 0.000000, 0), // rp_dye_w1i1_d
		new BuySellList(6965, 100, 0.000000, 0), // rp_dye_w1m1_d
		new BuySellList(6969, 100, 0.000000, 0), // rp_dye_c1c1_c
		new BuySellList(6968, 100, 0.000000, 0), // rp_dye_c1s1_c
		new BuySellList(6971, 100, 0.000000, 0), // rp_dye_d1c1_c
		new BuySellList(6970, 100, 0.000000, 0), // rp_dye_d1s1_c
		new BuySellList(6972, 100, 0.000000, 0), // rp_dye_i1m1_c
		new BuySellList(6973, 100, 0.000000, 0), // rp_dye_i1w1_c
		new BuySellList(6974, 100, 0.000000, 0), // rp_dye_m1i1_c
		new BuySellList(6975, 100, 0.000000, 0), // rp_dye_m1w1_c
		new BuySellList(6966, 100, 0.000000, 0), // rp_dye_s1c1_c
		new BuySellList(6967, 100, 0.000000, 0), // rp_dye_s1d1_c
		new BuySellList(6976, 100, 0.000000, 0), // rp_dye_w1i1_c
		new BuySellList(6977, 100, 0.000000, 0), // rp_dye_w1m1_c
		new BuySellList(6924, 100, 0.000000, 0), // rp_eye_bandage_of_pirate
		new BuySellList(6923, 100, 0.000000, 0), // rp_hair_pin_of_lady
		new BuySellList(6925, 100, 0.000000, 0), // rp_monocle
		new BuySellList(6922, 100, 0.000000, 0), // rp_party_mask
		new BuySellList(6981, 100, 0.000000, 0), // rp_dye_c2c2_c
		new BuySellList(6980, 100, 0.000000, 0), // rp_dye_c2s2_c
		new BuySellList(6983, 100, 0.000000, 0), // rp_dye_d2c2_c
		new BuySellList(6982, 100, 0.000000, 0), // rp_dye_d2s2_c
		new BuySellList(6984, 100, 0.000000, 0), // rp_dye_i2m2_c
		new BuySellList(6985, 100, 0.000000, 0), // rp_dye_i2w2_c
		new BuySellList(6986, 100, 0.000000, 0), // rp_dye_m2i2_c
		new BuySellList(6987, 100, 0.000000, 0), // rp_dye_m2w2_c
		new BuySellList(6978, 100, 0.000000, 0), // rp_dye_s2c2_c
		new BuySellList(6979, 100, 0.000000, 0), // rp_dye_s2d2_c
		new BuySellList(6988, 100, 0.000000, 0), // rp_dye_w2i2_c
		new BuySellList(6989, 100, 0.000000, 0), // rp_dye_w2m2_c
		new BuySellList(6928, 100, 0.000000, 0), // rp_greater_healing_potion
		new BuySellList(6993, 100, 0.000000, 0), // rp_dye_c3c3_c
		new BuySellList(6992, 100, 0.000000, 0), // rp_dye_c3s3_c
		new BuySellList(6995, 100, 0.000000, 0), // rp_dye_d3c3_c
		new BuySellList(6994, 100, 0.000000, 0), // rp_dye_d3s3_c
		new BuySellList(6996, 100, 0.000000, 0), // rp_dye_i3m3_c
		new BuySellList(6997, 100, 0.000000, 0), // rp_dye_i3w3_c
		new BuySellList(6998, 100, 0.000000, 0), // rp_dye_m3i3_c
		new BuySellList(6999, 100, 0.000000, 0), // rp_dye_m3w3_c
		new BuySellList(6990, 100, 0.000000, 0), // rp_dye_s3c3_c
		new BuySellList(6991, 100, 0.000000, 0), // rp_dye_s3d3_c
		new BuySellList(7000, 100, 0.000000, 0), // rp_dye_w3i3_c
		new BuySellList(7001, 100, 0.000000, 0), // rp_dye_w3m3_c
		new BuySellList(7005, 100, 0.000000, 0), // rp_dye_c4c4_c
		new BuySellList(7004, 100, 0.000000, 0), // rp_dye_c4s4_c
		new BuySellList(7007, 100, 0.000000, 0), // rp_dye_d4c4_c
		new BuySellList(7006, 100, 0.000000, 0), // rp_dye_d4s4_c
		new BuySellList(7008, 100, 0.000000, 0), // rp_dye_i4m4_c
		new BuySellList(7009, 100, 0.000000, 0), // rp_dye_i4w4_c
		new BuySellList(7010, 100, 0.000000, 0), // rp_dye_m4i4_c
		new BuySellList(7011, 100, 0.000000, 0), // rp_dye_m4w4_c
		new BuySellList(7002, 100, 0.000000, 0), // rp_dye_s4c4_c
		new BuySellList(7003, 100, 0.000000, 0), // rp_dye_s4d4_c
		new BuySellList(7012, 100, 0.000000, 0), // rp_dye_w4i4_c
		new BuySellList(7013, 100, 0.000000, 0), // rp_dye_w4m4_c
		new BuySellList(7693, 100, 0.000000, 0), // rp_cat_ear
		new BuySellList(7700, 100, 0.000000, 0), // rp_daisy_hairpin
		new BuySellList(7699, 100, 0.000000, 0), // rp_forget_me_not_hairpin
		new BuySellList(7691, 100, 0.000000, 0), // rp_maidens_hairpin
		new BuySellList(7690, 100, 0.000000, 0), // rp_outlaw_eyepatch
		new BuySellList(7692, 100, 0.000000, 0), // rp_rabbit_ear
		new BuySellList(7689, 100, 0.000000, 0), // rp_racoon_ear
		new BuySellList(16110, 100, 0.000000, 0), // rp_black_skeleton_circlet_i
		new BuySellList(16111, 100, 0.000000, 0), // rp_oldgold_skeleton_circlet_i
		new BuySellList(16112, 100, 0.000000, 0), // rp_green_skeleton_circlet_i
		new BuySellList(16113, 100, 0.000000, 0), // rp_brown_skeleton_circlet_i
		new BuySellList(16114, 100, 0.000000, 0), // rp_shark_of_cap_i
		new BuySellList(16115, 100, 0.000000, 0), // rp_gold_horn_cap_i
		new BuySellList(16116, 100, 0.000000, 0), // rp_silver_horn_cap_i
		new BuySellList(16117, 100, 0.000000, 0), // rp_penguin_of_cap_i
		new BuySellList(16118, 100, 0.000000, 0), // rp_turban_brown_hat_i
		new BuySellList(16119, 100, 0.000000, 0), // rp_turban_yellow_hat_i
		new BuySellList(16120, 100, 0.000000, 0), // rp_turtle_of_cap_i
		new BuySellList(16121, 100, 0.000000, 0) // rp_cow_of_cap_i
	};
	
	// NPCs
	private static final int MR_CAT = 31756;
	private static final int MISS_QUEEN = 31757;
	// Vars
	private static final int CUSTOM_EVENT_ID = 1001;
	
	private static final int MS_ASK_BUYSELL = -1;
	
	private static final int MS_REPLY_BUY_SELLLIST0 = 0;
	private static final int MS_REPLY_BUY_SELLLIST1 = 1;
	private static final int MS_REPLY_BUY_SELLLIST2 = 2;
	private static final int MS_REPLY_BUY_SELLLIST3 = 3;
	private static final int MS_REPLY_BUY_SELLLIST4 = 4;
	private static final int MS_REPLY_BUY_SELLLIST5 = 5;
	private static final int MS_REPLY_BUY_SELLLIST6 = 6;
	private static final int MS_REPLY_BUY_SELLLIST7 = 7;
	
	private L2BuyList sellList0;
	private L2BuyList sellList1;
	private L2BuyList sellList2;
	private L2BuyList sellList3;
	private L2BuyList sellList4;
	private L2BuyList sellList5;
	private L2BuyList sellList6;
	private L2BuyList sellList7;
	
	public ClassMaster() {
		bindStartNpc(MR_CAT, MISS_QUEEN);
		bindFirstTalk(MR_CAT, MISS_QUEEN);
		bindTalk(MR_CAT, MISS_QUEEN);
		bindMenuSelected(MR_CAT, MISS_QUEEN);
		if (character().alternateClassMaster()) {
			setOnEnterWorld(true);
			bindTutorial();
			bindTutorialQuestionMark();
		}
		
		if (character().allowClassMasters()) {
			addSpawn(MR_CAT, new Location(147728, 27408, -2198, 16500));
			addSpawn(MISS_QUEEN, new Location(147761, 27408, -2198, 16500));
			addSpawn(MR_CAT, new Location(148560, -57952, -2974, 53000));
			addSpawn(MISS_QUEEN, new Location(148514, -57972, -2974, 53000));
			addSpawn(MR_CAT, new Location(110592, 220400, -3667, 0));
			addSpawn(MISS_QUEEN, new Location(110592, 220443, -3667, 0));
			addSpawn(MR_CAT, new Location(117200, 75824, -2725, 25000));
			addSpawn(MISS_QUEEN, new Location(117160, 75784, -2725, 25000));
			addSpawn(MR_CAT, new Location(116224, -181728, -1373, 0));
			addSpawn(MISS_QUEEN, new Location(116218, -181793, -1379, 0));
			addSpawn(MR_CAT, new Location(114880, -178144, -827, 0));
			addSpawn(MISS_QUEEN, new Location(114880, -178196, -827, 0));
			addSpawn(MR_CAT, new Location(83076, 147912, -3467, 32000));
			addSpawn(MISS_QUEEN, new Location(83082, 147845, -3467, 32000));
			addSpawn(MR_CAT, new Location(81136, 54576, -1517, 32000));
			addSpawn(MISS_QUEEN, new Location(81126, 54519, -1517, 32000));
			addSpawn(MR_CAT, new Location(45472, 49312, -3067, 53000));
			addSpawn(MISS_QUEEN, new Location(45414, 49296, -3067, 53000));
			addSpawn(MR_CAT, new Location(47648, 51296, -2989, 38500));
			addSpawn(MISS_QUEEN, new Location(47680, 51255, -2989, 38500));
			addSpawn(MR_CAT, new Location(17956, 170536, -3499, 48000));
			addSpawn(MISS_QUEEN, new Location(17913, 170536, -3499, 48000));
			addSpawn(MR_CAT, new Location(15584, 142784, -2699, 16500));
			addSpawn(MISS_QUEEN, new Location(15631, 142778, -2699, 16500));
			addSpawn(MR_CAT, new Location(11340, 15972, -4577, 14000));
			addSpawn(MISS_QUEEN, new Location(11353, 16022, -4577, 14000));
			addSpawn(MR_CAT, new Location(10968, 17540, -4567, 55000));
			addSpawn(MISS_QUEEN, new Location(10918, 17511, -4567, 55000));
			addSpawn(MR_CAT, new Location(-14048, 123184, -3115, 32000));
			addSpawn(MISS_QUEEN, new Location(-14050, 123229, -3115, 32000));
			addSpawn(MR_CAT, new Location(-44979, -113508, -194, 32000));
			addSpawn(MISS_QUEEN, new Location(-44983, -113554, -194, 32000));
			addSpawn(MR_CAT, new Location(-84119, 243254, -3725, 8000));
			addSpawn(MISS_QUEEN, new Location(-84047, 243193, -3725, 8000));
			addSpawn(MR_CAT, new Location(-84336, 242156, -3725, 24500));
			addSpawn(MISS_QUEEN, new Location(-84294, 242204, -3725, 24500));
			addSpawn(MR_CAT, new Location(-82032, 150160, -3122, 16500));
			addSpawn(MISS_QUEEN, new Location(-81967, 150160, -3122, 16500));
			addSpawn(MR_CAT, new Location(147865, -58047, -2979, 48999));
			addSpawn(MISS_QUEEN, new Location(147906, -58047, -2979, 48999));
			addSpawn(MR_CAT, new Location(147300, -56466, -2779, 11500));
			addSpawn(MISS_QUEEN, new Location(147333, -56483, -2784, 11500));
			addSpawn(MR_CAT, new Location(44176, -48732, -800, 33000));
			addSpawn(MISS_QUEEN, new Location(44176, -48688, -800, 33000));
			addSpawn(MR_CAT, new Location(44333, -47639, -800, 49999));
			addSpawn(MISS_QUEEN, new Location(44371, -47638, -800, 49999));
			addSpawn(MR_CAT, new Location(87596, -140674, -1542, 16500));
			addSpawn(MISS_QUEEN, new Location(87644, -140674, -1542, 16500));
			addSpawn(MR_CAT, new Location(87824, -142256, -1343, 44000));
			addSpawn(MISS_QUEEN, new Location(87856, -142272, -1344, 44000));
			addSpawn(MR_CAT, new Location(-116948, 46841, 367, 49151));
			addSpawn(MISS_QUEEN, new Location(-116902, 46841, 367, 49151));
		}
		
		sellList0 = buildBuySellList(_sellList0, MR_CAT, 0);
		sellList0 = buildBuySellList(_sellList0, MISS_QUEEN, 0);
		sellList1 = buildBuySellList(_sellList1, MR_CAT, 1);
		sellList1 = buildBuySellList(_sellList1, MISS_QUEEN, 1);
		sellList2 = buildBuySellList(_sellList2, MR_CAT, 2);
		sellList2 = buildBuySellList(_sellList2, MISS_QUEEN, 2);
		sellList3 = buildBuySellList(_sellList3, MR_CAT, 3);
		sellList3 = buildBuySellList(_sellList3, MISS_QUEEN, 3);
		sellList4 = buildBuySellList(_sellList4, MR_CAT, 4);
		sellList4 = buildBuySellList(_sellList4, MISS_QUEEN, 4);
		sellList5 = buildBuySellList(_sellList5, MR_CAT, 5);
		sellList5 = buildBuySellList(_sellList5, MISS_QUEEN, 5);
		sellList6 = buildBuySellList(_sellList6, MR_CAT, 6);
		sellList6 = buildBuySellList(_sellList6, MISS_QUEEN, 6);
		sellList7 = buildBuySellList(_sellList7, MR_CAT, 7);
		sellList7 = buildBuySellList(_sellList7, MISS_QUEEN, 7);
	}
	
	@Override
	public void onTutorialEvent(L2PcInstance player, String command) {
		if (command.startsWith("CO")) {
			onTutorialLink(player, command);
		}
	}
	
	@Override
	public void onEnterWorld(L2PcInstance player) {
		showQuestionMark(player);
		Containers.Players().addListener(new ConsumerEventListener(Containers.Players(), PLAYER_LEVEL_CHANGED, (PlayerLevelChanged event) -> {
			showQuestionMark(event.player());
		}, this));
	}
	
	@Override
	public void onMenuSelected(PlayerMenuSelected event) {
		final var talker = event.player();
		final var npc = (L2Npc) event.npc();
		
		final var ask = event.ask();
		final var reply = event.reply();
		
		switch (ask) {
			case MS_ASK_BUYSELL -> {
				switch (reply) {
					case MS_REPLY_BUY_SELLLIST0 -> {
						showBuySell(talker, npc, sellList0);
					}
					case MS_REPLY_BUY_SELLLIST1 -> {
						showBuySell(talker, npc, sellList1);
					}
					case MS_REPLY_BUY_SELLLIST2 -> {
						showBuySell(talker, npc, sellList2);
					}
					case MS_REPLY_BUY_SELLLIST3 -> {
						showBuySell(talker, npc, sellList3);
					}
					case MS_REPLY_BUY_SELLLIST4 -> {
						showBuySell(talker, npc, sellList4);
					}
					case MS_REPLY_BUY_SELLLIST5 -> {
						showBuySell(talker, npc, sellList5);
					}
					case MS_REPLY_BUY_SELLLIST6 -> {
						showBuySell(talker, npc, sellList6);
					}
					case MS_REPLY_BUY_SELLLIST7 -> {
						showBuySell(talker, npc, sellList7);
					}
				}
			}
		}
	}
	
	@Override
	public String onEvent(String event, L2Npc npc, L2PcInstance player) {
		if (event.endsWith(".htm")) {
			return event;
		}
		if (event.startsWith("1stClass")) {
			showHtmlMenu(player, npc.getObjectId(), 1);
		} else if (event.startsWith("2ndClass")) {
			showHtmlMenu(player, npc.getObjectId(), 2);
		} else if (event.startsWith("3rdClass")) {
			showHtmlMenu(player, npc.getObjectId(), 3);
		} else if (event.startsWith("change_class")) {
			int val = Integer.parseInt(event.substring(13));
			if (checkAndChangeClass(player, val)) {
				String msg = getHtm(player.getHtmlPrefix(), "ok.htm").replace("%name%", ClassListData.getInstance().getClass(val).getClientCode());
				showResult(player, msg, null);
				return "";
			}
		} else if (event.startsWith("become_noble")) {
			if (!player.isNoble()) {
				player.setNoble(true);
				player.sendPacket(new UserInfo(player));
				player.sendPacket(new ExBrExtraUserInfo(player));
				return "nobleok.htm";
			}
		} else if (event.startsWith("learn_skills")) {
			player.giveAvailableSkills(character().autoLearnForgottenScrollSkills(), true);
		} else if (event.startsWith("increase_clan_level")) {
			if (!player.isClanLeader()) {
				return "noclanleader.htm";
			}
			if (player.getClan().getLevel() >= 5) {
				return "noclanlevel.htm";
			}
			player.getClan().changeLevel(5);
		} else {
			LOG.warn("Player {} send invalid request [{}]!", player, event);
		}
		return "";
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player) {
		return npc.getId() + ".htm";
	}
	
	private void onTutorialLink(L2PcInstance player, String request) {
		if (!character().alternateClassMaster() || (request == null) || !request.startsWith("CO")) {
			return;
		}
		
		if (!player.getFloodProtectors().getServerBypass().tryPerformAction("changeclass")) {
			return;
		}
		
		try {
			int val = Integer.parseInt(request.substring(2));
			checkAndChangeClass(player, val);
		} catch (NumberFormatException e) {
			LOG.warn("Player {} send invalid class change request [{}]!", player, request);
		}
		player.sendPacket(STATIC_PACKET);
	}
	
	@Override
	public void onTutorialQuestionMark(L2PcInstance player, int number) {
		if (!character().alternateClassMaster() || (number != CUSTOM_EVENT_ID)) {
			return;
		}
		showTutorialHtml(player);
	}
	
	private void showQuestionMark(L2PcInstance player) {
		if (!character().alternateClassMaster()) {
			return;
		}
		
		final ClassId classId = player.getClassId();
		if (getMinLevel(classId.level()) > player.getLevel()) {
			return;
		}
		
		if (!character().getClassMaster().isAllowed(classId.level() + 1)) {
			return;
		}
		
		player.sendPacket(new TutorialShowQuestionMark(CUSTOM_EVENT_ID));
	}
	
	private void showHtmlMenu(L2PcInstance player, int objectId, int level) {
		if (!character().allowClassMasters()) {
			String msg = getHtm(player.getHtmlPrefix(), "disabled.htm");
			showResult(player, msg, null);
			return;
		}
		if (!character().getClassMaster().isAllowed(level)) {
			final NpcHtmlMessage html = new NpcHtmlMessage(objectId);
			final int jobLevel = player.getClassId().level();
			final StringBuilder sb = new StringBuilder(100);
			sb.append("<html><body>");
			switch (jobLevel) {
				case 0:
					if (character().getClassMaster().isAllowed(1)) {
						sb.append("Come back here when you reached level 20 to change your class.<br>");
					} else if (character().getClassMaster().isAllowed(2)) {
						sb.append("Come back after your first occupation change.<br>");
					} else if (character().getClassMaster().isAllowed(3)) {
						sb.append("Come back after your second occupation change.<br>");
					} else {
						sb.append("I can't change your occupation.<br>");
					}
					break;
				case 1:
					if (character().getClassMaster().isAllowed(2)) {
						sb.append("Come back here when you reached level 40 to change your class.<br>");
					} else if (character().getClassMaster().isAllowed(3)) {
						sb.append("Come back after your second occupation change.<br>");
					} else {
						sb.append("I can't change your occupation.<br>");
					}
					break;
				case 2:
					if (character().getClassMaster().isAllowed(3)) {
						sb.append("Come back here when you reached level 76 to change your class.<br>");
					} else {
						sb.append("I can't change your occupation.<br>");
					}
					break;
				case 3:
					sb.append("There is no class change available for you anymore.<br>");
					break;
			}
			sb.append("</body></html>");
			html.setHtml(sb.toString());
			html.replace("%req_items%", getRequiredItems(level));
			player.sendPacket(html);
			return;
		}
		
		final ClassId currentClassId = player.getClassId();
		if (currentClassId.level() >= level) {
			String msg = getHtm(player.getHtmlPrefix(), "nomore.htm");
			showResult(player, msg, null);
			return;
		}
		
		final int minLevel = getMinLevel(currentClassId.level());
		if ((player.getLevel() >= minLevel) || character().allowEntireTree()) {
			final StringBuilder menu = new StringBuilder(100);
			for (ClassId cid : ClassId.values()) {
				if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2)) {
					continue;
				}
				if (validateClassId(currentClassId, cid) && (cid.level() == level)) {
					StringUtil.append(menu, "<a action=\"bypass -h Quest ClassMaster change_class ", String.valueOf(cid.getId()), "\">", ClassListData.getInstance().getClass(cid).getClientCode(), "</a><br>");
				}
			}
			
			if (menu.length() > 0) {
				String msg = getHtm(player.getHtmlPrefix(), "template.htm").replace("%name%", ClassListData.getInstance().getClass(currentClassId).getClientCode()).replace("%menu%", menu.toString());
				showResult(player, msg, null);
				return;
				
			}
			String msg = getHtm(player.getHtmlPrefix(), "comebacklater.htm").replace("%level%", String.valueOf(getMinLevel(level - 1)));
			showResult(player, msg, null);
			return;
		}
		
		if (minLevel < Integer.MAX_VALUE) {
			String msg = getHtm(player.getHtmlPrefix(), "comebacklater.htm").replace("%level%", String.valueOf(minLevel));
			showResult(player, msg, null);
			return;
		}
		
		showResult(player, getHtm(player.getHtmlPrefix(), "nomore.htm"), null);
	}
	
	private void showTutorialHtml(L2PcInstance player) {
		final ClassId currentClassId = player.getClassId();
		if ((getMinLevel(currentClassId.level()) > player.getLevel()) && !character().allowEntireTree()) {
			return;
		}
		
		String msg = getHtm(player.getHtmlPrefix(), "tutorialtemplate.htm");
		msg = msg.replaceAll("%name%", ClassListData.getInstance().getClass(currentClassId).getEscapedClientCode());
		
		final StringBuilder menu = new StringBuilder(100);
		for (ClassId cid : ClassId.values()) {
			if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2)) {
				continue;
			}
			if (validateClassId(currentClassId, cid)) {
				StringUtil.append(menu, "<a action=\"link CO", String.valueOf(cid.getId()), "\">", ClassListData.getInstance().getClass(cid).getEscapedClientCode(), "</a><br>");
			}
		}
		
		msg = msg.replaceAll("%menu%", menu.toString());
		msg = msg.replace("%req_items%", getRequiredItems(currentClassId.level() + 1));
		player.sendPacket(new TutorialShowHtml(msg));
	}
	
	private boolean checkAndChangeClass(L2PcInstance player, int val) {
		final ClassId currentClassId = player.getClassId();
		if ((getMinLevel(currentClassId.level()) > player.getLevel()) && !character().allowEntireTree()) {
			return false;
		}
		
		if (!validateClassId(currentClassId, val)) {
			return false;
		}
		
		final int newJobLevel = currentClassId.level() + 1;
		
		// Weight/Inventory check
		if (!character().getClassMaster().getRewardItems(newJobLevel).isEmpty() && !player.isInventoryUnder90(false)) {
			player.sendPacket(INVENTORY_LESS_THAN_80_PERCENT);
			return false;
		}
		
		// check if player have all required items for class transfer
		for (ItemHolder holder : character().getClassMaster().getRequireItems(newJobLevel)) {
			if (player.getInventory().getInventoryItemCount(holder.getId(), -1) < holder.getCount()) {
				player.sendPacket(NOT_ENOUGH_ITEMS);
				return false;
			}
		}
		
		// get all required items for class transfer
		for (ItemHolder holder : character().getClassMaster().getRequireItems(newJobLevel)) {
			if (!player.destroyItemByItemId("ClassMaster", holder.getId(), holder.getCount(), player, true)) {
				return false;
			}
		}
		
		// reward player with items
		for (ItemHolder holder : character().getClassMaster().getRewardItems(newJobLevel)) {
			player.addItem("ClassMaster", holder.getId(), holder.getCount(), player, true);
		}
		
		player.setClassId(val);
		
		if (player.isSubClassActive()) {
			player.getSubClasses().get(player.getClassIndex()).setClassId(player.getActiveClass());
		} else {
			player.setBaseClass(player.getActiveClass());
		}
		
		player.broadcastUserInfo();
		
		if (character().getClassMaster().isAllowed(player.getClassId().level() + 1) && character().alternateClassMaster() && (((player.getClassId().level() == 1) && (player.getLevel() >= 40)) || ((player.getClassId().level() == 2) && (player.getLevel() >= 76)))) {
			showQuestionMark(player);
		}
		
		return true;
	}
	
	/**
	 * @param level - current skillId level (0 - start, 1 - first, etc)
	 * @return minimum player level required for next class transfer
	 */
	private static int getMinLevel(int level) {
		return switch (level) {
			case 0 -> 20;
			case 1 -> 40;
			case 2 -> 76;
			default -> Integer.MAX_VALUE;
		};
	}
	
	/**
	 * Returns true if class change is possible
	 * @param oldCID current player ClassId
	 * @param val new class index
	 * @return {@code true} if the class ID is valid
	 */
	private static boolean validateClassId(ClassId oldCID, int val) {
		return validateClassId(oldCID, ClassId.getClassId(val));
	}
	
	/**
	 * Returns true if class change is possible
	 * @param oldCID current player ClassId
	 * @param newCID new ClassId
	 * @return true if class change is possible
	 */
	private static boolean validateClassId(ClassId oldCID, ClassId newCID) {
		return (newCID != null) && (newCID.getRace() != null) && ((oldCID.equals(newCID.getParent()) || (character().allowEntireTree() && newCID.childOf(oldCID))));
	}
	
	private static String getRequiredItems(int level) {
		if ((character().getClassMaster().getRequireItems(level) == null) || character().getClassMaster().getRequireItems(level).isEmpty()) {
			return "<tr><td>none</td></tr>";
		}
		final StringBuilder sb = new StringBuilder();
		for (ItemHolder holder : character().getClassMaster().getRequireItems(level)) {
			sb.append("<tr><td><font color=\"LEVEL\">");
			sb.append(holder.getCount());
			sb.append("</font></td><td>");
			sb.append(ItemTable.getInstance().getTemplate(holder.getId()).getName());
			sb.append("</td></tr>");
		}
		return sb.toString();
	}
}