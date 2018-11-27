package com.noodlemire.chancelpixeldungeon.actors.buffs;

import com.noodlemire.chancelpixeldungeon.actors.Char;
import com.noodlemire.chancelpixeldungeon.actors.hero.Hero;
import com.noodlemire.chancelpixeldungeon.messages.Messages;
import com.noodlemire.chancelpixeldungeon.sprites.CharSprite;
import com.noodlemire.chancelpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Might extends FlavourBuff {

    public static final float DURATION	= 200f;
    private static final String HTGAIN = "htgain";
    private int HTGain;

    {
        type = buffType.POSITIVE;
    }

    @Override
    public void storeInBundle( Bundle bundle )
    {
        super.storeInBundle( bundle );
        bundle.put( HTGAIN, HTGain );
    }

    @Override
    public void restoreFromBundle( Bundle bundle )
    {
        super.restoreFromBundle( bundle );
        HTGain = bundle.getInt( HTGAIN );
        set(false);
    }

    @Override
    public void detach() {
        target.sprite.showStatus( CharSprite.WARNING, Messages.get(this, "msg_detach") );
        if(target instanceof Hero)
        {
            Hero hero = (Hero)target;
            hero.HTBoost -= HTGain;
            hero.updateHT(false);
        }
        super.detach();
    }

    public void set(boolean newHT)
    {
        if(newHT) HTGain = target.HT/4;

        if(target instanceof Hero)
        {
            Hero hero = (Hero) target;

            hero.HTBoost += HTGain;
            hero.updateHT(true);
        }
        else if(newHT)
            target.HP += HTGain;
    }

    @Override
    public int icon() {
        return BuffIndicator.MIGHT;
    }

    @Override
    public void tintIcon(Image icon) {
        greyIcon(icon, 10f, cooldown());
    }

    @Override
    public String toString() {
        return Messages.get(this, "name");
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns());
    }
}
