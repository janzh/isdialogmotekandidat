package no.nav.syfo.testhelper.generator

import no.nav.syfo.dialogmotekandidat.DIALOGMOTEKANDIDAT_STOPPUNKT_DURATION_DAYS
import no.nav.syfo.dialogmotekandidat.DialogmotekandidatStoppunkt
import no.nav.syfo.domain.PersonIdentNumber
import java.time.LocalDate

fun generateDialogmotekandidatStoppunktPlanlagt(
    arbeidstakerPersonIdent: PersonIdentNumber,
    planlagt: LocalDate,
): DialogmotekandidatStoppunkt = DialogmotekandidatStoppunkt.planlagt(
    arbeidstakerPersonIdent = arbeidstakerPersonIdent,
    tilfelleStart = planlagt.minusDays(DIALOGMOTEKANDIDAT_STOPPUNKT_DURATION_DAYS),
)
