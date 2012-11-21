package drceph.petrogen.common;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class BlockPetroleumGenerator extends BlockContainer {
	
	public BlockPetroleumGenerator(int id, int texture) {
		super(id,texture,Material.iron);
		this.setCreativeTab(CreativeTabs.tabRedstone);
	}

	@Override
	public String getTextureFile() {
		return "/drceph/petrogen/sprites/blocks.png";
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f, float g, float t){
		// Just making an instance of the TileEntity that the player clicked on
		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

		// Checking if the TileEntity is nothing or if the player is sneaking
		if(tile_entity == null || player.isSneaking()){
		// Returns false so it doesn't update anything
		return false;
		}
		
		if (world.isRemote) {
        	return true;
        }
		
		if (tile_entity instanceof TileEntityPetroleumGenerator) {
			
			TileEntityPetroleumGenerator cast_tile_entity = (TileEntityPetroleumGenerator) tile_entity;

			System.out.println(cast_tile_entity.amount);
			player.openGui(PetroleumGenerator.instance, 0, world, x, y, z);
			
		}
		// Returns true to force an update
		return true;
		}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		// TODO Auto-generated method stub
		TileEntity te = new TileEntityPetroleumGenerator();
		System.err.println(te.xCoord+"x"+te.yCoord+"x"+te.zCoord);
		return te;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		// TODO Auto-generated method stub
		return this.createNewTileEntity(world);
	}

	@Override
	public int getBlockTextureFromSide(int par1) {
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
	}
	
	

}
