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

package com.noodlemire.chancelpixeldungeon.items.keys;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.journal.Notes;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public abstract class Key extends Item
{

	public static final float TIME_TO_UNLOCK = 1f;

	{
		unique = true;
	}

	public int depth;

	@Override
	public boolean stackable()
	{
		return true;
	}

	@Override
	public boolean isSimilar(Item item)
	{
		return item.getClass() == getClass() && ((Key) item).depth == depth;
	}

	@Override
	public boolean doPickUp(Hero hero)
	{
		GameScene.pickUpJournal(this, hero.pos);
		WndJournal.last_index = 1;
		Notes.add(this);
		Sample.INSTANCE.play(Assets.SND_ITEM);
		hero.spendAndNext(TIME_TO_PICK_UP);
		GameScene.updateKeyDisplay();
		return true;
	}

	private static final String DEPTH = "depth";

	@Override
	public void storeInBundle(Bundle bundle)
	{
		super.storeInBundle(bundle);
		bundle.put(DEPTH, depth);
	}

	@Override
	public void restoreFromBundle(Bundle bundle)
	{
		super.restoreFromBundle(bundle);
		depth = bundle.getInt(DEPTH);
	}

	@Override
	public boolean isUpgradable()
	{
		return false;
	}

	@Override
	public boolean isIdentified()
	{
		return true;
	}

}
