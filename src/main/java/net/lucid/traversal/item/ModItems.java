package net.lucid.traversal.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.lucid.traversal.Traversal;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    //Items
    public static final Item SHATTERED_SOUL = registerItem("shattered_soul", new Item(new Item.Settings()));

    public static final Item BUCKET_OF_NETHER_OIL = registerItem("bucket_of_nether_oil", new Item(new Item.Settings().maxCount(1)));

    public static final Item DRIED_KELP_WRAPPED_FRIED_SOUL = registerItem("dried_kelp_wrapped_fried_soul", new Item(new Item.Settings().food(FoodComponents.DRIED_KELP)
            .food(new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(10.0f)
            .snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1), 1.0F)
            .build())));

    public static final Item KELP_WRAPPED_SOUL = registerItem("kelp_wrapped_soul", new Item(new Item.Settings().food(FoodComponents.DRIED_KELP)
            .food(new FoodComponent.Builder()
                    .nutrition(1)
                    .saturationModifier(0.5f)
                    .snack()
                    .build())));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Traversal.MOD_ID, name), item);
    }

    public static void registerModItems(){
        Traversal.LOGGER.info("Registering Mod items for " + Traversal.MOD_ID);

        /*Item Locations*/
        //Ingredients
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(SHATTERED_SOUL);
            entries.add(KELP_WRAPPED_SOUL);
            entries.add(BUCKET_OF_NETHER_OIL);
        });

        //Food
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(KELP_WRAPPED_SOUL);
            entries.add(DRIED_KELP_WRAPPED_FRIED_SOUL);
        });

    }
}
