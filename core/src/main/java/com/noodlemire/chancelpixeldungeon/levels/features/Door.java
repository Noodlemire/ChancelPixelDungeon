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

package com.noodlemire.chancelpixeldungeon.levels.features;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;

public class Door
{
	public static void enter(int pos)
	{
		Level.set(pos, Terrain.OPEN_DOOR);
		GameScene.updateMap(pos);

		if(Dungeon.level.heroFOV[pos])
			Dungeon.observe();

		Dungeon.playAt(Assets.SND_OPEN, pos);
	}

	public static void leave(int pos)
	{
		if(Dungeon.level.heaps.get(pos) == null)
		{
			Level.set(pos, Terrain.DOOR);
			GameScene.updateMap(pos);
			if(Dungeon.level.heroFOV[pos])
				Dungeon.observe();
		}
	}
}
