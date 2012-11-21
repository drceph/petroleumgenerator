package drceph.petrogen.common;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
	
	public void registerRenderThings() {
		
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te!=null && te instanceof TileEntityPetroleumGenerator) {
			TileEntityPetroleumGenerator tepg = (TileEntityPetroleumGenerator) te;
			return new ContainerPetroleumGenerator(player.inventory, tepg);
		} else { 
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

}
