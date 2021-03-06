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

package com.noodlemire.chancelpixeldungeon.items;

import com.noodlemire.chancelpixeldungeon.Assets;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Speck;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Dewdrop extends Item
{

	{
		image = ItemSpriteSheet.DEWDROP;
	}

	@Override
	public boolean stackable()
	{
		return true;
	}

	@Override
	public boolean doPickUp(Hero hero)
	{
		DewVial vial = hero.belongings.getItem(DewVial.class);

		if(vial != null && !vial.isFull())
			vial.collectDew(this);
		else
		{
			//20 drops for a full heal
			float healthPercent = 0.05f;
			int heal = Math.round(hero.HT() * healthPercent * quantity);

			int effect = Math.min(hero.HT() - hero.HP(), heal);
			if(effect > 0)
			{
				hero.heal(effect, this);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
			else
			{
				GLog.i(Messages.get(this, "already_full"));
				return false;
			}
		}

		Sample.INSTANCE.play(Assets.SND_DEWDROP);
		hero.spendAndNext(TIME_TO_PICK_UP);

		return true;
	}

	@Override
	//max of one dew in a stack
	public Item quantity(int value)
	{
		quantity = Math.min(value, 1);
		return this;
	}
}
