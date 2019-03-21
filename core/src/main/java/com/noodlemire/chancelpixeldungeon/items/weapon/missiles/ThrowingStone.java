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

import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class ThrowingStone extends MissileWeapon
{
	{
		image = ItemSpriteSheet.THROWING_STONE;
		tier = 1;
		sticky = false;
		bones = false;
	}

	@Override
	public int min(int lvl)
	{
		return Math.round(super.min(lvl) * 0.5f); //1, down from 2
	}

	@Override
	protected float durabilityPerUse()
	{
		return super.durabilityPerUse() * 3.34f;
	}

	@Override
	public int price()
	{
		return quantity;
	}
}
