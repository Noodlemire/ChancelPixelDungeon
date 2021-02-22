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

import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class Greataxe extends MeleeWeapon
{
	{
		image = ItemSpriteSheet.GREATAXE;
		tier = 5;
	}

	@Override
	public int max(int lvl)
	{
		//50 base, up from 30; scaling unchanged
		return super.max(lvl) + 20;
	}

	@Override
	public int STRReq(int lvl)
	{
		//20 base strength req, up from 18
		return super.STRReq(lvl) + 2;
	}
}