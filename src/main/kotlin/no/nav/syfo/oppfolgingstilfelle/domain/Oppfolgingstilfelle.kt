package no.nav.syfo.oppfolgingstilfelle.domain

import no.nav.syfo.dialogmotekandidat.domain.*
import no.nav.syfo.domain.PersonIdentNumber
import no.nav.syfo.domain.Virksomhetsnummer
import no.nav.syfo.util.isAfterOrEqual
import java.time.LocalDate
import java.time.OffsetDateTime

data class Oppfolgingstilfelle(
    val personIdent: PersonIdentNumber,
    val tilfelleStart: LocalDate,
    val tilfelleEnd: LocalDate,
    val arbeidstakerAtTilfelleEnd: Boolean,
    val virksomhetsnummerList: List<Virksomhetsnummer>,
)

fun Oppfolgingstilfelle.isDialogmotekandidat(
    dialogmotekandidatEndringList: List<DialogmotekandidatEndring>,
    latestDialogmoteFerdigstilt: OffsetDateTime?,
) = this.isDialogmotekandidat() &&
    (latestDialogmoteFerdigstilt == null || latestDialogmoteFerdigstilt.toLocalDate().isBefore(this.tilfelleStart)) &&
    dialogmotekandidatEndringList.isLatestStoppunktKandidatMissingOrNotInOppfolgingstilfelle(
        tilfelleStart = this.tilfelleStart,
    )

fun Oppfolgingstilfelle.isDialogmotekandidat(): Boolean {
    val dialogmotekandidatStoppunktPlanlagt = DialogmotekandidatStoppunkt.stoppunktPlanlagtDato(tilfelleStart, tilfelleEnd)
    return arbeidstakerAtTilfelleEnd && tilfelleEnd.isAfterOrEqual(dialogmotekandidatStoppunktPlanlagt)
}
