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

package com.noodlemire.chancelpixeldungeon.items.food;

import com.noodlemire.chancelpixeldungeon.Challenges;
import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.ChancelPixelDungeon;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.EarthImbue;
import com.noodlemire.chancelpixeldungeon.actors.buffs.FireImbue;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Hunger;
import com.noodlemire.chancelpixeldungeon.actors.buffs.ToxicImbue;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.items.Item;
import com.noodlemire.chancelpixeldungeon.items.Recipe;
import com.noodlemire.chancelpixeldungeon.items.potions.Potion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfCorrosivity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfFrost;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHealing;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfInvisibility;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfLevitation;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfHydrocombustion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfMight;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPlacebo;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfPurity;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRefreshment;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfRepulsion;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfShielding;
import com.noodlemire.chancelpixeldungeon.items.potions.PotionOfToxicity;
import com.noodlemire.chancelpixeldungeon.levels.Terrain;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.plants.Plant.Seed;
import com.noodlemire.chancelpixeldungeon.plants.Sungrass;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSprite;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Blandfruit extends Food {

	public Potion potionAttrib = null;
	public ItemSprite.Glowing potionGlow = null;

	{
		stackable = true;
		image = ItemSpriteSheet.BLANDFRUIT;

		//only applies when blandfruit is cooked
		energy = Hunger.STARVING;

		bones = true;
	}

	@Override
	public boolean isSimilar( Item item ) {
		if (item instanceof Blandfruit){
			if (potionAttrib == null){
                return ((Blandfruit) item).potionAttrib == null;
			} else if (((Blandfruit)item).potionAttrib != null){
                return ((Blandfruit) item).potionAttrib.getClass() == potionAttrib.getClass();
			}
		}
		return false;
	}

	@Override
	public void execute( Hero hero, String action ) {

		if (action.equals( AC_EAT ) && potionAttrib == null) {

			GLog.w( Messages.get(this, "raw"));
			return;

		}

		super.execute(hero, action);

		if (action.equals( AC_EAT ) && potionAttrib != null){

			if (potionAttrib instanceof PotionOfFrost) {
				GLog.i(Messages.get(this, "ice_msg"));
				FrozenCarpaccio.effect(hero);
			} else if (potionAttrib instanceof PotionOfHydrocombustion){
				GLog.i(Messages.get(this, "fire_msg"));
				Buff.affect(hero, FireImbue.class).set(FireImbue.DURATION);
			} else if (potionAttrib instanceof PotionOfToxicity) {
				GLog.i(Messages.get(this, "toxic_msg"));
				Buff.affect(hero, ToxicImbue.class).set(ToxicImbue.DURATION);
			} else if (potionAttrib instanceof PotionOfCorrosivity) {
				GLog.i(Messages.get(this, "para_msg"));
				Buff.affect(hero, EarthImbue.class, EarthImbue.DURATION);
			} else {
				potionAttrib.apply(hero);
			}

		}
	}

	@Override
	public String desc() {
		if (potionAttrib== null) return super.desc();
		else return Messages.get(this, "desc_cooked");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public Item cook(Seed seed){

		try {
			return imbuePotion((Potion)seed.alchemyClass.newInstance());
		} catch (Exception e) {
			ChancelPixelDungeon.reportException(e);
			return null;
		}

	}

	public Item imbuePotion(Potion potion){

		potionAttrib = potion;
		potionAttrib.ownedByFruit = true;

		potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;

		if (potionAttrib instanceof PotionOfHealing){
			name = Messages.get(this, "sunfruit");
			potionGlow = new ItemSprite.Glowing( 0x2EE62E );
		} else if (potionAttrib instanceof PotionOfCorrosivity){
			name = Messages.get(this, "rotfruit");
			potionGlow = new ItemSprite.Glowing( 0xCC0022 );
		} else if (potionAttrib instanceof PotionOfShielding){
			name = Messages.get(this, "earthfruit");
			potionGlow = new ItemSprite.Glowing( 0x67583D );
		} else if (potionAttrib instanceof PotionOfInvisibility){
			name = Messages.get(this, "blindfruit");
			potionGlow = new ItemSprite.Glowing( 0xE5D273 );
		} else if (potionAttrib instanceof PotionOfHydrocombustion){
			name = Messages.get(this, "firefruit");
			potionGlow = new ItemSprite.Glowing( 0xFF7F00 );
		} else if (potionAttrib instanceof PotionOfFrost){
			name = Messages.get(this, "icefruit");
			potionGlow = new ItemSprite.Glowing( 0x66B3FF );
		} else if (potionAttrib instanceof PotionOfRepulsion){
			name = Messages.get(this, "fadefruit");
			potionGlow = new ItemSprite.Glowing( 0xB8E6CF );
		} else if (potionAttrib instanceof PotionOfToxicity){
			name = Messages.get(this, "sorrowfruit");
			potionGlow = new ItemSprite.Glowing( 0xA15CE5 );
		} else if (potionAttrib instanceof PotionOfLevitation) {
			name = Messages.get(this, "stormfruit");
			potionGlow = new ItemSprite.Glowing( 0x1C3A57 );
		} else if (potionAttrib instanceof PotionOfRefreshment) {
			name = Messages.get(this, "dreamfruit");
			potionGlow = new ItemSprite.Glowing( 0x8E2975 );
		} else if (potionAttrib instanceof PotionOfMight) {
			name = Messages.get(this, "deadfruit");
			potionGlow = new ItemSprite.Glowing( 0x000000 );
		} else if (potionAttrib instanceof PotionOfPlacebo) {
			name = Messages.get(this, "metafruit");
			potionGlow = new ItemSprite.Glowing( 0x999999 );
		}

		return this;
	}

	private static final String POTIONATTRIB = "potionattrib";
	
	@Override
	protected void onThrow(int cell) {
		if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {
			super.onThrow( cell );
			
		} else if (potionAttrib instanceof PotionOfHydrocombustion ||
				potionAttrib instanceof PotionOfToxicity ||
				potionAttrib instanceof PotionOfCorrosivity ||
				potionAttrib instanceof PotionOfFrost ||
				potionAttrib instanceof PotionOfLevitation ||
				potionAttrib instanceof PotionOfPurity) {
			
			Dungeon.level.press( cell, null, true );
			potionAttrib.shatter( cell );
			
		} else {
			super.onThrow( cell );
		}
	}
	
	@Override
	public void reset() {
		if (potionAttrib != null)
			imbuePotion(potionAttrib);
		else
			super.reset();
	}
	
	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put( POTIONATTRIB , potionAttrib);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(POTIONATTRIB)) {
			imbuePotion((Potion) bundle.get(POTIONATTRIB));
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return potionGlow;
	}
	
	public static class CookFruit extends Recipe {
		
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;
			
			if (ingredients.get(0) instanceof Blandfruit){
				if (!(ingredients.get(1) instanceof Seed)){
					return false;
				}
			} else if (ingredients.get(0) instanceof Seed){
				if (ingredients.get(1) instanceof Blandfruit){
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}
			
			Blandfruit fruit = (Blandfruit) ingredients.get(0);
			Seed seed = (Seed) ingredients.get(1);
			
			if (fruit.quantity() >= 1 && fruit.potionAttrib == null
				&& seed.quantity() >= 1){

                return !Dungeon.isChallenged(Challenges.NO_HEALING)
                        || !(seed instanceof Sungrass.Seed);
            }
			
			return false;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 2;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);
			
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
	}

}
