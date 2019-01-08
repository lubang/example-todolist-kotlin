import { MutationTree } from 'vuex'
import { TodoState, TodoMutations } from './store-types'

export const mutations: MutationTree<TodoState> = {
  [TodoMutations.FETCH_TODOLIST](state) {
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
    state.error = `서버와의 연결에 장애가 발생하였습니다. (${message})`
  },
  [TodoMutations.INIT_WARN](state) {
    state.warn = null
  },
  [TodoMutations.WARN](state, { warn }) {
    state.warn = warn
  },
}
