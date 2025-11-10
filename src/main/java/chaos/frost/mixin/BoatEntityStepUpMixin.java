package chaos.frost.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBoatEntity.class)
public abstract class BoatEntityStepUpMixin extends VehicleEntity implements Leashable {
    public BoatEntityStepUpMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public float getStepHeight() {
        return .51f;
    }
}
