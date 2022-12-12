package mindead.types;

import mindead.content.Schematics;
import mindustry.game.Schematic;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Sounds;

public class Engine extends ZoneActivatable {

    public static final float radius = 80f;
    public static final Schematic schematic = Schematics.engine;

    public Engine(int x, int y) {
        super(x, y, radius);
        Schematics.at(schematic, x, y, Team.sharded);
    }

    @Override
    public boolean accept(Player player) {
        return player.team() == Team.sharded;
    }

    @Override
    public void playSound() {
        float pitch = progress == 1f ? 1f : .6f; // sound length depends on pitch and idk why
        if (timer.get(4.5f * 60f / pitch)) soundAt(Sounds.combustion, x, y, pitch / 1.4f, pitch);
    }
}
