package drceph.petrogen.client;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import drceph.petrogen.common.CommonProxy;
import drceph.petrogen.common.TileEntityPetroleumGenerator;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings() {
		// TODO Auto-generated method stub
		MinecraftForgeClient.preloadTexture("/drceph/petrogen/sprites/blocks.png");
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int X, int Y, int Z) {
		TileEntity te=world.getBlockTileEntity(X, Y, Z);
		if (te!=null && te instanceof TileEntityPetroleumGenerator) {
			TileEntityPetroleumGenerator temg=(TileEntityPetroleumGenerator) te;
			return new GuiPetroleumGenerator(player.inventory, temg);
		} else {
			return null;
		}
	}
}
