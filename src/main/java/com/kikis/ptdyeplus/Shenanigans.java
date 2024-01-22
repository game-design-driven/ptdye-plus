package com.kikis.ptdyeplus;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.AnvilMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public final class Shenanigans {
    /**
     * A {@link Runnable} that's allowed to throw
     */
    private interface Deviltry { void run() throws Exception; }

    /**
     * A reflected reference of {@link Field#modifiers}, an int storing {@link Modifier} bit flags, such as {@link Modifier#FINAL} which is what we use here.
     */
    @SuppressWarnings("JavadocReference")
    private static final Field modifiersField;


    /**
     * Two collections of {@link Deviltry}, one of which requires {@link this#modifiersField} to have been set, and one of which doesn't.
     * This way we skip the former if we know they can't succeed.
     */
    private static final Tuple<List<Deviltry>,List<Deviltry>> buffoonery = new Tuple<>(ImmutableList.of(
            // requires modifiersField
            () -> setFinalField(null, AnvilMenu.class.getDeclaredField("COST_RENAME"), 0, true)
    ), ImmutableList.of(
            // doesn't require modifiersField
    ));

    /**
     * The combined values of {@link this#buffoonery} such that it contains all items in either list.
     */
    private static final List<Deviltry> chicanery = new ImmutableList.Builder<Deviltry>().addAll(buffoonery.getA()).addAll(buffoonery.getB()).build();

    // Prepare for Mischief
    static {
        Field modifiers = null;
        try {
            // Get the field at Field#modifiers, allowing it to be accessed, and set this#modifiersField to the reference.
            modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
        } catch (Exception e) {
            PtdyePlus.LOGGER.error("Failed to prepare for mischief!");
        } finally {
            modifiersField = modifiers;
        }
    }

    /**
     * A sort of init method, which will run all applicable {@link Deviltry} in {@link this#buffoonery}. It runs these one at a time in such a manner that one failing does not impact the rest.
     */
    public static void commenceTomfoolery() {
        var waggery = modifiersField == null ? buffoonery.getB() : chicanery;
        for (int i = 0; i < waggery.size(); i++) {
            var hijink = waggery.get(i);
            try {
                hijink.run();
            } catch (Exception e) {
                PtdyePlus.LOGGER.error("Hijink {} failed!", i);
            }
        }
    }

    /**
     * Use literal witchcraft to set the value of a final field.
     * @param instance The object that you wish to set the field on, or null for a static field.
     * @param field The reflected field that you wish to set.
     * @param value The new value to set the field to.
     * @param restore If the field should be set back to final on completion.
     * @return Success or failure
     */
    private static boolean setFinalField(Object instance, Field field, Object value, boolean restore) {
        Preconditions.checkNotNull(modifiersField);
        try {
            field.setAccessible(true);
            var cast = field.getType().cast(value);
            if (!getFinal(field)) {
                field.set(instance, cast);
                return true;
            }
            setFinal(field, false);
            field.set(instance, cast);
            if (restore) setFinal(field, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sets whether a field is final
     * @param field The field to set
     * @param state Whether final should be set
     * @throws IllegalAccessException If the modifier cannot be changed.
     */
    private static void setFinal(Field field, boolean state) throws IllegalAccessException {
        var mod = field.getModifiers();
        if (state) mod = mod | Modifier.FINAL; else mod = mod & ~Modifier.FINAL;
        modifiersField.set(field, mod);
    }

    /**
     * Gets whether a field is final
     * @param field The field to set
     * @return Whether the field is final
     */
    private static boolean getFinal(Field field) { return (field.getModifiers() & Modifier.FINAL) != 0; }
}
