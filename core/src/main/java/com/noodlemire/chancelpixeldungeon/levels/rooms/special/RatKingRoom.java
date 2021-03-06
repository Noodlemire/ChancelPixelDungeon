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

package com.noodlemire.chancelpixeldungeon.levels.rooms.special;

import com.noodlemire.chancelpixeldungeon.actors.mobs.npcs.RatKing;
import com.noodlemire.chancelpixeldungeon.items.Gold;
import com.noodlemire.chancelpixeldungeon.items.Heap;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.levels.Level;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.levels.painters.Painter;
import com.noodlemire.chancelpixeldungeon.levels.rooms.Room;
import com.noodlemire.chancelpixeldungeon.levels.rooms.standard.SewerBossEntranceRoom;
import com.watabou.utils.Random;

public class RatKingRoom extends SpecialRoom
{
	@Override
	public boolean canConnect(Room r)
	{
		//never connects at the entrance
		return !(r instanceof SewerBossEntranceRoom) && super.canConnect(r);
	}

	@Override
	public int minConnections(int direction)
	{
		return 2;
	}

	@Override
	public int maxConnections(int direction)
	{
		return 2;
	}

	//reduced max size to limit chest numbers.
	// normally would gen with 8-28, this limits it to 8-16
	@Override
	public int maxHeight()
	{
		return 7;
	}

	public int maxWidth()
	{
		return 7;
	}

	public void paint(Level level)
	{
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);

		for(Door door : connected.values())
			door.set(Door.Type.REGULAR);

		for(int i = left + 1; i < right; i++)
		{
			addChest(level, (top + 1) * level.width() + i);
			addChest(level, (bottom - 1) * level.width() + i);
		}

		for(int i = top + 2; i < bottom - 1; i++)
		{
			addChest(level, i * level.width() + left + 1);
			addChest(level, i * level.width() + right - 1);
		}

		RatKing king = new RatKing();
		king.pos = level.pointToCell(random(2));
		level.mobs.add(king);
	}

	private static void addChest(Level level, int pos)
	{
		Item prize = new Gold(Random.IntRange(10, 25));

		level.drop(prize, pos).type = Heap.Type.CHEST;
	}
}
