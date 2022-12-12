package mindead.types;

import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Sounds;

import static mindustry.Vars.*;

public class Collectible extends ZoneActivatable {

    public static final float radius = 20f;
    public final Bonus bonus;

    public Collectible(int x, int y, Bonus bonus) {
        super(x, y, radius);
        this.bonus = bonus;

        world.tile(x, y).setBlock(bonus.block, Team.sharded);
    }

    @Override
    public boolean accept(Player player) {
        return player.team() == Team.sharded; // TODO && data.bonus == null
    }

    @Override
    public void playSound() {
        float pitch = activation == 0 ? .2f : .9f;
        if (timer.get(60f / pitch)) soundAt(Sounds.chatMessage, x, y, 120f, pitch);

        if (activation == 0f) progress = 0f;
    }
}
