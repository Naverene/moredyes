package info.kg6jay.moredyes.block;

/** Which texture in the sheet (see Textures) a block shows on each side. */
public final class Faces {

    /** By side: 0 bottom, 1 top, 2 to 5 north, south, west, east. */
    private final int[] textures;

    private Faces(int[] textures) {
        this.textures = textures;
    }

    public static Faces all(int texture) {
        return new Faces(new int[] { texture, texture, texture, texture, texture, texture });
    }

    public static Faces of(int top, int side, int bottom) {
        return new Faces(new int[] { bottom, top, side, side, side, side });
    }

    /** A different texture on the north and west sides, like the front of a crafting table. */
    public static Faces of(int top, int side, int bottom, int front) {
        return new Faces(new int[] { bottom, top, front, side, front, side });
    }

    public int get(int side) {
        return this.textures[side < 0 || side > 5 ? 2 : side];
    }

    public int top() {
        return this.textures[1];
    }

    public int side() {
        return this.textures[2];
    }

    public int bottom() {
        return this.textures[0];
    }
}
