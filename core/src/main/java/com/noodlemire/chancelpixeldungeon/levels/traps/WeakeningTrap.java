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

package com.noodlemire.chancelpixeldungeon.levels.traps;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Weakness;
import com.noodlemire.chancelpixeldungeon.effects.CellEmitter;
import com.noodlemire.chancelpixeldungeon.effects.particles.ShadowParticle;

public class WeakeningTrap extends Trap
{

	{
		color = GREEN;
		shape = WAVES;
	}

	@Override
	public void activate()
	{
		if(Dungeon.level.heroFOV[pos])
		{
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
		}

		Char ch = Actor.findChar(pos);
		if(ch == Dungeon.hero)
		{
			Buff.prolong(ch, Weakness.class, Weakness.DURATION * 2f);
		}
	}
}