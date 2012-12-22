/* Petroleum Generator: a Buildcraft/IndustrialCraft2 crossover mod for Minecraft.
 *
 * Version 0.4
 *
 * This software is available through the following channels:
 * http://github.com/chrisduran/petroleumgenerator
 *
 * Copyright (c) 2012  Chris Duran
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * https://github.com/chrisduran/petroleumgenerator/blob/master/LICENCE
 *
 */

package drceph.petrogen.common;

import ic2.api.Direction;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import buildcraft.BuildCraftEnergy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
//import ic2.api.Direction;
//import ic2.api.EnergyNet;
//import ic2.api.IEnergySource;
//import ic2.api.INetworkDataProvider;
//import ic2.api.INetworkUpdateListener;
//import ic2.api.NetworkHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import net.minecraftforge.liquids.LiquidDictionary.LiquidRegisterEvent;

/**
 * @author chris 
 *         fuel outputs 96000EU @ 16EU/t; oil outputs 9600EU @ 8EU/t
 *         for easy maths:
 *         100000EU @ 20EU/t (1 'volume' == 5 ticks); oil 10000EU @ 10EU/t (1 'volume' == 1 tick)
 */
public class TileEntityPetroleumGenerator extends TileEntity implements 
			IInventory, ITankContainer, IEnergySource {
	
	//CONSTANTS
	public static final int MAX_VOLUME = LiquidContainerRegistry.BUCKET_VOLUME * 10;
	public static final int MAX_CHARGE = 30000;
	public static final int FUEL_GAUGE_SCALE = 58;
	public static final int ENERGY_GAUGE_SCALE = 24;
	public static final int SLOT_COUNT = 1;
	
	//Proxied
	public int amount;
	public int charge;
	public int active;
	public int currentEu;

	//Variables
	private ItemStack[] inventory;
	private boolean initialised;
	private FuelPetroleumGenerator fuel;
	private int buffer;
	private boolean hasRedstonePower; //Does NO work on redstone power (IC2 paradigm)

	public TileEntityPetroleumGenerator() {
		this.inventory = new ItemStack[SLOT_COUNT];
		amount = 0;
		charge = 0;
		buffer = 0;
		currentEu = 0;
		fuel = null;
		hasRedstonePower = false;
	}
	
	/**
	 * gets the current liquidstack and liquidstack volume for this tank
	 * @return LiquidStack representing the tank contents, null if empty tank.
	 */
	public LiquidStack getLiquidStack() {
		if (fuel != null) {
			LiquidStack newStack = fuel.getFuel().copy();
			newStack.amount = amount;
			return newStack;
		} else {
			return null;
		}
	}

	@Override
	public void updateEntity() {
		
		
		boolean didBurn = false;
		boolean didExpel = false;
		boolean changed = false;
		// Add to energynet on first update, and initialise network voodoo
		if (!initialised && worldObj != null) {
			if (!worldObj.isRemote) {
				EnergyNet.getForWorld(worldObj).addTileEntity(this);
				updateRedstoneStatus();
			}
			initialised = true;
			changed = true;
		}
		
		//only runs it on the serverside, so that it updates the client???
		//remember to notify of change
		if (!worldObj.isRemote) {
			
			//STEP 1: Try to fill tank
			if (amount <= MAX_VOLUME - LiquidContainerRegistry.BUCKET_VOLUME) {
				changed = fillTankFromInventory(this.inventory[0]);
			}
				
			//STEP 2: Try to charge battery
			if (amount > 0) {
				if (buffer <= 0 && charge < MAX_CHARGE-fuel.getEuPacketSize()) {
					amount--;
					buffer = fuel.getEuPerLiquidUnit();
				}
			}
			
			if (buffer > 0) {
				buffer -= currentEu;
				charge += currentEu;
				charge = Math.min(charge, MAX_CHARGE);
				didBurn = true;
				//changed = true;
			}
			
			//STEP 3: Try to emit power (from charge; unless redstoned)
			if (!hasRedstonePower && charge >= currentEu) {
				int unused = EnergyNet.getForWorld(worldObj).emitEnergyFrom(this,currentEu);
				didExpel = currentEu != unused;
				charge -= (currentEu-unused);
				//changed = true;
			}
			
			//STEP 4: cleanup
			if (buffer <= 0 && amount <= 0) {
				fuel = null;
				//changed = true;
			}
		
		
			if (didExpel || didBurn) {
				active = 1;
			} else {
				active = 0;
			}
			
			BlockPetroleumGenerator.updateBlockState(active==1, worldObj, xCoord, yCoord, zCoord);
		
		}
		if (changed) {
			onInventoryChanged();
		}
		


	}
	

	private boolean fillTankFromInventory(ItemStack itemStack) {
	
		boolean changed = false;
		
		LiquidStack liquid = LiquidContainerRegistry.getLiquidForFilledItem(itemStack);
		if (liquid == null) return changed;
		
		if (this.fuel == null && FuelPetroleumGenerator.isValidFuel(liquid.itemID)) {
			setCurrentLiquid(liquid.itemID);
			
		}
		
		if (isCurrentFuel(liquid)) {
			this.fill(0,liquid,true);
			if (LiquidContainerRegistry.isBucket(itemStack)) {
				this.inventory[0] = itemStack.getItem().getContainerItemStack(null);
			} else {
				decrStackSize(0, 1);
			}
			changed = true;
		}
		
		return changed;
	}
	
	public void setCurrentLiquid(int id) {
		this.fuel = FuelPetroleumGenerator.getFuelByItemId(id);
		if (fuel != null) {
			this.currentEu = fuel.getEuPacketSize();
		}
	}
	
	public int getCurrentLiquidId() {
		if (fuel != null) {
			return fuel.getItemId();
		} else {
			return 0;
		}
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}

	// @Override
	public boolean isAddedToEnergyNet() {
		return initialised;
	}

	@Override
	public void invalidate() {
		// remove from EnergyNet when invalidating the TE
		if (worldObj != null && initialised) {
			EnergyNet.getForWorld(worldObj).removeTileEntity(this);
		}
		super.invalidate();
	}
	
	

	//@Override
	public int getMaxEnergyOutput() {
		return 16;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				onInventoryChanged();
				return itemstack;
			}
			ItemStack itemstack1 = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			onInventoryChanged();
			return itemstack1;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
		if (this.inventory[var1] != null) {
			ItemStack var2 = this.inventory[var1];
			this.inventory[var1] = null;
			return var2;
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "TileEntityPetroleumGenerator";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
		if (worldObj == null) {
			return true;
		}
		if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this) {
			return false;
		}
		return entityPlayer.getDistanceSq((double) xCoord + 0.5D,
				(double) yCoord + 0.5D, (double) zCoord + 0.5D) <= 64D;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);

			byte slot = tag.getByte("Slot");

			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		int liquidId = tagCompound.getInteger("liquidId");
		setCurrentLiquid(liquidId);
		amount = tagCompound.getInteger("amount");
		charge = tagCompound.getInteger("charge");
		buffer = tagCompound.getInteger("buffer");
		currentEu = tagCompound.getInteger("currentEu");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];

			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();

				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		tagCompound.setTag("Inventory", itemList);
		int liquidId = fuel==null?0:fuel.getItemId();
		tagCompound.setInteger("liquidId", liquidId);
		tagCompound.setInteger("amount", amount);
		tagCompound.setInteger("charge",charge);
		tagCompound.setInteger("buffer",buffer);
		tagCompound.setInteger("currentEu",currentEu);
	}

	@Override
	public void openChest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub

	}

	//@Override
	public void onNetworkUpdate(String field) {

	}

	private static List<String> fields = Arrays.asList(new String[0]);

	//@Override
	public List<String> getNetworkedFields() {
		return fields;
	}
	
	public int getScaledVolume() {
		double ratio = MAX_VOLUME / (FUEL_GAUGE_SCALE*1.0);
		double scaled_volume = amount / ratio;
		return ((int) Math.round(scaled_volume));
	}
	
	public int getScaledEnergy() {
		double ratio = MAX_CHARGE / (ENERGY_GAUGE_SCALE*1.0);
		double scaled_charge = charge / ratio;
		return ((int) Math.round(scaled_charge));
	}
	
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return this.fill(0, resource, doFill);
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		//System.err.println("Current: "+liquidId+":"+liquidMeta+" Input: "+resource.itemID+":"+resource.itemMeta);
		if (fuel == null && FuelPetroleumGenerator.isValidFuel(resource.itemID)) {
			setCurrentLiquid(resource.itemID);
		}
		
		if (!isCurrentFuel(resource)) {
			return 0;
		}
		
		int payload = resource.amount;
		
		if (amount+payload <= MAX_VOLUME) {
			if (doFill) {
				amount += payload;
			}
			return payload;
		} else {
			int difference = MAX_VOLUME - amount;
			if (doFill) {
				amount = MAX_VOLUME;
			}
			return difference;
		}
	}
	
	private boolean isCurrentFuel(LiquidStack resource) {
		if (fuel == null || fuel.getItemId() != resource.itemID) {
			return false;
		} 
		return true;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return new LiquidTank[] {new LiquidTank(MAX_VOLUME)};
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return null;
	}
	
	public void updateRedstoneStatus() {
		hasRedstonePower = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}
	
	

}
