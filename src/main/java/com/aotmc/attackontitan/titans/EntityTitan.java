package com.aotmc.attackontitan.titans;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

public class EntityTitan extends EntityGiantZombie {

    public EntityTitan(org.bukkit.World world) {
        super(EntityTypes.GIANT, ((CraftWorld)world).getHandle());
        this.G = 4.0F;

        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.32);
    }

    protected void initPathfinder() {
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 0.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 0.5D));
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 0.5D, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
    }

    public void tick() {
        super.tick();
    }
}
