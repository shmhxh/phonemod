package com.example.phone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PhoneMod implements ModInitializer {
    public static final String MOD_ID = "phonemod";
    public static final ItemGroup PHONE_GROUP = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "phone_group"),
        () -> new ItemStack(ModItems.PHONE));

    @Override
    public void onInitialize() {
        ModItems.register();
    }
}