package no.nav.syfo.application

import io.ktor.server.application.*
import no.nav.syfo.application.database.DatabaseEnvironment
import no.nav.syfo.application.kafka.KafkaEnvironment
import no.nav.syfo.client.ClientEnvironment
import no.nav.syfo.client.ClientsEnvironment
import no.nav.syfo.client.azuread.AzureEnvironment

const val NAIS_DATABASE_ENV_PREFIX = "NAIS_DATABASE_ISDIALOGMOTEKANDIDAT_ISDIALOGMOTEKANDIDAT_DB"

data class Environment(
    val database: DatabaseEnvironment = DatabaseEnvironment(
        host = getEnvVar("${NAIS_DATABASE_ENV_PREFIX}_HOST"),
        port = getEnvVar("${NAIS_DATABASE_ENV_PREFIX}_PORT"),
        name = getEnvVar("${NAIS_DATABASE_ENV_PREFIX}_DATABASE"),
        username = getEnvVar("${NAIS_DATABASE_ENV_PREFIX}_USERNAME"),
        password = getEnvVar("${NAIS_DATABASE_ENV_PREFIX}_PASSWORD"),
    ),
    val azure: AzureEnvironment = AzureEnvironment(
        appClientId = getEnvVar("AZURE_APP_CLIENT_ID"),
        appClientSecret = getEnvVar("AZURE_APP_CLIENT_SECRET"),
        appWellKnownUrl = getEnvVar("AZURE_APP_WELL_KNOWN_URL"),
        openidConfigTokenEndpoint = getEnvVar("AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"),
    ),

    val electorPath: String = getEnvVar("ELECTOR_PATH"),

    val kafka: KafkaEnvironment = KafkaEnvironment(
        aivenBootstrapServers = getEnvVar("KAFKA_BROKERS"),
        aivenCredstorePassword = getEnvVar("KAFKA_CREDSTORE_PASSWORD"),
        aivenKeystoreLocation = getEnvVar("KAFKA_KEYSTORE_PATH"),
        aivenSecurityProtocol = "SSL",
        aivenTruststoreLocation = getEnvVar("KAFKA_TRUSTSTORE_PATH"),
        aivenSchemaRegistryUrl = getEnvVar("KAFKA_SCHEMA_REGISTRY"),
        aivenRegistryUser = getEnvVar("KAFKA_SCHEMA_REGISTRY_USER"),
        aivenRegistryPassword = getEnvVar("KAFKA_SCHEMA_REGISTRY_PASSWORD"),
    ),
    val clients: ClientsEnvironment = ClientsEnvironment(
        syfotilgangskontroll = ClientEnvironment(
            baseUrl = getEnvVar("SYFOTILGANGSKONTROLL_URL"),
            clientId = getEnvVar("SYFOTILGANGSKONTROLL_CLIENT_ID"),
        ),
        oppfolgingstilfelle = ClientEnvironment(
            baseUrl = getEnvVar("ISOPPFOLGINGSTILFELLE_URL"),
            clientId = getEnvVar("ISOPPFOLGINGSTILFELLE_CLIENT_ID"),
        ),
        pdl = ClientEnvironment(
            baseUrl = getEnvVar("PDL_URL"),
            clientId = getEnvVar("PDL_CLIENT_ID"),
        ),
    ),
    val toggleKafkaConsumerIdenthendelseEnabled: Boolean = getEnvVar("TOGGLE_KAFKA_IDENTHENDELSE_CONSUMER_ENABLED").toBoolean(),
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")

val Application.envKind get() = environment.config.property("ktor.environment").getString()

fun Application.isDev(block: () -> Unit) {
    if (envKind == "dev") block()
}

fun Application.isProd(block: () -> Unit) {
    if (envKind == "production") block()
}
