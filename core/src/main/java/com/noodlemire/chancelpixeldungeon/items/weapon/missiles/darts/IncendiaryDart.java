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

package com.noodlemire.chancelpixeldungeon.items.weapon.missiles.darts;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Burning;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

public class IncendiaryDart extends TippedDart
{

	{
		image = ItemSpriteSheet.INCENDIARY_DART;
	}

	@Override
	protected void onThrow(int cell)
	{
		Char enemy = Actor.findChar(cell);
		if((enemy == null || enemy == curUser) && Dungeon.level.flamable[cell])
		{
			GameScene.add(Blob.seed(cell, 4, Fire.class));
			Dungeon.level.drop(new Dart(), cell).sprite.drop();
		}
		else
		{
			super.onThrow(cell);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage)
	{
		Buff.affect(defender, Burning.class).reignite();
		return super.proc(attacker, defender, damage);
	}
}
