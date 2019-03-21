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

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.Badges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Awareness;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.BlobEmitter;
import com.noodlemire.chancelpixeldungeon.effects.Identification;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.journal.Notes.Landmark;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class WaterOfAwareness extends WellWater
{

	@Override
	protected boolean affectHero(Hero hero)
	{

		Sample.INSTANCE.play(Assets.SND_DRINK);
		emitter.parent.add(new Identification(hero.sprite.center()));

		hero.belongings.observe();

		for(int i = 0; i < Dungeon.level.length(); i++)
		{

			int terr = Dungeon.level.map[i];
			if((Terrain.flags[terr] & Terrain.SECRET) != 0)
			{

				Dungeon.level.discover(i);

				if(Dungeon.level.heroFOV[i])
				{
					GameScene.discoverTile(i, terr);
				}
			}
		}

		Buff.affect(hero, Awareness.class, Awareness.DURATION);
		Dungeon.observe();

		Dungeon.hero.interrupt();

		GLog.p(Messages.get(this, "procced"));

		return true;
	}

	@Override
	protected Item affectItem(Item item)
	{
		if(item.isIdentified() || item.image == ItemSpriteSheet.POTION_UNSTABLE || item.image == ItemSpriteSheet.SCROLL_MYSTERY)
		{
			return null;
		}
		else
		{
			item.identify();
			Badges.validateItemLevelAquired(item);

			emitter.parent.add(new Identification(DungeonTilemap.tileCenterToWorld(pos)));

			return item;
		}
	}

	@Override
	protected Landmark record()
	{
		return Landmark.WELL_OF_AWARENESS;
	}

	@Override
	public void use(BlobEmitter emitter)
	{
		super.use(emitter);
		emitter.pour(Speck.factory(Speck.QUESTION), 0.3f);
	}

	@Override
	public String tileDesc()
	{
		return Messages.get(this, "desc");
	}
}
