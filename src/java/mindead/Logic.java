package mindead;

import arc.func.Cons;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindead.types.Human;
import mindustry.content.UnitTypes;
import mindustry.game.Team;
import mindustry.gen.Player;
import useful.DynamicMenus;

import static mindustry.Vars.*;

public class Logic {

    public static final int spawnRadius = 5;

    public static Vec2 survivalSpawn = new Vec2();
    public static Vec2 murdererSpawn = new Vec2();

    public static Seq<Human> survivals = new Seq<>();
    public static Seq<Human> murderers = new Seq<>();

    public static boolean isPlaying;

    public static Team assign(Player player) {
        return murderers.isEmpty() || survivals.size / murderers.size >= 3 ? Team.crux : Team.sharded;
    }

    public static void join(Player player) {
        var found = Human.find(player.uuid());
        if (found != null) {
            found.setPlayer(player);
            return;
        }

        DynamicMenus.menu(player, "@join.name", "@join.text", new String[][] {{"Crawler"}}, option -> {
            Human human = new Human(player, UnitTypes.crawler);
            spawn(human);

            if (player.team() == Team.sharded) survivals.add(human);
            if (player.team() == Team.crux) murderers.add(human);
        });
    }

    public static void leave(Player player) {}

    // region humans

    public static Seq<Human> all() {
        return survivals.copy().add(murderers);
    }

    public static void each(Cons<Human> cons) {
        survivals.each(cons);
        murderers.each(cons);
    }

    // endregion
    // region spawn

    public static void spawn(Human human) {
        if (isPlaying) {
            // TODO spawn within severals seconds in selected position
        } else {
            Vec2 spawn = human.team() == Team.sharded ? Logic.survivalSpawn : Logic.murdererSpawn;
            spawn(human, spawn);
        }
    }

    public static void spawn(Human human, Vec2 spawn) {
        spawn = new Vec2().rnd(Logic.spawnRadius * tilesize).add(spawn);
        human.setUnit(human.type.spawn(human.team(), spawn));
    }

    public static void setSurvivalSpawn(int x, int y) {
        survivalSpawn.set(x, y).scl(tilesize);
        Geometry.circle(x, y, spawnRadius * tilesize, (cx, cy) -> world.tile(x, y).setAir());
    }

    public static void setMurdererSpawn(int x, int y) {
        murdererSpawn.set(x, y).scl(tilesize);
        Geometry.circle(x, y, spawnRadius * tilesize, (cx, cy) -> world.tile(x, y).setAir());
    }

    // endregion
}
