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

public class SatelliteLaserItem extends Item {
    public SatelliteLaserItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (world.isClient()) {
            return TypedActionResult.success(itemStack);
        }

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
            user.sendMessage(Text.literal("§6[Skybreaker] Satellite Laser painting target at " + pos.toShortString() + "..."), true);
            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, 1.0f, 1.2f);

            ServerWorld serverWorld = (ServerWorld) world;
            // Beam cascade effect
            for (int i = 0; i < 5; i++) {
                serverWorld.createExplosion(null, pos.getX(), pos.getY() + i, pos.getZ(), 3.0f, World.ExplosionSourceType.TNT);
            }

            itemStack.damage(1, user, PlayerEntity.getSlotForHand(hand));
            user.getItemCooldownManager().set(this, 140);
            return TypedActionResult.success(itemStack);
        } else {
            user.sendMessage(Text.literal("§c[Skybreaker] Target out of range."), true);
            return TypedActionResult.pass(itemStack);
        }
    }
}
