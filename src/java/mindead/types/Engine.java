package mindead.types;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.util.Interval;
import mindustry.content.Fx;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

public class Engine implements Position {

    public static final float radius = 80f;

    private float x, y;
    private float progress;

    private Interval timer = new Interval();

    public Engine(int x, int y) { // TODO: add ShortSchematics and spawn engine scheme
        this.x = x * tilesize + 4f;
        this.y = y * tilesize + 4f;
    }

    public void update() {
        float repairSpeed = 0f;
        for (Player player : Groups.player)
            if (within(player, radius)) repairSpeed += 0.01; // TODO: different repair speed for different unit types

        progress = Mathf.clamp(progress + repairSpeed);
        float visualProgress = repairSpeed == 0f ? 1f : progress;

        Color color = Color.valueOf("38d667").lerp(Pal.health, 1 - visualProgress);
        for (int deg = 0; deg < visualProgress * 360f; deg += 10)
            Call.effect(Fx.mineSmall, x + Mathf.cosDeg(deg) * radius, y + Mathf.sinDeg(deg) * radius, 0f, color);

        float volume = progress == 1f ? 1f : .5f;
        if (timer.get(4.55f * 60f / volume)) // sound length depends on pitch and idk why
            Call.soundAt(Sounds.combustion, x, y, volume, volume); // volume and pitch are the same
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
