# Database Migration Rules

We use **Flyway** for database migrations. Treat your database schema like code: it must be versioned, reviewed, and immutable once deployed.

## Naming Convention
Files must be named exactly like this:
`V<Version>__<Description>.sql`

*   `V` - Prefix (Capital V).
*   `<Version>` - Version number. Use sequential numbers (1, 2, 3) or timestamps (202310251430). We will use sequential numbers for this project.
*   `__` - **Double underscore** separator (CRITICAL).
*   `<Description>` - Words separated by underscores (e.g., `create_users_table`).
*   `.sql` - Suffix.

**Examples:**
*   `V1__init.sql`
*   `V2__create_users_table.sql`
*   `V3__add_status_to_orders.sql`

## The Golden Rules
1.  **NEVER modify an existing migration script** once it has been committed and pushed to the main branch. If you made a mistake, write a new migration (e.g., `V4__fix_user_column.sql`) to correct it. Modifying an existing script will cause Flyway checksum validation to fail on startup, crashing the app.
2.  **No Hibernate Auto-DDL**: `spring.jpa.hibernate.ddl-auto` must ALWAYS be set to `validate` in production and local environments. Hibernate should only verify the schema matches the entities, never alter it.
3.  **Idempotency**: Where possible, write safe SQL (e.g., `CREATE TABLE IF NOT EXISTS`, `DROP TABLE IF EXISTS`).