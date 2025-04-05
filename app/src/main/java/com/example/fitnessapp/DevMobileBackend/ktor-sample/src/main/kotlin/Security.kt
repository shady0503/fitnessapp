package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.Utils.verifyToken
import com.kborowy.authprovider.firebase.firebase
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import java.io.File

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    /*al jwtAudience = "jwt-audience"
    val jwtDomain = "https://jwt-provider-domain/"
    val jwtRealm = "ktor sample app"
    val jwtSecret = "secret"
    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }*/

    install(Authentication) {
        bearer("firebase-auth") {
            authenticate { tokenCredential ->
                val decodedToken = verifyToken(tokenCredential.token)
                if (decodedToken != null) {
                    UserIdPrincipal(decodedToken.uid) // User authenticated, using Firebase UID
                } else {
                    null // Token verification failed
                }
            }
        }
    }

    /*install(Authentication) {
        firebase {
            adminFile = File("path/to/admin/file.json")
            realm = "My Server"
            validate { token ->
                MyAuthenticatedUser(id = token.uid)
            }
        }
    }*/
    routing {
        authenticate("firebase-auth") {
            get("/secure") {
                val user = call.principal<UserIdPrincipal>()
                call.respondText("Authenticated as ${user?.name}")
            }
        }
    }
    /*routing {
        authenticate("auth-oauth-google") {
            get("login") {
                call.respondRedirect("/callback")
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.authentication.principal()
                call.sessions.set(UserSession(principal?.accessToken.toString()))
                call.respondRedirect("/hello")
            }
        }
    }*/
}

class UserSession(accessToken: String)
