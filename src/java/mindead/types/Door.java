package mindead.types;

import arc.func.Cons2;
import arc.graphics.Color;
import arc.math.Mathf;
import mindead.Generator;
import mindead.content.Schematics;
import mindustry.content.Fx;
import mindustry.game.Schematic;
import mindustry.gen.Call;
import mindustry.world.Tile;

import static mindustry.Vars.*;

public class Door {

    public int x, y, length;
    public boolean vertical;

    public int progress;
    public boolean opening;

    public Door(Tile start, Tile end) {
        this.x = start.x < end.x ? start.x : end.x;
        this.y = start.y < end.y ? start.y : end.y;

        this.vertical = start.x == end.x;
        this.length = Math.abs(vertical ? start.y - end.y : start.x - end.x);

        generate((schema, y) -> Schematics.terrainAt(schema, globalX(0, y), globalY(0, y), vertical));
    }

    public void generate(Cons2<Schematic, Integer> cons) {
        for (int i = 4; i < length - 2; i += 4)
            cons.get(Schematics.doorFragment, i);

        cons.get(Schematics.doorStart, 0);
        cons.get(Schematics.doorEnd, length);
    }

    public void update() { // TODO win for survivors and lose for murderers
        if (!opening || progress == length - 3 || ++progress % 2 == 0) return;

        slice((length + progress) / 2);
        slice((length - progress) / 2);
    }

    public void slice(int localY) {
        for (int x = -2; x <= 2; x++) {
            var tile = globalTile(x, localY);
            if (tile.build != null) tile.build.kill();

            if (Mathf.chance(.8f)) Call.effect(Fx.smokeCloud, tile.drawx(), tile.drawy(), 0f, Color.white);
        }
    }

    public void open() {
        opening = true;
        Generator.playCutscene();
    }

    // region getters

    public int globalX(int localX, int localY) {
        return this.x + (vertical ? localX : localY);
    }

    public int globalY(int localX, int localY) {
        return this.y + (vertical ? localY : localX);
    }

    public Tile globalTile(int localX, int localY) {
        return world.tile(globalX(localX, localY), globalY(localX, localY));
    }

    public int centerX() {
        return vertical ? this.x : this.x + length / 2;
    }

    public int centerY() {
        return vertical ? this.y + length / 2 : this.y;
    }

    // endregion
}
