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
package com.l2jserver.datapack.ai.npc.Merchant.Groot;

import com.l2jserver.datapack.ai.npc.Merchant.Merchant;

/**
* @author Charus
* @version 2.6.3.0
*/
public class Groot extends Merchant {
	
	private static final BuySellList[] _sellList0 = new BuySellList[] {
		new BuySellList(4445, 10, 0.000000, 0), // dye_s1c3_d
		new BuySellList(4446, 10, 0.000000, 0), // dye_s1d3_d
		new BuySellList(4447, 10, 0.000000, 0), // dye_c1s3_d
		new BuySellList(4448, 10, 0.000000, 0), // dye_c1d3_d
		new BuySellList(4449, 10, 0.000000, 0), // dye_d1s3_d
		new BuySellList(4450, 10, 0.000000, 0), // dye_d1c3_d
		new BuySellList(4451, 10, 0.000000, 0), // dye_i1m3_d
		new BuySellList(4452, 10, 0.000000, 0), // dye_i1w3_d
		new BuySellList(4453, 10, 0.000000, 0), // dye_m1i3_d
		new BuySellList(4454, 10, 0.000000, 0), // dye_m1w3_d
		new BuySellList(4455, 10, 0.000000, 0), // dye_w1i3_d
		new BuySellList(4456, 10, 0.000000, 0), // dye_w1m3_d
		new BuySellList(4457, 10, 0.000000, 0), // dye_s1c2_d
		new BuySellList(4458, 10, 0.000000, 0), // dye_s1d2_d
		new BuySellList(4459, 10, 0.000000, 0), // dye_c1s2_d
		new BuySellList(4460, 10, 0.000000, 0), // dye_c1d2_d
		new BuySellList(4461, 10, 0.000000, 0), // dye_d1s2_d
		new BuySellList(4462, 10, 0.000000, 0), // dye_d1c2_d
		new BuySellList(4463, 10, 0.000000, 0), // dye_i1m2_d
		new BuySellList(4464, 10, 0.000000, 0), // dye_i1w2_d
		new BuySellList(4465, 10, 0.000000, 0), // dye_m1i2_d
		new BuySellList(4466, 10, 0.000000, 0), // dye_m1w2_d
		new BuySellList(4467, 10, 0.000000, 0), // dye_w1i2_d
		new BuySellList(4468, 10, 0.000000, 0), // dye_w1m2_d
		new BuySellList(4481, 10, 0.000000, 0), // dye_s1c3_c
		new BuySellList(4482, 10, 0.000000, 0), // dye_s1d3_c
		new BuySellList(4483, 10, 0.000000, 0), // dye_c1s3_c
		new BuySellList(4484, 10, 0.000000, 0), // dye_c1c3_c
		new BuySellList(4485, 10, 0.000000, 0), // dye_d1s3_c
		new BuySellList(4486, 10, 0.000000, 0), // dye_d1c3_c
		new BuySellList(4487, 10, 0.000000, 0), // dye_i1m3_c
		new BuySellList(4488, 10, 0.000000, 0), // dye_i1w3_c
		new BuySellList(4489, 10, 0.000000, 0), // dye_m1i3_c
		new BuySellList(4490, 10, 0.000000, 0), // dye_m1w3_c
		new BuySellList(4491, 10, 0.000000, 0), // dye_w1i3_c
		new BuySellList(4492, 10, 0.000000, 0), // dye_w1m3_c
		new BuySellList(4493, 10, 0.000000, 0), // dye_s1c2_c
		new BuySellList(4494, 10, 0.000000, 0), // dye_s1d2_c
		new BuySellList(4495, 10, 0.000000, 0), // dye_c1s2_c
		new BuySellList(4496, 10, 0.000000, 0), // dye_c1c2_c
		new BuySellList(4497, 10, 0.000000, 0), // dye_d1s2_c
		new BuySellList(4498, 10, 0.000000, 0), // dye_d1c2_c
		new BuySellList(4499, 10, 0.000000, 0), // dye_i1m2_c
		new BuySellList(4500, 10, 0.000000, 0), // dye_i1w2_c
		new BuySellList(4501, 10, 0.000000, 0), // dye_m1i2_c
		new BuySellList(4502, 10, 0.000000, 0), // dye_m1w2_c
		new BuySellList(4503, 10, 0.000000, 0), // dye_w1i2_c
		new BuySellList(4504, 10, 0.000000, 0), // dye_w1m2_c
		new BuySellList(4505, 10, 0.000000, 0), // dye_s2c4_c
		new BuySellList(4506, 10, 0.000000, 0), // dye_s2d4_c
		new BuySellList(4507, 10, 0.000000, 0), // dye_c2s4_c
		new BuySellList(4508, 10, 0.000000, 0), // dye_c2c4_c
		new BuySellList(4509, 10, 0.000000, 0), // dye_d2s4_c
		new BuySellList(4510, 10, 0.000000, 0), // dye_d2c4_c
		new BuySellList(4511, 10, 0.000000, 0), // dye_i2m4_c
		new BuySellList(4512, 10, 0.000000, 0), // dye_i2w4_c
		new BuySellList(4513, 10, 0.000000, 0), // dye_m2i4_c
		new BuySellList(4514, 10, 0.000000, 0), // dye_m2w4_c
		new BuySellList(4515, 10, 0.000000, 0), // dye_w2i4_c
		new BuySellList(4516, 10, 0.000000, 0), // dye_w2m4_c
		new BuySellList(4517, 10, 0.000000, 0), // dye_s2c3_c
		new BuySellList(4518, 10, 0.000000, 0), // dye_s2d3_c
		new BuySellList(4519, 10, 0.000000, 0), // dye_c2s3_c
		new BuySellList(4520, 10, 0.000000, 0), // dye_c2c3_c
		new BuySellList(4521, 10, 0.000000, 0), // dye_d2s3_c
		new BuySellList(4522, 10, 0.000000, 0), // dye_d2c3_c
		new BuySellList(4523, 10, 0.000000, 0), // dye_i2m3_c
		new BuySellList(4524, 10, 0.000000, 0), // dye_i2w3_c
		new BuySellList(4525, 10, 0.000000, 0), // dye_m2i3_c
		new BuySellList(4526, 10, 0.000000, 0), // dye_m2w3_c
		new BuySellList(4527, 10, 0.000000, 0), // dye_w2i3_c
		new BuySellList(4528, 10, 0.000000, 0), // dye_w2m3_c
		new BuySellList(4529, 10, 0.000000, 0), // dye_s3c5_c
		new BuySellList(4530, 10, 0.000000, 0), // dye_s3d5_c
		new BuySellList(4531, 10, 0.000000, 0), // dye_c3s5_c
		new BuySellList(4532, 10, 0.000000, 0), // dye_c3c5_c
		new BuySellList(4533, 10, 0.000000, 0), // dye_d3s5_c
		new BuySellList(4534, 10, 0.000000, 0), // dye_d3c5_c
		new BuySellList(4535, 10, 0.000000, 0), // dye_i3m5_c
		new BuySellList(4536, 10, 0.000000, 0), // dye_i3w5_c
		new BuySellList(4537, 10, 0.000000, 0), // dye_m3i5_c
		new BuySellList(4538, 10, 0.000000, 0), // dye_m3w5_c
		new BuySellList(4539, 10, 0.000000, 0), // dye_w3i5_c
		new BuySellList(4540, 10, 0.000000, 0), // dye_w3m5_c
		new BuySellList(4541, 10, 0.000000, 0), // dye_s3c4_c
		new BuySellList(4542, 10, 0.000000, 0), // dye_s3d4_c
		new BuySellList(4543, 10, 0.000000, 0), // dye_c3s4_c
		new BuySellList(4544, 10, 0.000000, 0), // dye_c3c4_c
		new BuySellList(4545, 10, 0.000000, 0), // dye_d3s4_c
		new BuySellList(4546, 10, 0.000000, 0), // dye_d3c4_c
		new BuySellList(4547, 10, 0.000000, 0), // dye_i3m4_c
		new BuySellList(4548, 10, 0.000000, 0), // dye_i3w4_c
		new BuySellList(4549, 10, 0.000000, 0), // dye_m3i4_c
		new BuySellList(4550, 10, 0.000000, 0), // dye_m3w4_c
		new BuySellList(4551, 10, 0.000000, 0), // dye_w3i4_c
		new BuySellList(4552, 10, 0.000000, 0), // dye_w3m4_c
		new BuySellList(4565, 10, 0.000000, 0), // dye_s4c6_c
		new BuySellList(4566, 10, 0.000000, 0), // dye_s4d6_c
		new BuySellList(4567, 10, 0.000000, 0), // dye_c4s6_c
		new BuySellList(4568, 10, 0.000000, 0), // dye_c4c6_c
		new BuySellList(4569, 10, 0.000000, 0), // dye_d4s6_c
		new BuySellList(4570, 10, 0.000000, 0), // dye_d4c6_c
		new BuySellList(4571, 10, 0.000000, 0), // dye_i4m6_c
		new BuySellList(4572, 10, 0.000000, 0), // dye_i4w6_c
		new BuySellList(4573, 10, 0.000000, 0), // dye_m4i6_c
		new BuySellList(4574, 10, 0.000000, 0), // dye_m4w6_c
		new BuySellList(4575, 10, 0.000000, 0), // dye_w4i6_c
		new BuySellList(4576, 10, 0.000000, 0), // dye_w4m6_c
		new BuySellList(4577, 10, 0.000000, 0), // dye_s4c5_c
		new BuySellList(4578, 10, 0.000000, 0), // dye_s4d5_c
		new BuySellList(4579, 10, 0.000000, 0), // dye_c4s5_c
		new BuySellList(4580, 10, 0.000000, 0), // dye_c4c5_c
		new BuySellList(4581, 10, 0.000000, 0), // dye_d4s5_c
		new BuySellList(4582, 10, 0.000000, 0), // dye_d4c5_c
		new BuySellList(4583, 10, 0.000000, 0), // dye_i4m5_c
		new BuySellList(4584, 10, 0.000000, 0), // dye_i4w5_c
		new BuySellList(4585, 10, 0.000000, 0), // dye_m4i5_c
		new BuySellList(4586, 10, 0.000000, 0), // dye_m4w5_c
		new BuySellList(4587, 10, 0.000000, 0), // dye_w4i5_c
		new BuySellList(4588, 10, 0.000000, 0) // dye_w4m5_c
	};
	
	private static final int npcId = 30093;
	
	public Groot() {
		super(npcId);
		
		super.sellList0 = buildBuySellList(_sellList0, npcId, 0);
		
		super.fnHi = "groot001.htm";
		super.fnYouAreChaotic = "groot006.htm";
	}
}