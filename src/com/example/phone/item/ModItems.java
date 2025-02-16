package com.example.phone.item;

import com.example.phone.PhoneMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final PhoneItem PHONE = new PhoneItem(new Item.Settings().maxCount(1));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(PhoneMod.MOD_ID, "mobile_phone"), PHONE);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(PHONE));
    }
}