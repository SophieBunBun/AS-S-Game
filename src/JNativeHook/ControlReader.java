package JNativeHook;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import data.GameData;

public class ControlReader implements NativeKeyListener {

    GameData gameData;

    public ControlReader(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
        gameData.setKeyPressed(nativeEvent.getKeyCode());
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
    }
}
