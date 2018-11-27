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

package com.noodlemire.chancelpixeldungeon.items.artifacts;

import com.noodlemire.chancelpixeldungeon.Dungeon;
import com.noodlemire.chancelpixeldungeon.actors.Actor;
import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Buff;
import com.noodlemire.chancelpixeldungeon.actors.buffs.Cripple;
import com.noodlemire.chancelpixeldungeon.actors.buffs.LockedFloor;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.effects.Chains;
import com.noodlemire.chancelpixeldungeon.effects.Pushing;
import com.noodlemire.chancelpixeldungeon.mechanics.Ballistica;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.scenes.CellSelector;
import com.noodlemire.chancelpixeldungeon.scenes.GameScene;
import com.noodlemire.chancelpixeldungeon.sprites.ItemSpriteSheet;
import com.noodlemire.chancelpixeldungeon.tiles.DungeonTilemap;
import com.noodlemire.chancelpixeldungeon.ui.QuickSlotButton;
import com.noodlemire.chancelpixeldungeon.utils.BArray;
import com.noodlemire.chancelpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class EtherealChains extends Artifact {

	public static final String AC_CAST       = "CAST";

	{
		image = ItemSpriteSheet.ARTIFACT_CHAINS;

		levelCap = 5;
		exp = 0;

		charge = 5;

		defaultAction = AC_CAST;
		usesTargeting = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped(hero) && charge > 0 && !cursed)
			actions.add(AC_CAST);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_CAST)){

			curUser = hero;

			if (!isEquipped( hero )) {
				GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				QuickSlotButton.cancel();

			} else if (charge < 1) {
				GLog.i( Messages.get(this, "no_charge") );
				QuickSlotButton.cancel();

			} else if (cursed) {
				GLog.w( Messages.get(this, "cursed") );
				QuickSlotButton.cancel();

			} else {
				GameScene.selectCell(caster);
			}

		}
	}

	private CellSelector.Listener caster = new CellSelector.Listener(){

		@Override
		public void onSelect(Integer target) {
			if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

				//chains cannot be used to go where it is impossible to walk to
				PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
				if (PathFinder.distance[curUser.pos] == Integer.MAX_VALUE){
					GLog.w( Messages.get(EtherealChains.class, "cant_reach") );
					return;
				}
				
				final Ballistica chain = new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET);
				
				if (Actor.findChar( chain.collisionPos ) != null){
					chainEnemy( chain, curUser, Actor.findChar( chain.collisionPos ));
				} else {
					chainLocation( chain, curUser );
				}

			}

		}

		@Override
		public String prompt() {
			return Messages.get(EtherealChains.class, "prompt");
		}
	};
	
	//pulls an enemy to a position along the chain's path, as close to the hero as possible
	private void chainEnemy( Ballistica chain, final Hero hero, final Char enemy ){
		
		if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
			GLog.w( Messages.get(this, "cant_pull") );
			return;
		}
		
		int bestPos = -1;
		for (int i : chain.subPath(1, chain.dist)){
			//prefer to the earliest point on the path
			if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
				bestPos = i;
				break;
			}
		}
		
		if (bestPos == -1) {
			GLog.i(Messages.get(this, "does_nothing"));
			return;
		}
		
		final int pulledPos = bestPos;
		
		int chargeUse = Dungeon.level.distance(enemy.pos, pulledPos);
		if (chargeUse > charge) {
			GLog.w( Messages.get(this, "no_charge") );
			return;
		} else {
			charge -= chargeUse;
			updateQuickslot();
		}
		
		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), enemy.sprite.center(), new Callback() {
			public void call() {
				Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
					public void call() {
						Dungeon.level.press(pulledPos, enemy, true);
					}
				}));
				enemy.pos = pulledPos;
				Dungeon.observe();
				GameScene.updateFog();
				hero.spendAndNext(1f);
			}
		}));
	}
	
	//pulls the hero along the chain to the collosionPos, if possible.
	private void chainLocation( Ballistica chain, final Hero hero ){
		
		//don't pull if the collision spot is in a wall
		if (Dungeon.level.solid[chain.collisionPos]){
			GLog.i( Messages.get(this, "inside_wall"));
			return;
		}
		
		//don't pull if there are no solid objects next to the pull location
		boolean solidFound = false;
		for (int i : PathFinder.NEIGHBOURS8){
			if (Dungeon.level.solid[chain.collisionPos + i]){
				solidFound = true;
				break;
			}
		}
		if (!solidFound){
			GLog.i( Messages.get(EtherealChains.class, "nothing_to_grab") );
			return;
		}
		
		final int newHeroPos = chain.collisionPos;
		
		int chargeUse = Dungeon.level.distance(hero.pos, newHeroPos);
		if (chargeUse > charge){
			GLog.w( Messages.get(EtherealChains.class, "no_charge") );
			return;
		} else {
			charge -= chargeUse;
			updateQuickslot();
		}
		
		hero.busy();
		hero.sprite.parent.add(new Chains(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(newHeroPos), new Callback() {
			public void call() {
				Actor.add(new Pushing(hero, hero.pos, newHeroPos, new Callback() {
					public void call() {
						Dungeon.level.press(newHeroPos, hero);
					}
				}));
				hero.spendAndNext(1f);
				hero.pos = newHeroPos;
				Dungeon.observe();
				GameScene.updateFog();
			}
		}));
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new chainsRecharge();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (isEquipped( Dungeon.hero )){
			desc += "\n\n";
			if (cursed)
				desc += Messages.get(this, "desc_cursed");
			else
				desc += Messages.get(this, "desc_equipped");
		}
		return desc;
	}

	public class chainsRecharge extends ArtifactBuff{

		@Override
		public boolean act() {
			int chargeTarget = 5+(level()*2);
			LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeTarget && !cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1 / (40f - (chargeTarget - charge)*2f);
			} else if (cursed && Random.Int(100) == 0){
				Buff.prolong( target, Cripple.class, 10f);
			}

			if (partialCharge >= 1) {
				partialCharge --;
				charge ++;
			}

			updateQuickslot();

			spend( TICK );

			return true;
		}

		public void gainExp( float levelPortion ) {
			if (cursed) return;

			exp += Math.round(levelPortion*100);

			//past the soft charge cap, gaining  charge from leveling is slowed.
			if (charge > 5+(level()*2)){
				levelPortion *= (5+((float)level()*2))/charge;
			}
			partialCharge += levelPortion*10f;

			if (exp > 100+level()*50 && level() < levelCap){
				exp -= 100+level()*50;
				GLog.p( Messages.get(this, "levelup") );
				upgrade();
			}

		}
	}
}
