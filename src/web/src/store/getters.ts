import { GetterTree } from 'vuex'
import { TodoState } from './store-types'
import { TodoItem } from '@/types'

export const getters: GetterTree<TodoState, TodoState> = {
  todolist(state): TodoItem[] {
    return state.todolist
  },
  totalCount(state): number {
    return state.totalCount
  },
  totalPage(state): number {
    if (state.totalCount === 0) {
      return 0
    }
    return Math.ceil(state.totalCount / state.rowsPerPage)
  },
}
