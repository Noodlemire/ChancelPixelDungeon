package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Fire;

public class FlameGeyser extends ManualGeyser
{
	{
		spriteX = 0;
		spriteY = 1;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return Fire.class;
	}
}
