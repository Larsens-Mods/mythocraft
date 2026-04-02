package de.larsensmods.mythocraft.world.level.util;

import com.google.common.hash.Hashing;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class LabyrinthUtilFunctions {

    public static byte calculateCellType(long seed, int chunkX, int chunkZ){
        byte result = 0;
        if(Math.floorMod(randVal(seed, chunkX, chunkZ), 6) < 4){
            result |= Connections.EAST.getFlag();
        }
        if(Math.floorMod(randVal(seed + 1, chunkX, chunkZ), 6) < 4){
            result |= Connections.SOUTH.getFlag();
        }
        if(Math.floorMod(randVal(seed, chunkX - 1, chunkZ), 6) < 4){
            result |= Connections.WEST.getFlag();
        }
        if(Math.floorMod(randVal(seed + 1, chunkX, chunkZ - 1), 6) < 4){
            result |= Connections.NORTH.getFlag();
        }
        return result;
    }

    public static boolean canBeBossTile(long seed, int chunkX, int chunkZ){
        return Math.floorMod(randVal(seed - 8, chunkX, chunkZ), 64) < 2;
    }

    public static int getTileVariant(long seed, int chunkX, int chunkZ, int variantCount){
        if(variantCount <= 0){
            throw new IllegalArgumentException("variantCount must be a positive number");
        }
        return Math.floorMod(randVal(seed + 8, chunkX, chunkZ), variantCount);
    }

    private static int randVal(long seed, int chunkX, int chunkZ) {
        return Hashing.murmur3_32_fixed().hashLong(seed + chunkX * 341873128712L + chunkZ * 132897987541L).asInt();
    }

    public static Shape getShape(byte cellType){
        return switch (cellType) {
            case 0 -> Shape.EMPTY;
            case 1, 2, 4, 8 -> Shape.DEAD_END;
            case 3, 6, 9, 12 -> Shape.CURVE;
            case 5, 10 -> Shape.STRAIGHT;
            case 7, 11, 13, 14 -> Shape.THREE_WAY_JUNCTION;
            case 15 -> Shape.FOUR_WAY_JUNCTION;
            default -> null;
        };
    }

    public static boolean hasConnection(byte cellType, Direction direction){
        return switch (direction) {
            case NORTH -> Connections.hasConnection(cellType, Connections.NORTH);
            case EAST -> Connections.hasConnection(cellType, Connections.EAST);
            case SOUTH -> Connections.hasConnection(cellType, Connections.SOUTH);
            case WEST -> Connections.hasConnection(cellType, Connections.WEST);
            default -> false;
        };
    }

    /**
     * Layouts required for the structures:
     * ---
     * oXo
     * XXX
     * oXo
     * ---
     * oXo
     * XXX
     * ooo
     * ---
     * ooo
     * XXX
     * ooo
     * ---
     * oXo
     * oXX
     * ooo
     * ---
     * oXo
     * oXo
     * ooo
     * ---
     * ooo
     * oXo
     * ooo
     * ---
     *     EAST
     * NORTH  SOUTH
     *     WEST
     * ---
     *   +x
     * -z  +z
     *   -x
     *
     * @param shape Shape of the tile
     * @param cellType Byte of the cell type
     * @return Rotation required based on the layout
     */
    public static Rotation calcRequiredRotation(Shape shape, byte cellType){
        if(shape == Shape.EMPTY || shape == Shape.FOUR_WAY_JUNCTION){
            return Rotation.NONE;
        }else if(shape == Shape.STRAIGHT){
            if(hasConnection(cellType, Direction.NORTH)){
                return Rotation.NONE;
            }else{
                return Rotation.CLOCKWISE_90;
            }
        }else if(shape == Shape.CURVE){
            if(hasConnection(cellType, Direction.NORTH) && hasConnection(cellType, Direction.EAST)){
                return Rotation.COUNTERCLOCKWISE_90;
            }else if(hasConnection(cellType, Direction.EAST) && hasConnection(cellType, Direction.SOUTH)){
                return Rotation.NONE;
            }else if(hasConnection(cellType, Direction.SOUTH) && hasConnection(cellType, Direction.WEST)){
                return Rotation.CLOCKWISE_90;
            }else{
                return Rotation.CLOCKWISE_180;
            }
        }else if(shape == Shape.DEAD_END){
            if(hasConnection(cellType, Direction.NORTH)){
                return Rotation.COUNTERCLOCKWISE_90;
            }else if(hasConnection(cellType, Direction.EAST)){
                return Rotation.NONE;
            }else if(hasConnection(cellType, Direction.SOUTH)){
                return Rotation.CLOCKWISE_90;
            }else{
                return Rotation.CLOCKWISE_180;
            }
        }else if(shape == Shape.THREE_WAY_JUNCTION){
            if(!hasConnection(cellType, Direction.NORTH)){
                return Rotation.CLOCKWISE_90;
            }else if(!hasConnection(cellType, Direction.EAST)){
                return Rotation.CLOCKWISE_180;
            }else if(!hasConnection(cellType, Direction.SOUTH)){
                return Rotation.COUNTERCLOCKWISE_90;
            }else{
                return Rotation.NONE;
            }
        }else{
            return Rotation.NONE;
        }
    }

    enum Connections {
        SOUTH(1), EAST(2), NORTH(4), WEST(8);

        public static boolean hasConnection(byte type, Connections connection){
            return (type & connection.getFlag()) == connection.getFlag();
        }

        private final byte flag;

        Connections(int flag){
            this.flag = (byte) flag;
        }

        public byte getFlag() {
            return flag;
        }
    }

    public enum Shape {
        EMPTY, DEAD_END, STRAIGHT, CURVE, THREE_WAY_JUNCTION, FOUR_WAY_JUNCTION
    }

}
