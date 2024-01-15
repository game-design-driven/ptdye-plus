package com.kikis.ptdyeplus;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.AnvilMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public final class Shenanigans {
    private interface Deviltry { void run() throws Exception; }

    private static final Field modifiersField;



    private static final Tuple<List<Deviltry>,List<Deviltry>> buffoonery = new Tuple<>(ImmutableList.of(
            // requires modifiersField
            () -> setFinalField(null, AnvilMenu.class.getDeclaredField("COST_RENAME"), 0, true)
    ), ImmutableList.of(
            // doesn't require modifiersField
    ));

    private static final List<Deviltry> chicanery = new ImmutableList.Builder<Deviltry>().addAll(buffoonery.getA()).addAll(buffoonery.getB()).build();

    // Prepare for Mischief
    static {
        Field modifiers = null;
        try {
            modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
        } catch (Exception e) {
            PtdyePlus.LOGGER.error("Failed to prepare for mischief!");
        } finally {
            modifiersField = modifiers;
        }
    }

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

    private static void setFinal(Field field, boolean state) throws IllegalAccessException {
        var mod = field.getModifiers();
        if (state) mod = mod | Modifier.FINAL; else mod = mod & ~Modifier.FINAL;
        modifiersField.set(field, mod);
    }

    private static boolean getFinal(Field field) { return (field.getModifiers() & Modifier.FINAL) != 0; }
}
