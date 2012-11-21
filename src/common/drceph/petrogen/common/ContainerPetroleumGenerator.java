package drceph.petrogen.common;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Slot;

public class ContainerPetroleumGenerator extends Container {
	
	public TileEntityPetroleumGenerator tileEntity;
	private EntityPlayer invokingPlayer;
	private int lastAmount = 0;
	
	public ContainerPetroleumGenerator(IInventory playerInventory, TileEntityPetroleumGenerator tileEntity) {
		this.tileEntity = tileEntity;
		this.invokingPlayer = ((InventoryPlayer) playerInventory).player;
		layoutContainer(playerInventory,tileEntity);
	}

	private void layoutContainer(IInventory playerInventory, IInventory inventory) {
		addSlotToContainer(new Slot(inventory, 0, 22, 36));
		for (int inventoryRow = 0; inventoryRow < 3; inventoryRow++)
		{
			for (int inventoryColumn = 0; inventoryColumn < 9; inventoryColumn++)
			{
				addSlotToContainer(new Slot(playerInventory, inventoryColumn + inventoryRow * 9 + 9, 8 + inventoryColumn * 18, 84 + inventoryRow * 18));
			}
		}

		for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++)
		{
			addSlotToContainer(new Slot(playerInventory, hotbarSlot, 8 + hotbarSlot * 18, 142));
		}

	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return tileEntity.isUseableByPlayer(entityplayer);
	}
	
	public EntityPlayer getPlayer() {
		return invokingPlayer;
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1iCrafting) {
		// TODO Auto-generated method stub
		super.addCraftingToCrafters(par1iCrafting);
		
		par1iCrafting.sendProgressBarUpdate(this, 0, this.tileEntity.amount);
	}

	@Override
	public void updateCraftingResults() {
		// TODO Auto-generated method stub
		super.updateCraftingResults();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting crafter = (ICrafting)this.crafters.get(i);
			if (this.lastAmount != this.tileEntity.amount) {
				crafter.sendProgressBarUpdate(this, 0, this.tileEntity.amount);
			}
		}
		
		this.lastAmount = this.tileEntity.amount;
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory) {
		// TODO Auto-generated method stub
		super.onCraftMatrixChanged(par1iInventory);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2) {
		// TODO Auto-generated method stub
		super.updateProgressBar(par1, par2);
		
		if (par1 == 0) {
			this.tileEntity.amount = par2;
		}
	}
	
	

}
