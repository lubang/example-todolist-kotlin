import { MutationTree } from 'vuex'
import { TodoState, TodoMutations } from './store-types'

export const mutations: MutationTree<TodoState> = {
  [TodoMutations.FETCH_TODOLIST](state) {
    state.totalCount = 0
    state.todolist = []
    state.error = null
  },
  [TodoMutations.FETCH_TODOLIST_SUCCEED](
    state,
    { offset, rowsPerPage, totalCount, results },
  ) {
    state.offset = offset
    state.rowsPerPage = rowsPerPage
    state.totalCount = totalCount
    state.todolist = results
  },
  [TodoMutations.FETCH_TODOLIST_FAILED](state, { message }) {
    state.error = message
  },
}
