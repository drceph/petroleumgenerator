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

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
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
		int currentMetadata = world.getBlockMetadata(x, y, z);
		int newMetadata = active ? 1 : 0;
		if (currentMetadata > 1) newMetadata += 2;
		world.setBlockMetadataWithNotify(x,y,z,newMetadata);
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int par2) {
		switch (par2) {
			
			case 1:
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
			case 2:
				switch (par1) {
				case 0:
				case 1:
					return 1;
				case 2:
				case 3:
					return 1;
				default:
					return 3;
				}
			case 3:
				switch (par1) {
				case 0:
				case 1:
					return 1;
				case 2:
				case 3:
					return 1;
				default:
					return 0;
				}
			default:
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
	public void onBlockPlacedBy(World world, int x, int y, int z, net.minecraft.entity.EntityLiving player) {
		
		//from 0 to 3, inclusive	
		int facing = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		PetroleumGenerator.log.info("Function gave: "+facing);
		int currentMetadata = world.getBlockMetadata(x, y, z);
		
		switch (facing) {
		case 1:
		case 3:
			world.setBlockMetadataWithNotify(x,y,z,currentMetadata+2);
			break;
		default:
			world.setBlockMetadataWithNotify(x,y,z,currentMetadata);
		}
		
	};

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

                            ItemStack newItem = new ItemStack(item.itemID, item.stackSize, item.getItemDamage());
                            if (item.hasTagCompound()) {
                                newItem.setTagCompound((NBTTagCompound) item.getTagCompound().copy());
                            }
                            EntityItem entityItem = new EntityItem(world,
                                            x + rx, y + ry, z + rz,
                                            newItem);

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
