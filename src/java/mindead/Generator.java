package mindead;

import mindead.types.Engine;
import mindustry.content.Blocks;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.game.Rules.TeamRule;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.maps.Map;
import mindustry.world.Block;

import static arc.Core.*;
import static mindead.Main.*;
import static mindustry.Vars.*;

public class Generator {

    public static final Block survivalSpawn = Blocks.coreShard;
    public static final Block murderSpawn = Blocks.coreCitadel;
    public static final Block generator = Blocks.combustionGenerator;
    public static final Block exitDoor = Blocks.multiPress;

    public static final Rules rules = new Rules();
    public static Map lobby, last;

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

    public static void play() {
        clear();

        last = maps.customMaps().random(last);
        world.loadMap(last);

        generate();
    }

    public static void generate() {
        Groups.build.each(build -> {
            if (build.block == generator) engines.add(new Engine(build.tileX(), build.tileY()));
        });

        app.post(() -> {
            state.rules = rules;
            Call.setRules(rules);
        });
    }

    public static void clear() {
        engines.clear();
    }
}
