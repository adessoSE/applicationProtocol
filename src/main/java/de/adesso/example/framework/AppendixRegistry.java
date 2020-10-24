package de.adesso.example.framework;

import java.util.UUID;

import lombok.NonNull;

public interface AppendixRegistry {

	/**
	 * Find an id of an appendix providing the required type.
	 *
	 * @param parameterType the required type
	 * @return the UUID of the appendix providing this type
	 */
	UUID lookUp(@NonNull Class<?> parameterType);
}
