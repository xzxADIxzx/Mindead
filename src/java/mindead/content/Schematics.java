package mindead.content;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Prop;
import useful.ShortSchematics;

import static mindustry.Vars.*;

public class Schematics {

    public static Schematic engine, doorStart, doorFragment, doorEnd;

    public static void load() {
        // region schematics

        engine = load(0, -1, "BCCtAAbiaAbbwAabIaabBдABb");

        doorStart = load(-2, -2, "ccBiaABkaABiBABkBABibABkbABiAaBkAaBiaaBkaaBiBaBkBaBibaCtbaBiCaBkCaBiABBkABBiaBBkaBBiBBCtBBBibBBkbBBiCBBkCBBiAbBkAbBiabCtabBiBbBkBbBibbBkbbBiCbBkCbBiACBkACBiCCBkCC");
        doorFragment = load(-2, -2, "cCBeAA^BBHbABeCABHAaBeaa^aBHba^aBHAB^aBeBB^aBHCBBeAbBHabBeBb^B");
        doorEnd = load(-2, -2, "ccBiAABkAABiCABkCABiAaBkAaBiaaBkaaBiBaBkBaBibaCtbaBiCaBkCaBiABBkABBiaBBkaBBiBBCtBBBibBBkbBBiCBBkCBBiAbBkAbBiabCtabBiBbBkBbBibbBkbbBiCbBkCbBiaCBkaCBiBCBkBCBibCBkbC");

        // endregion
        // region config

        doorStart.tiles.each(st -> st.block == Blocks.illuminator, st -> st.config = Color.scarlet.rgba());
        doorEnd.tiles.each(st -> st.block == Blocks.illuminator, st -> st.config = Color.scarlet.rgba());

        // endregion
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

    public static void terrainAt(Schematic schematic, int x, int y) {
        schematic.tiles.each(st -> {
            Tile tile = world.tile(st.x + x, st.y + y);

            if (st.block.isFloor() && !st.block.isOverlay())
                tile.setFloorUnder(st.block.asFloor());
            else if (st.block instanceof Prop)
                tile.setBlock(st.block);
            else if (st.block.isOverlay())
                tile.setOverlay(st.block);
            else
                tile.setBlock(st.block, Team.malis);

            if (st.config != null) tile.build.configure(st.config);
        });
    }

    public static void terrainAt(Schematic schematic, int x, int y, boolean vertical) {
        if (vertical)
            terrainAt(schematic, x, y);
        else {
            flip(schematic);
            terrainAt(schematic, x, y);
            flip(schematic);
        }
    }

    private static void flip(Schematic schematic) {
        schematic.tiles.each(st -> {
            short temp = st.x;
            st.x = st.y;
            st.y = temp;
        });
    }
}
