package com.drakenrijken.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RealmReportTest {
	@Test
	void toontNiveauMachtEnDrempel() {
		assertEquals("Rijk niveau 0 van 5, macht 0, volgend niveau bij 10 macht (nog 10).", RealmReport.describe(0));
		assertEquals("Rijk niveau 1 van 5, macht 12, volgend niveau bij 50 macht (nog 38).", RealmReport.describe(12));
		assertEquals("Rijk niveau 5 van 5, macht 5000, hoogste niveau bereikt.", RealmReport.describe(5000));
	}
}
