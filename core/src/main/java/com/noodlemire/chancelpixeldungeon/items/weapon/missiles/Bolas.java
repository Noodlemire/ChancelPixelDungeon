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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Bolas extends MissileWeapon
{
	{
		image = ItemSpriteSheet.BOLAS;
		tier = 3;
	}

	@Override
	public int min(int lvl)
	{
		return Math.round(super.min(lvl) * 0.667f); //4, down from 6
	}

	@Override
	public int max(int lvl)
	{
		return Math.round(super.max(lvl) * 0.4f); //6, down from 15
	}

	@Override
	public int proc(Char attacker, Char defender, int damage)
	{
		Buff.prolong(defender, Cripple.class, Cripple.DURATION);
		return super.proc(attacker, defender, damage);
	}

	@Override
	protected float durabilityPerUse()
	{
		return super.durabilityPerUse() * 2f;
	}
}
