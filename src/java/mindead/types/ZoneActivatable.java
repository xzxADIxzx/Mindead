package mindead.types;

import arc.audio.Sound;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.struct.Seq;
import arc.util.Interval;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

public abstract class ZoneActivatable implements Position {

    protected static final Seq<Runnable> sounds = new Seq<>();
    protected Interval timer = new Interval();

    protected float x, y, radius, step;
    protected float progress, activation;

    public ZoneActivatable(int x, int y, float radius) {
        this.x = x * tilesize + 4f;
        this.y = y * tilesize + 4f;

        this.radius = radius;
        this.step = 800f / radius;
    }

    public void update() {
        activation = 0f;
        for (Player player : Groups.player)
            if (within(player, radius) && accept(player)) activation += .01f; // TODO different activation speed for different unit types

        progress = Mathf.clamp(progress + activation);
        float visualProgress = activation == 0f ? 1f : progress;

        Color color = Color.valueOf("38d667").lerp(Pal.health, 1f - visualProgress);
        for (int deg = 0; deg < visualProgress * 360f; deg += step)
            Call.effect(Fx.mineSmall, x + Mathf.cosDeg(deg) * radius, y + Mathf.sinDeg(deg) * radius, 0f, color);

        playSound();
    }

    // region getters

    public boolean inactivated() {
        return progress < 1f;
    }

    public abstract boolean accept(Player player);

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    // endregion
    // region sounds

    public static void soundAt(Sound sound, float x, float y, float volume, float pitch) {
        sounds.add(() -> Call.soundAt(sound, x, y, volume, pitch));
    }

    public static void playSounds() {
        for (int i = 0; i < sounds.size; i++) Time.run(i, sounds.pop());
    }

    public abstract void playSound();

    // endregion
}
