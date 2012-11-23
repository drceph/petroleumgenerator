Petroleum Generator
===================

version 0.4

A [buildcraft](http://www.mod-buildcraft.com)  <> [IC2](http://www.industrial-craft.net) crossover mod. 
Tasty on its own; Delicious served with a main course of [Forestry](http://forestry.sengir.net/wiki/).

This mod adds a Petroleum Generator to the game. This generator will produce EU directly from Buildcraft Fuel/Oil, which can be either pumped in using buildcraft liquid pipes, or manually deposited using containers. Want to burn some Biofuel in this? Tough. DrCeph Industries doesn't believe in the green revolution. It is just a fad that will pass, like hypercolor and scrunchies.

Why another BC/IC conversion mod?
---------------------------------

**Because Challenge maps!**

The focus of this mod is to make conversion of oil/fuel to EU a lossy process and to fix the scaling issues that come with using BC power directly.

Most conversion mods out there use coal as a conversion point between BC and IC2. Whilst I understand this position, once you get to higher tier fuels you have a conversion ratio that gets out of control. For example, Fuel producing about 1.5 million EU using a combustion engine through a MJ to EU conversion block (last tested using MC1.4.2, BC3.1.8, IC1.108 and Transformers 1.6). [As SirSengir, of Forestry/Buildcraft points out](http://www.mod-buildcraft.com/forums/topic/inefficient-power-conversion/), using coal to build the ratio ignores the different scaling between the two mods' power systems.

Enter Forestry: Forestry is great. It has an EU generator for Biofuels and a MJ engine that runs on EU. What it misses out however is EU generation using Buildcraft's oil resource. When combined with the Forestry mod this covers all aspects of the buildcraft power/fuel to EU conversions, with delicious inefficiencies. 

How much EU will I get for oil and fuel?
----------------------------------------

I've modelled this generator on the EU conversion rates on the Bio Generator in the Forestry mod. 

A bucket of oil will produce 10,000 EU at 10EU/t  
A bucket of fuel will produce 100,000 EU at 20EU/t  

Note 1: These values were derived by looking at the MJ output of oil compared to biomass, and of fuel compared to biofuel.   
Note 2: They have been tweaked slightly to make everything work nicely in integer space.  
Note 3: Notice that the power ratio between fuel and oil is 10:1, conserving the BC power differential.  

By comparison, the Biogenerator currently produces (as of Forestry 1.6):  

8,000 EU @ 8EU/t for Biomass  
32,000 EU @ 16EU/t for Biofuel  

Note 1: This is a power ratio of 4:1 between Biofuel to Biomass.  
Note 2: This keeps the 80% efficiency of biomass/biofuel to oil/fuel, when normalised for the power ratio.  

Crafting Recipe
---------------
The crafting recipe is as follows:

x A x  
x B x  
C D C  
  
Where:  
x = blank spot  
A = Electronic Circuit  
B = Generator  
C = Water Cell  
D = Combustion Engine  

In picture form:  
![xAx xBx CDC](https://raw.github.com/chrisduran/petroleumgenerator/5eb0aa755571616cefb5d49f271a6a7a0865d304/art/screenshots/crafting.png)

Screenshots
-----------

Screenshots can be found in the github repository: [https://github.com/chrisduran/petroleumgenerator/tree/39c152feb1df45180f090e79639c3bea9a325222/art/screenshots](https://github.com/chrisduran/petroleumgenerator/tree/39c152feb1df45180f090e79639c3bea9a325222/art/screenshots)

TODO
----

This isn't a completed product. Although the base functionality is there, DrCeph Industries always has more modules to pack in. A list of what is next in line:

* The current texture is dire at best, learn to texture/model and/or blackmail somebody else to do it.
* Make engine enable/disable on redstone power.
* Add support for Forestry capsules and cells. Currently takes fuel/oil from liquid pipes and buckets only. 
* Make blockID and fuel-to-EU ratio configurable.
* The elements on the GUI could use an improvement in spacing.

Requirements
------------

Requires IndustrialCraft2 v1.109 or greater and BuildCraft 3.2.0 or greater. Which means it also requires the recommended Forge version for those addons and Minecraft 1.4.5.

Installation
------------

Follow the installation instructions for Forge, IndustrialCraft2 and BuildCraft. Once these are installed successfully, place the PetroGen mod file in the same 'mods' directory. Shake then stir.

License
-------

https://github.com/chrisduran/petroleumgenerator/blob/master/LICENCE

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

Resources
---------
In the interest of full disclosure, here are other places this mod has a presence:

IndustrialCraft2 forums (required dependancy) [http://forum.industrial-craft.net/index.php?page=Thread&threadID=8385](http://forum.industrial-craft.net/index.php?page=Thread&threadID=8385)  
FTB (Modpack) [http://forum.feed-the-beast.com/threads/…sover-mod.2246/](http://forum.feed-the-beast.com/threads/…sover-mod.2246/)  