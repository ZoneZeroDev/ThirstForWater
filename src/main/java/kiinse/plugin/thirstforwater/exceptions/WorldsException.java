package kiinse.plugin.thirstforwater.exceptions;

import kiinse.plugins.darkwaterapi.api.exceptions.DarkWaterBaseException;

@SuppressWarnings("unused")
public class WorldsException extends DarkWaterBaseException {

    public WorldsException() {super();}

    public WorldsException(String message) {super(message);}

    public WorldsException(Throwable cause) {super(cause);}

    public WorldsException(String message, Throwable cause) {super(message, cause);}
}
