package channelAlpha.adaptor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardTracker implements KeyListener {
    private final Map<Integer, Boolean> keyPressedMap = new HashMap<>();

    public KeyboardTracker() {
        // Initialize common keys to false
        keyPressedMap.put(KeyEvent.VK_UP, false);
        keyPressedMap.put(KeyEvent.VK_DOWN, false);
        keyPressedMap.put(KeyEvent.VK_LEFT, false);
        keyPressedMap.put(KeyEvent.VK_RIGHT, false);
        // Add any other keys you want to track
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedMap.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedMap.put(e.getKeyCode(), false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used for key state tracking
    }

    public boolean isUAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_UP, false);
    }

    public boolean isLAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_LEFT, false);
    }

    public boolean isRAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_RIGHT, false);
    }

    public boolean isDAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_DOWN, false);
    }
    
    // Generic check for any key
    public boolean isKeyDown(int keyCode) {
        return keyPressedMap.getOrDefault(keyCode, false);
    }
}