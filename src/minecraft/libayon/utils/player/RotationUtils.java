package libayon.utils.player;


import com.google.common.base.Predicates;
import libayon.module.modules.combat.KillAura;
import libayon.module.modules.world.Scaffold.BlockCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.*;

import java.util.List;


public class RotationUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    /*
     * Sets the player's head rotations to the given yaw and pitch (visual-only).
     */
    public static void setRotations(float yaw, float pitch) {
        mc.thePlayer.rotationPitchHead = pitch;
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationYawHead = yaw;
    }

    public static void setRotationsFloat(float[] rotations) {
        float yaw = rotations[0];
        float pitch = rotations[1];
        mc.thePlayer.rotationPitchHead = pitch;
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationYawHead = yaw;
    }

    public static boolean isFaced(Entity targetEntity, double blockReachDistance) {
        return raycastEntity(blockReachDistance, KillAura.yaw, KillAura.pitch, entity -> entity == targetEntity) != null;
    }

    public static Entity raycastEntity(final double range, final IEntityFilter entityFilter) {
        return raycastEntity(range, KillAura.yaw, KillAura.pitch,
                entityFilter);
    }

    private static Entity raycastEntity(double range, float yaw, float pitch, IEntityFilter entityFilter) {
        final Entity renderViewEntity = mc.getRenderViewEntity();

        if (renderViewEntity != null && mc.theWorld != null) {
            double blockReachDistance = range;
            final Vec3 eyePosition = renderViewEntity.getPositionEyes(1F);
            final float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            final float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            final float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
            final float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

            final Vec3 entityLook = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            final Vec3 vector = eyePosition.addVector(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance);
            final List<Entity> entityList = mc.theWorld.getEntitiesInAABBexcluding(renderViewEntity, renderViewEntity.getEntityBoundingBox().addCoord(entityLook.xCoord * blockReachDistance, entityLook.yCoord * blockReachDistance, entityLook.zCoord * blockReachDistance).expand(1D, 1D, 1D), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));

            Entity pointedEntity = null;

            for (final Entity entity : entityList) {
                if (!entityFilter.canRaycast(entity))
                    continue;

                final float collisionBorderSize = entity.getCollisionBorderSize();
                final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().expand(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                final MovingObjectPosition movingObjectPosition = axisAlignedBB.calculateIntercept(eyePosition, vector);

                if (axisAlignedBB.isVecInside(eyePosition)) {
                    if (blockReachDistance >= 0.0D) {
                        pointedEntity = entity;
                        blockReachDistance = 0.0D;
                    }
                } else if (movingObjectPosition != null) {
                    final double eyeDistance = eyePosition.distanceTo(movingObjectPosition.hitVec);

                    if (eyeDistance < blockReachDistance || blockReachDistance == 0.0D) {
                        if (entity == renderViewEntity.ridingEntity) {
                            if (blockReachDistance == 0.0D)
                                pointedEntity = entity;
                        } else {
                            pointedEntity = entity;
                            blockReachDistance = eyeDistance;
                        }
                    }
                }
            }

            return pointedEntity;
        }

        return null;
    }

    public static float[] getFixedRotation(float[] rotations, float[] lastRotations) {
        Minecraft mc = Minecraft.getMinecraft();
        float yaw = rotations[0];
        float pitch = rotations[1];
        float lastYaw = lastRotations[0];
        float lastPitch = lastRotations[1];
        float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 1.2F;
        float deltaYaw = yaw - lastYaw;
        float deltaPitch = pitch - lastPitch;
        float fixedDeltaYaw = deltaYaw - deltaYaw % gcd;
        float fixedDeltaPitch = deltaPitch - deltaPitch % gcd;
        float fixedYaw = lastYaw + fixedDeltaYaw;
        float fixedPitch = lastPitch + fixedDeltaPitch;
        return new float[]{fixedYaw, fixedPitch};
    }

    public static float[] limitAngleChange(float[] currRot, float[] targetRot, float turnSpeed) {
        float currentYaw = currRot[0];
        float currentPitch = currRot[1];
        float targetYaw = targetRot[0];
        float targetPitch = targetRot[1];
        float yawDifference = getAngleDifference(targetYaw, currentYaw);
        float pitchDifference = getAngleDifference(targetPitch, currentPitch);
        float limitedYaw = currentYaw + ((yawDifference > turnSpeed) ? turnSpeed : Math.max(yawDifference, -turnSpeed));
        float limitedPitch = currentPitch + ((pitchDifference > turnSpeed) ? turnSpeed : Math.max(pitchDifference, -turnSpeed));
        return new float[]{limitedYaw, limitedPitch};
    }

    public static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0F + 540.0F) % 360.0F - 180.0F;
    }

    public static float clampRotation() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYawHead;
        float n = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }

    public static float getSensitivityMultiplier() {
        float SENSITIVITY = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENSITIVITY * SENSITIVITY * SENSITIVITY * 8.0F) * 0.15F;
    }

    public static float smoothRotation(float from, float to, float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return from + f;
    }

    public static float[] getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double) (ent.getEyeHeight() / 2.0f);
        return getRotationFromPosition(x, y, z);
    }

    public static float[] getRotationFromPosition(double x, double y, double z) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new float[]{yaw, pitch};
    }

    /*
     * Sets the player's head rotations to the given yaw and pitch (visual-only).
     */
    public static void setRotations(float[] rotations) {
        setRotations(rotations[0], rotations[1]);
    }

    public static float[] getFacingRotations(BlockCache blockCache) {
        double d1 = blockCache.position.getX() + 0.5D - mc.thePlayer.posX + blockCache.facing.getFrontOffsetX() / 2.0D;
        double d2 = blockCache.position.getZ() + 0.5D - mc.thePlayer.posZ + blockCache.facing.getFrontOffsetZ() / 2.0D;
        double d3 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - (blockCache.position.getY());
        double d4 = MathHelper.sqrt_double(d1 * d1 + d2 * d2);
        float f1 = (float) (Math.atan2(d2, d1) * 180.0D / Math.PI) - 90.0F;
        float f2 = (float) (Math.atan2(d3, d4) * 180.0D / Math.PI);
        if (f1 < 0.0F) {
            f1 += 360.0F;
        }
        return new float[]{f1, f2};
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 1.3 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }

    public static float[] getFacingRotations2(final int paramInt1, final double d, final int paramInt3) {
        final EntitySnowball localEntityPig = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return getRotationsNeeded(localEntityPig);
    }

    public static float getYaw(Vec3 to) {
        float x = (float) (to.xCoord - mc.thePlayer.posX);
        float z = (float) (to.zCoord - mc.thePlayer.posZ);
        float var1 = (float) (StrictMath.atan2(z, x) * 180.0D / StrictMath.PI) - 90.0F;
        float rotationYaw = mc.thePlayer.rotationYaw;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }

    public static Vec3 getVecRotations(float yaw, float pitch) {
        double d = Math.cos(Math.toRadians(-yaw) - Math.PI);
        double d1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
        double d2 = -Math.cos(Math.toRadians(-pitch));
        double d3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3(d1 * d2, d3, d * d2);
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = RotationUtils.mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double) player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public interface IEntityFilter {
        boolean canRaycast(final Entity entity);
    }


}
