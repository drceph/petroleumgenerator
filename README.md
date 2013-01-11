Petroleum Generator
===================

version 1.2

### [DOWNLOAD THE LATEST VERSION](http://goo.gl/oGrTY)
or [see all downloads](http://goo.gl/jya7H)

A [buildcraft](http://www.mod-buildcraft.com)  <> [IC2](http://www.industrial-craft.net) crossover mod. 
Tasty on its own; Delicious served with a main course of [Forestry](http://forestry.sengir.net/wiki/).

This mod adds a Petroleum Generator to the game. This generator will produce EU directly from Buildcraft Fuel/Oil, which can be either pumped in using buildcraft liquid pipes, or manually deposited using containers. Want to burn some Biofuel in this? Tough. DrCeph Industries doesn't believe in the green revolution. It is just a fad that will pass, like hypercolor and scrunchies.

Also recently added was an IC2-based processing chain to turn raw materials from TrainCraft (Oily Sands and Crude Oil ore) into Buildcraft oil. This chain is only enabled if TrainCraft is installed.

Why another BC/IC conversion mod?
---------------------------------

**Because Challenge maps!**

The focus of this mod is to make conversion of oil/fuel to EU a lossy process and to fix the scaling issues that come with using BC power directly.

Most conversion mods out there use coal as a conversion point between BC and IC2. Whilst I understand this position, once you get to higher tier fuels you have a conversion ratio that gets out of control. For example, Fuel producing about 1.5 million EU using a combustion engine through a MJ to EU conversion block (last tested using MC1.4.2, BC3.1.8, IC1.108 and Transformers 1.6). [As SirSengir, of Forestry/Buildcraft points out](http://www.mod-buildcraft.com/forums/topic/inefficient-power-conversion/), using coal to build the ratio ignores the different scaling between the two mods' power systems.

Enter Forestry: Forestry is great. It has an EU generator for Biofuels and a MJ engine that runs on EU. What it misses out however is EU generation using Buildcraft's oil resource. When combined with the Forestry mod this covers all aspects of the buildcraft power/fuel to EU conversions, with delicious inefficiencies. 

How much EU will I get for oil and fuel?
----------------------------------------

I've modelled this generator on the EU conversion rates on the Bio Generator in the Forestry mod. 

A bucket of oil will produce 30,000 EU at 10EU/t  
A bucket of fuel will produce 300,000 EU at 25EU/t  

Note 1: These values were derived by looking at the MJ output of oil compared to biomass, and of fuel compared to biofuel. Then a boost due to oil being a non-renewable energy source.
Note 2: They have been tweaked slightly to make everything work nicely in integer space.  
Note 3: Notice that the power ratio between fuel and oil is 10:1, conserving the BC power differential.

By comparison, the Biogenerator currently produces (as of Forestry 1.6):  

8,000 EU @ 8EU/t for Biomass  
32,000 EU @ 16EU/t for Biofuel  


Crafting Recipe
---------------

### Petroleum Generator

The crafting recipe is as follows:

x A x  
D B D  
D C D  
  
Where:  
x = blank spot  
A = Generator  
B = Flint and Steel
C = Piston
D = Water Cell

In picture form:  
![xAx DBD DCD](https://raw.github.com/chrisduran/petroleumgenerator/6d5de2e67dcce4b41a191752f05ee6220276fa05/art/screenshots/crafting.png)

### Traincraft oily sands and ore processing chain

If Traincraft is installed PetroGen will enable a processing chain to convert the Oil Sands (oily sands) and Crude Oil (oily ore) from Traincraft into Petroleum Generator compatible BC oil. The conversion rate is 10x sands or 5x ore, plus work, giving one bucket of oil.

Step 1: Macerate ores into Bituminous Sludge (2x sands for 1 sludge, 1x ore for 1 sludge)   
![Ore to Sludge](https://raw.github.com/chrisduran/petroleumgenerator/523f1a6e3264c2066928331feabc5f3b1e43caab/art/screenshots/TC_ores_to_sludge.png)

Step 2: Craft 5x Bituminous Sludge and 1x Empty Bucket to get 1x Bituminous Sludge Bucket (shapeless recipe, non-reversable)   
![Sludge to Sludge Bucket](https://raw.github.com/chrisduran/petroleumgenerator/523f1a6e3264c2066928331feabc5f3b1e43caab/art/screenshots/Sludge_to_sludge_bucket.png)

Step 3: Extract 1x Bituminous Sludge Bucket into 1x Oil Bucket.   
![Sludge Bucket to Oil Bucket](https://raw.github.com/chrisduran/petroleumgenerator/523f1a6e3264c2066928331feabc5f3b1e43caab/art/screenshots/Sludge_bucket_to_oil.png)

Screenshots
-----------

Screenshots can be found in the github repository: [https://github.com/chrisduran/petroleumgenerator/tree/6d5de2e67dcce4b41a191752f05ee6220276fa05/art/screenshots](https://github.com/chrisduran/petroleumgenerator/tree/6d5de2e67dcce4b41a191752f05ee6220276fa05/art/screenshots)

TODO
----

This isn't a completed product. Although the base functionality is there, DrCeph Industries always has more modules to pack in. A list of what is next in line:

[See the isues list for the 'Version 1.0 Release' milestone](https://github.com/chrisduran/petroleumgenerator/issues?direction=asc&milestone=1&page=1&sort=created&state=open)

Requirements
------------

Requires IndustrialCraft2 v1.109 or greater and BuildCraft 3.2.0 or greater. Which means it also requires the recommended Forge version for those addons and Minecraft 1.4.5.

Installation
------------

Follow the installation instructions for Forge, IndustrialCraft2 and BuildCraft. Once these are installed successfully, place the PetroGen mod file in the same 'mods' directory. Shake then stir.

If you prefer @mjramonru's texture, replace the blocks.png file in drceph/petrogen/sprites with the blocksAlt.png file (i.e. rename blocksAlt.png to blocks.png). **Yell at me if you want it to be the default texture. It is a pretty cool texture, check it out.** 

License
-------

https://github.com/chrisduran/petroleumgenerator/blob/master/LICENCE

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

@mjramonru retains all rights and ownership of his texture file, blocksAlt.png - I am simply hosting the file in the modpack for the convenience of end users.

Changelog
---------

1.2:
* Block front panel now faces player on placement
* Cross-mod compatability - now able to convert TrainCraft ores into Buildcraft oil.

1.1:
* Updated to MC 1.4.6, IC v1.112 and BuildCraft 3.3.0

1.0:
* Full release! Updated to IC v1.110 and BuildCraft 3.2.2. 

0.14:
* Updated default EU/bucket for fuel (again) to 300k. This is after some discussions on the IC2 and FTB forums with people who are better at balance than I.
* Also increased the output to 25 EU/t for fuel so that you don't need to shave after every bucket!
* Block now updates texture on activity - you can now tell at a glance that you're burning precious fuel!
* Also made the recipe in line with the costs of the Geothermal generator, and TE magma engines. Lava is fuel's closest 'competitor', so lessening the gap makes fuel less unattractive.

0.10:
* Updated default EU/bucket for oil and fuel to 30k and 200k respectively. Due to relative scarcity of resource compared to other sources.
* Removed electronic circuit from recipe after some analysis of the relative costs of geothermal and biogenerators.
* Powering the generator by redstone will block emitting of EU packets. Tank and buffer will still fill, however.
* Updated alternative texture by @mjramonru.


0.7:
* Added configurable options. Fuel/oil now configurable in steps of 20k/10k EU respectively. Block ID also configurable (default is 3143).
* Added alternative texture, graciously created by @mjramonru. 

0.5:
* Added support for stackable containers of fuel and oil, notably Forestry capsules and cans.

0.4:
* Initial release.

Credits
-------

@mjramonru for the awesome texture.

Resources
---------
In the interest of full disclosure, here are other places this mod has a presence:

IndustrialCraft2 forums (required dependancy) [http://forum.industrial-craft.net/index.php?page=Thread&threadID=8385](http://forum.industrial-craft.net/index.php?page=Thread&threadID=8385)  
FTB (Modpack) [http://forum.feed-the-beast.com/threads/…sover-mod.2246/](http://forum.feed-the-beast.com/threads/…sover-mod.2246/)  
