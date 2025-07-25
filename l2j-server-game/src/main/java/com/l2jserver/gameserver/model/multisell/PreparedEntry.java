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
package com.l2jserver.gameserver.model.multisell;

import static com.l2jserver.gameserver.model.itemcontainer.Inventory.ADENA_ID;

import java.util.ArrayList;

import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author DS
 */
public class PreparedEntry extends Entry {
	private long _taxAmount = 0;
	
	public PreparedEntry(Entry template, L2ItemInstance item, boolean applyTaxes, boolean maintainEnchantment, double taxRate) {
		_entryId = template.getEntryId() * 100000;
		if (maintainEnchantment && (item != null)) {
			_entryId += item.getEnchantLevel();
		}
		
		ItemInfo info = null;
		long adenaAmount = 0;
		
		_ingredients = new ArrayList<>(template.getIngredients().size());
		for (Ingredient ing : template.getIngredients()) {
			if (ing.getItemId() == ADENA_ID) {
				// Tax ingredients added only if taxes enabled
				if (ing.isTaxIngredient()) {
					// if taxes are to be applied, modify/add the adena count based on the template adena/ancient adena count
					if (applyTaxes) {
						_taxAmount += Math.round(ing.getItemCount() * taxRate);
					}
				} else {
					adenaAmount += ing.getItemCount();
				}
				// do not yet add this adena amount to the list as non-taxIngredient adena might be entered later (order not guaranteed)
				continue;
			} else if (maintainEnchantment && (item != null) && ing.isArmorOrWeapon()) {
				info = new ItemInfo(item);
				final Ingredient newIngredient = ing.getCopy();
				newIngredient.setItemInfo(info);
				_ingredients.add(newIngredient);
			} else {
				final Ingredient newIngredient = ing.getCopy();
				_ingredients.add(newIngredient);
			}
		}
		
		// now add the adena, if any.
		adenaAmount += _taxAmount; // do not forget tax
		if (adenaAmount > 0) {
			_ingredients.add(new Ingredient(ADENA_ID, adenaAmount, false, false));
		}
		
		// now copy products
		_products = new ArrayList<>(template.getProducts().size());
		for (Ingredient ing : template.getProducts()) {
			if (!ing.isStackable()) {
				_stackable = false;
			}
			
			final Ingredient newProduct = ing.getCopy();
			if (maintainEnchantment && ing.isArmorOrWeapon()) {
				newProduct.setItemInfo(info);
			} else if (ing.isArmorOrWeapon() && (ing.getTemplate().getDefaultEnchantLevel() > 0)) {
				info = new ItemInfo(ing.getTemplate().getDefaultEnchantLevel());
				newProduct.setItemInfo(info);
			}
			_products.add(newProduct);
		}
	}
	
	@Override
	public final long getTaxAmount() {
		return _taxAmount;
	}
}