#include "model_layer/model.h"

#include <gtest/gtest.h>
#include <loki/ScopeGuard.h>

#include <functional>
#include <memory>
#include <ostream>

#include "core/concepts.h"
#include "data_access_layer/fake_store.h"
#include "data_access_layer/postgresql_queries.h"  // BAD!! too low level
#include "data_access_layer/sqlite_queries.h"
#include "heart/config.h"
#include "model_layer/entities.h"
#include "view/renders.h"

namespace {
using entities::TaskEntities;
using Loki::MakeObjGuard;
using Loki::ScopeGuard;
using models::Model;
using pq_dal::PostgreSQLDataBase;
using renders::render_task_store;
using std::cout;
using std::ref;

TEST(AppCore, Create) {
    // make_shared получает по копии - проблема с некопируемыми объектами
    auto pool = concepts::db_manager_concept_t(sqlite_queries::SQLiteDataBase());
    {
        Model app_ptr(pool);

        // добавляем записи
        auto data = fake_store::get_all();

        // как добавить пачкой?
        // std::for_each(data.begin(), data.end(), bind(&Model::_append,
        // ref(*app_ptr), _1));

        // renders::render_task_store(cout, *(app_ptr.get()));
    }

    {
        Model app_ptr(pool);
        auto _ = MakeObjGuard(app_ptr, &Model::dropStore);

        // renders::render_task_store(cout, *(app_ptr.get()));
    }

    auto q = pool.getTaskTableQuery();
    // EXPECT_THROW(q->draw(cout), pqxx::undefined_table);
}

TEST(AppCore, UpdatePriority) {
    auto db = concepts::db_manager_concept_t(sqlite_queries::SQLiteDataBase());

    {
        Model app_ptr(db);
        auto _ = MakeObjGuard(app_ptr, &Model::dropStore);

        // добавляем записи
        auto data = fake_store::get_all();
        // adobe::for_each(data, bind(&Model::_append, ref(*app_ptr), _1));
        renders::render_task_store(cout, app_ptr);

        // Change priority
        // data[0]->set_priority(10);
        // app_ptr->update(data[0]);
        renders::render_task_store(cout, app_ptr);
    }

    auto q = db.getTaskTableQuery();
    // EXPECT_THROW(q->draw(cout), pqxx::undefined_table);
}

TEST(AppCore, SelectionByPriority) {}

}  // namespace
