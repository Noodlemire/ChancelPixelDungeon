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
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Shadows;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShaftParticle;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public class Foliage extends Blob
{
	@Override
	protected void evolve()
	{
		int[] map = Dungeon.level.map;

		boolean visible = false;

		int cell;
		for(int i = area.left; i < area.right; i++)
		{
			for(int j = area.top; j < area.bottom; j++)
			{
				cell = i + j * Dungeon.level.width();
				if(cur[cell] > 0)
				{

					off[cell] = cur[cell];
					volume += off[cell];

					if(map[cell] == Terrain.EMBERS)
					{
						map[cell] = Terrain.GRASS;
						GameScene.updateMap(cell);
					}

					visible = visible || Dungeon.level.heroFOV[cell];

				}
				else
					off[cell] = 0;
			}
		}

		Hero hero = Dungeon.hero;
		if(hero.isAlive() && hero.visibleDangers() == 0 && cur[hero.pos] > 0)
			Buff.affect(hero, Shadows.class).prolong();

		if(visible)
			Notes.add(Notes.Landmark.GARDEN);
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);
		emitter.start(ShaftParticle.FACTORY, 0.9f, 0);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
