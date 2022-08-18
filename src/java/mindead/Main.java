package mindead;

import arc.Events;
import arc.util.CommandHandler;
import arc.util.Timer;
import mindead.types.Engine;
import mindustry.game.EventType.*;
import mindustry.mod.Plugin;

public class Main extends Plugin {

    private Engine engine;

    @Override
    public void init() {
        Generator.load();

        Timer.schedule(() -> {
            if (engine != null) engine.update();
        }, 0f, 0.2f);

        Events.on(PlayerJoin.class, event -> {
            engine = new Engine(event.player.tileX(), event.player.tileY() + 10);
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {}

    @Override
    public void registerServerCommands(CommandHandler handler) {}
}
