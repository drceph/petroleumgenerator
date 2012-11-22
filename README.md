Petroleum Generator
===================

A [buildcraft](http://www.mod-buildcraft.com)  <> [IC2](http://www.industrial-craft.net) crossover mod. 
Tasty on its own; Delicious served with a main course of [Forestry](http://forestry.sengir.net/wiki/).

This mod adds a Petroleum Generator to the game. This generator will produce EU directly from Buildcraft Fuel/Oil, which can be either pumped in as a buildcraft liquid pipes, or manually deposited using containers. Want to burn some Biofuel in this? Touch. DrCeph Industries doesn't believe in the green revolution. It is just a fad that will pass, like hypercolor and scrunchies.

Why another BC/IC conversion mod?
---------------------------------

**Because Challenge maps!**

The focus of this mod is to make conversion of oil/fuel to EU a lossy process and to fix the scaling issues that come with using BC power directly.

Most conversion mods out there use coal as a conversion point between BC and IC2. Whilst I understand this position, once you get to higher tier fuels you have a conversion ratio that gets out of control. For example, Fuel producing about 1.5 million EU using a combustion engine through a MJ to EU conversion block (last tested using MC1.4.2, BC3.1.8, IC1.108 and Sykes' Transformers 1.6). [As SirSengir, of Forestry/Buildcraft points out](http://www.mod-buildcraft.com/forums/topic/inefficient-power-conversion/), using coal to build the ratio ignores the different scaling between the two mods' power systems.

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

Tips and Tricks
---------------

**Tip 1: Don't run your refinery using an electric engine powered by petroleum generator.**  
Everytime you convert you will lose power. Try producing enough fuel with an oil-fired combustion engine to allow you to run a fuel-combustion engine to refine your oil. You will produce fuel far faster than you use it in this way, leaving more than enough spare to produce EU.

License
-------

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

https://github.com/chrisduran/petroleumgenerator/blob/master/LICENCE