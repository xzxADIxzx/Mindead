package mindead;

import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.game.Rules.TeamRule;
import mindustry.gen.Call;

import static arc.Core.*;
import static mindustry.Vars.*;

public class Generator {

    public static final Rules rules = new Rules();

    public static void load() {
        rules.waves = false;
        rules.canGameOver = false;
        rules.modeName = "Mindead";

        for (Team team : Team.all) {
            TeamRule rule = rules.teams.get(team);
            rule.cheat = true; // for effects from blocks
            rule.unitDamageMultiplier = rule.blockDamageMultiplier = 0f;
        }
    }

    public static void generate() {
        app.post(() -> {
            state.rules = rules;
            Call.setRules(rules);
        });
    }
}
