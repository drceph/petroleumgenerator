package drceph.petrogen.common;

import net.minecraft.src.Block;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
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
	
	//TODO add to configuration file
	public static int fuelEnergy = 96000; //EU output per bucket
	public static int oilEnergy = 9600; //EU output per bucket
	public static int fuelOutput = 16; //EU per tick
	public static int oilOutput = 8; //EU per tick
	
	public static Block petroleumGeneratorBlock;
	
	@Init
	public void load(FMLInitializationEvent event) {
		
		petroleumGeneratorBlock = new BlockPetroleumGenerator(700,0).setBlockName("petroleumGeneratorBlock");
		petroleumGeneratorBlock.setLightValue(0.8f);
		petroleumGeneratorBlock.setHardness(1.5f);
		
		GameRegistry.registerBlock(petroleumGeneratorBlock);
		MinecraftForge.setBlockHarvestLevel(petroleumGeneratorBlock, "pickaxe", 0);
		LanguageRegistry.addName(petroleumGeneratorBlock, "Petroleum Generator");
		
		GameRegistry.registerTileEntity(TileEntityPetroleumGenerator.class, "TileEntityPetroleumGenerator");
		
		proxy.registerRenderThings();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		
		
	}
	
}