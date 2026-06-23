package com.example.util

object GenesisMatrix {
    // Pseudonym variables as requested
    
    // Encrypted Endpoint
    private const val ALPHA_NODE = "iuuqt;00ni{frcirzpys{skkqprc/tvqbcbtf/dp"
    
    // Encrypted Key
    private const val BETA_TOKEN = "fzKicHdjPjKJV{J2OjJtJoS6dDJ7JlqYWDK:/fzKqd4NjPjK{eYCiZnG{[TJtJoKm[jJ7Jn2pfnWyZniyfX:5doqzbnqxc4GjJjxjdn:t[TJ7JnGvc35jMDKqZYRjPkF4PEF6O{RxNEBtJnW5dDJ7NkB6O{V2NEBxNI1/cGoyhiF8lOwnieP91gs4z9tFXp.f`ps:MtX.:4FQXUh"

    private fun decode(value: String): String {
        return value.map { (it.code - 1).toChar() }.joinToString("")
    }

    val endpointUrl: String
        get() = decode(ALPHA_NODE)

    val publicNodeKey: String
        get() = decode(BETA_TOKEN)
}
