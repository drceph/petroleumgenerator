package drceph.petrogen.client;

import org.lwjgl.opengl.GL11;

import drceph.petrogen.common.*;
import net.minecraft.src.Container;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.IInventory;

public class GuiPetroleumGenerator extends GuiContainer {

	private TileEntityPetroleumGenerator tileEntity;
	
	public GuiPetroleumGenerator(IInventory player, TileEntityPetroleumGenerator chest) {
		super(new ContainerPetroleumGenerator(player, chest));
		this.tileEntity = chest;
		this.allowUserInput=false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2) {
        fontRenderer.drawString("Petroleum Generator", 8, 4, 0x404040);
        //System.out.println(container==null?"null":container.toString());
        //todo: makes a new container object each click, need to get the info from the tileentity??
        fontRenderer.drawString("V:" +tileEntity.amount, 8, (ySize - 96) + 2, 0x404040);
        
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int k = mc.renderEngine.getTexture("/drceph/petrogen/sprites/gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(k);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        
        //liquid
        int fuelScaled = tileEntity.getScaledVolume();
        if (FuelPetroleumGenerator.isValidFuel(tileEntity.liquidId)) {
        	int fuelOffset = FuelPetroleumGenerator.getFuelByItemId(tileEntity.liquidId).getGuiOffset();
        	drawTexturedModalRect(x+50, y+14+tileEntity.FUEL_GAUGE_SCALE-fuelScaled, 192+(16*fuelOffset), 0, 16, fuelScaled);
            drawTexturedModalRect(x+50, y+14, 176, 0, 16, 58); //liquid_scale_marks
        }
        
        //energy
        int energyScaled = 0;
        
	}

}
