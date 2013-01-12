/* Petroleum Generator: a Buildcraft/IndustrialCraft2 crossover mod for Minecraft.
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

import ic2.api.Ic2Recipes;

import java.util.List;
import java.util.logging.Logger;

import buildcraft.BuildCraftEnergy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
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

@Mod(modid = "drceph.petrogen", name = "Petroleum Generator", version = "1.2.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, clientPacketHandlerSpec = @SidedPacketHandler (channels = {"petrogen" }, packetHandler = drceph.petrogen.client.ClientPacketHandler.class),
			serverPacketHandlerSpec =@SidedPacketHandler(channels = {"petrogen" }, packetHandler = drceph.petrogen.common.ServerPacketHandler.class))

public class PetroleumGenerator {

	@SidedProxy(clientSide = "drceph.petrogen.client.ClientProxy", serverSide = "drceph.petrogen.common.CommonProxy")
	public static drceph.petrogen.common.CommonProxy proxy;
	
	@Instance("drceph.petrogen")
	public static PetroleumGenerator instance;
	
	public static Logger log = Logger.getLogger("PetroGen");
	
	//Fuel variables related to power and potential
	public static int oilPower = 10; //EU per tick
	public static int fuelPower = 25; //EU per tick
	private int oilStep = 10000;   
	private int fuelStep = 25000;  
	private int defaultOilMultiplier = 3;   // Configurable. step*multiplier is 
	private int defaultFuelMultiplier = 12; // EU output per bucket of fuel.
	
	//configuration file fields
	private int fuelMultiplier;
	private int oilMultiplier;
	private int petroleumGeneratorBlockId;
	private int bituminousSludgeId;
	private int bituminousSludgeBucketId;
	private int bituminousBurnTime;
	
	public static Block petroleumGeneratorBlock;
	public static ItemBituminousProduct bituminousSludgeItem = null;
	public static ItemBituminousProduct bituminousSludgeBucketItem = null;
	
	@PreInit
	public void load(FMLPreInitializationEvent event) {
		
		log.setParent(FMLLog.getLogger());
		
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		// loading the configuration from its file
        
		config.load();
        petroleumGeneratorBlockId = config.getBlock("block","blockPetroleumGenerator",3143).getInt();
     
        config.addCustomCategoryComment(Configuration.CATEGORY_GENERAL, "Oil is multiplied by 10,000 for the total EU/bucket (Default: 10,000 x 3 : 30,000 EU) \nFuel is multiplied by 25,000 for the total EU/bucket (Default: 25,000 x 12 : 300,000 EU)");
        oilMultiplier = config.get(Configuration.CATEGORY_GENERAL, "oil_multiplier", defaultOilMultiplier).getInt();
        oilMultiplier = Math.max(oilMultiplier, 1);
        fuelMultiplier = config.get(Configuration.CATEGORY_GENERAL, "fuel_multiplier", defaultFuelMultiplier).getInt();
        fuelMultiplier = Math.max(fuelMultiplier, 1);
        
        bituminousSludgeId = config.getItem("itemBituminousSludge", 11855).getInt();
        bituminousSludgeBucketId = config.getItem("itemBituminousSludgeBucket", 11856).getInt();
        
        bituminousBurnTime = config.get(Configuration.CATEGORY_GENERAL, "bituminous_sludge_burntime", 1200,"Default is 75% of coal burntime - set to zero to disable use of sludge as a fuel").getInt();
        bituminousBurnTime = Math.max(bituminousBurnTime, 0);
        
        config.save();
		
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		
		petroleumGeneratorBlock = new BlockPetroleumGenerator(petroleumGeneratorBlockId,0);
		petroleumGeneratorBlock.setBlockName("blockPetroleumGenerator");
		petroleumGeneratorBlock.setHardness(2.0f);
		petroleumGeneratorBlock.setStepSound(Block.soundMetalFootstep);
		
		GameRegistry.registerBlock(petroleumGeneratorBlock,"blockPetroleumGenerator");
		MinecraftForge.setBlockHarvestLevel(petroleumGeneratorBlock, "pickaxe", 0);
		LanguageRegistry.addName(petroleumGeneratorBlock, "Petroleum Generator");
		ItemStack aStack = ic2.api.Items.getItem("generator");
		ItemStack bStack = new ItemStack(Block.pistonBase);
		ItemStack cStack = new ItemStack(Item.flintAndSteel);
		ItemStack dStack = ic2.api.Items.getItem("waterCell");
		
		GameRegistry.addRecipe(new ItemStack(petroleumGeneratorBlock)," G ","CPC","CFC",
				'G',aStack,'P',bStack,'F',cStack,'C',dStack);
		
		GameRegistry.registerTileEntity(TileEntityPetroleumGenerator.class, "TileEntityPetroleumGenerator");
		
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketOil)),oilStep*oilMultiplier,oilPower,0);
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketFuel)),fuelStep*fuelMultiplier,fuelPower,1);
		
		if (Loader.isModLoaded("TC")) {
			log.info("TrainCraft found! Bituminising all the things.");
			
			//add sludge
			bituminousSludgeItem = new ItemBituminousProduct(bituminousSludgeId);
			bituminousSludgeItem.setMaxStackSize(64);
			bituminousSludgeItem.setCreativeTab(CreativeTabs.tabMisc);
			bituminousSludgeItem.setIconIndex(0);
			bituminousSludgeItem.setItemName("bituminousSludge");
			LanguageRegistry.addName(bituminousSludgeItem,"Bituminous Sludge");
			
			//add sludge bucket
			bituminousSludgeBucketItem = new ItemBituminousProduct(bituminousSludgeBucketId);
			bituminousSludgeBucketItem.setMaxStackSize(1);
			bituminousSludgeBucketItem.setCreativeTab(CreativeTabs.tabMisc);
			bituminousSludgeBucketItem.setIconIndex(1);
			bituminousSludgeBucketItem.setItemName("bituminousSludgeBucket");
			//bituminousSludgeBucketItem.setContainerItem(Item.bucketEmpty);
			LanguageRegistry.addName(bituminousSludgeBucketItem,"Bituminous Sludge Bucket");
			
			//craft sludge into bucket
			ItemStack sludgeBucketItemStack = new ItemStack(bituminousSludgeBucketItem);
			ItemStack sludgeItemStack = new ItemStack(bituminousSludgeItem);
			GameRegistry.addShapelessRecipe(sludgeBucketItemStack,
					new ItemStack(Item.bucketEmpty),sludgeItemStack,sludgeItemStack,
					sludgeItemStack,sludgeItemStack,sludgeItemStack);
			
			GameRegistry.registerFuelHandler(new FuelHandlerBituminousProduct(bituminousBurnTime));
			
			//GameRegistry.addShapelessRecipe(new ItemStack(bituminousSludgeItem,5), sludgeBucketItemStack);
			
			//IC2 PIPELINE
			Ic2Recipes.addExtractorRecipe(sludgeBucketItemStack, new ItemStack(BuildCraftEnergy.bucketOil));
			
			List<ItemStack> oilsandsOre = OreDictionary.getOres("oilsandsOre");
			log.info("Found " + oilsandsOre.size() + " oilsandsOre to process.");
			
			List<ItemStack> petroleumOre = OreDictionary.getOres("petroleumOre");
			log.info("Found " + petroleumOre.size() + " petroleumOre to process.");
			
			//second macerator recipes below to deal with non-standard ore generation
			for (ItemStack ore : oilsandsOre) {
				Ic2Recipes.addMaceratorRecipe(
						new ItemStack(ore.getItem(),2), 
						sludgeItemStack);
				Ic2Recipes.addMaceratorRecipe(
						new ItemStack(ore.getItem(),2,ore.itemID), 
						sludgeItemStack);
			}
			
			for (ItemStack ore : petroleumOre) {
				Ic2Recipes.addMaceratorRecipe(
						new ItemStack(ore.getItem(),1), 
						sludgeItemStack);
				Ic2Recipes.addMaceratorRecipe(
						new ItemStack(ore.getItem(),1,ore.itemID), 
						sludgeItemStack);
			}
			
		} else {
			log.info("TrainCraft NOT found.. do nothing.");
		}
		
		//log.info(FuelPetroleumGenerator.getFuelByItemId(4064).getEuPerLiquidUnit()+" in this many ticks: "+FuelPetroleumGenerator.getFuelByItemId(4064).getTicksForLiquidUnit());
				
		proxy.registerRenderThings();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		
		
	}

	
}