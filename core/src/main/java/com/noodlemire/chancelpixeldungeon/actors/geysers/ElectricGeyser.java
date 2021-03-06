package com.noodlemire.chancelpixeldungeon.actors.geysers;

import com.noodlemire.chancelpixeldungeon.actors.blobs.Blob;
import com.noodlemire.chancelpixeldungeon.actors.blobs.Electricity;

public class ElectricGeyser extends AutoGeyser
{
	{
		spriteX = 3;
		spriteY = 2;
	}

	@Override
	public Class<? extends Blob> blobClass()
	{
		return Electricity.class;
	}
}
