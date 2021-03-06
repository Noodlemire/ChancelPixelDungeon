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

public class Kpinga extends MissileWeapon
{
	{
		image = ItemSpriteSheet.KPINGA;
		tier = 5;
		baseUses = 4;
		ACC = 2; //Twice as accurate as other missile weapons
	}

	@Override
	public int max(int lvl)
	{
		return super.max(lvl) + 10;
	}

	@Override
	public int STRReq(int lvl)
	{
		return super.STRReq(lvl) + 2;
	}
}