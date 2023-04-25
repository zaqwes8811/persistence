// UI:
// http://qt-project.org/doc/qt-4.8/qtablewidget.html#details
//
// Model:
//
// Store:
#include <actors_and_workers/actor_ui.h>
#include <actors_and_workers/arch.h>
#include <actors_and_workers/concurent_queues.h>
#include <data_access_layer/sqlite_queries.h>
#include <gtest/gtest.h>

#include <atomic>
#include <cassert>
#include <iostream>
#include <memory>
#include <thread>

#include "common/app_types.h"
#include "core/concepts.h"
#include "core/scopes.h"
#include "data_access_layer/postgresql_queries.h"
#include "heart/config.h"
#include "model_layer/isolation.h"
#include "model_layer/model.h"
#include "view/mainwindow.h"

namespace {
// by value, not by type
enum db_vars { DB_SQLITE, DB_POSTGRES };

concepts::db_manager_concept_t build_database(const int selector) {
    if (true) {  // selector == DB_SQLITE) {
        return concepts::db_manager_concept_t(sqlite_queries::SQLiteDataBase());
    } else {
        /*return
            concepts::db_manager_concept_t(pq_dal::PostgreSQLDataBase(
                                                    models::kConnection,
           models::kTaskTableNameRef
                                                    ));*/
    }
}
}  // namespace

TEST(Blocked, UIActorTest) {
    auto db = build_database(DB_SQLITE);
    auto f = Dispatcher::ActivateUiEventLoop(db);

    f.get();
}

// FIXME: Boost.Signal
