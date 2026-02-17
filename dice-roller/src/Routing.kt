// Set up application routing and request handling

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        get("/") { call.displayForm() }
        get("/roll") { call.handleDiceRoll() }
    }
}

private suspend fun ApplicationCall.displayForm() {
    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Dice Roller" }
        content {
            h1 { +"Dice Roller" }
            form(action = "/roll", method = FormMethod.get) {
                label {
                    htmlFor = "numDice"
                    +"Number of dice"
                }
                numberInput {
                    id = "numDice"
                    name = "num"
                    min = "1"
                    max = "10"
                    value = "3"
                    required = true
                }

                label {
                    htmlFor = "dieName"
                    +"Die to roll"
                }
                select {
                    id = "dieName"
                    name = "die"
                    required = true
                    dieOptions.forEach { value ->
                        option {
                            if (value == "d6") { selected = true }
                            +value
                        }
                    }
                }

                button { +"Roll Dice" }
            }
        }
    }
}

private suspend fun ApplicationCall.handleDiceRoll() {
    val (num, die) = getDiceDetails(request)
    val results = diceRoll(num, die)

    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Dice Results" }
        content {
            h1 { +"Dice Results" }

            p { +"You rolled ${num}${die}" }

            p {
                +"The result was: "
                strong {
                    +"${results[0]}"
                    for (result in results.drop(1)) {
                        +", $result"
                    }
                }
            }

            p {
                +"For a total of "
                strong{ +"${results.sum()}" }
            }

            p {
                +"You can "
                a(request.uri) { +"repeat this roll" }
                +", or request a "
                a("/") { +"new roll" }
                +"."
            }
        }
    }
}

private fun getDiceDetails(request: ApplicationRequest) = Pair(
    request.queryParameters["num"]?.toInt() ?: error("Number of dice not specified"),
    request.queryParameters["die"] ?: error("Die not specified")
)
