package com.github.alexnijjar.beyond_earth.items.vehicles;

import java.util.List;

import com.github.alexnijjar.beyond_earth.items.FluidContainingItem;
import com.github.alexnijjar.beyond_earth.util.FluidUtils;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public abstract class VehicleItem extends Item implements FluidContainingItem {

	public VehicleItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		long fuel = FluidUtils.dropletsToMillibuckets(this.getAmount(stack));
		tooltip.add(Text.translatable("tooltip.beyond_earth.vehicle_fuel", fuel, FluidUtils.dropletsToMillibuckets(this.getTankSize())).setStyle(Style.EMPTY.withColor(fuel > 0 ? Formatting.GREEN : Formatting.RED)));
	}
}
