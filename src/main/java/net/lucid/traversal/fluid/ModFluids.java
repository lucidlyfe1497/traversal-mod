package net.lucid.traversal.fluid;

import com.google.common.collect.UnmodifiableIterator;
import net.lucid.traversal.fluid.custom.NetherOilFluid;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModFluids {
    public static final Fluid FLOWING_NETHER_OIL = (FlowableFluid)register("flowing_nether_oil", new NetherOilFluid());
    public static final Fluid NETHER_OIL = (FlowableFluid)register("nether_oil", new NetherOilFluid());

    private static <T extends Fluid> T register(String id, T value) {
        return (T)(Registry.register(Registries.FLUID, id, value));
    }

    static {
        for(Fluid fluid : Registries.FLUID) {
            UnmodifiableIterator var2 = fluid.getStateManager().getStates().iterator();

            while(var2.hasNext()) {
                FluidState fluidState = (FluidState)var2.next();
                Fluid.STATE_IDS.add(fluidState);
            }
        }

    }
}

