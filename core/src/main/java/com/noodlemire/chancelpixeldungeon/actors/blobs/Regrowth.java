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

package com.noodlemire.chancelpixeldungeon.actors.blobs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Roots;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.LeafParticle;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public class Regrowth extends GasBlob
{
	{
		harmful = true;
	}

	@Override
	void affect(Char ch, int cell) {
		Buff.prolong(ch, Roots.class, TICK);
	}

	@Override
	void affect(int cell)
	{
		int c = Dungeon.level.map[cell];
		int c1 = c;
		if(c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO)
			c1 = (cur[cell] > 9 && Actor.findChar(cell) == null)
					? Terrain.HIGH_GRASS : Terrain.GRASS;
		else if(c == Terrain.GRASS && cur[cell] > 9 && Dungeon.level.plants.get(cell) == null && Actor.findChar(cell) == null)
			c1 = Terrain.HIGH_GRASS;

		if(c1 != c)
		{
			Level.set(cell, c1);
			GameScene.updateMap(cell);
		}
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);

		emitter.start(LeafParticle.LEVEL_SPECIFIC, 0.2f, 0);
	}
}
