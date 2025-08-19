package abiy.ChannelAlpha.adaptor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardTracker implements KeyListener {
    private final Map<Integer, Boolean> keyPressedMap = new HashMap<>(); // Map to track key press states

    public KeyboardTracker() {
        // Initialize common keys to false
        keyPressedMap.put(KeyEvent.VK_UP, false); // Initialize up arrow key state
        keyPressedMap.put(KeyEvent.VK_DOWN, false); // Initialize down arrow key state
        keyPressedMap.put(KeyEvent.VK_LEFT, false); // Initialize left arrow key state
        keyPressedMap.put(KeyEvent.VK_RIGHT, false); // Initialize right arrow key state
        // Add any other keys you want to track
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressedMap.put(e.getKeyCode(), true); // Mark key as pressed when key press event occurs
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressedMap.put(e.getKeyCode(), false); // Mark key as released when key release event occurs
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used for key state tracking
    }

    public boolean isUAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_UP, false); // Check if up arrow key is currently pressed
    }

    public boolean isLAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_LEFT, false); // Check if left arrow key is currently pressed
    }

    public boolean isRAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_RIGHT, false); // Check if right arrow key is currently pressed
    }

    public boolean isDAdown() {
        return keyPressedMap.getOrDefault(KeyEvent.VK_DOWN, false); // Check if down arrow key is currently pressed
    }
    
    // Generic check for any key
    public boolean isKeyDown(int keyCode) {
        return keyPressedMap.getOrDefault(keyCode, false); // Check if specified key code is currently pressed
    }
}