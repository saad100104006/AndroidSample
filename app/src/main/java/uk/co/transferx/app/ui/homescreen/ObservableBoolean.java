package uk.co.transferx.app.ui.homescreen;

import java.util.Observable;

public class ObservableBoolean extends Observable
{
    private boolean n = false;
    public ObservableBoolean( boolean val)
    {
        this.n = val;
    }
    public void setValue(boolean n)
    {
        this.n = n;
        setChanged();
        notifyObservers();
    }
    public boolean getValue()
    {
        return n;
    }
}