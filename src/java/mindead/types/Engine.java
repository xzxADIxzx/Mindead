package mindead.types;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Time;
import mindead.content.Schematics;
import mindustry.content.Fx;
import mindustry.game.Schematic;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

public class Engine implements Position {

    public static final float radius = 80f;
    public static final Schematic schematic = Schematics.engine;
    public static final Seq<Runnable> sounds = new Seq<>();

    private float x, y;
    private float progress;

    private Interval timer = new Interval();

    public Engine(int x, int y) {
        this.x = x * tilesize + 4f;
        this.y = y * tilesize + 4f;

        Schematics.at(schematic, x, y, state.rules.defaultTeam);
    }

    public void update() {
        float repairSpeed = 0f;
        for (Player player : Groups.player)
            if (within(player, radius)) repairSpeed += .01f; // TODO: different repair speed for different unit types

        progress = Mathf.clamp(progress + repairSpeed);
        float visualProgress = repairSpeed == 0f ? 1f : progress;

        Color color = Color.valueOf("38d667").lerp(Pal.health, 1 - visualProgress);
        for (int deg = 0; deg < visualProgress * 360f; deg += 10)
            Call.effect(Fx.mineSmall, x + Mathf.cosDeg(deg) * radius, y + Mathf.sinDeg(deg) * radius, 0f, color);

        float pitch = progress == 1f ? 1f : .6f; // sound length depends on pitch and idk why
        if (timer.get(4.5f * 60f / pitch)) soundAt(x, y, pitch);
    }

    public boolean inactivated() {
        return progress < 1f;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public void soundAt(float x, float y, float pitch) {
        sounds.add(() -> Call.soundAt(Sounds.combustion, x, y, pitch / 1.2f, pitch));
    }

    public static void playSounds() {
        for (int i = 0; i < sounds.size; i++)
            Time.run(i, sounds.pop());
    }
}
