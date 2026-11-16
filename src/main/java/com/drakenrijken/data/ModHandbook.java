package com.drakenrijken.data;

import com.drakenrijken.DrakenRijken;
import com.drakenrijken.core.Handbook;
import java.io.IOException;
import java.util.List;

/** Houdt de geladen handleiding vast (DR-706). */
public final class ModHandbook {
	private static Handbook book;

	private ModHandbook() {}

	public static Handbook book() {
		if (book == null) {
			try (var in = ModHandbook.class.getResourceAsStream("/data/drakenrijken/handbook/handleiding.json")) {
				book = in == null ? new Handbook(List.of()) : HandbookLoader.load(new String(in.readAllBytes()), DrakenRijken.LOGGER::warn);
			} catch (IOException e) {
				DrakenRijken.LOGGER.warn("Handleiding niet te laden: {}", e.getMessage());
				book = new Handbook(List.of());
			}
		}
		return book;
	}
}
