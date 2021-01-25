package pl.kdomian.workshops.exceptions;

import pl.kdomian.workshops.Entities;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(Entities entityType, Long id) {
        super("Could not find " + entityType.name() + " with id: " + id);
    }
}
