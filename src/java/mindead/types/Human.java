package mindead.types;

import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Player;
import mindustry.type.UnitType;
import useful.Bundle;
import useful.Bundle.LocaleProvider;

import java.util.Locale;

public class Human implements LocaleProvider {

    public Player player;
    public Locale locale;

    public UnitType type;
    public Bonus bonus;

    public Human(Player player, UnitType type) {
        this.player = player;
        this.locale = Bundle.locale(player);

        this.type = type;
        this.player.team(type == UnitTypes.crawler ? Team.sharded : Team.crux); // TODO replace with Units.team(UnitType type)
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
