package kiinse.plugin.thirstforwater.exceptions;

import kiinse.plugins.darkwaterapi.api.exceptions.DarkWaterBaseException;

@SuppressWarnings("unused")
public class ThirstException extends DarkWaterBaseException {

    public ThirstException() {super();}

    public ThirstException(String message) {super(message);}

    public ThirstException(Throwable cause) {super(cause);}

    public ThirstException(String message, Throwable cause) {super(message, cause);}
}

