package com.byeboo.app.core.navigation

object DeepLink {
    private const val SCHEME = "myapp"

    object Quest {
        const val QUEST_OPEN = "$SCHEME://quest"
        const val QUEST_REACTION = "$SCHEME://common-quests"
    }
}
