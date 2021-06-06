package com.hungry.oauthsample.db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils

class V1__init_db: BaseJavaMigration() {
    override fun migrate(context: Context?) {
        SchemaUtils.createSchema(Schema("users"))
    }
}