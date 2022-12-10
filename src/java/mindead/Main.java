package mindead;

import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Timer;
import mindead.content.Schematics;
import mindead.types.Engine;
import mindustry.mod.Plugin;
import useful.Bundle;

public class Main extends Plugin {

    public static Seq<Engine> engines = new Seq<>();

    @Override
    public void init() {
        Bundle.load(Main.class);

        Schematics.load();
        Generator.load();

        Timer.schedule(() -> {
            engines.each(Engine::update);
        }, 0f, 0.2f);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {}

    @Override
    public void registerServerCommands(CommandHandler handler) {}
}
