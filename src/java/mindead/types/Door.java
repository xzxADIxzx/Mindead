package mindead.types;

import arc.func.Cons2;
import arc.math.Mathf;
import mindead.content.Schematics;
import mindustry.game.Schematic;
import mindustry.world.Tile;

public class Door {

    public int sx, sy, ex, ey, dx, dy;

    public Door(Tile start, Tile end) {
        this.sx = start.x < end.x ? start.x : end.x;
        this.ex = start.x < end.x ? end.x : start.x;

        this.sy = start.y < end.y ? start.y : end.y;
        this.ey = start.y < end.y ? end.y : start.y;

        this.dx = Mathf.clamp(ex - sy, 0, 1);
        this.dy = Mathf.clamp(ey - sy, 0, 1);

        if (sx == ex) generate(sy, ey, (schema, y) -> Schematics.terrainAt(schema, sx, y, true)); // vertical
        if (sy == ey) generate(sx, ex, (schema, x) -> Schematics.terrainAt(schema, x, sy, false)); // horizontal
    }

    public void generate(int from, int to, Cons2<Schematic, Integer> cons) {
        for (int i = from + 4; i < to - 2; i += 4)
            cons.get(Schematics.doorFragment, i);

        cons.get(Schematics.doorStart, from);
        cons.get(Schematics.doorEnd, to);
    }

    public void update() {} // TODO win for survivors and lose for murderer
}
