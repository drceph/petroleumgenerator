package drceph.petrogen.common;

import net.minecraft.item.Item;

public class ItemBituminousProduct extends Item {
	
	public ItemBituminousProduct(int par1) {
		super(par1);
	}
	
	@Override
	public String getTextureFile() {
		return CommonProxy.ITEM_TEXTURE;
	}

}
