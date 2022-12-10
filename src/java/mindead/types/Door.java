package mindead.types;

import mindustry.world.Tile;

public class Door {

    public Door(Tile start, Tile end) {
        start.setAir(); // TODO temp
        end.setAir();
    }
}
