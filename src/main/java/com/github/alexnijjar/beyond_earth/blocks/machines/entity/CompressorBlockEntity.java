package com.github.alexnijjar.beyond_earth.blocks.machines.entity;

import org.jetbrains.annotations.Nullable;

import com.github.alexnijjar.beyond_earth.BeyondEarth;
import com.github.alexnijjar.beyond_earth.recipes.CookingRecipe;
import com.github.alexnijjar.beyond_earth.registry.ModBlockEntities;
import com.github.alexnijjar.beyond_earth.registry.ModRecipes;
import com.github.alexnijjar.beyond_earth.screen.handler.CompressorScreenHandler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CompressorBlockEntity extends ProcessingMachineBlockEntity {

    public CompressorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COMPRESSOR, blockPos, blockState);
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CompressorScreenHandler(syncId, inv, this);
    }

    @Override
    public long getMaxGeneration() {
        return BeyondEarth.CONFIG.compressor.maxEnergy;
    }

    @Override
    public long getEnergyPerTick() {
        return BeyondEarth.CONFIG.compressor.energyPerTick;
    }

    @Override
    public long getMaxEnergyInsert() {
        return BeyondEarth.CONFIG.compressor.energyPerTick * 32;
    }

    // Input and output.
    @Override
    public int getInventorySize() {
        return 2;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return slot == 0;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }

    @Override
    public void tick() {
        if (!this.world.isClient) {
            if (this.hasEnergy()) {
                ItemStack input = this.getStack(0);
                if (!input.isEmpty() && (input.getItem().equals(this.inputItem) || this.inputItem == null)) {
                    this.setActive(true);
                    if (this.cookTime < this.cookTimeTotal) {
                        this.cookTime++;
                        this.drainEnergy();

                    } else if (this.outputStack != null) {
                        input.decrement(1);
                        this.finishCooking();

                    } else {
                        CookingRecipe recipe = this.createRecipe(ModRecipes.COMPRESSING_RECIPE, input, true);
                        if (recipe != null) {
                            this.cookTimeTotal = recipe.getCookTime();
                            this.cookTime = 0;
                        }
                    }
                } else if (this.outputStack != null) {
                    this.stopCooking();
                } else {
                    this.setActive(false);
                }
            }
        }
    }
}