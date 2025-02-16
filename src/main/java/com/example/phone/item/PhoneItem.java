package com.example.phone.item;

import com.example.phone.client.PhoneScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class PhoneItem extends Item {
    private static final String ENERGY_KEY = "Energy";
    private static final int MAX_ENERGY = 100;

    public PhoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (getEnergy(stack) <= 0) {
            user.sendMessage(Text.literal("§c电量不足，请充电！"));
            return TypedActionResult.fail(stack);
        }
        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new PhoneScreen(stack));
        }
        return TypedActionResult.success(stack);
    }

    public static int getEnergy(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.contains(ENERGY_KEY)) {
            nbt.putInt(ENERGY_KEY, MAX_ENERGY);
        }
        return nbt.getInt(ENERGY_KEY);
    }

    public static void setEnergy(ItemStack stack, int energy) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(ENERGY_KEY, Math.min(energy, MAX_ENERGY));
    }

    public static void consumeEnergy(ItemStack stack, int amount) {
        setEnergy(stack, getEnergy(stack) - amount);
    }
}