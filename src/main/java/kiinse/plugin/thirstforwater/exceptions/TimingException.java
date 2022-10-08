package kiinse.plugin.thirstforwater.exceptions;

import kiinse.plugins.darkwaterapi.api.exceptions.DarkWaterBaseException;

@SuppressWarnings("unused")
public class TimingException extends DarkWaterBaseException {

    public TimingException() {super();}

    public TimingException(String message) {super(message);}

    public TimingException(Throwable cause) {super(cause);}

    public TimingException(String message, Throwable cause) {super(message, cause);}
}