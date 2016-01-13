package com.jeprolab.assets.extend.controls.switchbutton;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.input.KeyEvent.KEY_RELEASED;

/**
 *
 * Created by jeprodev on 19/06/2014.
 */
public class JeproSwitchButtonBehavior   <C extends ButtonBase> extends BehaviorBase<C> {
    private boolean keyDown;
    private static final String PRESS_ACTION = "Pressed";
    private static final String RELEASE_ACTION = "Released";

    protected static final List<KeyBinding> BUTTON_BINDINGS = new ArrayList<>();

    static {
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KEY_PRESSED, PRESS_ACTION));
        BUTTON_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KEY_RELEASED, RELEASE_ACTION));
    }

    public JeproSwitchButtonBehavior(final C button){
        super(button, BUTTON_BINDINGS);
    }

    public JeproSwitchButtonBehavior(final C button, final List<KeyBinding> bindings){
        super(button, bindings);
    }

    @Override
    protected void callAction(String name){
        if(!getControl().isDisabled()) {
            if (PRESS_ACTION.equals(name)) {
                keyPressed();
            } else if (RELEASE_ACTION.equals(name)) {
                keyReleased();
            } else {
                super.callAction(name);
            }
        }
    }

    private void keyPressed(){
        final ButtonBase button = getControl();
        if(!button.isPressed() && !button.isArmed()){
            keyDown = true;
            button.arm();
        }
    }

    private void keyReleased(){
        final ButtonBase button = getControl();
        if(keyDown){
            keyDown = false;
            if(button.isArmed()){
                button.disarm();
                button.fire();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent evt){
        final ButtonBase button = getControl();
        super.mousePressed(evt);
        if(!button.isFocused() && button.isFocusTraversable()){
            button.requestFocus();
        }

        boolean valid = (evt.getButton() == MouseButton.PRIMARY && !evt.isMiddleButtonDown() || evt.isSecondaryButtonDown() || evt.isShiftDown() || evt.isControlDown() || evt.isAltDown() || evt.isMetaDown());
        if(!button.isArmed() && valid){
            button.arm();
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt){
        final ButtonBase button = getControl();
        if(!keyDown && button.isArmed()){
            button.fire();
            button.disarm();
        }
    }

    @Override
    public void mouseEntered(MouseEvent evt){
        final ButtonBase button = getControl();
        super.mousePressed(evt);
        if(!keyDown && button.isPressed()){
            button.arm();
        }
    }

    @Override
    public void mouseExited(MouseEvent evt){
        final ButtonBase button = getControl();
        super.mouseExited(evt);
        if(!keyDown  && button.isArmed()){
            button.disarm();
        }
    }

    @Override
    protected void focusChanged(){
        final ButtonBase button = getControl();
        if(keyDown && !button.isFocused()){
            keyDown = false;
            button.disarm();
        }
    }
}