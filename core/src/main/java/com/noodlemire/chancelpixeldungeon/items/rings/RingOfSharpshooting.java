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

package com.noodlemire.chancelpixeldungeon.items.rings;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;

import java.text.DecimalFormat;

public class RingOfSharpshooting extends Ring
{
	{
		icon = ItemSpriteSheet.Icons.RING_SHARPSHOOTING;
	}

	@Override
	protected RingBuff buff()
	{
		return new Aim();
	}

	//roughly in line with the boost a weapon gets from an upgrade
	public static int levelDamageBonus( Char target )
	{
		return getBonus(target, RingOfSharpshooting.Aim.class);
	}

	public static float durabilityMultiplier(Char target)
	{
		return (float) (Math.pow(1.2, getBonus(target, Aim.class)));
	}

	public String statsInfo()
	{
		if(isIdentified())
			return Messages.get(this, "stats", soloBonus(), new DecimalFormat("#.##").format(100f * (Math.pow(1.2, soloBonus()) - 1f)));
		else
			return Messages.get(this, "typical_stats", 1, new DecimalFormat("#.##").format(20f));
	}

	private class Aim extends RingBuff {}
}
