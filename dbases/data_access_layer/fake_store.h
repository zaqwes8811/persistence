#ifndef TEST_HELP_DATA_H
#define TEST_HELP_DATA_H

#include <vector>

#include "model_layer/entities.h"

namespace fake_store {
// Оно просто создает задачи, это не фейковое хранилище, но чем-то похоже
entities::TaskEntities get_all();

std::vector<entities::Task> get_all_values();
}  // namespace fake_store

#endif  // TEST_HELP_DATA_H
