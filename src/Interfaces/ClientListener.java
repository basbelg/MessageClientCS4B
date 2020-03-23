package Interfaces;

import java.util.Observable;
import java.util.Observer;

public interface ClientListener extends Observer
{
    @Override
    void update(Observable o, Object arg);
}
