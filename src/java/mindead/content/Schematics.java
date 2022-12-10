package mindead.content;

import mindustry.game.Schematic;
import mindustry.game.Team;
import useful.ShortSchematics;

import static mindustry.Vars.*;

public class Schematics {

    public static Schematic engine;

    public static void load() {
        engine = load(0, -1, "BCCtAAbiaAbbwAabIaabBдABb");
    }

    public static Schematic load(int x, int y, String base) {
        Schematic schematic = ShortSchematics.read("#A" + base);
        schematic.tiles.each(st -> {
            st.x += x;
            st.y += y;
        });
        return schematic;
    }

    public static void at(Schematic schematic, int x, int y, Team team) {
        schematic.tiles.each(st -> world.tile(st.x + x, st.y + y).setBlock(st.block, team, st.rotation));
    }
}
