package mindead.types;

import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import useful.Bundle;
import useful.Bundle.LocaleProvider;

import java.util.Locale;

public class Human implements LocaleProvider {

    public Player player;
    public Locale locale;

    public UnitType type;
    public Bonus bonus;

    private Tile tileOn;

    public Human(Player player, UnitType type) {
        this.player = player;
        this.locale = Bundle.locale(player);
        this.type = type;
    }

    public void update() {
        if (player.tileOn() == this.tileOn) return;
        this.tileOn = player.tileOn();

        if (tileOn.floor() == Blocks.iceSnow) tileOn.setFloorNet(Blocks.ice);
        if (tileOn.floor() == Blocks.snow) tileOn.setFloorNet(Blocks.iceSnow);
    }

    // region getters

    public Team team() {
        return player.team();
    }

    @Override
    public Locale locale() {
        return locale;
    }

    // endregion
}
