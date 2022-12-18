package mindead.types;

import mindead.Logic;
import mindustry.content.Blocks;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.gen.Unit;
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
    private Unit unit;

    public Human(Player player, UnitType type) {
        setPlayer(player);
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
    // region setters

    public void setPlayer(Player player) {
        if (this.player != null) player.team(this.player.team());
        if (this.unit != null) player.unit(this.unit);

        this.player = player;
        this.locale = Bundle.locale(player);
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        this.player.unit(unit);
    }

    // endregion
    // region find

    public static Human find(Player player) {
        return Logic.all().find(human -> human.player == player);
    }

    public static Human find(String uuid) {
        return Logic.all().find(human -> human.player.uuid().equals(uuid));
    }

    // endregion
}
