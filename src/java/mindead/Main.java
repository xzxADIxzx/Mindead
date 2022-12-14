package mindead;

import arc.Events;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.Timer;
import mindead.content.*;
import mindead.types.*;
import mindustry.core.GameState.State;
import mindustry.game.EventType.*;
import mindustry.mod.Plugin;
import useful.Bundle;
import useful.DynamicMenus;

import static mindustry.Vars.*;

public class Main extends Plugin {

    public static Seq<Engine> engines = new Seq<>();
    public static Seq<Collectible> collectibles = new Seq<>();
    public static Door door;

    @Override
    public void init() { // TODO when player leave, replace unit controller with AI
        Bundle.load(Main.class);
        DynamicMenus.load();

        Bonuses.load();
        Schematics.load();
        Generator.load();

        netServer.assigner = (player, players) -> Logic.assign(player);

        Events.on(PlayerJoin.class, event -> Logic.join(event.player));
        Events.on(PlayerLeave.class, event -> Logic.leave(event.player));

        Timer.schedule(() -> {
            engines.each(Engine::update);
            collectibles.each(Collectible::update);
            door.update();

            Logic.each(Human::update);
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

            Generator.playLobby();

            logic.play();
            netServer.openServer();
        });
    }
}
