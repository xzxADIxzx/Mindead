package mindead;

import arc.util.CommandHandler;
import mindustry.mod.Plugin;

public class Main extends Plugin {

    @Override
    public void init() {
        Generator.load();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {}

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register("init", "TEST.", args -> {
            // TODO: some test stuffs
        });
    }
}
