/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.noodlemire.chancelpixeldungeon.items.weapon.melee;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class WarHammer extends MeleeWeapon
{
	{
		image = ItemSpriteSheet.WAR_HAMMER;
		tier = 5;
		ACC = 1.20f; //20% boost to accuracy
	}

	@Override
	public int max(int lvl)
	{
		//24 base, down from 30; scaling unchanged
		return super.max(lvl) - (tier + 1);
	}

	@Override
	public int crit(Char attacker, Char defender, int damage)
	{
		//No on-hit effect
		//See Mob/defenseSkill(char Enemy)
		return damage;
	}
}