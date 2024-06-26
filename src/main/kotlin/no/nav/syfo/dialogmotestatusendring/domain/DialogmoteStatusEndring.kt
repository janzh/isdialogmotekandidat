package no.nav.syfo.dialogmotestatusendring.domain

import no.nav.syfo.dialogmote.avro.KDialogmoteStatusEndring
import no.nav.syfo.domain.PersonIdentNumber
import java.time.OffsetDateTime
import java.time.ZoneOffset

enum class DialogmoteStatusEndringType {
    INNKALT,
    AVLYST,
    FERDIGSTILT,
    NYTT_TID_STED,
    LUKKET,
}

data class DialogmoteStatusEndring private constructor(
    val personIdentNumber: PersonIdentNumber,
    val type: DialogmoteStatusEndringType,
    val createdAt: OffsetDateTime,
    val moteTidspunkt: OffsetDateTime,
    val statusTidspunkt: OffsetDateTime,
) {
    companion object {
        fun create(kafkaDialogmoteStatusEndring: KDialogmoteStatusEndring) = DialogmoteStatusEndring(
            personIdentNumber = PersonIdentNumber(kafkaDialogmoteStatusEndring.getPersonIdent()),
            type = DialogmoteStatusEndringType.valueOf(kafkaDialogmoteStatusEndring.getStatusEndringType()),
            createdAt = OffsetDateTime.now(),
            moteTidspunkt = OffsetDateTime.ofInstant(
                kafkaDialogmoteStatusEndring.getDialogmoteTidspunkt(),
                ZoneOffset.UTC,
            ),
            statusTidspunkt = OffsetDateTime.ofInstant(
                kafkaDialogmoteStatusEndring.getStatusEndringTidspunkt(),
                ZoneOffset.UTC,
            ),
        )
    }
}
