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

package drceph.petrogen.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import drceph.petrogen.common.CommonProxy;
import drceph.petrogen.common.TileEntityPetroleumGenerator;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderThings() {
		MinecraftForgeClient.preloadTexture("/drceph/petrogen/sprites/blocks.png");
		MinecraftForgeClient.preloadTexture(ITEM_TEXTURE);
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
