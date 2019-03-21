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

package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Freezing;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.actors.mobs.Thief;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.food.FrozenCarpaccio;
import com.noodlemire.chancelpixeldungeon.items.food.MysteryMeat;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Frost extends FlavourBuff implements Expulsion
{
	private static final float DURATION = 5f;

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public boolean attachTo(Char target)
	{
		if(super.attachTo(target))
		{

			target.paralysed++;
			Buff.detach(target, Burning.class);
			Buff.detach(target, Chill.class);

			if(target instanceof Hero)
			{

				Hero hero = (Hero) target;
				ArrayList<Item> freezable = new ArrayList<>();
				//does not reach inside of containers
				for(Item i : hero.belongings.backpack.items)
				{
					if(i instanceof Potion || i instanceof MysteryMeat)
					{
						freezable.add(i);
					}
				}

				if(!freezable.isEmpty())
				{
					Item toFreeze = Random.element(freezable).detach(hero.belongings.backpack);
					if(toFreeze instanceof Potion)
					{
						((Potion) toFreeze).shatter(hero.pos);
					}
					else if(toFreeze instanceof MysteryMeat)
					{
						FrozenCarpaccio carpaccio = new FrozenCarpaccio();
						if(!carpaccio.collect(hero.belongings.backpack))
						{
							Dungeon.level.drop(carpaccio, target.pos).sprite.drop();
						}
					}
					GLog.w(Messages.get(this, "freezes", toFreeze.toString()));
				}

			}
			else if(target instanceof Thief)
			{

				Item item = ((Thief) target).item;

				if(item instanceof Potion)
				{
					((Potion) ((Thief) target).item).shatter(target.pos);
					((Thief) target).item = null;
				}
				else if(item instanceof MysteryMeat)
				{
					((Thief) target).item = new FrozenCarpaccio();
				}

			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void detach()
	{
		super.detach();
		if(target.paralysed > 0)
			target.paralysed--;
		if(Dungeon.level.water[target.pos])
			Buff.prolong(target, Chill.class, 4f);
	}

	@Override
	public int icon()
	{
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on)
	{
		if(on) target.sprite.add(CharSprite.State.FROZEN);
		else target.sprite.remove(CharSprite.State.FROZEN);
	}

	@Override
	public String toString()
	{
		return Messages.get(this, "name");
	}

	@Override
	public String desc()
	{
		return Messages.get(this, "desc", dispTurns());
	}

	@Override
	public Class<? extends Blob> expulse()
	{
		//I don't know how you'd drink a potion while frozen, but here's something to happen just in case.
		return Freezing.class;
	}

	public static float duration(Char ch)
	{
		return DURATION;
	}
}
