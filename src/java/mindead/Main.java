package mindead;

import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Timer;
import mindead.content.*;
import mindead.types.*;
import mindustry.core.GameState.State;
import mindustry.mod.Plugin;
import useful.Bundle;

import static mindustry.Vars.*;

public class Main extends Plugin {

    public static Seq<Engine> engines = new Seq<>();
    public static Seq<Collectible> collectibles = new Seq<>();
    public static Door door;

    @Override
    public void init() {
        Bundle.load(Main.class);

        Bonuses.load();
        Schematics.load();
        Generator.load();

        Timer.schedule(() -> {
            engines.each(Engine::update);
            collectibles.each(Collectible::update);
            door.update();

            Engine.playSounds();

            if (door.opening || engines.contains(Engine::inactivated)) return;
            door.open();
        }, 0f, 0.2f);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {}

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register("host", "Initialize new game.", args -> {
            if (state.is(State.playing)) {
                Log.err("Already hosting. Type 'stop' will not help you.");
                return;
            }

            Generator.play();

            logic.play();
            netServer.openServer();
        });
    }
}
