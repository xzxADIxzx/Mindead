package mindead;

import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import mindead.content.Bonuses;
import mindead.types.*;
import mindustry.content.Blocks;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.game.Rules.TeamRule;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Sounds;
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
    public static final Block collectible = Blocks.sorter;
    public static final Block exitDoor = Blocks.multiPress;

    public static final Rules rules = new Rules();
    public static String cutscene;

    public static Seq<Map> available;
    public static Map lobby, last;

    public static void load() {
        rules.waves = false;
        rules.canGameOver = false;
        rules.lighting = true;
        rules.unitCap = 16;
        rules.modeName = "Mindead";

        for (Team team : Team.all) {
            TeamRule rule = rules.teams.get(team);
            rule.cheat = true; // for effects from blocks
            rule.unitDamageMultiplier = rule.blockDamageMultiplier = 0f;
        }

        cutscene = mods.getMod(Main.class).root.child("cutscene").readString();
        lobby = maps.byName("Lobby");

        if (lobby == null) throw new RuntimeException("Lobby map not found! Did you forget to download this map from GitHub?");

        available = maps.customMaps();
        available.remove(lobby);
    }

    public static void playCutscene() {
        state.rules.objectiveFlags.add("play");
        Call.setRules(state.rules);

        Time.run(50f, () -> Call.sound(Sounds.wave, 10f, .9f, -1f));
        Time.run(60f, () -> Call.sound(Sounds.wave, 10f, .9f, 1f));
    }

    public static void playLobby() {
        clear();
        world.loadMap(lobby);
        generate();
    }

    public static void play() {
        clear();

        last = available.random(last);
        world.loadMap(last);

        try {
            generate();
        } catch (Throwable ignored) {
            available.remove(last);
            Log.err("Failed to load map " + last.name(), ignored);

            if (available.any()) play();
        }
    }

    public static void generate() {
        Groups.build.each(build -> {
            if (build.block == survivalSpawn) Logic.setSurvivalSpawn(build.tileX(), build.tileY());
            if (build.block == murdererSpawn) Logic.setMurdererSpawn(build.tileX(), build.tileY());

            if (build.block == generator) engines.add(new Engine(build.tileX(), build.tileY()));
            if (build.block == exitDoor && door == null) {
                Building end = findDoorEnd(build);
                if (end == null) return;

                door = new Door(build.tileOn(), end.tileOn());
            }
        });

        world.tiles.eachTile(tile -> { // idk why, but sorter is not a building
            Building build = tile.build;
            if (build != null && build.block == collectible) collectibles.add(new Collectible(build.tileX(), build.tileY(), Bonuses.repairTools));
        });

        if (engines.isEmpty()) throw new RuntimeException("No engines found!");
        if (door == null) throw new RuntimeException("No door found!");

        world.tile(0, 0).setBlock(Blocks.worldProcessor, Team.malis);
        if (world.build(0, 0) instanceof LogicBuild build) build.updateCode(cutscene.formatted(door.centerX(), door.centerY()));

        state.rules = rules;
        app.post(() -> Call.setRules(rules));
    }

    public static void clear() {
        engines.clear();
        collectibles.clear();
        door = null;

        Logic.survivals.clear();
        Logic.murderers.clear();

        Time.clear();
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
