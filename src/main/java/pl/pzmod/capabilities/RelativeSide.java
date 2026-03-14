package pl.pzmod.capabilities;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public enum RelativeSide {
    FRONT,
    LEFT,
    RIGHT,
    BACK,
    TOP,
    BOTTOM,;

    public @NotNull Direction getDirection(@NotNull Direction facing) {
        return switch (this) {
            case FRONT -> facing;
            case BACK -> facing.getOpposite();
            case LEFT -> facing == Direction.DOWN || facing == Direction.UP ? Direction.EAST : facing.getClockWise();
            case RIGHT -> facing == Direction.DOWN || facing == Direction.UP ? Direction.WEST : facing.getCounterClockWise();
            case TOP -> switch (facing) {
                case DOWN -> Direction.NORTH;
                case UP -> Direction.SOUTH;
                default -> Direction.UP;
            };
            case BOTTOM -> switch (facing) {
                case DOWN -> Direction.SOUTH;
                case UP -> Direction.NORTH;
                default -> Direction.DOWN;
            };
        };
    }

    public static @NotNull RelativeSide fromDirections(@NotNull Direction facing, @NotNull Direction side) {
        if (side == facing) {
            return FRONT;
        } else if (side == facing.getOpposite()) {
            return BACK;
        } else if (facing == Direction.DOWN || facing == Direction.UP) {
            return switch (side) {
                case NORTH -> facing == Direction.DOWN ? TOP : BOTTOM;
                case SOUTH -> facing == Direction.DOWN ? BOTTOM : TOP;
                case WEST -> RIGHT;
                case EAST -> LEFT;
                default -> throw new IllegalStateException("Case should have been caught earlier.");
            };
        } else if (side == Direction.DOWN) {
            return BOTTOM;
        } else if (side == Direction.UP) {
            return TOP;
        } else if (side == facing.getCounterClockWise()) {
            return RIGHT;
        } else if (side == facing.getClockWise()) {
            return LEFT;
        }
        //Fall back to front, should never get here
        return FRONT;
    }
}
