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

@Mod(modid = "drceph.petrogen", name = "Petroleum Generator", version = "0.2")
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
	
	public static Block petroleumGeneratorBlock;
	
	@PreInit
	public void load(FMLPreInitializationEvent event) {
		log.setParent(FMLLog.getLogger());
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
		
		petroleumGeneratorBlock = new BlockPetroleumGenerator(700,0).setBlockName("petroleumGeneratorBlock");
		petroleumGeneratorBlock.setLightValue(0.8f);
		petroleumGeneratorBlock.setHardness(1.5f);
		
		GameRegistry.registerBlock(petroleumGeneratorBlock);
		MinecraftForge.setBlockHarvestLevel(petroleumGeneratorBlock, "pickaxe", 0);
		LanguageRegistry.addName(petroleumGeneratorBlock, "Petroleum Generator");
		
		GameRegistry.registerTileEntity(TileEntityPetroleumGenerator.class, "TileEntityPetroleumGenerator");
		
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketOil)),oilEnergy,oilOutput,0);
		new FuelPetroleumGenerator(LiquidContainerRegistry.getLiquidForFilledItem(new ItemStack(BuildCraftEnergy.bucketFuel)),fuelEnergy,fuelOutput,1);
		
		//log.info(FuelPetroleumGenerator.getFuelByItemId(4064).getEuPerLiquidUnit()+" in this many ticks: "+FuelPetroleumGenerator.getFuelByItemId(4064).getTicksForLiquidUnit());
				
		proxy.registerRenderThings();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		
		
	}
	
}