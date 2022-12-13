package mindead;

import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.game.Team;
import mindustry.gen.Player;

import static mindustry.Vars.*;

public class Logic {

    public static final int spawnRadius = 5;

    public static Point2 survivalSpawn = new Point2();
    public static Point2 murdererSpawn = new Point2();

    public static Seq<Player> survivals = new Seq<>();
    public static Seq<Player> murderers = new Seq<>();

    public static boolean isPlaying;

    public static Team assign(Player player) { // TODO derelict and spawn within severals seconds
        Team team = survivals.size / murderers.size >= 1f / 3f ? Team.crux : Team.sharded;

        if (team == Team.sharded) survivals.add(player);
        if (team == Team.crux) murderers.add(player);

        return team;
    }

    public static void setSurvivalSpawn(int x, int y) {
        survivalSpawn.set(x, y);
        Geometry.circle(x, y, spawnRadius * tilesize, (cx, cy) -> world.tile(x, y).setAir());
    }

    public static void setMurdererSpawn(int x, int y) {
        murdererSpawn.set(x, y);
        Geometry.circle(x, y, spawnRadius * tilesize, (cx, cy) -> world.tile(x, y).setAir());
    }
}
