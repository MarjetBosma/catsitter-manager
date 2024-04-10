package nl.novi.catsittermanager.helpers;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ControllerHelper {
    public static String checkForBindingResult(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField());
            sb.append(" : ");
            sb.append(fe.getDefaultMessage());
            sb.append("\n");
        }
        return sb.toString();
    }
}
