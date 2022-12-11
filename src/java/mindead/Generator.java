package mindead;

import mindead.types.Door;
import mindead.types.Engine;
import mindustry.content.Blocks;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.game.Rules.TeamRule;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.maps.Map;
import mindustry.world.Block;
import mindustry.world.blocks.logic.LogicBlock.LogicBuild;

import static arc.Core.*;
import static mindead.Main.*;
import static mindustry.Vars.*;

public class Generator {

    public static final Block survivalSpawn = Blocks.coreShard;
    public static final Block murdererSpawn = Blocks.coreCitadel;
    public static final Block generator = Blocks.combustionGenerator;
    public static final Block exitDoor = Blocks.multiPress;

    public static final Rules rules = new Rules();
    public static String cutscene;
    public static Map lobby, last;

    public static void load() {
        rules.waves = false;
        rules.canGameOver = false;
        rules.lighting = true;
        rules.modeName = "Mindead";

        for (Team team : Team.all) {
            TeamRule rule = rules.teams.get(team);
            rule.cheat = true; // for effects from blocks
            rule.unitDamageMultiplier = rule.blockDamageMultiplier = 0f;
        }

        cutscene = mods.getMod(Main.class).root.child("cutscene").readString();
    }

    public static void playCutscene() {
        state.rules.objectiveFlags.add("play");
        Call.setRules(state.rules);
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

            if (build.block == exitDoor && door == null) {
                Building end = findDoorEnd(build);
                if (end == null) return;

                door = new Door(build.tileOn(), end.tileOn());
            }
        });

        if (engines.isEmpty()) throw new RuntimeException("No engines found!");
        if (door == null) throw new RuntimeException("No door found!");

        world.tile(0, 0).setBlock(Blocks.worldProcessor, Team.malis);
        if (world.build(0, 0) instanceof LogicBuild build) build.updateCode(cutscene.formatted(door.cx, door.cy));

        app.post(() -> {
            state.rules = rules;
            Call.setRules(rules);
        });
    }

    public static void clear() {
        engines.clear();
        door = null;
    }

    private static Building findDoorEnd(Building start) {
        for (int x = 0; x < world.width(); x++) {
            Building end = world.build(x, start.tileY());
            if (end != null && end.block == exitDoor && end != start) return end;
        }

        for (int y = 0; y < world.height(); y++) {
            Building end = world.build(start.tileX(), y);
            if (end != null && end.block == exitDoor && end != start) return end;
        }

        return null;
    }
}
