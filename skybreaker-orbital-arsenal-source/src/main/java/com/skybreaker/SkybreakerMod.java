package com.skybreaker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skybreaker.item.MeteorBarrageItem;
import com.skybreaker.item.OrbitalRailgunItem;
import com.skybreaker.item.SatelliteLaserItem;

public class SkybreakerMod implements ModInitializer {
    public static final String MOD_ID = "skybreaker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<ItemGroup> SKYBREAKER_GROUP_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(MOD_ID, "group"));

    public static final Item ORBITAL_RAILGUN = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "orbital_railgun"),
            new OrbitalRailgunItem(new Item.Settings().maxCount(1))
    );

    public static final Item SATELLITE_LASER = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "satellite_laser"),
            new SatelliteLaserItem(new Item.Settings().maxCount(1))
    );

    public static final Item METEOR_BARRAGE = Registry.register(
            Registries.ITEM,
            Identifier.of(MOD_ID, "meteor_barrage"),
            new MeteorBarrageItem(new Item.Settings().maxCount(1))
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Skybreaker: Orbital Arsenal");

        Registry.register(Registries.ITEM_GROUP, SKYBREAKER_GROUP_KEY, FabricItemGroup.builder()
                .icon(() -> new ItemStack(ORBITAL_RAILGUN))
                .displayName(Text.translatable("itemGroup.skybreaker.group"))
                .entries((context, entries) -> {
                    entries.add(ORBITAL_RAILGUN);
                    entries.add(SATELLITE_LASER);
                    entries.add(METEOR_BARRAGE);
                })
                .build());
    }
}
