package drceph.petrogen.common;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class FuelHandlerBituminousProduct implements IFuelHandler {

	private final int sludge_burn_time;
	
	public FuelHandlerBituminousProduct(int burnTime) {
		super();
		sludge_burn_time = burnTime;
	}
	
	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.itemID == PetroleumGenerator.bituminousSludgeItem.shiftedIndex) {
			return sludge_burn_time;
		} else if (fuel.itemID == PetroleumGenerator.bituminousSludgeBucketItem.shiftedIndex) {
			return sludge_burn_time * 5;
		} else {
			return 0;
		}
	}

}
