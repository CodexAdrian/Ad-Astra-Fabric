package com.github.alexnijjar.beyond_earth.items.armour;

import java.util.stream.StreamSupport;

import com.github.alexnijjar.beyond_earth.BeyondEarth;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;

public class NetheriteSpaceSuit extends SpaceSuit {

    public NetheriteSpaceSuit(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public long getTankSize() {
        return BeyondEarth.CONFIG.spaceSuit.netheriteSpaceSuitTankSize;
    }

    public static boolean hasFullSet(LivingEntity entity) {
        return StreamSupport.stream(entity.getArmorItems().spliterator(), false).allMatch(s -> s.getItem() instanceof NetheriteSpaceSuit);
    }
}