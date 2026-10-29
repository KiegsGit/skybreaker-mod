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

public class MeteorBarrageItem extends Item {
    public MeteorBarrageItem(Settings settings) {
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
            user.sendMessage(Text.literal("§4[Skybreaker] Summoning Meteor Barrage at " + pos.toShortString() + "!"), true);
            world.playSound(null, pos, SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 1.0f, 0.4f);

            ServerWorld serverWorld = (ServerWorld) world;
            // Spawn multiple impact explosions in a radius
            for (int x = -2; x <= 2; x += 2) {
                for (int z = -2; z <= 2; z += 2) {
                    serverWorld.createExplosion(null, pos.getX() + x, pos.getY(), pos.getZ() + z, 4.0f, World.ExplosionSourceType.TNT);
                }
            }

            itemStack.damage(1, user, PlayerEntity.getSlotForHand(hand));
            user.getItemCooldownManager().set(this, 200);
            return TypedActionResult.success(itemStack);
        } else {
            user.sendMessage(Text.literal("§c[Skybreaker] Invalid impact zone."), true);
            return TypedActionResult.pass(itemStack);
        }
    }
}
