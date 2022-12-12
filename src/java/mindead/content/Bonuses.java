package mindead.content;

import mindead.types.Bonus;
import mindustry.content.Blocks;
import mindustry.content.Items;

public class Bonuses {

    public static Bonus repairTools;

    public static void load() {
        repairTools = new Bonus() {{
            item = Items.phaseFabric;
            block = Blocks.shieldedWall;
        }};
    }
}
