package com.skybreaker.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class OrbitalRailgunItem extends Item {
    public OrbitalRailgunItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world.isClient()) {
            return TypedActionResult.success(itemStack);
        }

        // Raycast up to 128 blocks
        Vec3d eyePos = user.getEyePos();
        Vec3d rotationVec = user.getRotationVector();
        Vec3d targetVec = eyePos.add(rotationVec.x * 128.0, rotationVec.y * 128.0, rotationVec.z * 128.0);

        BlockHitResult hitResult = world.raycast(new RaycastContext(
                eyePos, targetVec,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                user
        ));

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            user.sendMessage(Text.literal("§b[Skybreaker] Orbital Railgun targeted at " + pos.toShortString() + ". Initiating strike sequence..."), true);
            world.playSound(null, pos, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0f, 0.5f);

            // Schedule orbital strike after countdown (simulated server task or instant for reliability)
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos strikePos = pos.up();

            // Create explosion & shockwave
            serverWorld.createExplosion(null, strikePos.getX(), strikePos.getY(), strikePos.getZ(), 6.0f, World.ExplosionSourceType.TNT);
            serverWorld.playSound(null, strikePos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.WEATHER, 3.0f, 0.8f);

            itemStack.damage(1, user, PlayerEntity.getSlotForHand(hand));
            user.getItemCooldownManager().set(this, 100); // 5 second cooldown
            return TypedActionResult.success(itemStack);
        } else {
            user.sendMessage(Text.literal("§c[Skybreaker] Invalid target location."), true);
            return TypedActionResult.pass(itemStack);
        }
    }
}
