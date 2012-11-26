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

import java.util.logging.Logger;

import buildcraft.BuildCraftEnergy;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "drceph.petrogen", name = "Petroleum Generator", version = "0.5")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, clientPacketHandlerSpec = @SidedPacketHandler (channels = {"petrogen" }, packetHandler = drceph.petrogen.client.ClientPacketHandler.class),
			serverPacketHandlerSpec =@SidedPacketHandler(channels = {"petrogen" }, packetHandler = drceph.petrogen.common.ServerPacketHandler.class))

public class PetroleumGenerator {

	@SidedProxy(clientSide = "drceph.petrogen.client.ClientProxy", serverSide = "drceph.petrogen.common.CommonProxy")
	public static drceph.petrogen.common.CommonProxy proxy;
	
	@Instance("drceph.petrogen")
	public static PetroleumGenerator instance;
	
	public static Logger log = Logger.getLogger("PetroGen");
	
	//TODO add to configuration file
	public static int fuelEnergy = 100000; //EU output per bucket
	public static int oilEnergy = 10000; //EU output per bucket
	public static int fuelOutput = 20; //EU per tick
	public static int oilOutput = 10; //EU per tick
	
	private int petroleumGeneratorBlockId;
	
	public static Block petroleumGeneratorBlock;
	
	@PreInit
	public void load(FMLPreInitializationEvent event) {
		
		log.setParent(FMLLog.getLogger());
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		// loading the configuration from its file
        
		config.load();
        petroleumGeneratorBlockId = config.getBlock("block","blockPetroleumGenerator",3143).getInt();
        config.save();
		
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		
		petroleumGeneratorBlock = new BlockPetroleumGenerator(petroleumGeneratorBlockId,0);
		petroleumGeneratorBlock.setBlockName("petroleumGeneratorBlock");
		petroleumGeneratorBlock.setHardness(2.0f);
		petroleumGeneratorBlock.setStepSound(Block.soundMetalFootstep);
		
		GameRegistry.registerBlock(petroleumGeneratorBlock);
		MinecraftForge.setBlockHarvestLevel(petroleumGeneratorBlock, "pickaxe", 0);
		LanguageRegistry.addName(petroleumGeneratorBlock, "Petroleum Generator");
		ItemStack aStack = ic2.api.Items.getItem("electronicCircuit");
		ItemStack bStack = ic2.api.Items.getItem("generator");
		ItemStack cStack = new ItemStack(BuildCraftEnergy.engineBlock, 1, 2);
		ItemStack dStack = ic2.api.Items.getItem("waterCell");
		
		GameRegistry.addRecipe(new ItemStack(petroleumGeneratorBlock)," a "," b ","dcd",
				'a',aStack,'b',bStack,'c',cStack,'d',dStack);
		
		GameRegistry.registerTileEntity(TileEntityPetroleumGenerator.class, "TileEntityPetroleumGenerator");
		
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketOil)),oilEnergy,oilOutput,0);
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketFuel)),fuelEnergy,fuelOutput,1);
		
		//log.info(FuelPetroleumGenerator.getFuelByItemId(4064).getEuPerLiquidUnit()+" in this many ticks: "+FuelPetroleumGenerator.getFuelByItemId(4064).getTicksForLiquidUnit());
				
		proxy.registerRenderThings();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		
		
	}
	
}