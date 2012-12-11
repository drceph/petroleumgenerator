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

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class BlockPetroleumGenerator extends BlockContainer {

	public BlockPetroleumGenerator(int id, int texture) {
		super(id, texture, Material.iron);
		this.setRequiresSelfNotify();
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public String getTextureFile() {
		return "/drceph/petrogen/sprites/blocks.png";
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int i, float f, float g, float t) {
		// Just making an instance of the TileEntity that the player clicked on
		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

		if (tile_entity == null || player.isSneaking()) {
			return false;
		}

		if (world.isRemote) {
			return true;
		}

		if (tile_entity instanceof TileEntityPetroleumGenerator) {

			TileEntityPetroleumGenerator cast_tile_entity = (TileEntityPetroleumGenerator) tile_entity;
			player.openGui(PetroleumGenerator.instance, 0, world, x, y, z);

		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		TileEntity te = new TileEntityPetroleumGenerator();
		//System.err.println(te.xCoord + "x" + te.yCoord + "x" + te.zCoord);
		return te;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return this.createNewTileEntity(world);
	}
	
	public static void updateBlockState(boolean active, World world, int x, int y, int z) {
		if (active) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
		} else {
			world.setBlockMetadataWithNotify(x, y, z, 0);
		}
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		if (par2 == 1) {
			switch (par1) {
			case 0:
			case 1:
				return 2;
			case 2:
			case 3:
				return 0;
			default:
				return 1;
			}
		} else {
			return getBlockTextureFromSide(par1);
		}
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
		switch (par1) {
		case 0:
		case 1:
			return 2;
		case 2:
		case 3:
			return 3;
		default:
			return 1;
		}
	}
	
	

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
            dropItems(world, x, y, z);
            super.breakBlock(world, x, y, z, par5, par6);
    }
    
    private void dropItems(World world, int x, int y, int z){
            Random rand = new Random();

            TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
            if (!(tileEntity instanceof IInventory)) {
                    return;
            }
            IInventory inventory = (IInventory) tileEntity;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack item = inventory.getStackInSlot(i);

                    if (item != null && item.stackSize > 0) {
                            float rx = rand.nextFloat() * 0.8F + 0.1F;
                            float ry = rand.nextFloat() * 0.8F + 0.1F;
                            float rz = rand.nextFloat() * 0.8F + 0.1F;

                            EntityItem entityItem = new EntityItem(world,
                                            x + rx, y + ry, z + rz,
                                            new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                            if (item.hasTagCompound()) {
                                    entityItem.item.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                            }

                            float factor = 0.05F;
                            entityItem.motionX = rand.nextGaussian() * factor;
                            entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                            entityItem.motionZ = rand.nextGaussian() * factor;
                            world.spawnEntityInWorld(entityItem);
                            item.stackSize = 0;
                    }
            }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y,
    		int z, int neighbourBlockId) {
    	super.onNeighborBlockChange(world, x, y, z, neighbourBlockId);
    	TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
    	if (tileEntity != null && tileEntity instanceof TileEntityPetroleumGenerator) {
    		((TileEntityPetroleumGenerator) tileEntity).updateRedstoneStatus();
    	}
    }

}
