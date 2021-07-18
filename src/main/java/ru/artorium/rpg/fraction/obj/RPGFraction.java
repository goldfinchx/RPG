package ru.artorium.rpg.fraction.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RPGFraction {
    VIKINGS("Викинги", new String[]{
            "",
            "§fСеверные племена, разобщённые на кланы.",
            "§fНе имеют главного правителя, зарабатывают",
            "§fграбежами других земель"
    }),
    ANGLO_SAXONS("Англо-саксы", new String[]{
            "",
            "§fНесколько народов, заселивших британские",
            "§fострова, и разделённые на несколько королевств"
    });

    private final String title;
    private final String[] description;

    public static RPGFraction getByString(String title) {
        for (RPGFraction rpgFraction : values())
            if (rpgFraction.name().equalsIgnoreCase(title))
                return rpgFraction;

        return null;
    }

}
