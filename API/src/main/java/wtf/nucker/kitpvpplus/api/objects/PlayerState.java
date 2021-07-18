package wtf.nucker.kitpvpplus.api.objects;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 14/07/2021
 */
public enum PlayerState {

    SPAWN,
    ARENA,
    PROTECTED;

    public static PlayerState fromInstanceState(wtf.nucker.kitpvpplus.player.PlayerState state) {
        switch (state) {
            case SPAWN:
                return PlayerState.SPAWN;
            case ARENA:
                return PlayerState.ARENA;
            case PROTECTED:
                return PlayerState.PROTECTED;
        }

        return null;
    }

    public static wtf.nucker.kitpvpplus.player.PlayerState toInstanceState(PlayerState state) {
        switch (state) {
            case SPAWN:
                return wtf.nucker.kitpvpplus.player.PlayerState.SPAWN;
            case ARENA:
                return wtf.nucker.kitpvpplus.player.PlayerState.ARENA;
            case PROTECTED:
                return wtf.nucker.kitpvpplus.player.PlayerState.PROTECTED;
        }
        return null;
    }
}
