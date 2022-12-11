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

    public int sx, sy, ex, ey, cx, cy, dx, dy;
    public int length;

    public int progress;
    public boolean opening;

    public Door(Tile start, Tile end) {
        this.sx = start.x < end.x ? start.x : end.x;
        this.ex = start.x < end.x ? end.x : start.x;

        this.sy = start.y < end.y ? start.y : end.y;
        this.ey = start.y < end.y ? end.y : start.y;

        this.cx = (sx + ex) / 2;
        this.cy = (sy + ey) / 2;

        this.dx = Mathf.clamp(ex - sy, 0, 1);
        this.dy = Mathf.clamp(ey - sy, 0, 1);

        this.length = sx == ex ? ey - sy : ex - sx;

        if (sx == ex) generate(sy, ey, (schema, y) -> Schematics.terrainAt(schema, sx, y, true)); // vertical
        if (sy == ey) generate(sx, ex, (schema, x) -> Schematics.terrainAt(schema, x, sy, false)); // horizontal
    }

    public void generate(int from, int to, Cons2<Schematic, Integer> cons) {
        for (int i = from + 4; i < to - 2; i += 4)
            cons.get(Schematics.doorFragment, i);

        cons.get(Schematics.doorStart, from);
        cons.get(Schematics.doorEnd, to);
    }

    public void update() { // TODO win for survivors and lose for murderers
        if (!opening || progress == length / 2 - 1) return;

        if (sx == ex) {
            slice(sx - 2, cy + progress + 1, 5, 1);
            slice(sx - 2, cy - progress, 5, 1);
        }
        if (sy == ey) {
            slice(cx + progress + 1, sy - 2, 1, 5);
            slice(cx - progress, sy - 2, 1, 5);
        }

        progress++;
    }

    public void slice(int sx, int sy, int width, int height) {
        for (int x = sx; x < sx + width; x++) {
            for (int y = sy; y < sy + height; y++) {
                var build = world.tile(x, y).build;
                if (build != null) build.kill();

                if (Mathf.chance(.8f)) Call.effect(Fx.smokeCloud, x * tilesize, y * tilesize, 0f, Color.white);
            }
        }
    }

    public void open() {
        opening = true;
        Generator.playCutscene();
    }
}
