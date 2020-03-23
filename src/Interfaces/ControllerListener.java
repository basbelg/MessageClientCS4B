package Interfaces;

import java.util.Observable;
import java.util.Observer;

public interface ControllerListener extends Observer
{
    @Override
    void update(Observable o, Object arg);
}
